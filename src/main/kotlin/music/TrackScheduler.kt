package music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo
import java.util.LinkedList
import java.util.Queue

class TrackScheduler(
    val audioPlayer: AudioPlayer
) : AudioEventAdapter() {
    val playlist: MutableList<AudioTrack> = mutableListOf()
    var currentIndex: Int = -1

    fun add(track: AudioTrack) {
        playlist.add(track)
        if (currentIndex == -1) currentIndex = 0
    }

    fun removeAt(index: Int) {
        playlist.removeAt(index)

        if (playlist.isEmpty()) {
            currentIndex = -1
        } else if (currentIndex > index) {
            currentIndex--
        } else if (currentIndex == index) {
            if (currentIndex >= playlist.size) currentIndex = playlist.size - 1
        }
    }

    fun play() {
        audioPlayer.startTrack(playlist[currentIndex].makeClone(), false)
    }

    fun stop() {
        audioPlayer.stopTrack()
    }

    fun next() {
        currentIndex = (currentIndex + 1) % playlist.size
    }

    fun previous() {
        currentIndex = if (currentIndex - 1 < 0) playlist.size - 1 else currentIndex - 1
    }

    fun shuffle() {

    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason?) {
        if (endReason?.mayStartNext == true) {
            next()
        }
    }

    fun isPlaylistEmpty(): Boolean {
        return playlist.isEmpty()
    }

    fun getTrack(index: Int): AudioTrack? {
        return if (index in playlist.indices) {
            playlist[index]
        } else {
            null
        }
    }

    fun getCurrentTrack(): AudioTrack? {
        return getTrack(currentIndex)
    }

    fun isTrackPlaying(): Boolean {
        return audioPlayer.playingTrack != null
    }

    fun getPlayingTrack(): AudioTrack? {
        return audioPlayer.playingTrack
    }


}