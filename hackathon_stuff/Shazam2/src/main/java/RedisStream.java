import Service.LyricsService;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

public class RedisStream {

    private static final Gson gson     = new Gson();
    private final static Logger logger = Logger.getLogger(RedisStream.class);
    private final LyricsService lyricsService;

    public RedisStream(LyricsService lyricsService) {
        this.lyricsService = lyricsService;
    }

    public void run(){
        ReadFromRedis redisStream = new ReadFromRedis("redis://colin.dev.shazamteam.net:6379");
        redisStream.observe()
                .onBackpressureLatest()
                .map(rawJson -> tagFromJson(rawJson))
                .filter(tag -> tag.match.track.metadata != null)
                .subscribe(tag -> {
                    lyricsService.run(tag.installation.country, tag.match.track.id);
                });
    }

    private static Tag tagFromJson(String rawTag) {
        return gson.fromJson(rawTag, Tag.class);
    }

    private static class Tag {
        private Match match;
        private Installation installation;
    }

    private static class Installation{
        private String country;
    }
    private static class Match {
        private Track track;
    }

    private static class Track {
        private Metadata metadata;
        private String id;
        private String country;
    }

    private static class Metadata {
        String artistname;
        String tracktitle;

        @Override
        public String toString() {
            return String.format("%s by %s", tracktitle, artistname);
        }
    }
}
