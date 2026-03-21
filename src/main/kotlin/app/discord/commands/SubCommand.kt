package app.discord.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

abstract class SubCommand: CommandNode {

    fun toSubcommandData(): SubcommandData {
        return SubcommandData(name, description)
            .addOptions(options)
    }

    final override fun execute(event: SlashCommandInteractionEvent) {
        executeBody(event) // useful for consistency between execution of commands and subCommands
    }

    abstract override fun executeBody(event: SlashCommandInteractionEvent)
}