package discord.commands.global

import discord.commands.Command
import helpers.EmbedHelper
import helpers.LogsHelper
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.utils.FileUpload

class SendCommand : Command() {
    override val name = "send"
    override val description = "Envoie un message dans les mp de quelqu'un du serveur"
    override val options = listOf(
        OptionData(OptionType.USER, "user", "L'utilisateur à qui envoyer le message", true),
        OptionData(OptionType.STRING, "message", "Le message à envoyer"),
        OptionData(OptionType.ATTACHMENT, "attachment", "Le fichier à envoyer")
    )

    override fun executeBody(event: SlashCommandInteractionEvent) {
        val userId = event.getOption("user")?.asUser
        if (userId == null) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.ERROR,
                    description = "Aucun utilisateur n'a été trouvé"
                )
            ).setEphemeral(true)
                .queue()

            LogsHelper.failure(
                event,
                "No user found",
                Error("No user found for ${event.name} command")
            )

            return
        }

        val message = event.getOption("message")?.asString
        val attachment = event.getOption("attachment")?.asAttachment
        if (message.isNullOrEmpty() && attachment == null) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "Vous devez spécifier soit un message, soit un fichier, soit les deux."
                )
            ).setEphemeral(true)
                .queue()

            return
        }

        event.deferReply().setEphemeral(true).queue()

        userId.openPrivateChannel().queue(
            { channel ->
                try {
                    val messageAction = if (attachment != null) {
                        val data = attachment.proxy.download().join()
                        val fileUpload = FileUpload.fromData(data, attachment.fileName)
                        channel.sendFiles(fileUpload).setContent(message ?: "")
                    } else {
                        channel.sendMessage(message!!)
                    }

                    messageAction.queue(
                        {
                            event.hook.editOriginalEmbeds(
                                EmbedHelper.createEmbed(
                                    type = EmbedHelper.Type.SUCCESS,
                                    description = "Message envoyé avec succès à ${userId.name}"
                                )
                            ).queue()
                            LogsHelper.success(event, "Message sent successfully")
                        },
                        { err ->
                            event.hook.editOriginalEmbeds(
                                EmbedHelper.createEmbed(
                                    type = EmbedHelper.Type.ERROR,
                                    description = "Impossible d'envoyer le message, l'utilisateur a probablement bloqué ses MP."
                                )
                            ).queue()
                            LogsHelper.failure(event, "Can't send message to user", err)
                        }
                    )
                }catch (e: Exception) {
                    event.hook.editOriginalEmbeds(
                        EmbedHelper.createEmbed(
                            type = EmbedHelper.Type.ERROR,
                            description = "Erreur lors du téléchargement du fichier"
                        )
                    ).queue()
                    LogsHelper.failure(event, "File download failed", e)
                }

            },
            {
                event.hook.editOriginal("Impossible d'ouvrir le canal privé.").queue()
            }
        )


    }
}