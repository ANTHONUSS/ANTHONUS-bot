package fr.ANTHONUSApps.Listeners;

import fr.ANTHONUSApps.Commands.SlashCommands.music.ListCommand;
import fr.ANTHONUSApps.LOGs;
import fr.ANTHONUSApps.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class SelectionMenuListener extends ListenerAdapter {
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        int currentPage = Integer.parseInt(event.getValues().get(0));

        ListCommand listCommand = new ListCommand();
        String currentList = listCommand.makeList(currentPage);

        event.editMessage(currentList)
                .setComponents(
                        ActionRow.of(
                                Button.primary("previous_page_" + currentPage, "⬅️ Page précédente").withDisabled(currentPage == 1),
                                Button.primary("next_page_" + currentPage, "➡️ Page suivante").withDisabled(currentPage == MusicManager.getTotalPages())
                        ),
                        ActionRow.of(event.getComponent())
                )
                .queue();
    }
}
