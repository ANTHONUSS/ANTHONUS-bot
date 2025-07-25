package fr.anthonus.commands.slashCommands.config;

import fr.anthonus.logs.LOGs;
import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.utils.ServerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ReloadConfigCommand extends Command {
    public ReloadConfigCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /reload-config initialisée", CustomLogType.COMMAND);
    }

    @Override
    public void run() {
        ServerManager.servers.get(currentEvent.getGuild().getIdLong()).getSettingJson().loadJson();

        currentEvent.reply("Configuration rechargée avec succès !").setEphemeral(true).queue();

        LOGs.sendLog("Configuration rechargée pour le serveur : " + currentEvent.getGuild().getName(), CustomLogType.COMMAND);
    }
}
