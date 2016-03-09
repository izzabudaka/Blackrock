package Utility;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Home on 09/03/16.
 */
public class TrackQueue {
    private LinkedBlockingQueue queue = new LinkedBlockingQueue();

    public void addTrack(String name){
        queue.add(name);
    }

    public String getTrack(){
        return (String) queue.poll();
    }
}
