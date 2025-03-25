package fr.anthonus.Commands.SlashCommands.music;

import fr.anthonus.Commands.SlashCommands.Command;
import fr.anthonus.LOGs;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

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
    }

    private void downloadMusic() {
        Thread downloadThread = new Thread(() -> {
            String musicName = getMusicNameFromURL();

            currentEvent.getHook().sendMessage("## :arrow_down: Téléchargement de `" + musicName + "` en cours...")
                    .setEphemeral(true)
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
                    currentEvent.getHook().deleteOriginal().queue();
                    currentEvent.getChannel().sendMessage("## ✅ La musique `" + musicName + "` à été téléchargée.")
                            .queue();
                    LOGs.sendLog("Musique téléchargée"
                            + "\nNom : " + musicName
                            + "\nUser : " + currentEvent.getUser().getName(), "DOWNLOAD");
                } else {
                    currentEvent.getHook().editOriginal("## :x: Une erreur est survenue lors du téléchargement")
                            .queue();
                }
            } catch (Exception e) {
                currentEvent.getHook().editOriginal("## :x: Une erreur est survenue lors du téléchargement : " + e.getMessage())
                        .queue();
            }
        });

        downloadThread.start();
    }

    private String getMusicNameFromURL(){
        String musicName;

        try {
            ProcessBuilder titleProcessBuilder = new ProcessBuilder(
                    "yt-dlp.exe",
                    "--get-title",
                    url
            );
            Process titleProcess = titleProcessBuilder.start();

            BufferedReader titleReader = new BufferedReader(new InputStreamReader(titleProcess.getInputStream()));
            musicName = titleReader.readLine();
            titleProcess.waitFor();

            if (musicName == null || musicName.isEmpty()) musicName = "Titre inconnu";

            return musicName;
        } catch (Exception e) {
            currentEvent.getHook().sendMessage("## :x: Impossible de récupérer le nom de la musique.")
                    .setEphemeral(true)
                    .queue();
            return null;
        }
    }
}
