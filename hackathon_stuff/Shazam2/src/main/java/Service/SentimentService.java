package Service;

import Utility.SentimentMap;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import org.json.JSONObject;

/**
 * Created by Home on 09/03/16.
 */
public class SentimentService {
    private final HttpClient client;

    public SentimentService(HttpClient client) {
        this.client = client;
    }

    public void run(String country, String lyrics) {
        String body ="apikey=8986f3d10be3379f4a1d21fcf04d2d64305df133&outputMode=json&text="+lyrics;
        HttpClientRequest request = client.post("gateway-a.watsonplatform.net", "/calls/text/TextGetTextSentiment", new Handler<HttpClientResponse>() {
            @Override
            public void handle(HttpClientResponse httpClientResponse) {
                httpClientResponse.bodyHandler(new Handler<Buffer>() {
                    @Override
                    public void handle(Buffer buffer) {
                        System.out.println("Response (" + buffer.length() + "): ");
                        String jsonLyrics   = buffer.getString(0, buffer.length());
                        JSONObject jsonObj  = new JSONObject(jsonLyrics);
                        double sentiment    = jsonObj.getJSONObject("docSentiment").getDouble("score");
                        SentimentMap.addSentiment(country, sentiment);
                    }
                });
            }
        });
        request.putHeader("content-length", String.valueOf(body.length()));
        request.end(body);
    }
}

