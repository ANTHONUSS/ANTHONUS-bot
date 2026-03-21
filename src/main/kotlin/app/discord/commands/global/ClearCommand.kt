package app.discord.commands.global

import app.discord.commands.Command
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData


class ClearCommand: Command() {
    override val name = "clear"
    override val description = "Supprime un certain nombre de messages du salon."
    override val defaultPermissions = DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)
    override val options = listOf(
        OptionData(OptionType.INTEGER, "nombre", "Nombre de messages (1-100)", true)
            .setRequiredRange(1, 100)
    )

    override fun executeBody(event: SlashCommandInteractionEvent) {
        val amount = event.getOption("nombre")?.asInt ?: 0

        //TODO: à terminer
        event.reply("je vais clear des messages ouhouhouuu").queue()

    }
}