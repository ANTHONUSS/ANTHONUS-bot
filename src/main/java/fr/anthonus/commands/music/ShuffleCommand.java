package fr.anthonus.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShuffleCommand extends Command {
    public ShuffleCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /shuffle initialisée", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        if (isQueueEmpty()) return;

        long guildID = currentEvent.getGuild().getIdLong();
        List<AudioTrack> queue = ServerManager.getServer(guildID).getPlayerManager().getQueue();

        for (int i = 0; i < queue.size(); i++) {
            int randomIndex = (int) (Math.random() * queue.size());
            AudioTrack temp = queue.get(i);
            queue.set(i, queue.get(randomIndex));
            queue.set(randomIndex, temp);
        }

        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle(":twisted_rightwards_arrows: Playlist mélangée :twisted_rightwards_arrows:");

        embed.setColor(Color.GREEN);

        currentEvent.replyEmbeds(embed.build()).queue();

    }

    @Override
    protected boolean isQueueEmpty() {
        long guildID = currentEvent.getGuild().getIdLong();
        List<AudioTrack> queue = ServerManager.getServer(guildID).getPlayerManager().getQueue();

        if (queue.isEmpty() || queue.size() == 1) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(":warning: Pas assez de musiques dans la file d'attente :warning:");
            if (queue.size() == 1) {
                embed.setDescription("Il n'y a qu'une seule musique dans la file d'attente, le mélange n'est pas possible.");
            } else {
                embed.setDescription("La file d'attente est vide, veuillez ajouter des musiques pour mélanger.");
            }

            embed.setColor(Color.YELLOW);

            embed.setFooter("\uD83D\uDCA1 Ajoutez des musiques avec la commande /add", null);

            currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();
            LOGs.sendLog("Aucune musique dans la liste pour le serveur " + currentEvent.getGuild().getName(), DefaultLogType.WARNING);

            return true;
        }

        return false;
    }
}
