package fr.anthonus.Listeners;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandAutoCompleteListener extends ListenerAdapter {
    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        String focusedOption = event.getFocusedOption().getName();
        String userInput = event.getFocusedOption().getValue();

        switch (focusedOption) {
            case "musique" -> {
                List<String> musics = new ArrayList<>();
                for (AudioTrack track : MusicManager.musicsList) musics.add(MusicManager.getFileName(track.getInfo().uri));

                List<Command.Choice> choices = musics.stream()
                        .filter(name -> name.toLowerCase().contains(userInput.toLowerCase()))
                        .map(name -> new Command.Choice(name, name))
                        .limit(25)
                        .toList();

                event.replyChoices(choices).queue();
            }
            case "playlist-music" -> {
                List<String> musicsQueue = new ArrayList<>();
                for (AudioTrack track : MusicManager.players.get(event.getGuild().getIdLong()).getQueue()) musicsQueue.add(MusicManager.getFileName(track.getInfo().uri));

                List<Command.Choice> choices = musicsQueue.stream()
                        .filter(name -> name.toLowerCase().contains(userInput.toLowerCase()))
                        .map(name -> new Command.Choice(name, name))
                        .limit(25)
                        .toList();

                event.replyChoices(choices).queue();
            }
        }
    }
}
