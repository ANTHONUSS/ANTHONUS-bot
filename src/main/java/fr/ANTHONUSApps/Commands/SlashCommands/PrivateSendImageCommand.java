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
        currentEvent.deferReply().setEphemeral(true).queue(
                deferSuccess -> {
                    if (fichier == null || fichier.getAsAttachment() == null) {
                        currentEvent.getHook().editOriginal("Type du fichier invalide").queue();
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
                                                        currentEvent.getHook().editOriginal("Message envoyé avec succès !").queue();
                                                        LOGs.sendLog("Message privé envoyé"
                                                                        + "\nUser : @" + currentEvent.getUser().getEffectiveName()
                                                                        + "\nServeur : " + currentEvent.getGuild().getName()
                                                                        + "\nPersonne : " + personne.getEffectiveName()
                                                                        + "\nFichier : " + attachment.getUrl(),
                                                                LOGs.LogType.COMMAND);
                                                    },
                                                    failure -> {
                                                        currentEvent.getHook().editOriginal("Impossible d'envoyer le message à cet utilisateur.").queue();
                                                        LOGs.sendLog("Erreur lors de l'envoi du message : " + failure.getMessage(),
                                                                LOGs.LogType.ERROR);
                                                    }
                                            );
                                });
                            }).exceptionally(error -> {
                                currentEvent.getHook().editOriginal("Erreur lors du téléchargement du fichier : " + error.getMessage()).queue();
                                LOGs.sendLog("Erreur lors de l'envoi du message : " + error.getMessage(),
                                        LOGs.LogType.ERROR);
                                return null;
                            });
                },
                failure -> {
                    LOGs.sendLog("Erreur lors de l'envoi du deferReply", LOGs.LogType.ERROR);
                }
        );
    }
}
