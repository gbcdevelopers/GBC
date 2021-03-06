package gbc.sa.vansales.data;
import android.content.Context;

import java.util.HashMap;

import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class Pricing {

    private static final String COLLECTION_NAME = "PricingSet";
    private static final String DRIVER = "Driver";

    public static void load(Context context,String driver, DatabaseHandler db){
        HashMap<String, String> params = new HashMap<>();
        driver = driver.toUpperCase();
        params.put(DRIVER,driver);
        HashMap<String,String>expansion = new HashMap<>();

        new DownloadData(context,COLLECTION_NAME,params,expansion,db);
    }
}
