package fr.ANTHONUSApps.Utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class APICall {
    private Request currentRequest;

    private final OkHttpClient client = new OkHttpClient();

    public APICall(Request request) {
        this.currentRequest = request;
    }

    public JsonObject call() {
        try (Response response = client.newCall(currentRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String jsonData = response.body().string();

            return JsonParser.parseString(jsonData).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
