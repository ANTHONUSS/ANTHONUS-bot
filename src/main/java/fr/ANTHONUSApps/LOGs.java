package fr.ANTHONUSApps;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LOGs {
    public static final String DEFAULT = "\u001B[0m";   //Reset the color
    public static final String RED = "\u001B[31m";      //Errors
    public static final String YELLOW = "\u001B[33m";   //Normal logs
    public static final String GREEN = "\u001B[32m";    //Commands
    public static final String CYAN = "\u001B[36m";     //Auto Commands
    public static final String PURPLE = "\u001B[35m";   //APIRequests
    public static final String BLUE = "\u001B[34m";
    public static final String PINK = "\033[38;5;213m"; //Debug

    public enum LogType {
        ERROR, NORMAL, COMMAND, AUTOCOMMAND, API, DEBUG
    }

    public static void sendLog(String message, LogType logType) {
        String color;
        String enterMessage;
        switch (logType) {
            case ERROR -> {
                color = RED;
                enterMessage = "ERROR ==> ";
                System.err.println(color + enterMessage + message + DEFAULT);
                writeToFile(enterMessage + message);
                return;
            }
            case NORMAL -> {
                color = YELLOW;
                enterMessage = "LOG ==> ";
            }
            case COMMAND -> {
                color = GREEN;
                enterMessage = "COMMAND ==> ";
            }
            case AUTOCOMMAND -> {
                color = CYAN;
                enterMessage = "AUTOCOMMAND ==> ";
            }
            case API -> {
                color = PURPLE;
                enterMessage = "API REQUEST ==> ";
            }
            case DEBUG -> {
                color = PINK;
                enterMessage = "DEBUG ==> ";
            }
            default -> {
                System.out.println(RED + "Mauvais logType entré : " + logType + DEFAULT);
                return;
            }
        }

        System.out.println(color + enterMessage + message + DEFAULT);
        writeToFile(enterMessage + message);
    }

    private static void writeToFile(String log) {
        try {
            File logFile = new File("logs.txt");

            if (!logFile.exists()) {
                if (logFile.createNewFile()) {
                    System.out.println(GREEN + "Fichier logs.txt créé." + DEFAULT);
                } else {
                    System.err.println(RED + "Impossible de créer le fichier logs.txt." + DEFAULT);
                    return;
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(log);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println(RED + "Impossible d'écrire dans le fichier logs : " + e.getMessage() + DEFAULT);
        }
    }
}
