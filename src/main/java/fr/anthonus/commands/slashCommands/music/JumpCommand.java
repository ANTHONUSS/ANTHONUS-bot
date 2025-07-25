package fr.anthonus.commands.slashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.ServerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;

public class JumpCommand extends Command {
    private String selectedMusic;

    public JumpCommand(SlashCommandInteractionEvent event, String selectedMusic) {
        super(event);

        this.selectedMusic = selectedMusic;

        LOGs.sendLog("Commande /jump initialisée", CustomLogType.COMMAND);
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();
        AudioManager audioManager = currentEvent.getGuild().getAudioManager();
        ArrayList<AudioTrack> queue = ServerManager.servers.get(guildID).getQueue();

        if (!audioManager.isConnected()) {
            currentEvent.reply("## :warning: Le bot n'est pas connecté à un salon vocal").setEphemeral(true).queue();
            return;
        }

        if (queue.isEmpty()) {
            currentEvent.reply("## :warning: La file d'attente est vide").setEphemeral(true).queue();
            return;
        }

        for (AudioTrack track : queue) {
            if (ServerManager.getFileName(track.getInfo().uri).equals(selectedMusic)) {
                ServerManager.servers.get(guildID).getAudioPlayer().startTrack(track.makeClone(), false);
                ServerManager.servers.get(guildID).setCurrentTrack(track);


                currentEvent.reply("## ✅ Musique `" + ServerManager.getFileName(track.getInfo().uri) + "` jouée").queue();
                return;
            }
        }

        currentEvent.reply("## :x: La musique `" + selectedMusic + "` n'est pas dans la playlist").setEphemeral(true).queue();
        LOGs.sendLog("Musique " + selectedMusic + " non trouvée dans la playlist", DefaultLogType.ERROR);
    }
}
