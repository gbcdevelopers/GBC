package gbc.sa.vansales.sap;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.activities.SettingsActivity;
import gbc.sa.vansales.models.OfflinePost;
import gbc.sa.vansales.models.OfflineResponse;
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
    public static String TAG = "SyncData";
    DatabaseHandler db = new DatabaseHandler(this);

    public SyncData(){
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("I am here", "IntentService" + Settings.getString(App.IS_DATA_SYNCING));
        if(!Boolean.parseBoolean(Settings.getString(App.IS_DATA_SYNCING))){
            Log.e("Inside", "Inside" + getSyncCount());
            if(getSyncCount()>0){
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
        if(!isEmpty){
            new syncData("");
        }

    }
    public void generateBatch(String request) {

        String purchaseNumber = "";
        String tempPurchaseNumber = "";
        String customerNumber = "";
        String tempCustomerNumber = "";


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
                    odometerList.add(object);
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
                        OfflinePost object = new OfflinePost();
                        object.setCollectionName(App.POST_COLLECTION);
                        object.setMap(Helpers.buildLoadConfirmationHeader(cursor.getString(cursor.getColumnIndex(db.KEY_FUNCTION)),
                                cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID)), Settings.getString(App.DRIVER)));
                        object.setDeepEntity(deepEntity);
                        arrayList.add(object);
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
                            documentDate = pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_DATE));
                            if(purchaseNumber.equals("")){
                                purchaseNumber = tempPurchaseNumber;
                            }
                            else if(purchaseNumber.equals(tempPurchaseNumber)){

                            }
                            else{
                                OfflinePost object = new OfflinePost();
                                object.setCollectionName(App.POST_COLLECTION);
                                object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadVarianceFunction,"",ConfigStore.LoadVarianceDebit,Settings.getString(App.DRIVER),"",purchaseNumber,documentDate));
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
                                object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadVarianceFunction, "", ConfigStore.LoadVarianceDebit, Settings.getString(App.DRIVER), "", purchaseNumber,documentDate));
                                object.setDeepEntity(deepEntity);
                                arrayList.add(object);
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

                    Cursor pendingLoadRequestCursor = db.getData(db.LOAD_VARIANCE_ITEMS_POST,itemMap,filter);
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
                                object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadVarianceFunction,"",ConfigStore.LoadVarianceCredit,Settings.getString(App.DRIVER),"",purchaseNumber,documentDate));
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
                                object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadVarianceFunction, "", ConfigStore.LoadVarianceCredit, Settings.getString(App.DRIVER), "", purchaseNumber,documentDate));
                                object.setDeepEntity(deepEntity);
                                arrayList.add(object);
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
                    itemMap.put(db.KEY_CUSTOMER_NO,"");
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
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.CustomerOrderRequestDocumentType, customerNumber, "", purchaseNumber,documentDate));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.CustomerOrderRequestDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                    object.setDeepEntity(deepEntity);
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
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.CustomerOrderRequestDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.CustomerOrderRequestDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                    object.setDeepEntity(deepEntity);
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
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.InvoiceRequestFunction, "", ConfigStore.InvoiceDocumentType, customerNumber, "", purchaseNumber,""));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
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
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.InvoiceRequestFunction, "", ConfigStore.InvoiceDocumentType, customerNumber, "", purchaseNumber,""));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.InvoiceRequestFunction, "", ConfigStore.InvoiceDocumentType, customerNumber, "", purchaseNumber,""));
                                    object.setDeepEntity(deepEntity);
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
                        while (pendingInvoiceCursor.moveToNext());
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            case ConfigStore.ReturnsFunction+"G":{
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
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMapReason(ConfigStore.ReturnsFunction, "", ConfigStore.GoodReturnType, customerNumber, "", purchaseNumber, "", reasonCode));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMapReason(ConfigStore.ReturnsFunction, "", ConfigStore.GoodReturnType, customerNumber, "", purchaseNumber, "", reasonCode));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
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
                                jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
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
                                    object.setMap(Helpers.buildHeaderMapReason(ConfigStore.ReturnsFunction, "", ConfigStore.GoodReturnType, customerNumber, "", purchaseNumber, "", reasonCode));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMapReason(ConfigStore.ReturnsFunction, "", ConfigStore.GoodReturnType, customerNumber, "", purchaseNumber, "", reasonCode));
                                    object.setDeepEntity(deepEntity);
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
                        while (pendingInvoiceCursor.moveToNext());
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
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMapReason(ConfigStore.ReturnsFunction, "", ConfigStore.BadReturnType, customerNumber, "", purchaseNumber, "", reasonCode));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMapReason(ConfigStore.ReturnsFunction, "", ConfigStore.BadReturnType, customerNumber, "", purchaseNumber, "", reasonCode));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
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
                                jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
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
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMapReason(ConfigStore.ReturnsFunction, "", ConfigStore.BadReturnType, customerNumber, "", purchaseNumber, "", reasonCode));
                                    object.setDeepEntity(deepEntity);
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
                        String deliveryNo = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_DELIVERY_NO));
                        int itemno = 10;
                        String documentDate = "";
                        do{
                            tempPurchaseNumber = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_ORDER_ID));
                            tempCustomerNumber = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_CUSTOMER_NO));
                            documentDate = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_DATE));
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
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryRequestFunction, deliveryNo, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryRequestFunction, deliveryNo, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                    object.setDeepEntity(deepEntity);
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
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryRequestFunction, deliveryNo, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryRequestFunction, deliveryNo, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                    object.setDeepEntity(deepEntity);
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
                            tempPurchaseNumber = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_ORDER_ID));
                            tempCustomerNumber = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_CUSTOMER_NO));
                            deliveryNo = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_DELIVERY_NO));
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
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryDeleteRequestFunction, deliveryNo, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryDeleteRequestFunction, deliveryNo, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    customerNumber = tempCustomerNumber;
                                    deepEntity = new JSONArray();
                                }

                            }

                            if(pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                JSONObject jo = new JSONObject();
                                jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                jo.put("OrderId",deliveryNo);
                                jo.put("Material",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                jo.put("Description",UrlBuilder.decodeString(pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                jo.put("Plant","");
                                jo.put("Quantity",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_CASE)));
                                jo.put("ItemValue", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_AMOUNT)));
                                jo.put("UoM", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)));
                                jo.put("Value", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_AMOUNT)));
                                jo.put("Storagelocation", "");
                                jo.put("RejReason",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_REASON_CODE)));
                                jo.put("Route", Settings.getString(App.ROUTE));
                                itemno = itemno+10;
                                deepEntity.put(jo);
                            }
                            else{
                                JSONObject jo = new JSONObject();
                                jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                jo.put("OrderId",deliveryNo);
                                jo.put("Material",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                jo.put("Description",UrlBuilder.decodeString(pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                jo.put("Plant","");
                                jo.put("Quantity",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UNIT)));
                                jo.put("ItemValue", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_AMOUNT)));
                                jo.put("UoM", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)));
                                jo.put("Value", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_AMOUNT)));
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
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryDeleteRequestFunction, deliveryNo, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.CustomerDeliveryDeleteRequestFunction, deliveryNo, ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber, documentDate));
                                    object.setDeepEntity(deepEntity);
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
                this.data = IntegrationService.batchRequestBeginDay(getApplicationContext(), App.POST_COLLECTION, beginDayList);
            }
            else if(this.value.equals("ODOMETER")){
                this.data = IntegrationService.batchRequestOdometer(getApplicationContext(), App.POST_ODOMETER_SET, odometerList);
            }
            else{
                this.data = IntegrationService.batchRequest(getApplicationContext(), App.POST_COLLECTION, arrayList);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("REturn data", "" + this.data.size());
            try{
                for(OfflineResponse response:this.data){
                    Log.e("Resp Fun","" + response.getFunction());
                    switch (response.getFunction()){
                        case ConfigStore.BeginDayFunction: {
                            HashMap<String,String>map = new HashMap<>();
                            map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                            HashMap<String,String> filter = new HashMap<>();
                            filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                            filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            filter.put(db.KEY_FUNCTION,ConfigStore.BeginDayFunction);
                            db.updateData(db.BEGIN_DAY, map, filter);
                            break;
                        }
                        case ConfigStore.OdometerFunction: {
                            HashMap<String,String>map = new HashMap<>();
                            map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_IS_POSTED,response.getCustomerID().equals("Y")?App.DATA_IS_POSTED:App.DATA_MARKED_FOR_POST);

                            HashMap<String,String>filter = new HashMap<>();
                            filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                            filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            db.updateData(db.ODOMETER,map,filter);
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
                                db.updateData(db.VISIT_LIST_POST,map,filter);
                            }
                            break;
                        }
                        case ConfigStore.LoadConfirmationFunction:{
                            if(response.getResponse_code().equals("201")){
                                HashMap<String,String>map = new HashMap<>();
                                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);

                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                db.updateData(db.LOAD_CONFIRMATION_HEADER,map,filter);
                            }
                            break;
                        }
                        case ConfigStore.LoadVarianceFunction+"D":{
                            HashMap<String,String>map = new HashMap<>();
                            map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                            map.put(db.KEY_ORDER_ID,response.getOrderID());

                            HashMap<String,String> filter = new HashMap<>();
                            filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                            filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                            filter.put(db.KEY_DOCUMENT_TYPE,ConfigStore.LoadVarianceDebit);
                            db.updateData(db.LOAD_VARIANCE_ITEMS_POST, map, filter);
                            break;
                        }
                        case ConfigStore.LoadVarianceFunction+"C":{
                            HashMap<String,String>map = new HashMap<>();
                            map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                            map.put(db.KEY_ORDER_ID,response.getOrderID());

                            HashMap<String,String> filter = new HashMap<>();
                            filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                            filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                            filter.put(db.KEY_DOCUMENT_TYPE,ConfigStore.LoadVarianceCredit);
                            db.updateData(db.LOAD_VARIANCE_ITEMS_POST, map, filter);
                            break;
                        }
                        case ConfigStore.LoadRequestFunction:{
                            HashMap<String,String>map = new HashMap<>();
                            map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                            map.put(db.KEY_ORDER_ID,response.getOrderID());

                            HashMap<String,String> filter = new HashMap<>();
                            filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                            filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                            db.updateData(db.LOAD_REQUEST, map, filter);
                            break;
                        }
                        case ConfigStore.CustomerOrderRequestFunction+"O":{
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                            map.put(db.KEY_ORDER_ID,response.getOrderID());

                            HashMap<String, String> filter = new HashMap<>();
                            filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                            filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                            filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                            db.updateData(db.ORDER_REQUEST, map, filter);
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
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                                filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                                db.updateData(db.CAPTURE_SALES_INVOICE, map, filter);
                            }

                            break;
                        }
                        case ConfigStore.ReturnsFunction+"G":{

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                            map.put(db.KEY_ORDER_ID,response.getOrderID());

                            HashMap<String, String> filter = new HashMap<>();
                            filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                            filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                            filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                            filter.put(db.KEY_REASON_TYPE,App.GOOD_RETURN);
                            db.updateData(db.RETURNS, map, filter);

                            break;
                        }
                        case ConfigStore.ReturnsFunction+"B":{

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
                            db.updateData(db.RETURNS, map, filter);

                            break;
                        }
                        case ConfigStore.CustomerDeliveryRequestFunction:{
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                            map.put(db.KEY_ORDER_ID,response.getOrderID());

                            HashMap<String, String> filter = new HashMap<>();
                            filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                            filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                            filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                            db.updateData(db.CUSTOMER_DELIVERY_ITEMS_POST, map, filter);
                            break;
                        }
                        case ConfigStore.CustomerDeliveryDeleteRequestFunction:{
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                            map.put(db.KEY_ORDER_ID,response.getOrderID());

                            HashMap<String, String> filter = new HashMap<>();
                            filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                            filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            filter.put(db.KEY_ORDER_ID,response.getPurchaseNumber());
                            filter.put(db.KEY_CUSTOMER_NO,response.getCustomerID());
                            db.updateData(db.CUSTOMER_DELIVERY_ITEMS_DELETE_POST, map, filter);
                            break;
                        }
                        case ConfigStore.CollectionFunction: {

                            break;
                        }
                        case ConfigStore.AddCustomerFunction: {

                            break;
                        }
                        case ConfigStore.EndDayFunction: {

                            break;
                        }
                        case ConfigStore.UnloadFunction+"U":{

                            break;
                        }
                    }
                }
                Settings.setString(App.IS_DATA_SYNCING,"false");
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    public int getSyncCount(String function){
        int syncCount = 0;
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

                break;
            }
            case ConfigStore.AddCustomerFunction:{

                break;
            }
            case ConfigStore.EndDayFunction:{

                break;
            }
            case ConfigStore.UnloadFunction+"U":{

                break;
            }

        }

        return syncCount;
    }
    public int getSyncCount(){
        int syncCount = 0;
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
        Cursor loadVarianceCursor = db.getData(db.LOAD_VARIANCE_ITEMS_POST,map,filter);

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
        Log.e("Sync count","" + syncCount);
        return syncCount;
    }
}
