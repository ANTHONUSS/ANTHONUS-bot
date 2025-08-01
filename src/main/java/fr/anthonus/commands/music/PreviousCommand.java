package fr.anthonus.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.track.YoutubeAudioTrack;
import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.music.PlayerManager;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class PreviousCommand extends Command {
    public PreviousCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /previous initialisée", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        if (!isThereACurrentTrack()) return;

        long guildID = currentEvent.getGuild().getIdLong();
        PlayerManager playerManager = ServerManager.getServer(guildID).getPlayerManager();

        AudioTrack previousTrack = ServerManager.getServer(guildID).getPlayerManager().getPreviousTrack();
        if (previousTrack == null)  previousTrack = ServerManager.getServer(guildID).getPlayerManager().getCurrentTrack();

        playerManager.getPlayer().startTrack(previousTrack.makeClone(), false);
        playerManager.setCurrentTrack(previousTrack);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":track_previous: Musique précédente lancée : `" + previousTrack.getInfo().title + "`:track_previous:");

        if (previousTrack instanceof YoutubeAudioTrack) {
            String videoId = previousTrack.getIdentifier();
            String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
            embed.setThumbnail(thumbnailUrl);
        }

        embed.setColor(Color.CYAN);

        currentEvent.replyEmbeds(embed.build()).queue();
    }
}
