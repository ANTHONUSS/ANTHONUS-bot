package fr.anthonus.listeners;

import fr.anthonus.commands.autoCommands.FeurCommand;
import fr.anthonus.commands.autoCommands.InteractionCommand;
import fr.anthonus.LOGs;
import fr.anthonus.utils.ServerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
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
        double prob = ServerManager.servers.get(event.getGuild().getIdLong()).getSettingJson().getAutoCommandProbability();
        double rand = Math.random() * 100;
        if (rand < prob) {
            InteractionCommand interactionCommand = new InteractionCommand(event);
            interactionCommand.run();
        }
    }
}
