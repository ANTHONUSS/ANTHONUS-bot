package fr.anthonus.Commands.SlashCommands.music;

import fr.anthonus.Commands.SlashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class DownloadCommand extends Command {
    private final String url;

    public DownloadCommand(SlashCommandInteractionEvent event, String url) {
        super(event);

        this.url = url;

        LOGs.sendLog("Commande /download initialisée", "COMMAND");
    }

    @Override
    public void run() {
        File musicFolder = new File("Music");

        if (!musicFolder.exists()) {
            if(musicFolder.mkdir()) {
                LOGs.sendLog("Dossier de musique créé", "COMMAND");
            } else {
                currentEvent.reply("## :x: Erreur lors de la création du dossier de musique.").queue();
                LOGs.sendLog("Erreur lors de la création du dossier de musique", "ERROR");
                return;
            }
        }

        currentEvent.deferReply(true).queue();

        downloadMusic();
        currentEvent.getHook().sendMessage("## :arrow_down: Téléchargement de la musique en cours...")
                .setEphemeral(true)
                .queue();
        LOGs.sendLog("Début du téléchargement de la musique...", "DOWNLOAD");
    }

    private void downloadMusic() {
        Thread downloadThread = new Thread(() -> {
            String musicName = getMusicNameFromURL();

            currentEvent.getHook().editOriginal("## :arrow_down: Téléchargement de `" + musicName + "` en cours...")
                    .queue();
            LOGs.sendLog("Musique " + musicName + " en cours de téléchargement par @" + currentEvent.getUser().getName(), "DOWNLOAD");
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "yt-dlp.exe",
                    "-x",
                    "--audio-format", "mp3",
                    "--no-playlist",
                    "-o", "Music/%(title)s.%(ext)s",
                    url
            );

            try {
                Process process = processBuilder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    LOGs.sendLog(line, "DOWNLOAD");
                }

                while ((line = errorReader.readLine()) != null) {
                    LOGs.sendLog(line, "ERROR");
                }

                int exitCode = process.waitFor();

                if (exitCode == 0) {

                    musicName = getMostRecentFile().replace(".mp3", "");

                    MusicManager.addTrackToList("Music/" + musicName + ".mp3");
                    LOGs.sendLog("Musique ajoutée à la liste", "DOWNLOAD");

                    currentEvent.getHook().deleteOriginal().queue();
                    currentEvent.getChannel().sendMessage("## ✅ La musique `" + musicName + "` à été téléchargée.")
                            .queue();
                    LOGs.sendLog("Musique téléchargée"
                            + "\nNom : " + musicName
                            + "\nUser : " + currentEvent.getUser().getName(), "DOWNLOAD");
                } else {
                    currentEvent.getHook().editOriginal("## :x: Une erreur est survenue lors du téléchargement")
                            .queue();
                    LOGs.sendLog("Erreur lors du téléchargement", "ERROR");
                }
            } catch (Exception e) {
                currentEvent.getHook().editOriginal("## :x: Une erreur est survenue lors du téléchargement : " + e.getMessage())
                        .queue();
                LOGs.sendLog("Erreur lors du téléchargement " + e.getMessage(), "ERROR");
            }
        });

        downloadThread.start();
    }

    private String getMostRecentFile() {
        Path foderPath = Paths.get("Music");

        try (Stream<Path> files = Files.list(foderPath)) {
            Optional<Path> latestFile = files
                    .filter(Files::isRegularFile)
                    .max(Comparator.comparing(this::getCreationTime));

            return latestFile.get().getFileName().toString();

        } catch (IOException e){
            LOGs.sendLog(e.getMessage(), "ERROR");
        }

        return null;
    }

    private FileTime getCreationTime(Path path) {
        try {
            return Files.readAttributes(path, BasicFileAttributes.class).creationTime();
        } catch (IOException e) {
            LOGs.sendLog(e.getMessage(), "ERROR");
            return FileTime.fromMillis(0);
        }
    }

    private String getMusicNameFromURL(){
        String musicName;

        try {
            ProcessBuilder titleProcessBuilder = new ProcessBuilder(
                    "yt-dlp.exe",
                    "--get-title",
                    "--no-playlist",
                    url
            );
            Process titleProcess = titleProcessBuilder.start();

            BufferedReader titleReader = new BufferedReader(new InputStreamReader(titleProcess.getInputStream()));
            musicName = titleReader.readLine();
            titleProcess.waitFor();

            if (musicName == null || musicName.isEmpty()) musicName = "Titre inconnu";

            return musicName;
        } catch (Exception e) {
            currentEvent.getHook().editOriginal("## :x: Impossible de récupérer le nom de la musique.")
                    .queue();
            LOGs.sendLog("Impossible de récupérer le nom de la musique", "ERROR");
            return null;
        }
    }
}
