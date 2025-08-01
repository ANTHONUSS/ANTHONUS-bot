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
        long guildID = currentEvent.getGuild().getIdLong();
        List<AudioTrack> tracks = ServerManager.getServer(guildID).getPlayerManager().getTracks();

        if (tracks.isEmpty()) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(":warning: Aucune musique dans la liste :warning:");
            embed.setDescription("La liste des musiques est vide.");

            embed.setColor(Color.YELLOW);

            embed.setFooter("\uD83D\uDCA1 Ajoutez des musiques avec la commande /add", null);

            currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();
            LOGs.sendLog("Aucune musique dans la liste pour le serveur " + currentEvent.getGuild().getName(), DefaultLogType.WARNING);
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":scroll: File d'attente :scroll:");

        StringBuilder trackList = new StringBuilder();
        int i = 1;
        for (AudioTrack track : tracks) {
            trackList.append("**"+i+".** `").append(track.getInfo().title).append("`\n");
            i++;
        }
        embed.setDescription(trackList.toString());

        embed.setFooter("Total : " + tracks.size() + (tracks.size() == 1 ? " musique" : " musiques"), null);

        embed.setColor(Color.CYAN);

        currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }
}
