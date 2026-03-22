package discord.commands.music

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import discord.commands.SubCommand
import helpers.CommandHelper
import helpers.EmbedHelper
import helpers.LogsHelper
import music.PlayerManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class AddMusicCommand : SubCommand() {
    override val name = "add"
    override val description = "Ajoute une piste audio à la file d'attente"
    override val options = listOf(
        OptionData(OptionType.STRING, "url", "URL Youtube de la vidéo", true)
    )

    override fun executeBody(event: SlashCommandInteractionEvent) {
        if (!CommandHelper.isUserInVoiceChannel(event)) return

        val url = event.getOption("url")?.asString
        if (url.isNullOrEmpty()) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.ERROR,
                    description = "Aucun url passé en paramètre"
                )
            ).setEphemeral(true)
                .queue()

            LogsHelper.failure(
                event,
                "No url in parameters",
                Error("no url for ${event.subcommandName} command")
            )

            return
        }

        event.deferReply().queue()

        if (CommandHelper.isGuildNull(event)) return
        val guild = event.guild ?: return
        val guildMusicManager = PlayerManager.getGuildMusicManager(guild)

        PlayerManager.playerManager.loadItemOrdered(
            guildMusicManager,
            url,
            object : AudioLoadResultHandler {
                override fun trackLoaded(track: AudioTrack?) {
                    if (track == null) {
                        event.hook.editOriginalEmbeds(
                            EmbedHelper.createEmbed(
                                type = EmbedHelper.Type.ERROR,
                                description = "Impossible de charger la musique"
                            )
                        ).queue()

                    } else {
                        guildMusicManager.scheduler.add(track)

                        event.hook.editOriginalEmbeds(
                            EmbedHelper.createEmbed(
                                type = EmbedHelper.Type.SUCCESS,
                                description = "Musique chargée et ajoutée à la playlist",
                                thumbnailUrl = track.info.artworkUrl ?: "https://img.youtube.com/vi/${track.identifier}/hqdefault.jpg",
                                fields = listOf(
                                    EmbedHelper.Field("Titre", track.info.title, true),
                                    EmbedHelper.Field("Auteur", track.info.author, true),
                                    EmbedHelper.Field("Lien", track.info.uri, true)
                                )
                            )
                        ).queue()

                        LogsHelper.success(event, "Track loaded : ${track.info.title} | ${track.info.uri}")
                    }
                }

                override fun playlistLoaded(playlist: AudioPlaylist?) {
                    if (playlist == null) {
                        event.hook.editOriginalEmbeds(
                            EmbedHelper.createEmbed(
                                type = EmbedHelper.Type.ERROR,
                                description = "Impossible de charger la playlist"
                            )
                        ).queue()

                        return
                    }

                    val track = playlist.selectedTrack ?: playlist.tracks.firstOrNull()
                    trackLoaded(track)
                }

                override fun noMatches() {
                    event.hook.editOriginalEmbeds(
                        EmbedHelper.createEmbed(
                            type = EmbedHelper.Type.WARNING,
                            description = "Aucune musique n'a été trouvée pour ce lien (lien non supporté ou invalide)"
                        )
                    ).queue()
                }

                override fun loadFailed(e: FriendlyException) {
                    event.hook.editOriginalEmbeds(
                        EmbedHelper.createEmbed(
                            type = EmbedHelper.Type.ERROR,
                            description = "Une erreur est survenue lors du chargement de la musique"
                        )
                    ).queue()

                    LogsHelper.failure(event, "Error while loading music", e)
                }

            })

    }


}