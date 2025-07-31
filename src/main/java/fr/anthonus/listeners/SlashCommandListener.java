package fr.anthonus.listeners;

import fr.anthonus.commands.InfoCommand;
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
            case "info" -> {
                new InfoCommand(event).run();
            }
        }

        LOGs.sendLog("Commande terminée", DefaultLogType.COMMAND);
    }
}
