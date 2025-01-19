package fr.ANTHONUSApps.Listeners;

import fr.ANTHONUSApps.Commands.SlashCommands.ComplimentCommand;
import fr.ANTHONUSApps.Commands.SlashCommands.CursedImageCommand;
import fr.ANTHONUSApps.Commands.SlashCommands.RoastCommand;
import fr.ANTHONUSApps.Commands.SlashCommands.TranslateCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
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
            case "roast" -> {
                String personne = event.getOption("personne").getAsUser().getEffectiveName();
                String contexte = "";
                if (event.getOption("contexte") != null) {
                    contexte = event.getOption("contexte").getAsString();
                }

                RoastCommand currentCommand = new RoastCommand(event, personne, contexte);
                currentCommand.run();
            }
            case "compliment" -> {
                String personne = event.getOption("personne").getAsUser().getEffectiveName();
                String contexte = "";
                if (event.getOption("contexte") != null) {
                    contexte = event.getOption("contexte").getAsString();
                }

                ComplimentCommand currentCommand = new ComplimentCommand(event, personne, contexte);
                currentCommand.run();
            }
        }
    }
}
