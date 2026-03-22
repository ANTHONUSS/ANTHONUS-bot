package discord.commands.music

import discord.commands.SubCommand
import helpers.CommandHelper
import helpers.EmbedHelper
import music.PlayerManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.awt.Color

class ListMusicCommand: SubCommand() {
    override val name = "list"
    override val description = "Affiche la liste des pistes audio dans la file d'attente"

    override fun executeBody(event: SlashCommandInteractionEvent) {
        if (CommandHelper.isUserInVoiceChannel(event)) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "Vous devez être dans un salon vocal pour utiliser cette commande"
                )
            ).setEphemeral(true).queue()
            return
        }

        if (CommandHelper.isGuildNull(event)) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.ERROR,
                    description = "Commande exécutée hors-serveur"
                )
            ).setEphemeral(true).queue()
            return
        }
        val guild = event.guild ?: return

        val guildMusicManager = PlayerManager.getGuildMusicManager(guild)
        val scheduler = guildMusicManager.scheduler

        if (CommandHelper.isPlaylistEmpty(scheduler)) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "La playlist est vide"
                )
            ).setEphemeral(true).queue()
            return
        }

        val playlist = scheduler.playlist

        val trackList = StringBuilder()
        playlist.forEachIndexed { index, track ->
            if (index == scheduler.currentIndex) {
                trackList.append(":cd: ")
            }

            trackList.append("**${index+1}.** `${track.info.title}`\n")
        }
        event.replyEmbeds(
            EmbedHelper.createEmbed(
                title = ":scroll: Playlist du serveur :scroll:",
                description = trackList.toString(),
                color = Color.CYAN
            )
        ).setEphemeral(true)
            .queue()
    }
}