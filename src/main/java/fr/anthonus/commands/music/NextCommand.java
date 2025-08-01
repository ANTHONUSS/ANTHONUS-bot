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
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;

public class NextCommand extends Command {
    public NextCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /next initialisée", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        if (!isThereACurrentTrack()) return;

        long guildID = currentEvent.getGuild().getIdLong();
        PlayerManager playerManager = ServerManager.getServer(guildID).getPlayerManager();
        AudioManager audioManager = currentEvent.getGuild().getAudioManager();

        AudioTrack nextTrack = ServerManager.getServer(guildID).getPlayerManager().getNextTrack();
        if (nextTrack == null) {
            audioManager.closeAudioConnection();
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(":warning: Pas de musiques suivantes :warning:");
            embed.setDescription("Plus de musiques dans la playlist - Bot déconnecté");

            embed.setColor(Color.YELLOW);

            currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();
            return;
        }

        playerManager.getPlayer().startTrack(nextTrack.makeClone(), false);
        playerManager.setCurrentTrack(nextTrack);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":track_next: Musique suivante lancée : `" + nextTrack.getInfo().title + "`:track_next:");

        if (nextTrack instanceof YoutubeAudioTrack) {
            String videoId = nextTrack.getIdentifier();
            String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
            embed.setThumbnail(thumbnailUrl);
        }

        embed.setColor(Color.CYAN);

        currentEvent.replyEmbeds(embed.build()).queue();
    }
}
