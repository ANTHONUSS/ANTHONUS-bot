package fr.anthonus.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.CustomLogType;
import fr.anthonus.logs.logTypes.DefaultLogType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerManager {
    public static final Map<Long, Server> servers = new HashMap<>();

    public static final AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

    public static final ArrayList<AudioTrack> musicsList = new ArrayList<>();


    static {
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public static void updateMusicsList() {
        File musicsFolder = new File("Music");
        if (musicsFolder.exists() && musicsFolder.isDirectory()) {
            for (File file : musicsFolder.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".mp3")) {
                    addTrackToList(file.getAbsolutePath());
                    LOGs.sendLog("Chargement de " + file.getName(), CustomLogType.FILE_LOADING);
                }
            }
        }
    }

    public static void addTrackToList(String filePath) {
        audioPlayerManager.loadItem(filePath, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicsList.add(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                LOGs.sendLog("Playlists non pris en charge", DefaultLogType.ERROR);
            }

            @Override
            public void noMatches() {
                LOGs.sendLog("Aucune musique trouvée", DefaultLogType.ERROR);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                LOGs.sendLog(exception.getMessage(), DefaultLogType.ERROR);
            }
        });
    }

    public static String getFileName(String fileURI) {
        File file = new File(fileURI);
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    public static int getTotalPages() {
        return (int) Math.ceil((double) musicsList.size() / 10);
    }
}
