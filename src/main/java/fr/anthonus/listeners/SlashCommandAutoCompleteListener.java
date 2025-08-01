package fr.anthonus.listeners;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.utils.servers.ServerManager;
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
        long guildId = event.getGuild().getIdLong();

        switch (focusedOption) {
            case "music" -> {
                List<String> musicsQueue = new ArrayList<>();
                List<AudioTrack> tracks = ServerManager.getServer(guildId).getPlayerManager().getQueue();
                for (AudioTrack track : tracks) musicsQueue.add(track.getInfo().title);

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
