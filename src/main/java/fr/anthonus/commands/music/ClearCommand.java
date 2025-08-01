package fr.anthonus.commands.music;

import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class ClearCommand extends Command {

    public ClearCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /clear-playlist initialisée", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        if (isQueueEmpty()) return;

        long guildID = currentEvent.getGuild().getIdLong();

        if (ServerManager.getServer(guildID).getPlayerManager().getCurrentTrack() != null) {

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(":warning: Musique en cours de lecture :warning:");
            embed.setDescription("Impossible de vider la file d'attente pendant la lecture d'une musique.");

            embed.setColor(Color.YELLOW);

            currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();

            return;
        }

        ServerManager.getServer(guildID).getPlayerManager().clearTracks();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":broom: File d'attente vidée :broom:");

        embed.setColor(Color.GREEN);

        currentEvent.replyEmbeds(embed.build()).queue();

        LOGs.sendLog("Playlist vidée", DefaultLogType.COMMAND);
    }
}
