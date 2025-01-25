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
    //ChatGPT
    public static String tokenIA;

    public static void main(String[] args) throws InterruptedException, IOException, NoSuchAlgorithmException {
        Dotenv dotenv = Dotenv.load();

        //Load ChatGPT api key
        tokenIA = dotenv.get("gpt");
        if (tokenIA == null || tokenIA.isEmpty()) {
            LOGs.sendLog("Clé API ChatGPT non trouvé dans le fichier .env", LOGs.LogType.ERROR);
            return;
        } else {
            System.out.println("Token ChatGPT chargé");
        }

        //Load discord token
        String tokenDiscord = dotenv.get("DISCORD_TOKEN");
        if (tokenDiscord == null || tokenDiscord.isEmpty()) {
            LOGs.sendLog("Token Discord non trouvé dans le fichier .env", LOGs.LogType.ERROR);
            return;
        }

        //Load the bot
        JDA jda = JDABuilder.createDefault(tokenDiscord)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new MessageListener())
                .addEventListeners(new SlashCommandListener())
                .build();

        jda.awaitReady();
        LOGs.sendLog("Bot démarré", LOGs.LogType.NORMAL);

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

                Commands.slash("private-send-image", "Envoie un message anonyme à un utilisateur en MP")
                        .addOption(USER, "personne", "Personne à qui envoyer le message", true)
                        .addOption(ATTACHMENT, "fichier", "Image/vidéo à envoyer", true),

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