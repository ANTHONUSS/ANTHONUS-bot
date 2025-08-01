package fr.anthonus.commands.user;

import fr.anthonus.commands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.Utils;
import fr.anthonus.utils.api.RedditAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.IOException;

public class CursedCommand extends Command {
    public CursedCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /cursed initialisée", DefaultLogType.COMMAND);
    }

    @Override
    public void run() {
        currentEvent.deferReply().queue();

        String imgURL = RedditAPI.getBlursedImageURL("blursedimages");
        try {
            byte[] imageBytes = Utils.downloadImageToByteArray(imgURL);
            String fileName = imgURL.substring(imgURL.lastIndexOf("/") + 1);

            currentEvent.getHook().sendFiles(FileUpload.fromData(imageBytes, fileName)).queue();
        } catch (IOException e) {
            EmbedBuilder embed = new EmbedBuilder();

            embed.setTitle(":x: ERREUR :x:");
            embed.setDescription("Une erreur est survenue lors du téléchargement de l'image.");

            embed.setColor(0xFF0000);

            currentEvent.getHook().sendMessageEmbeds(embed.build()).queue();
        }
    }
}
