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

public class AllowFeurCommand extends Command {
    private final boolean allowFeur;

    public AllowFeurCommand(SlashCommandInteractionEvent event, boolean allowFeur) {
        super(event);
        this.allowFeur = allowFeur;

        LOGs.sendLog("Commande /settings allowFeur initialisée avec la valeur : " + allowFeur, DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        long guildId = currentEvent.getGuild().getIdLong();
        Server currentServer = ServerManager.getServer(guildId);

        if (allowFeur == currentServer.isAllowFeur()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle(":warning: ATTENTION :warning:");
            embedBuilder.setDescription("La possibilité de répondre feur aux messages est déjà définie à `" + allowFeur + "`.");

            embedBuilder.setColor(Color.YELLOW);

            currentEvent.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            LOGs.sendLog("La possibilité de répondre feur aux messages est déjà définie à " + allowFeur + " pour le serveur " + currentServer.getServerName(), DefaultLogType.COMMAND);
            return;
        }

        currentServer.setAllowFeur(allowFeur);
        DataBaseManager.saveServer(currentServer);

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(":white_check_mark: Succès :white_check_mark:");
        embedBuilder.setDescription("La probabilité de répondre feur aux messages a été définie à `" + allowFeur + "`.");

        embedBuilder.setColor(Color.GREEN);

        currentEvent.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();

        LOGs.sendLog("La probabilité de répondre feur aux messages a été définie à " + allowFeur + " pour le serveur " + currentServer.getServerName(), DefaultLogType.COMMAND);
    }
}
