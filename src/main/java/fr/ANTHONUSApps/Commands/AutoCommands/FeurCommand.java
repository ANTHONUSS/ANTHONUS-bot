package fr.ANTHONUSApps.Commands.AutoCommands;

import fr.ANTHONUSApps.LOGs;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class FeurCommand {
    private MessageReceivedEvent currentEvent;

    public FeurCommand(MessageReceivedEvent event) {
        this.currentEvent = event;
    }

    public void feurReply() {
        if (Math.random() >= 0.5) currentEvent.getMessage().reply("feur").queue();
        else currentEvent.getMessage().reply("coubeh").queue();

        LOGs.sendLog("feur envoyé"
                        + "\nUser : @" + currentEvent.getAuthor().getName()
                        + "\nServeur : " + currentEvent.getGuild().getName()
                        + "\nSalon : #" + currentEvent.getChannel().getName(),
                LOGs.LogType.FEUR);
    }
}