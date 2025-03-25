package fr.ANTHONUSApps.Utils.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import static fr.ANTHONUSApps.Main.jda;

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
            AudioTrack nextTrack = MusicManager.players.get(guildId).getNextTrack();
            if (nextTrack == null) {
                jda.getGuildById(guildId).getAudioManager().closeAudioConnection();
                return;
            }
            audioPlayer.startTrack(nextTrack.makeClone(), false);
            MusicManager.players.get(guildId).setCurrentTrack(nextTrack);
            MusicManager.players.get(guildId).setCurrentTrack(null);
        }
    }
}
