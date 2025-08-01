package fr.anthonus;

import fr.anthonus.listeners.GuildNameChangeListener;
import fr.anthonus.listeners.MessageListener;
import fr.anthonus.listeners.SlashCommandAutoCompleteListener;
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
                        new MessageListener(),
                        new SlashCommandAutoCompleteListener()
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

                Commands.slash("cursed", "Envoie une image bizarre aléatoire venant de reddit"),

                // SETTINGS COMMANDS
                Commands.slash("settings", "Commande relative aux paramètres du bot")
                        .addSubcommands(
                                new SubcommandData("allowfeur", "Active ou désactive les réponses feur")
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
                        .addSubcommands(
                                new SubcommandData("add", "Ajoute une piste audio à la file d'attente")
                                        .addOption(STRING, "url", "URL de la vidéo YouTube", true),

                                new SubcommandData("remove", "Supprime une piste audio de la file d'attente")
                                        .addOption(STRING, "music", "Nom de la musique à supprimer", true, true),

                                new SubcommandData("clear", "Vide la file d'attente"),

                                new SubcommandData("list", "Affiche la liste des pistes audio dans la file d'attente"),

                                new SubcommandData("shuffle", "Mélange la file d'attente"),

                                new SubcommandData("loop", "Active ou désactive la répétition de la file d'attente"),

                                new SubcommandData("track", "Affiche la piste audio en cours de lecture"),

                                new SubcommandData("play", "Joue la première piste audio de la file d'attente"),

                                new SubcommandData("stop", "Arrête la musique et déconnecte le bot"),

                                new SubcommandData("next", "Joue la piste audio suivante dans la file d'attente"),

                                new SubcommandData("previous", "Rejoue la piste audio précédente dans la file d'attente"),

                                new SubcommandData("jump", "Joue une piste audio spécifique de la file d'attente")
                                        .addOption(STRING, "music", "Nom de la musique à jouer", true, true)

                        )
        );
        commands.queue();
        LOGs.sendLog("Commandes chargées", DefaultLogType.LOADING);
    }

}