package app.discord.commands

import app.discord.commands.global.ClearCommand
import app.discord.commands.global.InfoCommand

object CommandRegistry {
    private val commands: List<Command> = listOf(
        InfoCommand(),
        ClearCommand()
    )

    fun all(): List<Command> = commands
    fun find(name: String): CommandNode? = commands.firstOrNull { it.name == name }
}