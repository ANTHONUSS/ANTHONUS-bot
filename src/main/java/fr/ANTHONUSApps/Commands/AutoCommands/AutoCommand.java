package fr.ANTHONUSApps.Commands.AutoCommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class AutoCommand {
    protected MessageReceivedEvent currentEvent;

    protected AutoCommand(MessageReceivedEvent event) {
        currentEvent = event;
    }

    protected abstract void run();
}
