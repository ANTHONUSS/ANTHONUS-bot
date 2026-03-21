package discord.commands

import discord.commands.global.ClearCommand
import discord.commands.global.CursedCommand
import discord.commands.global.InfoCommand
import discord.commands.global.SendCommand
import discord.commands.music.MusicCommand

object CommandRegistry {
    private val commands: List<Command> = listOf(
        InfoCommand(),
        ClearCommand(),
        SendCommand(),
        CursedCommand(),
        MusicCommand()
    )

    fun all(): List<Command> = commands
    fun find(name: String): CommandNode? = commands.firstOrNull { it.name == name }
}