package discord.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.OptionData

interface CommandNode {
    val name: String
    val description: String
    val options: List<OptionData> get() = emptyList()

    fun execute(event: SlashCommandInteractionEvent)
    fun executeBody(event: SlashCommandInteractionEvent)
}