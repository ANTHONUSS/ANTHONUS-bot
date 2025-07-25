package fr.anthonus.commands.autoCommands;

import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.APICalls.APICallGPT;
import fr.anthonus.utils.ServerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Random;

public class InteractionCommand extends AutoCommand {
    private final String roastInteractionSystemMessage = """
            Tu est un bot discord, tu as été exécuté aléatoirement sur un des messages du serveur et ton seul et unique but est de clash la personne qui as été mentionné.
            Pas d'insultes vulgaires, mais ton but est de bien terminer la personne.
            Le message restera bien évidemment respectueux, mais ton but sera de clash la personne le plus possible.
            """;

    private final String bibleInteractionSystemMessage = """
            Tu est un bot discord, tu as été exécuté aléatoirement sur un des messages du serveur et ton seul et unique but est de citer un passage de la bible à la personne qui as été mentionné.
            Ton but est de citer un passage de la bible à la personne mentionnée, comme pour le remettre sur le droit chemin par rapport à ce qu'il a dit.
            Fais comme si ce que la personne à dit était mal par rapport à la religion, et redirige le vers le chrétienté en lui citant un passage de la bible.
            Précise bien que tu cite la bible dans ton message.
            Essaie de prendre un passage de la bible en rapport avec le message envoyé à l'utilisateur.
            Fais ensuite une petite conclusion en rapport avec ce passage cité et le message envoyé par la personne.
            """;

    public InteractionCommand(MessageReceivedEvent event) {
        super(event);

        LOGs.sendLog("AutoCommande d'interaction initialisée", DefaultLogType.AUTOCOMMAND);
    }

    @Override
    public void run() {
        if (!ServerManager.servers.get(currentEvent.getGuild().getIdLong()).getSettingJson().isAllowReply()) {
            LOGs.sendLog("AutoCommande reply désactivée", DefaultLogType.AUTOCOMMAND);
            return;
        }

        Random random = new Random();
        int rand = random.nextInt(3);
        switch (rand) {
            case 0 -> roastInteraction();
            case 1 -> bibleInteraction();
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
                        DefaultLogType.AUTOCOMMAND);
            } else {
                LOGs.sendLog("Erreur sur roastAutoCommand"
                                + "\nUser : @" + currentEvent.getMember().getEffectiveName()
                                + "\nServeur : " + currentEvent.getGuild().getName()
                                + "\nSalon : #" + currentEvent.getChannel().getName(),
                        DefaultLogType.ERROR);
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
                        DefaultLogType.AUTOCOMMAND);
            } else {
                LOGs.sendLog("Erreur sur bibleAutoCommand"
                                + "\nUser : @" + currentEvent.getMember().getEffectiveName()
                                + "\nServeur : " + currentEvent.getGuild().getName()
                                + "\nSalon : #" + currentEvent.getChannel().getName(),
                        DefaultLogType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getGPTResponse(String systemMessage) {
        String userMessage = currentEvent.getMember().getEffectiveName() + " : " + currentEvent.getMessage().getContentRaw();

        APICallGPT gptRequest = new APICallGPT(300, systemMessage, userMessage);

        String response = gptRequest.call().getAsJsonArray("choices").get(0).getAsJsonObject()
                .getAsJsonObject("message").get("content").getAsString();

        return response;
    }
}
