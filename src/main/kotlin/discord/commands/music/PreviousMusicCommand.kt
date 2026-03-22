package discord.commands.music

import discord.commands.SubCommand
import helpers.CommandHelper
import helpers.EmbedHelper
import helpers.LogsHelper
import music.PlayerManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class PreviousMusicCommand: SubCommand() {
    override val name = "previous"
    override val description = "Passe à la musique précédente de la playlist"

    override fun executeBody(event: SlashCommandInteractionEvent) {
        if (CommandHelper.isUserInVoiceChannel(event)) {
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

        scheduler.previous()
        if (scheduler.isTrackPlaying()) scheduler.play()

        val track = scheduler.getCurrentTrack()
        if (track == null) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.ERROR,
                    description = "Impossible de récupérer la musique précédente"
                )
            ).setEphemeral(true)
                .queue()

            return
        }

        event.replyEmbeds(
            EmbedHelper.createEmbed(
                type = EmbedHelper.Type.SUCCESS,
                description = "Lecture de `${track.info?.title ?: "Titre inconnu"}`",
                thumbnailUrl = track.info?.artworkUrl ?: "https://img.youtube.com/vi/${track.identifier}/hqdefault.jpg"
            )
        ).queue()

        LogsHelper.success(event, "Previous track played")
    }
}