package fr.anthonus.Commands.SlashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.Commands.SlashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.time.Instant;
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
        long guildID = currentEvent.getGuild().getIdLong();

        ArrayList<AudioTrack> queue = MusicManager.players.get(guildID).getQueue();

        if (queue.isEmpty()) {
            currentEvent.reply("## :warning: La file d'attente est vide").setEphemeral(true).queue();
            return;
        }

        if (MusicManager.players.get(guildID).getCurrentTrack() != null && MusicManager.getFileName(MusicManager.players.get(guildID).getCurrentTrack().getInfo().uri).equals(selectedMusic)) {
            currentEvent.reply("## :warning: Impossible de retirer la musique en cours de lecture").setEphemeral(true).queue();
            return;
        }

        for (int i = 0; i < queue.size(); i++) {
            if (MusicManager.getFileName(queue.get(i).getInfo().uri).equals(selectedMusic)) {
                MusicManager.players.get(guildID).setLastModified(Instant.now());

                currentEvent.reply("## ✅ Musique `" + MusicManager.getFileName(queue.get(i).getInfo().uri) + "` retirée de la playlist").queue();

                queue.remove(i);
                return;
            }
        }

        currentEvent.reply("## :x: La musique `" + selectedMusic + "` n'est pas dans la playlist").setEphemeral(true).queue();
        LOGs.sendLog("Musique " + selectedMusic + " non trouvée dans la playlist", "ERROR");

    }
}
