package fr.ANTHONUSApps.Commands.SlashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.ANTHONUSApps.Commands.SlashCommands.Command;
import fr.ANTHONUSApps.LOGs;
import fr.ANTHONUSApps.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;

public class RemoveCommand extends Command {
    private final String selectedMusic;

    public RemoveCommand(SlashCommandInteractionEvent event, String selectedMusic){
        super(event);
        this.selectedMusic = selectedMusic;

        LOGs.sendLog("Commande /remove initialisée", "COMMAND");
    }

    @Override
    public void run() {
        ArrayList<AudioTrack> queue = MusicManager.players.get(currentEvent.getGuild().getIdLong()).getQueue();

        if (queue.isEmpty()) {
            currentEvent.reply("La file d'attente est vide").setEphemeral(true).queue();
            return;
        }

        if (MusicManager.players.get(currentEvent.getGuild().getIdLong()).getCurrentTrack() != null && MusicManager.getFileName(MusicManager.players.get(currentEvent.getGuild().getIdLong()).getCurrentTrack().getInfo().uri).equals(selectedMusic)) {
            currentEvent.reply("Impossible de retirer la musique en cours de lecture").setEphemeral(true).queue();
            return;
        }

        for (int i = 0; i < queue.size(); i++) {
            if (MusicManager.getFileName(queue.get(i).getInfo().uri).equals(selectedMusic)) {
                currentEvent.reply("Musique " + MusicManager.getFileName(queue.get(i).getInfo().uri) + "retirée de la playlist").queue();
                queue.remove(i);
                return;
            }
        }

        LOGs.sendLog("Musique " + selectedMusic + " non trouvée dans la playlist", "ERROR");

    }
}
