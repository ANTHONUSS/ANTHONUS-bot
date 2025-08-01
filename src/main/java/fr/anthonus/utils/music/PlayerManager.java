package fr.anthonus.utils.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private final AudioPlayerManager manager;
    private final AudioPlayer player;
    private AudioTrack currentTrack;

    private final List<AudioTrack> tracks;

    public PlayerManager() {
        manager = new DefaultAudioPlayerManager();
        player = manager.createPlayer();
        tracks = new ArrayList<>();
    }

    public AudioPlayerManager getManager() {
        return manager;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public AudioTrack getCurrentTrack() {
        return currentTrack;
    }
    public void setCurrentTrack(AudioTrack track) {
        currentTrack = track;
    }

    public List<AudioTrack> getTracks() {
        return tracks;
    }
    public void addTrack(AudioTrack track) {
        tracks.add(track);
    }
    public void clearTracks() {
        tracks.clear();
    }
    public void removeTrack(AudioTrack track) {
        tracks.remove(track);
    }
}
