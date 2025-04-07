package fr.anthonus.commands.slashCommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.LOGs;
import fr.anthonus.utils.ServerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.ArrayList;
import java.util.List;

public class ListCommand extends Command {

    public ListCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /list initialisée", "COMMAND");
    }

    public ListCommand() {
        super(null);

        LOGs.sendLog("Sous-commande /list initialisée", "COMMAND");
    }

    @Override
    public void run() {
        StringSelectMenu.Builder menuBuilder = StringSelectMenu.create("select_menu")
                .setPlaceholder("Aller à la page...");
        for (int i = 1; i <= ServerManager.getTotalPages(); i++) {
            menuBuilder.addOption("Page " + i, String.valueOf(i));
        }

        currentEvent.reply(makeList(1))
                .addActionRow(
                        Button.primary("previous_page_1", "⬅️ Page précédente").asDisabled(),
                        Button.primary("next_page_1", "➡️ Page suivante")
                )
                .addActionRow(
                        menuBuilder.build()
                )
                .setEphemeral(true)
                .queue();
    }

    public String makeList(int page) {
        LOGs.sendLog("Création de la liste...", "COMMAND");
        List<AudioTrack> tracks = new ArrayList<>();
        tracks.addAll(ServerManager.musicsList);

        List<AudioTrack> pageContent = tracks.stream()
                .skip((page - 1) * 10L)
                .limit(10)
                .toList();

        StringBuilder pageString = new StringBuilder();
        pageString.append("# Liste des cartes (").append(page).append("/").append(ServerManager.getTotalPages()).append(")\n\n");

        for (AudioTrack track : pageContent) {
            pageString.append("- ").append(ServerManager.getFileName(track.getInfo().uri)).append("\n");
        }
        pageString.append("\n");

        LOGs.sendLog("Liste créée", "COMMAND");

        return pageString.toString();
    }
}
