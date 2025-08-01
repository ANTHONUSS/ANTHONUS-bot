package fr.anthonus;

import fr.anthonus.listeners.GuildNameChangeListener;
import fr.anthonus.listeners.MessageListener;
import fr.anthonus.listeners.SlashCommandListener;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.settings.SettingsLoader;
import fr.anthonus.utils.servers.DataBaseManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

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
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MESSAGES
                )
                .addEventListeners(
                        new SlashCommandListener(),
                        new GuildNameChangeListener(),
                        new MessageListener()
                )
                .build();

        jda.awaitReady();
        LOGs.sendLog("Bot démarré", DefaultLogType.LOADING);

        //Load the slash commands
        LOGs.sendLog("Chargement des commandes...", DefaultLogType.LOADING);
        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(
                // USER COMMANDS
                Commands.slash("info", "Affiche les informations du bot"),

                Commands.slash("send", "Envoie un message dans les mp de quelqu'un du serveur")
                        .addOption(USER, "user", "L'utilisateur à qui envoyer le message", true)
                        .addOption(STRING, "message", "Le message à envoyer")
                        .addOption(ATTACHMENT, "attachment", "Le fichier à envoyer"),

                // SETTINGS COMMANDS
                Commands.slash("settings", "Commande relative aux paramètres du bot")
                        .addSubcommands(
                                new SubcommandData("autocommandprobability", "Change la probabilité de commandes automatiques")
                                        .addOptions(new OptionData(INTEGER, "probability", "La probabilité de commandes automatiques (0-100)", true)
                                                .setMinValue(0)
                                                .setMaxValue(100)
                                        ),

                                new SubcommandData("allowfeur", "Active ou désactive les réponses feur")
                                        .addOption(BOOLEAN, "valeur", "La valeur à définir", true),

                                new SubcommandData("allowreply", "Active ou désactive les réponses par ChatGPT")
                                        .addOption(BOOLEAN, "valeur", "La valeur à définir", true)
                        )
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),

                // ADMIN COMMANDS
                Commands.slash("clear", "Supprime un certain nombre de messages du salon.")
                        .addOptions(new OptionData(INTEGER, "nombre", "nombre de messages à supprimer", true)
                                .setRequiredRange(1, 100))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)),

                // MUSIC COMMANDS
                Commands.slash("music", "Commandes relatives à la musique")
                        .addSubcommands()
        );
        commands.queue();
        LOGs.sendLog("Commandes chargées", DefaultLogType.LOADING);
    }

}