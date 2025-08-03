package fr.anthonus.commands.music;

import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.api.YoutubeAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.List;

public class AddSearchCommand extends Command {
    private final String query;

    public AddSearchCommand(SlashCommandInteractionEvent event, String query) {
        super(event);
        this.query = query;

        LOGs.sendLog("Commande /add-search initialisée avec la query " + query, DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        List<String> videoURLs = YoutubeAPI.getVideoURL(query, 1);

        if (videoURLs.isEmpty()) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(":warning: Aucune vidéo trouvée pour votre recherche :warning:");
            embed.setDescription("Essayez une autre recherche.");

            embed.setColor(Color.YELLOW);

            currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();
            return;
        }

        String videoURL = videoURLs.get(0);

        new AddCommand(currentEvent, videoURL).run();

    }
}
