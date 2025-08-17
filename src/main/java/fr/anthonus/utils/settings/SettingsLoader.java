package fr.anthonus.utils.settings;

import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class SettingsLoader {
    private static String tokenDiscord;

    private static String tokenOpenAI;
    private static String tokenGemini;

    private static String youtubeApiKey;

    private static String sharingDirectory;
    private static String sharingURL;

    public static boolean loadEnv() {
        Dotenv dotenv = Dotenv.configure()
                .directory("conf")
                .load();

        //Load discord token
        LOGs.sendLog("Chargement du token Discord...", DefaultLogType.FILE_LOADING);
        tokenDiscord = dotenv.get("DISCORD_TOKEN");
        if (tokenDiscord == null || tokenDiscord.isEmpty()) {
            LOGs.sendLog("Token Discord non trouvé dans le fichier .env", DefaultLogType.ERROR);
            return false;
        } else {
            LOGs.sendLog("Token Discord chargé", DefaultLogType.FILE_LOADING);
        }

        //Load ChatGPT api key
        LOGs.sendLog("Chargement du token ChatGPT...", DefaultLogType.FILE_LOADING);
        tokenOpenAI = dotenv.get("OPENAI_TOKEN");
        if (tokenOpenAI == null || tokenOpenAI.isEmpty()) {
            LOGs.sendLog("Clé API OpenAI non trouvé dans le fichier .env", DefaultLogType.ERROR);
            return false;
        } else {
            LOGs.sendLog("Token OpenAI chargé", DefaultLogType.FILE_LOADING);
        }

        //Load Gemini api key
        LOGs.sendLog("Chargement du token Gemini...", DefaultLogType.FILE_LOADING);
        tokenGemini = dotenv.get("GEMINI_TOKEN");
        if (tokenGemini == null || tokenGemini.isEmpty()) {
            LOGs.sendLog("Clé API Gemini non trouvé dans le fichier .env", DefaultLogType.ERROR);
            return false;
        } else {
            LOGs.sendLog("Token Gemini chargé", DefaultLogType.FILE_LOADING);
        }

        //load Youtube API Key
        LOGs.sendLog("Chargement du token Youtube...", DefaultLogType.FILE_LOADING);
        youtubeApiKey = dotenv.get("TOUTUBE_API_KEY");
        if (youtubeApiKey == null || youtubeApiKey.isEmpty()) {
            LOGs.sendLog("Token Youtube non trouvé dans le fichier .env", DefaultLogType.ERROR);
            return false;
        } else {
            LOGs.sendLog("Token Youtube chargé", DefaultLogType.FILE_LOADING);
        }

        //load nas sharing url Key
        LOGs.sendLog("Chargement du chemin de partage...", DefaultLogType.FILE_LOADING);
        sharingDirectory = dotenv.get("SHARING_DIRECTORY");
        if (sharingDirectory == null || sharingDirectory.isEmpty()) {
            LOGs.sendLog("Chemin de partage non trouvé dans le fichier .env", DefaultLogType.ERROR);
            return false;
        } else {
            File chemin = new File(sharingDirectory);
            if (!chemin.exists() || !chemin.isDirectory()) {
                LOGs.sendLog("Le chemin de partage n'existe pas : " + sharingDirectory, DefaultLogType.ERROR);
                return false;
            }
            LOGs.sendLog("Chemin de partage chargé", DefaultLogType.FILE_LOADING);
        }

        //load nas sharing url Key
        LOGs.sendLog("Chargement du lien de partage...", DefaultLogType.FILE_LOADING);
        sharingURL = dotenv.get("SHARING_URL");
        if (sharingURL == null || sharingURL.isEmpty()) {
            LOGs.sendLog("Chemin de partage non trouvé dans le fichier .env", DefaultLogType.ERROR);
            return false;
        } else {
            LOGs.sendLog("Chemin de partage chargé", DefaultLogType.FILE_LOADING);
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

    public static String getFastTalkPersonnality() {
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
    public static String getTokenGemini() {
        return tokenGemini;
    }

    public static String getYoutubeApiKey() {
        return youtubeApiKey;
    }

    public static String getSharingDirectory() {
        return sharingDirectory;
    }
    public static String getSharingURL() {
        return sharingURL;
    }

}
