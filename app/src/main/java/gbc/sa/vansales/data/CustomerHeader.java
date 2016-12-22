package gbc.sa.vansales.data;
import android.content.Context;

import java.util.HashMap;

import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class CustomerHeader {

    private static final String COLLECTION_NAME = "CustomerHeaderSet";
    private static final String CUSTOMER_SALES_AREAS = "CustomerSalesAreas";
    private static final String CUSTOMER_OPEN_ITEMS = "CustomerOpenItems";
    private static final String CUSTOMER_CREDIT = "CustomerCredit";
    private static final String TRIP_ID = "ITripId";

    public static void load(Context context,String tripId, DatabaseHandler db){
        HashMap<String, String> params = new HashMap<>();
        params.put(TRIP_ID,tripId);

        HashMap<String,String>expansion = new HashMap<>();
        expansion.put(CUSTOMER_SALES_AREAS,CUSTOMER_SALES_AREAS);
        expansion.put(CUSTOMER_OPEN_ITEMS,CUSTOMER_OPEN_ITEMS);
        expansion.put(CUSTOMER_CREDIT,CUSTOMER_CREDIT);

        new DownloadData(context,COLLECTION_NAME,params,expansion,db);
    }
}
