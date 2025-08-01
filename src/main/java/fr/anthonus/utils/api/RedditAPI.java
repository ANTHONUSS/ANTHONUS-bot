package fr.anthonus.utils.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Random;

public class RedditAPI {
    private static final OkHttpClient client = new OkHttpClient();

    private static final String HEADER_NAME = "User-Agent";
    private static final String HEADER_VALUE = "ANTHONUS-Bot (https://github.com/ANTHONUSS/ANTHONUS-bot) by /u/Darkcp_YTB";

    public static String getBlursedImageURL(String subreddit) {
        String redditUrl = "https://www.reddit.com/r/" + subreddit + "/hot.json?limit=100";

        LOGs.sendLog("Création de la requête pour Reddit", DefaultLogType.API);
        Request request = new Request.Builder()
                .url(redditUrl)
                .header(HEADER_NAME, HEADER_VALUE)
                .build();

        LOGs.sendLog("Envoi de la requête à Reddit", DefaultLogType.API);
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

        JsonArray posts = JsonParser.parseString(jsonData)
                .getAsJsonObject()
                .getAsJsonObject("data")
                .getAsJsonArray("children");

        JsonObject randomPost = getRandomPost(posts);
        while (randomPost == null) {
            randomPost = getRandomPost(posts);
        }

        return randomPost.get("url").getAsString();
    }

    private static JsonObject getRandomPost(JsonArray posts) {
        Random random = new Random();
        JsonObject randomPostData = posts.get(random.nextInt(posts.size()))
                .getAsJsonObject()
                .getAsJsonObject("data");

        if (randomPostData.has("url")) {
            String url = randomPostData.get("url").getAsString();
            if (url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".gif")) {
                return randomPostData;
            }
        }

        return null;
    }

}
