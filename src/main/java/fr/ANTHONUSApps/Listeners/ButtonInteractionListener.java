package fr.ANTHONUSApps.Listeners;

import fr.ANTHONUSApps.Commands.SlashCommands.music.ListCommand;
import fr.ANTHONUSApps.Utils.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class ButtonInteractionListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();
        int currentPage = Integer.parseInt(buttonId.replace("previous_page_", "").replace("next_page_", ""));


        if (buttonId.startsWith("previous_page_")) {
            currentPage--;
        } else if (buttonId.startsWith("next_page_")) {
            currentPage++;
        }

        StringSelectMenu.Builder menuBuilder = StringSelectMenu.create("select_menu")
                .setPlaceholder("Aller à la page...");
        for (int i = 1; i <= MusicManager.getTotalPages(); i++) {
            menuBuilder.addOption("Page " + i, String.valueOf(i));
        }

        ListCommand listCommand = new ListCommand();
        String currentList = listCommand.makeList(currentPage);

        event.editMessage(currentList)
                .setComponents(
                        ActionRow.of(
                                Button.primary("previous_page_" + currentPage, "⬅️ Page précédente").withDisabled(currentPage == 1),
                                Button.primary("next_page_" + currentPage, "➡️ Page suivante").withDisabled(currentPage == MusicManager.getTotalPages())
                        ),
                        ActionRow.of(menuBuilder.build())
                )
                .queue();
    }
}
