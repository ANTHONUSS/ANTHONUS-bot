package fr.anthonus.listeners;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.utils.ServerManager;
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
                for (AudioTrack track : ServerManager.musicsList) musics.add(ServerManager.getFileName(track.getInfo().uri));

                List<Command.Choice> choices = musics.stream()
                        .filter(name -> name.toLowerCase().contains(userInput.toLowerCase()))
                        .map(name -> new Command.Choice(name, name))
                        .limit(25)
                        .toList();

                event.replyChoices(choices).queue();
            }
            case "queue-music" -> {
                List<String> musicsQueue = new ArrayList<>();
                for (AudioTrack track : ServerManager.servers.get(event.getGuild().getIdLong()).getQueue()) musicsQueue.add(ServerManager.getFileName(track.getInfo().uri));

                List<Command.Choice> choices = musicsQueue.stream()
                        .filter(name -> name.toLowerCase().contains(userInput.toLowerCase()))
                        .map(name -> new Command.Choice(name, name))
                        .limit(25)
                        .toList();

                event.replyChoices(choices).queue();
            }

            case "paramètre" -> {
                List<String> settingsList = ServerManager.servers.get(event.getGuild().getIdLong()).getSettingJson().getSettingsList();
                List<String> prettySettingsList = ServerManager.servers.get(event.getGuild().getIdLong()).getSettingJson().getSettingsListPretty();

                List<Command.Choice> choices = new ArrayList<>();
                for (int i = 0; i < settingsList.size(); i++) {
                    choices.add(new Command.Choice(prettySettingsList.get(i), settingsList.get(i)));
                }

                event.replyChoices(choices).queue();
            }
            case "valeur" -> {
                String setting = event.getOption("paramètre").getAsString();

                List<Command.Choice> choices = new ArrayList<>();

                switch (setting) {
                    case "autoCommandProbability" -> {
                        for (int i = 0; i < 25; i++)
                            choices.add(new Command.Choice(i + " %", String.valueOf(i)));
                    }
                    case "timeBeforeResetQueue" -> {
                        choices.add(new Command.Choice("1 heure", "1"));
                        for (int i = 2; i < 11; i++)
                            choices.add(new Command.Choice(i + " heures", String.valueOf(i)));
                    }
                    case "allowFeur", "allowReply", "allowModify" -> {
                        choices.add(new Command.Choice("oui", "true"));
                        choices.add(new Command.Choice("non", "false"));
                    }
                    default -> {
                        return;
                    }
                }

                event.replyChoices(choices).queue();
            }
        }
    }
}
