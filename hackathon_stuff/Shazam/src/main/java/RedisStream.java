import Utility.TrackQueue;
import com.google.gson.Gson;

public class RedisStream {

    private static final Gson gson = new Gson();
    private final TrackQueue trackQueue;

    public RedisStream(TrackQueue trackQueue){
        this.trackQueue = trackQueue;
    }

    private void run(){
        ReadFromRedis redisStream = new ReadFromRedis("redis://colin.dev.shazamteam.net:6379");
        redisStream.observe()
                .onBackpressureLatest()
                .map(rawJson -> tagFromJson(rawJson))
                .filter(tag -> tag.match.track.metadata != null)
                .toBlocking()
                .subscribe( tag-> {
                    trackQueue.addTrack(tag.match.track.id);
                });
    }

    private static Tag tagFromJson(String rawTag) {
        return gson.fromJson(rawTag, Tag.class);
    }

    private static class Tag {
        private Match match;
    }

    private static class Match {
        private Track track;
    }

    private static class Track {
        private Metadata metadata;
        private String id;
    }

    private static class Metadata {
        String artistname;
        String tracktitle;

        @Override
        public String toString() {
            return String.format("%s by %s %s", tracktitle, artistname);
        }
    }
}
