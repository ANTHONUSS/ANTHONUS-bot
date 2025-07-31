package fr.anthonus.commands;

import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.SettingsLoader;
import fr.anthonus.utils.servers.Server;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class InfoCommand extends Command {

    public InfoCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /info initialisée", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("ANTHONUS-Bot !!");
        embed.setThumbnail(currentEvent.getJDA().getSelfUser().getAvatarUrl());

        embed.addField("Ping", "`" + currentEvent.getJDA().getGatewayPing() + " ms`", true);
        embed.addField("Version", "`" + SettingsLoader.getVersion() + "`", true);

        StringBuilder credits = new StringBuilder();
        credits.append("Développé par ANTHONUS, (je pense que c'est assez évident)").append("\n");
        credits.append("[Mon site web](https://anthonus.fr)");
        embed.addField("Crédits du bot", credits.toString(), false);

        StringBuilder settings = new StringBuilder();
        long guildId = currentEvent.getGuild().getIdLong();
        Server currentServer = ServerManager.getServer(guildId);
        settings.append("- Probabilité de commandes automatiques : `")
                .append(currentServer.getAutoCommandProbability())
                .append("%`").append("\n");
        settings.append("- Autorise les réponses feur : `")
                .append(currentServer.isAllowFeur() ? "Oui" : "Non").append("`").append("\n");
        settings.append("- Autorise les réponses par ChatGPT : `")
                .append(currentServer.isAllowReply() ? "Oui" : "Non").append("`").append("\n");

        embed.addField("Paramètres du serveur", settings.toString(), false);

        currentEvent.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }
}
