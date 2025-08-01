package fr.anthonus.commands.music;

import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.api.YoutubeAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.List;

public class SearchCommand extends Command {
    private String query;
    private boolean addDirectly;

    public SearchCommand(SlashCommandInteractionEvent event, String query, boolean addDirectly) {
        super(event);
        this.query = query;
        this.addDirectly = addDirectly;

        LOGs.sendLog("Commande /music search initialisée avec la query " + query, DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        int maxResults;
        if (addDirectly) {
            maxResults = 1;
        } else {
            maxResults = 10;
        }
        List<String> videoURLs = YoutubeAPI.getVideoURL(query, maxResults);

        if (videoURLs.isEmpty()) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(":warning: Aucune vidéo trouvée pour votre recherche :warning:");
            embed.setDescription("Essayez une autre recherche.");

            embed.setColor(Color.YELLOW);

            currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();
            return;
        }

        if (addDirectly) {
            String videoURL = videoURLs.get(0);

            new AddCommand(currentEvent, videoURL).run();
        } else {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(":mag: Résultats de la recherche pour : `" + query + "` :mag:");

            StringBuilder description = new StringBuilder();
            for (int i = 0; i < videoURLs.size(); i++) {
                String videoURL = videoURLs.get(i);
                description.append("**").append(i + 1).append("**. ").append(videoURL).append("\n");
            }
            embed.setDescription(description.toString());

            embed.setColor(Color.RED);

            currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }



    }
}
