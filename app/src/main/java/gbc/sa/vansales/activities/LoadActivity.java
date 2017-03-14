package gbc.sa.vansales.activities;
import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gbc.sa.vansales.adapters.LoadDeliveryHeaderAdapter;
import gbc.sa.vansales.data.Messages;
import gbc.sa.vansales.data.OrderReasons;
import gbc.sa.vansales.models.LoadDeliveryHeader;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/************************************************************
 @ This activity shows all the load for the said driver for the
 @ day. Could be one or multiple. This activity is launched automatically
 @ once begin day is done or manually when the user clicks on Load button
 @ from the manage inventory screen
 ************************************************************/
public class LoadActivity extends AppCompatActivity {
    private static final String TRIP_ID = "ITripId";
    public static Object o;
    public static LoadConstants fullObject;
    ArrayList<LoadDeliveryHeader> loadDeliveryHeaders;
    //LoadDeliveryHeaderAdapter adapter;
    ListView lv;
    DatabaseHandler db = new DatabaseHandler(this);
    private ArrayAdapter<LoadDeliveryHeader> adapter;
    //    TextView status;
    SwipeRefreshLayout refreshLayout;
    LoadingSpinner loadingSpinner;
    boolean fetchloadComplete = false;
    int changeCount = 0;

    private static final String COLLECTION_NAME = "LoadDeliveryHdSet";
    private static final String LOAD_DEL_ITEMS = "LoadDelItems";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        setTitle(getString(R.string.load));
        loadingSpinner = new LoadingSpinner(this);
        try{
            Helpers.logData(LoadActivity.this, "Driver reached the load screen");
            OrderReasons.loadData(getApplicationContext());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    dispatchRefresh();
                }
            });
            loadDeliveryHeaders = new ArrayList<>();
            // searchResults= GetSearchResults();
            adapter = new LoadDeliveryHeaderAdapter(LoadActivity.this, loadDeliveryHeaders);
            lv = (ListView) findViewById(R.id.srListView);
            new fetchLoads(Settings.getString(TRIP_ID));
            lv.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    LoadDeliveryHeader load = loadDeliveryHeaders.get(position);
                    Helpers.logData(LoadActivity.this, "Going to Load Summary Screen for load No" + load.getDeliveryNo());
                    Intent i = new Intent(LoadActivity.this, LoadSummaryActivity.class);
                    i.putExtra("headerObj", load);
                    startActivityForResult(i, 10);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    public void dispatchRefresh() {
        try{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoadActivity.this);
            alertDialogBuilder.setTitle(R.string.message)
                    .setMessage(R.string.reload_msg)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if(Helpers.isNetworkAvailable(LoadActivity.this)){
                                if(fetchloadComplete){
                                    refreshLayout.setRefreshing(true);
                                    if (refreshLayout.isRefreshing()) {
                                        refreshLayout.setRefreshing(false);
                                        new reloadLoads(Settings.getString(App.TRIP_ID));
                                    }
                                }

                            }
                            else{
                                refreshLayout.setRefreshing(false);
                                Toast.makeText(LoadActivity.this,"No internet connection available",Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (refreshLayout.isRefreshing()) {
                                refreshLayout.setRefreshing(false);
                                // adapter.notifyDataSetChanged();
                            }
                            if (loadingSpinner.isShowing()) {
                                loadingSpinner.hide();
                            }
                            dialog.dismiss();
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      //  Toast.makeText(getApplicationContext(), "Load Checked!", Toast.LENGTH_SHORT).show();
        lv.setAdapter(new LoadDeliveryHeaderAdapter(LoadActivity.this, loadDeliveryHeaders));
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backButtonAction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void backButtonAction() {
        Helpers.logData(LoadActivity.this, "Clicked Back on Load Screen.");
        Intent i = new Intent(LoadActivity.this, DashboardActivity.class);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }
    private ArrayList<LoadConstants> GetSearchResults() {
        ArrayList<LoadConstants> results = new ArrayList<LoadConstants>();
        LoadConstants sr = new LoadConstants();
        sr.setName("Load # 1");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);
        sr = new LoadConstants();
        sr.setName("Load # 2");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);
        sr = new LoadConstants();
        sr.setName("Load # 3");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);
        sr = new LoadConstants();
        sr.setName("Load #4");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);
        sr = new LoadConstants();
        sr.setName("Load # 5");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);
        sr = new LoadConstants();
        sr.setName("Load # 6");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);
        sr = new LoadConstants();
        sr.setName("Load 7");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);
        return results;
    }
    public void setLoadDelivery(Cursor loadCursor) {
        try{
            Cursor cursor = loadCursor;
            cursor.moveToFirst();
            //Log.e("cursor", "" + cursor.getCount());
            do {
                LoadDeliveryHeader loadDeliveryHeader = new LoadDeliveryHeader();
                loadDeliveryHeader.setDeliveryNo(cursor.getString(cursor.getColumnIndex(db.KEY_DELIVERY_NO)));
                Helpers.logData(LoadActivity.this, "Loads Delivery No for Driver :" + loadDeliveryHeader.getDeliveryNo());
                loadDeliveryHeader.setLoadingDate(cursor.getString(cursor.getColumnIndex(db.KEY_DELIVERY_DATE)));
                loadDeliveryHeader.setLoadVerified(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(db.KEY_IS_VERIFIED))));
                Helpers.logData(LoadActivity.this, "Loads Verification for Driver :" + loadDeliveryHeader.getDeliveryNo() + loadDeliveryHeader.isLoadVerified());
                //loadDeliveryHeader.setAvailableLoad("1");
                loadDeliveryHeaders.add(loadDeliveryHeader);
            }
            while (cursor.moveToNext());
            //Log.e("loadDeliver", "" + loadDeliveryHeaders.size());
            adapter.notifyDataSetChanged();
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        //Log.e("adapter", "" + adapter.getCount());
    }
    /************************************************************
     @ Fetching loads for the driver
     ************************************************************/
    private class fetchLoads extends AsyncTask<Void, Void, Void> {
        String tripId;
        private fetchLoads(String tripId) {
            this.tripId = tripId;
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try{
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_TRIP_ID, "");
                map.put(db.KEY_DELIVERY_NO, "");
                map.put(db.KEY_DELIVERY_DATE, "");
                map.put(db.KEY_DELIVERY_TYPE, "");
                map.put(db.KEY_IS_VERIFIED, "");
                HashMap<String, String> filters = new HashMap<>();
                filters.put(db.KEY_TRIP_ID, Settings.getString(TRIP_ID));
                // filters.put(db.KEY_IS_VERIFIED,"false");
                Cursor cursor = db.getData(db.LOAD_DELIVERY_HEADER, map, filters);
                if (cursor.getCount() > 0) {
                    Helpers.logData(LoadActivity.this, "Setting loads for driver against trip id:" + this.tripId);
                    Helpers.logData(LoadActivity.this, "Number of loads for driver against trip id:" + this.tripId + cursor.getCount());
                    setLoadDelivery(cursor);
                }
            }
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
            }
            finally {
                db.close();
            }

            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
                loadingSpinner.show();
            }
            else{
                loadingSpinner.show();
            }
            Helpers.logData(LoadActivity.this, "Fetching loads for driver against trip id:" + this.tripId);
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try{
                fetchloadComplete = true;
                if(loadingSpinner.isShowing()){
                    loadingSpinner.hide();
                }
                adapter = new LoadDeliveryHeaderAdapter(LoadActivity.this, loadDeliveryHeaders);
                lv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Helpers.logData(LoadActivity.this, "Completed Fetching loads for driver against trip id:" + this.tripId);
            }
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
            }

        }
    }
    /***********************************************************
     @ Downloading new loads during mid day reload
     ***********************************************************/
    private class reloadLoads extends AsyncTask<Void,Void,Void>{
        String tripId;
        HashMap<String, String> params = new HashMap<>();
        HashMap<String,String>expansion = new HashMap<>();

        private reloadLoads(String tripId) {
            this.tripId = tripId;
            params.put(TRIP_ID, tripId);
            execute();
        }
        @Override
        protected void onPreExecute() {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
                loadingSpinner.show();
            }
            else{
                loadingSpinner.show();
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
            expansion.put(LOAD_DEL_ITEMS,LOAD_DEL_ITEMS);
            String url = UrlBuilder.buildExpansion(COLLECTION_NAME, this.params, this.expansion);
            JSONArray jsonArray = IntegrationService.getService(LoadActivity.this, url);
            Log.e("Exp Response", ""+ jsonArray);

            try {
                Log.e("Metadata", "" + jsonArray.getJSONObject(0).getJSONObject("__metadata").getString("type"));
                Helpers.logData(LoadActivity.this, jsonArray.getJSONObject(0).getJSONObject("__metadata").getString("type"));
                String metadata = jsonArray.getJSONObject(0).getJSONObject("__metadata").getString("type");
                parseJSON(metadata,jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
                Crashlytics.logException(e);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            try{
                if(loadingSpinner.isShowing()){
                    loadingSpinner.hide();
                }
                if(changeCount>0){
                    lockTransactions();
                }
                if(fetchloadComplete){
                    new fetchLoads(Settings.getString(App.TRIP_ID));
                }
            }
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
            }
        }
    }
    void parseJSON(String metadata,JSONArray jsonArray) throws JSONException {
        try{
            switch (metadata){
                case ConfigStore.LoadDeliveryEntity:
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject headerObj = jsonArray.getJSONObject(i);
                        JSONArray loadItems = headerObj.getJSONObject("LoadDelItems").getJSONArray("results");

                        HashMap<String, String> headerParams = new HashMap<>();
                        headerParams.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
                        headerParams.put(db.KEY_DELIVERY_NO  ,headerObj.get("DeliveryNo").toString());
                        headerParams.put(db.KEY_CREATED_BY ,headerObj.get("CreatedBy").toString());
                        headerParams.put(db.KEY_CREATED_TIME,headerObj.get("EntryTime").toString());
                        headerParams.put(db.KEY_SALES_DIST, headerObj.get("SalesDist").toString());
                        headerParams.put(db.KEY_DATE, Helpers.formatDate(Helpers.formatDate(headerObj.get("Creationdate").toString()), App.DATE_FORMAT));
                        //headerParams.put(db.KEY_AS_DATE, Helpers.formatDate(Helpers.formatDate(headerObj.get("Asdate").toString()), App.DATE_FORMAT));
                        headerParams.put(db.KEY_SHIPPING_PT  ,headerObj.get("ShippingPt").toString());
                        headerParams.put(db.KEY_SALES_ORG  ,headerObj.get("SalesOrg").toString());
                        headerParams.put(db.KEY_DELIVERY_TYPE  ,headerObj.get("Delvtype").toString());
                        headerParams.put(db.KEY_DELIVERY_DEFN  ,headerObj.get("DelvDefin").toString());
                        headerParams.put(db.KEY_ORDER_COMB  ,headerObj.get("OrderComb").toString());
                        headerParams.put(db.KEY_GOODS_MOVEMENT_DATE  ,Helpers.formatDate(Helpers.formatDate(headerObj.get("GoodsMvtdate").toString()), App.DATE_FORMAT));
                        headerParams.put(db.KEY_LOADING_DATE   ,Helpers.formatDate(Helpers.formatDate(headerObj.get("LoadingDat").toString()), App.DATE_FORMAT));
                        headerParams.put(db.KEY_TRANSPLANT_DATE  ,Helpers.formatDate(Helpers.formatDate(headerObj.get("TransplDate").toString()), App.DATE_FORMAT));
                        headerParams.put(db.KEY_DELIVERY_DATE  ,Helpers.formatDate(Helpers.formatDate(headerObj.get("DelvDate").toString()), App.DATE_FORMAT));
                        headerParams.put(db.KEY_PICKING_DATE   ,Helpers.formatDate(Helpers.formatDate(headerObj.get("PickingDae").toString()), App.DATE_FORMAT));
                        headerParams.put(db.KEY_UNLOAD_POINT, headerObj.get("UnloadPt").toString());
                        headerParams.put(db.KEY_IS_VERIFIED, "false");

                        HashMap<String,String>filterMap = new HashMap<>();
                        filterMap.put(db.KEY_DELIVERY_NO, headerObj.get("DeliveryNo").toString());
                        if(!db.checkData(db.LOAD_DELIVERY_HEADER,filterMap)){
                            changeCount++;
                            db.addData(db.LOAD_DELIVERY_HEADER, headerParams);
                        }

                        for(int j=0;j<loadItems.length();j++){
                            JSONObject object = loadItems.getJSONObject(j);
                            HashMap<String, String> params = new HashMap<>();
                            params.put(db.KEY_DELIVERY_NO,object.get("DeliveryNo").toString());
                            params.put(db.KEY_ORDER_ID,object.get("OrderId").toString());
                            params.put(db.KEY_ITEM_NO,object.get("Itemno").toString());
                            params.put(db.KEY_ITEM_CATEGORY, object.get("DelvItmCat").toString());
                            params.put(db.KEY_CREATED_BY, object.get("CreatedBy").toString());
                            params.put(db.KEY_ENTRY_TIME  ,object.get("EntryTime").toString());
                            params.put(db.KEY_DATE   ,Helpers.formatDate(Helpers.formatDate(object.get("CreationDat").toString()), App.DATE_FORMAT));
                            params.put(db.KEY_MATERIAL_NO   ,object.get("MaterialNo").toString());
                            params.put(db.KEY_MATERIAL_ENTERED   ,object.get("MaterialEntered").toString());
                            params.put(db.KEY_MATERIAL_GROUP ,object.get("MatGroup").toString());
                            params.put(db.KEY_PLANT ,object.get("Plant").toString());
                            params.put(db.KEY_STORAGE_LOCATION , object.get("StorLocation").toString());
                            params.put(db.KEY_BATCH , object.get("Batch").toString());
                            params.put(db.KEY_ACTUAL_QTY,object.get("ActQtyDel").toString());
                            params.put(db.KEY_REMAINING_QTY,object.get("ActQtyDel").toString());
                            params.put(db.KEY_UOM ,object.get("Uom").toString());
                            params.put(db.KEY_DIST_CHANNEL ,object.get("DistCha").toString());
                            params.put(db.KEY_DIVISION ,object.get("Division").toString());
                            params.put(db.KEY_IS_VERIFIED,"false");

                            HashMap<String,String>filterItem = new HashMap<>();
                            filterItem.put(db.KEY_DELIVERY_NO, object.get("DeliveryNo").toString());
                            if(!db.checkData(db.LOAD_DELIVERY_ITEMS,filterItem)){
                                db.addData(db.LOAD_DELIVERY_ITEMS,params);
                            }
                        }
                    }
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    void lockTransactions(){
        HashMap<String, String> altMap = new HashMap<>();
        altMap.put(db.KEY_IS_BEGIN_DAY, "true");
        altMap.put(db.KEY_IS_LOAD_VERIFIED,"false");
      //  altMap.put(db.KEY_IS_UNLOAD,"false");
        altMap.put(db.KEY_IS_END_DAY,"false");
        HashMap<String, String> filterMap = new HashMap<>();
        //filterMap.put(db.KEY_IS_BEGIN_DAY, "false");
        db.updateData(db.LOCK_FLAGS, altMap, filterMap);
    }
}