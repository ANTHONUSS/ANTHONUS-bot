package discord.commands

import helpers.EmbedHelper
import helpers.LogsHelper
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

abstract class Command : CommandNode {
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

    final override fun execute(event: SlashCommandInteractionEvent) {
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

    override fun executeBody(event: SlashCommandInteractionEvent) {
        event.replyEmbeds(
            EmbedHelper.createEmbed(
                type = EmbedHelper.Type.ERROR,
                title = "Commande non-implémenté"
            )
        ).setEphemeral(true)
            .queue()

        LogsHelper.log.error(
            "Non-implemented function ran",
            NotImplementedError("The command ${this.name} Does not have an override of executeBody")
        )
    }
}