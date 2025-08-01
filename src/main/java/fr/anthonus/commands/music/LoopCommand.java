package fr.anthonus.commands.music;

import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.music.PlayerManager;
import fr.anthonus.utils.servers.DataBaseManager;
import fr.anthonus.utils.servers.Server;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class LoopCommand extends Command {

    public LoopCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /loop exécutée", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();
        Server server = ServerManager.getServer(guildID);

        if (server.isLooping()) {
            server.setLooping(false);
            DataBaseManager.saveServer(server);

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(":repeat::x: Boucle désactivée :x::repeat:");

            embed.setColor(Color.RED);

            currentEvent.replyEmbeds(embed.build()).queue();

            LOGs.sendLog("La boucle a été désactivée", DefaultLogType.COMMAND);
        } else {
            server.setLooping(true);
            DataBaseManager.saveServer(server);

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(":repeat::white_check_mark: Boucle activée :white_check_mark::repeat:");

            embed.setColor(Color.GREEN);

            currentEvent.replyEmbeds(embed.build()).queue();

            LOGs.sendLog("La boucle a été activée", DefaultLogType.COMMAND);
        }
    }
}
