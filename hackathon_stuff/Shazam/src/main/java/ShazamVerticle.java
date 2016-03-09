import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.apache.log4j.Logger;

/**
 * Created by Home on 09/03/16.
 */
public class ShazamVerticle extends AbstractVerticle {
    private final static Logger logger = Logger.getLogger(ShazamVerticle.class);
    @Override
    public void start(Future<Void> fut) {
        HttpServer server                  = vertx.createHttpServer();
        Router router                  = Router.router(vertx);


        server.requestHandler(router::accept).listen(8080);
    }
}
