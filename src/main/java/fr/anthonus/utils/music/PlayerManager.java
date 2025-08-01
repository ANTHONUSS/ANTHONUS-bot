package fr.anthonus.utils.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.utils.servers.Server;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private final AudioPlayerManager manager;
    private final AudioPlayer player;

    private AudioTrack currentTrack;
    private final List<AudioTrack> queue;

    private final Server parent;

    public PlayerManager(Server parent) {
        manager = new DefaultAudioPlayerManager();
        player = manager.createPlayer();
        queue = new ArrayList<>();

        this.parent = parent;
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

    public AudioTrack getNextTrack() {
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i) == currentTrack) {
                if (i + 1 < queue.size()) {
                    return queue.get(i + 1);
                } else if (parent.isLooping()) {
                    return queue.get(0);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public AudioTrack getPreviousTrack() {
        for (int i = queue.size()-1; i >= 0; i--) {
            if (queue.get(i) == currentTrack) {
                if (i - 1 >= 0) {
                    return queue.get(i - 1);
                } else if (parent.isLooping()) {
                    return queue.get(queue.size() - 1);
                } else {
                    return null;
                }
            }
        }
        return null;
    }
}
