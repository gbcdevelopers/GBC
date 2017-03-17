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
        generateData(db);
    }

    public static void generateData(DatabaseHandler db){
        HashMap<String,String>map = new HashMap<>();
        map.put(db.KEY_CUSTOMER_NO,"");
        map.put(db.KEY_DIST_CHANNEL,"");
        map.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map.put(db.KEY_FOC_ASSIGNING_QUANTITY, "1");
        map.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map);

        HashMap<String,String>map1 = new HashMap<>();
        map1.put(db.KEY_CUSTOMER_NO,"");
        map1.put(db.KEY_DIST_CHANNEL,"");
        map1.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000003");
        map1.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map1.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map1.put(db.KEY_FOC_ASSIGNING_QUANTITY,"1");
        map1.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map1.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map1);

        HashMap<String,String>map2 = new HashMap<>();
        map2.put(db.KEY_CUSTOMER_NO,"");
        map2.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000008");
        map2.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map2.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map2.put(db.KEY_FOC_ASSIGNING_QUANTITY,"1");
        map2.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map2.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map2);

        HashMap<String,String>map3 = new HashMap<>();
        map3.put(db.KEY_CUSTOMER_NO,"");
        map3.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000006");
        map3.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map3.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map3.put(db.KEY_FOC_ASSIGNING_QUANTITY,"1");
        map3.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map3.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map3);

        /*HashMap<String,String>map2 = new HashMap<>();
        map2.put(db.KEY_CUSTOMER_NO,"0000200513");
        map2.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000020");
        map2.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000021");
        map2.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map2.put(db.KEY_FOC_ASSIGNING_QUANTITY,"2");
        map2.put(db.KEY_FOC_DATE_FROM,"2017.03.03");
        map2.put(db.KEY_FOC_DATE_TO,"2017.03.12");
        db.addData(db.FOC_RULES, map2);

        HashMap<String,String>map3 = new HashMap<>();
        map3.put(db.KEY_CUSTOMER_NO,"0000200513");
        map3.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000020");
        map3.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000021");
        map3.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map3.put(db.KEY_FOC_ASSIGNING_QUANTITY,"2");
        map3.put(db.KEY_FOC_DATE_FROM,"2017.03.03");
        map3.put(db.KEY_FOC_DATE_TO,"2017.03.12");
        db.addData(db.FOC_RULES, map3);

        HashMap<String,String>map4 = new HashMap<>();
        map4.put(db.KEY_CUSTOMER_NO,"0000200513");
        map4.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map4.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000003");
        map4.put(db.KEY_FOC_QUALIFYING_QUANTITY,"5");
        map4.put(db.KEY_FOC_ASSIGNING_QUANTITY,"");
        map4.put(db.KEY_FOC_DATE_FROM,"2017.03.03");
        map4.put(db.KEY_FOC_DATE_TO,"2017.03.12");
        db.addData(db.FOC_RULES, map4);*/
    }

}
