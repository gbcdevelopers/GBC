package gbc.sa.vansales.data;
import android.content.Context;

import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
import gbc.sa.vansales.utils.Settings;
/**
 * Created by Rakshit on 02-Jan-17.
 */
public class Messages {
    private static final String COLLECTION_NAME = "MessagesSet";
    private static final String DRIVER = "Driver";

    public static void load(Context context,String driver, DatabaseHandler db){
        HashMap<String, String> params = new HashMap<>();
        params.put(DRIVER, driver);

        HashMap<String,String>expansion = new HashMap<>();

        new DownloadData(context,COLLECTION_NAME,params,expansion,db);
    }
}
