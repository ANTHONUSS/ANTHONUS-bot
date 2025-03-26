package fr.anthonus.Commands.SlashCommands.music;

import fr.anthonus.Commands.SlashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.Utils.Music.MusicManager;
import fr.anthonus.Utils.Music.MusicPlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class LoopCommand extends Command {
    public LoopCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /loop initialisée", "COMMAND");
    }

    @Override
    public void run() {
        long guildID = currentEvent.getGuild().getIdLong();

        MusicPlayerManager playerManager = MusicManager.players.get(guildID);
        if (playerManager.isLooping()) {
            playerManager.setLooping(false);
            currentEvent.reply("## :repeat:❌ La playlist ne sera plus lue en boucle.").queue();
        } else {
            playerManager.setLooping(true);
            currentEvent.reply("## :repeat:✅ La playlist sera lue en boucle.").queue();
        }
    }
}
