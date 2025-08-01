package fr.anthonus.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.Main;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.music.PlayerManager;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.List;

public abstract class Command {
    protected SlashCommandInteractionEvent currentEvent;

    public Command(SlashCommandInteractionEvent event) {
        currentEvent = event;
    }

    public abstract void run();

    protected boolean isUserInVoiceChannel() {
        AudioChannelUnion voiceChannel = currentEvent.getMember().getVoiceState().getChannel();
        if (voiceChannel == null) {
            LOGs.sendLog("Aucun salon vocal trouvé pour l'utilisateur", DefaultLogType.WARNING);

            EmbedBuilder embed = new EmbedBuilder();

            embed.setTitle(":warning: ATTENTION :warning:");
            embed.setDescription("Vous devez être dans un salon vocal pour utiliser cette commande.");

            embed.setColor(Color.YELLOW);

            currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();

            return false;
        }
        return true;
    }

    protected boolean isQueueEmpty() {
        long guildID = currentEvent.getGuild().getIdLong();
        List<AudioTrack> queue = ServerManager.getServer(guildID).getPlayerManager().getQueue();

        if (queue.isEmpty()) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(":warning: Aucune musique dans la liste :warning:");
            embed.setDescription("La liste des musiques est vide.");

            embed.setColor(Color.YELLOW);

            embed.setFooter("\uD83D\uDCA1 Ajoutez des musiques avec la commande /add", null);

            currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();
            LOGs.sendLog("Aucune musique dans la liste pour le serveur " + currentEvent.getGuild().getName(), DefaultLogType.WARNING);

            return true;
        }

        return false;
    }

    protected boolean isThereACurrentTrack() {
        long guildID = currentEvent.getGuild().getIdLong();
        PlayerManager playerManager = ServerManager.getServer(guildID).getPlayerManager();
        AudioTrack currentTrack = playerManager.getCurrentTrack();

        if (currentTrack == null) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(":warning: ATTENTION :warning:");
            embed.setDescription("Le bot ne joue aucune musique.");

            embed.setColor(Color.YELLOW);

            currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();
            LOGs.sendLog("Le bot ne joue aucune musique", DefaultLogType.WARNING);
            return false;
        }

        return true;
    }
}
