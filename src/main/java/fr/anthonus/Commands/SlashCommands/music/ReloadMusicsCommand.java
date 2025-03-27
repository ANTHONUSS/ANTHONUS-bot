package fr.anthonus.Commands.SlashCommands.music;

import fr.anthonus.Commands.SlashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ReloadMusicsCommand extends Command {
    public ReloadMusicsCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /reloadmusics initialisée", "COMMAND");
    }

    @Override
    public void run() {
        currentEvent.deferReply().queue();

        MusicManager.musicsList.clear();
        MusicManager.updateMusicsList();
        currentEvent.getHook().editOriginal("## ✅ Liste des musiques rechargée").queue();
    }
}
