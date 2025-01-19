package fr.ANTHONUSApps.Commands;

import fr.ANTHONUSApps.LOGs;
import fr.ANTHONUSApps.Utils.APICalls.APICallGPT;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ComplimentCommand {
    private SlashCommandInteractionEvent currentEvent;
    private String personne;
    private String contexte;

    private final String systemMessage = """
            Tu est un bot discord, tu as été exécuté via la commande /compliment @mention et ton seul est unique but est de complimenter la personne qui as été mentionné.
            Ton but sera de complimenter la personne le plus possible.
            Tu prendra en compte dans le message de l'utilisateur le pseudo de la personne ainsi que le contexte pour le compliment dessus.
            """;

    public ComplimentCommand(SlashCommandInteractionEvent event, String personne, String contexte) {
        this.currentEvent = event;
        this.personne = personne;
        this.contexte = contexte;

        LOGs.sendLog("Roast command initialisée.", LOGs.LogType.COMMAND);
    }

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
                                            + "\nPersonne : " + personne,
                                    LOGs.LogType.COMMAND);
                        } else {
                            currentEvent.getHook().editOriginal("Erreur avec ChatGPT").queue();
                            LOGs.sendLog("Erreur sur roastCommand"
                                            + "\nUser : @" + currentEvent.getUser().getEffectiveName()
                                            + "\nServeur : " + currentEvent.getGuild().getName()
                                            + "\nSalon : #" + currentEvent.getChannel().getName(),
                                    LOGs.LogType.ERROR);
                        }
                    } catch (Exception e) {
                        currentEvent.getHook().editOriginal("Une erreur est survenue lors de la communication avec ChatGPT" + e.getMessage()).queue();
                        e.printStackTrace();
                    }
                },
                failure -> {
                    LOGs.sendLog("Erreur lors de l'envoi du deferReply", LOGs.LogType.ERROR);
                }
        );
    }

    private String getGPTResponse() {
        String userMessage = "Personne mentionnée : " + personne;
        if(!contexte.isEmpty()) userMessage += "\nContexte : " + contexte;

        APICallGPT gptRequest = new APICallGPT(300, systemMessage, userMessage);

        String response = gptRequest.call().getAsJsonArray("choices").get(0).getAsJsonObject()
                .getAsJsonObject("message").get("content").getAsString();

        return response;
    }
}
