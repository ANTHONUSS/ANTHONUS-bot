package fr.anthonus.commands.slashCommands.music;

import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.utils.ServerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ReloadMusicsCommand extends Command {
    public ReloadMusicsCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /reloadmusics initialisée", "COMMAND");
    }

    @Override
    public void run() {
        currentEvent.deferReply().queue();

        ServerManager.musicsList.clear();
        ServerManager.updateMusicsList();
        currentEvent.getHook().editOriginal("## ✅ Liste des musiques rechargée").queue();
    }
}
