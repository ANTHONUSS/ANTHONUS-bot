package discord.commands.global

import api.RedditApi
import discord.commands.Command
import helpers.EmbedHelper
import helpers.LogsHelper
import helpers.NetHelper
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.utils.FileUpload

class CursedCommand : Command() {
    override val name = "cursed"
    override val description = "Envoie une image aléatoire du subreddit r/blursed"

    override fun executeBody(event: SlashCommandInteractionEvent) {
        event.deferReply().queue()

        val imageUrl = RedditApi.getRandomImage("blursedimages")
        if (imageUrl == null) {
            event.hook.editOriginalEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.ERROR,
                    title = "une erreur est survenue lors de la récupération de l'URL"
                )
            ).queue()

            LogsHelper.log.error(
                "Error while retrieving reddit image url",
                Exception("Error while retrieving reddit image url")
            )

            return
        }

        val fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1)
        val imageData = FileUpload.fromData(NetHelper.downloadImage(imageUrl), fileName)

        event.hook.sendFiles(imageData)
            .queue(
                {
                    LogsHelper.success(event, "Cursed reddit image sent successfully")
                },
                { err ->
                    LogsHelper.failure(event, "Cursed reddit image not sent", err)
                }
            )
    }
}