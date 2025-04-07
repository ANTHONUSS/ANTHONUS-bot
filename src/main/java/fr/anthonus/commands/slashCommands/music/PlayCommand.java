package fr.anthonus.commands.slashCommands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.utils.music.AudioPlayerSendHandler;
import fr.anthonus.utils.ServerManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;

public class PlayCommand extends Command {
    public PlayCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /play initialisée", "COMMAND");
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();

        AudioPlayer audioPlayer = ServerManager.servers.get(guildID).getAudioPlayer();
        ArrayList<AudioTrack> queue = ServerManager.servers.get(guildID).getQueue();

        if (queue.isEmpty()) {
            currentEvent.reply("## :warning: La file d'attente est vide.").setEphemeral(true).queue();
            return;
        }

        if (audioPlayer.getPlayingTrack() != null) {
            audioPlayer.stopTrack();
        }

        AudioTrack currentTrack = queue.get(0);

        AudioManager audioManager = currentEvent.getGuild().getAudioManager();
        audioManager.setSendingHandler(new AudioPlayerSendHandler(audioPlayer));

        if (!audioManager.isConnected()) {
            Member member = currentEvent.getMember();

            if (member != null && member.getVoiceState() != null && member.getVoiceState().inAudioChannel()) {
                audioManager.openAudioConnection(member.getVoiceState().getChannel());

                currentEvent.reply("## ✅ Playlist jouée dans `" + member.getVoiceState().getChannel().getName() + "`").queue();

                audioPlayer.startTrack(currentTrack.makeClone(), false);

                ServerManager.servers.get(guildID).setCurrentTrack(currentTrack);

            } else {
                currentEvent.reply("## :warning: Vous devez être connecté à un salon vocal pour utiliser cette commande.").setEphemeral(true).queue();
            }
        }


    }
}
