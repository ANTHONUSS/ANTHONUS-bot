package fr.ANTHONUSApps.Listeners;

import fr.ANTHONUSApps.LOGs;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw();

        feurReply(message, event);
    }

    private void feurReply(String message, MessageReceivedEvent event) {
        if (message.toLowerCase().matches(".*\\bquoi\\s?\\p{Punct}*$")) {

            if(Math.random() >= 0.5) event.getMessage().reply("feur").queue();
            else event.getMessage().reply("coubeh").queue();

            LOGs.sendLog("feur envoyé"
                            + "\nUser : @" + event.getAuthor().getName()
                            + "\nServeur : " + event.getGuild().getName()
                            + "\nSalon : #" + event.getChannel().getName(),
                    LOGs.LogType.FEUR);
        }
    }
}
