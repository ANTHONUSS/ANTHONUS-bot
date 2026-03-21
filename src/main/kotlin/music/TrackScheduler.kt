package music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import java.util.LinkedList
import java.util.Queue

class TrackScheduler(
    val audioPlayer: AudioPlayer
): AudioEventAdapter() {
    val queue: Queue<AudioTrack> = LinkedList()

    fun queue(track: AudioTrack) {
        if (!audioPlayer.startTrack(track, true)) {
            queue.offer(track)
        }
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason?) {
        if (endReason?.mayStartNext == true) {
            val nextTrack = queue.poll()
            if (nextTrack != null) {
                audioPlayer.startTrack(nextTrack, false)
            }
        }
    }


}