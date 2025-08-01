package fr.anthonus.utils.servers;

import fr.anthonus.Main;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerManager {
    private static final Map<Long, Server> servers = new ConcurrentHashMap<>();

    public static void addServer(Server server) {
        servers.put(server.getGuildId(), server);
        LOGs.sendLog("Nouveau serveur ajouté ou mis à jour en mémoire : " + server.getServerName(), DefaultLogType.MEMORY_LOADING);
    }

    public static void loadServers() {
        for (Guild guild : Main.jda.getGuilds()) {
            long serverId = guild.getIdLong();
            Server server = DataBaseManager.loadServer(serverId);

            if (server == null) {
                server = new Server(serverId);
                servers.put(server.getGuildId(), server);
                DataBaseManager.saveServer(server);
            } else {
                servers.put(serverId, server);
                if (!server.getServerName().equals(guild.getName())) {
                    server.setServerName(guild.getName());
                    DataBaseManager.saveServer(server);
                }
            }
            LOGs.sendLog("Nouveau serveur ajouté en mémoire : " + guild.getName(), DefaultLogType.MEMORY_LOADING);
        }
    }

    public static Map<Long, Server> getServerList() {
        return servers;
    }

    public static Server getServer(long guildId) {
        return servers.get(guildId);
    }
}
