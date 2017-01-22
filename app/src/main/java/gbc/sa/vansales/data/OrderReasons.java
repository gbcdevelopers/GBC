package gbc.sa.vansales.data;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.Reasons;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class OrderReasons {

    private static final String COLLECTION_NAME = "OrderReasonSet";
    private static final String TRIP_ID = "ITripId";

    private static ArrayList<Reasons> data = new ArrayList<>();

    public static void load(Context context,String tripId, DatabaseHandler db){
        HashMap<String, String> params = new HashMap<>();
        HashMap<String,String>expansion = new HashMap<>();

        new DownloadData(context,COLLECTION_NAME,params,expansion,db);
    }

    public static void loadData(Context context){
        data.clear();
        DatabaseHandler db = new DatabaseHandler(context);
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_REASON_TYPE, "");
        map.put(db.KEY_REASON_DESCRIPTION, "");
        map.put(db.KEY_REASON_CODE, "");
        HashMap<String, String> filter = new HashMap<>();
        Cursor cursor = db.getData(db.REASONS, map, filter);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Reasons reasons = new Reasons();
                reasons.setReasonID(cursor.getString(cursor.getColumnIndex(db.KEY_REASON_CODE)));
                reasons.setReasonType(cursor.getString(cursor.getColumnIndex(db.KEY_REASON_TYPE)));
                reasons.setReasonDescription(cursor.getString(cursor.getColumnIndex(db.KEY_REASON_DESCRIPTION)));
                data.add(reasons);
            }
            while (cursor.moveToNext());
        }
    }

    public static ArrayList<Reasons> get() {
        return data;
    }
}
