package fr.anthonus.commands.slashCommands.music;

import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.utils.ServerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ClearQueueCommand extends Command {

    public ClearQueueCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /clear-playlist initialisée", "COMMAND");
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();

        if (ServerManager.servers.get(guildID).getCurrentTrack() != null) {

            currentEvent.reply("## ❌ Impossible de vider la playlist pendant la lecture d'une musique").setEphemeral(true).queue();
            return;
        }

        ServerManager.servers.get(guildID).getQueue().clear();

        ServerManager.servers.get(guildID).setLastModified(null);

        currentEvent.reply("## ✅ Playlist vidée").queue();
        LOGs.sendLog("Playlist vidée", "COMMAND");
    }
}
