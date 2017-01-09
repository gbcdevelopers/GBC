package gbc.sa.vansales.activities;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.LoadSummaryBadgeAdapter;
import gbc.sa.vansales.adapters.LoadVerifyBadgeAdapter;
import gbc.sa.vansales.models.LoadDeliveryHeader;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
/**
 * Created by Rakshit on 19-Nov-16.
 */
public class LoadVerifyActivity extends AppCompatActivity {
    ArrayList<LoadSummary> loadSummaryList;
    ArrayAdapter<LoadSummary>adapter;
    private ListView listView;
    LoadDeliveryHeader object;
    DatabaseHandler db;
    private static final String TRIP_ID = "ITripId";
    ArrayList<LoadSummary> dataNew;
    ArrayList<LoadSummary> dataOld;
    LoadingSpinner loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_verify);
        db = new DatabaseHandler(LoadVerifyActivity.this);
        loadingSpinner = new LoadingSpinner(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        dataNew = new ArrayList<>();
        dataOld = new ArrayList<>();
        dataNew = intent.getParcelableArrayListExtra("loadSummary");
        dataOld = intent.getParcelableArrayListExtra("loadSummaryOld");
        object = (LoadDeliveryHeader)intent.getParcelableExtra("headerObj");
       // Log.e("****",""+dataOld.size());

        loadSummaryList = new ArrayList<>();
      //  loadSummaryList = dataNew;
        adapter = new LoadVerifyBadgeAdapter(this, loadSummaryList);
        loadSummaryList = generateData(dataNew,dataOld);
        for(int i=0;i<loadSummaryList.size();i++){
            System.out.println(loadSummaryList.get(i).getQuantityCases());
        }

        listView = (ListView)findViewById(R.id.loadSummaryList);
        listView.setAdapter(adapter);

    }

    public void confirmLoad(View v){
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(db.KEY_IS_VERIFIED, "true");

        HashMap<String,String> filters = new HashMap<>();
        filters.put(db.KEY_DELIVERY_NO, object.getDeliveryNo());
        db.updateData(db.LOAD_DELIVERY_HEADER, parameters, filters);

        addItemstoVan(dataNew);

        if(!checkIfLoadExists()){
            if(createDataForPost(dataNew,dataOld)){
                HashMap<String, String> altMap = new HashMap<>();
                altMap.put(db.KEY_IS_LOAD_VERIFIED, "true");
                HashMap<String, String> filter = new HashMap<>();
                filter.put(db.KEY_IS_LOAD_VERIFIED,"false");

                db.updateData(db.LOCK_FLAGS, altMap, filter);
                new postData().execute();
                /*Intent intent = new Intent(LoadVerifyActivity.this,MyCalendarActivity.class);
                startActivity(intent);*/
            }
        }
        else{
            Intent intent = new Intent(LoadVerifyActivity.this,LoadActivity.class);
            startActivity(intent);
        }


    }
    public void cancel(View v){
        Intent intent = new Intent(LoadVerifyActivity.this,LoadSummaryActivity.class);
        intent.putExtra("headerObj", object);
        startActivity(intent);
    }
    private boolean checkIfLoadExists(){

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TRIP_ID, "");
        map.put(db.KEY_DELIVERY_NO, "");
        map.put(db.KEY_DELIVERY_DATE,"");
        map.put(db.KEY_DELIVERY_TYPE,"");

        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TRIP_ID, Settings.getString(TRIP_ID));
        filters.put(db.KEY_IS_VERIFIED, "false");

        Cursor cursor = db.getData(db.LOAD_DELIVERY_HEADER,map,filters);
        if(cursor.getCount()>0){
            return true;
        }
        else{
            return false;
        }

    }
    private boolean checkMaterialExists(String materialno,String uom){

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_MATERIAL_NO, "");
        if(uom.equals(App.CASE_UOM)){
            map.put(db.KEY_UOM_CASE,"");
        }
        else if(uom.equals(App.BOTTLES_UOM)){
            map.put(db.KEY_UOM_UNIT,"");
        }


        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_MATERIAL_NO, materialno);
        if(uom.equals(App.CASE_UOM)){
            filters.put(db.KEY_UOM_CASE, uom);
        }
        else if(uom.equals(App.BOTTLES_UOM)){
            filters.put(db.KEY_UOM_UNIT, uom);
        }



        Cursor cursor = db.getData(db.VAN_STOCK_ITEMS,map,filters);
        Log.e("Cursorcountcheck", "" + materialno + uom + cursor.getCount());
        if(cursor.getCount()>0){
            return true;
        }
        else{
            return false;
        }

    }
    private void addItemstoVan(ArrayList<LoadSummary>dataNew){
        for(int i=0;i<dataNew.size();i++){

            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_ENTRY_TIME, Helpers.getCurrentTimeStamp());
            map.put(db.KEY_DELIVERY_NO,object.getDeliveryNo().toString());
            map.put(db.KEY_MATERIAL_NO, dataNew.get(i).getMaterialNo().toString());
            map.put(db.KEY_ITEM_NO, dataNew.get(i).getItemCode().toString());
            map.put(db.KEY_MATERIAL_DESC1, dataNew.get(i).getItemDescription().toString());

            if(!(dataNew.get(i).getQuantityCases().isEmpty())||(dataNew.get(i).getQuantityCases().equals(""))
              ||(dataNew.get(i).getQuantityCases().equals("0"))||(dataNew.get(i).getQuantityCases().equals(""))){
                //Check if old data exists for case
                if(checkMaterialExists(dataNew.get(i).getMaterialNo().toString(),App.CASE_UOM)){
                    //Logic to read Customer Delivery Item and block material quantity based on UOM
                    int reserved = 0;
                    //Getting old data
                    HashMap<String, String> oldData = new HashMap<>();
                    oldData.put(db.KEY_MATERIAL_NO, "");
                    oldData.put(db.KEY_UOM,"");
                    oldData.put(db.KEY_ACTUAL_QTY_CASE,"");
                    oldData.put(db.KEY_RESERVED_QTY_CASE,"");
                    oldData.put(db.KEY_REMAINING_QTY_CASE,"");

                    HashMap<String, String> filterOldData = new HashMap<>();
                    filterOldData.put(db.KEY_MATERIAL_NO,dataNew.get(i).getMaterialNo().toString());
                    filterOldData.put(db.KEY_MATERIAL_NO,App.CASE_UOM);

                    Cursor cursor = db.getData(db.VAN_STOCK_ITEMS,oldData,filterOldData);
                    if(cursor.getCount()>0){
                        cursor.moveToFirst();
                    }
                    float actualQtyCase = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY_CASE)));
                    float reservedQtyCase = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_RESERVED_QTY_CASE)));
                    float remainingQtyCase = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_CASE)));

                    actualQtyCase+=Float.parseFloat(dataNew.get(i).getQuantityCases().toString());
                    remainingQtyCase+=Float.parseFloat(dataNew.get(i).getQuantityCases().toString());
                    map.put(db.KEY_ACTUAL_QTY_CASE,String.valueOf(actualQtyCase));
                    map.put(db.KEY_RESERVED_QTY_CASE,String.valueOf(reservedQtyCase));
                    map.put(db.KEY_REMAINING_QTY_CASE,String.valueOf(remainingQtyCase));
                    map.put(db.KEY_UOM_CASE, App.CASE_UOM);

                    //db.updateData(db.VAN_STOCK_ITEMS,map,filterOldData);
                }
                else{
                    //Logic to read Customer Delivery Item and block material quantity based on UOM
                    int reservedQty = 0;
                    map.put(db.KEY_ACTUAL_QTY_CASE,dataNew.get(i).getQuantityCases().toString());
                    map.put(db.KEY_RESERVED_QTY_CASE,String.valueOf(reservedQty));
                    map.put(db.KEY_REMAINING_QTY_CASE,String.valueOf(Float.parseFloat(dataNew.get(i).getQuantityCases().toString())-reservedQty));
                    map.put(db.KEY_UOM_CASE, App.CASE_UOM);
                   // db.addData(db.VAN_STOCK_ITEMS, map);
                }

            }
            if(!(dataNew.get(i).getQuantityUnits().isEmpty())||(dataNew.get(i).getQuantityUnits().equals(""))
                    ||(dataNew.get(i).getQuantityUnits().equals("0"))||(dataNew.get(i).getQuantityUnits().equals(""))){

                if(checkMaterialExists(dataNew.get(i).getMaterialNo().toString(),App.BOTTLES_UOM)){

                    //Logic to read Customer Delivery Item and block material quantity based on UOM
                    int reserved = 0;

                    //Getting old data
                    HashMap<String, String> oldData = new HashMap<>();
                    oldData.put(db.KEY_MATERIAL_NO, "");
                    oldData.put(db.KEY_UOM,"");
                    oldData.put(db.KEY_ACTUAL_QTY_UNIT,"");
                    oldData.put(db.KEY_RESERVED_QTY_UNIT,"");
                    oldData.put(db.KEY_REMAINING_QTY_UNIT,"");

                    HashMap<String, String> filterOldData = new HashMap<>();
                    filterOldData.put(db.KEY_MATERIAL_NO,dataNew.get(i).getMaterialNo().toString());
                    filterOldData.put(db.KEY_MATERIAL_NO,App.BOTTLES_UOM);

                    Cursor cursor = db.getData(db.VAN_STOCK_ITEMS,oldData,filterOldData);
                    if(cursor.getCount()>0){
                        cursor.moveToFirst();
                    }
                    float actualQtyUnit = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY_UNIT)));
                    float reservedQtyUnit = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_RESERVED_QTY_UNIT)));
                    float remainingQtyUnit = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_UNIT)));

                    actualQtyUnit+=Float.parseFloat(dataNew.get(i).getQuantityUnits().toString());
                    remainingQtyUnit+=Float.parseFloat(dataNew.get(i).getQuantityUnits().toString());
                    map.put(db.KEY_ACTUAL_QTY_UNIT,String.valueOf(actualQtyUnit));
                    map.put(db.KEY_RESERVED_QTY_UNIT,String.valueOf(reservedQtyUnit));
                    map.put(db.KEY_REMAINING_QTY_UNIT,String.valueOf(remainingQtyUnit));
                    map.put(db.KEY_UOM_UNIT, App.BOTTLES_UOM);

                  //  db.updateData(db.VAN_STOCK_ITEMS, map, filterOldData);
                }
                else{
                    //Logic to read Customer Delivery Item and block material quantity based on UOM
                    int reservedQty = 0;
                    map.put(db.KEY_ACTUAL_QTY_UNIT,dataNew.get(i).getQuantityUnits().toString());
                    map.put(db.KEY_RESERVED_QTY_UNIT,String.valueOf(reservedQty));
                    map.put(db.KEY_REMAINING_QTY_UNIT,String.valueOf(Float.parseFloat(dataNew.get(i).getQuantityUnits().toString())-reservedQty));
                    map.put(db.KEY_UOM_UNIT, App.BOTTLES_UOM);
                }
            }

            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_MATERIAL_NO,dataNew.get(i).getMaterialNo().toString());

            if((checkMaterialExists(dataNew.get(i).getMaterialNo().toString(),App.CASE_UOM))||(checkMaterialExists(dataNew.get(i).getMaterialNo().toString(),App.BOTTLES_UOM))){
                db.updateData(db.VAN_STOCK_ITEMS,map,filter);
            }
            else{
                db.addData(db.VAN_STOCK_ITEMS,map);
            }
        }
    }
    private boolean createDataForPost(ArrayList<LoadSummary>dataNew,ArrayList<LoadSummary>dataOld){
        for(int i=0;i<dataNew.size();i++){


            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_ENTRY_TIME, Helpers.getCurrentTimeStamp());
            map.put(db.KEY_DELIVERY_NO,object.getDeliveryNo().toString());
            map.put(db.KEY_MATERIAL_NO, dataNew.get(i).getMaterialNo().toString());
            map.put(db.KEY_ITEM_NO, dataNew.get(i).getItemCode().toString());
            map.put(db.KEY_MATERIAL_DESC1, dataNew.get(i).getItemDescription().toString());
            if(dataNew.get(i).getItemCode().equals(dataOld.get(i).getItemCode()))
            {
                // Log.e("I m here","here")
                map.put(db.KEY_ORG_CASE,dataOld.get(i).getQuantityCases());
                map.put(db.KEY_VAR_CASE, dataNew.get(i).getQuantityCases());
                map.put(db.KEY_ORG_UNITS,dataOld.get(i).getQuantityUnits());
                map.put(db.KEY_VAR_UNITS, dataNew.get(i).getQuantityUnits());
            }
            db.addData(db.LOAD_DELIVERY_ITEMS_POST,map);
        }
        return true;
    }
    public ArrayList<LoadSummary> generateData(ArrayList<LoadSummary>dataNew,ArrayList<LoadSummary>dataOld){

            for(int i=0;i<dataNew.size();i++){
                LoadSummary loadSummary = new LoadSummary();
                loadSummary.setItemCode(dataNew.get(i).getItemCode());
                loadSummary.setItemDescription(dataNew.get(i).getItemDescription());
                if(dataNew.get(i).getItemCode().equals(dataOld.get(i).getItemCode()))
                {
                   // Log.e("I m here","here");
                    loadSummary.setQuantityCases(dataOld.get(i).getQuantityCases()+"|"+dataNew.get(i).getQuantityCases());
                }
                if(dataNew.get(i).getItemCode().equals(dataOld.get(i).getItemCode())) {
                    loadSummary.setQuantityUnits(dataOld.get(i).getQuantityUnits() + "|" + dataNew.get(i).getQuantityUnits());
                }
                loadSummaryList.add(loadSummary);
            }


        adapter.notifyDataSetChanged();
        return loadSummaryList;
    }

    private  String postData(){
        String orderID = "";
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("Function", ConfigStore.LoadConfirmationFunction);
           // map.put("OrderId", "0000000022");  //For testing purpose
            map.put("OrderId", object.getDeliveryNo());
            map.put("CustomerId", Settings.getString(App.DRIVER));
            JSONArray deepEntity = new JSONArray();
            JSONObject obj = new JSONObject();
            deepEntity.put(obj);
            orderID = IntegrationService.postData(LoadVerifyActivity.this, App.POST_COLLECTION, map, deepEntity);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return orderID;
    }

    public class postData extends AsyncTask<Void, Void, Void>{
        String orderID = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            this.orderID = postData();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }

            if(this.orderID.contains("Error")){
                Toast.makeText(getApplicationContext(), this.orderID.replaceAll("Error","").trim(), Toast.LENGTH_SHORT).show();
            }
            else if(this.orderID!=null&&!(this.orderID.equalsIgnoreCase(""))){
                new updateStockforCustomer().execute();
               /* Intent intent = new Intent(LoadVerifyActivity.this,MyCalendarActivity.class);
                startActivity(intent);*/
            }
            else{
                new updateStockforCustomer().execute();
            }
        }
    }

    public class updateStockforCustomer extends AsyncTask<Void,Void,Void>{
        LoadingSpinner updateSpinner = new LoadingSpinner(LoadVerifyActivity.this,getString(R.string.updatingdata));
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            updateSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            calculateStock();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(updateSpinner.isShowing()){
                updateSpinner.hide();
            }
            Intent intent = new Intent(LoadVerifyActivity.this,MyCalendarActivity.class);
            startActivity(intent);
        }
    }

    void calculateStock(){


        HashMap<String,String> map = new HashMap<>();
        map.put(db.KEY_DELIVERY_NO,"");
        map.put(db.KEY_ITEM_NO,"");
        map.put(db.KEY_ITEM_CATEGORY,"");
        map.put(db.KEY_CREATED_BY,"");
        map.put(db.KEY_ENTRY_TIME,"");
        map.put(db.KEY_DATE ,"");
        map.put(db.KEY_MATERIAL_NO ,"");
        map.put(db.KEY_MATERIAL_ENTERED ,"");
        map.put(db.KEY_MATERIAL_GROUP ,"");
        map.put(db.KEY_PLANT ,"");
        map.put(db.KEY_STORAGE_LOCATION ,"");
        map.put(db.KEY_BATCH ,"");
        map.put(db.KEY_ACTUAL_QTY,"");
        map.put(db.KEY_REMAINING_QTY,"");
        map.put(db.KEY_UOM,"");
        map.put(db.KEY_DIST_CHANNEL ,"");
        map.put(db.KEY_DIVISION ,"");
        map.put(db.KEY_IS_DELIVERED,"");
        HashMap<String,String> filter = new HashMap<>();
        Cursor deliveryCursor = db.getData(db.CUSTOMER_DELIVERY_ITEMS,map,filter);
        do{

            HashMap<String,String>mapVanStock = new HashMap<>();
            mapVanStock.put(db.KEY_DELIVERY_NO,"");
            mapVanStock.put(db.KEY_ITEM_NO,"");
            mapVanStock.put(db.KEY_ITEM_CATEGORY,"");
            mapVanStock.put(db.KEY_CREATED_BY,"");
            mapVanStock.put(db.KEY_ENTRY_TIME,"");
            mapVanStock.put(db.KEY_DATE ,"");
            mapVanStock.put(db.KEY_MATERIAL_NO ,"");
            mapVanStock.put(db.KEY_MATERIAL_DESC1 ,"");
            mapVanStock.put(db.KEY_MATERIAL_ENTERED ,"");
            mapVanStock.put(db.KEY_MATERIAL_GROUP ,"");
            mapVanStock.put(db.KEY_PLANT ,"");
            mapVanStock.put(db.KEY_STORAGE_LOCATION ,"");
            mapVanStock.put(db.KEY_BATCH ,"");
            mapVanStock.put(db.KEY_ACTUAL_QTY_CASE,"");
            mapVanStock.put(db.KEY_ACTUAL_QTY_UNIT,"");
            mapVanStock.put(db.KEY_RESERVED_QTY_CASE,"");
            mapVanStock.put(db.KEY_RESERVED_QTY_UNIT,"");
            mapVanStock.put(db.KEY_REMAINING_QTY_CASE,"");
            mapVanStock.put(db.KEY_REMAINING_QTY_UNIT,"");
            mapVanStock.put(db.KEY_UOM_CASE,"");
            mapVanStock.put(db.KEY_UOM_UNIT,"");
            mapVanStock.put(db.KEY_DIST_CHANNEL ,"");
            mapVanStock.put(db.KEY_DIVISION ,"");
            mapVanStock.put(db.KEY_IS_VERIFIED,"");
            HashMap<String,String> filterVanStock = new HashMap<>();
            Cursor vanStockCursor = db.getData(db.VAN_STOCK_ITEMS,mapVanStock,filterVanStock);

            do{
               if(vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_MATERIAL_NO)).equals(deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_MATERIAL_NO)))){
                    if(deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM_NEW)){
                        String deliveryCase = deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_ACTUAL_QTY));
                        String actualCase = vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_ACTUAL_QTY_CASE));
                        String reservedCase = vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_RESERVED_QTY_CASE));
                        String remainingCase = vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_REMAINING_QTY_CASE));

                        HashMap<String,String> updateMap = new HashMap<>();
                        updateMap.put(db.KEY_ACTUAL_QTY_CASE,actualCase);
                        updateMap.put(db.KEY_RESERVED_QTY_CASE,String.valueOf(Float.parseFloat(reservedCase)+Float.parseFloat(deliveryCase)));
                        updateMap.put(db.KEY_REMAINING_QTY_CASE,String.valueOf(Float.parseFloat(remainingCase) - Float.parseFloat(deliveryCase)));

                        HashMap<String,String>updateFilter = new HashMap<>();
                        updateFilter.put(db.KEY_MATERIAL_NO,vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        Log.e("Update Map C/S","" + updateMap);
                        db.updateData(db.VAN_STOCK_ITEMS,updateMap,updateFilter);
                    }
                    else if(deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                        String deliveryUnits = deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_ACTUAL_QTY));
                        String actualUnits = vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_ACTUAL_QTY_UNIT));
                        String reservedUnits = vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_RESERVED_QTY_UNIT));
                        String remainingUnits = vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_REMAINING_QTY_UNIT));

                        HashMap<String,String> updateMap = new HashMap<>();
                        updateMap.put(db.KEY_ACTUAL_QTY_UNIT,actualUnits);
                        updateMap.put(db.KEY_RESERVED_QTY_UNIT,String.valueOf(Float.parseFloat(reservedUnits)+Float.parseFloat(deliveryUnits)));
                        updateMap.put(db.KEY_REMAINING_QTY_UNIT,String.valueOf(Float.parseFloat(remainingUnits)-Float.parseFloat(deliveryUnits)));

                        Log.e("Update Map B/T", "" + updateMap);
                        HashMap<String,String>updateFilter = new HashMap<>();
                        updateFilter.put(db.KEY_MATERIAL_NO,vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_MATERIAL_NO)));

                        db.updateData(db.VAN_STOCK_ITEMS,updateMap,updateFilter);
                    }
                    break;
                }
            }
            while (vanStockCursor.moveToNext());
        }
        while (deliveryCursor.moveToNext());
    }
}
