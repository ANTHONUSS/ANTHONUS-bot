package fr.anthonus.Commands.SlashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.Commands.SlashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.time.Instant;
import java.util.ArrayList;

public class JumpCommand extends Command {
    private String selectedMusic;

    public JumpCommand(SlashCommandInteractionEvent event, String selectedMusic) {
        super(event);

        this.selectedMusic = selectedMusic;

        LOGs.sendLog("Commande /jump initialisée", "COMMAND");
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();
        AudioManager audioManager = currentEvent.getGuild().getAudioManager();
        ArrayList<AudioTrack> queue = MusicManager.players.get(guildID).getQueue();

        if (!audioManager.isConnected()) {
            currentEvent.reply("## :warning: Le bot n'est pas connecté à un salon vocal").setEphemeral(true).queue();
            return;
        }

        if (queue.isEmpty()) {
            currentEvent.reply("## :warning: La file d'attente est vide").setEphemeral(true).queue();
            return;
        }

        for (AudioTrack track : queue) {
            if (MusicManager.getFileName(track.getInfo().uri).equals(selectedMusic)) {
                MusicManager.players.get(guildID).getAudioPlayer().startTrack(track.makeClone(), false);
                MusicManager.players.get(guildID).setCurrentTrack(track);


                currentEvent.reply("## ✅ Musique `" + MusicManager.getFileName(track.getInfo().uri) + "` jouée").queue();
                return;
            }
        }

        currentEvent.reply("## :x: La musique `" + selectedMusic + "` n'est pas dans la playlist").setEphemeral(true).queue();
        LOGs.sendLog("Musique " + selectedMusic + " non trouvée dans la playlist", "ERROR");
    }
}
