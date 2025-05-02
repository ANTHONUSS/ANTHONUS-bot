package fr.anthonus.commands.slashCommands.config;

import fr.anthonus.logs.LOGs;
import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.logs.logTypes.CustomLogType;
import fr.anthonus.utils.ServerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class GetConfigCommand extends Command {
    public GetConfigCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /get-config initialisée", CustomLogType.COMMAND);
    }

    @Override
    public void run() {
        String config = ServerManager.servers.get(currentEvent.getGuild().getIdLong()).getSettingJson().toString();

        currentEvent.reply(config).setEphemeral(true).queue();
    }
}
