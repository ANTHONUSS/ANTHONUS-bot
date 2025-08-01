package fr.anthonus.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.track.YoutubeAudioTrack;
import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.music.PlayerManager;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.List;

public class AddCommand extends Command {
    private String url;
    private static final String URL_REGEX =
            "^(https?://)" +                      // schéma http ou https
                    "([\\w\\-]+\\.)+[a-zA-Z]{2,6}" +      // nom de domaine et TLD
                    "(:\\d{1,5})?" +                      // port optionnel
                    "(/[-a-zA-Z0-9@:%_\\+.~#?&/=]*)?$";   // chemin optionnel

    public AddCommand(SlashCommandInteractionEvent event, String url) {
        super(event);
        this.url = url;

        LOGs.sendLog("Commande /add exécutée avec l'URL : " + url, DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        if (!url.matches(URL_REGEX)) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(":warning: URL invalide :warning:");
            embed.setDescription("Veuillez fournir une URL valide pour la musique.");

            embed.setColor(Color.YELLOW);

            currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();

            LOGs.sendLog("URL invalide fournie : " + url, DefaultLogType.WARNING);
            return;
        }

        long guildID = currentEvent.getGuild().getIdLong();
        List<AudioTrack> tracks = ServerManager.getServer(guildID).getPlayerManager().getQueue();

        if (tracks.size() >= 25) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(":warning: File d'attente pleine :warning:");
            embed.setDescription("La file d'attente ne peut pas contenir plus de 25 musiques. Veuillez en retirer certaines avant d'en ajouter de nouvelles.");

            embed.setColor(Color.YELLOW);

            currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();

            LOGs.sendLog("File d'attente pleine, impossible de rajouter de musiques", DefaultLogType.WARNING);
            return;
        }

        currentEvent.deferReply().queue();

        PlayerManager playerManager = ServerManager.getServer(guildID).getPlayerManager();
        AudioPlayerManager audioPlayerManager = playerManager.getManager();

        if (url.contains("youtu.be") || url.contains("youtube.com")) {
            audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager());
            LOGs.sendLog("Enregistrement du gestionnaire de source Youtube", DefaultLogType.COMMAND);
        } else {
            AudioSourceManagers.registerRemoteSources(audioPlayerManager);
            LOGs.sendLog("Enregistrement des sources distantes", DefaultLogType.COMMAND);
        }

        audioPlayerManager.loadItem(url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                LOGs.sendLog("Chargement de la musique : " + track.getInfo().uri, DefaultLogType.COMMAND);
                playerManager.addTrack(track);
                LOGs.sendLog("Musique " + track.getInfo().title + " ajoutée à la file d'attente pour le serveur " + currentEvent.getGuild().getName(),
                        DefaultLogType.COMMAND);

                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle(":minidisc: Musique ajoutée à la file d'attente : `" + track.getInfo().title + "`:minidisc:");

                if (track instanceof YoutubeAudioTrack) {
                    String videoId = track.getIdentifier();
                    String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
                    embed.setThumbnail(thumbnailUrl);
                }

                embed.setColor(Color.GREEN);

                currentEvent.getHook().sendMessageEmbeds(embed.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                LOGs.sendLog("Playlists non pris en charge", DefaultLogType.WARNING);
            }
            @Override
            public void noMatches() {
                LOGs.sendLog("Aucune musique trouvée", DefaultLogType.WARNING);
            }
            @Override
            public void loadFailed(FriendlyException exception) {
                LOGs.sendLog(exception.getMessage(), DefaultLogType.WARNING);
            }
        });

    }
}
