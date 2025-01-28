package fr.ANTHONUSApps.Default.Listeners;

import fr.ANTHONUSApps.Default.Commands.AutoCommands.FeurCommand;
import fr.ANTHONUSApps.Default.Commands.AutoCommands.InteractionCommand;
import fr.ANTHONUSApps.LOGs;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
    private final int prob = 3;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) {
            LOGs.sendLog("Message envoyé dans les mp du bot"
                            + "\nUser : @" + event.getAuthor().getEffectiveName()
                            + "\nMessage : " + event.getMessage().getContentRaw(),
                    LOGs.LogType.NORMAL);
            return;
        }

        String message = event.getMessage().getContentRaw();

        //Verification pour FEUR
        if (message.toLowerCase().matches(".*\\bquoi\\s?\\p{Punct}*$")) {
            FeurCommand feurCommand = new FeurCommand(event);
            feurCommand.run();
        }

        //Verification pour RandomInteraction
        double rand = Math.random() * 100;
        if (rand < prob) {
            InteractionCommand interactionCommand = new InteractionCommand(event);
            interactionCommand.run();
        }
    }
}
