package fr.ANTHONUSApps.Listeners;

import fr.ANTHONUSApps.Commands.CursedImageCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()){
            case "cursed-image" -> {
                CursedImageCommand currentCommand = new CursedImageCommand(event);
                currentCommand.run();
            }
        }
    }
}
