package gbc.sa.vansales.data;
import android.app.AlertDialog;
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
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Rakshit on 17-Dec-16.
 */
public class TripHeader {
    private static final String COLLECTION_NAME = "TripHdSet";
    private static final String EXPANSION_NAME = "TripSalesArea";
    private static final String TRIP_ID = "ITripId";
    LoadingSpinner loadingSpinner;

    public static void load(String tripId, DatabaseHandler db){
        new downloadData("GBC012000000001");
    }

    private static class downloadData extends AsyncTask<Void,Void,Void> {

        private String url;
        private String jsonResponse;

        private downloadData(String tripid) {

            HashMap<String, String> map = new HashMap<>();
            map.put(TRIP_ID, tripid);
            this.url = UrlBuilder.build(COLLECTION_NAME, null, map);
            this.url = url + "&$expand=" + EXPANSION_NAME;
            execute();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            this.jsonResponse = IntegrationService.loadData(this.url);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                JSONObject jsonObj = new JSONObject(this.jsonResponse);
                jsonObj = jsonObj.getJSONObject("d");
                JSONArray jsonArray = jsonObj.getJSONArray("results");
                jsonObj = jsonArray.getJSONObject(0);
                Log.e("JSON Trip", "" + jsonObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
