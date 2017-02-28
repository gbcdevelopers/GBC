package gbc.sa.vansales.data;
import android.content.Context;

import java.util.HashMap;

import gbc.sa.vansales.models.FOC;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
/**
 * Created by Rakshit on 03-Feb-17.
 */
public class FOCData {

    private static final String COLLECTION_NAME = "Promotion02Set";
    private static final String DRIVER = "Driver";

    public static void load(Context context,String driver, DatabaseHandler db){
       /* HashMap<String, String> params = new HashMap<>();
        params.put(DRIVER,driver);
        HashMap<String,String>expansion = new HashMap<>();

        new DownloadData(context,COLLECTION_NAME,params,expansion,db);*/
       // generateData(db);
    }

    public static void generateData(DatabaseHandler db){
        HashMap<String,String>map = new HashMap<>();
        map.put(db.KEY_CUSTOMER_NO,"0000200002");
        map.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000003");
        map.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map.put(db.KEY_FOC_ASSIGNING_QUANTITY,"1");
        map.put(db.KEY_FOC_DATE_FROM,"2017.02.03");
        map.put(db.KEY_FOC_DATE_TO,"2017.02.12");
        db.addData(db.FOC_RULES, map);

        HashMap<String,String>map1 = new HashMap<>();
        map1.put(db.KEY_CUSTOMER_NO,"0000200002");
        map1.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000003");
        map1.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000009");
        map1.put(db.KEY_FOC_QUALIFYING_QUANTITY,"5");
        map1.put(db.KEY_FOC_ASSIGNING_QUANTITY,"1");
        map1.put(db.KEY_FOC_DATE_FROM,"2017.02.03");
        map1.put(db.KEY_FOC_DATE_TO, "2017.02.12");
        db.addData(db.FOC_RULES, map1);

        HashMap<String,String>map2 = new HashMap<>();
        map2.put(db.KEY_CUSTOMER_NO,"0000200002");
        map2.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000020");
        map2.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000021");
        map2.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map2.put(db.KEY_FOC_ASSIGNING_QUANTITY,"2");
        map2.put(db.KEY_FOC_DATE_FROM,"2017.02.03");
        map2.put(db.KEY_FOC_DATE_TO,"2017.02.12");
        db.addData(db.FOC_RULES, map2);
    }

}
