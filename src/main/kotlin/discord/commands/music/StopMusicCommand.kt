package discord.commands.music

import discord.commands.SubCommand
import helpers.CommandHelper
import helpers.EmbedHelper
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class StopMusicCommand: SubCommand() {
    override val name = "stop"
    override val description = "Arrête la musique et déconnecte le bot"

    override fun executeBody(event: SlashCommandInteractionEvent) {
        if (!CommandHelper.isUserInVoiceChannel(event)) return
        //TODO: vérifier si une track est en cours de lecture

        event.deferReply().queue()

        if (CommandHelper.isGuildNull(event)) return
        val guild = event.guild!!
        val audioManager = guild.audioManager
        // TODO: arrêter la track
        if (audioManager.isConnected) {
            audioManager.closeAudioConnection()
        }

        event.hook.editOriginalEmbeds(
            EmbedHelper.createEmbed(
                type = EmbedHelper.Type.SUCCESS,
                description = "Le bot a été déconnecté du salon"
            )
        ).queue()
    }
}