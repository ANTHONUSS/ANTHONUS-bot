package fr.ANTHONUSApps.Utils.APICalls;

import com.google.gson.JsonObject;
import fr.ANTHONUSApps.LOGs;
import okhttp3.Request;

public class APICallReddit extends APICall {

    public APICallReddit(String url, String headerName, String headerValue) {
        LOGs.sendLog("Création de la requête pour Reddit", LOGs.LogType.API);
        currentRequest = new Request.Builder()
                .url(url)
                .header(headerName, headerValue)
                .build();

        LOGs.sendLog("requête pour Reddit créée", LOGs.LogType.API);
    }

    @Override
    public JsonObject call() {
        return super.call();
    }
}
