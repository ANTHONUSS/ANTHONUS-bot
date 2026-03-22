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

        event.deferReply().queue()

        if (CommandHelper.isGuildNull(event)) return
        // already verified in the statements before
        val guild = event.guild!!
        val member = event.member!!
        val voiceChannel = member.voiceState!!.channel!!

        val guildMusicManager = PlayerManager.getGuildMusicManager(guild)
        val audioManager = guild.audioManager

        if (!audioManager.isConnected) {
            audioManager.sendingHandler = guildMusicManager.sendHandler
            audioManager.openAudioConnection(voiceChannel)
        }

        val firstTrack = guildMusicManager.scheduler.queue.poll()
        if (firstTrack == null) {
            event.hook.editOriginalEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "La file d'attente est vide"
                )
            ).queue()

            return
        }

        guildMusicManager.player.volume = 50
        guildMusicManager.player.startTrack(firstTrack.makeClone(), false)

        event.hook.editOriginalEmbeds(
            EmbedHelper.createEmbed(
                type = EmbedHelper.Type.SUCCESS,
                description = "Lecture de `${firstTrack.info.title}` dans `${voiceChannel.name}`"
            )
        ).queue()
    }
}