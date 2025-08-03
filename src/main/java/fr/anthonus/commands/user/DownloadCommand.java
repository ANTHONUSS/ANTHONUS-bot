package fr.anthonus.commands.user;

import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.api.YoutubeAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class DownloadCommand extends Command {
    private String lien;
    private final boolean isMusic;

    public DownloadCommand(SlashCommandInteractionEvent event, String lien, boolean isMusic) {
        super(event);

        this.lien = lien;
        this.isMusic = isMusic;

        LOGs.sendLog("Commande /download lancée avec le lien : " + lien, DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        if (lien.contains("?list=")) {
            lien = lien.replaceAll("\\?list=.*", "");
        }

        String videoTitle = YoutubeAPI.getVideoTitle(lien);

        if (videoTitle == null) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(":warning: Format de lien invalide :warning:");
            StringBuilder description = new StringBuilder("Le lien fourni n'est pas valide. Les causes possibles sont :\n");
            description.append("- Le lien n'est pas un lien YouTube.\n");
            description.append("- Le lien n'est pas un lien YouTube valide.\n");
            description.append("- Le lien est une playlist ou une chaîne YouTube.\n");

            embed.setDescription(description.toString());

            embed.setColor(Color.YELLOW);

            currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();
            return;
        }

        currentEvent.deferReply().setEphemeral(true).queue();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":arrow_heading_down: Téléchargement de `" + videoTitle + "` en cours... :arrow_heading_down:");
        embed.setDescription("Type de téléchargement : " + (isMusic ? "`Musique`" : "`Vidéo`"));

        embed.setColor(Color.GRAY);

        currentEvent.getHook().sendMessageEmbeds(embed.build()).setEphemeral(true).queue();

        Thread downloadThread = new Thread(() -> {
            YoutubeAPI.downloadVideo(currentEvent, lien, isMusic, videoTitle);
        });
        LOGs.sendLog("Démarrage du thread de téléchargement pour la vidéo : " + videoTitle, DefaultLogType.COMMAND);
        downloadThread.start();
    }
}
