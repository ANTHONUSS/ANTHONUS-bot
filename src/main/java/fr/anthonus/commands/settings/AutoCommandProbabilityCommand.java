package fr.anthonus.commands.settings;

import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.servers.DataBaseManager;
import fr.anthonus.utils.servers.Server;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class AutoCommandProbabilityCommand extends Command {
    private final int probability;

    public AutoCommandProbabilityCommand(SlashCommandInteractionEvent event, int probability) {
        super(event);
        this.probability = probability;

        LOGs.sendLog("Commande /settings autoCommandProbability initialisée avec une probabilité de " + probability + "%", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        long guildId = currentEvent.getGuild().getIdLong();
        Server currentServer = ServerManager.getServer(guildId);

        if (probability == currentServer.getAutoCommandProbability()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle(":warning: ATTENTION :warning:");
            embedBuilder.setDescription("La probabilité de commandes automatiques est déjà définie à `" + probability + "%`.");

            embedBuilder.setColor(Color.YELLOW);

            currentEvent.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            LOGs.sendLog("La probabilité de commandes automatiques est déjà définie à " + probability + "% pour le serveur " + currentServer.getServerName(), DefaultLogType.COMMAND);
            return;
        }

        currentServer.setAutoCommandProbability(probability);
        DataBaseManager.saveServer(currentServer);

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(":white_check_mark: Succès :white_check_mark:");
        embedBuilder.setDescription("La probabilité de commandes automatiques a été définie à `" + probability + "%`.");

        embedBuilder.setColor(Color.GREEN);

        currentEvent.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();

        LOGs.sendLog("La probabilité de commandes automatiques a été définie à " + probability + "% pour le serveur " + currentServer.getServerName(), DefaultLogType.COMMAND);

    }
}
