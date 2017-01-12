package gbc.sa.vansales.activities;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.CustomerDelivery;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.data.LoadDelivery;
import gbc.sa.vansales.data.Messages;
import gbc.sa.vansales.data.TripHeader;
import gbc.sa.vansales.data.VisitList;
import gbc.sa.vansales.models.OfflinePost;
import gbc.sa.vansales.models.OfflineResponse;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.Chain;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public class SettingsActivity extends AppCompatActivity {
    String lang;
    Switch languageSwitch;
    LoadingSpinner loadingSpinner;
    ImageView iv_back;
    TextView tv_top_header;
    ImageView iv_refresh;
    Button btn_sync_data;
    DatabaseHandler db = new DatabaseHandler(this);
    ArrayList<OfflinePost> arrayList = new ArrayList<>();
    LoadingSpinner loadingSpinnerPost;
    LoginActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingSpinner = new LoadingSpinner(this,getString(R.string.changinglanguage));
        loadingSpinnerPost = new LoadingSpinner(this,getString(R.string.posting));
        setContentView(R.layout.activity_settings);
        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);
        iv_refresh=(ImageView) findViewById(R.id.iv_refresh);

        btn_sync_data = (Button)findViewById(R.id.btn_synchronize);
        setSyncCount();
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.settings));
        iv_refresh.setVisibility(View.INVISIBLE);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lang = "";
        try{
            lang = Settings.getString(App.LANGUAGE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        languageSwitch = (Switch)findViewById(R.id.languageButton);
        Log.e("Lang in Settings","" + lang);
        if(lang==null){
            languageSwitch.setChecked(false);
        }
        else if(lang.equals("en")){
            languageSwitch.setChecked(false);
        }
        else if(lang.equals("ar")){
            languageSwitch.setChecked(true);
        }
        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Settings.setString(App.IS_LOGGED_ID,"true");
                    Settings.setString(App.LANGUAGE, "ar");
                    AppController.changeLanguage(getBaseContext(), "ar");
                    Handler handler = new Handler();
                    loadingSpinner.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(loadingSpinner.isShowing()){
                                loadingSpinner.hide();
                            }
                            AppController.restartApp(getBaseContext());
                        }
                    }, 2000);
                } else {
                    Settings.setString(App.IS_LOGGED_ID,"true");
                    Settings.setString(App.LANGUAGE, "en");
                    AppController.changeLanguage(getBaseContext(), "en");
                    Handler handler = new Handler();
                    loadingSpinner.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(loadingSpinner.isShowing()){
                                loadingSpinner.hide();
                            }
                            AppController.restartApp(getBaseContext());
                        }
                    }, 2000);
                }
            }
        });

    }

    public void setSyncCount(){
        int syncCount = 0;
        HashMap<String,String> map = new HashMap<String, String>();
        map.put(db.KEY_TIME_STAMP, "");

        HashMap<String,String> filter = new HashMap<>();
        filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
        Cursor loadRequest = db.getData(db.LOAD_REQUEST,map,filter);
        Cursor orderRequest = db.getData(db.ORDER_REQUEST,map,filter);
        if(loadRequest.getCount()>0){
            syncCount += loadRequest.getCount();
        }
        if(orderRequest.getCount()>0){
            syncCount += orderRequest.getCount();
        }


        btn_sync_data.setText(getString(R.string.synchronize) + "(" + String.valueOf(syncCount) + ")");
    }
    public int getSyncCount(){
        int syncCount = 0;
        HashMap<String,String> map = new HashMap<String, String>();
        map.put(db.KEY_TIME_STAMP, "");

        HashMap<String,String> filter = new HashMap<>();
        filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
        Cursor loadRequest = db.getData(db.LOAD_REQUEST,map,filter);
        Cursor orderRequest = db.getData(db.ORDER_REQUEST,map,filter);
        if(loadRequest.getCount()>0){
            syncCount += loadRequest.getCount();
        }
        if(orderRequest.getCount()>0){
            syncCount += orderRequest.getCount();
        }
        return syncCount;
    }
    public void syncData(View view){
        generateBatch(ConfigStore.LoadRequestFunction);
        generateBatch(ConfigStore.CustomerOrderRequestFunction+"O");
        new syncData().execute();

    }
    public void clearData(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingsActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.data_loss_msg))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.proceed), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Settings.clearPreferenceStore();
                        SettingsActivity.this.deleteDatabase("gbc.db");
                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    public void reloadData(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingsActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.data_loss_msg))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.proceed), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tripID = Settings.getString(App.TRIP_ID);
                        String username = Settings.getString(App.DRIVER);
                        Settings.clearPreferenceStore();
                        SettingsActivity.this.deleteDatabase("gbc.db");
                        Settings.initialize(getApplicationContext());
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        db.getWritableDatabase();
                        downloadData(tripID,username);

                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    public void downloadData(final String tripId, final String username){
        Log.e("Inside chain", "" + tripId);

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_IS_BEGIN_DAY, "false");
        map.put(db.KEY_IS_LOAD_VERIFIED, "false");
        map.put(db.KEY_IS_END_DAY,"false");

        db.addData(db.LOCK_FLAGS, map);


        Chain chain = new Chain(new Chain.Link(){
            @Override
            public void run() {
                go();
            }
        });

        chain.setFail(new Chain.Link() {
            @Override
            public void run() throws Exception {
                fail();
            }
        });

        chain.add(new Chain.Link() {
            @Override
            public void run() {
                TripHeader.load(SettingsActivity.this, tripId, db);
                LoadDelivery.load(SettingsActivity.this, tripId, db);
                ArticleHeaders.load(SettingsActivity.this, tripId, db);
                CustomerHeaders.load(SettingsActivity.this, tripId, db);
                VisitList.load(SettingsActivity.this, tripId, db);
                Messages.load(SettingsActivity.this, username, db);
                CustomerDelivery.load(SettingsActivity.this, tripId, db);
                /*ArticleHeaders.loadData(getApplicationContext());
                CustomerHeaders.loadData(getApplicationContext());*/
            }
        });

        chain.add(new Chain.Link(){
            @Override
            public void run() {
                /*TripHeader.load(LoginActivity.this,tripId, db);
                LoadDelivery.load(LoginActivity.this,tripId, db);
                ArticleHeaders.load(LoginActivity.this, tripId, db);
                CustomerHeaders.load(LoginActivity.this, tripId, db);
                VisitList.load(LoginActivity.this,tripId, db);
                Messages.load(LoginActivity.this,username,db);*/
                ArticleHeaders.loadData(getApplicationContext());
                CustomerHeaders.loadData(getApplicationContext());
            }
        });

        chain.start();

    }

    private void go() {
        loadingSpinner.hide();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void fail() {

        if(loadingSpinner.isShowing()){
            loadingSpinner.hide();
            finish();
        }
    }

    public class syncData extends AsyncTask<Void,Void,Void>{
        ArrayList<OfflineResponse> data = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            loadingSpinnerPost.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            for (OfflinePost offlinePost:arrayList){
                Log.e("Payload Batch","" + offlinePost.getMap());
            }
            this.data = IntegrationService.batchRequest(SettingsActivity.this, App.POST_COLLECTION, arrayList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            try{
                for(OfflineResponse response:this.data){
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
            }
            catch (Exception e){
                e.printStackTrace();
            }

            if(loadingSpinnerPost.isShowing()){
                setSyncCount();
                loadingSpinnerPost.hide();
            }

        }
    }

    private void createBatch(String collectionName,HashMap<String,String> map, JSONArray deepEntity){
        Log.e("Map", "" + map);
        Log.e("Deep Entity", "" + deepEntity);
        OfflinePost object = new OfflinePost();
        object.setCollectionName(collectionName);
        object.setMap(map);
        object.setDeepEntity(deepEntity);
        arrayList.add(object);
        for (OfflinePost offlinePost:arrayList){
            Log.e("Payload Batch","" + offlinePost.getMap());
        }
        for(int i=0;i<arrayList.size();i++){
            OfflinePost obj = arrayList.get(i);
            Log.e("Payload 2","" + obj.getMap());

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

            }
        }

    }

}
