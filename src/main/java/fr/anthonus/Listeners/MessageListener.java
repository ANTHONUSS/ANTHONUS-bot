package fr.anthonus.Listeners;

import fr.anthonus.Commands.AutoCommands.FeurCommand;
import fr.anthonus.Commands.AutoCommands.InteractionCommand;
import fr.anthonus.LOGs;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static fr.anthonus.Main.autocommandProb;

public class MessageListener extends ListenerAdapter {
    private final double prob = autocommandProb;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) {
            LOGs.sendLog("Message envoyé dans les mp du bot"
                            + "\nUser : @" + event.getAuthor().getEffectiveName()
                            + "\nMessage : " + event.getMessage().getContentRaw(),
                    "DEFAULT");
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
