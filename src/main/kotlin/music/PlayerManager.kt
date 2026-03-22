package music

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager
import dev.lavalink.youtube.YoutubeAudioSourceManager
import net.dv8tion.jda.api.entities.Guild

object PlayerManager {
    val playerManager = DefaultAudioPlayerManager().apply {
        registerSourceManager(YoutubeAudioSourceManager(true))
        registerSourceManager(SoundCloudAudioSourceManager.createDefault())
        registerSourceManager(HttpAudioSourceManager())
    }
    private val musicManagers = mutableMapOf<Long, GuildMusicManager>()

    fun getGuildMusicManager(guild: Guild): GuildMusicManager {
        return musicManagers.getOrPut(guild.idLong) {
            GuildMusicManager(playerManager)
        }
    }
}