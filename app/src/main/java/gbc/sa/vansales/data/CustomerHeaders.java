package gbc.sa.vansales.data;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class CustomerHeaders {

    private static final String COLLECTION_NAME = "CustomerHeaderSet";
    private static final String CUSTOMER_SALES_AREAS = "CustomerSalesAreas";
    private static final String CUSTOMER_OPEN_ITEMS = "CustomerOpenItems";
    private static final String CUSTOMER_CREDIT = "CustomerCredit";
    private static final String CUSTOMER_FLAGS = "CustomerFlags";
    private static final String TRIP_ID = "ITripId";

    private static ArrayList<CustomerHeader> data = new ArrayList<>();
    public static void load(Context context,String tripId, DatabaseHandler db){
        HashMap<String, String> params = new HashMap<>();
        params.put(TRIP_ID,tripId);

        HashMap<String,String>expansion = new HashMap<>();
        expansion.put(CUSTOMER_SALES_AREAS,CUSTOMER_SALES_AREAS);
        expansion.put(CUSTOMER_OPEN_ITEMS,CUSTOMER_OPEN_ITEMS);
        expansion.put(CUSTOMER_CREDIT,CUSTOMER_CREDIT);
        expansion.put(CUSTOMER_FLAGS,CUSTOMER_FLAGS);
        new DownloadData(context,COLLECTION_NAME,params,expansion,db);
    }
    public static void loadData(Context context){
        data.clear();
        DatabaseHandler db = new DatabaseHandler(context);
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TRIP_ID,"");
        map.put(db.KEY_ORDER_BLOCK ,"");
        map.put(db.KEY_INVOICE_BLOCK,"");
        map.put(db.KEY_DELIVERY_BLOCK,"");
        map.put(db.KEY_ROOM_NO,"");
        map.put(db.KEY_FLOOR,"");
        map.put(db.KEY_BUILDING,"");
        map.put(db.KEY_HOME_CITY,"");
        map.put(db.KEY_STREET5,"");
        map.put(db.KEY_STREET4,"");
        map.put(db.KEY_STREET3,"");
        map.put(db.KEY_STREET2,"");
        map.put(db.KEY_NAME4,"");
        map.put(db.KEY_DRIVER,"");
        map.put(db.KEY_CUSTOMER_NO,"");
        map.put(db.KEY_COUNTRY_CODE,"");
        map.put(db.KEY_NAME3,"");
        map.put(db.KEY_NAME1,"");
        map.put(db.KEY_ADDRESS,"");
        map.put(db.KEY_STREET,"");
        map.put(db.KEY_NAME2,"");
        map.put(db.KEY_CITY,"");
        map.put(db.KEY_DISTRICT,"");
        map.put(db.KEY_REGION,"");
        map.put(db.KEY_SITE_CODE,"");
        map.put(db.KEY_POST_CODE,"");
        map.put(db.KEY_PHONE_NO,"");
        map.put(db.KEY_COMPANY_CODE,"");
        map.put(db.KEY_LATITUDE,"");
        map.put(db.KEY_LONGITUDE,"");
        map.put(db.KEY_TERMS,"");
        map.put(db.KEY_TERMS_DESCRIPTION,"");

        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.CUSTOMER_HEADER,map,filters);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                CustomerHeader customerHeader = new CustomerHeader();

                customerHeader.setAddress(cursor.getString(cursor.getColumnIndex(db.KEY_ADDRESS)));
                customerHeader.setBuilding(cursor.getString(cursor.getColumnIndex(db.KEY_BUILDING)));
                customerHeader.setCity(cursor.getString(cursor.getColumnIndex(db.KEY_CITY)));
                customerHeader.setCountryCode(cursor.getString(cursor.getColumnIndex(db.KEY_COUNTRY_CODE)));
                customerHeader.setCustomerNo(cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_NO)));
                customerHeader.setDeliveryBlock(cursor.getString(cursor.getColumnIndex(db.KEY_DELIVERY_BLOCK)));
                customerHeader.setDistrict(cursor.getString(cursor.getColumnIndex(db.KEY_DISTRICT)));
                customerHeader.setDriver1(cursor.getString(cursor.getColumnIndex(db.KEY_DRIVER)));
                customerHeader.setFloor(cursor.getString(cursor.getColumnIndex(db.KEY_FLOOR)));
                customerHeader.setHomeCity(cursor.getString(cursor.getColumnIndex(db.KEY_HOME_CITY)));
                customerHeader.setInvoiceBlock(cursor.getString(cursor.getColumnIndex(db.KEY_INVOICE_BLOCK)));
                customerHeader.setName1(cursor.getString(cursor.getColumnIndex(db.KEY_NAME1)));
                customerHeader.setName2(cursor.getString(cursor.getColumnIndex(db.KEY_NAME2)));
                customerHeader.setName3(cursor.getString(cursor.getColumnIndex(db.KEY_NAME3)));
                customerHeader.setName4(cursor.getString(cursor.getColumnIndex(db.KEY_NAME4)));
                customerHeader.setOrderBlock(cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_BLOCK )));
                customerHeader.setPhone(cursor.getString(cursor.getColumnIndex(db.KEY_PHONE_NO)));
                customerHeader.setPostCode(cursor.getString(cursor.getColumnIndex(db.KEY_POST_CODE)));
                customerHeader.setRegion(cursor.getString(cursor.getColumnIndex(db.KEY_REGION)));
                customerHeader.setRoomNo(cursor.getString(cursor.getColumnIndex(db.KEY_ROOM_NO)));
                customerHeader.setSiteCode(cursor.getString(cursor.getColumnIndex(db.KEY_SITE_CODE)));
                customerHeader.setStreet2(cursor.getString(cursor.getColumnIndex(db.KEY_STREET2)));
                customerHeader.setStreet3(cursor.getString(cursor.getColumnIndex(db.KEY_STREET3)));
                customerHeader.setStreet4(cursor.getString(cursor.getColumnIndex(db.KEY_STREET4)));
                customerHeader.setStreet5(cursor.getString(cursor.getColumnIndex(db.KEY_STREET5)));
                customerHeader.setStreet(cursor.getString(cursor.getColumnIndex(db.KEY_STREET)));
                customerHeader.setTripId(cursor.getString(cursor.getColumnIndex(db.KEY_TRIP_ID)));
                customerHeader.setLatitude(cursor.getString(cursor.getColumnIndex(db.KEY_LATITUDE)));
                customerHeader.setLongitude(cursor.getString(cursor.getColumnIndex(db.KEY_LONGITUDE)));
                customerHeader.setTerms(cursor.getString(cursor.getColumnIndex(db.KEY_TERMS)));
                customerHeader.setTermDescription(cursor.getString(cursor.getColumnIndex(db.KEY_TERMS_DESCRIPTION)));
                data.add(customerHeader);

            }
            while (cursor.moveToNext());
            Log.e("Load Data", "" + data.size());
        }

    }
    public static ArrayList<CustomerHeader> get() {
        return data;
    }
}
