package fr.anthonus.listeners;

import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) return;

        String message = event.getMessage().getContentRaw();

        if (message.toLowerCase().matches(".*\\bquoi\\s?\\p{Punct}*$")) {
            handleFeur(event);
        }
    }

    private void handleFeur(MessageReceivedEvent event) {
        long guildId = event.getGuild().getIdLong();

        if (!ServerManager.getServer(guildId).isAllowFeur()) return;

        if (Math.random() >= 0.5) {
            event.getMessage().reply("feur").queue();
        } else {
            event.getMessage().reply("coubeh").queue();
        }

        LOGs.sendLog("Feur envoyé à @" + event.getAuthor().getEffectiveName(), DefaultLogType.AUTOCOMMAND);

    }
}
