package fr.anthonus.utils.APICalls;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.anthonus.logs.LOGs;
import fr.anthonus.Main;
import fr.anthonus.logs.logTypes.CustomLogType;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class APICallGPT extends APICall {

    public APICallGPT(int maxTokens, String systemMessage, String userMessage) {
        LOGs.sendLog("Création de la requête pour chatGPT", CustomLogType.API);
        JsonObject json = new JsonObject();
        json.addProperty("model", "gpt-4o-mini");
        json.addProperty("max_tokens", maxTokens);

        JsonArray messages = new JsonArray();

        JsonObject systemMessageJson = new JsonObject();
        systemMessageJson.addProperty("role", "system");
        systemMessageJson.addProperty("content", systemMessage);
        messages.add(systemMessageJson);

        JsonObject userMessageJson = new JsonObject();
        userMessageJson.addProperty("role", "user");
        userMessageJson.addProperty("content", userMessage);
        messages.add(userMessageJson);

        json.add("messages", messages);

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + Main.tokenOpenAI)
                .post(body)
                .build();

        LOGs.sendLog("requête pour ChatGPT créée", CustomLogType.API);

        currentRequest = request;
    }

    @Override
    public JsonObject call() {
        return super.call();
    }
}
