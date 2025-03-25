package fr.anthonus.Commands.AutoCommands;

import fr.anthonus.LOGs;
import fr.anthonus.Utils.APICalls.APICallGPT;
import fr.anthonus.Utils.WebhookMessage;
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

    private final String beaufMessage = """
            Tu est un bot discord, tu as été exécuté aléatoirement sur un des messages du serveur et ton seul est unique but est de modifier le message qui t'a été envoyé.
            Je m'explique. Tu doit prendre le message de l'utilisateur, le réécrire mais en modifiant ou rajoutant une partie pour le rendre différent.
            Le message devra ressembler à un message écris par un gros beauf marseillais qui aime bien l'humour vache et lourde.
            Tu doit bien garder le contexte exact du message de base.
            Tu ne doit SURTOUT pas préciser le nom de l'utilisateur au début du message ou des guillemets autour.
            """;

    private final String uwuMessage = """
            Tu est un bot discord, tu as été exécuté aléatoirement sur un des messages du serveur et ton seul est unique but est de modifier le message qui t'a été envoyé.
            Je m'explique. Tu doit prendre le message de l'utilisateur, le réécrire mais en modifiant ou rajoutant une partie pour le rendre différent.
            Le message devra ressembler à un message écris par une uwu-girl / e-girl / cat-girl d'internet qui parle de façon cringe et "kawaii".
            Tu doit bien garder le contexte exact du message de base.
            Tu ne doit SURTOUT pas préciser le nom de l'utilisateur au début du message ou des guillemets autour.
            """;

    private final String skibidiMessage = """
            Tu est un bot discord, tu as été exécuté aléatoirement sur un des messages du serveur et ton seul est unique but est de modifier le message qui t'a été envoyé.
            Je m'explique. Tu doit prendre le message de l'utilisateur, le réécrire mais en modifiant ou rajoutant une partie pour le rendre différent.
            Le message devra ressembler à un message écris par un gamin totalement brainrot par les memes récents (skibidi toilet, alpha, aura, ...).
            Tu doit bien garder le contexte exact du message de base.
            Tu ne doit SURTOUT pas préciser le nom de l'utilisateur au début du message ou des guillemets autour.
            """;

    private final String depressMessage = """
            Tu est un bot discord, tu as été exécuté aléatoirement sur un des messages du serveur et ton seul est unique but est de modifier le message qui t'a été envoyé.
            Je m'explique. Tu doit prendre le message de l'utilisateur, le réécrire mais en modifiant ou rajoutant une partie pour le rendre différent.
            Le message devra ressembler à un message écris par un dépressif qui n'a aucun ton dans son message, super plat et sans intérêt.
            Tu doit bien garder le contexte exact du message de base.
            Tu ne doit SURTOUT pas préciser le nom de l'utilisateur au début du message ou des guillemets autour.
            """;

    private final String ancienMessage = """
            Tu est un bot discord, tu as été exécuté aléatoirement sur un des messages du serveur et ton seul est unique but est de modifier le message qui t'a été envoyé.
            Je m'explique. Tu doit prendre le message de l'utilisateur, le réécrire mais en modifiant ou rajoutant une partie pour le rendre différent.
            Le message devra ressembler à un message écris par quelqu'un d'une époque victorienne, avec des formules de politesse et un langage soutenu, qui utilise des termes anciens et des phrases pleines de courtoisie et de formalité.
            Tu doit bien garder le contexte exact du message de base.
            Tu ne doit SURTOUT pas préciser le nom de l'utilisateur au début du message ou des guillemets autour.
            """;

    private final String poeteMessage = """
            Tu est un bot discord, tu as été exécuté aléatoirement sur un des messages du serveur et ton seul est unique but est de modifier le message qui t'a été envoyé.
            Je m'explique. Tu doit prendre le message de l'utilisateur, le réécrire mais en modifiant ou rajoutant une partie pour le rendre différent.
            Le message devra ressembler à un message écris par un grand poète romantique du 19ème siècle, avec un langage fleuri, des métaphores et des comparaisons pleines de passion et d’émotions profondes.
            Tu doit bien garder le contexte exact du message de base.
            Tu ne doit SURTOUT pas préciser le nom de l'utilisateur au début du message ou des guillemets autour.
            """;

    private final String rappeurMessage = """
            Tu est un bot discord, tu as été exécuté aléatoirement sur un des messages du serveur et ton seul est unique but est de modifier le message qui t'a été envoyé.
            Je m'explique. Tu doit prendre le message de l'utilisateur, le réécrire mais en modifiant ou rajoutant une partie pour le rendre différent.
            Le message devra ressembler à un message écris par un rappeur de la street, qui fait des rimes et du rap dans tous ses messages.
            Tu doit bien garder le contexte exact du message de base.
            Tu ne doit SURTOUT pas préciser le nom de l'utilisateur au début du message ou des guillemets autour.
            """;

    public InteractionCommand(MessageReceivedEvent event) {
        super(event);

        LOGs.sendLog("AutoCommande d'interaction initialisée", "AUTOCOMMAND");
    }

    @Override
    public void run() {
        Random random = new Random();
        int rand = random.nextInt(3);

        switch (rand) {
            case 0 -> roastInteraction();
            case 1 -> bibleInteraction();
            case 2 -> {
                int rand2 = random.nextInt(7);
                switch (rand2) {
                    case 0 -> modifyInteraction(beaufMessage, "beauf");
                    case 1 -> modifyInteraction(uwuMessage, "uwu");
                    case 2 -> modifyInteraction(skibidiMessage, "skibidi");
                    case 3 -> modifyInteraction(depressMessage, "depress");
                    case 4 -> modifyInteraction(ancienMessage, "ancien");
                    case 5 -> modifyInteraction(poeteMessage, "poete");
                    case 6 -> modifyInteraction(rappeurMessage, "rappeur");
                }
            }
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
                        "AUTOCOMMAND");
            } else {
                LOGs.sendLog("Erreur sur roastAutoCommand"
                                + "\nUser : @" + currentEvent.getMember().getEffectiveName()
                                + "\nServeur : " + currentEvent.getGuild().getName()
                                + "\nSalon : #" + currentEvent.getChannel().getName(),
                        "ERROR");
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
                        "AUTOCOMMAND");
            } else {
                LOGs.sendLog("Erreur sur bibleAutoCommand"
                                + "\nUser : @" + currentEvent.getMember().getEffectiveName()
                                + "\nServeur : " + currentEvent.getGuild().getName()
                                + "\nSalon : #" + currentEvent.getChannel().getName(),
                        "ERROR");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void modifyInteraction(String gptContext, String mode) {
        try {
            String response = getGPTResponse(gptContext);
            if (response != null) {
                WebhookMessage webhookMessage = new WebhookMessage(currentEvent, response);
                webhookMessage.send();
                currentEvent.getMessage().delete().queue();

                LOGs.sendLog("modification de message effectuée aléatoirement"
                                + "\nUser : @" + currentEvent.getMember().getEffectiveName()
                                + "\nServeur : " + currentEvent.getGuild().getName()
                                + "\nSalon : #" + currentEvent.getChannel().getName()
                                + "\nMessage originel : " + currentEvent.getMessage().getContentRaw()
                                + "\nMessage modifié : " + response
                                + "\nMode : " + mode,
                        "AUTOCOMMAND");
            } else {
                LOGs.sendLog("Erreur sur modifyInteraction"
                                + "\nUser : @" + currentEvent.getMember().getEffectiveName()
                                + "\nServeur : " + currentEvent.getGuild().getName()
                                + "\nSalon : #" + currentEvent.getChannel().getName()
                                + "\nMessage originel : " + currentEvent.getMessage().getContentRaw(),
                        "ERROR");
            }
        } catch (Exception e) {
            LOGs.sendLog("Erreur sur modifyInteraction"
                            + "\nUser : @" + currentEvent.getMember().getEffectiveName()
                            + "\nServeur : " + currentEvent.getGuild().getName()
                            + "\nSalon : #" + currentEvent.getChannel().getName()
                            + "\nMessage originel : " + currentEvent.getMessage().getContentRaw(),
                    "ERROR");
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
