package fr.anthonus.utils.APICalls;

import com.google.gson.JsonObject;
import fr.anthonus.LOGs;
import okhttp3.Request;

public class APICallReddit extends APICall {

    public APICallReddit(String url, String headerName, String headerValue) {
        LOGs.sendLog("Création de la requête pour Reddit", "API");
        currentRequest = new Request.Builder()
                .url(url)
                .header(headerName, headerValue)
                .build();

        LOGs.sendLog("requête pour Reddit créée", "API");
    }

    @Override
    public JsonObject call() {
        return super.call();
    }
}
