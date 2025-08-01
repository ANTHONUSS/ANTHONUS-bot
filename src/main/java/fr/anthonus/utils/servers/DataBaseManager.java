package fr.anthonus.utils.servers;

import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;

import java.sql.*;

public class DataBaseManager {
    public static final String DB_URL = "jdbc:sqlite:data/database.db";

    public static void initDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String createServersQuery = "CREATE TABLE IF NOT EXISTS Servers (" +
                    "serverId INTEGER PRIMARY KEY," +
                    "serverName STRING NOT NULL," +
                    "allowFeur BOOLEAN NOT NULL" +
                    ");";

            conn.createStatement().execute(createServersQuery);
            LOGs.sendLog("Table Servers initialisée avec succès !", DefaultLogType.DATABASE_LOADING);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement de la base de données : " + e);
        }
    }

    public static void loadDataBase() {
        LOGs.sendLog("Chargement des serveurs", DefaultLogType.DATABASE_LOADING);
        ServerManager.loadServers();
        LOGs.sendLog("Serveurs chargés avec succès !", DefaultLogType.DATABASE_LOADING);
    }

    public static void saveServer(Server server) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String insertQuery = "INSERT OR REPLACE INTO Servers (serverId, serverName, allowFeur) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);

            preparedStatement.setLong(1, server.getGuildId());
            preparedStatement.setString(2, server.getServerName());
            preparedStatement.setBoolean(3, server.isAllowFeur());

            preparedStatement.executeUpdate();

            LOGs.sendLog("Serveur sauvegardé avec succès : " + server.getServerName(), DefaultLogType.DATABASE_LOADING);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du serveur : " + e);
        }
    }

    public static Server loadServer(long guildId) {
        try(Connection conn = DriverManager.getConnection(DB_URL)) {
            String selectQuery = "SELECT * FROM Servers WHERE serverId = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
            preparedStatement.setLong(1, guildId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                LOGs.sendLog("Serveur chargé avec succès : " + resultSet.getString("serverName"), DefaultLogType.DATABASE_LOADING);
                return new Server(
                        resultSet.getLong("serverId"),
                        resultSet.getString("serverName"),
                        resultSet.getBoolean("allowFeur")
                );

            } else {
                LOGs.sendLog("Aucun serveur trouvé avec l'ID : " + guildId, DefaultLogType.DATABASE_LOADING);

                return null; // Server not found
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du serveur : " + e);
        }
    }

}
