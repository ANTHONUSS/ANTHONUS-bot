package fr.anthonus.listeners;

import fr.anthonus.commands.InfoCommand;
import fr.anthonus.commands.settings.AllowFeurCommand;
import fr.anthonus.commands.settings.AllowReplyCommand;
import fr.anthonus.commands.settings.AutoCommandProbabilityCommand;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) {
            event.reply("Impossible d'utiliser de commandes dans les mp du bot.").setEphemeral(true).queue();
            return;
        }

        switch (event.getName()) {
            // USER COMMANDS
            case "info" -> {
                new InfoCommand(event).run();
            }

            //SETTINGS COMMANDS
            case "settings" -> {
                String subCommand = event.getSubcommandName();
                switch (subCommand) {
                    case "autocommandprobability" -> {
                        int probability = event.getOption("probability").getAsInt();
                        new AutoCommandProbabilityCommand(event, probability).run();

                    }
                    case "allowfeur" -> {
                        boolean allowFeur = event.getOption("valeur").getAsBoolean();
                        new AllowFeurCommand(event, allowFeur).run();

                    }
                    case "allowreply" -> {
                        boolean allowReply = event.getOption("valeur").getAsBoolean();
                        new AllowReplyCommand(event, allowReply).run();

                    }
                }
            }
        }

        LOGs.sendLog("Commande terminée", DefaultLogType.COMMAND);
    }
}
