package gbc.sa.vansales.data;
import android.content.Context;

import java.util.HashMap;

import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class Odometer {

    private static final String COLLECTION_NAME = "TRIP_IDSet";
    private static final String DRIVER = "Driver";

    public static void load(Context context,String tripID, DatabaseHandler db){
        HashMap<String, String> params = new HashMap<>();
        tripID = tripID.toUpperCase();
        params.put(DRIVER,tripID);
        HashMap<String,String>expansion = new HashMap<>();

        new DownloadData(context,COLLECTION_NAME,params,expansion,db);
    }
}
