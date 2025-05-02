package fr.anthonus.commands.slashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.CustomLogType;
import fr.anthonus.utils.ServerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;

public class ShuffleCommand extends Command {
    public ShuffleCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /shuffle initialisée", CustomLogType.COMMAND);
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();

        ArrayList<AudioTrack> queue = ServerManager.servers.get(guildID).getQueue();

        if (queue.isEmpty()) {
            currentEvent.reply("## :warning: La file d'attente est vide").setEphemeral(true).queue();
            return;
        }

        for (int i = 0; i < queue.size(); i++) {
            int randomIndex = (int) (Math.random() * queue.size());
            AudioTrack temp = queue.get(i);
            queue.set(i, queue.get(randomIndex));
            queue.set(randomIndex, temp);
        }

        currentEvent.reply("## ✅ Playlist mélangée").queue();
    }
}
