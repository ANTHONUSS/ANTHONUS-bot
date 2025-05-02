package fr.anthonus.commands.slashCommands.admin;

import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.CustomLogType;
import fr.anthonus.logs.logTypes.DefaultLogType;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.io.File;
import java.io.IOException;

public class UpdateAvatarCommand extends Command {

    public UpdateAvatarCommand(SlashCommandInteractionEvent event) {
        super(event);

        LOGs.sendLog("Commande /update-avatar initialisée", CustomLogType.COMMAND);
    }

    @Override
    public void run() {
        try {
            File avatarGif = new File("conf/avatar.gif");
            File avatarPng = new File("conf/avatar.png");
            Icon newIcon = null;
            if (!avatarGif.exists() && !avatarPng.exists()) {
                currentEvent.reply("Il n'y a pas de fichier avatar.gif ou avatar.png présent dans les fichiers du bot")
                        .setEphemeral(true)
                        .queue();
                LOGs.sendLog("Le fichier n'existe pas dans " + avatarGif.getAbsolutePath(), DefaultLogType.ERROR);
                LOGs.sendLog("Le fichier n'existe pas dans " + avatarPng.getAbsolutePath(), DefaultLogType.ERROR);
                return;
            } else if (avatarGif.exists() && avatarPng.exists()){
                currentEvent.reply("fichiers .png et .gif trouvés dans le serveur, veuillez n'en prendre qu'un seul sur les deux.")
                        .setEphemeral(true)
                        .queue();
                LOGs.sendLog("Fichiers .png et .gif trouvés dans le serveur.", DefaultLogType.ERROR);
                return;
            } else if (avatarGif.exists() && !avatarPng.exists()){
                newIcon = Icon.from(avatarGif);
            } else if (!avatarGif.exists() && avatarPng.exists()){
                newIcon = Icon.from(avatarPng);
            }

            currentEvent.getJDA().getSelfUser().getManager().setAvatar(newIcon).queue(
                    success -> {
                        currentEvent.reply("Avatar du bot mis à jour.")
                                .setEphemeral(true)
                                .queue();
                        LOGs.sendLog("Avatar du bot mis à jour.", DefaultLogType.DEFAULT);
                    },
                    failure -> {
                        currentEvent.reply("Erreur lors de la mise à jour de l'avatar du bot")
                                .setEphemeral(true)
                                .queue();
                        LOGs.sendLog("Erreur lors de la mise à jour de l'avatar du bot", DefaultLogType.ERROR);
                    }
            );
        } catch (IOException e) {
            currentEvent.reply("Erreur lors de la mise à jour de l'avatar du bot")
                    .setEphemeral(true)
                    .queue();
            LOGs.sendLog("Erreur lors de la mise à jour de l'avatar du bot", DefaultLogType.ERROR);
        }
    }
}
