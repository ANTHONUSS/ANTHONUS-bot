package fr.anthonus.utils.servers;

import fr.anthonus.Main;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.music.PlayerManager;

public class Server {
    private final long guildId;
    private String serverName;
    private boolean allowFeur;

    private PlayerManager playerManager;
    private boolean looping;

    public Server(long guildId, String serverName, boolean allowFeur, boolean looping) {
        this.guildId = guildId;

        this.serverName = serverName;
        this.allowFeur = allowFeur;
        this.looping = looping;

        this.playerManager = new PlayerManager();
    }

    public Server(long guildId) {
        this.guildId = guildId;
        this.serverName = Main.jda.getGuildById(guildId).getName();

        this.allowFeur = true;
        this.looping = false;

        this.playerManager = new PlayerManager();
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

    public PlayerManager getPlayerManager() {return playerManager;}

    public boolean isLooping() {return looping;}
    public void setLooping(boolean looping) {
        this.looping = looping;
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
