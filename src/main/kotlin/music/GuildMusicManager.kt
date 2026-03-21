package music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager

class GuildMusicManager(playerManager: AudioPlayerManager) {
    val player: AudioPlayer = playerManager.createPlayer()
    val scheduler: TrackScheduler = TrackScheduler(player)
    val sendHandler: AudioPlayerSendHandler = AudioPlayerSendHandler(player)

    init {
        player.addListener(scheduler)
    }
}