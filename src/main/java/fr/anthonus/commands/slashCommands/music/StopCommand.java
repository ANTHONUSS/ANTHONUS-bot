package fr.anthonus.commands.slashCommands.music;

import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.utils.ServerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class StopCommand extends Command {
    public StopCommand(SlashCommandInteractionEvent event) {
        super(event);
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();
        AudioManager audioManager = currentEvent.getGuild().getAudioManager();

        if (audioManager.isConnected()) {
            audioManager.closeAudioConnection();
            ServerManager.servers.get(guildID).setCurrentTrack(null);
            currentEvent.reply("## ✅ Musique arrêtée").queue();
        } else {
            currentEvent.reply("## :warning: Le bot ne joue actuellement pas de musique").queue();
        }
    }
}
