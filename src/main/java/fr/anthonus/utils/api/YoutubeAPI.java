package fr.anthonus.utils.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.settings.SettingsLoader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class YoutubeAPI {
    private static final OkHttpClient client = new OkHttpClient();

    public static List<String> getVideoURL(String query, int maxResults) {
        query = URLEncoder.encode(query, StandardCharsets.UTF_8);

        String apiURL = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=" + maxResults + "&q=" + query + "&type=video&key=" + SettingsLoader.getYoutubeApiKey();

        String jsonData = getJsonData(apiURL);

        JsonArray items = JsonParser.parseString(jsonData)
                .getAsJsonObject()
                .getAsJsonArray("items");

        if (items.size() == 0) {
            LOGs.sendLog("Aucun résultat trouvé pour la requête : " + query, DefaultLogType.WARNING);
            return new ArrayList<>();
        }

        List<String> videoURLs = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            String videoId = items.get(i)
                    .getAsJsonObject()
                    .getAsJsonObject("id")
                    .get("videoId").getAsString();

            String videoURL = "https://www.youtube.com/watch?v=" + videoId;
            videoURLs.add(videoURL);
        }

        return videoURLs;

    }

    public static String getVideoTitle(String videoURL) {
        String videoId = getVideoId(videoURL);

        String apiURL = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + videoId + "&key=" + SettingsLoader.getYoutubeApiKey();

        String jsonData = getJsonData(apiURL);

        JsonArray items = JsonParser.parseString(jsonData)
                .getAsJsonObject()
                .getAsJsonArray("items");

        if (items.isEmpty()) {
            LOGs.sendLog("Aucun résultat trouvé pour la requête avec l'id : " + videoId, DefaultLogType.WARNING);
            return null;
        }

        return items.get(0).getAsJsonObject()
                .getAsJsonObject("snippet")
                .get("title").getAsString();
    }

    public static void downloadVideo(SlashCommandInteractionEvent event, String lien, boolean isMusic, String videoTitle) {
        LOGs.sendLog("Démarrage du téléchargement de la vidéo : " + lien, DefaultLogType.DOWNLOAD);
        File tempFolder = new File("temp");

        LOGs.sendLog("Création du processus de téléchargement", DefaultLogType.DOWNLOAD);
        ProcessBuilder processBuilder;
        videoTitle = sanitizeFileName(videoTitle);
        String outputPattern = "temp/" + videoTitle + (isMusic ? ".mp3" : ".mp4");
        if (isMusic) {
            processBuilder = new ProcessBuilder(
                    "data/yt-dlp.exe",
                    "--embed-thumbnail",
                    "--embed-metadata",
                    "-f", "bestaudio",
                    "-x",
                    "--audio-format", "mp3",
                    "--audio-quality", "320k",
                    "--output", outputPattern,
                    lien
            );
        } else {
            processBuilder = new ProcessBuilder(
                    "data/yt-dlp.exe",
                    "--embed-thumbnail",
                    "--embed-metadata",
                    "-f", "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best",
                    "--merge-output-format", "mp4",
                    "--output", outputPattern,
                    lien
            );
        }
        LOGs.sendLog("Processus de téléchargement créé", DefaultLogType.DOWNLOAD);

        LOGs.sendLog("Lancement du processus de téléchargement", DefaultLogType.DOWNLOAD);
        try {
            Process process = processBuilder.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;

            while ((line = br.readLine()) != null) {
                LOGs.sendLog(line, DefaultLogType.DOWNLOAD_CMD);
            }

            while ((line = errorReader.readLine()) != null) {
                LOGs.sendLog(line, DefaultLogType.ERROR_CMD);
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new IOException("Le processus de téléchargement a échoué avec le code de sortie : " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            sendErrorEmbed(event, e);
        }
        LOGs.sendLog("Téléchargement terminé", DefaultLogType.DOWNLOAD);

        LOGs.sendLog("Upload du fichier sur le NAS", DefaultLogType.DOWNLOAD);
        File downloadedFile = new File(tempFolder, videoTitle + (isMusic ? ".mp3" : ".mp4"));

        LOGs.sendLog("Déplacement du fichier téléchargé vers le NAS", DefaultLogType.DOWNLOAD);
        try {
            Path sourcePath = Paths.get(downloadedFile.getAbsolutePath());
            Path destinationPath = Paths.get(SettingsLoader.getSharingDirectory(), downloadedFile.getName());
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            sendErrorEmbed(event, e);
            return;
        }
        LOGs.sendLog("Fichier déplacé vers le NAS", DefaultLogType.DOWNLOAD);

        LOGs.sendLog("Envoi du lien de téléchargement", DefaultLogType.DOWNLOAD);
        String encodedFileName = URLEncoder.encode(downloadedFile.getName(), StandardCharsets.UTF_8);
        String downloadLink = SettingsLoader.getSharingURL() + "/" + encodedFileName;
        downloadLink = downloadLink.replace("+", "%20");

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":white_check_mark: Téléchargement terminé :white_check_mark:");
        embed.setDescription("- [Lien direct](" + downloadLink + ")\n" +
                "- [Lien de téléchargement](" + downloadLink + "?download)");
        embed.setFooter("Le fichier sera disponible pendant 48 heures.");
        embed.setColor(Color.GREEN);
        event.getHook().editOriginalEmbeds(embed.build()).queue();
        LOGs.sendLog("Lien de téléchargement envoyé", DefaultLogType.DOWNLOAD);
    }

    private static void sendErrorEmbed(SlashCommandInteractionEvent event, Exception e) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":x: Erreur lors du téléchargement :x:");
        embed.setDescription("Une erreur est survenue lors du téléchargement de la vidéo. Veuillez réessayer.\n" +
                "Détails de l'erreur :\n" + e.getMessage());
        embed.setFooter("Si le problème persiste, veuillez contacter le support.");

        embed.setColor(Color.RED);

        event.getHook().editOriginalEmbeds(embed.build()).queue();

        LOGs.sendLog("Erreur lors du téléchargement de la vidéo :\n" + e.getMessage(), DefaultLogType.ERROR);

        throw new RuntimeException(e);
    }

    private static String sanitizeFileName(String name) {
        return name.replaceAll("[\\\\/:*?\"<>|]", "");
    }

    private static String getVideoId(String videoURL) {
        if (videoURL.contains("youtu.be/")) {
            return videoURL.substring(videoURL.lastIndexOf("/") + 1);
        } else if (videoURL.contains("youtube.com/watch?v=")) {
            return videoURL.substring(videoURL.indexOf("v=") + 2, videoURL.indexOf("&"));
        } else {
            throw new IllegalArgumentException("URL de vidéo YouTube invalide : " + videoURL);
        }
    }

    private static String getJsonData(String url) {
        LOGs.sendLog("Création de la requête pour l'API YouTube", DefaultLogType.API);
        Request request = new Request.Builder()
                .url(url)
                .build();

        LOGs.sendLog("Envoi de la requête à l'API YouTube", DefaultLogType.API);
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            LOGs.sendLog("Requête recue", DefaultLogType.API);

            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

