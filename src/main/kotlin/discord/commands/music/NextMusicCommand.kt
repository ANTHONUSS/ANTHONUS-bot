package discord.commands.music

import discord.commands.SubCommand
import helpers.CommandHelper
import helpers.EmbedHelper
import helpers.LogsHelper
import music.PlayerManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class NextMusicCommand : SubCommand() {
    override val name = "next"
    override val description = "Passe à la prochaine musique de la playlist"

    override fun executeBody(event: SlashCommandInteractionEvent) {
        if (!CommandHelper.isUserInVoiceChannel(event)) return

        if (CommandHelper.isGuildNull(event)) return
        // already verified in the statements before
        val guild = event.guild!!

        val guildMusicManager = PlayerManager.getGuildMusicManager(guild)

        val scheduler = guildMusicManager.scheduler

        if (CommandHelper.isPlaylistEmpty(event, scheduler)) return

        scheduler.next()
        if (scheduler.isTrackPlaying()) scheduler.play()

        val track = scheduler.getCurrentTrack()
        event.replyEmbeds(
            EmbedHelper.createEmbed(
                type = EmbedHelper.Type.SUCCESS,
                description = "Lecture de `${scheduler.getCurrentTrack()?.info?.title ?: "Titre inconnu"}`",
                thumbnailUrl = track?.info?.artworkUrl ?: "https://img.youtube.com/vi/${track?.identifier}/hqdefault.jpg"
            )
        ).queue()

        LogsHelper.success(event, "Next track played")
    }
}