package fr.anthonus.listeners;

import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.servers.DataBaseManager;
import fr.anthonus.utils.servers.Server;
import fr.anthonus.utils.servers.ServerManager;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildNameChangeListener extends ListenerAdapter {

    @Override
    public void onGuildUpdateName(GuildUpdateNameEvent event) {
        String oldName = event.getOldName();
        String newName = event.getNewName();
        long guildId = event.getGuild().getIdLong();
        Server server = ServerManager.getServer(guildId);
        Server newServer = new Server(guildId, newName, server.isAllowFeur());

        ServerManager.addServer(newServer);
        DataBaseManager.saveServer(newServer);

        LOGs.sendLog("Changement de nom du serveur : " + oldName + " -> " + newName, DefaultLogType.DEFAULT);
    }
}
