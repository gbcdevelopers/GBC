package gbc.sa.vansales.data;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
import gbc.sa.vansales.utils.LoadingSpinner;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class LoadDelivery {
    private static final String COLLECTION_NAME = "LoadDeliveryHdSet";
    private static final String EXPANSION_NAME = "LoadDelItems";
    private static final String TRIP_ID = "ITripId";

    public static void load(String tripId, DatabaseHandler db){
        new DownloadData(tripId,COLLECTION_NAME,EXPANSION_NAME,db);
    }
}
