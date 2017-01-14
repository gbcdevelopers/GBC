package gbc.sa.vansales.sap;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.activities.SettingsActivity;
import gbc.sa.vansales.models.OfflinePost;
import gbc.sa.vansales.models.OfflineResponse;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.Settings;
/**
 * Created by Rakshit on 11-Jan-17.
 */
public class SyncData extends IntentService {
    ArrayList<OfflinePost> arrayList = new ArrayList<>();
    public static String TAG = "SyncData";
    DatabaseHandler db = new DatabaseHandler(this);

    public SyncData(){
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("I am here", "IntentService" + Settings.getString(App.IS_DATA_SYNCING));
        if(!Boolean.parseBoolean(Settings.getString(App.IS_DATA_SYNCING))){
            Log.e("Inside","Inside" + getSyncCount());
            if(getSyncCount()>0){
                Settings.setString(App.IS_DATA_SYNCING,"true");
                syncData();
            }
        }
    }
    public void syncData(){
        boolean isEmpty = true;
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
        if(getSyncCount(ConfigStore.CustomerDeliveryRequestFunction)>0){
            if(isEmpty){
                isEmpty = false;
            }
            generateBatch(ConfigStore.CustomerDeliveryRequestFunction);
        }
        if(!isEmpty){
            new syncData().execute();
        }

    }
    public void generateBatch(String request) {

        String purchaseNumber = "";
        String tempPurchaseNumber = "";
        String customerNumber = "";
        String tempCustomerNumber = "";

        switch (request){
            case ConfigStore.LoadRequestFunction:{
                try{

                    JSONArray deepEntity = new JSONArray();
                    HashMap<String, String> itemMap = new HashMap<>();
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
                        do{
                            tempPurchaseNumber = pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_ORDER_ID));
                            if(purchaseNumber.equals("")){
                                purchaseNumber = tempPurchaseNumber;
                            }
                            else if(purchaseNumber.equals(tempPurchaseNumber)){

                            }
                            else{
                                OfflinePost object = new OfflinePost();
                                object.setCollectionName(App.POST_COLLECTION);
                                object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction,"",ConfigStore.DocumentType,Settings.getString(App.DRIVER),"",purchaseNumber));
                                object.setDeepEntity(deepEntity);
                                arrayList.add(object);
                                purchaseNumber = tempPurchaseNumber;
                                deepEntity = new JSONArray();
                            }

                            if(pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)){
                                JSONObject jo = new JSONObject();
                                jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                jo.put("Material",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                jo.put("Description",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                                jo.put("Plant","");
                                jo.put("Quantity",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_CASE)));
                                jo.put("ItemValue", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                jo.put("UoM", App.CASE_UOM);
                                jo.put("Value", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                jo.put("Storagelocation", "");
                                jo.put("Route", Settings.getString(App.ROUTE));
                                itemno = itemno+10;
                                deepEntity.put(jo);
                            }
                            if(pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                JSONObject jo = new JSONObject();
                                jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                jo.put("Material",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                jo.put("Description",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                                jo.put("Plant","");
                                jo.put("Quantity",pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_UNIT)));
                                jo.put("ItemValue", pendingLoadRequestCursor.getString(pendingLoadRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                jo.put("UoM", App.BOTTLES_UOM);
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
                                object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.DocumentType, Settings.getString(App.DRIVER), "", purchaseNumber));
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
                        do{
                            tempPurchaseNumber = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_ORDER_ID));
                            tempCustomerNumber = pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_CUSTOMER_NO));
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
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.DocumentType, customerNumber, "", purchaseNumber));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.DocumentType, customerNumber, "", purchaseNumber));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    customerNumber = tempCustomerNumber;
                                    deepEntity = new JSONArray();
                                }

                            }

                            if(pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)){
                                JSONObject jo = new JSONObject();
                                jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                jo.put("Material",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                jo.put("Description",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                                jo.put("Plant","");
                                jo.put("Quantity",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_CASE)));
                                jo.put("ItemValue", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                jo.put("UoM", App.CASE_UOM);
                                jo.put("Value", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                jo.put("Storagelocation", "");
                                jo.put("Route", Settings.getString(App.ROUTE));
                                itemno = itemno+10;
                                deepEntity.put(jo);
                            }
                            if(pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                JSONObject jo = new JSONObject();
                                jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                jo.put("Material",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                jo.put("Description",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                                jo.put("Plant","");
                                jo.put("Quantity",pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_UNIT)));
                                jo.put("ItemValue", pendingOrderRequestCursor.getString(pendingOrderRequestCursor.getColumnIndex(db.KEY_PRICE)));
                                jo.put("UoM", App.BOTTLES_UOM);
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
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.DocumentType, customerNumber, "", purchaseNumber));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.LoadRequestFunction, "", ConfigStore.DocumentType, customerNumber, "", purchaseNumber));
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
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.InvoiceRequestFunction, "", ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.InvoiceRequestFunction, "", ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    customerNumber = tempCustomerNumber;
                                    deepEntity = new JSONArray();
                                }

                            }

                            if(pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)){
                                JSONObject jo = new JSONObject();
                                jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                jo.put("Material",pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                jo.put("Description",pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                                jo.put("Plant","");
                                jo.put("Quantity",pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_CASE)));
                                jo.put("ItemValue", pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_PRICE)));
                                jo.put("UoM", App.CASE_UOM);
                                jo.put("Value", pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_PRICE)));
                                jo.put("Storagelocation", "");
                                jo.put("Route", Settings.getString(App.ROUTE));
                                itemno = itemno+10;
                                deepEntity.put(jo);
                            }
                            if(pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                JSONObject jo = new JSONObject();
                                jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                                jo.put("Material",pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                jo.put("Description",pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                                jo.put("Plant","");
                                jo.put("Quantity",pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_UNIT)));
                                jo.put("ItemValue", pendingInvoiceCursor.getString(pendingInvoiceCursor.getColumnIndex(db.KEY_PRICE)));
                                jo.put("UoM", App.BOTTLES_UOM);
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
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.InvoiceRequestFunction, "", ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber));
                                    object.setDeepEntity(deepEntity);
                                    arrayList.add(object);
                                    purchaseNumber = tempPurchaseNumber;
                                    deepEntity = new JSONArray();
                                }
                                else{
                                    OfflinePost object = new OfflinePost();
                                    object.setCollectionName(App.POST_COLLECTION);
                                    object.setMap(Helpers.buildHeaderMap(ConfigStore.InvoiceRequestFunction, "", ConfigStore.DeliveryDocumentType, customerNumber, "", purchaseNumber));
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
        }

    }
    public class syncData extends AsyncTask<Void,Void,Void> {
        ArrayList<OfflineResponse> data = new ArrayList<>();
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(Void... params) {
            Log.e("Going for Batch Request","(Y)");
            this.data = IntegrationService.batchRequest(getApplicationContext(), App.POST_COLLECTION, arrayList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("REturn data","" + this.data.size());
            try{
                for(OfflineResponse response:this.data){
                    Log.e("Resp Fun","" + response.getFunction());
                    switch (response.getFunction()){
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
                            Log.e("OK","OK");
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
                        case ConfigStore.LoadConfirmationFunction:{

                            break;
                        }
                        case ConfigStore.InvoiceRequestFunction:{
                            break;
                        }
                        case ConfigStore.CustomerDeliveryRequestFunction:{

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
            case ConfigStore.LoadRequestFunction:{
                Cursor loadRequest = db.getData(db.LOAD_REQUEST,map,filter);
                syncCount = loadRequest.getCount();
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
        }

        return syncCount;
    }
    public int getSyncCount(){
        int syncCount = 0;
        HashMap<String,String> map = new HashMap<String, String>();
        map.put(db.KEY_TIME_STAMP, "");

        HashMap<String,String> filter = new HashMap<>();
        filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
        Cursor loadRequest = db.getData(db.LOAD_REQUEST,map,filter);
        Cursor orderRequest = db.getData(db.ORDER_REQUEST,map,filter);
        Cursor invoiceRequest = db.getData(db.CAPTURE_SALES_INVOICE,map,filter);
        Cursor deliveryRequest = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST,map,filter);
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
        Log.e("Sync count","" + syncCount);
        return syncCount;
    }
}
