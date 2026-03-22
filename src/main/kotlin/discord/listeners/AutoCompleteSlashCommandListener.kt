package discord.listeners

import helpers.LogsHelper
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.Command

class AutoCompleteSlashCommandListener: ListenerAdapter() {

    override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        val focusedOption = event.focusedOption.name

        when (focusedOption) {
            "music" -> {
                handleMusicChoice(event)
            }
        }
    }

    private fun handleMusicChoice(event: CommandAutoCompleteInteractionEvent) {
        val guild = event.guild ?: return
        val musicManager = music.PlayerManager.getGuildMusicManager(guild)
        val tracks = musicManager.scheduler.playlist

        val userInput = event.focusedOption.value
        val musicsQueue = tracks.map { it.info.title }
        val choices = musicsQueue
            .filter { it.contains(userInput, ignoreCase = true) }
            .distinct()
            .take(25)
            .map { Command.Choice(it, it) }

        event.replyChoices(choices).queue(
            {},
            { err -> LogsHelper.failure(event, "Error while sending choices", err) }
        )

    }
}