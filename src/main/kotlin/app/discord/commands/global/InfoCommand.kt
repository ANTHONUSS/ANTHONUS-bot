package app.discord.commands.global

import app.discord.commands.Command
import app.discord.commands.CommandNode
import app.helpers.EmbedHelper
import app.helpers.LogsHelper
import app.helpers.SettingsHelper
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class InfoCommand : Command() {
    override val name = "info"
    override val description = "Affiche les informations relatives au bot"

    override fun executeBody(event: SlashCommandInteractionEvent) {
        val embed = EmbedHelper.createEmbed(
            title = "ANTHONUS-bot",
            thumbnailUrl = event.jda.selfUser.avatarUrl,
            fields = listOf(
                EmbedHelper.Field("Ping", "${event.jda.gatewayPing} ms", true),
                EmbedHelper.Field("Version", SettingsHelper.version, true),
                EmbedHelper.Field(
                    "Crédits",
                    "Developpe par ANTHONUS, (je pense que c'est assez evident)\n[Mon site web](https://anthonus.fr)"
                )
            )
        )

        event.replyEmbeds(embed).setEphemeral(true).queue(
            { LogsHelper.success(event, "Info message sent successfully") },
            { err -> LogsHelper.failure(event, "An error occurred while sending info message", err) }
        )
    }
}