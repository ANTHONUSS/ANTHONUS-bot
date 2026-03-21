package discord.commands

import discord.commands.global.*

object CommandRegistry {
    private val commands: List<Command> = listOf(
        InfoCommand(),
        ClearCommand(),
        SendCommand(),
        CursedCommand()
    )

    fun all(): List<Command> = commands
    fun find(name: String): CommandNode? = commands.firstOrNull { it.name == name }
}