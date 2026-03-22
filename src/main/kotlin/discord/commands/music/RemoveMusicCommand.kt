package discord.commands.music

import discord.commands.SubCommand
import helpers.CommandHelper
import helpers.EmbedHelper
import helpers.LogsHelper
import music.PlayerManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class RemoveMusicCommand : SubCommand() {
    override val name = "remove"
    override val description = "Enlève une musique de la playlist"
    override val options = listOf(
        OptionData(
            OptionType.STRING,
            "music",
            "Nom de la musique à supprimer",
            true,
            true
        )
    )

    override fun executeBody(event: SlashCommandInteractionEvent) {
        if (!CommandHelper.isUserInVoiceChannel(event)) return

        val musicName = event.getOption("music")?.asString
        if (musicName.isNullOrEmpty()) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "Aucune musique sélectionnés"
                )
            ).setEphemeral(true)
                .queue()

            return
        }

        if (CommandHelper.isGuildNull(event)) return
        val guild = event.guild ?: return

        val guildMusicManager = PlayerManager.getGuildMusicManager(guild)

        val scheduler = guildMusicManager.scheduler

        if (CommandHelper.isPlaylistEmpty(event, scheduler)) return

        val index = scheduler.playlist.indexOfFirst { it.info.title.equals(musicName, ignoreCase = false) }

        if (scheduler.isTrackPlaying() && index == scheduler.currentIndex) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "Impossible de retirer la musique en cours de lecture"
                )
            ).setEphemeral(true).queue()
            return
        }

        if (index != -1) {
            val trackToDelete = scheduler.playlist[index]

            scheduler.removeAt(index)

            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.SUCCESS,
                    description = "Musique retirée avec succès",
                    thumbnailUrl = trackToDelete.info.artworkUrl ?: "https://img.youtube.com/vi/${trackToDelete.identifier}/hqdefault.jpg"
                )
            ).queue()

            LogsHelper.success(event, "Track removed from playlist")

            return
        }

        event.replyEmbeds(
            EmbedHelper.createEmbed(
                type = EmbedHelper.Type.WARNING,
                description = "Musique non trouvée"
            )
        ).setEphemeral(true)
            .queue()
    }


}