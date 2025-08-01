package fr.anthonus.utils.settings;

import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SettingsLoader {
    private static String tokenDiscord;
    private static String tokenOpenAI;

    private static String fastTalkPersonnality;

    public static boolean loadEnv() {
        Dotenv dotenv = Dotenv.configure()
                .directory("conf")
                .load();

        //Load ChatGPT api key
        LOGs.sendLog("Chargement du token ChatGPT...", DefaultLogType.FILE_LOADING);
        tokenOpenAI = dotenv.get("OPENAI_TOKEN");
        if (tokenOpenAI == null || tokenOpenAI.isEmpty()) {
            LOGs.sendLog("Clé API OpenAI non trouvé dans le fichier .env", DefaultLogType.ERROR);
            return false;
        } else {
            LOGs.sendLog("Token OpenAI chargé", DefaultLogType.FILE_LOADING);
        }

        //Load discord token
        LOGs.sendLog("Chargement du token Discord...", DefaultLogType.FILE_LOADING);
        tokenDiscord = dotenv.get("DISCORD_TOKEN");
        if (tokenDiscord == null || tokenDiscord.isEmpty()) {
            LOGs.sendLog("Token Discord non trouvé dans le fichier .env", DefaultLogType.ERROR);
            return false;
        } else {
            LOGs.sendLog("Token Discord chargé", DefaultLogType.FILE_LOADING);
        }

        return true;
    }

    public static String getVersion() {
        try (InputStream in = SettingsLoader.class.getClassLoader().getResourceAsStream("version.txt")) {
            if (in != null) {
                return new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
            }
        } catch (IOException ignored) {}
        return "unknown";
    }

    public static String getFastTalkPersonnality(){
        try (InputStream in = SettingsLoader.class.getClassLoader().getResourceAsStream("chatGPT/fastTalkPersonnality.txt")) {
            if (in != null) {
                return new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
            }
        } catch (IOException ignored) {}
        return null;
    }

    public static String getTokenDiscord() {
        return tokenDiscord;
    }

    public static String getTokenOpenAI() {
        return tokenOpenAI;
    }
}
