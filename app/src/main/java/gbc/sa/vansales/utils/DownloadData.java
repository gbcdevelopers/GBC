package gbc.sa.vansales.utils;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class DownloadData extends AsyncTask<Void, Void, Void>{

    private HashMap<String,String> params;
    private String collectionName;
    private HashMap<String,String>expansions;
    private DatabaseHandler db;

    public DownloadData(String collectionName,HashMap<String, String> parameters, HashMap<String, String> expansion, DatabaseHandler db) {
        this.params = parameters;
        this.collectionName = collectionName;
        this.expansions = expansion;
        this.db = db;
        execute();
    }


    @Override
    protected Void doInBackground(Void... params) {
        String url = UrlBuilder.buildExpansion(this.collectionName, this.params, this.expansions);
        Log.e("URL in DD",url);
        return null;
    }
}
