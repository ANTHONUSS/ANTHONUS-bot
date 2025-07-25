package fr.anthonus.commands.slashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.ServerManager;
import fr.anthonus.utils.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.time.Instant;

public class AddCommand extends Command {
    private final String selectedMusic;

    public AddCommand(SlashCommandInteractionEvent event, String selectedMusic) {
        super(event);
        this.selectedMusic = selectedMusic;

        LOGs.sendLog("Commande /add initialisée", CustomLogType.COMMAND);
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();

        Server playerManager = ServerManager.servers.get(guildID);

        for (AudioTrack track : ServerManager.musicsList){
            if (ServerManager.getFileName(track.getInfo().uri).equals(selectedMusic)){
                playerManager.addTrackToQueue(track);

                playerManager.setLastModified(Instant.now());

                currentEvent.reply("## ✅ Musique `" + selectedMusic + "` ajoutée à la queue").queue();
                LOGs.sendLog("Musique " + selectedMusic + " ajoutée à la queue pour le serveur " + currentEvent.getGuild().getName(), CustomLogType.COMMAND);
                return;
            }
        }

        currentEvent.reply("## :x: La musique `" + selectedMusic + "` n'existe pas dans la liste").setEphemeral(true).queue();
        LOGs.sendLog("Musique " + selectedMusic + " non trouvée dans la liste", DefaultLogType.ERROR);
    }

}
