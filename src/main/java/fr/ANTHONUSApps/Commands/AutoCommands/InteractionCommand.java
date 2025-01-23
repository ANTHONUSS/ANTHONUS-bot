package fr.ANTHONUSApps.Commands.AutoCommands;

import fr.ANTHONUSApps.LOGs;
import fr.ANTHONUSApps.Utils.APICalls.APICallGPT;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class InteractionCommand extends AutoCommand{
    private final String roastInteractionSystemMessage = """
            Tu est un bot discord, tu as été exécuté aléatoirement sur un des messages du serveur et ton seul est unique but est de clash la personne qui as été mentionné.
            Pas d'insultes vulgaires, mais ton but est de bien terminer la personne.
            Le message restera bien évidemment respectueux, mais ton but sera de clash la personne le plus possible.
            """;

    private final String bibleInteractionSystemMessage = """
            Tu est un bot discord, tu as été exécuté aléatoirement sur un des messages du serveur et ton seul est unique but est de citer un passage de la bible à la personne qui as été mentionné.
            Ton but est de citer un passage de la bible à la personne mentionnée, comme pour le remettre sur le droit chemin par rapport à ce qu'il a dit.
            Fais comme si ce que la personne à dit était mal par rapport à la religion, et redirige le vers le chrétienté en lui citant un passage de la bible.
            Précise bien que tu cite la bible dans ton message.
            Essaie de prendre un passage de la bible en rapport avec le message envoyé à l'utilisateur.
            Fais ensuite une petite conclusion en rapport avec ce passage cité et le message envoyé par la personne.
            """;

    public InteractionCommand(MessageReceivedEvent event) {
        super(event);

        LOGs.sendLog("InteractionCommand initialisée", LOGs.LogType.AUTOCOMMAND);
    }

    @Override
    public void run() {
        double rand = Math.random() * 100;
        if (rand <= 50) {
            roastInteraction();
        } else if (rand > 50) {
            bibleInteraction();
        }
    }

    private void roastInteraction() {
        try {
            String response = getGPTResponse(roastInteractionSystemMessage);
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

    private void bibleInteraction() {
        try {
            String response = getGPTResponse(bibleInteractionSystemMessage);
            if (response != null) {
                currentEvent.getMessage().reply(response).queue();
                LOGs.sendLog("bible envoyé aléatoirement"
                                + "\nUser : @" + currentEvent.getMember().getEffectiveName()
                                + "\nServeur : " + currentEvent.getGuild().getName()
                                + "\nSalon : #" + currentEvent.getChannel().getName(),
                        LOGs.LogType.AUTOCOMMAND);
            } else {
                LOGs.sendLog("Erreur sur bibleAutoCommand"
                                + "\nUser : @" + currentEvent.getMember().getEffectiveName()
                                + "\nServeur : " + currentEvent.getGuild().getName()
                                + "\nSalon : #" + currentEvent.getChannel().getName(),
                        LOGs.LogType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getGPTResponse(String systemMessage) {
        String userMessage =  currentEvent.getMember().getEffectiveName() + " : " + currentEvent.getMessage().getContentRaw();

        APICallGPT gptRequest = new APICallGPT(300,systemMessage , userMessage);

        String response = gptRequest.call().getAsJsonArray("choices").get(0).getAsJsonObject()
                .getAsJsonObject("message").get("content").getAsString();

        return response;
    }
}
