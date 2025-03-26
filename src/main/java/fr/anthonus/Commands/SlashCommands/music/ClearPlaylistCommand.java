package fr.anthonus.Commands.SlashCommands.music;

import fr.anthonus.Commands.SlashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ClearPlaylistCommand extends Command {

    public ClearPlaylistCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /clear-playlist initialisée", "COMMAND");
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();

        MusicManager.players.get(guildID).getQueue().clear();

        currentEvent.reply("## ✅ Playlist vidée").queue();
        LOGs.sendLog("Playlist vidée", "COMMAND");
    }
}
