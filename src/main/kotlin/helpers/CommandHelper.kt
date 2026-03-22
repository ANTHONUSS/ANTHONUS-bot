package helpers

import music.TrackScheduler
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent


object CommandHelper {

    fun isGuildNull(event: SlashCommandInteractionEvent): Boolean {
        return event.guild == null
    }

    fun isUserInVoiceChannel(event: SlashCommandInteractionEvent): Boolean {
        val voiceChannel: AudioChannelUnion? = event.member?.voiceState?.channel

        return voiceChannel == null
    }

    fun isPlaylistEmpty(scheduler: TrackScheduler): Boolean {
        return scheduler.isPlaylistEmpty()
    }
}