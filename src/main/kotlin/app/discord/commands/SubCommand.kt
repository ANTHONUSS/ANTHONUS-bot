package app.discord.commands

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

abstract class SubCommand: CommandNode {
    fun toSubcommandData(): SubcommandData {
        return SubcommandData(name, description)
            .addOptions(options)
    }
}