package discord.commands.music

import discord.commands.SubCommand
import helpers.CommandHelper
import helpers.EmbedHelper
import helpers.LogsHelper
import music.PlayerManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import java.awt.Color

class TrackMusicCommand: SubCommand() {
    override val name = "track"
    override val description = "Affiche les informations de la musique sélectionnée"
    override val options = listOf(
        OptionData(
            OptionType.STRING,
            "music",
            "Nom de la musique",
            false,
            true
        )
    )

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

        val musicName = event.getOption("music")?.asString?.takeIf { it.isNotBlank() }
        val track = if (musicName == null) {
            scheduler.getCurrentTrack()
        } else {
            scheduler.playlist.firstOrNull { it.info.title.equals(musicName, ignoreCase = true) }
        }
        if (track == null) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.ERROR,
                    description = "Impossible de récupérer la musique sélectionnée"
                )
            ).setEphemeral(true)
                .queue()

            return
        }

        event.replyEmbeds(
            EmbedHelper.createEmbed(
                title = ":information_source: Informations sur la musique actuellement jouée :information_source:",
                fields = listOf(
                    EmbedHelper.Field("Titre", "`${track.info.title}`"),
                    EmbedHelper.Field("Auteur", "`${track.info.author}`"),
                    if (track.info.isStream) EmbedHelper.Field("Type", "`En direct (stream)`")
                    else EmbedHelper.Field("Durée", "`${track.info.length.toDurationString()}`"),
                    EmbedHelper.Field("Lien", "${track.info.uri}"),
                ),
                thumbnailUrl = track.info?.artworkUrl ?: "https://img.youtube.com/vi/${track.identifier}/hqdefault.jpg",
                color = Color.CYAN
            )
        ).setEphemeral(true)
            .queue()

        LogsHelper.success(event, "Track info displayed")
    }

    private fun Long.toDurationString(): String {
        val totalSeconds = this / 1000
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds / 60) % 60
        val hours = totalSeconds / 3600

        return if (hours > 0)
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        else
            "%02d:%02d".format(minutes, seconds)
    }
}