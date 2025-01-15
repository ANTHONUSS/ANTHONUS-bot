package fr.ANTHONUSApps.Commands;


import com.google.gson.*;
import fr.ANTHONUSApps.Utils.APICall;
import fr.ANTHONUSApps.LOGs;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.Random;

public class CursedImageCommand {
    private SlashCommandInteractionEvent currentEvent;

    private final OkHttpClient client = new OkHttpClient();
    private final String SUBREDDIT = "blursedimages";
    private final int postsLimit = 100;
    private final String REDDIT_URL = "https://www.reddit.com/r/" + SUBREDDIT + "/hot.json?limit=" + postsLimit;


    public CursedImageCommand(SlashCommandInteractionEvent event) {
        this.currentEvent = event;
    }

    public void run() {
        try {
            String imageURL = getImageUrl();
            if (imageURL != null) {
                currentEvent.reply(imageURL).queue();
                LOGs.sendLog("CursedImage générée"
                                + "\nUser : @" + currentEvent.getUser().getEffectiveName()
                                + "\nServeur : " + currentEvent.getGuild().getName()
                                + "\nSalon : #" + currentEvent.getChannel().getName()
                                + "\nImage: " + imageURL,
                        LOGs.LogType.CURSED);
            } else {
                currentEvent.reply("Aucune image trouvée")
                        .setEphemeral(true)
                        .queue();
                LOGs.sendLog("Erreur sur CursedImage"
                                + "\nUser : @" + currentEvent.getUser().getEffectiveName()
                                + "\nServeur : " + currentEvent.getGuild().getName()
                                + "\nSalon : #" + currentEvent.getChannel().getName(),
                        LOGs.LogType.ERROR);
            }
        } catch (Exception e) {
            currentEvent.reply("Une erreur est survenue lors de la récupération de l'image" + e.getMessage()).queue();
            e.printStackTrace();
        }
    }

    private String getImageUrl() throws IOException {
        Request request = new Request.Builder()
                .url(REDDIT_URL)
                .header("User-Agent", "ANTHONUS-Bot (https://github.com/ANTHONUSS/ANTHONUS-bot) by /u/Darkcp_YTB ")
                .build();

        APICall redditRequest = new APICall(request);
        JsonArray posts = redditRequest.call().getAsJsonObject("data").getAsJsonArray("children");

        Random random = new Random();
        JsonObject randomPost = posts.get(random.nextInt(posts.size()))
                .getAsJsonObject()
                .getAsJsonObject("data");

        if (randomPost.has("url")) {
            return randomPost.get("url").getAsString();
        }
        return null;
    }
}
