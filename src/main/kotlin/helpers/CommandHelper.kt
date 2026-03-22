package helpers

import music.TrackScheduler
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent


object CommandHelper {

    fun isGuildNull(event: SlashCommandInteractionEvent): Boolean {
        return if (event.guild == null) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.ERROR,
                    description = "Commande exécutée hors-serveur"
                )
            ).setEphemeral(true)
                .queue()

            true
        } else {
            false
        }
    }

    fun isUserInVoiceChannel(event: SlashCommandInteractionEvent): Boolean {
        val voiceChannel: AudioChannelUnion? = event.member?.voiceState?.channel

        return if (voiceChannel == null) {

            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "Vous devez être dans un salon vocal pour utiliser cette commande"
                )
            ).setEphemeral(true)
                .queue()

            false
        } else {
            true
        }
    }

    fun isPlaylistEmpty(event: SlashCommandInteractionEvent, scheduler: TrackScheduler): Boolean {
        return if (scheduler.isPlaylistEmpty()) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "La playlist est vide"
                )
            ).setEphemeral(true)
                .queue()

            true
        } else {
            false
        }
    }

    fun isTrackPlaying(event: SlashCommandInteractionEvent, scheduler: TrackScheduler): Boolean {
        return if (scheduler.isTrackPlaying()) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "La musique est déjà en cours d'exécution"
                )
            ).setEphemeral(true)
                .queue()

            true
        } else {
            false
        }
    }

    fun isThereSelectedMusic(event: SlashCommandInteractionEvent, scheduler: TrackScheduler): Boolean {
        val currentTrack = scheduler.getCurrentTrack()
        return if (currentTrack == null) {
            event.replyEmbeds(
                EmbedHelper.createEmbed(
                    type = EmbedHelper.Type.WARNING,
                    description = "Aucune musique n'est sélectionnée"
                )
            ).setEphemeral(true)
                .queue()

            false
        } else {
            true
        }
    }
}