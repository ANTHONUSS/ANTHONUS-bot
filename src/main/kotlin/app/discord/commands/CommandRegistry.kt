package app.discord.commands

object CommandRegistry {
    private val commands: List<Command> = listOf(
        InfoCommand()
    )

    fun all(): List<Command> = commands
    fun find(name: String): Command? = commands.firstOrNull { it.name == name }
}