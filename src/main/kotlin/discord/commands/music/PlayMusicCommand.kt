package discord.commands.music

import discord.commands.SubCommand
import helpers.CommandHelper
import helpers.EmbedHelper
import helpers.LogsHelper
import music.PlayerManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class PlayMusicCommand: SubCommand() {
    override val name = "play"
    override val description = "Joue la première piste audio de la file d'attente"

    override fun executeBody(event: SlashCommandInteractionEvent) {
        if (!CommandHelper.isUserInVoiceChannel(event)) return

        if (CommandHelper.isGuildNull(event)) return
        val guild = event.guild ?: return
        val member = event.member ?: return
        val voiceChannel = member.voiceState?.channel ?: return

        val guildMusicManager = PlayerManager.getGuildMusicManager(guild)
        val audioManager = guild.audioManager

        val scheduler = guildMusicManager.scheduler

        if (CommandHelper.isPlaylistEmpty(event, scheduler)) return
        if (scheduler.isTrackPlaying()) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "La musique est déjà en cours de lecture"
                )
            ).setEphemeral(true)
                .queue()

            return
        }

        event.deferReply().queue()

        if (!audioManager.isConnected) {
            audioManager.sendingHandler = guildMusicManager.sendHandler
            audioManager.openAudioConnection(voiceChannel)
        }

        scheduler.play()

        event.hook.editOriginalEmbeds(
            EmbedHelper.createEmbed(
                type = EmbedHelper.Type.SUCCESS,
                description = "Lecture de `${scheduler.getCurrentTrack()?.info?.title ?: "Titre inconnu"}` dans <#${voiceChannel.id}>"
            )
        ).queue()

        LogsHelper.success(event, "Bot connected and track played in ${voiceChannel.name}")
    }
}