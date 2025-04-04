package fr.anthonus.Commands.SlashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.Commands.SlashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class PreviousCommand extends Command {
    public PreviousCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /previous initialisée", "COMMAND");
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();
        AudioManager audioManager = currentEvent.getGuild().getAudioManager();

        if (!audioManager.isConnected()) {
            currentEvent.reply("## :warning: Le bot n'est pas connecté à un salon vocal").setEphemeral(true).queue();
            return;
        }

        AudioTrack previousTrack = MusicManager.players.get(guildID).getPreviousTrack();
        if (previousTrack == null)  previousTrack = MusicManager.players.get(guildID).getCurrentTrack();

        MusicManager.players.get(guildID).getAudioPlayer().startTrack(previousTrack.makeClone(), false);
        MusicManager.players.get(guildID).setCurrentTrack(previousTrack);

        currentEvent.reply("## :arrow_left: Musique précédente lancée").queue();
    }
}
