package fr.ANTHONUSApps;

import fr.ANTHONUSApps.Listeners.MessageListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class Main {
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
                .build();

        jda.awaitReady();
        LOGs.sendLog("Bot démarré", LOGs.LogType.NORMAL);

        //Load the slash commands
        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(
        );
        commands.queue();
    }
}