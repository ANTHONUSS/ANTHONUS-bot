package fr.anthonus.utils;

import net.dv8tion.jda.api.entities.Guild;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String replaceIDsByNickname(Guild guild, String message) {
        Pattern mentionPattern = Pattern.compile("<@!?(\\d+)>");
        Matcher matcher = mentionPattern.matcher(message);

        Set<String> ids = new HashSet<>();
        while (matcher.find()) {
            ids.add(matcher.group(1));
        }

        Map<String, String> idToName = new HashMap<>();
        for (String id : ids) {
            try {
                String name = guild.retrieveMemberById(id)
                    .submit()
                    .thenApply(member -> member.getEffectiveName())
                    .exceptionally(e -> "inconnu")
                    .get();
                idToName.put(id, name);
            } catch (Exception e) {
                idToName.put(id, "inconnu");
            }
        }

        String result = message;
        for (String id : ids) {
            result = result.replaceAll("<@!?" + id + ">", "@" + idToName.get(id));
        }

        return result;
    }

    public static byte[] downloadImageToByteArray(String imgURL) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(imgURL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erreur lors du téléchargment " + response);
            }

            return response.body().bytes();
        }

    }
}