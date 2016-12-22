package gbc.sa.vansales.utils;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.activities.LoginActivity;
import gbc.sa.vansales.sap.IntegrationService;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class DownloadData extends AsyncTask<Void, Void, Void>{

    private HashMap<String,String> params;
    private String collectionName;
    private HashMap<String,String>expansions;
    private DatabaseHandler db;
    private Context context;

    public DownloadData(Context context,String collectionName,HashMap<String, String> parameters, HashMap<String, String> expansion, DatabaseHandler db) {
        this.params = parameters;
        this.collectionName = collectionName;
        this.expansions = expansion;
        this.db = db;
        this.context = context;
        execute();
    }


    @Override
    protected Void doInBackground(Void... params) {
        String url = UrlBuilder.buildExpansion(this.collectionName, this.params, this.expansions);
        JSONObject jsonObject = IntegrationService.getService(this.context,url);
        Log.e("Exp Response", ""+ jsonObject);

        try {
            Log.e("Metadata","" + jsonObject.getJSONObject("__metadata").getString("type"));
            String metadata = jsonObject.getJSONObject("__metadata").getString("type");
            parseJSON(metadata,jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    void parseJSON(String metadata,JSONObject jsonObject){
        switch (metadata){
            case ConfigStore.TripHeaderEntity:

                break;
            case ConfigStore.LoadDeliveryEntity:

                break;
            case ConfigStore.ArticleHeaderEntity:

                break;
            case ConfigStore.VisitListEntity:

                break;
            case ConfigStore.CustomerHeaderEntity:

                break;
        }
    }
}

