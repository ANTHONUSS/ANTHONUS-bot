package fr.anthonus.commands.slashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.utils.ServerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class NextCommand extends Command {
    public NextCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /next initialisée", "COMMAND");
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();
        AudioManager audioManager = currentEvent.getGuild().getAudioManager();

        if (!audioManager.isConnected()) {
            currentEvent.reply("## :warning: Le bot n'est pas connecté à un salon vocal").setEphemeral(true).queue();
            return;
        }

        AudioTrack nextTrack = ServerManager.servers.get(guildID).getNextTrack();
        if (nextTrack == null) {
            audioManager.closeAudioConnection();
            currentEvent.reply("## :warning: Plus de musiques dans la playlist - Bot déconnecté").queue();
            return;
        }

        ServerManager.servers.get(guildID).getAudioPlayer().startTrack(nextTrack.makeClone(), false);
        ServerManager.servers.get(guildID).setCurrentTrack(nextTrack);

        currentEvent.reply("## :arrow_right: Musique suivante lancée").queue();
    }
}
