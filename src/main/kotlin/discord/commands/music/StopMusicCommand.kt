package discord.commands.music

import discord.commands.SubCommand
import helpers.CommandHelper
import helpers.EmbedHelper
import helpers.LogsHelper
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class StopMusicCommand: SubCommand() {
    override val name = "stop"
    override val description = "Arrête la musique et déconnecte le bot"

    override fun executeBody(event: SlashCommandInteractionEvent) {
        if (!CommandHelper.isUserInVoiceChannel(event)) return


        if (CommandHelper.isGuildNull(event)) return
        val guild = event.guild!!
        val audioManager = guild.audioManager

        val guildMusicManager = music.PlayerManager.getGuildMusicManager(guild)
        val scheduler = guildMusicManager.scheduler

        if (!scheduler.isTrackPlaying()) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "Aucune musique n'est en cours de lecture"
                )
            ).setEphemeral(true)
                .queue()

            return
        }

        event.deferReply().queue()

        if (audioManager.isConnected) {
            audioManager.closeAudioConnection()
        }

        scheduler.stop()

        event.hook.editOriginalEmbeds(
            EmbedHelper.createEmbed(
                type = EmbedHelper.Type.SUCCESS,
                description = "Le bot a été déconnecté du salon"
            )
        ).queue()

        LogsHelper.success(event, "Bot disconnected and track stopped")
    }
}