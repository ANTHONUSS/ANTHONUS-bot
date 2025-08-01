package fr.anthonus.utils.servers;

import fr.anthonus.Main;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;

public class Server {
    private final long guildId;
    private String serverName;
    private boolean allowFeur;

    public Server(long guildId, String serverName, boolean allowFeur) {
        this.guildId = guildId;

        this.serverName = serverName;
        this.allowFeur = allowFeur;
    }

    public Server(long guildId) {
        this.guildId = guildId;
        this.serverName = Main.jda.getGuildById(guildId).getName();

        this.allowFeur = true;
    }

    public long getGuildId() {
        return guildId;
    }

    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
        LOGs.sendLog("Le nom du serveur a été mis à jour : " + serverName, DefaultLogType.MEMORY_LOADING);
    }

    public boolean isAllowFeur() {
        return allowFeur;
    }
    public void setAllowFeur(boolean allowFeur) {
        this.allowFeur = allowFeur;
        LOGs.sendLog("L'autorisation de Feur a été mise à jour : " + allowFeur, DefaultLogType.MEMORY_LOADING);
    }


    @Override
    public String toString() {
        return "Server{" +
                "guildId=" + guildId +
                ", serverName='" + serverName + '\'' +
                ", allowFeur=" + allowFeur +
                '}';
    }

}
