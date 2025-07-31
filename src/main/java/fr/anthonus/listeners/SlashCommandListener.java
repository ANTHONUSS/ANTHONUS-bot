package fr.anthonus.listeners;

import fr.anthonus.commands.user.InfoCommand;
import fr.anthonus.commands.admin.ClearCommand;
import fr.anthonus.commands.settings.AllowFeurCommand;
import fr.anthonus.commands.settings.AllowReplyCommand;
import fr.anthonus.commands.settings.AutoCommandProbabilityCommand;
import fr.anthonus.commands.user.SendCommand;
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

            // ADMIN COMMANDS
            case "clear" -> {
                int count = event.getOption("nombre").getAsInt();

                new ClearCommand(event, count).run();
            }
        }

        LOGs.sendLog("Commande terminée", DefaultLogType.COMMAND);
    }
}
