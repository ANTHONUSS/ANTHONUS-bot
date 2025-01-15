package fr.ANTHONUSApps.Commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.ANTHONUSApps.LOGs;
import fr.ANTHONUSApps.Main;
import fr.ANTHONUSApps.Utils.APICall;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class TranslateCommand {
    private SlashCommandInteractionEvent currentEvent;
    private String message;
    private Mode mode;
    private final String API_KEY = Main.tokenIA;

    private final String gptSystemUwU = """
            Convertis un message écrit en français en style UWU-Girl/E-Girl/cat-girl. Ajoute des emojis et adapte le texte pour refléter ce style distinctif, tout en gardant la structure et le sens de l'original. Réponds toujours en français.
            
            # Détails
            
            - Emploie des suffixes et des termes propres au style UWU/E-Girl (par exemple, remplacer "r" par "w", ajouter "nya" à la fin des phrases).
            - Ajoute des emojis mignons ou appropriés tels que 😺, 🌸, et ✨.
            - Assure-toi de conserver le contexte et le ton du message original.
            
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
            
            # Output Format
            
            - Réponse en français transformée avec le langage "brainrot".
            - Conserver la logique de l’original tout en employant des termes déstructurés.
            
            # Exemples
            
            - Entrée : "Je vais à la boulangerie acheter du pain."
            - Sortie : "Yo, je skibidi vers la boulange pour choper du pain aura style."
            
            - Entrée : "Aujourd'hui, il fait plutôt beau et j'ai envie de sortir."
            - Sortie : "Today, vibe sigma, le soleil alpha me dope, let's go skibidi dehors."
            """;

    private enum Mode {
        UWU, BRAINROT
    }

    public TranslateCommand(SlashCommandInteractionEvent event, String mode, String message) {
        this.currentEvent = event;
        this.message = message;
        if(mode.equals("uwu")) this.mode = Mode.UWU;
        else if(mode.equals("brainrot")) this.mode = Mode.BRAINROT;
    }

    public void run(){
        try {
            String response = getGPTResponse();
            if (response != null) {
                currentEvent.reply(response).queue();
                LOGs.sendLog("Translation effectuée"
                                + "\nUser : @" + currentEvent.getUser().getEffectiveName()
                                + "\nServeur : " + currentEvent.getGuild().getName()
                                + "\nSalon : #" + currentEvent.getChannel().getName()
                                + "\nMode : " + mode,
                        LOGs.LogType.CURSED);
            } else {
                currentEvent.reply("Erreur avec ChatGPT")
                        .setEphemeral(true)
                        .queue();
                LOGs.sendLog("Erreur sur CursedImage"
                                + "\nUser : @" + currentEvent.getUser().getEffectiveName()
                                + "\nServeur : " + currentEvent.getGuild().getName()
                                + "\nSalon : #" + currentEvent.getChannel().getName(),
                        LOGs.LogType.ERROR);
            }
        } catch (Exception e) {
            currentEvent.reply("Une erreur est survenue lors de la rcommunication avec ChatGPT" + e.getMessage()).queue();
            e.printStackTrace();
        }
    }

    private String getGPTResponse(){
        Request request = requestBuilder();

        APICall gptRequest = new APICall(request);
        String response = gptRequest.call().getAsJsonArray("choices").get(0).getAsJsonObject()
                .getAsJsonObject("message").get("content").getAsString();

        return response;
    }

    private Request requestBuilder(){
        JsonObject json = new JsonObject();
        json.addProperty("model", "gpt-4o-mini");
        json.addProperty("max_tokens", 500);

        JsonArray messages = new JsonArray();

        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        if(mode==Mode.UWU) systemMessage.addProperty("content", gptSystemUwU);
        else if(mode==Mode.BRAINROT) systemMessage.addProperty("content", gptSystemBrainrot);
        messages.add(systemMessage);

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", message);
        messages.add(userMessage);

        json.add("messages", messages);

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        return request;
    }
}
