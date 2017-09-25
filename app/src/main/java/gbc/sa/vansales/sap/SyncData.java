package gbc.sa.vansales.sap;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.activities.SettingsActivity;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.OfflinePost;
import gbc.sa.vansales.models.OfflineResponse;
import gbc.sa.vansales.models.Unload;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Rakshit on 11-Jan-17.
 */
public class SyncData extends IntentService {
    ArrayList<OfflinePost> arrayList = new ArrayList<>();
    ArrayList<OfflinePost> beginDayList = new ArrayList<>();
    ArrayList<OfflinePost> odometerList = new ArrayList<>();
    ArrayList<OfflinePost> customerList = new ArrayList<>();
    public static String TAG = "SyncData";
    DatabaseHandler db = new DatabaseHandler(this);
    public ArrayList<ArticleHeader> articles;
    Activity activity;

    public SyncData(){
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("I am here", "IntentService" + Settings.getString(App.IS_DATA_SYNCING));

        if(!Boolean.parseBoolean(Settings.getString(App.IS_DATA_SYNCING))){
            Log.e("Inside", "Inside" + getSyncCount());
            if(getSyncCount()>0){
                articles = new ArrayList<>();
                articles = ArticleHeaders.get();
                Settings.setString(App.IS_DATA_SYNCING,"true");
                syncData();
            }
        }
    }
    public void syncData(){
        boolean isEmpty = true;
        if(getSyncCount(ConfigStore.BeginDayFunction)>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.BeginDayFunction);
            new syncData("BEGINDAY");
        }
        if(getSyncCount(ConfigStore.OdometerFunction)>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.OdometerFunction);
            new syncData("ODOMETER");
        }
        if(getSyncCount(ConfigStore.AddCustomerFunction)>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.AddCustomerFunction);
            new syncData("ADDCUSTOMER");
        }
        if(getSyncCount(ConfigStore.LoadConfirmationFunction)>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.LoadConfirmationFunction);
        }
        if(getSyncCount(ConfigStore.LoadVarianceFunction+"D")>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.LoadVarianceFunction+"D");
        }
        if(getSyncCount(ConfigStore.LoadVarianceFunction+"C")>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.LoadVarianceFunction+"C");
        }
        if(getSyncCount(ConfigStore.LoadRequestFunction)>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.LoadRequestFunction);
        }
        if(getSyncCount(ConfigStore.CustomerOrderRequestFunction+"O")>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.CustomerOrderRequestFunction+"O");
        }
        if(getSyncCount(ConfigStore.InvoiceRequestFunction)>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.InvoiceRequestFunction);
        }
        if(getSyncCount(ConfigStore.ReturnsFunction+"G")>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.ReturnsFunction+"G");
        }
        if(getSyncCount(ConfigStore.ReturnsFunction+"B")>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.ReturnsFunction+"B");
        }
        if(getSyncCount(ConfigStore.CustomerDeliveryRequestFunction)>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.CustomerDeliveryRequestFunction);
        }
        if(getSyncCount(ConfigStore.CustomerDeliveryDeleteRequestFunction)>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.CustomerDeliveryDeleteRequestFunction);
        }
        if(getSyncCount(ConfigStore.VisitListFunction)>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.VisitListFunction);
        }
        if(getSyncCount(ConfigStore.CollectionFunction)>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.CollectionFunction);
        }
        if(getSyncCount(ConfigStore.PartialCollectionFunction)>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.PartialCollectionFunction);
        }
        /*if(getSyncCount(ConfigStore.CollectionFunction+"D")>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.CollectionFunction+"D");
        }*/
        if(getSyncCount(ConfigStore.UnloadFunction+"U")>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.UnloadFunction+"U");
        }
        if(!isEmpty){
            new syncData("");
        }

    }
    public void generateBatch(String request) {
        //Log.e("Request","Request" + request);
        try{

            String purchaseNumber = "";
            String tempPurchaseNumber = "";
            String customerNumber = "";
            String tempCustomerNumber = "";
            String deliveryNumber = "";
            String tempDeliveryNumber = "";
            String customerPO = "";

            switch (request){

                case ConfigStore.BeginDayFunction:{
                    JSONArray deepEntity = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    deepEntity.put(jsonObject);
                    HashMap<String, String> map = new HashMap<>();
                    map.put(db.KEY_TIME_STAMP,"");
                    map.put(db.KEY_TRIP_ID,"");
                    map.put(db.KEY_PURCHASE_NUMBER,"");
                    map.put(db.KEY_DATE,"");
                    map.put(db.KEY_IS_SELECTED, "");
                    HashMap<String,String> filter = new HashMap<>();
                    filter.put(db.KEY_FUNCTION, ConfigStore.BeginDayFunction);
                    filter.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                    Cursor beginDayCursor = db.getData(db.BEGIN_DAY,map,filter);
                    if(beginDayCursor.getCount()>0){
                        beginDayCursor.moveToFirst();
                        OfflinePost object = new OfflinePost();
                        object.setCollectionName(App.POST_COLLECTION);
                        object.setMap(Helpers.buildBeginDayHeader(ConfigStore.BeginDayFunction, Settings.getString(App.TRIP_ID),
                                Settings.getString(App.DRIVER), beginDayCursor.getString(beginDayCursor.getColumnIndex(db.KEY_TIME_STAMP)),
                                beginDayCursor.getString(beginDayCursor.getColumnIndex(db.KEY_PURCHASE_NUMBER))));
                        object.setDeepEntity(deepEntity);
                        Helpers.logData(getApplication(), "Begin Day Batch Header" + object.getMap().toString());
                        Helpers.logData(getApplication(), "Begin Day Batch Body" + deepEntity.toString());
                        beginDayList.add(object);
                    }
                    break;
                }
                case ConfigStore.OdometerFunction:{
                    JSONArray deepEntity = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    deepEntity.put(jsonObject);
                    HashMap<String, String> map = new HashMap<>();
                    map.put(db.KEY_ODOMETER_VALUE, "");
                    map.put(db.KEY_TRIP_ID, "");
                    map.put(db.KEY_PURCHASE_NUMBER, "");
                    map.put(db.KEY_TIME_STAMP, "");
                    HashMap<String, String> filter = new HashMap<>();
                    filter.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                    Cursor odometerCursor = db.getData(db.ODOMETER,map,filter);
                    if(odometerCursor.getCount()>0){
                        odometerCursor.moveToFirst();
                        OfflinePost object = new OfflinePost();
                        object.setCollectionName(App.POST_ODOMETER_SET);
                        object.setMap(Helpers.buildOdometerHeader(odometerCursor.getString(odometerCursor.getColumnIndex(db.KEY_TRIP_ID))
                                , odometerCursor.getString(odometerCursor.getColumnIndex(db.KEY_ODOMETER_VALUE))));
                        object.setDeepEntity(deepEntity);
                        Helpers.logData(getApplication(), "Odometer Batch Header" + object.getMap().toString());
                        Helpers.logData(getApplication(), "Odometer Batch Body" + deepEntity.toString());
                        odometerList.add(object);
                    }
                    break;
                }
                case ConfigStore.AddCustomerFunction:{
                    JSONArray deepEntity = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    deepEntity.put(jsonObject);
                    HashMap<String, String> map = new HashMap<>();
                    map.put(db.KEY_TIME_STAMP,"");
                    map.put(db.KEY_CUSTOMER_NO,"");
                    map.put(db.KEY_OWNER_NAME,"");
                    map.put(db.KEY_OWNER_NAME_AR,"");
                    map.put(db.KEY_TRADE_NAME,"");
                    map.put(db.KEY_TRADE_NAME_AR,"");
                    map.put(db.KEY_AREA,"");
                    map.put(db.KEY_STREET,"");
                    map.put(db.KEY_CR_NO,"");
                    map.put(db.KEY_PO_BOX,"");
                    map.put(db.KEY_EMAIL,"");
                    map.put(db.KEY_TELEPHONE,"");
                    map.put(db.KEY_FAX,"");
                    map.put(db.KEY_SALES_AREA,"");
                    map.put(db.KEY_DISTRIBUTION,"");
                    map.put(db.KEY_DIVISION,"");
                    map.put(db.KEY_IS_POSTED,"");
                    map.put(db.KEY_IS_PRINTED,"");
                    map.put(db.KEY_LATITUDE,"");
                    map.put(db.KEY_LONGITUDE,"");
                    HashMap<String, String> filter = new HashMap<>();
                    filter.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                    Cursor c = db.getData(db.NEW_CUSTOMER_POST,map,filter);
                    if(c.getCount()>0){
                        c.moveToFirst();
                        do{
                            OfflinePost object = new OfflinePost();
                            object.setCollectionName(App.POST_CUSTOMER_SET);
                            object.setMap(Helpers.buildnewCustomerHeader(c.getString(c.getColumnIndex(db.KEY_CUSTOMER_NO)), c.getString(c.getColumnIndex(db.KEY_OWNER_NAME)),
                                    c.getString(c.getColumnIndex(db.KEY_OWNER_NAME_AR)), c.getString(c.getColumnIndex(db.KEY_TRADE_NAME)), c.getString(c.getColumnIndex(db.KEY_TRADE_NAME_AR)),
                                    c.getString(c.getColumnIndex(db.KEY_AREA)), c.getString(c.getColumnIndex(db.KEY_STREET)), c.getString(c.getColumnIndex(db.KEY_CR_NO)),
                                    c.getString(c.getColumnIndex(db.KEY_PO_BOX)), c.getString(c.getColumnIndex(db.KEY_EMAIL)),
                                    c.getString(c.getColumnIndex(db.KEY_TELEPHONE)), c.getString(c.getColumnIndex(db.KEY_FAX)),
                                    c.getString(c.getColumnIndex(db.KEY_SALES_AREA)), c.getString(c.getColumnIndex(db.KEY_DISTRIBUTION)),
                                    c.getString(c.getColumnIndex(db.KEY_DIVISION)),c.getString(c.getColumnIndex(db.KEY_LATITUDE)),
                                    c.getString(c.getColumnIndex(db.KEY_LONGITUDE))));
                            object.setDeepEntity(deepEntity);
                            Helpers.logData(getApplication(), "Add Customer Batch Header" + object.getMap().toString());
                            Helpers.logData(getApplication(), "Add Customer Batch Body" + deepEntity.toString());
                            customerList.add(object);
                        }
                        while (c.moveToNext());

                    }
                    break;
                }
                case ConfigStore.VisitListFunction:{
                    JSONArray deepEntity = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    deepEntity.put(jsonObject);
                    HashMap<String,String>map = new HashMap<>();
                    map.put(db.KEY_TIME_STAMP,"");
                    map.put(db.KEY_START_TIMESTAMP,"");
                    map.put(db.KEY_END_TIMESTAMP,"");
                    map.put(db.KEY_VISITLISTID,"");
                    map.put(db.KEY_TRIP_ID,"");
                    map.put(db.KEY_ACTIVITY_ID,"");
                    map.put(db.KEY_VISIT_SERVICED_REASON,"");
                    map.put(db.KEY_CUSTOMER_NO,"");
                    map.put(db.KEY_IS_POSTED,"");
                    map.put(db.KEY_IS_PRINTED,"");
                    HashMap<String, String> filter = new HashMap<>();
                    filter.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                    Cursor visitListCursor = db.getData(db.VISIT_LIST_POST,map,filter);
                    if(visitListCursor.getCount()>0){
                        visitListCursor.moveToFirst();
                        do{
                            OfflinePost object = new OfflinePost();
                            object.setCollectionName(App.POST_COLLECTION);
                            object.setMap(Helpers.buildHeaderMapVisitList(ConfigStore.VisitListFunction,
                                    visitListCursor.getString(visitListCursor.getColumnIndex(db.KEY_START_TIMESTAMP)),
                                    visitListCursor.getString(visitListCursor.getColumnIndex(db.KEY_END_TIMESTAMP)),
                                    visitListCursor.getString(visitListCursor.getColumnIndex(db.KEY_VISITLISTID)),
                                    visitListCursor.getString(visitListCursor.getColumnIndex(db.KEY_ACTIVITY_ID)),
                                    visitListCursor.getString(visitListCursor.getColumnIndex(db.KEY_VISIT_SERVICED_REASON)),
                                    visitListCursor.getString(visitListCursor.getColumnIndex(db.KEY_CUSTOMER_NO))));
                            object.setDeepEntity(deepEntity);
                            Helpers.logData(getApplication(), "Visit List Batch Header" + object.getMap().toString());
                            Helpers.logData(getApplication(), "Visit List Batch Body" + deepEntity.toString());
                            arrayList.add(object);
                        }
                        while (visitListCursor.moveToNext());

                    }
                    break;
                }
                case ConfigStore.LoadConfirmationFunction:{
                    try{
                        JSONArray deepEntity = new JSONArray();
                        JSONObject obj = new JSONObject();
                        deepEntity.put(obj);
                        HashMap<String,String> map = new HashMap<>();
                        map.put(db.KEY_TIME_STAMP,"");
                        map.put(db.KEY_TRIP_ID,"");
                        map.put(db.KEY_FUNCTION,"");
                        map.put(db.KEY_ORDER_ID,"");
                        map.put(db.KEY_CUSTOMER_NO,"");
                        map.put(db.KEY_IS_POSTED,"");
                        map.put(db.KEY_IS_PRINTED,"");
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                        Cursor cursor = db.getData(db.LOAD_CONFIRMATION_HEADER,map,filter);
                        if(cursor.getCount()>0){
                            cursor.moveToFirst();
                            do{
                                OfflinePost object = new OfflinePost();
                                object.setCollectionName(App.POST_COLLECTION);
                                object.setMap(Helpers.buildLoadConfirmationHeader(cursor.getString(cursor.getColumnIndex(db.KEY_FUNCTION)),
                                        cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID)), Settings.getString(App.DRIVER)));
                                object.setDeepEntity(deepEntity);
                                Helpers.logData(getApplication(), "Load Confirmation Batch Header" + object.getMap().toString());
                                Helpers.logData(getApplication(), "Load Confirmation Batch Body" + deepEntity.toString());
                                arrayList.add(object);
                            }
                            while (cursor.moveToNext());
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case ConfigStore.LoadVarianceFunction+"D":{
                    try{

                        JSONArray deepEntity = new JSONArray();
                        HashMap<String, String> itemMap = new HashMap<>();
                        itemMap.put(db.KEY_DATE,"");
                        itemMap.put(db.KEY_ITEM_NO,"");
                        itemMap.put(db.KEY_MATERIAL_NO,"");
                        itemMap.put(db.KEY_MATERIAL_DESC1,"");
                        itemMap.put(db.KEY_CASE,"");
                        itemMap.put(db.KEY_UNIT,"");
                        itemMap.put(db.KEY_UOM,"");
                        itemMap.put(db.KEY_PRICE,"");
                        itemMap.put(db.KEY_ORDER_ID,"");
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                        filter.put(db.KEY_DOCUMENT_TYPE,ConfigStore.LoadVarianceDebit);

                        Cursor pendingLoadRequestCursor = db.getData(db.LOAD_VARIANCE_ITEMS_POST,itemMap,filter);
                        if(pendingLoadRequestCursor.getCount()>0){
                            pendingLoadRequestCursor.moveToFirst();
                            int itemno = 10;
                            String documentDate = "";
                            do{
                                tempPurchaseNumber = pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_ORDER_ID));
                                //documentDate = pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_DATE));
                                if(purchaseNumber.equals("")){
                                    purchaseNumber = tempPurchaseNumber;
                                }
                                else if(purchaseNumber.equals(tempPurchaseNumber)){

                                }
                                else{
                                /*OfflinePost object = new OfflinePost();
                                object.setCollectionName(App.POST_COLLECTION);
                                object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadVarianceFunction,"",ConfigStore.LoadVarianceDebit,Settings.getString(App.DRIVER),"",purchaseNumber,documentDate));
                                object.setDeepEntity(deepEntity);
                                arrayList.add(object);
                                purchaseNumber = tempPurchaseNumber;
                                deepEntity = new JSONArray();*/
                                }

                                if(pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                    jo.put("Material",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description", UrlBuilder.decodeString(pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_CASE)));
                                    jo.put("ItemValue", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("UoM", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                else{
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                    jo.put("Material",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description",UrlBuilder.decodeString(pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UNIT)));
                                    jo.put("ItemValue", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("UoM", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                //Check if cursor is at last position
                                if(pendingLoadRequestCursor.getPosition()==pendingLoadRequestCursor.getCount()-1){
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadVarianceFunction, "", ConfigStore.LoadVarianceDebit, Settings.getString(App.DRIVER), "", purchaseNumber,documentDate));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    Helpers.logData(getApplication(), "Load Variance Batch Header" + object.getMap().toString());
                                    Helpers.logData(getApplication(), "Load Variance Batch Body" + deepEntity.toString());
                                    deepEntity = new JSONArray();
                                }

                            }
                            while (pendingLoadRequestCursor.moveToNext());
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case ConfigStore.LoadVarianceFunction+"C":{
                    try{

                        JSONArray deepEntity = new JSONArray();
                        HashMap<String, String> itemMap = new HashMap<>();
                        itemMap.put(db.KEY_DATE,"");
                        itemMap.put(db.KEY_ITEM_NO,"");
                        itemMap.put(db.KEY_MATERIAL_NO,"");
                        itemMap.put(db.KEY_MATERIAL_DESC1,"");
                        itemMap.put(db.KEY_CASE,"");
                        itemMap.put(db.KEY_UNIT,"");
                        itemMap.put(db.KEY_UOM,"");
                        itemMap.put(db.KEY_PRICE,"");
                        itemMap.put(db.KEY_ORDER_ID,"");
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                        filter.put(db.KEY_DOCUMENT_TYPE,ConfigStore.LoadVarianceCredit);

                        Cursor pendingLoadRequestCursor = db.getData(db.LOAD_VARIANCE_ITEMS_POST,itemMap,filter);
                        if(pendingLoadRequestCursor.getCount()>0){
                            pendingLoadRequestCursor.moveToFirst();
                            int itemno = 10;
                            String documentDate = "";
                            do{
                                tempPurchaseNumber = pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_ORDER_ID));
                                //documentDate = pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_DATE));
                                if(purchaseNumber.equals("")){
                                    purchaseNumber = tempPurchaseNumber;
                                }
                                else if(purchaseNumber.equals(tempPurchaseNumber)){

                                }
                                else{
                                /*OfflinePost object = new OfflinePost();
                                object.setCollectionName(App.POST_COLLECTION);
                                object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadVarianceFunction,"",ConfigStore.LoadVarianceCredit,Settings.getString(App.DRIVER),"",purchaseNumber,documentDate));
                                object.setDeepEntity(deepEntity);
                                arrayList.add(object);
                                purchaseNumber = tempPurchaseNumber;
                                deepEntity = new JSONArray();*/
                                }

                                if(pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                    jo.put("Material",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description", UrlBuilder.decodeString(pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_CASE)));
                                    jo.put("ItemValue", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("UoM", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                else{
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                    jo.put("Material",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description",UrlBuilder.decodeString(pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UNIT)));
                                    jo.put("ItemValue", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("UoM", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                //Check if cursor is at last position
                                if(pendingLoadRequestCursor.getPosition()==pendingLoadRequestCursor.getCount()-1){
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadVarianceFunction, "", ConfigStore.LoadVarianceCredit, Settings.getString(App.DRIVER), "", purchaseNumber,documentDate));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    Helpers.logData(getApplication(), "Load Variance Batch Header" + object.getMap().toString());
                                    Helpers.logData(getApplication(), "Load Variance Batch Body" + deepEntity.toString());
                                    deepEntity = new JSONArray();
                                }

                            }
                            while (pendingLoadRequestCursor.moveToNext());
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case ConfigStore.LoadRequestFunction:{
                    try{

                        JSONArray deepEntity = new JSONArray();
                        HashMap<String, String> itemMap = new HashMap<>();
                        itemMap.put(db.KEY_DATE,"");
                        itemMap.put(db.KEY_ITEM_NO,"");
                        itemMap.put(db.KEY_MATERIAL_NO,"");
                        itemMap.put(db.KEY_MATERIAL_DESC1,"");
                        itemMap.put(db.KEY_CASE,"");
                        itemMap.put(db.KEY_UNIT,"");
                        itemMap.put(db.KEY_UOM,"");
                        itemMap.put(db.KEY_PRICE,"");
                        itemMap.put(db.KEY_ORDER_ID,"");
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);

                        Cursor pendingLoadRequestCursor = db.getData(db.LOAD_REQUEST,itemMap,filter);
                        if(pendingLoadRequestCursor.getCount()>0){
                            pendingLoadRequestCursor.moveToFirst();
                            int itemno = 10;
                            String documentDate = "";
                            do{
                                tempPurchaseNumber = pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_ORDER_ID));
                                documentDate = pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_DATE));
                                if(purchaseNumber.equals("")){
                                    purchaseNumber = tempPurchaseNumber;
                                }
                                else if(purchaseNumber.equals(tempPurchaseNumber)){

                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction,"",ConfigStore.DocumentType,Settings.getString(App.DRIVER),"",purchaseNumber,documentDate));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }

                                if(pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                    jo.put("Material",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description", UrlBuilder.decodeString(pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_CASE)));
                                    jo.put("ItemValue", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("UoM", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                else{
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                    jo.put("Material",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description",UrlBuilder.decodeString(pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UNIT)));
                                    jo.put("ItemValue", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("UoM", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                //Check if cursor is at last position
                                if(pendingLoadRequestCursor.getPosition()==pendingLoadRequestCursor.getCount()-1){
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.DocumentType, Settings.getString(App.DRIVER), "", purchaseNumber,documentDate));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    Helpers.logData(getApplication(), "Load Request Batch Header" + object.getMap().toString());
                                    Helpers.logData(getApplication(), "Load Request Batch Body" + deepEntity.toString());
                                    deepEntity = new JSONArray();
                                }

                            }
                            while (pendingLoadRequestCursor.moveToNext());
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case ConfigStore.CustomerOrderRequestFunction+"O":{
                    try{

                        JSONArray deepEntity = new JSONArray();
                        HashMap<String, String> itemMap = new HashMap<>();
                        itemMap.put(db.KEY_DATE,"");
                        itemMap.put(db.KEY_ITEM_NO,"");
                        itemMap.put(db.KEY_MATERIAL_NO,"");
                        itemMap.put(db.KEY_MATERIAL_DESC1,"");
                        itemMap.put(db.KEY_CASE,"");
                        itemMap.put(db.KEY_UNIT,"");
                        itemMap.put(db.KEY_UOM,"");
                        itemMap.put(db.KEY_PRICE,"");
                        itemMap.put(db.KEY_ORDER_ID,"");
                        itemMap.put(db.KEY_CUSTOMER_NO, "");
                        itemMap.put(db.KEY_CUSTOMER_PO,"");
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);

                        Cursor pendingOrderRequestCursor = db.getData(db.ORDER_REQUEST,itemMap,filter);
                        if(pendingOrderRequestCursor.getCount()>0){
                            pendingOrderRequestCursor.moveToFirst();
                            int itemno = 10;
                            String documentDate = "";
                            do{
                                tempPurchaseNumber = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_ORDER_ID));
                                tempCustomerNumber = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_CUSTOMER_NO));
                                documentDate = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_DATE));
                                customerPO = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_CUSTOMER_PO));
                                if(customerNumber.equals("")){
                                    customerNumber = tempCustomerNumber;
                                }
                                if(purchaseNumber.equals("")){
                                    purchaseNumber = tempPurchaseNumber;
                                }
                                else if(purchaseNumber.equals(tempPurchaseNumber)){

                                }
                                else{
                                    if(customerNumber.equals(tempCustomerNumber)){
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMapOrder(ConfigStore.LoadRequestFunction, "", ConfigStore.CustomerOrderRequestDocumentType, customerNumber, "", purchaseNumber, documentDate,customerPO));
                                        object.setDeepEntity(deepEntity);
                                        Helpers.logData(getApplication(), "Order Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Order Batch Body" + deepEntity.toString());
                                        arrayList.add(object);
                                        purchaseNumber = tempPurchaseNumber;
                                        deepEntity = new JSONArray();
                                    }
                                    else{
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        //object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.CustomerOrderRequestDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                        object.setMap(Helpers.buildHeaderMapOrder(ConfigStore.LoadRequestFunction, "", ConfigStore.CustomerOrderRequestDocumentType, customerNumber, "", purchaseNumber, documentDate, customerPO));
                                        object.setDeepEntity(deepEntity);
                                        Helpers.logData(getApplication(), "Customer Order Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Customer Order Batch Body" + deepEntity.toString());
                                        arrayList.add(object);
                                        purchaseNumber = tempPurchaseNumber;
                                        customerNumber = tempCustomerNumber;
                                        deepEntity = new JSONArray();
                                    }

                                }

                                if(pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                    jo.put("Material",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description",UrlBuilder.decodeString(pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_CASE)));
                                    jo.put("ItemValue", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("UoM", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                else{
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                    jo.put("Material",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description",UrlBuilder.decodeString(pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UNIT)));
                                    jo.put("ItemValue", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("UoM", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                //Check if cursor is at last position
                                if(pendingOrderRequestCursor.getPosition()==pendingOrderRequestCursor.getCount()-1){

                                    if(customerNumber.equals(tempCustomerNumber)){
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        //object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.CustomerOrderRequestDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                        object.setMap(Helpers.buildHeaderMapOrder(ConfigStore.LoadRequestFunction, "", ConfigStore.CustomerOrderRequestDocumentType, customerNumber, "", purchaseNumber, documentDate, customerPO));
                                        object.setDeepEntity(deepEntity);
                                        Helpers.logData(getApplication(), "Customer Order Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Customer Order Batch Body" + deepEntity.toString());

                                        arrayList.add(object);
                                        purchaseNumber = tempPurchaseNumber;
                                        deepEntity = new JSONArray();
                                    }
                                    else{
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        //object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.CustomerOrderRequestDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                        object.setMap(Helpers.buildHeaderMapOrder(ConfigStore.LoadRequestFunction, "", ConfigStore.CustomerOrderRequestDocumentType, customerNumber, "", purchaseNumber, documentDate, customerPO));
                                        object.setDeepEntity(deepEntity);
                                        Helpers.logData(getApplication(), "Customer Order Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Customer Order Batch Body" + deepEntity.toString());
                                        arrayList.add(object);
                                        purchaseNumber = tempPurchaseNumber;
                                        customerNumber = tempCustomerNumber;
                                        deepEntity = new JSONArray();
                                    }

                                /*OfflinePost object = new OfflinePost();
                                object.setCollectionName(App.POST_COLLECTION);
                                object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.DocumentType, "0000205005", "", purchaseNumber));
                                object.setDeepEntity(deepEntity);
                                arrayList.add(object);
                                deepEntity = new JSONArray();*/
                                }

                            }
                            while (pendingOrderRequestCursor.moveToNext());
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case ConfigStore.InvoiceRequestFunction:{
                    try{

                        JSONArray deepEntity = new JSONArray();
                        HashMap<String, String> itemMap = new HashMap<>();
                        itemMap.put(db.KEY_ITEM_NO,"");
                        itemMap.put(db.KEY_MATERIAL_NO,"");
                        itemMap.put(db.KEY_MATERIAL_DESC1,"");
                        itemMap.put(db.KEY_ORG_CASE,"");
                        itemMap.put(db.KEY_ORG_UNITS,"");
                        itemMap.put(db.KEY_UOM,"");
                        itemMap.put(db.KEY_AMOUNT,"");
                        itemMap.put(db.KEY_ORDER_ID,"");
                        itemMap.put(db.KEY_CUSTOMER_NO,"");
                        itemMap.put(db.KEY_PURCHASE_NUMBER,"");
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);

                        Cursor pendingInvoiceCursor = db.getData(db.CAPTURE_SALES_INVOICE,itemMap,filter);
                        Cursor foccursor = db.getData(db.FOC_INVOICE,itemMap,filter);
                        if(pendingInvoiceCursor.getCount()>0){
                            pendingInvoiceCursor.moveToFirst();
                            int itemno = 10;
                            do{
                                tempPurchaseNumber = pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_ORDER_ID));
                                tempCustomerNumber = pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_CUSTOMER_NO));
                                if(customerNumber.equals("")){
                                    customerNumber = tempCustomerNumber;
                                }
                                if(purchaseNumber.equals("")){
                                    purchaseNumber = tempPurchaseNumber;
                                }
                                else if(purchaseNumber.equals(tempPurchaseNumber)){

                                }
                                else{
                                    if(customerNumber.equals(tempCustomerNumber)){
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMap(ConfigStore.InvoiceRequestFunction, "", ConfigStore.InvoiceDocumentType, customerNumber, "", purchaseNumber,""));
                                        object.setDeepEntity(deepEntity);
                                        arrayList.add(object);
                                        Helpers.logData(getApplication(), "Invoice Order Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Invoice Order Batch Body" + deepEntity.toString());

                                        purchaseNumber = tempPurchaseNumber;
                                        deepEntity = new JSONArray();
                                    }
                                    else{
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMap(ConfigStore.InvoiceRequestFunction, "", ConfigStore.InvoiceDocumentType, customerNumber, "", purchaseNumber,""));
                                        object.setDeepEntity(deepEntity);
                                        arrayList.add(object);
                                        Helpers.logData(getApplication(), "Invoice Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Invoice Batch Body" + deepEntity.toString());

                                        purchaseNumber = tempPurchaseNumber;
                                        customerNumber = tempCustomerNumber;
                                        deepEntity = new JSONArray();
                                    }

                                }

                                if(pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                    jo.put("Material",pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description",UrlBuilder.decodeString(pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_ORG_CASE)));
                                    jo.put("ItemValue", pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_AMOUNT)));
                                    jo.put("UoM", pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_AMOUNT)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                else{
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                    jo.put("Material",pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description",UrlBuilder.decodeString(pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_ORG_UNITS)));
                                    jo.put("ItemValue", pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_AMOUNT)));
                                    jo.put("UoM", pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_AMOUNT)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                //Check if cursor is at last position
                                if(pendingInvoiceCursor.getPosition()==pendingInvoiceCursor.getCount()-1){

                                    if(customerNumber.equals(tempCustomerNumber)){
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMap(ConfigStore.InvoiceRequestFunction, "", ConfigStore.InvoiceDocumentType, customerNumber, "", purchaseNumber, ""));
                                        object.setDeepEntity(deepEntity);
                                        arrayList.add(object);
                                        Helpers.logData(getApplication(), "Invoice Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Invoice Batch Body" + deepEntity.toString());
                                        purchaseNumber = tempPurchaseNumber;
                                        deepEntity = new JSONArray();
                                    }
                                    else{
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMap(ConfigStore.InvoiceRequestFunction, "", ConfigStore.InvoiceDocumentType, customerNumber, "", purchaseNumber, ""));
                                        object.setDeepEntity(deepEntity);
                                        arrayList.add(object);
                                        Helpers.logData(getApplication(), "Invoice Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Invoice Batch Body" + deepEntity.toString());
                                        purchaseNumber = tempPurchaseNumber;
                                        customerNumber = tempCustomerNumber;
                                        deepEntity = new JSONArray();
                                    }

                                /*OfflinePost object = new OfflinePost();
                                object.setCollectionName(App.POST_COLLECTION);
                                object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.DocumentType, "0000205005", "", purchaseNumber));
                                object.setDeepEntity(deepEntity);
                                arrayList.add(object);
                                deepEntity = new JSONArray();*/
                                }

                            }
                            while (pendingInvoiceCursor.moveToNext());
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case ConfigStore.ReturnsFunction+"G":{
                    Log.e("Call here","Call here");
                    try{

                        JSONArray deepEntity = new JSONArray();
                        HashMap<String, String> itemMap = new HashMap<>();
                        itemMap.put(db.KEY_CUSTOMER_NO,"");
                        itemMap.put(db.KEY_ITEM_NO,"");
                        itemMap.put(db.KEY_MATERIAL_NO,"");
                        itemMap.put(db.KEY_MATERIAL_DESC1,"");
                        itemMap.put(db.KEY_CASE,"");
                        itemMap.put(db.KEY_UNIT,"");
                        itemMap.put(db.KEY_UOM,"");
                        itemMap.put(db.KEY_PRICE,"");
                        itemMap.put(db.KEY_ORDER_ID,"");
                        itemMap.put(db.KEY_REASON_CODE,"");
                        itemMap.put(db.KEY_PURCHASE_NUMBER,"");
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                        filter.put(db.KEY_REASON_TYPE,App.GOOD_RETURN);

                        Cursor pendingGRCursor = db.getData(db.RETURNS,itemMap,filter);
                        if(pendingGRCursor.getCount()>0){
                            pendingGRCursor.moveToFirst();
                            int itemno = 10;
                            String reasonCode = "";
                            do{
                                tempPurchaseNumber = pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_ORDER_ID));
                                tempCustomerNumber = pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_CUSTOMER_NO));
                                reasonCode = pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_REASON_CODE));
                                if(customerNumber.equals("")){
                                    customerNumber = tempCustomerNumber;
                                }
                                if(purchaseNumber.equals("")){
                                    purchaseNumber = tempPurchaseNumber;
                                }
                                else if(purchaseNumber.equals(tempPurchaseNumber)){

                                }
                                else{
                                    if(customerNumber.equals(tempCustomerNumber)){
                                    /*OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMapReason(ConfigStore.ReturnsFunction, "", ConfigStore.GoodReturnType, customerNumber, "", purchaseNumber, "", reasonCode));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();*/
                                    }
                                    else{
                                        if(deepEntity.length()>0){
                                            OfflinePost object = new OfflinePost();
                                            object.setCollectionName(App.POST_COLLECTION);
                                            object.setMap(Helpers.buildHeaderMapReason(ConfigStore.ReturnsFunction, "", ConfigStore.GoodReturnType, customerNumber, "", purchaseNumber, "", reasonCode));
                                            object.setDeepEntity(deepEntity);
                                            arrayList.add(object);
                                            Helpers.logData(getApplication(), "Returns Batch Header" + object.getMap().toString());
                                            Helpers.logData(getApplication(), "Returns Batch Body" + deepEntity.toString());
                                            purchaseNumber = tempPurchaseNumber;
                                            customerNumber = tempCustomerNumber;
                                            deepEntity = new JSONArray();
                                        }

                                    }

                                }

                                if(pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                    jo.put("Material",pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description",UrlBuilder.decodeString(pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_CASE)));
                                    jo.put("ItemValue", pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("UoM", pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                else{
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                    jo.put("Material",pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description",UrlBuilder.decodeString(pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_UNIT)));
                                    jo.put("ItemValue", pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("UoM", pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingGRCursor.getString(pendingGRCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                //Check if cursor is at last position
                                if(pendingGRCursor.getPosition()==pendingGRCursor.getCount()-1){

                                    if(customerNumber.equals(tempCustomerNumber)){
                                        //Log.e("This is test1","Test1");
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMapReason(ConfigStore.ReturnsFunction, "", ConfigStore.GoodReturnType, customerNumber, "", purchaseNumber, "", reasonCode));
                                        object.setDeepEntity(deepEntity);
                                        Helpers.logData(getApplication(), "Returns Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Returns Batch Body" + deepEntity.toString());

                                        arrayList.add(object);
                                        purchaseNumber = tempPurchaseNumber;
                                        deepEntity = new JSONArray();

                                    }
                                    else{
                                        //Log.e("This is test2","Test2");
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMapReason(ConfigStore.ReturnsFunction, "", ConfigStore.GoodReturnType, customerNumber, "", purchaseNumber, "", reasonCode));
                                        object.setDeepEntity(deepEntity);
                                        arrayList.add(object);
                                        Helpers.logData(getApplication(), "Returns Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Returns Batch Body" + deepEntity.toString());

                                        purchaseNumber = tempPurchaseNumber;
                                        customerNumber = tempCustomerNumber;
                                        deepEntity = new JSONArray();
                                    }

                                /*OfflinePost object = new OfflinePost();
                                object.setCollectionName(App.POST_COLLECTION);
                                object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.DocumentType, "0000205005", "", purchaseNumber));
                                object.setDeepEntity(deepEntity);
                                arrayList.add(object);
                                deepEntity = new JSONArray();*/
                                }

                            }
                            while (pendingGRCursor.moveToNext());
                            Log.e("ArrayList","" + arrayList.size());
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case ConfigStore.ReturnsFunction+"B":{
                    try{

                        JSONArray deepEntity = new JSONArray();
                        HashMap<String, String> itemMap = new HashMap<>();
                        itemMap.put(db.KEY_CUSTOMER_NO,"");
                        itemMap.put(db.KEY_ITEM_NO,"");
                        itemMap.put(db.KEY_MATERIAL_NO,"");
                        itemMap.put(db.KEY_MATERIAL_DESC1,"");
                        itemMap.put(db.KEY_CASE,"");
                        itemMap.put(db.KEY_UNIT,"");
                        itemMap.put(db.KEY_UOM,"");
                        itemMap.put(db.KEY_PRICE,"");
                        itemMap.put(db.KEY_ORDER_ID,"");
                        itemMap.put(db.KEY_REASON_CODE,"");
                        itemMap.put(db.KEY_PURCHASE_NUMBER,"");
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                        filter.put(db.KEY_REASON_TYPE,App.BAD_RETURN);

                        Cursor pendingInvoiceCursor = db.getData(db.RETURNS,itemMap,filter);
                        if(pendingInvoiceCursor.getCount()>0){
                            pendingInvoiceCursor.moveToFirst();
                            int itemno = 10;
                            String reasonCode = "";
                            do{
                                tempPurchaseNumber = pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_ORDER_ID));
                                tempCustomerNumber = pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_CUSTOMER_NO));
                                reasonCode = pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_REASON_CODE));
                                if(customerNumber.equals("")){
                                    customerNumber = tempCustomerNumber;
                                }
                                if(purchaseNumber.equals("")){
                                    purchaseNumber = tempPurchaseNumber;
                                }
                                else if(purchaseNumber.equals(tempPurchaseNumber)){

                                }
                                else{
                                    if(customerNumber.equals(tempCustomerNumber)){
                                    /*OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMapReason(ConfigStore.ReturnsFunction, "", ConfigStore.BadReturnType, customerNumber, "", purchaseNumber, "", reasonCode));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();*/
                                    }
                                    else{
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMapReason(ConfigStore.ReturnsFunction, "", ConfigStore.BadReturnType, customerNumber, "", purchaseNumber, "", reasonCode));
                                        object.setDeepEntity(deepEntity);
                                        arrayList.add(object);
                                        Helpers.logData(getApplication(), "Returns Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Returns Batch Body" + deepEntity.toString());

                                        purchaseNumber = tempPurchaseNumber;
                                        customerNumber = tempCustomerNumber;
                                        deepEntity = new JSONArray();
                                    }

                                }

                                if(pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                                    jo.put("Material",pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description",UrlBuilder.decodeString(pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_CASE)));
                                    jo.put("ItemValue", pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("UoM", pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                else{
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                                    jo.put("Material",pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description",UrlBuilder.decodeString(pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_UNIT)));
                                    jo.put("ItemValue", pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("UoM", pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_PRICE)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                //Check if cursor is at last position
                                if(pendingInvoiceCursor.getPosition()==pendingInvoiceCursor.getCount()-1){

                                    if(customerNumber.equals(tempCustomerNumber)){
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMapReason(ConfigStore.ReturnsFunction, "", ConfigStore.BadReturnType, customerNumber, "", purchaseNumber, "", reasonCode));
                                        object.setDeepEntity(deepEntity);
                                        arrayList.add(object);
                                        Helpers.logData(getApplication(), "Returns Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Returns Batch Body" + deepEntity.toString());

                                        purchaseNumber = tempPurchaseNumber;
                                        deepEntity = new JSONArray();
                                    }
                                    else{
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMapReason(ConfigStore.ReturnsFunction, "", ConfigStore.BadReturnType, customerNumber, "", purchaseNumber, "", reasonCode));
                                        object.setDeepEntity(deepEntity);
                                        arrayList.add(object);
                                        Helpers.logData(getApplication(), "Returns Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Returns Batch Body" + deepEntity.toString());

                                        purchaseNumber = tempPurchaseNumber;
                                        customerNumber = tempCustomerNumber;
                                        deepEntity = new JSONArray();
                                    }

                                /*OfflinePost object = new OfflinePost();
                                object.setCollectionName(App.POST_COLLECTION);
                                object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.DocumentType, "0000205005", "", purchaseNumber));
                                object.setDeepEntity(deepEntity);
                                arrayList.add(object);
                                deepEntity = new JSONArray();*/
                                }

                            }
                            while (pendingInvoiceCursor.moveToNext());
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case ConfigStore.CustomerDeliveryRequestFunction:{
                    try{
                        JSONArray deepEntity = new JSONArray();
                        HashMap<String, String> itemMap = new HashMap<>();

                        itemMap.put(db.KEY_ITEM_NO, "");
                        itemMap.put(db.KEY_DELIVERY_NO,"");
                        itemMap.put(db.KEY_MATERIAL_NO, "");
                        itemMap.put(db.KEY_MATERIAL_DESC1, "");
                        itemMap.put(db.KEY_CASE, "");
                        itemMap.put(db.KEY_UNIT, "");
                        itemMap.put(db.KEY_AMOUNT, "");
                        itemMap.put(db.KEY_ORDER_ID, "");
                        itemMap.put(db.KEY_ORDER_ID,"");
                        itemMap.put(db.KEY_CUSTOMER_NO,"");
                        itemMap.put(db.KEY_UOM,"");
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);

                        Cursor pendingOrderRequestCursor = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST,itemMap,filter);
                        if(pendingOrderRequestCursor.getCount()>0){
                            pendingOrderRequestCursor.moveToFirst();
                            //String deliveryNo = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_DELIVERY_NO));
                            int itemno = 10;
                            String documentDate = "";
                            do{
                                tempDeliveryNumber = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_DELIVERY_NO));
                                tempPurchaseNumber = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_ORDER_ID));
                                tempCustomerNumber = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_CUSTOMER_NO));
//                            documentDate = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_DATE));
                                if(customerNumber.equals("")){
                                    customerNumber = tempCustomerNumber;
                                }
                                if(deliveryNumber.equals("")){
                                    deliveryNumber = tempDeliveryNumber;
                                }
                                if(purchaseNumber.equals("")){
                                    purchaseNumber = tempPurchaseNumber;
                                }
                                else if(purchaseNumber.equals(tempPurchaseNumber)){

                                }
                                else{
                                    if(customerNumber.equals(tempCustomerNumber)){
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryRequestFunction, deliveryNumber, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                        object.setDeepEntity(deepEntity);
                                        arrayList.add(object);
                                        Helpers.logData(getApplication(), "Delivery Request Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Delivery Request Batch Body" + deepEntity.toString());

                                        purchaseNumber = tempPurchaseNumber;
                                        deliveryNumber = tempDeliveryNumber;
                                        deepEntity = new JSONArray();
                                    }
                                    else{
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryRequestFunction, deliveryNumber, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                        object.setDeepEntity(deepEntity);
                                        arrayList.add(object);
                                        Helpers.logData(getApplication(), "Delivery Request Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Delivery Request Batch Body" + deepEntity.toString());

                                        purchaseNumber = tempPurchaseNumber;
                                        deliveryNumber = tempDeliveryNumber;
                                        customerNumber = tempCustomerNumber;
                                        deepEntity = new JSONArray();
                                    }

                                }

                                if(pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                    JSONObject jo = new JSONObject();
                                    //jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                    jo.put("Item", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_ITEM_NO)));
                                    jo.put("Material",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description",UrlBuilder.decodeString(pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_CASE)));
                                    jo.put("ItemValue", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_AMOUNT)));
                                    jo.put("UoM", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_AMOUNT)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                else{
                                    JSONObject jo = new JSONObject();
                                    //jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                    jo.put("Item", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_ITEM_NO)));
                                    jo.put("Material",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description",UrlBuilder.decodeString(pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UNIT)));
                                    jo.put("ItemValue", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_AMOUNT)));
                                    jo.put("UoM", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)));
                                    jo.put("Value", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_AMOUNT)));
                                    jo.put("Storagelocation", "");
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                //Check if cursor is at last position
                                if(pendingOrderRequestCursor.getPosition()==pendingOrderRequestCursor.getCount()-1){

                                    if(customerNumber.equals(tempCustomerNumber)){
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryRequestFunction, deliveryNumber, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                        object.setDeepEntity(deepEntity);
                                        arrayList.add(object);
                                        Helpers.logData(getApplication(), "Delivery Request Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Delivery Request Batch Body" + deepEntity.toString());

                                        purchaseNumber = tempPurchaseNumber;
                                        deliveryNumber = tempDeliveryNumber;
                                        deepEntity = new JSONArray();
                                    }
                                    else{
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryRequestFunction, deliveryNumber, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                        object.setDeepEntity(deepEntity);
                                        arrayList.add(object);
                                        Helpers.logData(getApplication(), "Delivery Request Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Delivery Request Batch Body" + deepEntity.toString());

                                        purchaseNumber = tempPurchaseNumber;
                                        deliveryNumber = tempDeliveryNumber;
                                        customerNumber = tempCustomerNumber;
                                        deepEntity = new JSONArray();
                                    }

                                /*OfflinePost object = new OfflinePost();
                                object.setCollectionName(App.POST_COLLECTION);
                                object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.DocumentType, "0000205005", "", purchaseNumber));
                                object.setDeepEntity(deepEntity);
                                arrayList.add(object);
                                deepEntity = new JSONArray();*/
                                }

                            }
                            while (pendingOrderRequestCursor.moveToNext());
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case ConfigStore.CustomerDeliveryDeleteRequestFunction:{
                    try{

                        JSONArray deepEntity = new JSONArray();

                        HashMap<String, String> itemMap = new HashMap<>();
                        itemMap.put(db.KEY_DELIVERY_NO,"");
                        // itemMap.put(db.KEY_DATE,"");
                        itemMap.put(db.KEY_ITEM_NO,"");
                        itemMap.put(db.KEY_MATERIAL_NO,"");
                        itemMap.put(db.KEY_MATERIAL_DESC1,"");
                        itemMap.put(db.KEY_CASE,"");
                        itemMap.put(db.KEY_UNIT,"");
                        itemMap.put(db.KEY_UOM,"");
                        itemMap.put(db.KEY_REASON_CODE,"");
                        itemMap.put(db.KEY_REASON_DESCRIPTION,"");
                        itemMap.put(db.KEY_AMOUNT,"");
                        itemMap.put(db.KEY_ORDER_ID,"");
                        itemMap.put(db.KEY_CUSTOMER_NO,"");
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);

                        Cursor pendingOrderRequestCursor = db.getData(db.CUSTOMER_DELIVERY_ITEMS_DELETE_POST,itemMap,filter);
                        if(pendingOrderRequestCursor.getCount()>0){
                            pendingOrderRequestCursor.moveToFirst();
                            int itemno = 10;
                            String deliveryNo = "";
                            String documentDate = "";
                            do{
                                tempDeliveryNumber = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_DELIVERY_NO));
                                tempPurchaseNumber = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_ORDER_ID));
                                tempCustomerNumber = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_CUSTOMER_NO));
                                deliveryNo = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_DELIVERY_NO));
                                if(customerNumber.equals("")){
                                    customerNumber = tempCustomerNumber;
                                }
                                if(deliveryNumber.equals("")){
                                    deliveryNumber = tempDeliveryNumber;
                                }
                                if(purchaseNumber.equals("")){
                                    purchaseNumber = tempPurchaseNumber;
                                }
                                else if(purchaseNumber.equals(tempPurchaseNumber)){

                                }
                                else{

                                    if(deliveryNumber.equals(tempDeliveryNumber)){

                                    }
                                    else{
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryDeleteRequestFunction, deliveryNumber, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                        object.setDeepEntity(deepEntity);
                                        arrayList.add(object);
                                        Helpers.logData(getApplication(), "Delivery Delete Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Delivery Delete Batch Body" + deepEntity.toString());

                                        purchaseNumber = tempPurchaseNumber;
                                        deliveryNumber = tempDeliveryNumber;
                                        customerNumber = tempCustomerNumber;
                                        deepEntity = new JSONArray();
                                    }
                                /*if(customerNumber.equals(tempCustomerNumber)){
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryDeleteRequestFunction, deliveryNumber, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    deliveryNumber = tempDeliveryNumber;
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryDeleteRequestFunction, deliveryNumber, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deliveryNumber = tempDeliveryNumber;
                                    customerNumber = tempCustomerNumber;
                                    deepEntity = new JSONArray();
                                }*/

                                }

                                if(pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                                    jo.put("OrderId", deliveryNo);
                                    jo.put("Material",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description",UrlBuilder.decodeString(pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_CASE)));
                                    //jo.put("ItemValue", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_AMOUNT)));
                                    jo.put("UoM", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)));
                                    //jo.put("Value", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_AMOUNT)));
                                    jo.put("Storagelocation", "");
                                    jo.put("RejReason",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_REASON_CODE)));
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                else{
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                                    jo.put("OrderId", deliveryNo);
                                    jo.put("Material",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                    jo.put("Description",UrlBuilder.decodeString(pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                    jo.put("Plant","");
                                    jo.put("Quantity",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UNIT)));
                                    // jo.put("ItemValue", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_AMOUNT)));
                                    jo.put("UoM", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)));
                                    //jo.put("Value", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_AMOUNT)));
                                    jo.put("Storagelocation", "");
                                    jo.put("RejReason",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_REASON_CODE)));
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno+10;
                                    deepEntity.put(jo);
                                }
                                //Check if cursor is at last position
                                if(pendingOrderRequestCursor.getPosition()==pendingOrderRequestCursor.getCount()-1){

                                    if(customerNumber.equals(tempCustomerNumber)){
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryDeleteRequestFunction, deliveryNumber, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                        object.setDeepEntity(deepEntity);
                                        arrayList.add(object);
                                        Helpers.logData(getApplication(), "Delivery Delete Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Delivery Delete Batch Body" + deepEntity.toString());

                                        purchaseNumber = tempPurchaseNumber;
                                        deliveryNumber = tempDeliveryNumber;
                                        deepEntity = new JSONArray();
                                    }
                                    else{
                                        OfflinePost object = new OfflinePost();
                                        object.setCollectionName(App.POST_COLLECTION);
                                        object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryDeleteRequestFunction, deliveryNumber, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                        object.setDeepEntity(deepEntity);
                                        arrayList.add(object);
                                        Helpers.logData(getApplication(), "Delivery Delete Batch Header" + object.getMap().toString());
                                        Helpers.logData(getApplication(), "Delivery Delete Batch Body" + deepEntity.toString());

                                        purchaseNumber = tempPurchaseNumber;
                                        deliveryNumber = tempDeliveryNumber;
                                        customerNumber = tempCustomerNumber;
                                        deepEntity = new JSONArray();
                                    }

                                /*OfflinePost object = new OfflinePost();
                                object.setCollectionName(App.POST_COLLECTION);
                                object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.DocumentType, "0000205005", "", purchaseNumber));
                                object.setDeepEntity(deepEntity);
                                arrayList.add(object);
                                deepEntity = new JSONArray();*/
                                }

                            }
                            while (pendingOrderRequestCursor.moveToNext());
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case ConfigStore.CollectionFunction:{
                    try{
                        JSONArray deepEntity = new JSONArray();
                        ArrayList<String>tempCustomers = new ArrayList<>();
                        HashMap<String, String> itemMap = new HashMap<>();
                        itemMap.put(db.KEY_CUSTOMER_NO,"");
                        // itemMap.put(db.KEY_DATE,"");
                        itemMap.put(db.KEY_AMOUNT_CLEARED,"");
                        itemMap.put(db.KEY_INVOICE_NO,"");
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                        filter.put(db.KEY_IS_INVOICE_COMPLETE,App.INVOICE_COMPLETE);
                        Cursor pCCursor = db.getData(db.COLLECTION,itemMap,filter);
                        if(pCCursor.getCount()>0){
                            pCCursor.moveToFirst();
                            do{
                                if(!tempCustomers.contains(pCCursor.getString(pCCursor.getColumnIndex(db.KEY_CUSTOMER_NO)))){
                                    tempCustomers.add(pCCursor.getString(pCCursor.getColumnIndex(db.KEY_CUSTOMER_NO)));
                                }
                            }
                            while (pCCursor.moveToNext());
                        }
                        for(int i=0;i<tempCustomers.size();i++){
                            double amountcleared = 0;
                            HashMap<String, String> map = new HashMap<>();
                            map.put(db.KEY_AMOUNT_CLEARED,"");
                            map.put(db.KEY_INVOICE_NO,"");
                            HashMap<String, String> filt = new HashMap<>();
                            filt.put(db.KEY_CUSTOMER_NO,tempCustomers.get(i).toString());
                            filt.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                            Cursor cursor = db.getData(db.COLLECTION,itemMap,filt);

                            JSONArray deep = new JSONArray();
                            if(cursor.getCount()>0){
                                cursor.moveToFirst();

                                do{
                                    JSONObject object = new JSONObject();
                                    amountcleared += Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                                    object.put("OrderId",cursor.getString(cursor.getColumnIndex(db.KEY_INVOICE_NO)));
                                    deep.put(object);
                                }
                                while (cursor.moveToNext());
                            }
                            OfflinePost offlinePost = new OfflinePost();
                            offlinePost.setCollectionName(App.POST_COLLECTION);
                            offlinePost.setMap(Helpers.buildCollectionHeader(ConfigStore.CollectionFunction, tempCustomers.get(i).toString(), String.valueOf(amountcleared)));
                            offlinePost.setDeepEntity(deep);
                            Helpers.logData(getApplication(), "Collection Batch Header" + offlinePost.getMap().toString());
                            Helpers.logData(getApplication(), "Collection Batch Body" + deep.toString());

                            arrayList.add(offlinePost);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case ConfigStore.PartialCollectionFunction:{
                    try{
                        JSONArray deepEntity = new JSONArray();
                        ArrayList<String>tempCustomers = new ArrayList<>();
                        HashMap<String, String> itemMap = new HashMap<>();
                        itemMap.put(db.KEY_CUSTOMER_NO,"");
                        // itemMap.put(db.KEY_DATE,"");
                        itemMap.put(db.KEY_AMOUNT_CLEARED,"");
                        itemMap.put(db.KEY_INVOICE_NO,"");
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                        filter.put(db.KEY_IS_INVOICE_COMPLETE,App.INVOICE_PARTIAL);
                        Cursor pCCursor = db.getData(db.COLLECTION,itemMap,filter);
                        if(pCCursor.getCount()>0){
                            pCCursor.moveToFirst();
                            do{
                                if(!tempCustomers.contains(pCCursor.getString(pCCursor.getColumnIndex(db.KEY_CUSTOMER_NO)))){
                                    tempCustomers.add(pCCursor.getString(pCCursor.getColumnIndex(db.KEY_CUSTOMER_NO)));
                                }
                            }
                            while (pCCursor.moveToNext());
                        }
                        for(int i=0;i<tempCustomers.size();i++){
                            double amountcleared = 0;
                            HashMap<String, String> map = new HashMap<>();
                            map.put(db.KEY_AMOUNT_CLEARED,"");
                            itemMap.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,"");
                            map.put(db.KEY_INVOICE_NO,"");
                            HashMap<String, String> filt = new HashMap<>();
                            filt.put(db.KEY_CUSTOMER_NO,tempCustomers.get(i).toString());
                            filt.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                            filt.put(db.KEY_IS_INVOICE_COMPLETE,App.INVOICE_PARTIAL);
                            Cursor cursor = db.getData(db.COLLECTION,itemMap,filt);

                            JSONArray deep = new JSONArray();
                            if(cursor.getCount()>0){
                                cursor.moveToFirst();
                                do{
                                    JSONObject object = new JSONObject();
                                    amountcleared += Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                                    object.put("OrderId",cursor.getString(cursor.getColumnIndex(db.KEY_INVOICE_NO)));
                                    object.put("Value",cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                                    deep.put(object);
                                }
                                while (cursor.moveToNext());
                            }
                            OfflinePost offlinePost = new OfflinePost();
                            offlinePost.setCollectionName(App.POST_COLLECTION);
                            offlinePost.setMap(Helpers.buildCollectionHeader(ConfigStore.PartialCollectionFunction, tempCustomers.get(i).toString(), String.valueOf(amountcleared)));
                            offlinePost.setDeepEntity(deep);
                            Helpers.logData(getApplication(), "Partial Collection Batch Header" + offlinePost.getMap().toString());
                            Helpers.logData(getApplication(), "Partial Collection Batch Body" + deep.toString());

                            arrayList.add(offlinePost);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case ConfigStore.CollectionFunction+"D":{
                /*try{
                    JSONArray deepEntity = new JSONArray();
                    ArrayList<String>tempCustomers = new ArrayList<>();
                    HashMap<String, String> itemMap = new HashMap<>();
                    itemMap.put(db.KEY_CUSTOMER_NO,"");
                    // itemMap.put(db.KEY_DATE,"");
                    itemMap.put(db.KEY_AMOUNT_CLEARED,"");
                    itemMap.put(db.KEY_INVOICE_NO,"");
                    HashMap<String, String> filter = new HashMap<>();
                    filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                    Cursor pCCursor = db.getData(db.DRIVER_COLLECTION,itemMap,filter);
                    if(pCCursor.getCount()>0){
                        pCCursor.moveToFirst();
                        double amountcleared = 0;
                        do{
                            JSONArray deep = new JSONArray();
                            if(pCCursor.getCount()>0){
                                pCCursor.moveToFirst();

                                do{
                                    JSONObject object = new JSONObject();
                                    amountcleared += Double.parseDouble(pCCursor.getString(pCCursor.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                                    object.put("OrderId",pCCursor.getString(pCCursor.getColumnIndex(db.KEY_INVOICE_NO)));
                                    deep.put(object);
                                }
                                while (pCCursor.moveToNext());
                            }
                            OfflinePost offlinePost = new OfflinePost();
                            offlinePost.setCollectionName(App.POST_COLLECTION);
                            offlinePost.setMap(Helpers.buildCollectionHeader(ConfigStore.CollectionFunction, Settings.getString(App.DRIVER), String.valueOf(amountcleared)));
                            offlinePost.setDeepEntity(deep);
                            arrayList.add(offlinePost);
                        }
                        while (pCCursor.moveToNext());
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }*/
                    break;
                }
                case ConfigStore.UnloadFunction+"U":{
                    try{
                        ArrayList<Unload> arrayListDebit  = new ArrayList<>();
                        ArrayList<Unload> arrayListCredit = new ArrayList<>();
                        ArrayList<Unload> arrayListEndingInventory = new ArrayList<>();
                        for(ArticleHeader articleHeader:articles){
                            HashMap<String,String>map = new HashMap<>();
                            map.put(db.KEY_ID,"");
                            map.put(db.KEY_TIME_STAMP,"");
                            map.put(db.KEY_REASON_CODE,"");
                            map.put(db.KEY_VARIANCE_TYPE,"");
                            map.put(db.KEY_TRIP_ID,"");
                            map.put(db.KEY_ITEM_NO,"");
                            map.put(db.KEY_MATERIAL_DESC1,"");
                            map.put(db.KEY_MATERIAL_NO,"");
                            map.put(db.KEY_MATERIAL_GROUP,"");
                            map.put(db.KEY_CASE,"");
                            map.put(db.KEY_UNIT,"");
                            map.put(db.KEY_UOM,"");
                            map.put(db.KEY_PRICE,"");
                            map.put(db.KEY_ORDER_ID,"");
                            map.put(db.KEY_PURCHASE_NUMBER,"");
                            map.put(db.KEY_IS_POSTED,"");
                            map.put(db.KEY_IS_PRINTED,"");
                            HashMap<String,String>filter = new HashMap<>();
                            filter.put(db.KEY_MATERIAL_NO,articleHeader.getMaterialNo());
                            filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                            Cursor c = db.getData(db.UNLOAD_VARIANCE,map,filter);
                            if(c.getCount()>0){
                                c.moveToFirst();
                                Unload unload = new Unload();
                                unload.setMaterial_no(c.getString(c.getColumnIndex(db.KEY_MATERIAL_NO)));
                                unload.setMaterial_description(c.getString(c.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                                unload.setItem_code(c.getString(c.getColumnIndex(db.KEY_ITEM_NO)));
                                String varianceType = c.getString(c.getColumnIndex(db.KEY_VARIANCE_TYPE));
                                unload.setUom(c.getString(c.getColumnIndex(db.KEY_UOM)));
                                tempPurchaseNumber = c.getString(c.getColumnIndex(db.KEY_ORDER_ID));
                                double cases = 0;
                                double units = 0;
                                do{
                                    cases += Double.parseDouble(c.getString(c.getColumnIndex(db.KEY_CASE)));
                                    units += Double.parseDouble(c.getString(c.getColumnIndex(db.KEY_UNIT)));
                                }
                                while (c.moveToNext());

                                unload.setCases(String.valueOf(cases));
                                unload.setPic(String.valueOf(units));
                                if(varianceType.equals(App.TRUCK_DAMAGE)||varianceType.equals(App.THEFT)){
                                    arrayListDebit.add(unload);
                                }
                                else if(varianceType.equals(App.ENDING_INVENTORY)){
                                    Log.e("I am going EI","EI");
                                    arrayListEndingInventory.add(unload);
                                }
                                else if(varianceType.equals(App.EXCESS)){
                                    arrayListCredit.add(unload);
                                }

                            }
                        }
                        if(arrayListDebit.size()>0){
                            JSONArray deepEntity = new JSONArray();
                            int itemno = 10;
                            for(Unload unload:arrayListDebit){
                                if(unload.getUom().equals(App.CASE_UOM)||unload.getUom().equals(App.BOTTLES_UOM)){
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                                    jo.put("Material", unload.getMaterial_no());
                                    jo.put("Description", unload.getName());
                                    jo.put("Plant", App.PLANT);
                                    jo.put("Quantity", unload.getCases());
                                    jo.put("ItemValue", unload.getPrice());
                                    jo.put("UoM", unload.getUom());
                                    jo.put("Value", unload.getPrice());
                                    jo.put("Storagelocation", App.STORAGE_LOCATION);
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno + 10;
                                    deepEntity.put(jo);
                                }
                                else{
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                                    jo.put("Material", unload.getMaterial_no());
                                    jo.put("Description", unload.getName());
                                    jo.put("Plant", App.PLANT);
                                    jo.put("Quantity", unload.getCases());
                                    jo.put("ItemValue", unload.getPrice());
                                    jo.put("UoM", unload.getUom());
                                    jo.put("Value", unload.getPrice());
                                    jo.put("Storagelocation", App.STORAGE_LOCATION);
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno + 10;
                                    deepEntity.put(jo);
                                }
                            }
                            OfflinePost offlinePost = new OfflinePost();
                            offlinePost.setCollectionName(App.POST_COLLECTION);
                            offlinePost.setMap(Helpers.buildHeaderMap(ConfigStore.UnloadFunction, "", ConfigStore.LoadVarianceDebit, Settings.getString(App.DRIVER), "", tempPurchaseNumber, ""));
                            offlinePost.setDeepEntity(deepEntity);
                            Helpers.logData(getApplication(), "Unload Debit Header" + offlinePost.getMap().toString());
                            Helpers.logData(getApplication(), "Unload Debit Batch Body" + deepEntity.toString());
                            arrayList.add(offlinePost);
                        }
                        if(arrayListEndingInventory.size()>0){
                            JSONArray deepEntity = new JSONArray();
                            int itemno = 10;
                            for(Unload unload:arrayListEndingInventory){
                                if(unload.getUom().equals(App.CASE_UOM)||unload.getUom().equals(App.BOTTLES_UOM)){
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                                    jo.put("Material", unload.getMaterial_no());
                                    jo.put("Description", unload.getName());
                                    jo.put("Plant", App.PLANT);
                                    jo.put("Quantity", unload.getCases());
                                    jo.put("ItemValue", unload.getPrice());
                                    jo.put("UoM", unload.getUom());
                                    jo.put("Value", unload.getPrice());
                                    jo.put("Storagelocation", App.STORAGE_LOCATION);
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno + 10;
                                    deepEntity.put(jo);
                                }
                                else{
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                                    jo.put("Material", unload.getMaterial_no());
                                    jo.put("Description", unload.getName());
                                    jo.put("Plant", App.PLANT);
                                    jo.put("Quantity", unload.getCases());
                                    jo.put("ItemValue", unload.getPrice());
                                    jo.put("UoM", unload.getUom());
                                    jo.put("Value", unload.getPrice());
                                    jo.put("Storagelocation", App.STORAGE_LOCATION);
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno + 10;
                                    deepEntity.put(jo);
                                }
                            }
                            OfflinePost offlinePost = new OfflinePost();
                            offlinePost.setCollectionName(App.POST_COLLECTION);
                            offlinePost.setMap(Helpers.buildHeaderMap(ConfigStore.UnloadFunction, "", ConfigStore.EndingInventory, Settings.getString(App.DRIVER), "", tempPurchaseNumber, ""));
                            offlinePost.setDeepEntity(deepEntity);
                            Helpers.logData(getApplication(), "Ending Inventory Batch Header" + offlinePost.getMap().toString());
                            Helpers.logData(getApplication(), "Ending Inventory Batch Body" + deepEntity.toString());
                            arrayList.add(offlinePost);
                        }
                        if(arrayListCredit.size()>0){
                            JSONArray deepEntity = new JSONArray();
                            int itemno = 10;
                            for(Unload unload:arrayListDebit){
                                if(unload.getUom().equals(App.CASE_UOM)||unload.getUom().equals(App.BOTTLES_UOM)){
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                                    jo.put("Material", unload.getMaterial_no());
                                    jo.put("Description", unload.getName());
                                    jo.put("Plant", App.PLANT);
                                    jo.put("Quantity", unload.getCases());
                                    jo.put("ItemValue", unload.getPrice());
                                    jo.put("UoM", unload.getUom());
                                    jo.put("Value", unload.getPrice());
                                    jo.put("Storagelocation", App.STORAGE_LOCATION);
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno + 10;
                                    deepEntity.put(jo);
                                }
                                else{
                                    JSONObject jo = new JSONObject();
                                    jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                                    jo.put("Material", unload.getMaterial_no());
                                    jo.put("Description", unload.getName());
                                    jo.put("Plant", App.PLANT);
                                    jo.put("Quantity", unload.getCases());
                                    jo.put("ItemValue", unload.getPrice());
                                    jo.put("UoM", unload.getUom());
                                    jo.put("Value", unload.getPrice());
                                    jo.put("Storagelocation", App.STORAGE_LOCATION);
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno + 10;
                                    deepEntity.put(jo);
                                }
                            }
                            OfflinePost offlinePost = new OfflinePost();
                            offlinePost.setCollectionName(App.POST_COLLECTION);
                            offlinePost.setMap(Helpers.buildHeaderMap(ConfigStore.UnloadFunction, "", ConfigStore.LoadVarianceCredit, Settings.getString(App.DRIVER), "", tempPurchaseNumber, ""));
                            offlinePost.setDeepEntity(deepEntity);
                            Helpers.logData(getApplication(), "ArrayList Credit Batch Header" + offlinePost.getMap().toString());
                            Helpers.logData(getApplication(), "ArrayList Credit Batch Body" + deepEntity.toString());
                            arrayList.add(offlinePost);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }


    }
    public class syncData extends AsyncTask<Void,Void,Void> {
        ArrayList<OfflineResponse> data = new ArrayList<>();
        String value = "";
        @Override
        protected void onPreExecute() {

        }
        private syncData(String value){
            this.value = value;
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Log.e("Going for Batch Request", "(Y)" + this.value);
            if(this.value.equals("BEGINDAY")){
                Log.e("Going for beginday","");
                this.data = IntegrationService.batchRequestBeginDay(getApplication(), App.POST_COLLECTION, beginDayList);
            }
            else if(this.value.equals("ODOMETER")){
                this.data = IntegrationService.batchRequestOdometer(getApplication(), App.POST_ODOMETER_SET, odometerList);
            }
            else if(this.value.equals("ADDCUSTOMER")){
                this.data = IntegrationService.batchRequestCustomer(getApplication(), App.POST_CUSTOMER_SET, customerList);
            }
            else{
                this.data = IntegrationService.batchRequest(getApplication(), App.POST_COLLECTION, arrayList);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("REturn data", "" + this.data.size());
            try{
                for(OfflineResponse response:this.data){
                    Log.e("Resp Fun","" + response.getFunction() + " " + response.getPurchaseNumber() + " " + response.getOrderID());
                    Helpers.logData(getApplication(),"ODATA Response" + response.getResponse_code() + "-" + response.getResponse_message()
                    + "-" + response.getFunction() + "-" + response.getOrderID() + "-" + response.getCustomerID());
                    switch (response.getFunction()){
                        case ConfigStore.BeginDayFunction: {
                            if(response.getResponse_code().equals("201")){
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_FUNCTION,ConfigStore.BeginDayFunction);
                                Helpers.logData(getApplication(),"Going for Update" + ConfigStore.BeginDayFunction);
                                db.updateData(db.BEGIN_DAY, map, filter);
                            }
                            else{
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);
                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_FUNCTION,ConfigStore.BeginDayFunction);
                                db.updateData(db.BEGIN_DAY, map, filter);
                            }

                            break;
                        }
                        case ConfigStore.OdometerFunction: {
                            if(response.getResponse_code().equals("201")){
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,response.getCustomerID().equals("Y")?App.DATA_IS_POSTED:App.DATA_MARKED_FOR_POST);

                                HashMap<String,String>filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                Helpers.logData(getApplication(), "Going for Update" + "Odometer");
                                db.updateData(db.ODOMETER,map,filter);
                            }
                            else{
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);

                                HashMap<String,String>filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                db.updateData(db.ODOMETER,map,filter);
                            }

                            break;
                        }
                        case ConfigStore.VisitListFunction:{
                            if(response.getResponse_code().equals("201")){
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);

                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                Helpers.logData(getApplication(), "Going for Update" + "Visit List");
                                db.updateData(db.VISIT_LIST_POST,map,filter);
                            }
                            else{
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);

                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                db.updateData(db.VISIT_LIST_POST,map,filter);
                            }
                            break;
                        }
                        case ConfigStore.LoadConfirmationFunction:{
                            if(response.getResponse_code().equals("201")){
                                if(response.getOrderID().equals("9999999999")){
                                    HashMap<String,String>map = new HashMap<>();
                                    map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                    map.put(db.KEY_IS_POSTED,App.DATA_ERROR);

                                    HashMap<String,String> filter = new HashMap<>();
                                    filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                    filter.put(db.KEY_CUSTOMER_NO,Settings.getString(App.DRIVER));
                                    //filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                    db.updateData(db.LOAD_CONFIRMATION_HEADER,map,filter);
                                }
                                else{
                                    HashMap<String,String>map = new HashMap<>();
                                    map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                    map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);

                                    HashMap<String,String> filter = new HashMap<>();
                                    filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                    filter.put(db.KEY_CUSTOMER_NO,Settings.getString(App.DRIVER));
                                    //filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                    Helpers.logData(getApplication(),"Going for Update" + "LCON");
                                    db.updateData(db.LOAD_CONFIRMATION_HEADER,map,filter);
                                }

                            }
                            else{
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);

                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_CUSTOMER_NO,Settings.getString(App.DRIVER));
                                //filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                db.updateData(db.LOAD_CONFIRMATION_HEADER,map,filter);
                            }
                            break;
                        }
                        case ConfigStore.LoadVarianceFunction+"D":{
                            if(response.getResponse_code().equals("201")){
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_DOCUMENT_TYPE,ConfigStore.LoadVarianceDebit);
                                Helpers.logData(getApplication(), "Going for Update" + ConfigStore.LoadVarianceDebit);
                                db.updateData(db.LOAD_VARIANCE_ITEMS_POST, map, filter);
                            }
                            else{
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_DOCUMENT_TYPE,ConfigStore.LoadVarianceDebit);
                                db.updateData(db.LOAD_VARIANCE_ITEMS_POST, map, filter);
                            }

                            break;
                        }
                        case ConfigStore.LoadVarianceFunction+"C":{
                            if(response.getResponse_code().equals("201")){
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_DOCUMENT_TYPE,ConfigStore.LoadVarianceCredit);
                                Helpers.logData(getApplication(), "Going for Update" + ConfigStore.LoadVarianceCredit);
                                db.updateData(db.LOAD_VARIANCE_ITEMS_POST, map, filter);
                            }
                            else{
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_DOCUMENT_TYPE,ConfigStore.LoadVarianceCredit);
                                db.updateData(db.LOAD_VARIANCE_ITEMS_POST, map, filter);
                            }

                            break;
                        }
                        case ConfigStore.LoadRequestFunction:{
                            if(response.getResponse_code().equals("201")){
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                Helpers.logData(getApplication(), "Going for Update" + ConfigStore.LoadRequestFunction);
                                db.updateData(db.LOAD_REQUEST, map, filter);
                            }
                            else{
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                db.updateData(db.LOAD_REQUEST, map, filter);
                            }
                            break;
                        }
                        case ConfigStore.CustomerOrderRequestFunction+"O":{
                            if(response.getResponse_code().equals("201")){
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                Helpers.logData(getApplication(), "Going for Update" + "Order Request");
                                db.updateData(db.ORDER_REQUEST, map, filter);
                            }
                            else{
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                db.updateData(db.ORDER_REQUEST, map, filter);
                            }

                            break;
                        }
                        case ConfigStore.InvoiceRequestFunction:{
                            if(response.getResponse_code().equals("201")){
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                //filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO, response.getCustomerID());
                                Helpers.logData(getApplication(), "Going for Update" + ConfigStore.InvoiceRequestFunction);
                                db.updateData(db.CAPTURE_SALES_INVOICE, map, filter);

                                HashMap<String, String> map1 = new HashMap<String, String>();
                                map1.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map1.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                                map1.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter1 = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                //filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter1.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                Helpers.logData(getApplication(), "Going for Update2" + ConfigStore.InvoiceRequestFunction);
                                db.updateData(db.CAPTURE_SALES_INVOICE, map1, filter1);

                                HashMap<String,String>returnFilter = new HashMap<>();
                                returnFilter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                returnFilter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                returnFilter.put(db.KEY_CUSTOMER_NO, response.getCustomerID());
                                if(db.checkData(db.RETURNS,returnFilter)){
                                    Helpers.logData(getApplication(), "Going for Update" + "Returns");
                                    db.updateData(db.RETURNS,map,returnFilter);
                                }
                            }
                            else{
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                db.updateData(db.CAPTURE_SALES_INVOICE, map, filter);
                            }

                            break;
                        }
                        case ConfigStore.ReturnsFunction+"G":{
                            if(response.getResponse_code().equals("201")){
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID, response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                filter.put(db.KEY_REASON_TYPE,App.GOOD_RETURN);
                                Helpers.logData(getApplication(), "Going for Update" + "Good Returns");
                                db.updateData(db.RETURNS, map, filter);
                            }
                            else{
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                filter.put(db.KEY_REASON_TYPE,App.GOOD_RETURN);
                                db.updateData(db.RETURNS, map, filter);
                            }

                            break;
                        }
                        case ConfigStore.ReturnsFunction+"B":{
                            if(response.getResponse_code().equals("201")){
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                filter.put(db.KEY_REASON_TYPE,App.BAD_RETURN);
                                Helpers.logData(getApplication(), "Going for Update" + "Bad Returns");
                                db.updateData(db.RETURNS, map, filter);
                            }
                            else{
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                filter.put(db.KEY_REASON_TYPE,App.BAD_RETURN);
                                db.updateData(db.RETURNS, map, filter);
                            }

                            break;
                        }
                        case ConfigStore.CustomerDeliveryRequestFunction:{
                            if(response.getResponse_code().equals("201")){
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                //filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                Helpers.logData(getApplication(), "Going for Update" + ConfigStore.CustomerDeliveryRequestFunction);
                                db.updateData(db.CUSTOMER_DELIVERY_ITEMS_POST, map, filter);
                            }
                            else{
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                //filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                db.updateData(db.CUSTOMER_DELIVERY_ITEMS_POST, map, filter);
                            }
                            break;
                        }
                        case ConfigStore.CustomerDeliveryDeleteRequestFunction:{
                            if(response.getResponse_code().equals("201")){
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                //filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                Helpers.logData(getApplication(), "Going for Update" + ConfigStore.CustomerDeliveryDeleteRequestFunction);
                                db.updateData(db.CUSTOMER_DELIVERY_ITEMS_DELETE_POST, map, filter);
                            }
                            else{
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);
                                map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                db.updateData(db.CUSTOMER_DELIVERY_ITEMS_DELETE_POST, map, filter);
                            }
                            break;
                        }
                        case ConfigStore.CollectionFunction: {
                            if(response.getResponse_code().equals("201")){
                                HashMap<String,String>ivMap = new HashMap<>();
                                ivMap.put(db.KEY_AMOUNT_CLEARED,"");
                                ivMap.put(db.KEY_INVOICE_AMOUNT,"");

                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                                // map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                //filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                if(!response.getPurchaseNumber().equals("")){
                                    filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                }
                                filter.put(db.KEY_CUSTOMER_NO, response.getCustomerID());

                                Cursor prevAmnt = db.getData(db.COLLECTION,ivMap,filter);
                                double newinvAmount = 0;
                                if(prevAmnt.getCount()>0){
                                    prevAmnt.moveToFirst();
                                    newinvAmount  = Double.parseDouble(prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_INVOICE_AMOUNT)))-
                                            Double.parseDouble(prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                                }
                                if(!(newinvAmount==0)){
                                    map.put(db.KEY_INVOICE_AMOUNT,String.valueOf(newinvAmount));
                                    map.put(db.KEY_AMOUNT_CLEARED,String.valueOf("0"));
                                }
                                Helpers.logData(getApplication(), "Going for Update" + ConfigStore.CollectionFunction);
                                db.updateData(db.COLLECTION,map,filter);
                            }
                            else{
                                HashMap<String,String>ivMap = new HashMap<>();
                                ivMap.put(db.KEY_AMOUNT_CLEARED,"");
                                ivMap.put(db.KEY_INVOICE_AMOUNT,"");

                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);
                                // map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                //filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                //filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO, response.getCustomerID());

                                Cursor prevAmnt = db.getData(db.COLLECTION,ivMap,filter);
                                double newinvAmount = 0;
                                if(prevAmnt.getCount()>0){
                                    prevAmnt.moveToFirst();
                                    newinvAmount  = Double.parseDouble(prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_INVOICE_AMOUNT)))-
                                            Double.parseDouble(prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                                }
                                if(!(newinvAmount==0)){
                                    map.put(db.KEY_INVOICE_AMOUNT,String.valueOf(newinvAmount));
                                    map.put(db.KEY_AMOUNT_CLEARED,String.valueOf("0"));
                                }
                                db.updateData(db.COLLECTION,map,filter);
                            }
                            break;
                        }
                        case ConfigStore.CollectionFunction+"D": {
                            if(response.getResponse_code().equals("201")){
                                HashMap<String,String>ivMap = new HashMap<>();
                                ivMap.put(db.KEY_AMOUNT_CLEARED,"");
                                ivMap.put(db.KEY_INVOICE_AMOUNT,"");

                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                                // map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                //filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                if(!response.getPurchaseNumber().equals("")){
                                    filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                }
                                filter.put(db.KEY_CUSTOMER_NO, response.getCustomerID());

                                Cursor prevAmnt = db.getData(db.DRIVER_COLLECTION,ivMap,filter);
                                double newinvAmount = 0;
                                if(prevAmnt.getCount()>0){
                                    prevAmnt.moveToFirst();
                                    newinvAmount  = Double.parseDouble(prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_INVOICE_AMOUNT)))-
                                            Double.parseDouble(prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                                }
                                if(!(newinvAmount==0)){
                                    map.put(db.KEY_INVOICE_AMOUNT,String.valueOf(newinvAmount));
                                    map.put(db.KEY_AMOUNT_CLEARED,String.valueOf("0"));
                                }
                                Helpers.logData(getApplication(), "Going for Update" + "Driver Collection");
                                db.updateData(db.DRIVER_COLLECTION,map,filter);
                            }
                            else{
                                HashMap<String,String>ivMap = new HashMap<>();
                                ivMap.put(db.KEY_AMOUNT_CLEARED,"");
                                ivMap.put(db.KEY_INVOICE_AMOUNT,"");

                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);
                                // map.put(db.KEY_ORDER_ID,response.getOrderID());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                //filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                //filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO, response.getCustomerID());

                                Cursor prevAmnt = db.getData(db.DRIVER_COLLECTION,ivMap,filter);
                                double newinvAmount = 0;
                                if(prevAmnt.getCount()>0){
                                    prevAmnt.moveToFirst();
                                    newinvAmount  = Double.parseDouble(prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_INVOICE_AMOUNT)))-
                                            Double.parseDouble(prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                                }
                                if(!(newinvAmount==0)){
                                    map.put(db.KEY_INVOICE_AMOUNT,String.valueOf(newinvAmount));
                                    map.put(db.KEY_AMOUNT_CLEARED,String.valueOf("0"));
                                }
                                db.updateData(db.DRIVER_COLLECTION,map,filter);
                            }
                            break;
                        }
                        case ConfigStore.PartialCollectionFunction: {
                            if(response.getResponse_code().equals("201")){
                                HashMap<String,String>ivMap = new HashMap<>();
                                ivMap.put(db.KEY_AMOUNT_CLEARED,"");
                                ivMap.put(db.KEY_INVOICE_AMOUNT,"");

                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                                // map.put(db.KEY_ORDER_ID,response.getOrderID());

                                //Added by Rakshit on 09/04/2017 to store the partial collection temporarily
                                //Change no 20170409
                                HashMap<String,String>partMap = new HashMap<>();
                                partMap.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                //filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                if(!response.getOrderID().equals("")){
                                    filter.put(db.KEY_INVOICE_NO,response.getOrderID());
                                    partMap.put(db.KEY_INVOICE_NO,response.getOrderID()); //Change no 20170409
                                }
                                filter.put(db.KEY_CUSTOMER_NO, response.getCustomerID());
                                partMap.put(db.KEY_CUSTOMER_NO,response.getCustomerID()); //Change no 20170409

                                Cursor prevAmnt = db.getData(db.COLLECTION,ivMap,filter);
                                double newinvAmount = 0;
                                if(prevAmnt.getCount()>0){
                                    prevAmnt.moveToFirst();
                                    newinvAmount  = Double.parseDouble(prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_INVOICE_AMOUNT)))-
                                            Double.parseDouble(prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                                    partMap.put(db.KEY_INVOICE_AMOUNT,prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_INVOICE_AMOUNT)));
                                    partMap.put(db.KEY_AMOUNT_CLEARED,prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                                }
                                if(!(newinvAmount==0)){
                                    map.put(db.KEY_INVOICE_AMOUNT,String.valueOf(newinvAmount));
                                    map.put(db.KEY_AMOUNT_CLEARED,String.valueOf("0"));
                                }
                                Helpers.logData(getApplication(), "Going for Update" + ConfigStore.PartialCollectionFunction);
                                db.updateData(db.COLLECTION, map, filter);
                                db.addData(db.PARTIAL_COLLECTION_TEMP,partMap); //Change no 20170409
                            }
                            else{
                                HashMap<String,String>ivMap = new HashMap<>();
                                ivMap.put(db.KEY_AMOUNT_CLEARED,"");
                                ivMap.put(db.KEY_INVOICE_AMOUNT,"");

                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);
                                // map.put(db.KEY_ORDER_ID,response.getOrderID());

                                //Added by Rakshit on 09/04/2017 to store the partial collection temporarily
                                //Change no 20170409
                                HashMap<String,String>partMap = new HashMap<>();
                                partMap.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());

                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                //filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                //filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO, response.getCustomerID());
                                partMap.put(db.KEY_CUSTOMER_NO,response.getCustomerID()); //Change no 20170409

                                Cursor prevAmnt = db.getData(db.COLLECTION,ivMap,filter);
                                double newinvAmount = 0;
                                if(prevAmnt.getCount()>0){
                                    prevAmnt.moveToFirst();
                                    newinvAmount  = Double.parseDouble(prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_INVOICE_AMOUNT)))-
                                            Double.parseDouble(prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_AMOUNT_CLEARED)));

                                    partMap.put(db.KEY_INVOICE_AMOUNT, prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_INVOICE_AMOUNT)));
                                    partMap.put(db.KEY_AMOUNT_CLEARED, prevAmnt.getString(prevAmnt.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                                }
                                if(!(newinvAmount==0)){
                                    map.put(db.KEY_INVOICE_AMOUNT,String.valueOf(newinvAmount));
                                    map.put(db.KEY_AMOUNT_CLEARED,String.valueOf("0"));
                                }
                                Log.e("Going Partial","" + map);
                                Log.e("Going Filter","" + filter);
                                db.updateData(db.COLLECTION,map,filter);
                                db.addData(db.PARTIAL_COLLECTION_TEMP,partMap); //Change no 20170409
                            }
                            break;
                        }
                        case ConfigStore.AddCustomerFunction: {
                            if(response.getResponse_code().equals("201")){
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);

                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                Helpers.logData(getApplication(), "Going for Update" + ConfigStore.AddCustomerFunction);
                                db.updateData(db.NEW_CUSTOMER_POST,map,filter);
                            }
                            else{
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);

                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                db.updateData(db.NEW_CUSTOMER_POST,map,filter);
                            }
                            break;
                        }
                        case ConfigStore.EndDayFunction: {

                            break;
                        }
                        case ConfigStore.UnloadFunction+"U":{
                            if(response.getResponse_code().equals("201")){
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);

                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                               // filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                Helpers.logData(getApplication(), "Going for Update" + "Unload Function");
                                db.updateData(db.UNLOAD_VARIANCE,map,filter);
                                db.updateData(db.UNLOAD_TRANSACTION,map,filter);
                            }
                            else{
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_ERROR);

                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                //filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                db.updateData(db.UNLOAD_VARIANCE,map,filter);
                            }
                            break;
                        }
                    }
                }
                Settings.setString(App.IS_DATA_SYNCING,"false");
            }
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
            }

        }
    }
    public int getSyncCount(String function) {
        int syncCount = 0;
        try{
            HashMap<String,String> map = new HashMap<String, String>();
            map.put(db.KEY_TIME_STAMP, "");

            HashMap<String,String> filter = new HashMap<>();
            filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);

            switch (function){
                case ConfigStore.BeginDayFunction:{
                    filter.put(db.KEY_FUNCTION,ConfigStore.BeginDayFunction);
                    Cursor beginDayRequest = db.getData(db.BEGIN_DAY,map,filter);
                    syncCount = beginDayRequest.getCount();
                    break;
                }
                case ConfigStore.OdometerFunction:{
                    Cursor odometerRequest = db.getData(db.ODOMETER,map,filter);
                    syncCount = odometerRequest.getCount();
                    break;
                }
                case ConfigStore.LoadConfirmationFunction:{
                    Cursor loadConfirmationRequest = db.getData(db.LOAD_CONFIRMATION_HEADER,map,filter);
                    syncCount = loadConfirmationRequest.getCount();
                    Log.e("LCO Count","" + syncCount);
                    break;
                }
                case ConfigStore.LoadVarianceFunction+"D":{
                    filter.put(db.KEY_DOCUMENT_TYPE, ConfigStore.LoadVarianceDebit);
                    Cursor loadVarianceDebitRequest = db.getData(db.LOAD_VARIANCE_ITEMS_POST,map,filter);
                    syncCount = loadVarianceDebitRequest.getCount();
                    break;
                }
                case ConfigStore.LoadVarianceFunction+"C":{
                    filter.put(db.KEY_DOCUMENT_TYPE, ConfigStore.LoadVarianceCredit);
                    Cursor loadVarianceCreditRequest = db.getData(db.LOAD_VARIANCE_ITEMS_POST,map,filter);
                    syncCount = loadVarianceCreditRequest.getCount();
                    break;
                }
                case ConfigStore.LoadRequestFunction:{
                    Cursor loadRequest = db.getData(db.LOAD_REQUEST,map,filter);
                    syncCount = loadRequest.getCount();
                    break;
                }
                case ConfigStore.VisitListFunction:{
                    Cursor visitListRequest = db.getData(db.VISIT_LIST_POST,map,filter);
                    syncCount = visitListRequest.getCount();
                    break;
                }
                case ConfigStore.CustomerOrderRequestFunction+"O":{
                    Cursor orderRequest = db.getData(db.ORDER_REQUEST,map,filter);
                    syncCount = orderRequest.getCount();
                    break;
                }
                case ConfigStore.InvoiceRequestFunction:{
                    Cursor invoiceRequest = db.getData(db.CAPTURE_SALES_INVOICE,map,filter);
                    syncCount = invoiceRequest.getCount();
                    break;
                }
                case ConfigStore.CustomerDeliveryRequestFunction:{
                    Cursor deliveryRequest = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST,map,filter);
                    syncCount = deliveryRequest.getCount();
                    break;
                }
                case ConfigStore.CustomerDeliveryDeleteRequestFunction:{
                    Cursor deliveryDeleteRequest = db.getData(db.CUSTOMER_DELIVERY_ITEMS_DELETE_POST,map,filter);
                    syncCount = deliveryDeleteRequest.getCount();
                    break;
                }
                //Case statement for Good Returns
                case ConfigStore.ReturnsFunction+"G":{
                    filter.put(db.KEY_REASON_TYPE,App.GOOD_RETURN);
                    Cursor goodReturnsRequest = db.getData(db.RETURNS,map,filter);
                    syncCount = goodReturnsRequest.getCount();
                    Log.e("GR Sync Count","" + syncCount);
                    break;
                }
                //Case Statement for Bad Returns
                case ConfigStore.ReturnsFunction+"B": {
                    filter.put(db.KEY_REASON_TYPE,App.BAD_RETURN);
                    Cursor goodReturnsRequest = db.getData(db.RETURNS,map,filter);
                    syncCount = goodReturnsRequest.getCount();
                    break;
                }
                case ConfigStore.CollectionFunction:{
                    filter.put(db.KEY_IS_INVOICE_COMPLETE,App.INVOICE_COMPLETE);
                    Cursor collectionRequest = db.getData(db.COLLECTION,map,filter);
                    syncCount = collectionRequest.getCount();
                    break;
                }
                case ConfigStore.PartialCollectionFunction:{
                    filter.put(db.KEY_IS_INVOICE_COMPLETE,App.INVOICE_PARTIAL);
                    Cursor collectionRequest = db.getData(db.COLLECTION,map,filter);
                    syncCount = collectionRequest.getCount();
                    break;
                }
            /*case ConfigStore.CollectionFunction+"D":{
                Cursor driverCollectionRequest = db.getData(db.DRIVER_COLLECTION,map,filter);
                syncCount = driverCollectionRequest.getCount();
                break;
            }*/
                case ConfigStore.AddCustomerFunction:{
                    Cursor newCustomerRequest = db.getData(db.NEW_CUSTOMER_POST,map,filter);
                    syncCount = newCustomerRequest.getCount();
                    break;
                }
                case ConfigStore.EndDayFunction:{

                    break;
                }
                case ConfigStore.UnloadFunction+"U":{
                    Cursor unloadFunction = db.getData(db.UNLOAD_VARIANCE,map,filter);
                    syncCount = unloadFunction.getCount();
                    Log.e("Unload Sync Count","" + syncCount);
                    break;
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }


        return syncCount;
    }
    public int getSyncCount(){
        int syncCount = 0;
        try{
            HashMap<String,String> map = new HashMap<String, String>();
            map.put(db.KEY_TIME_STAMP, "");

            HashMap<String,String> filter = new HashMap<>();
            filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);

            //Filter for Begin Day
            HashMap<String,String> bdFilter = new HashMap<>();
            bdFilter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
            bdFilter.put(db.KEY_FUNCTION,ConfigStore.BeginDayFunction);

            //Filter for good Return
            HashMap<String,String> grFilter = new HashMap<>();
            grFilter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
            grFilter.put(db.KEY_REASON_TYPE,App.GOOD_RETURN);

            //Filter for bad Return
            HashMap<String,String> brFilter = new HashMap<>();
            brFilter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
            brFilter.put(db.KEY_REASON_TYPE,App.BAD_RETURN);

            Cursor beginDayRequest = db.getData(db.BEGIN_DAY,map,bdFilter);
            Cursor odometerRequest = db.getData(db.ODOMETER,map,filter);
            Cursor loadConfirmationRequest = db.getData(db.LOAD_CONFIRMATION_HEADER,map,filter);
            Cursor loadRequest = db.getData(db.LOAD_REQUEST,map,filter);
            Cursor orderRequest = db.getData(db.ORDER_REQUEST,map,filter);
            Cursor invoiceRequest = db.getData(db.CAPTURE_SALES_INVOICE,map,filter);
            Cursor deliveryRequest = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST,map,filter);
            Cursor deliveryDeleteRequest = db.getData(db.CUSTOMER_DELIVERY_ITEMS_DELETE_POST,map,filter);
            Cursor goodReturnRequest = db.getData(db.RETURNS,map,grFilter);
            Cursor badReturnRequest = db.getData(db.RETURNS,map,brFilter);
            Cursor visitListCursor = db.getData(db.VISIT_LIST_POST,map,filter);
            Cursor newCustomerCursor = db.getData(db.NEW_CUSTOMER_POST,map,filter);
            Cursor loadVarianceCursor = db.getData(db.LOAD_VARIANCE_ITEMS_POST,map,filter);
            Cursor collectionCursor = db.getData(db.COLLECTION,map,filter);
            Cursor driverCollectionCursor = db.getData(db.DRIVER_COLLECTION,map,filter);
            Cursor unloadCursor = db.getData(db.UNLOAD_VARIANCE,map,filter);

            if(beginDayRequest.getCount()>0){
                syncCount+= beginDayRequest.getCount();
            }
            if(odometerRequest.getCount()>0){
                syncCount+= odometerRequest.getCount();
            }
            if(loadConfirmationRequest.getCount()>0){
                syncCount+= loadConfirmationRequest.getCount();
            }
            if(loadRequest.getCount()>0){
                syncCount += loadRequest.getCount();
            }
            if(orderRequest.getCount()>0){
                syncCount += orderRequest.getCount();
            }
            if(invoiceRequest.getCount()>0){
                syncCount += invoiceRequest.getCount();
            }
            if(deliveryRequest.getCount()>0){
                syncCount += deliveryRequest.getCount();
            }
            if(deliveryDeleteRequest.getCount()>0){
                syncCount += deliveryDeleteRequest.getCount();
            }
            if(goodReturnRequest.getCount()>0){
                syncCount += goodReturnRequest.getCount();
            }
            if(badReturnRequest.getCount()>0){
                syncCount += badReturnRequest.getCount();
            }
            if(visitListCursor.getCount()>0){
                syncCount += visitListCursor.getCount();
            }
            if(loadVarianceCursor.getCount()>0){
                syncCount += loadVarianceCursor.getCount();
            }
            if(collectionCursor.getCount()>0){
                syncCount += collectionCursor.getCount();
            }
            if(driverCollectionCursor.getCount()>0){
                syncCount += driverCollectionCursor.getCount();
            }
            if(newCustomerCursor.getCount()>0){
                syncCount += newCustomerCursor.getCount();
            }
            if(unloadCursor.getCount()>0){
                syncCount += unloadCursor.getCount();
            }
            Log.e("Sync count","" + syncCount);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return syncCount;
    }
}
