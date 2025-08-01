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

    private static String redditClientId;
    private static String redditClientSecret;

    private static String youtubeApiKey;

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

        //load reddit clien id
        LOGs.sendLog("Chargement du client id Reddit...", DefaultLogType.FILE_LOADING);
        redditClientId = dotenv.get("REDDIT_CLIENT_ID");
        if (redditClientId == null || redditClientId.isEmpty()) {
            LOGs.sendLog("Client id Reddit non trouvé dans le fichier .env", DefaultLogType.ERROR);
            return false;
        } else {
            LOGs.sendLog("Client id Reddit chargé", DefaultLogType.FILE_LOADING);
        }

        //load reddit clien secret
        LOGs.sendLog("Chargement du client secret Reddit...", DefaultLogType.FILE_LOADING);
        redditClientSecret = dotenv.get("REDDIT_CLIENT_SECRET");
        if (redditClientSecret == null || redditClientSecret.isEmpty()) {
            LOGs.sendLog("Client secret Reddit non trouvé dans le fichier .env", DefaultLogType.ERROR);
            return false;
        } else {
            LOGs.sendLog("Client secret Reddit chargé", DefaultLogType.FILE_LOADING);
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

    public static String getRedditClientId() {
        return redditClientId;
    }
    public static String getRedditClientSecret() {
        return redditClientSecret;
    }

    public static String getYoutubeApiKey() {
        return youtubeApiKey;
    }

}
