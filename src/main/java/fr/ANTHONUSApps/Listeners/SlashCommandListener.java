package fr.ANTHONUSApps.Listeners;

import fr.ANTHONUSApps.Commands.SlashCommands.*;
import fr.ANTHONUSApps.LOGs;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) {
            event.reply("Vous ne pouvez pas utiliser les commandes dans les mp du bot.").setEphemeral(true).queue();
            LOGs.sendLog("Commande slash exécutée dans les mp du bot"
                            + "\nUser : @" + event.getUser().getEffectiveName(),
                    LOGs.LogType.ERROR);
            return;
        }


        switch (event.getName()) {
            case "cursed" -> {
                CursedImageCommand cursedImageCommand = new CursedImageCommand(event);
                cursedImageCommand.run();
            }
            case "translate" -> {
                String selectedMode = event.getOption("mode").getAsString();
                String message = event.getOption("message").getAsString();

                TranslateCommand translateCommand = new TranslateCommand(event, selectedMode, message);
                translateCommand.run();
            }
            case "roast" -> {
                String personne = event.getOption("personne").getAsUser().getEffectiveName();
                String contexte = "";
                if (event.getOption("contexte") != null) {
                    contexte = event.getOption("contexte").getAsString();
                }

                RoastCommand roastCommand = new RoastCommand(event, personne, contexte);
                roastCommand.run();
            }
            case "compliment" -> {
                String personne = event.getOption("personne").getAsUser().getEffectiveName();
                String contexte = "";
                if (event.getOption("contexte") != null) {
                    contexte = event.getOption("contexte").getAsString();
                }

                ComplimentCommand complimentCommand = new ComplimentCommand(event, personne, contexte);
                complimentCommand.run();
            }
            case "private-send" -> {
                User personne = event.getOption("personne").getAsUser();
                String message = event.getOption("message").getAsString();

                PrivateSendCommand privateSendCommand = new PrivateSendCommand(event, personne, message);
                privateSendCommand.run();
            }
            case "clear" -> {
                int count = event.getOption("nombre").getAsInt();

                ClearCommand clearCommand = new ClearCommand(event, count);
                clearCommand.run();
            }
        }
    }
}
