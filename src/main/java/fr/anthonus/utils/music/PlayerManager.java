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

    private final List<AudioTrack> queue;

    public PlayerManager() {
        manager = new DefaultAudioPlayerManager();
        player = manager.createPlayer();
        queue = new ArrayList<>();
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

    public List<AudioTrack> getQueue() {
        return queue;
    }
    public void addTrack(AudioTrack track) {
        queue.add(track);
    }
    public void clearTracks() {
        queue.clear();
    }
    public void removeTrack(AudioTrack track) {
        queue.remove(track);
    }
}
