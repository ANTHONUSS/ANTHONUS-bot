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

public class AllowReplyCommand extends Command {
    private final boolean allowReply;

    public AllowReplyCommand(SlashCommandInteractionEvent event, boolean allowReply) {
        super(event);
        this.allowReply = allowReply;

        LOGs.sendLog("Commande /settings allowReply initialisée avec la valeur : " + allowReply, DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        long guildId = currentEvent.getGuild().getIdLong();
        Server currentServer = ServerManager.getServer(guildId);

        if (allowReply == currentServer.isAllowReply()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle(":warning: ATTENTION :warning:");
            embedBuilder.setDescription("La possibilité de répondre aux messages est déjà définie à `" + allowReply + "`.");

            embedBuilder.setColor(Color.YELLOW);

            currentEvent.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            LOGs.sendLog("La possibilité de répondre aux messages est déjà définie à " + allowReply + " pour le serveur " + currentServer.getServerName(), DefaultLogType.COMMAND);
            return;
        }

        currentServer.setAllowReply(allowReply);
        DataBaseManager.saveServer(currentServer);

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(":white_check_mark: Succès :white_check_mark:");
        embedBuilder.setDescription("La probabilité de répondre aux messages a été définie à `" + allowReply + "`.");

        embedBuilder.setColor(Color.GREEN);

        currentEvent.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();

        LOGs.sendLog("La probabilité de répondre aux messages a été définie à " + allowReply + " pour le serveur " + currentServer.getServerName(), DefaultLogType.COMMAND);
    }
}
