package gbc.sa.vansales.data;
import android.content.Context;

import java.util.HashMap;

import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class ArticleHeader {
    private static final String COLLECTION_NAME = "ArticleHeaderSet";
    private static final String ARTICLE_ALT_UOM = "ArticleAltuom";
    private static final String ARTICLE_SALES_AREAS = "ArticleSalesareas";
    private static final String TRIP_ID = "ITripId";

    public static void load(Context context,String tripId, DatabaseHandler db){
        HashMap<String, String> params = new HashMap<>();
        params.put(TRIP_ID,tripId);

        HashMap<String,String>expansion = new HashMap<>();
        expansion.put(ARTICLE_ALT_UOM,ARTICLE_ALT_UOM);
        expansion.put(ARTICLE_SALES_AREAS,ARTICLE_SALES_AREAS);

        new DownloadData(context,COLLECTION_NAME,params,expansion,db);
    }
}
