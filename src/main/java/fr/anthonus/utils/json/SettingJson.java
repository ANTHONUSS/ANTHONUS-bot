package fr.anthonus.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.anthonus.LOGs;
import fr.anthonus.Main;

import java.io.*;

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

    public Settings getSettings() {
        return settings;
    }

    public class Settings {
        public long autoCommandProbability;
        public boolean allowFeur;
        public boolean allowReply;
        public boolean allowModify;
        public int timeBeforeResetQueue;
    }
}
