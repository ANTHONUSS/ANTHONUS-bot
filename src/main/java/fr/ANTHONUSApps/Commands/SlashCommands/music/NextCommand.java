package fr.ANTHONUSApps.Commands.SlashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.ANTHONUSApps.Commands.SlashCommands.Command;
import fr.ANTHONUSApps.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class NextCommand extends Command {
    public NextCommand(SlashCommandInteractionEvent event) {
        super(event);
    }

    @Override
    public void run() {
        AudioTrack nextTrack = MusicManager.players.get(currentEvent.getGuild().getIdLong()).getNextTrack();
        if (nextTrack == null) {
            currentEvent.getGuild().getAudioManager().closeAudioConnection();
            return;
        }

        MusicManager.players.get(currentEvent.getGuild().getIdLong()).getAudioPlayer().startTrack(nextTrack.makeClone(), false);
        MusicManager.players.get(currentEvent.getGuild().getIdLong()).setCurrentTrack(nextTrack);

        currentEvent.reply("Musique suivante lancée").queue();
    }
}
