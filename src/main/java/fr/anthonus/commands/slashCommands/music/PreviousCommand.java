package fr.anthonus.commands.slashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.utils.ServerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class PreviousCommand extends Command {
    public PreviousCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /previous initialisée", CustomLogType.COMMAND);
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();
        AudioManager audioManager = currentEvent.getGuild().getAudioManager();

        if (!audioManager.isConnected()) {
            currentEvent.reply("## :warning: Le bot n'est pas connecté à un salon vocal").setEphemeral(true).queue();
            return;
        }

        AudioTrack previousTrack = ServerManager.servers.get(guildID).getPreviousTrack();
        if (previousTrack == null)  previousTrack = ServerManager.servers.get(guildID).getCurrentTrack();

        ServerManager.servers.get(guildID).getAudioPlayer().startTrack(previousTrack.makeClone(), false);
        ServerManager.servers.get(guildID).setCurrentTrack(previousTrack);

        currentEvent.reply("## :arrow_left: Musique précédente lancée").queue();
    }
}
