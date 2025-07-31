package fr.anthonus.utils.servers;

import fr.anthonus.Main;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;

public class Server {
    private final long guildId;
    private String serverName;
    private int autoCommandProbability;
    private boolean allowFeur;
    private boolean allowReply;

    public Server(long guildId, String serverName, int autoCommandProbability, boolean allowFeur, boolean allowReply) {
        this.guildId = guildId;

        this.serverName = serverName;
        this.autoCommandProbability = autoCommandProbability;
        this.allowFeur = allowFeur;
        this.allowReply = allowReply;
    }

    public Server(long guildId) {
        this.guildId = guildId;
        this.serverName = Main.jda.getGuildById(guildId).getName();

        this.autoCommandProbability = 10;
        this.allowFeur = true;
        this.allowReply = true;
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

    public int getAutoCommandProbability() {
        return autoCommandProbability;
    }
    public void setAutoCommandProbability(int autoCommandProbability) {
        this.autoCommandProbability = autoCommandProbability;
        LOGs.sendLog("La probabilité de commandes automatiques a été mise à jour : " + autoCommandProbability + "%", DefaultLogType.MEMORY_LOADING);
    }

    public boolean isAllowFeur() {
        return allowFeur;
    }
    public void setAllowFeur(boolean allowFeur) {
        this.allowFeur = allowFeur;
        LOGs.sendLog("L'autorisation de Feur a été mise à jour : " + allowFeur, DefaultLogType.MEMORY_LOADING);
    }

    public boolean isAllowReply() {
        return allowReply;
    }
    public void setAllowReply(boolean allowReply) {
        this.allowReply = allowReply;
        LOGs.sendLog("L'autorisation de réponse a été mise à jour : " + allowReply, DefaultLogType.MEMORY_LOADING);
    }

    @Override
    public String toString() {
        return "Server{" +
                "guildId=" + guildId +
                ", serverName='" + serverName + '\'' +
                ", autoCommandProbability=" + autoCommandProbability +
                ", allowFeur=" + allowFeur +
                ", allowReply=" + allowReply +
                '}';
    }

}
