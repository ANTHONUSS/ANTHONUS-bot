package fr.anthonus.listeners;

import fr.anthonus.commands.admin.*;
import fr.anthonus.commands.music.*;
import fr.anthonus.commands.settings.*;
import fr.anthonus.commands.user.*;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;

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
            case "send" -> {
                User userID = event.getOption("user").getAsUser();
                OptionMapping messageOption = event.getOption("message");
                String message = messageOption != null ? messageOption.getAsString() : null;
                OptionMapping attachmentOption = event.getOption("attachment");
                Message.Attachment attachment = attachmentOption != null ? attachmentOption.getAsAttachment() : null;

                if (messageOption == null && attachment == null) {
                    EmbedBuilder embed = new EmbedBuilder();

                    embed.setTitle(":warning: ATTENTION :warning:");
                    embed.setDescription("Vous devez spécifier soit un message, soit un fichier, soit les deux.");

                    embed.setColor(Color.YELLOW);

                    event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                    return;
                }

                new SendCommand(event, userID, message, attachment).run();
            }
            case "cursed" -> {
                new CursedCommand(event).run();
            }

            //SETTINGS COMMANDS
            case "settings" -> {
                String subCommand = event.getSubcommandName();
                switch (subCommand) {
                    case "allowfeur" -> {
                        boolean allowFeur = event.getOption("valeur").getAsBoolean();
                        new AllowFeurCommand(event, allowFeur).run();

                    }
                }
            }

            // ADMIN COMMANDS
            case "clear" -> {
                int count = event.getOption("nombre").getAsInt();

                new ClearCommand(event, count).run();
            }

            // MUSIC COMMANDS
            case "music" -> {
                String subCommand = event.getSubcommandName();
                switch (subCommand) {
                    case "add" -> {
                        String url = event.getOption("url").getAsString();
                        new AddCommand(event, url).run();
                    }
                    case "remove" -> {
                        String musicName = event.getOption("music").getAsString();
                        new RemoveCommand(event, musicName).run();
                    }
                    case "list" -> {
                        new ListCommand(event).run();
                    }
                }
            }
        }

        LOGs.sendLog("Commande terminée", DefaultLogType.COMMAND);
    }
}
