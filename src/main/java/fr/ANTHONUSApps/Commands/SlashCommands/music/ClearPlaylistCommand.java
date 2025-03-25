package fr.ANTHONUSApps.Commands.SlashCommands.music;

import fr.ANTHONUSApps.Commands.SlashCommands.Command;
import fr.ANTHONUSApps.LOGs;
import fr.ANTHONUSApps.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ClearPlaylistCommand extends Command {

    public ClearPlaylistCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /clear-playlist initialisée", "COMMAND");
    }

    @Override
    public void run() {
        MusicManager.players.get(currentEvent.getGuild().getIdLong()).getQueue().clear();
        currentEvent.reply("## ✅ Playlist vidée").queue();
    }
}
