package discord.commands.music

import discord.commands.SubCommand
import helpers.CommandHelper
import helpers.EmbedHelper
import helpers.LogsHelper
import music.PlayerManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class JumpMusicCommand : SubCommand() {
    override val name = "jump"
    override val description = "Joue directement à une musique de la playlist"
    override val options = listOf(
        OptionData(
            OptionType.STRING,
            "music",
            "Nom de la musique à jouer",
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

        if (index != -1) {
            val track = scheduler.playlist[index]

            scheduler.jump(index)
            if (scheduler.isTrackPlaying()) scheduler.play()

            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.SUCCESS,
                    description = "Lecture de `${scheduler.getCurrentTrack()?.info?.title ?: "Titre inconnu"}",
                    thumbnailUrl = track.info.artworkUrl ?: "https://img.youtube.com/vi/${track.identifier}/hqdefault.jpg"
                )
            ).queue()

            LogsHelper.success(event, "Track jumped to ${track.info.title}")

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