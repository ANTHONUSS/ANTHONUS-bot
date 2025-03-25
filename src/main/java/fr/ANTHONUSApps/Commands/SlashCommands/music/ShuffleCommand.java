package fr.ANTHONUSApps.Commands.SlashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.ANTHONUSApps.Commands.SlashCommands.Command;
import fr.ANTHONUSApps.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;

public class ShuffleCommand extends Command {
    public ShuffleCommand(SlashCommandInteractionEvent event) {
        super(event);
    }

    @Override
    public void run() {
        ArrayList<AudioTrack> queue = MusicManager.players.get(currentEvent.getGuild().getIdLong()).getQueue();

        if (queue.isEmpty()) {
            currentEvent.reply("La file d'attente est vide").setEphemeral(true).queue();
            return;
        }

        for (int i = 0; i < queue.size(); i++) {
            int randomIndex = (int) (Math.random() * queue.size());
            AudioTrack temp = queue.get(i);
            queue.set(i, queue.get(randomIndex));
            queue.set(randomIndex, temp);
        }

        currentEvent.reply("Playlist mélangée").queue();


    }
}
