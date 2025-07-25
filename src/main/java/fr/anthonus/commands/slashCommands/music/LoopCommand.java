package fr.anthonus.commands.slashCommands.music;

import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.utils.ServerManager;
import fr.anthonus.utils.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class LoopCommand extends Command {
    public LoopCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /loop initialisée", CustomLogType.COMMAND);
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();

        Server playerManager = ServerManager.servers.get(guildID);
        if (playerManager.isLooping()) {
            playerManager.setLooping(false);
            currentEvent.reply("## :repeat:❌ La playlist ne sera plus lue en boucle.").queue();
        } else {
            playerManager.setLooping(true);
            currentEvent.reply("## :repeat:✅ La playlist sera lue en boucle.").queue();
        }
    }
}
