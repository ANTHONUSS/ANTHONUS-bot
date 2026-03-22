package music

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import net.dv8tion.jda.api.entities.Guild

object PlayerManager {
    val playerManager = DefaultAudioPlayerManager().apply {
        registerSourceManager(dev.lavalink.youtube.YoutubeAudioSourceManager(true))
    }
    private val musicManagers = mutableMapOf<Long, GuildMusicManager>()

    fun getGuildMusicManager(guild: Guild): GuildMusicManager {
        return musicManagers.getOrPut(guild.idLong) {
            GuildMusicManager(playerManager)
        }
    }
}