package fr.anthonus.Listeners;

import fr.anthonus.Commands.SlashCommands.admin.ClearCommand;
import fr.anthonus.Commands.SlashCommands.admin.UpdateAvatarCommand;
import fr.anthonus.Commands.SlashCommands.music.*;
import fr.anthonus.Commands.SlashCommands.normal.*;
import fr.anthonus.LOGs;
import fr.anthonus.Utils.Music.MusicManager;
import fr.anthonus.Utils.Music.MusicPlayerManager;
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

        if (!MusicManager.players.containsKey(event.getGuild().getIdLong())) {
            MusicManager.players.put(event.getGuild().getIdLong(), new MusicPlayerManager(event.getGuild().getIdLong()));
            LOGs.sendLog("Nouveau MusicPlayerManager initialisé pour le serveur " + event.getGuild().getName(), "DEFAULT");
        }

        MusicPlayerManager player = MusicManager.players.get(event.getGuild().getIdLong());
        if (player.getLastModified() != null) {
            Instant lastModified = MusicManager.players.get(event.getGuild().getIdLong()).getLastModified();
            if (Duration.between(lastModified, Instant.now()).toHours() >= 4) {
                player.getQueue().clear();
                player.setLastModified(null);
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

            // MUSIC COMMANDS
            case "add" -> {
                String selectedMusic = event.getOption("musique", OptionMapping::getAsString);

                AddCommand addCommand = new AddCommand(event, selectedMusic);
                addCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "remove" -> {
                String selectedMusic = event.getOption("playlist-music", OptionMapping::getAsString);

                RemoveCommand removeCommand = new RemoveCommand(event, selectedMusic);
                removeCommand.run();

                LOGs.sendLog("Commande terminée", "COMMAND");
            }
            case "clear-playlist" -> {
                ClearPlaylistCommand clearPlaylistCommand = new ClearPlaylistCommand(event);
                clearPlaylistCommand.run();

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
        }
    }
}
