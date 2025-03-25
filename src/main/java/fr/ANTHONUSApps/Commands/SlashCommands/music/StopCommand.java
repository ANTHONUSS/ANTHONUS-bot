package fr.ANTHONUSApps.Commands.SlashCommands.music;

import fr.ANTHONUSApps.Commands.SlashCommands.Command;
import fr.ANTHONUSApps.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class StopCommand extends Command {
    public StopCommand(SlashCommandInteractionEvent event) {
        super(event);
    }

    @Override
    public void run() {
        AudioManager audioManager = currentEvent.getGuild().getAudioManager();
        if (audioManager.isConnected()) {
            audioManager.closeAudioConnection();
            MusicManager.players.get(currentEvent.getGuild().getIdLong()).setCurrentTrack(null);
            currentEvent.reply("## ✅ Musique arrêtée").queue();
        } else {
            currentEvent.reply("## :warning: Le bot ne joue actuellement pas de musique").queue();
        }
    }
}
