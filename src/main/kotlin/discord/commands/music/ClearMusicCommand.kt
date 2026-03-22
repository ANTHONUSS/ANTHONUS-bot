package discord.commands.music

import discord.commands.SubCommand
import helpers.CommandHelper
import helpers.EmbedHelper
import helpers.LogsHelper
import music.PlayerManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class ClearMusicCommand: SubCommand() {
    override val name = "clear"
    override val description = "Vide la playlist"

    override fun executeBody(event: SlashCommandInteractionEvent) {
        if (!CommandHelper.isUserInVoiceChannel(event)) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "Vous devez être dans un salon vocal pour utiliser cette commande"
                )
            ).setEphemeral(true).queue()
            return
        }

        if (CommandHelper.isGuildNull(event)) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.ERROR,
                    description = "Commande exécutée hors-serveur"
                )
            ).setEphemeral(true).queue()
            return
        }
        val guild = event.guild ?: return

        val guildMusicManager = PlayerManager.getGuildMusicManager(guild)
        val scheduler = guildMusicManager.scheduler

        if (CommandHelper.isPlaylistEmpty(scheduler)) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "La playlist est vide"
                )
            ).setEphemeral(true).queue()
            return
        }
        if (scheduler.isTrackPlaying()) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "Une musique est déjà en cours d'exécution"
                )
            ).setEphemeral(true).queue()
            return
        }

        scheduler.clear()

        event.replyEmbeds(
            EmbedHelper.createEmbed(
                type = EmbedHelper.Type.SUCCESS,
                description = "Playlist vidée"
            )
        ).queue()

        LogsHelper.success(event, "Playlist cleared")
    }
}