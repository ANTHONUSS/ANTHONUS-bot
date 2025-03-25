package fr.ANTHONUSApps.Commands.SlashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.ANTHONUSApps.Commands.SlashCommands.Command;
import fr.ANTHONUSApps.LOGs;
import fr.ANTHONUSApps.Utils.Music.MusicManager;
import fr.ANTHONUSApps.Utils.Music.MusicPlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.ArrayList;
import java.util.List;

public class ListQueueCommand extends Command {

    public ListQueueCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /list initialisée", "COMMAND");
    }

    @Override
    public void run() {

        if (MusicManager.players.get(currentEvent.getGuild().getIdLong()).getQueue().isEmpty()) {
            currentEvent.reply("La queue est vide").setEphemeral(true).queue();
            return;
        }

        List<AudioTrack> queue = MusicManager.players.get(currentEvent.getGuild().getIdLong()).getQueue();
        StringBuilder queueString = new StringBuilder();
        queueString.append("# Playlist actuelle :\n");
        for (int i = 0; i < queue.size(); i++) {
            queueString.append("- ").append(i + 1).append(". ").append(MusicManager.getFileName(queue.get(i).getInfo().uri)).append("\n");
        }

        currentEvent.reply(queueString.toString()).setEphemeral(true).queue();
    }
}
