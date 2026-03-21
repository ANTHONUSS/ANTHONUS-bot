package app.discord.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

interface Command {
    val name: String
    val description: String
    fun execute(event: SlashCommandInteractionEvent)
}