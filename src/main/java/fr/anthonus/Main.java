package fr.anthonus;

import fr.anthonus.listeners.*;
import fr.anthonus.utils.Server;
import fr.anthonus.utils.ServerManager;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class Main {
    private static String tokenDiscord;
    public static String tokenOpenAI;
    public static JDA jda;

    public static void main(String[] args) throws InterruptedException {
        // Load logs
        LOGs.addLogType("LOADING", 53, 74, 255);
        LOGs.addLogType("FILE_LOADING", 130, 0, 255);
        LOGs.addLogType("AUTOCOMMAND", 193, 92, 255);
        LOGs.addLogType("COMMAND", 255, 172, 53);
        LOGs.addLogType("API", 53, 255, 255);
        LOGs.addLogType("DOWNLOAD", 141, 255, 252);
        LOGs.addLogType("WARNING", 255, 255, 0);
        LOGs.addLogType("DEBUG", 255, 171, 247);

        LOGs.sendLog("Chargement des musiques...", "LOADING");
        ServerManager.updateMusicsList();
        LOGs.sendLog("Chargement des musiques terminé", "LOADING");

        Dotenv dotenv = Dotenv.configure()
                .directory("conf")
                .load();

        //Load ChatGPT api key
        LOGs.sendLog("Chargement du token ChatGPT...", "LOADING");
        tokenOpenAI = dotenv.get("OPENAI_TOKEN");
        if (tokenOpenAI == null || tokenOpenAI.isEmpty()) {
            LOGs.sendLog("Clé API OpenAI non trouvé dans le fichier .env", "ERROR");
            return;
        } else {
            LOGs.sendLog("Token OpenAI chargé", "LOADING");
        }

        //Load discord token
        LOGs.sendLog("Chargement du token Discord...", "LOADING");
        tokenDiscord = dotenv.get("DISCORD_TOKEN");
        if (tokenDiscord == null || tokenDiscord.isEmpty()) {
            LOGs.sendLog("Token Discord non trouvé dans le fichier .env", "ERROR");
            return;
        } else {
            LOGs.sendLog("Token Discord chargé", "LOADING");
        }

        LOGs.sendLog("Chargement du bot...", "LOADING");
        initBot();
    }

    private static void initBot() throws InterruptedException {
        //Load the bot
        jda = JDABuilder.createDefault(tokenDiscord)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new MessageListener())
                .addEventListeners(new SlashCommandListener())
                .addEventListeners(new SelectionMenuListener())
                .addEventListeners(new ButtonInteractionListener())
                .addEventListeners(new SlashCommandAutoCompleteListener())
                .build();

        jda.awaitReady();
        LOGs.sendLog("Bot démarré", "LOADING");

        for (Guild guild : jda.getGuilds()) {
            ServerManager.servers.put(guild.getIdLong(), new Server(guild.getIdLong()));
            LOGs.sendLog("Nouveau Server initialisé : " + guild.getName() , "LOADING");
        }

        //Load the slash commands
        LOGs.sendLog("Chargement des commandes...", "LOADING");
        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(
                // USER COMMANDS
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


                // ADMIN COMMANDS
                Commands.slash("clear", "Supprime un certain nombre de messages du salon.")
                        .addOptions(new OptionData(INTEGER, "nombre", "nombre de messages à supprimer", true)
                                .setRequiredRange(1, 100))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE)),

                Commands.slash("update-avatar", "Met à jour l'avatar du bot.")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),

                    //CONFIGURATION COMMANDS
                    Commands.slash("reload-config", "Recharge la configuration du serveur")
                                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),

                    Commands.slash("get-config", "Affiche la configuration du serveur")
                                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),

                    Commands.slash("set-config", "Modifie la configuration d'un paramètre du serveur")
                            .addOption(STRING, "paramètre", "Paramètre à modifier", true, true)
                            .addOption(STRING, "valeur", "Valeur du paramètre à modifier", true, true)
                                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),

                // MUSIC COMMANDS
                Commands.slash("add", "Ajoute une musique à la file d'attente")
                        .addOption(STRING, "musique", "Nom de la musique à jouer", true, true),

                Commands.slash("remove", "Enlève une musique de la file d'attente")
                        .addOption(STRING, "queue-music", "Nom de la musique à supprimer", true, true),

                Commands.slash("clear-queue", "Vide la file d'attente"),

                Commands.slash("list", "Liste toutes les musiques disponibles"),

                Commands.slash("list-queue", "Liste toutes les musiques de la file d'attente"),

                Commands.slash("play", "Joue la file d'attente dans l'ordre d'ajout"),

                Commands.slash("stop", "Arrête la musique et déconnecte le bot"),

                Commands.slash("next", "Joue la prochaine musique dans la file d'attente"),

                Commands.slash("previous", "Rejoue la musique précédente dans la file d'attente"),

                Commands.slash("jump", "Joue une musique de la file d'attente")
                        .addOption(STRING, "queue-music", "Position de la musique dans la file d'attente", true, true),

                Commands.slash("loop", "Active ou désactive la répétition de la musique en cours"),

                Commands.slash("shuffle", "Mélange la file d'attente"),

                Commands.slash("download", "Télécharge une musique depuis un URL Youtube")
                        .addOption(STRING, "url", "URL de la vidéo Youtube", true),

                Commands.slash("reload-musics", "Recharge la liste des musiques disponibles")
        );
        commands.queue();
        LOGs.sendLog("Commandes chargées", "LOADING");
    }
}