package fr.anthonus.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.utils.json.SettingJson;
import fr.anthonus.utils.music.TrackScheduler;
import okhttp3.internal.http2.Settings;

import java.time.Instant;
import java.util.ArrayList;

public class Server {
    private final Long guildId;

    private SettingJson settingJson;

    private final AudioPlayer audioPlayer;
    private final ArrayList<AudioTrack> queue;
    private AudioTrack currentTrack;
    private boolean looping;
    private Instant lastModified;

    public Server(Long guildId) {
        this.guildId = guildId;
        this.looping = false;

        this.audioPlayer = ServerManager.audioPlayerManager.createPlayer();
        this.queue = new ArrayList<>();


        this.audioPlayer.addListener(new TrackScheduler(this.audioPlayer, this.guildId));

        this.settingJson = new SettingJson(this.guildId);
    }

    public boolean isLooping() {
        return this.looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public void setCurrentTrack(AudioTrack track) {
        currentTrack = track;
    }

    public AudioTrack getCurrentTrack() {
        return currentTrack;
    }

    public AudioTrack getNextTrack() {
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i) == currentTrack) {
                if (i + 1 < queue.size()) {
                    return queue.get(i + 1);
                } else if (looping) {
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
                } else if (looping) {
                    return queue.get(queue.size() - 1);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public Instant getLastModified() {return this.lastModified;}
    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public ArrayList<AudioTrack> getQueue() {
        return queue;
    }
    public void addTrackToQueue(AudioTrack track) {
        queue.add(track);
    }

    public SettingJson getSettings() {
        return this.settingJson;
    }
}
