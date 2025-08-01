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
import java.util.ArrayList;
import java.util.List;

public class JumpCommand extends Command {
    private String selectedMusic;

    public JumpCommand(SlashCommandInteractionEvent event, String selectedMusic) {
        super(event);

        this.selectedMusic = selectedMusic;

        LOGs.sendLog("Commande /jump initialisée", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        if (!isThereACurrentTrack()) return;

        long guildID = currentEvent.getGuild().getIdLong();
        PlayerManager playerManager = ServerManager.getServer(guildID).getPlayerManager();
        List<AudioTrack> queue = ServerManager.getServer(guildID).getPlayerManager().getQueue();

        for (AudioTrack track : queue) {
            if (track.getInfo().title.equals(selectedMusic)) {
                playerManager.getPlayer().startTrack(track.makeClone(), false);
                playerManager.setCurrentTrack(track);


                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle(":arrow_heading_down: Passage à la musique : `" + track.getInfo().title + "`:arrow_heading_down:");

                if (track instanceof YoutubeAudioTrack) {
                    String videoId = track.getIdentifier();
                    String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
                    embed.setThumbnail(thumbnailUrl);
                }

                embed.setColor(Color.CYAN);

                currentEvent.replyEmbeds(embed.build()).queue();

                LOGs.sendLog("Musique " + track.getInfo().title + " jouée", DefaultLogType.COMMAND);

                return;
            }
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":warning: Musique non trouvée :warning:");
        embed.setDescription("La musique `" + selectedMusic + "` n'est pas dans la file d'attente");

        embed.setColor(Color.YELLOW);

        currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();

        LOGs.sendLog("Musique " + selectedMusic + " non trouvée dans la playlist", DefaultLogType.WARNING);
    }
}