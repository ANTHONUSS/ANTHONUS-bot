package fr.ANTHONUSApps.Commands.SlashCommands;

import fr.ANTHONUSApps.LOGs;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.utils.FileUpload;

public class PrivateSendImageCommand extends Command {
    private final User personne;
    private final OptionMapping fichier;

    public PrivateSendImageCommand(SlashCommandInteractionEvent event, User personne, OptionMapping fichier) {
        super(event);

        this.personne = personne;
        this.fichier = fichier;
    }

    @Override
    public void run() {
        if (fichier == null || fichier.getAsAttachment() == null) {
            currentEvent.reply("Type du fichier invalide").setEphemeral(true).queue();
            return;
        }

        Message.Attachment attachment = fichier.getAsAttachment();

        attachment.getProxy().download()
                .thenAccept(inputStream -> {
                    FileUpload fileUpload = FileUpload.fromData(inputStream, attachment.getFileName());

                    personne.openPrivateChannel().queue(privateChannel -> {
                        privateChannel.sendMessage("Vous avez reçu une image anonyme.")
                                .addFiles(fileUpload)
                                .queue(
                                        success -> {
                                            currentEvent.reply("Message envoyé avec succès !").setEphemeral(true).queue();
                                            LOGs.sendLog("Message privé envoyé"
                                                            + "\nUser : @" + currentEvent.getUser().getEffectiveName()
                                                            + "\nServeur : " + currentEvent.getGuild().getName()
                                                            + "\nPersonne : " + personne.getEffectiveName(),
                                                    LOGs.LogType.COMMAND);
                                        },
                                        failure -> {
                                            currentEvent.reply("Impossible d'envoyer le message à cet utilisateur.").setEphemeral(true).queue();
                                            LOGs.sendLog("Erreur lors de l'envoi du message : " + failure.getMessage(),
                                                    LOGs.LogType.ERROR);
                                        }
                                );
                    });
                }).exceptionally(error -> {
                    currentEvent.reply("Erreur lors du téléchargement du fichier : " + error.getMessage())
                            .setEphemeral(true)
                            .queue();
                    error.printStackTrace();
                    return null;
                });
    }
}
