package fr.ANTHONUSApps;

import fr.ANTHONUSApps.Listeners.MessageListener;
import fr.ANTHONUSApps.Listeners.SlashCommandListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class Main {
    public static String tokenOpenAI;
    public static double autocommandProb;

    public static void main(String[] args) throws InterruptedException, IOException, NoSuchAlgorithmException {
        Dotenv dotenv = Dotenv.load();

        LOGs.addLogType("COMMAND", 255, 172, 53);
        LOGs.addLogType("AUTOCOMMAND", 193, 92, 255);
        LOGs.addLogType("API", 53, 255, 255);

        //Load configurations
        String autocommandProbString = dotenv.get("AUTOCOMMAND_PROBABILITY");
        if (autocommandProbString == null || autocommandProbString.isEmpty()) {
            LOGs.sendLog("Paramètre \"AUTOCOMMAND_PROBABILITY\" non trouvé dans le fichier .env", "ERROR");
            return;
        } else {
            try {
                autocommandProb = Double.parseDouble(autocommandProbString);
            } catch (NumberFormatException e) {
                LOGs.sendLog("Paramètre \"AUTOCOMMAND_PROBABILITY\" non valide", "ERROR");
                return;
            }
            LOGs.sendLog("Paramètre \"AUTOCOMMAND_PROBABILITY\" chargé", "DEFAULT");
        }

        //Load ChatGPT api key
        tokenOpenAI = dotenv.get("OPENAI_TOKEN");
        if (tokenOpenAI == null || tokenOpenAI.isEmpty()) {
            LOGs.sendLog("Clé API OpenAI non trouvé dans le fichier .env", "ERROR");
            return;
        } else {
            LOGs.sendLog("Token OpenAI chargé", "DEFAULT");
        }

        //Load discord token
        String tokenDiscord = dotenv.get("DISCORD_TOKEN");
        if (tokenDiscord == null || tokenDiscord.isEmpty()) {
            LOGs.sendLog("Token Discord non trouvé dans le fichier .env", "DEFAULT");
            return;
        } else {
            LOGs.sendLog("Token Discord chargé", "DEFAULT");
        }

        //Load the bot
        JDA jda = JDABuilder.createDefault(tokenDiscord)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new MessageListener())
                .addEventListeners(new SlashCommandListener())
                .build();

        jda.awaitReady();
        LOGs.sendLog("Bot démarré", "DEFAULT");

        //Load the slash commands
        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(
                Commands.slash("cursed", "Envoie une image/vidéo (sans son) \"cursed\" depuis différents subreddits"),

                Commands.slash("roast", "Permet de clash la personne mentionnée")
                        .addOption(USER, "personne", "Personne à mentionner", true)
                        .addOption(STRING, "contexte", "Contexte à préciser au bot (facultatif)", false, false),

                Commands.slash("compliment", "Permet de complimenter la personne mentionnée")
                        .addOption(USER, "personne", "Personne à mentionner", true)
                        .addOption(STRING, "contexte", "Contexte à préciser au bot (facultatif)", false, false),

                Commands.slash("private-send", "Envoie un message anonyme à un utilisateur en MP")
                        .addOption(USER, "personne", "Personne à qui envoyer le message", true)
                        .addOption(STRING, "message", "Message à envoyer", true),

                Commands.slash("private-send-file", "Envoie un fichier anonyme à un utilisateur en MP")
                        .addOption(USER, "personne", "Personne à qui envoyer le message", true)
                        .addOption(ATTACHMENT, "fichier", "Image/vidéo/fichier à envoyer", true),

                Commands.slash("clear", "Supprime un certain nombre de messages du salon.")
                        .addOptions(new OptionData(INTEGER, "nombre", "nombre de messages à supprimer", true)
                                .setRequiredRange(1, 100))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)),

                Commands.slash("update-avatar", "Met à jour l'avatar du bot.")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        );
        commands.queue();
    }
}