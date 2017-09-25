package gbc.sa.vansales.data;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.DashboardActivity;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Rakshit on 17-Dec-16.
 */
public class TripHeader {
    private static final String COLLECTION_NAME = "TripHdSet";
    private static final String TRIP_SALES_AREA = "TripSalesArea";
    private static final String TRIP_ID = "ITripId";

    public static void load(Context context,String tripId, DatabaseHandler db){
       // Log.e("Inside TH","TH");
        HashMap<String, String>params = new HashMap<>();
        params.put(TRIP_ID,tripId);

        //params.put(TRIP_ID,"0000040000000044");
        HashMap<String,String>expansion = new HashMap<>();
        expansion.put(TRIP_SALES_AREA,TRIP_SALES_AREA);

        new DownloadData(context,COLLECTION_NAME,params,expansion,db);
        // new downloadData("GBC012000000001");
    }
}
