import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.apache.log4j.BasicConfigurator;

/**
 * Created by Home on 09/03/16.
 */
public class ShazamSentiment {
    public static void main(String[] args){
        BasicConfigurator.configure();
        VertxOptions options = new VertxOptions();
        options.setMaxEventLoopExecuteTime(Long.MAX_VALUE);
        Vertx vertx = Vertx.vertx(options);
        vertx.deployVerticle(ShazamVerticle.class.getName());
    }
}
