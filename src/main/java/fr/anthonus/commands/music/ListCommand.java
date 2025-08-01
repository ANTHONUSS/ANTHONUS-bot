package fr.anthonus.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.List;

public class ListCommand extends Command {

    public ListCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /list initialisée", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        if (isQueueEmpty()) return;

        long guildID = currentEvent.getGuild().getIdLong();
        List<AudioTrack> queue = ServerManager.getServer(guildID).getPlayerManager().getQueue();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":scroll: File d'attente :scroll:");

        StringBuilder trackList = new StringBuilder();
        int i = 1;
        for (AudioTrack track : queue) {
            trackList.append("**"+i+".** `").append(track.getInfo().title).append("`\n");
            i++;
        }
        embed.setDescription(trackList.toString());

        embed.setFooter("Total : " + queue.size() + (queue.size() == 1 ? " musique" : " musiques"), null);

        embed.setColor(Color.CYAN);

        currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }
}
