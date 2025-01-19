package fr.ANTHONUSApps;

import fr.ANTHONUSApps.Listeners.MessageListener;
import fr.ANTHONUSApps.Listeners.SlashCommandListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class Main {
    //ChatGPT
    public static String tokenIA;

    public static void main(String[] args) throws InterruptedException {
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
                Commands.slash("translate", "Traduis le message en paramètre en mode \"UwU\" ou \"Brainrot\"")
                        .addOptions(new OptionData(STRING, "mode", "Mode de traduction du message")
                                .setRequired(true)
                                .addChoice("UwU", "uwu")
                                .addChoice("Brainrot", "brainrot")
                        )
                        .addOption(STRING, "message", "Message à traduire", true, false),
                Commands.slash("roast", "Permet de clash la personne mentionnée")
                        .addOption(USER, "personne", "Personne à mentionner", true)
                        .addOption(STRING, "contexte", "Contexte à préciser au bot (facultatif)", false, false),
                Commands.slash("compliment", "Permet de complimenter la personne mentionnée")
                        .addOption(USER, "personne", "Personne à mentionner", true)
                        .addOption(STRING, "contexte", "Contexte à préciser au bot (facultatif)", false, false)
        );
        commands.queue();
    }
}