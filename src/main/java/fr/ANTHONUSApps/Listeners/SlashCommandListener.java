package fr.ANTHONUSApps.Listeners;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import fr.ANTHONUSApps.Commands.SlashCommands.admin.ClearCommand;
import fr.ANTHONUSApps.Commands.SlashCommands.admin.UpdateAvatarCommand;
import fr.ANTHONUSApps.Commands.SlashCommands.music.*;
import fr.ANTHONUSApps.Commands.SlashCommands.normal.*;
import fr.ANTHONUSApps.LOGs;
import fr.ANTHONUSApps.Utils.Music.MusicManager;
import fr.ANTHONUSApps.Utils.Music.MusicPlayerManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

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

        switch (event.getName()) {
            // NORMAL COMMANDS
            case "cursed" -> {
                CursedImageCommand cursedImageCommand = new CursedImageCommand(event);
                cursedImageCommand.run();
            }
            case "roast" -> {
                String personne = event.getOption("personne").getAsUser().getEffectiveName();
                String contexte = "";
                if (event.getOption("contexte") != null) {
                    contexte = event.getOption("contexte").getAsString();
                }

                RoastCommand roastCommand = new RoastCommand(event, personne, contexte);
                roastCommand.run();
            }
            case "compliment" -> {
                String personne = event.getOption("personne").getAsUser().getEffectiveName();
                String contexte = "";
                if (event.getOption("contexte") != null) {
                    contexte = event.getOption("contexte").getAsString();
                }

                ComplimentCommand complimentCommand = new ComplimentCommand(event, personne, contexte);
                complimentCommand.run();
            }
            case "private-send" -> {
                User personne = event.getOption("personne").getAsUser();
                String message = event.getOption("message").getAsString();

                PrivateSendCommand privateSendCommand = new PrivateSendCommand(event, personne, message);
                privateSendCommand.run();
            }
            case "private-send-file" -> {
                User personne = event.getOption("personne").getAsUser();
                OptionMapping fichier = event.getOption("fichier");

                PrivateSendFileCommand privateSendFileCommand = new PrivateSendFileCommand(event, personne, fichier);
                privateSendFileCommand.run();
            }

            // ADMIN COMMANDS
            case "clear" -> {
                int count = event.getOption("nombre").getAsInt();

                ClearCommand clearCommand = new ClearCommand(event, count);
                clearCommand.run();
            }
            case "update-avatar" -> {
                UpdateAvatarCommand updateAvatarCommand = new UpdateAvatarCommand(event);
                updateAvatarCommand.run();
            }

            // MUSIC COMMANDS
            case "add" -> {
                String selectedMusic = event.getOption("musique", OptionMapping::getAsString);

                AddCommand addCommand = new AddCommand(event, selectedMusic);
                addCommand.run();
            }
            case "remove" -> {
                String selectedMusic = event.getOption("playlist-music", OptionMapping::getAsString);

                RemoveCommand removeCommand = new RemoveCommand(event, selectedMusic);
                removeCommand.run();
            }
            case "clear-playlist" -> {
                ClearPlaylistCommand clearPlaylistCommand = new ClearPlaylistCommand(event);
                clearPlaylistCommand.run();
            }
            case "list" -> {
                ListCommand listCommand = new ListCommand(event);
                listCommand.run();
            }
            case "list-queue" -> {
                ListQueueCommand listQueueCommand = new ListQueueCommand(event);
                listQueueCommand.run();
            }
            case "play" -> {
                PlayCommand playCommand = new PlayCommand(event);
                playCommand.run();
            }
            case "stop" -> {
                StopCommand stopCommand = new StopCommand(event);
                stopCommand.run();
            }
            case "next" -> {
                NextCommand nextCommand = new NextCommand(event);
                nextCommand.run();
            }
            case "previous" -> {
                PreviousCommand previousCommand = new PreviousCommand(event);
                previousCommand.run();
            }
            case "loop" -> {
                LoopCommand loopCommand = new LoopCommand(event);
                loopCommand.run();
            }
            case "shuffle" -> {
                ShuffleCommand shuffleCommand = new ShuffleCommand(event);
                shuffleCommand.run();
            }
            case "download" -> {

            }
        }
    }
}
