package gbc.sa.vansales.data;
import java.util.HashMap;

import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class VisitList {

    private static final String COLLECTION_NAME = "VisitListSet";
    private static final String TRIP_ID = "ITripId";

    public static void load(String tripId, DatabaseHandler db){
        HashMap<String, String> params = new HashMap<>();
        params.put(TRIP_ID,tripId);

        HashMap<String,String>expansion = new HashMap<>();

        new DownloadData(COLLECTION_NAME,params,expansion,db);
    }
}
