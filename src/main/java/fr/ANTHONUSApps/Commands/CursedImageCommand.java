package fr.ANTHONUSApps.Commands;


import com.google.gson.*;
import fr.ANTHONUSApps.Utils.APICalls.APICall;
import fr.ANTHONUSApps.LOGs;
import fr.ANTHONUSApps.Utils.APICalls.APICallReddit;
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

        LOGs.sendLog("CursedImage command initialisée", LOGs.LogType.COMMAND);
    }

    public void run() {
        currentEvent.deferReply().queue(
                success -> {
                    try {
                        String imageURL = getImageUrl();
                        if (imageURL != null) {
                            currentEvent.getHook().editOriginal(imageURL).queue();
                            LOGs.sendLog("CursedImage générée"
                                            + "\nUser : @" + currentEvent.getUser().getEffectiveName()
                                            + "\nServeur : " + currentEvent.getGuild().getName()
                                            + "\nSalon : #" + currentEvent.getChannel().getName()
                                            + "\nImage : " + imageURL,
                                    LOGs.LogType.COMMAND);
                        } else {
                            currentEvent.getHook().editOriginal("Aucune image trouvée").queue();
                            LOGs.sendLog("Erreur sur CursedImage"
                                            + "\nUser : @" + currentEvent.getUser().getEffectiveName()
                                            + "\nServeur : " + currentEvent.getGuild().getName()
                                            + "\nSalon : #" + currentEvent.getChannel().getName(),
                                    LOGs.LogType.ERROR);
                        }
                    } catch (Exception e) {
                        currentEvent.getHook().editOriginal("Une erreur est survenue lors de la récupération de l'image" + e.getMessage()).queue();
                        e.printStackTrace();
                    }
                },
                failure -> {
                    LOGs.sendLog("Erreur lors de l'envoi du deferReply", LOGs.LogType.ERROR);
                }
        );

    }

    private String getImageUrl() throws IOException {
        APICallReddit redditRequest = new APICallReddit(REDDIT_URL, "User-Agent", "ANTHONUS-Bot (https://github.com/ANTHONUSS/ANTHONUS-bot) by /u/Darkcp_YTB");

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
