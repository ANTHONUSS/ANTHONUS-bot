package fr.anthonus.utils.APICalls;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.anthonus.logs.LOGs;
import fr.anthonus.logs.logTypes.DefaultLogType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public abstract class APICall {
    protected Request currentRequest;
    protected final OkHttpClient client = new OkHttpClient();

    protected JsonObject call() {
        LOGs.sendLog("Envoi de la requête", DefaultLogType.API);

        try (Response response = client.newCall(currentRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            LOGs.sendLog("Requête recue", DefaultLogType.API);

            String jsonData = response.body().string();

            return JsonParser.parseString(jsonData).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
