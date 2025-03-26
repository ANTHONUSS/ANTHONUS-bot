package fr.anthonus.Commands.SlashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.Commands.SlashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public class ListQueueCommand extends Command {

    public ListQueueCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /list-queue initialisée", "COMMAND");
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();

        if (MusicManager.players.get(guildID).getQueue().isEmpty()) {
            currentEvent.reply("## :warning: La queue est vide").setEphemeral(true).queue();
            return;
        }

        List<AudioTrack> queue = MusicManager.players.get(guildID).getQueue();
        StringBuilder queueString = new StringBuilder();
        queueString.append("# Playlist actuelle :\n");
        for (int i = 0; i < queue.size(); i++) {
            queueString.append("- ").append(i + 1).append(". ").append(MusicManager.getFileName(queue.get(i).getInfo().uri)).append("\n");
        }

        currentEvent.reply(queueString.toString()).setEphemeral(true).queue();
    }
}
