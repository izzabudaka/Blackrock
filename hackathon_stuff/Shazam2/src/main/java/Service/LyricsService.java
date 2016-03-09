package Service;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import org.json.JSONObject;

/**
 * Created by Home on 09/03/16.
 */
public class LyricsService {
    private final HttpClient client;
    private String url = "cdn.shazam.com";
    private SentimentService sentimentService;

    public LyricsService(HttpClient client) {
        this.client = client;
        sentimentService = new SentimentService(client);
    }

    public void run(String country, String trackId) {
            String sendUrl = String.format("/discovery/v1/en/US/web/-/track/%s", trackId);
            System.out.println(sendUrl);
            System.out.println(trackId);
            HttpClientRequest request = client.get(url, sendUrl, new Handler<HttpClientResponse>() {
                @Override
                public void handle(HttpClientResponse httpClientResponse) {
                    httpClientResponse.bodyHandler(new Handler<Buffer>() {
                        @Override
                        public void handle(Buffer buffer) {
                            System.out.println("Response (" + buffer.length() + "): ");
                            String jsonLyrics  = buffer.getString(0, buffer.length());
                            JSONObject jsonObj = new JSONObject(jsonLyrics);
                            try{
                                String lyrics      = jsonObj.getJSONObject("content").getJSONObject("lyrics").getString("text");
                                System.out.println(lyrics);
                                sentimentService.run(country, lyrics);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        //request.putHeader("content-type", "application/json");
        request.end();
        }
}
