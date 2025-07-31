package fr.anthonus.utils.servers;

import fr.anthonus.Main;

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
    }

    public int getAutoCommandProbability() {
        return autoCommandProbability;
    }
    public void setAutoCommandProbability(int autoCommandProbability) {
        this.autoCommandProbability = autoCommandProbability;
    }

    public boolean isAllowFeur() {
        return allowFeur;
    }
    public void setAllowFeur(boolean allowFeur) {
        this.allowFeur = allowFeur;
    }

    public boolean isAllowReply() {
        return allowReply;
    }
    public void setAllowReply(boolean allowReply) {
        this.allowReply = allowReply;
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
