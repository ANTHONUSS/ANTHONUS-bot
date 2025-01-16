package fr.ANTHONUSApps.Listeners;

import fr.ANTHONUSApps.Commands.CursedImageCommand;
import fr.ANTHONUSApps.Commands.TranslateCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()){
            case "cursed" -> {
                CursedImageCommand currentCommand = new CursedImageCommand(event);
                currentCommand.run();
            }
            case "translate" -> {
                String selectedMode = event.getOption("mode").getAsString();
                String message = event.getOption("message").getAsString();

                TranslateCommand currentCommand = new TranslateCommand(event, selectedMode, message);
                currentCommand.run();
            }
        }
    }
}
