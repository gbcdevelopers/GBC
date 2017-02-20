package gbc.sa.vansales.data;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class DriverRouteFlags {

    private static final String COLLECTION_NAME = "RouteFlagSet";
    private static final String TRIP_ID = "ITripId";
    private static App.DriverRouteControl driverFlag = new App.DriverRouteControl();

    public static void load(Context context,String tripId, DatabaseHandler db){
        HashMap<String, String> params = new HashMap<>();
        params.put(TRIP_ID,tripId);

        HashMap<String,String>expansion = new HashMap<>();
        new DownloadData(context,COLLECTION_NAME,params,expansion,db);
    }

    public static void loadData(Context context){
        driverFlag = new App.DriverRouteControl();
        DatabaseHandler db = new DatabaseHandler(context);
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TRIP_ID,"");
        map.put(db.KEY_DRIVER,"");
        map.put(db.KEY_ROUTE_TYPE,"");
        map.put(db.KEY_PROMPT_ODOMETER,"");
        map.put(db.KEY_EOD_SALES_REPORT,"");
        map.put(db.KEY_ENABLE_PVOID,"");
        map.put(db.KEY_ENABLE_NO_SALE,"");
        map.put(db.KEY_ENABLE_ADD_CUSTOMER,"");
        map.put(db.KEY_DEFAULT_DELIVERY_DAYS,"");
        map.put(db.KEY_PASSWORD1,"");
        map.put(db.KEY_PASSWORD2,"");
        map.put(db.KEY_PASSWORD3,"");
        map.put(db.KEY_PASSWORD4,"");
        map.put(db.KEY_PASSWORD5,"");
        map.put(db.KEY_DATE_TIME_CHANGE,"");
        map.put(db.KEY_PRICE_CHANGE,"");
        map.put(db.KEY_PROMO_OVERRIDE,"");
        map.put(db.KEY_ROUTE_SETUP,"");
        map.put(db.KEY_VIEW_STOCK,"");
        map.put(db.KEY_LOAD_SECURITY_GUARD,"");
        map.put(db.KEY_START_OF_DAY,"");
        map.put(db.KEY_SETTLEMENT,"");
        map.put(db.KEY_LOAD_ADJUST,"");
        map.put(db.KEY_ENFORCE_CALL_SEQ,"");
        map.put(db.KEY_DISPLAY_IV_SUMMARY,"");
        map.put(db.KEY_ALLOW_RADIUS,"");
        map.put(db.KEY_ENABLE_GPS,"");

        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.DRIVER_FLAGS,map,filters);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                driverFlag.setRouteType(cursor.getString(cursor.getColumnIndex(db.KEY_ROUTE_TYPE)));
                driverFlag.setPromptOdometer(cursor.getString(cursor.getColumnIndex(db.KEY_PROMPT_ODOMETER)).equals("0") ? false : true);
                driverFlag.setEodSalesReports(cursor.getString(cursor.getColumnIndex(db.KEY_EOD_SALES_REPORT)).equals("0") ? false : true);
                driverFlag.setIsDeleteInvoice(cursor.getString(cursor.getColumnIndex(db.KEY_ENABLE_PVOID)).equals("0") ? false : true);
                driverFlag.setIsNoSale(cursor.getString(cursor.getColumnIndex(db.KEY_ENABLE_NO_SALE)).equals("0") ? false : true);
                driverFlag.setIsAddCustomer(cursor.getString(cursor.getColumnIndex(db.KEY_ENABLE_ADD_CUSTOMER)).equals("0") ? false : true);
                driverFlag.setDefaultDeliveryDays(cursor.getString(cursor.getColumnIndex(db.KEY_DEFAULT_DELIVERY_DAYS)));
                driverFlag.setPassword1(cursor.getString(cursor.getColumnIndex(db.KEY_PASSWORD1)));
                driverFlag.setPassword2(cursor.getString(cursor.getColumnIndex(db.KEY_PASSWORD2)));
                driverFlag.setPassword3(cursor.getString(cursor.getColumnIndex(db.KEY_PASSWORD3)));
                driverFlag.setPassword4(cursor.getString(cursor.getColumnIndex(db.KEY_PASSWORD4)));
                driverFlag.setPassword5(cursor.getString(cursor.getColumnIndex(db.KEY_PASSWORD5)));
                driverFlag.setIsViewVanStock(cursor.getString(cursor.getColumnIndex(db.KEY_VIEW_STOCK)));
                driverFlag.setIsLoadSecurityGuard(cursor.getString(cursor.getColumnIndex(db.KEY_LOAD_SECURITY_GUARD)));
                driverFlag.setIsStartOfDay(cursor.getString(cursor.getColumnIndex(db.KEY_START_OF_DAY)));
                driverFlag.setIsEndTrip(cursor.getString(cursor.getColumnIndex(db.KEY_SETTLEMENT)));
                driverFlag.setIsCallSequence(cursor.getString(cursor.getColumnIndex(db.KEY_ENFORCE_CALL_SEQ)).equals("0") ? false : true);
                driverFlag.setIsDisplayInvoiceSummary(cursor.getString(cursor.getColumnIndex(db.KEY_DISPLAY_IV_SUMMARY)).equals("0") ? false : true);
                driverFlag.setIsAllowRadius(cursor.getString(cursor.getColumnIndex(db.KEY_ALLOW_RADIUS)).equals("0") ? false : true);
                driverFlag.setIsEnableGPS(cursor.getString(cursor.getColumnIndex(db.KEY_ENABLE_GPS)).equals("0") ? false : true);
            }
            while (cursor.moveToNext());

        }

    }

    public static App.DriverRouteControl get() {
        return driverFlag;
    }
}
