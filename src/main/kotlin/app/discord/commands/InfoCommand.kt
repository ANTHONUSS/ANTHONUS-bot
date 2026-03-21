package app.discord.commands

import app.helpers.EmbedHelper
import app.helpers.LogsHelper.log
import app.helpers.SettingsHelper
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent


class InfoCommand : Command {
    override val name: String = "info"
    override val description: String = "Affiche les informations relatives au bot"

    override fun execute(event: SlashCommandInteractionEvent) {
        val embed = EmbedHelper.createEmbed(
            title = "ANTHONUS-bot",
            thumbnailUrl = event.jda.selfUser.avatarUrl,
            fields = listOf(
                EmbedHelper.Field("Ping", "${event.jda.gatewayPing} ms", true),
                EmbedHelper.Field("Version", SettingsHelper.version, true)
            )
        )

        event.replyEmbeds(embed).setEphemeral(true).queue(
            { log.info("Info message sent successfully") },
            { err -> log.error("An error occurred while sending info message", err) }
        )
    }
}