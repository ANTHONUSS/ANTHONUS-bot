package fr.anthonus.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
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

public class RemoveCommand extends Command {
    private final String selectedMusic;

    public RemoveCommand(SlashCommandInteractionEvent event, String selectedMusic){
        super(event);
        this.selectedMusic = selectedMusic;

        LOGs.sendLog("Commande /remove initialisée", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        if (isQueueEmpty()) return;

        long guildID = currentEvent.getGuild().getIdLong();
        PlayerManager playerManager = ServerManager.getServer(guildID).getPlayerManager();

        List<AudioTrack> queue = playerManager.getQueue();

        AudioTrack currentTrack = playerManager.getCurrentTrack();
        if (currentTrack != null && currentTrack.getInfo().title.equalsIgnoreCase(selectedMusic)) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(":warning: Musique en cours de lecture :warning:");
            embed.setDescription("Impossible de retirer la musique en cours de lecture : `" + currentTrack.getInfo().title + "`");

            embed.setColor(Color.YELLOW);

            currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();
            return;
        }

        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).getInfo().title.equalsIgnoreCase(selectedMusic)) {

                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle(":cd::x: Musique retirée de la file d'attente : `" + queue.get(i).getInfo().title + "`:x::cd:");

                embed.setColor(Color.GREEN);

                if (queue.get(i) instanceof YoutubeAudioTrack) {
                    String videoId = queue.get(i).getIdentifier();
                    String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
                    embed.setThumbnail(thumbnailUrl);
                }

                currentEvent.replyEmbeds(embed.build()).queue();

                queue.remove(i);

                return;
            }
        }


        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":warning: Musique non trouvée :warning:");
        embed.setDescription("La musique `" + selectedMusic + "` n'est pas dans la file d'attente");

        embed.setColor(Color.YELLOW);

        currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();

        LOGs.sendLog("Musique " + selectedMusic + " non trouvée dans la file d'attente", DefaultLogType.WARNING);

    }
}
