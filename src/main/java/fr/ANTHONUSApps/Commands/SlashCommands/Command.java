package fr.ANTHONUSApps.Commands.SlashCommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class Command {
    protected SlashCommandInteractionEvent currentEvent;

    protected Command(SlashCommandInteractionEvent event) {
        currentEvent = event;
    }

    protected abstract void run();
}
