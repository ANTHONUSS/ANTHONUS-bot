package discord.commands.music

import discord.commands.SubCommand
import helpers.CommandHelper
import helpers.EmbedHelper
import music.PlayerManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class PlayMusicCommand: SubCommand() {
    override val name = "play"
    override val description = "Joue la première piste audio de la file d'attente"

    override fun executeBody(event: SlashCommandInteractionEvent) {
        if (!CommandHelper.isUserInVoiceChannel(event)) return

        if (CommandHelper.isGuildNull(event)) return
        // already verified in the statements before
        val guild = event.guild!!
        val member = event.member!!
        val voiceChannel = member.voiceState!!.channel!!

        val guildMusicManager = PlayerManager.getGuildMusicManager(guild)
        val audioManager = guild.audioManager

        val scheduler = guildMusicManager.scheduler

        if (CommandHelper.isPlaylistEmpty(event, scheduler)) return
        if (CommandHelper.isTrackPlaying(event, scheduler)) return
        if (!CommandHelper.isThereSelectedMusic(event, scheduler)) return

        event.deferReply().queue()

        if (!audioManager.isConnected) {
            audioManager.sendingHandler = guildMusicManager.sendHandler
            audioManager.openAudioConnection(voiceChannel)
        }

        scheduler.play()

        event.hook.editOriginalEmbeds(
            EmbedHelper.createEmbed(
                type = EmbedHelper.Type.SUCCESS,
                description = "Lecture de `${currentTrack.info.title}` dans <#${voiceChannel.id}>"
            )
        ).queue()
    }
}