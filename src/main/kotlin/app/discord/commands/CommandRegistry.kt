package app.discord.commands

import app.discord.commands.global.ClearCommand
import app.discord.commands.global.InfoCommand
import app.discord.commands.global.SendCommand

object CommandRegistry {
    private val commands: List<Command> = listOf(
        InfoCommand(),
        ClearCommand(),
        SendCommand()
    )

    fun all(): List<Command> = commands
    fun find(name: String): CommandNode? = commands.firstOrNull { it.name == name }
}