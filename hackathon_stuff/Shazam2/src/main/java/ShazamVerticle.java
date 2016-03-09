import Service.LyricsService;
import Utility.SentimentMap;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;

/**
 * Created by Home on 09/03/16.
 */
public class ShazamVerticle extends AbstractVerticle {
    private final static Logger logger = Logger.getLogger(ShazamVerticle.class);

    @Override
    public void start(Future<Void> fut) {
        HttpServer server           = vertx.createHttpServer();
        Router router               = Router.router(vertx);
        HttpClient httpClient       = vertx.createHttpClient();
        LyricsService lyricsService = new LyricsService(httpClient);
        //lyricsService.run("GB","276475110");
        RedisStream stream          = new RedisStream(lyricsService);
        stream.run();

        router.get("/").handler(routingContext -> {
            final CompletableFuture<JSONObject> result = new CompletableFuture<>();
            result.complete(new JSONObject(SentimentMap.getSentiments()));
            result.thenAccept(x -> routingContext.response().end(String.valueOf(x)));
        });
        server.requestHandler(router::accept).listen(8007);
    }
}
