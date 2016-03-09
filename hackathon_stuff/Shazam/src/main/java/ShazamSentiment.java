import Utility.TrackQueue;

/**
 * Created by Home on 09/03/16.
 */
public class ShazamSentiment {
    TrackQueue queue   = new TrackQueue();
    RedisStream stream = new RedisStream(queue);

}
