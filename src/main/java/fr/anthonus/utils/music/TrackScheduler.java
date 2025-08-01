package fr.anthonus.utils.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

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
            //TODO: faire en sorte de démarrer le prochaine piste, la première si le loop est activé et que c'est la dernière piste
//            if (looping) {
//                audioPlayer.startTrack(track.makeClone(), false);
//            } else {
//                jda.getGuildById(guildId).getAudioManager().closeAudioConnection();
//                looping = false;
//                PlayerManager.setCurrentTrack(null);
//            }
        }
    }
}
