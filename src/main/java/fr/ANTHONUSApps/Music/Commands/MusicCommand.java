package fr.ANTHONUSApps.Music.Commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class MusicCommand {
    protected SlashCommandInteractionEvent currentEvent;

    protected MusicCommand(SlashCommandInteractionEvent event) {
        currentEvent = event;
    }

    protected abstract void run();
}
