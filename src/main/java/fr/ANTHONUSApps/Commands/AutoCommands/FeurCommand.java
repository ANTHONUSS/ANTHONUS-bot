package fr.ANTHONUSApps.Commands.AutoCommands;

import fr.ANTHONUSApps.LOGs;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class FeurCommand extends AutoCommand{

    public FeurCommand(MessageReceivedEvent event) {
        super(event);

        LOGs.sendLog("AutoCommande feur intilisaliée", "AUTOCOMMAND");
    }

    @Override
    public void run() {
        if (Math.random() >= 0.5) currentEvent.getMessage().reply("feur").queue();
        else currentEvent.getMessage().reply("coubeh").queue();

        LOGs.sendLog("feur envoyé"
                        + "\nUser : @" + currentEvent.getAuthor().getName()
                        + "\nServeur : " + currentEvent.getGuild().getName()
                        + "\nSalon : #" + currentEvent.getChannel().getName(),
                "AUTOCOMMAND");
    }
}
