package fr.anthonus.utils.api;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.SettingsLoader;

public class OpenAIAPI {
    private static final OpenAIClient client;

    static {
        client = OpenAIOkHttpClient.builder()
                .apiKey(SettingsLoader.getTokenOpenAI())
                .build();
    }

    public static String getChatGPTResponse(String personality, String message) {
        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(ChatModel.GPT_4_1_NANO)
                .maxOutputTokens(512)
                .instructions(personality)
                .input(message)
                .build();

        LOGs.sendLog("Envoi de la requête à OpenAI", DefaultLogType.API);
        Response response = client.responses().create(params);
        LOGs.sendLog("Réponse reçue de OpenAI", DefaultLogType.API);

        return response.output().get(0).message().get().content().get(0).asOutputText().text();
    }

}
