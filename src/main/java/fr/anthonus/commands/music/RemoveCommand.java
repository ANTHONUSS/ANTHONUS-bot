package fr.anthonus.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.music.PlayerManager;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.time.Instant;
import java.util.ArrayList;
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
        long guildID = currentEvent.getGuild().getIdLong();
        PlayerManager playerManager = ServerManager.getServer(guildID).getPlayerManager();

        List<AudioTrack> queue = playerManager.getQueue();

        if (queue.isEmpty()) {
            currentEvent.reply("## :warning: La file d'attente est vide").setEphemeral(true).queue();
            return;
        }

        AudioTrack currentTrack = playerManager.getCurrentTrack();
        if (currentTrack != null && currentTrack.getInfo().title.equalsIgnoreCase(selectedMusic)) {
            currentEvent.reply("## :warning: Impossible de retirer la musique en cours de lecture").setEphemeral(true).queue();
            return;
        }

        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).getInfo().title.equalsIgnoreCase(selectedMusic)) {
                //TODO: faire l'embed
                currentEvent.reply("## ✅ Musique `" + queue.get(i).getInfo().title + "` retirée de la playlist").queue();

                queue.remove(i);

                return;
            }
        }

        //TODO: faire l'embed
        currentEvent.reply("## :x: La musique `" + selectedMusic + "` n'est pas dans la playlist").setEphemeral(true).queue();
        LOGs.sendLog("Musique " + selectedMusic + " non trouvée dans la playlist", DefaultLogType.ERROR);

    }
}
