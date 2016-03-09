import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.pubsub.api.rx.RedisPubSubReactiveCommands;
import rx.Observable;

class ReadFromRedis {
    private RedisPubSubReactiveCommands<String, String> commands;

    public ReadFromRedis(String uri) {
        RedisClient redisClient = RedisClient.create(uri);
        this.commands = redisClient.connectPubSub().reactive();
    }

    Observable<String> observe() {
        commands.subscribe("tags").subscribe();
        return commands.observeChannels()
                .map(pm -> pm.getMessage())
                .filter(message -> !message.equals("tags"));
    }
}
