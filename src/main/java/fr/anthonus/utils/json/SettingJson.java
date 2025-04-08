package fr.anthonus.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.anthonus.LOGs;
import fr.anthonus.Main;

import java.io.*;
import java.util.List;

public class SettingJson {
    private final long serverID;
    private Settings settings = new Settings();

    private final String serverName;
    private final File file;

    public SettingJson(long serverID) {
        this.serverID = serverID;

        this.serverName = Main.jda.getGuildById(serverID).getName();
        this.file = new File("Data/Settings/" + serverID + ".json");

        firstLoadJson();
    }

    private void firstLoadJson(){
        if (!file.exists()) {
            LOGs.sendLog("Aucun fichier JSON trouvé pour le serveur " + serverName + ", création d'un nouveau fichier...", "FILE_LOADING");

            try {
                file.createNewFile();
                LOGs.sendLog("Fichier JSON créé avec succès pour le serveur " + serverName, "FILE_LOADING");
            } catch (IOException e) {
                LOGs.sendLog("Erreur lors de la création du fichier JSON : " + e.getMessage(), "ERROR");
                return;
            }

            settings.autoCommandProbability = 1;
            settings.allowFeur = true;
            settings.allowReply = true;
            settings.allowModify = true;
            settings.timeBeforeResetQueue = 4;

            saveJson();
        } else {
            loadJson();
        }
    }

    public void saveJson() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try(FileWriter writer = new FileWriter(file)) {
                gson.toJson(settings, writer);
            }

            LOGs.sendLog("Fichier JSON sauvegardé avec succès pour le serveur " + serverName, "FILE_LOADING");

        } catch (IOException e) {
            LOGs.sendLog("Erreur lors de l'écriture du JSON : " + e.getMessage(), "ERROR");
        }
    }

    public void loadJson(){
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            settings = gson.fromJson(reader, Settings.class);

            LOGs.sendLog("Fichier JSON chargé avec succès pour le serveur " + serverName, "FILE_LOADING");
        } catch (IOException e) {
            LOGs.sendLog("Erreur lors de la lecture du fichier JSON : " + e.getMessage(), "ERROR");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("# Configuration pour le serveur " + serverName + " :\n");

        sb.append("- **Probabilité de commande automatique :** ").append(settings.autoCommandProbability).append("%\n");
        sb.append("- **Autoriser les réponses feur :** ").append(settings.allowFeur ? "oui" : "non").append("\n");
        sb.append("- **Autoriser les réponses par ChatGPT :** ").append(settings.allowReply ? "oui" : "non").append("\n");
        sb.append("- **Autoriser les modifications par ChatGPT :** ").append(settings.allowModify ? "oui" : "non").append("\n");
        sb.append("- **Temps avant réinitialisation de la file d'attente :** ").append(settings.timeBeforeResetQueue).append(" heures\n");

        return sb.toString();
    }

    public List<String> getSettingsList() {
        return List.of(
                "autoCommandProbability",
                "allowFeur",
                "allowReply",
                "allowModify",
                "timeBeforeResetQueue"
        );
    }

    public List<String> getSettingsListPretty() {
        return List.of(
                "Probabilité de commande automatique",
                "Autoriser les réponses feur",
                "Autoriser les réponses par ChatGPT",
                "Autoriser les modifications par ChatGPT",
                "Temps avant réinitialisation de la file d'attente"
        );
    }

    private class Settings {
        public long autoCommandProbability;
        public boolean allowFeur;
        public boolean allowReply;
        public boolean allowModify;
        public int timeBeforeResetQueue;
    }

    public boolean isAllowFeur() {
        return settings.allowFeur;
    }
    public long getAutoCommandProbability() {
        return settings.autoCommandProbability;
    }
    public boolean isAllowReply() {
        return settings.allowReply;
    }
    public boolean isAllowModify() {
        return settings.allowModify;
    }
    public int getTimeBeforeResetQueue() {
        return settings.timeBeforeResetQueue;
    }

    public void setAutoCommandProbability(long autoCommandProbability) {
        settings.autoCommandProbability = autoCommandProbability;
    }
    public void setAllowFeur(boolean allowFeur) {
        settings.allowFeur = allowFeur;
    }
    public void setAllowReply(boolean allowReply) {
        settings.allowReply = allowReply;
    }
    public void setAllowModify(boolean allowModify) {
        settings.allowModify = allowModify;
    }
    public void setTimeBeforeResetQueue(int timeBeforeResetQueue) {
        settings.timeBeforeResetQueue = timeBeforeResetQueue;
    }
}
