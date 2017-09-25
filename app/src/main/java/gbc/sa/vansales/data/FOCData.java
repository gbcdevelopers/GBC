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

    private static final String COLLECTION_NAME = "FreeGoodsSet";
    private static final String DRIVER = "Driver";

    public static void load(Context context,String driver, DatabaseHandler db){
        HashMap<String, String> params = new HashMap<>();
        params.put(DRIVER,driver);
        HashMap<String,String>expansion = new HashMap<>();

        new DownloadData(context,COLLECTION_NAME,params,expansion,db);
        //generateData(db);
    }

    public static void generateData(DatabaseHandler db){
        /*HashMap<String,String>map = new HashMap<>();
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
        db.addData(db.FOC_RULES, map3);*/

        HashMap<String,String>map = new HashMap<>();
        map.put(db.KEY_CUSTOMER_NO,"");
        map.put(db.KEY_DIST_CHANNEL,"20");
        map.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000000");
        map.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map);

        HashMap<String,String>map1 = new HashMap<>();
        map1.put(db.KEY_CUSTOMER_NO,"");
        map1.put(db.KEY_DIST_CHANNEL,"20");
        map1.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map1.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000000");
        map1.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map1.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map1.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map1.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map1);

        HashMap<String,String>map2 = new HashMap<>();
        map2.put(db.KEY_CUSTOMER_NO,"");
        map2.put(db.KEY_DIST_CHANNEL,"20");
        map2.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000002");
        map2.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000002");
        map2.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map2.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map2.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map2.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map2);

        HashMap<String,String>map3 = new HashMap<>();
        map3.put(db.KEY_CUSTOMER_NO,"");
        map3.put(db.KEY_DIST_CHANNEL,"20");
        map3.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000002");
        map3.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000002");
        map3.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map3.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map3.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map3.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map3);

        HashMap<String,String>map4 = new HashMap<>();
        map4.put(db.KEY_CUSTOMER_NO,"");
        map4.put(db.KEY_DIST_CHANNEL,"20");
        map4.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000003");
        map4.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000003");
        map4.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map4.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map4.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map4.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map4);

        HashMap<String,String>map5 = new HashMap<>();
        map5.put(db.KEY_CUSTOMER_NO,"");
        map5.put(db.KEY_DIST_CHANNEL,"20");
        map5.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000003");
        map5.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000003");
        map5.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map5.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map5.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map5.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map5);

        HashMap<String,String>map6 = new HashMap<>();
        map6.put(db.KEY_CUSTOMER_NO,"");
        map6.put(db.KEY_DIST_CHANNEL,"20");
        map6.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000004");
        map6.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000004");
        map6.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map6.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map6.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map6.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map6);

        HashMap<String,String>map7 = new HashMap<>();
        map7.put(db.KEY_CUSTOMER_NO,"");
        map7.put(db.KEY_DIST_CHANNEL,"20");
        map7.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000004");
        map7.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000004");
        map7.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map7.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map7.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map7.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map7);

        HashMap<String,String>map8 = new HashMap<>();
        map8.put(db.KEY_CUSTOMER_NO,"");
        map8.put(db.KEY_DIST_CHANNEL,"20");
        map8.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000006");
        map8.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map8.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map8.put(db.KEY_FOC_ASSIGNING_QUANTITY, "1");
        map8.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map8.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map8);

        HashMap<String,String>map9 = new HashMap<>();
        map9.put(db.KEY_CUSTOMER_NO,"");
        map9.put(db.KEY_DIST_CHANNEL,"20");
        map9.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000006");
        map9.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map9.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map9.put(db.KEY_FOC_ASSIGNING_QUANTITY, "3");
        map9.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map9.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map9);

        HashMap<String,String>map10 = new HashMap<>();
        map10.put(db.KEY_CUSTOMER_NO,"");
        map10.put(db.KEY_DIST_CHANNEL,"20");
        map10.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000008");
        map10.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000008");
        map10.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map10.put(db.KEY_FOC_ASSIGNING_QUANTITY, "1");
        map10.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map10.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map10);

        HashMap<String,String>map11 = new HashMap<>();
        map11.put(db.KEY_CUSTOMER_NO,"");
        map11.put(db.KEY_DIST_CHANNEL,"20");
        map11.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000008");
        map11.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000008");
        map11.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map11.put(db.KEY_FOC_ASSIGNING_QUANTITY, "3");
        map11.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map11.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map11);

        HashMap<String,String>map12 = new HashMap<>();
        map12.put(db.KEY_CUSTOMER_NO,"");
        map12.put(db.KEY_DIST_CHANNEL,"20");
        map12.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000009");
        map12.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000009");
        map12.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map12.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map12.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map12.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map12);

        HashMap<String,String>map13 = new HashMap<>();
        map13.put(db.KEY_CUSTOMER_NO,"");
        map13.put(db.KEY_DIST_CHANNEL,"20");
        map13.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000009");
        map13.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000009");
        map13.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map13.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map13.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map13.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map13);

        /*HashMap<String,String>map14 = new HashMap<>();
        map14.put(db.KEY_CUSTOMER_NO,"");
        map14.put(db.KEY_DIST_CHANNEL,"30");
        map14.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map14.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000000");
        map14.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map14.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map14.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map14.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map14);

        HashMap<String,String>map15 = new HashMap<>();
        map15.put(db.KEY_CUSTOMER_NO,"");
        map15.put(db.KEY_DIST_CHANNEL,"30");
        map15.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map15.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000000");
        map15.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map15.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map15.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map15.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map15);

        HashMap<String,String>map16 = new HashMap<>();
        map16.put(db.KEY_CUSTOMER_NO,"");
        map16.put(db.KEY_DIST_CHANNEL,"30");
        map16.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000002");
        map16.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000002");
        map16.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map16.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map16.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map16.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map16);

        HashMap<String,String>map17 = new HashMap<>();
        map17.put(db.KEY_CUSTOMER_NO,"");
        map17.put(db.KEY_DIST_CHANNEL,"30");
        map17.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000002");
        map17.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000002");
        map17.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map17.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map17.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map17.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map17);

        HashMap<String,String>map18 = new HashMap<>();
        map18.put(db.KEY_CUSTOMER_NO,"");
        map18.put(db.KEY_DIST_CHANNEL,"30");
        map18.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000003");
        map18.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000003");
        map18.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map18.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map18.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map18.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map18);

        HashMap<String,String>map19 = new HashMap<>();
        map19.put(db.KEY_CUSTOMER_NO,"");
        map19.put(db.KEY_DIST_CHANNEL,"30");
        map19.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000003");
        map19.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000003");
        map19.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map19.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map19.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map19.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map19);

        HashMap<String,String>map20 = new HashMap<>();
        map20.put(db.KEY_CUSTOMER_NO,"");
        map20.put(db.KEY_DIST_CHANNEL,"30");
        map20.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000004");
        map20.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000004");
        map20.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map20.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map20.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map20.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map20);

        HashMap<String,String>map21 = new HashMap<>();
        map21.put(db.KEY_CUSTOMER_NO,"");
        map21.put(db.KEY_DIST_CHANNEL,"30");
        map21.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000004");
        map21.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000004");
        map21.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map21.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map21.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map21.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map21);

        HashMap<String,String>map22 = new HashMap<>();
        map22.put(db.KEY_CUSTOMER_NO,"");
        map22.put(db.KEY_DIST_CHANNEL,"30");
        map22.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000006");
        map22.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map22.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map22.put(db.KEY_FOC_ASSIGNING_QUANTITY, "1");
        map22.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map22.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map22);

        HashMap<String,String>map23 = new HashMap<>();
        map23.put(db.KEY_CUSTOMER_NO,"");
        map23.put(db.KEY_DIST_CHANNEL,"30");
        map23.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000006");
        map23.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map23.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map23.put(db.KEY_FOC_ASSIGNING_QUANTITY, "3");
        map23.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map23.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map23);

        HashMap<String,String>map24 = new HashMap<>();
        map24.put(db.KEY_CUSTOMER_NO,"");
        map24.put(db.KEY_DIST_CHANNEL,"30");
        map24.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000008");
        map24.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000008");
        map24.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map24.put(db.KEY_FOC_ASSIGNING_QUANTITY, "1");
        map24.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map24.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map24);

        HashMap<String,String>map25 = new HashMap<>();
        map25.put(db.KEY_CUSTOMER_NO,"");
        map25.put(db.KEY_DIST_CHANNEL,"30");
        map25.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000008");
        map25.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000008");
        map25.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map25.put(db.KEY_FOC_ASSIGNING_QUANTITY, "3");
        map25.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map25.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map25);

        HashMap<String,String>map26 = new HashMap<>();
        map26.put(db.KEY_CUSTOMER_NO,"");
        map26.put(db.KEY_DIST_CHANNEL,"30");
        map26.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000009");
        map26.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000009");
        map26.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map26.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map26.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map26.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map26);

        HashMap<String,String>map27 = new HashMap<>();
        map27.put(db.KEY_CUSTOMER_NO,"");
        map27.put(db.KEY_DIST_CHANNEL,"30");
        map27.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000009");
        map27.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000009");
        map27.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map27.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map27.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map27.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map27);

        //Rules for Dist Channel 50
        HashMap<String,String>map28 = new HashMap<>();
        map28.put(db.KEY_CUSTOMER_NO,"");
        map28.put(db.KEY_DIST_CHANNEL,"50");
        map28.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map28.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000000");
        map28.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map28.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map28.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map28.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map28);

        HashMap<String,String>map29 = new HashMap<>();
        map29.put(db.KEY_CUSTOMER_NO,"");
        map29.put(db.KEY_DIST_CHANNEL,"50");
        map29.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map29.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000000");
        map29.put(db.KEY_FOC_QUALIFYING_QUANTITY,"100");
        map29.put(db.KEY_FOC_ASSIGNING_QUANTITY, "13");
        map29.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map29.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map29);

        HashMap<String,String>map30 = new HashMap<>();
        map30.put(db.KEY_CUSTOMER_NO,"");
        map30.put(db.KEY_DIST_CHANNEL,"50");
        map30.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000002");
        map30.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000002");
        map30.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map30.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map30.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map30.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map30);

        HashMap<String,String>map31 = new HashMap<>();
        map31.put(db.KEY_CUSTOMER_NO,"");
        map31.put(db.KEY_DIST_CHANNEL,"50");
        map31.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000002");
        map31.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000002");
        map31.put(db.KEY_FOC_QUALIFYING_QUANTITY,"100");
        map31.put(db.KEY_FOC_ASSIGNING_QUANTITY, "13");
        map31.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map31.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map31);

        HashMap<String,String>map32 = new HashMap<>();
        map32.put(db.KEY_CUSTOMER_NO,"");
        map32.put(db.KEY_DIST_CHANNEL,"50");
        map32.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000003");
        map32.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000003");
        map32.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map32.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map32.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map32.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map32);

        HashMap<String,String>map33 = new HashMap<>();
        map33.put(db.KEY_CUSTOMER_NO,"");
        map33.put(db.KEY_DIST_CHANNEL,"50");
        map33.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000003");
        map33.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000003");
        map33.put(db.KEY_FOC_QUALIFYING_QUANTITY,"100");
        map33.put(db.KEY_FOC_ASSIGNING_QUANTITY, "13");
        map33.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map33.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map33);

        HashMap<String,String>map34 = new HashMap<>();
        map34.put(db.KEY_CUSTOMER_NO,"");
        map34.put(db.KEY_DIST_CHANNEL,"50");
        map34.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000004");
        map34.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000004");
        map34.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map34.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map34.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map34.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map34);

        HashMap<String,String>map35 = new HashMap<>();
        map35.put(db.KEY_CUSTOMER_NO,"");
        map35.put(db.KEY_DIST_CHANNEL,"50");
        map35.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000004");
        map35.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000004");
        map35.put(db.KEY_FOC_QUALIFYING_QUANTITY,"100");
        map35.put(db.KEY_FOC_ASSIGNING_QUANTITY, "13");
        map35.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map35.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map35);

        HashMap<String,String>map36 = new HashMap<>();
        map36.put(db.KEY_CUSTOMER_NO,"");
        map36.put(db.KEY_DIST_CHANNEL,"50");
        map36.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000006");
        map36.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map36.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map36.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map36.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map36.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map36);

        HashMap<String,String>map37 = new HashMap<>();
        map37.put(db.KEY_CUSTOMER_NO,"");
        map37.put(db.KEY_DIST_CHANNEL,"50");
        map37.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000006");
        map37.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map37.put(db.KEY_FOC_QUALIFYING_QUANTITY,"100");
        map37.put(db.KEY_FOC_ASSIGNING_QUANTITY, "13");
        map37.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map37.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map37);

        HashMap<String,String>map38 = new HashMap<>();
        map38.put(db.KEY_CUSTOMER_NO,"");
        map38.put(db.KEY_DIST_CHANNEL,"50");
        map38.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000008");
        map38.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000008");
        map38.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map38.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map38.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map38.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map38);

        HashMap<String,String>map39 = new HashMap<>();
        map39.put(db.KEY_CUSTOMER_NO,"");
        map39.put(db.KEY_DIST_CHANNEL,"50");
        map39.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000008");
        map39.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000008");
        map39.put(db.KEY_FOC_QUALIFYING_QUANTITY,"100");
        map39.put(db.KEY_FOC_ASSIGNING_QUANTITY, "13");
        map39.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map39.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map39);

        HashMap<String,String>map40 = new HashMap<>();
        map40.put(db.KEY_CUSTOMER_NO,"");
        map40.put(db.KEY_DIST_CHANNEL,"50");
        map40.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000009");
        map40.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000009");
        map40.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map40.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map40.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map40.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map40);

        HashMap<String,String>map41 = new HashMap<>();
        map41.put(db.KEY_CUSTOMER_NO,"");
        map41.put(db.KEY_DIST_CHANNEL,"50");
        map41.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000009");
        map41.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000009");
        map41.put(db.KEY_FOC_QUALIFYING_QUANTITY,"100");
        map41.put(db.KEY_FOC_ASSIGNING_QUANTITY, "13");
        map41.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map41.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map41);*/

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
