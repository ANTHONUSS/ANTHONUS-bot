package discord.commands.music

import discord.commands.SubCommand
import helpers.CommandHelper
import helpers.EmbedHelper
import helpers.LogsHelper
import music.PlayerManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class ShuffleCommand: SubCommand() {
    override val name = "shuffle"
    override val description = "Mélange la playlist"

    override fun executeBody(event: SlashCommandInteractionEvent) {
        if (!CommandHelper.isUserInVoiceChannel(event)) return

        if (CommandHelper.isGuildNull(event)) return
        // already verified in the statements before
        val guild = event.guild!!

        val guildMusicManager = PlayerManager.getGuildMusicManager(guild)

        val scheduler = guildMusicManager.scheduler

        if (CommandHelper.isPlaylistEmpty(event, scheduler)) return

        scheduler.shuffle()

        event.replyEmbeds(
            EmbedHelper.createEmbed(
                type = EmbedHelper.Type.SUCCESS,
                description = "Playlist mélangée"
            )
        ).queue()

        LogsHelper.success(event, "Playlist shuffled")
    }


}