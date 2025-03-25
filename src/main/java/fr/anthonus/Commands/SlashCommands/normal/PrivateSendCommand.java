package fr.anthonus.Commands.SlashCommands.normal;

import fr.anthonus.Commands.SlashCommands.Command;
import fr.anthonus.LOGs;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PrivateSendCommand extends Command {
    private final User personne;
    private final String message;

    public PrivateSendCommand(SlashCommandInteractionEvent event, User personne, String message) {
        super(event);

        this.personne = personne;
        this.message = message;

        LOGs.sendLog("Commande /private-send initialisée", "COMMAND");
    }

    @Override
    public void run() {
        personne.openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage("## Vous avez reçu un message anonyme :\n\n" + message).queue(
                            success -> {
                                currentEvent.reply("## ✅ Message envoyé avec succès !").setEphemeral(true).queue();
                                LOGs.sendLog("Message privé envoyé"
                                                + "\nUser : @" + currentEvent.getUser().getEffectiveName()
                                                + "\nServeur : " + currentEvent.getGuild().getName()
                                                + "\nPersonne : " + personne.getEffectiveName()
                                                + "\nMessage : " + message,
                                        "COMMAND");
                            },
                            failure -> {
                                currentEvent.reply("## :x: Impossible d'envoyer le message à cet utilisateur.").setEphemeral(true).queue();
                                LOGs.sendLog("Message privé envoyé"
                                                + "\nUser : @" + currentEvent.getUser().getEffectiveName()
                                                + "\nServeur : " + currentEvent.getGuild().getName()
                                                + "\nPersonne : " + personne.getEffectiveName()
                                                + "\nMessage : " + message
                                                + "\nRaison : " + failure.getMessage(),
                                        "ERROR");
                            }
                    );
                }
        );
    }
}
