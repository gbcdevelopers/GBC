package gbc.sa.vansales.data;
import android.content.Context;

import java.util.HashMap;

import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
import gbc.sa.vansales.utils.LoadingSpinner;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class LoadDelivery {
    private static final String COLLECTION_NAME = "LoadDeliveryHdSet";
    private static final String LOAD_DEL_ITEMS = "LoadDelItems";
    private static final String TRIP_ID = "ITripId";

    public static void load(Context context,String tripId, DatabaseHandler db){

        HashMap<String, String> params = new HashMap<>();
        params.put(TRIP_ID,tripId);
      //  params.put(TRIP_ID,"GBC012000000008");

        HashMap<String,String>expansion = new HashMap<>();
        expansion.put(LOAD_DEL_ITEMS,LOAD_DEL_ITEMS);

        new DownloadData(context,COLLECTION_NAME,params,expansion,db);
    }
}
