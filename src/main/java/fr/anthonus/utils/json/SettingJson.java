package fr.anthonus.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.anthonus.LOGs;

import java.io.*;

public class SettingJson {
    private final long serverID;
    private Settings settings = new Settings();

    public SettingJson(long serverID) {
        this.serverID = serverID;

        firstLoadJson();
        loadJson();
    }

    private void firstLoadJson(){
        File file = new File("Data/Settings/" + serverID + ".json");

        if (!file.exists()) {
            LOGs.sendLog("Aucun fichier JSON trouvé pour le serveur " + serverID + ", création d'un nouveau fichier...", "DEFAULT");

            try {
                file.createNewFile();
            } catch (Exception e) {
                LOGs.sendLog("Erreur lors de la création du fichier JSON : " + e.getMessage(), "ERROR");
                return;
            }

            settings.autoCommandProbability = 1;
            settings.allowFeur = true;
            settings.allowReply = true;
            settings.allowModify = true;
            settings.timeBeforeResetQueue = 4;
        } else {
            try (FileReader reader = new FileReader(file)) {
                Gson gson = new Gson();
                settings = gson.fromJson(reader, Settings.class);
            } catch (Exception e) {
                LOGs.sendLog("Erreur lors du chargement du JSON : " + e.getMessage(), "ERROR");
            }
        }
    }

    public void loadJson() {
        File file = new File("Data/Settings/" + serverID + ".json");

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try(FileWriter writer = new FileWriter(file)) {
                gson.toJson(settings, writer);
            }

            LOGs.sendLog("Fichier JSON chargé avec succès pour le serveur " + serverID, "DEFAULT");

        } catch (Exception e) {
            LOGs.sendLog("Erreur lors de l'écriture du JSON : " + e.getMessage(), "ERROR");
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
