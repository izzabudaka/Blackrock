package Utility;

import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * Created by Home on 09/03/16.
 */
public class SentimentMap {
    private final static Logger logger = Logger.getLogger(SentimentMap.class);
    private static HashMap<String, Double> sentimentMap = new HashMap<>();

    public static void addSentiment(String country, double sentiment) {
        if(sentimentMap.containsKey(country)){
            sentimentMap.put(country, sentimentMap.get(country) + sentiment);
        } else {
            sentimentMap.put(country, sentiment);
        }
    }

    public static HashMap<String, Double> getSentiments(){
        return sentimentMap;
    }
}