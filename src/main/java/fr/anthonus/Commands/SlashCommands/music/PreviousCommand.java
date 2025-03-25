package fr.anthonus.Commands.SlashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.Commands.SlashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PreviousCommand extends Command {
    public PreviousCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /previous initialisée", "COMMAND");
    }

    @Override
    public void run() {
        AudioTrack previousTrack = MusicManager.players.get(currentEvent.getGuild().getIdLong()).getPreviousTrack();
        if (previousTrack == null)  previousTrack = MusicManager.players.get(currentEvent.getGuild().getIdLong()).getCurrentTrack();

        MusicManager.players.get(currentEvent.getGuild().getIdLong()).getAudioPlayer().startTrack(previousTrack.makeClone(), false);
        MusicManager.players.get(currentEvent.getGuild().getIdLong()).setCurrentTrack(previousTrack);

        currentEvent.reply("## :arrow_left: Musique précédente lancée").queue();
    }
}
