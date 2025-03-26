package fr.anthonus.Commands.SlashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.Commands.SlashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.Utils.Music.MusicManager;
import fr.anthonus.Utils.Music.MusicPlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.time.Instant;

public class AddCommand extends Command {
    private final String selectedMusic;

    public AddCommand(SlashCommandInteractionEvent event, String selectedMusic) {
        super(event);
        this.selectedMusic = selectedMusic;

        LOGs.sendLog("Commande /add initialisée", "COMMAND");
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();

        MusicPlayerManager playerManager = MusicManager.players.get(guildID);

        for (AudioTrack track : MusicManager.musicsList){
            if (MusicManager.getFileName(track.getInfo().uri).equals(selectedMusic)){
                playerManager.addTrackToQueue(track);

                playerManager.setLastModified(Instant.now());

                currentEvent.reply("## ✅ Musique `" + selectedMusic + "` ajoutée à la queue").queue();
                LOGs.sendLog("Musique " + selectedMusic + " ajoutée à la queue pour le serveur " + currentEvent.getGuild().getName(), "COMMAND");
                return;
            }
        }

        currentEvent.reply("## :x: La musique `" + selectedMusic + "` n'existe pas dans la liste").setEphemeral(true).queue();
        LOGs.sendLog("Musique " + selectedMusic + " non trouvée dans la liste", "ERROR");
    }

}
