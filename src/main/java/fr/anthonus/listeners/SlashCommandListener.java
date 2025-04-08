package fr.anthonus.listeners;

import fr.anthonus.commands.slashCommands.admin.ClearCommand;
import fr.anthonus.commands.slashCommands.admin.UpdateAvatarCommand;
import fr.anthonus.commands.slashCommands.config.GetConfigCommand;
import fr.anthonus.commands.slashCommands.config.ReloadConfigCommand;
import fr.anthonus.commands.slashCommands.config.SetConfigCommand;
import fr.anthonus.commands.slashCommands.music.*;
import fr.anthonus.commands.slashCommands.normal.*;
import fr.anthonus.LOGs;
import fr.anthonus.utils.ServerManager;
import fr.anthonus.utils.Server;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.time.Duration;
import java.time.Instant;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) {
            event.reply("Vous ne pouvez pas utiliser les commandes dans les mp du bot.").setEphemeral(true).queue();
            LOGs.sendLog("Commande slash exécutée dans les mp du bot"
                            + "\nUser : @" + event.getUser().getEffectiveName(),
                    "ERROR");
            return;
        }

        long guildId = event.getGuild().getIdLong();
        Server player = ServerManager.servers.get(guildId);
        if (player.getLastModified() != null) {
            Instant lastModified = ServerManager.servers.get(guildId).getLastModified();
            if (Duration.between(lastModified, Instant.now()).toHours() >= ServerManager.servers.get(guildId).getSettingJson().getTimeBeforeResetQueue()) {
                LOGs.sendLog("Réinitialisation automatique de la file d'attente du bot", "DEFAULT");
                player.getQueue().clear();
                player.setLastModified(null);
                player.setLooping(false);
            }
        }

        switch (event.getName()) {
            // NORMAL COMMANDS
            case "cursed" -> {
                CursedImageCommand cursedImageCommand = new CursedImageCommand(event);
                cursedImageCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "roast" -> {
                String personne = event.getOption("personne").getAsUser().getEffectiveName();
                String contexte = "";
                if (event.getOption("contexte") != null) {
                    contexte = event.getOption("contexte").getAsString();
                }

                RoastCommand roastCommand = new RoastCommand(event, personne, contexte);
                roastCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "compliment" -> {
                String personne = event.getOption("personne").getAsUser().getEffectiveName();
                String contexte = "";
                if (event.getOption("contexte") != null) {
                    contexte = event.getOption("contexte").getAsString();
                }

                ComplimentCommand complimentCommand = new ComplimentCommand(event, personne, contexte);
                complimentCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "private-send" -> {
                User personne = event.getOption("personne").getAsUser();
                String message = event.getOption("message").getAsString();

                PrivateSendCommand privateSendCommand = new PrivateSendCommand(event, personne, message);
                privateSendCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "private-send-file" -> {
                User personne = event.getOption("personne").getAsUser();
                OptionMapping fichier = event.getOption("fichier");

                PrivateSendFileCommand privateSendFileCommand = new PrivateSendFileCommand(event, personne, fichier);
                privateSendFileCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }

            // ADMIN COMMANDS
            case "clear" -> {
                int count = event.getOption("nombre").getAsInt();

                ClearCommand clearCommand = new ClearCommand(event, count);
                clearCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "update-avatar" -> {
                UpdateAvatarCommand updateAvatarCommand = new UpdateAvatarCommand(event);
                updateAvatarCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
                // CONFIGURATION COMMANDS
                case "reload-config" -> {
                    ReloadConfigCommand reloadConfigCommand = new ReloadConfigCommand(event);
                    reloadConfigCommand.run();

                    LOGs.sendLog("Commande terminée", "COMMAND");
                }
                case "get-config" -> {
                    GetConfigCommand getConfigCommand = new GetConfigCommand(event);
                    getConfigCommand.run();

                    LOGs.sendLog("Commande terminée", "COMMAND");
                }
                case "set-config" -> {
                    String parametre = event.getOption("paramètre").getAsString();
                    String valeur = event.getOption("valeur").getAsString();

                    SetConfigCommand setConfigCommand = new SetConfigCommand(event, parametre, valeur);
                    setConfigCommand.run();

                    LOGs.sendLog("Commande terminée", "COMMAND");
                }

            // MUSIC COMMANDS
            case "add" -> {
                String selectedMusic = event.getOption("musique", OptionMapping::getAsString);

                AddCommand addCommand = new AddCommand(event, selectedMusic);
                addCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "remove" -> {
                String selectedMusic = event.getOption("queue-music", OptionMapping::getAsString);

                RemoveCommand removeCommand = new RemoveCommand(event, selectedMusic);
                removeCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "clear-queue" -> {
                ClearQueueCommand clearQueueCommand = new ClearQueueCommand(event);
                clearQueueCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "list" -> {
                ListCommand listCommand = new ListCommand(event);
                listCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "list-queue" -> {
                ListQueueCommand listQueueCommand = new ListQueueCommand(event);
                listQueueCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "play" -> {
                PlayCommand playCommand = new PlayCommand(event);
                playCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "stop" -> {
                StopCommand stopCommand = new StopCommand(event);
                stopCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "next" -> {
                NextCommand nextCommand = new NextCommand(event);
                nextCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "previous" -> {
                PreviousCommand previousCommand = new PreviousCommand(event);
                previousCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "jump" -> {
                String selectedMusic = event.getOption("queue-music", OptionMapping::getAsString);

                JumpCommand jumpCommand = new JumpCommand(event, selectedMusic);
                jumpCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "loop" -> {
                LoopCommand loopCommand = new LoopCommand(event);
                loopCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "shuffle" -> {
                ShuffleCommand shuffleCommand = new ShuffleCommand(event);
                shuffleCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "download" -> {
                String url = event.getOption("url", OptionMapping::getAsString);

                DownloadCommand downloadCommand = new DownloadCommand(event, url);
                downloadCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "reload-musics" -> {
                ReloadMusicsCommand reloadMusicsCommand = new ReloadMusicsCommand(event);
                reloadMusicsCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
        }
    }
}
