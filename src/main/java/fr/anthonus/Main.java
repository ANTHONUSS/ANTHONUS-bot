package fr.anthonus;

import fr.anthonus.listeners.GuildNameChangeListener;
import fr.anthonus.listeners.SlashCommandListener;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.SettingsLoader;
import fr.anthonus.utils.servers.DataBaseManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class Main {
    public static JDA jda;

    public static void main(String[] args) throws InterruptedException {
        LOGs.sendLog("Chargement des paramètres...", DefaultLogType.LOADING);
        if (!SettingsLoader.loadEnv()) {
            LOGs.sendLog("Erreur lors du chargement des paramètres", DefaultLogType.ERROR);
            return;
        }
        LOGs.sendLog("Paramètres chargés", DefaultLogType.LOADING);

        LOGs.sendLog("Initialisation de la base de données...", DefaultLogType.LOADING);
        DataBaseManager.initDatabase();
        LOGs.sendLog("Base de données initialisée !", DefaultLogType.LOADING);

        LOGs.sendLog("Chargement du bot...", DefaultLogType.LOADING);
        initBot();

        LOGs.sendLog("Chargement de la base de donnée", DefaultLogType.LOADING);
        DataBaseManager.loadDataBase();
        LOGs.sendLog("Base de donnée chargée", DefaultLogType.LOADING);
    }

    private static void initBot() throws InterruptedException {
        //Load the bot
        jda = JDABuilder.createDefault(SettingsLoader.getTokenDiscord())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(
                        new SlashCommandListener(),
                        new GuildNameChangeListener()
                )
                .build();

        jda.awaitReady();
        LOGs.sendLog("Bot démarré", DefaultLogType.LOADING);

        //Load the slash commands
        LOGs.sendLog("Chargement des commandes...", DefaultLogType.LOADING);
        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(
                // USER COMMANDS
                Commands.slash("info", "Affiche les informations du bot")
        );
        commands.queue();
        LOGs.sendLog("Commandes chargées", DefaultLogType.LOADING);
    }

}