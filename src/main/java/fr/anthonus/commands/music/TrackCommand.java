package fr.anthonus.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.track.YoutubeAudioTrack;
import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.music.PlayerManager;
import fr.anthonus.utils.servers.Server;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

//TODO: tester la commande
public class TrackCommand extends Command {

    public TrackCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("commande /track initialisée", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        if (!isThereACurrentTrack()) return;

        long guildID = currentEvent.getGuild().getIdLong();
        Server server = ServerManager.getServer(guildID);
        PlayerManager playerManager = server.getPlayerManager();
        AudioTrack currentTrack = playerManager.getCurrentTrack();

        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle(":information_source: Informations sur la musique actuelle :information_source:");

        embed.addField(":scroll: Titre", "`" + currentTrack.getInfo().title + "`", false);
        embed.addField(":artist: Artiste", "`" + currentTrack.getInfo().author + "`", false);

        if (currentTrack.getInfo().isStream) {
            embed.addField(":radio: Type", "`En direct (stream)`", false);
        } else {
            embed.addField(":infinity: Durée", "`" + getDurationFormatted(currentTrack.getInfo().length) + "`", true);
            embed.addField(":hourglass_flowing_sand: Temps écoulé", "`" + getDurationFormatted(currentTrack.getPosition()) + "`", true);
            embed.addBlankField(true);
        }

        embed.addField(":repeat: Loop", server.isLooping() ? "`Activé`" : "`Désactivé`", true);
        embed.addBlankField(true);

        embed.addField(":link: URL", currentTrack.getInfo().uri, false);

        if (currentTrack instanceof YoutubeAudioTrack) {
            String videoId = currentTrack.getIdentifier();
            String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
            embed.setImage(thumbnailUrl);
        }

        embed.setColor(Color.CYAN);

        currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }

    private String getDurationFormatted(long duration) {
        long seconds = (duration / 1000) % 60;
        long minutes = (duration / (1000 * 60)) % 60;
        long hours = (duration / (1000 * 60 * 60)) % 24;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
}
