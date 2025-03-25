package fr.ANTHONUSApps.Commands.SlashCommands.normal;

import fr.ANTHONUSApps.Commands.SlashCommands.Command;
import fr.ANTHONUSApps.LOGs;
import fr.ANTHONUSApps.Utils.APICalls.APICallGPT;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class RoastCommand extends Command {
    private String personne;
    private String contexte;

    private final String systemMessage = """
            Tu est un bot discord, tu as été exécuté via la commande /roast @mention et ton seul est unique but est de clash la personne qui as été mentionné
            Pas d'insultes vulgaires, mais ton but est de bien terminer la personne.
            Le message restera bien évidemment respectueux, mais ton but sera de clash la personne le plus possible.
            Tu prendra en compte dans le message de l'utilisateur le pseudo de la personne ainsi que le contexte pour le clash dessus.
            """;

    public RoastCommand(SlashCommandInteractionEvent event, String personne, String contexte) {
        super(event);
        this.personne = personne;
        this.contexte = contexte;

        LOGs.sendLog("commande /roast initialisée.", "COMMAND");
    }

    @Override
    public void run() {
        currentEvent.deferReply().queue(
                success -> {
                    try {
                        String response = getGPTResponse();
                        if (response != null) {
                            currentEvent.getHook().editOriginal(response).queue();
                            LOGs.sendLog("roast envoyé"
                                            + "\nUser : @" + currentEvent.getUser().getEffectiveName()
                                            + "\nServeur : " + currentEvent.getGuild().getName()
                                            + "\nSalon : #" + currentEvent.getChannel().getName()
                                            + "\nPersonne : " + personne
                                            + "\nContexte : " + contexte,
                                    "COMMAND");
                        } else {
                            currentEvent.getHook().editOriginal("Erreur avec ChatGPT").queue();
                            LOGs.sendLog("Erreur sur roastCommand"
                                            + "\nUser : @" + currentEvent.getUser().getEffectiveName()
                                            + "\nServeur : " + currentEvent.getGuild().getName()
                                            + "\nSalon : #" + currentEvent.getChannel().getName(),
                                    "ERROR");
                        }
                    } catch (Exception e) {
                        currentEvent.getHook().editOriginal("## :x: Une erreur est survenue lors de la communication avec ChatGPT" + e.getMessage()).queue();
                        e.printStackTrace();
                    }
                },
                failure -> {
                    LOGs.sendLog("## :x: Erreur lors de l'envoi du deferReply", "ERROR");
                }
        );
    }

    private String getGPTResponse() {
        String userMessage = "Personne mentionnée : " + personne;
        if (!contexte.isEmpty()) userMessage += "\nContexte : " + contexte;

        APICallGPT gptRequest = new APICallGPT(300, systemMessage, userMessage);

        String response = gptRequest.call().getAsJsonArray("choices").get(0).getAsJsonObject()
                .getAsJsonObject("message").get("content").getAsString();

        return response;
    }
}
