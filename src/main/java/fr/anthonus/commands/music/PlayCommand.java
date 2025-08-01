package fr.anthonus.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.track.YoutubeAudioTrack;
import fr.anthonus.Main;
import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.music.AudioPlayerSendHandler;
import fr.anthonus.utils.music.PlayerManager;
import fr.anthonus.utils.music.TrackScheduler;
import fr.anthonus.utils.servers.Server;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;

public class PlayCommand extends Command {
    public PlayCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /play initialisée", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        if (!isUserInVoiceChannel()) return;
        if (isQueueEmpty()) return;

        currentEvent.deferReply().queue();

        long guildID = currentEvent.getGuild().getIdLong();
        Server server = ServerManager.getServer(guildID);
        PlayerManager playerManager = server.getPlayerManager();

        AudioManager audioManager = currentEvent.getGuild().getAudioManager();
        AudioPlayer audioPlayer = playerManager.getPlayer();

        audioPlayer.addListener(new TrackScheduler(audioPlayer, guildID));

        audioManager.setSendingHandler(new AudioPlayerSendHandler(audioPlayer));

        AudioChannelUnion voiceChannel = currentEvent.getMember().getVoiceState().getChannel();

        AudioTrack audioTrack = playerManager.getQueue().get(0);

        audioManager.openAudioConnection(voiceChannel);
        audioPlayer.setVolume(50);
        audioPlayer.startTrack(audioTrack.makeClone(), false);

        playerManager.setCurrentTrack(audioTrack);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":play_pause: Lecture de la piste dans `" + voiceChannel.getName() + "` :play_pause:");

        embed.setColor(Color.CYAN);

        currentEvent.getHook().sendMessageEmbeds(embed.build()).queue();
    }
}
