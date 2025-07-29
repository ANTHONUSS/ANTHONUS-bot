package fr.anthonus.utils.database;

import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:data/UserData/UserData.db";

    public static void initDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String createServersQuery = "CREATE TABLE IF NOT EXISTS Servers (" +
                    "server_id INTEGER PRIMARY KEY," +
                    "name TEXT NOT NULL" +
                    ");";

            String createSettingsQuery = "CREATE TABLE IF NOT EXISTS Settings (" +
                    "server_id INTEGER PRIMARY KEY," +
                    "autoCommandProbability INTEGER NOT NULL," +
                    "allowFeur BOOLEAN NOT NULL," +
                    "allowReply BOOLEAN NOT NULL," +
                    "timeBeforeResetQueue INTEGER NOT NULL," +
                    "FOREIGN KEY (server_id) REFERENCES Servers(server_id)" +
                    ");";

            conn.createStatement().execute(createServersQuery);
            conn.createStatement().execute(createSettingsQuery);

            LOGs.sendLog("Base de données initialisée avec succès", DefaultLogType.LOADING);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement de la base de données : " + e);
        }
    }

}
