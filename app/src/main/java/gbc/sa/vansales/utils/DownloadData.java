package gbc.sa.vansales.utils;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class DownloadData extends AsyncTask<Void, Void, Void>{

    private String tripId;
    private String collectionName;
    private String expansionName;
    private String[] expansionSets;
    private DatabaseHandler db;

    public DownloadData(String tripId, String collectionName, String expansionName, DatabaseHandler db) {
        this.tripId = tripId;
        this.collectionName = collectionName;
        this.expansionName = expansionName;
        this.db = db;
        execute();
    }

    public DownloadData(String tripId,String collectionName,String[] expansionSets, DatabaseHandler db) {
        this.tripId = tripId;
        this.collectionName = collectionName;
        this.expansionSets = expansionSets;
        this.db = db;
        execute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        return null;
    }
}
