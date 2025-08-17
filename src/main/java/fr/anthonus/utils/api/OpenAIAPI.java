package fr.anthonus.utils.api;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import fr.anthonus.utils.settings.SettingsLoader;

import java.util.List;

public class OpenAIAPI {
    private static final OpenAIClient client;

    static {
        client = OpenAIOkHttpClient.builder()
                .apiKey(SettingsLoader.getTokenOpenAI())
                .build();
    }

    public static String getChatGPTResponse(String personality, List<String> messages) {
        LOGs.sendLog("Création de la requête pour OpenAI", DefaultLogType.API);

        ChatCompletionCreateParams.Builder paramsBuilder = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_4O_MINI)
                .addSystemMessage(personality);

        for (String message : messages) {
            paramsBuilder.addUserMessage(message);
        }

        ChatCompletionCreateParams params = paramsBuilder.build();

        LOGs.sendLog("Envoi de la requête à OpenAI", DefaultLogType.API);
        ChatCompletion response = client.chat().completions().create(params);
        LOGs.sendLog("Réponse reçue de OpenAI", DefaultLogType.API);

        return response.choices().get(0).message().content().get();
    }

}
