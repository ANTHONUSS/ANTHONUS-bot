package discord.commands.global

import discord.commands.Command
import helpers.EmbedHelper
import helpers.LogsHelper
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit


class ClearCommand : Command() {
    override val name = "clear"
    override val description = "Supprime un certain nombre de messages du salon."
    override val defaultPermissions = DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)
    override val options = listOf(
        OptionData(OptionType.INTEGER, "nombre", "Nombre de messages (1-100)", true)
            .setRequiredRange(1, 100)
    )

    override fun executeBody(event: SlashCommandInteractionEvent) {
        val amount = event.getOption("nombre")?.asInt ?: 0

        val channel = event.channel
        if (channel !is GuildMessageChannel) {
            event.reply("Cette commande ne peut être utilisée que dans un salon de serveur.")
                .setEphemeral(true)
                .queue()
            return
        }

        channel.history.retrievePast(amount).queue { messages ->
            val twoWeeksAgo = OffsetDateTime.now().minus(14, ChronoUnit.DAYS).plusMinutes(1)

            val (recentMessages, oldMessages) = messages.partition {
                it.timeCreated.isAfter(twoWeeksAgo)
            }

            if (recentMessages.isEmpty()) {
                event.replyEmbeds(EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    title = "Aucun message récent (moins de 2 semaines) à supprimer."
                )).setEphemeral(true)
                    .queue()
                return@queue
            }

            channel.deleteMessages(recentMessages).queue(
                {
                    val msg = if (oldMessages.isNotEmpty()) {
                        "${recentMessages.size} messages supprimés. ${oldMessages.size} messages étaient trop vieux (> 14 jours) pour être supprimés en masse."
                    } else {
                        "${recentMessages.size} messages supprimés."
                    }

                    val embed = EmbedHelper.createEmbed(
                        type = EmbedHelper.Type.SUCCESS,
                        title = "Nettoyage terminé",
                        description = msg
                    )
                    event.replyEmbeds(embed)
                        .setEphemeral(true)
                        .queue()

                    LogsHelper.success(event, "${recentMessages.size} messages deleted")
                },
                { err ->
                    val embed = EmbedHelper.createEmbed(
                        type = EmbedHelper.Type.ERROR,
                        title = "Erreur lors de la suppression",
                        description = "cause : ${err.message}"
                    )
                    event.replyEmbeds(embed)
                        .setEphemeral(true)
                        .queue()

                    LogsHelper.failure(event, "Error deleting messages", err)
                }
            )
        }
    }
}