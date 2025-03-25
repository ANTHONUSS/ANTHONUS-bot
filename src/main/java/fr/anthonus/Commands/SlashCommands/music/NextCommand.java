package fr.anthonus.Commands.SlashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.Commands.SlashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class NextCommand extends Command {
    public NextCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /next initialisée", "COMMAND");
    }

    @Override
    public void run() {
        AudioTrack nextTrack = MusicManager.players.get(currentEvent.getGuild().getIdLong()).getNextTrack();
        if (nextTrack == null) {
            currentEvent.getGuild().getAudioManager().closeAudioConnection();
            currentEvent.reply("## :warning: Plus de musiques dans la playlist - Bot déconnecté").queue();
            return;
        }

        MusicManager.players.get(currentEvent.getGuild().getIdLong()).getAudioPlayer().startTrack(nextTrack.makeClone(), false);
        MusicManager.players.get(currentEvent.getGuild().getIdLong()).setCurrentTrack(nextTrack);

        currentEvent.reply("## :arrow_right: Musique suivante lancée").queue();
    }
}
