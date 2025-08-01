package fr.anthonus.utils.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import fr.anthonus.utils.servers.ServerManager;

import static fr.anthonus.Main.jda;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer audioPlayer;
    private Long guildId;

    public TrackScheduler(AudioPlayer audioPlayer, Long guildId) {
        this.audioPlayer = audioPlayer;
        this.guildId = guildId;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            AudioTrack nextTrack = ServerManager.getServer(guildId).getPlayerManager().getNextTrack();
            if (nextTrack == null) {
                jda.getGuildById(guildId).getAudioManager().closeAudioConnection();
                ServerManager.getServer(guildId).getPlayerManager().setCurrentTrack(null);
                return;
            }
            audioPlayer.startTrack(nextTrack.makeClone(), false);
            ServerManager.getServer(guildId).getPlayerManager().setCurrentTrack(nextTrack);
        }
    }
}
