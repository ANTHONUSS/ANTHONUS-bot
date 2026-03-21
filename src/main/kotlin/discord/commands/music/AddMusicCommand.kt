package discord.commands.music

import discord.commands.SubCommand
import helpers.EmbedHelper
import helpers.LogsHelper
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class AddMusicCommand: SubCommand() {
    override val name = "add"
    override val description = "Ajoute une piste audio à la file d'attente"
    override val options = listOf(
        OptionData(OptionType.STRING, "url", "URL Youtube de la vidéo", true)
    )

    override fun executeBody(event: SlashCommandInteractionEvent) {
        val url = event.getOption("url")?.asString
        if (url.isNullOrEmpty()) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.ERROR,
                    description = "Aucun url passé en paramètre"
                )
            ).setEphemeral(true)
                .queue()

            LogsHelper.log.error(
                "No url in parameters",
                Error("no url for ${event.subcommandName} command")
            )

            return
        }

        val youtubeRegex = "(http:|https:)?//(www\\.)?(youtube.com|youtu.be)/(watch)?(\\?v=)?(\\S+)?".toRegex()
        if (!youtubeRegex.matches(url)) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "L'URL passé n'est pas un lien Youtube"
                )
            ).setEphemeral(true)
                .queue()

            return
        }

        //TODO: Implémenter système d'ajout à la playlist

    }


}