package fr.anthonus.commands.slashCommands.normal;

import fr.anthonus.commands.slashCommands.Command;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.utils.FileUpload;

public class PrivateSendFileCommand extends Command {
    private final User personne;
    private final OptionMapping fichier;

    public PrivateSendFileCommand(SlashCommandInteractionEvent event, User personne, OptionMapping fichier) {
        super(event);

        this.personne = personne;
        this.fichier = fichier;

        LOGs.sendLog("Commande /private-send-file initialisée", CustomLogType.COMMAND);
    }

    @Override
    public void run() {
        currentEvent.deferReply().setEphemeral(true).queue(
                deferSuccess -> {
                    if (fichier == null || fichier.getAsAttachment() == null) {
                        currentEvent.getHook().editOriginal("## :x: Type du fichier invalide").queue();
                        return;
                    }

                    Message.Attachment attachment = fichier.getAsAttachment();

                    attachment.getProxy().download()
                            .thenAccept(inputStream -> {
                                FileUpload fileUpload = FileUpload.fromData(inputStream, attachment.getFileName());

                                personne.openPrivateChannel().queue(privateChannel -> {
                                    privateChannel.sendMessage("## Vous avez reçu un fichier anonyme.")
                                            .addFiles(fileUpload)
                                            .queue(
                                                    success -> {
                                                        currentEvent.getHook().editOriginal("## ✅ Message envoyé avec succès !").queue();
                                                        LOGs.sendLog("Message privé envoyé"
                                                                        + "\nUser : @" + currentEvent.getUser().getEffectiveName()
                                                                        + "\nServeur : " + currentEvent.getGuild().getName()
                                                                        + "\nPersonne : " + personne.getEffectiveName()
                                                                        + "\nFichier : " + attachment.getUrl(),
                                                                CustomLogType.COMMAND);
                                                    },
                                                    failure -> {
                                                        currentEvent.getHook().editOriginal("## :x: Impossible d'envoyer le message à cet utilisateur.").queue();
                                                        LOGs.sendLog("Erreur lors de l'envoi du message : " + failure.getMessage(),
                                                                DefaultLogType.ERROR);
                                                    }
                                            );
                                });
                            }).exceptionally(error -> {
                                currentEvent.getHook().editOriginal("## :x: Erreur lors du téléchargement du fichier : " + error.getMessage()).queue();
                                LOGs.sendLog("Erreur lors de l'envoi du message : " + error.getMessage(),
                                        DefaultLogType.ERROR);
                                return null;
                            });
                },
                failure -> {
                    LOGs.sendLog("Erreur lors de l'envoi du deferReply", DefaultLogType.ERROR);
                }
        );
    }
}
