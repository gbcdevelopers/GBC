package gbc.sa.vansales.data;
import android.content.Context;

import java.util.HashMap;

import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
/**
 * Created by Rakshit on 04-Jan-17.
 */
public class CustomerDelivery {
    private static final String COLLECTION_NAME = "CustomerDelHDSet";
    private static final String CUST_DEL_ITEMS = "CustDelItems";
    private static final String TRIP_ID = "ITripId";

    public static void load(Context context,String tripId, DatabaseHandler db){

        HashMap<String, String> params = new HashMap<>();
        params.put(TRIP_ID,tripId);

        HashMap<String,String>expansion = new HashMap<>();
        expansion.put(CUST_DEL_ITEMS,CUST_DEL_ITEMS);

        new DownloadData(context,COLLECTION_NAME,params,expansion,db);
    }
}
