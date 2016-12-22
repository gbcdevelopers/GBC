package gbc.sa.vansales.data;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class VisitList {

    private static final String COLLECTION_NAME = "VisitListSet";
    private static final String TRIP_ID = "ITripId";

    public static void load(String tripId, DatabaseHandler db){
        new DownloadData(tripId,COLLECTION_NAME,"",db);
    }
}
