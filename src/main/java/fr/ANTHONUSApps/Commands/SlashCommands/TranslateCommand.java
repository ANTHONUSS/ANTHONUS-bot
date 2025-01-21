package fr.ANTHONUSApps.Commands.SlashCommands;

import fr.ANTHONUSApps.LOGs;
import fr.ANTHONUSApps.Utils.APICalls.APICallGPT;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class TranslateCommand extends Command{
    private String userMessage;
    private String currentSystemMessage;

    private final String gptSystemUwU = """
            Convertis un message écrit en français en style UWU-Girl/E-Girl/cat-girl. Ajoute des emojis et adapte le texte pour refléter ce style distinctif, tout en gardant la structure et le sens de l'original. Réponds toujours en français.
            
            # Détails
            
            - Emploie des suffixes et des termes propres au style UWU/E-Girl (par exemple, remplacer "r" par "w", ajouter "nya" à la fin des phrases).
            - Ajoute des emojis mignons ou appropriés tels que 😺, 🌸, et ✨.
            - Assure-toi de conserver le contexte et le ton du message original.
            - tu ne dois ABSOLUMENT PAS répondre à la phrase de l'utilisateur, mais bien la traduire
            
            # Output Format
            
            Un court paragraphe en français transformé selon le style UWU-Girl/E-Girl/cat-girl, intégrant des emojis.
            
            # Exemples
            
            **Exemple 1**
            
            *Input:* Bonjour, comment vas-tu aujourd'hui ?
            
            *Output:* Bonjouww, comment vas-tu aujouwwd'hui nya~ ? 😺✨\s
            
            **Exemple 2**
            
            *Input:* J'adore les chats, ils sont si mignons.
            
            *Output:* J'adoore les chatonnss~ owow, ils sont si mignonsss nya~ 🌸🐾
            """;

    private final String gptSystemBrainrot = """
            Convertis un message écrit en français en style Skibidi-sigma brainrot. Adapte le texte pour refléter ce style distinctif, tout en gardant la structure et le sens de l'original. Réponds dans la langue du message envoyé.
            
            # Détails
            
            - Emploie des suffixes et des termes propres au style brainrot (par exemple, insérer des mots skibidi, sigma, aura, alpha, ...).
            - Assure-toi de conserver le contexte et le ton du message original.
            - tu ne dois ABSOLUMENT PAS répondre à la phrase de l'utilisateur, mais bien la traduire
            
            # Output Format
            
            - Réponse en français transformée avec le langage "brainrot".
            - Conserver la logique de l’original tout en employant des termes déstructurés.
            
            # Exemples
            
            - Entrée : "Je vais à la boulangerie acheter du pain."
            - Sortie : "Yo, je skibidi vers la boulange pour choper du pain aura style."
            
            - Entrée : "Aujourd'hui, il fait plutôt beau et j'ai envie de sortir."
            - Sortie : "Today, vibe sigma, le soleil alpha me dope, let's go skibidi dehors."
            """;

    public TranslateCommand(SlashCommandInteractionEvent event, String mode, String message) {
        super(event);
        this.userMessage = message;
        if (mode.equals("uwu")) this.currentSystemMessage = gptSystemUwU;
        else if (mode.equals("brainrot")) this.currentSystemMessage = gptSystemBrainrot;

        LOGs.sendLog("Translate command initialisée avec le mode " + mode + " et message \"" + message + "\"", LOGs.LogType.COMMAND);
    }

    @Override
    public void run() {
        currentEvent.deferReply().queue(
                success -> {
                    try {
                        String response = getGPTResponse();
                        if (response != null) {
                            currentEvent.getHook().editOriginal(response).queue();
                            LOGs.sendLog("Translation effectuée"
                                            + "\nUser : @" + currentEvent.getUser().getEffectiveName()
                                            + "\nServeur : " + currentEvent.getGuild().getName()
                                            + "\nSalon : #" + currentEvent.getChannel().getName(),
                                    LOGs.LogType.COMMAND);
                        } else {
                            currentEvent.getHook().editOriginal("Erreur avec ChatGPT").queue();
                            LOGs.sendLog("Erreur sur TranslateCommand"
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

        APICallGPT gptRequest = new APICallGPT(500, currentSystemMessage, userMessage);

        String response = gptRequest.call().getAsJsonArray("choices").get(0).getAsJsonObject()
                .getAsJsonObject("message").get("content").getAsString();

        return response;
    }
}
