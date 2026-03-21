package app.discord.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

abstract class Command: CommandNode {
    open val subCommands: List<SubCommand> = emptyList()
    open val defaultPermissions: DefaultMemberPermissions? = null


    fun toSlashCommandData(): SlashCommandData {
        val data = Commands.slash(name, description)

        defaultPermissions?.let { data.setDefaultPermissions(it) }

        if (subCommands.isNotEmpty()) {
            data.addSubcommands(subCommands.map { it.toSubcommandData() })
        } else {
            data.addOptions(options)
        }

        return data
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        val subcommandName = event.subcommandName

        if (subcommandName != null) {
            val target = subCommands.find { it.name == subcommandName }

            if (target != null) {
                target.execute(event)
            } else {
                event.reply("Error : can't find subCommand '$subcommandName'.")
                    .setEphemeral(true)
                    .queue()
            }
        } else {
            executeBody(event)
        }
    }

    open fun executeBody(event: SlashCommandInteractionEvent) {
        event.reply("Cette commande nécessite une sous-commande.").setEphemeral(true).queue()
    }
}