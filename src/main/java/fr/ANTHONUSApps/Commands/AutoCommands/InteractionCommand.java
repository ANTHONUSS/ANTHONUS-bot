package fr.ANTHONUSApps.Commands.AutoCommands;

import fr.ANTHONUSApps.LOGs;
import fr.ANTHONUSApps.Utils.APICalls.APICallGPT;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class InteractionCommand {
    private MessageReceivedEvent currentEvent;
    private final String roastInteractionSystemMessage = """
            Tu est un bot discord, tu as été exécuté aléatoirement sur un des messages du serveur et ton seul est unique but est de clash la personne qui as été mentionné
            Pas d'insultes vulgaires, mais ton but est de bien terminer la personne.
            Le message restera bien évidemment respectueux, mais ton but sera de clash la personne le plus possible.
            """;

    public InteractionCommand(MessageReceivedEvent event) {
        this.currentEvent = event;

        LOGs.sendLog("InteractionCommand initialisée", LOGs.LogType.AUTOCOMMAND);
    }

    public void randomInteraction() {
        double rand = Math.random() * 100;
        if (rand <= 100) {
            roastInteraction();
        }
    }

    private void roastInteraction() {
        try {
            String response = getGPTResponse();
            if (response != null) {
                currentEvent.getMessage().reply(response).queue();
                LOGs.sendLog("roast envoyé aléatoirement"
                                + "\nUser : @" + currentEvent.getMember().getEffectiveName()
                                + "\nServeur : " + currentEvent.getGuild().getName()
                                + "\nSalon : #" + currentEvent.getChannel().getName(),
                        LOGs.LogType.AUTOCOMMAND);
            } else {
                LOGs.sendLog("Erreur sur roastAutoCommand"
                                + "\nUser : @" + currentEvent.getMember().getEffectiveName()
                                + "\nServeur : " + currentEvent.getGuild().getName()
                                + "\nSalon : #" + currentEvent.getChannel().getName(),
                        LOGs.LogType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getGPTResponse() {
        String userMessage =  currentEvent.getMember().getEffectiveName() + " : " + currentEvent.getMessage().getContentRaw();

        APICallGPT gptRequest = new APICallGPT(300, roastInteractionSystemMessage, userMessage);

        String response = gptRequest.call().getAsJsonArray("choices").get(0).getAsJsonObject()
                .getAsJsonObject("message").get("content").getAsString();

        return response;
    }
}
