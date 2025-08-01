package fr.anthonus.utils.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.settings.SettingsLoader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class YoutubeAPI {
    private static final OkHttpClient client = new OkHttpClient();

    public static List<String> getVideoURL(String query, int maxResults) {
        query = URLEncoder.encode(query, StandardCharsets.UTF_8);

        String apiURL = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=" + maxResults + "&q=" + query + "&type=video&key=" + SettingsLoader.getYoutubeApiKey();

        LOGs.sendLog("Création de la requête pour l'API YouTube", DefaultLogType.API);
        Request request = new Request.Builder()
                .url(apiURL)
                .build();

        LOGs.sendLog("Envoi de la requête à l'API YouTube", DefaultLogType.API);
        String jsonData;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            LOGs.sendLog("Requête recue", DefaultLogType.API);

            jsonData = response.body().string();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

}
