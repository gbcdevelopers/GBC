package gbc.sa.vansales.activities;
/**
 * Created by Muhammad Umair on 05/12/2016.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.models.Unload;
import gbc.sa.vansales.models.UnloadSummaryPrint;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.PrinterHelper;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
public class UnloadActivity extends AppCompatActivity {
    GridView gridView;
    Button processUnloadInventory;
    Button btn_bad_return;
    Button btn_bad_return_variance;
    Button btn_fresh_unload;
    Button btn_ending_inventory;
    Button btn_inventory_variance;
    Button btn_truck_damage;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    int referenceCount = 0;
    int postCount = 0;
    private ArrayList<Unload>arrayList = new ArrayList<>();
    public ArrayList<ArticleHeader> articles;
    private ArrayList<Unload> dataStoreList = new ArrayList<>();
    private ArrayList<UnloadSummaryPrint> printUnloadList = new ArrayList<>();
    boolean isPrint = false;

    static final String[] badReturnitems = new String[]{
            "Bad Return", "Truck Damage", "Fresh Unload", "Ending Inventory", "INV. Variance", "Bad RTN. Variance"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unload);
        setTitle(getString(R.string.unload_inventory));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        articles = ArticleHeaders.get();
        loadingSpinner = new LoadingSpinner(this);
        btn_bad_return = (Button)findViewById(R.id.btn_bad_return);
        btn_bad_return_variance = (Button)findViewById(R.id.btn_bad_return_variance);
        btn_fresh_unload = (Button)findViewById(R.id.btn_fresh_unload);
        btn_ending_inventory = (Button)findViewById(R.id.btn_ending_inventory);
        btn_inventory_variance = (Button)findViewById(R.id.btn_inventory_variance);
        btn_truck_damage = (Button)findViewById(R.id.btn_truck_damage);
        new loadDataforPrint().execute();
        btn_bad_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(UnloadActivity.this,UnloadDetailActivity.class);
                Intent intent = new Intent(UnloadActivity.this,UnloadActivityBadReturnList.class);
                intent.putExtra("context","badreturn");
                startActivity(intent);
            }
        });

        btn_bad_return_variance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UnloadActivity.this,UnloadDetailActivity.class);
                intent.putExtra("context","badreturnvariance");
                startActivity(intent);
            }
        });

        btn_fresh_unload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UnloadActivity.this,UnloadDetailActivity.class);
                intent.putExtra("context","freshunload");
                startActivity(intent);
            }
        });

        btn_ending_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UnloadActivity.this,UnloadDetailActivity.class);
                intent.putExtra("context","endinginventory");
                startActivity(intent);
            }
        });

        btn_inventory_variance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UnloadActivity.this,UnloadDetailActivity.class);
                intent.putExtra("context","inventoryvariance");
                startActivity(intent);
            }
        });

        btn_truck_damage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UnloadActivity.this,UnloadDetailActivity.class);
                intent.putExtra("context","truckdamage");
                startActivity(intent);
            }
        });

       // gridView = (GridView) findViewById(R.id.gridView1);
        processUnloadInventory = (Button) findViewById(R.id.btnUnloadInventory);
        processUnloadInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UnloadActivity.this);
                alertDialogBuilder.setTitle(getString(R.string.message))
                        .setMessage(getString(R.string.unload_msg))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                final Dialog pd = new Dialog(UnloadActivity.this);
                                pd.setContentView(R.layout.dialog_doprint);
                                pd.setCancelable(false);
                                pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                LinearLayout btn_print = (LinearLayout) pd.findViewById(R.id.ll_print);
                                LinearLayout btn_notprint = (LinearLayout) pd.findViewById(R.id.ll_notprint);
                                pd.show();
                                btn_print.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //new loadDataforPrint().execute();
                                        if (unloadVarianceExist(App.ENDING_INVENTORY)||unloadVarianceExist(App.THEFT) || unloadVarianceExist(App.TRUCK_DAMAGE)||unloadVarianceExist(App.EXCESS)) {
                                            isPrint = true;
                                            new postDataNew().execute();
                                        } else {
                                            clearVanStock();   //For development purpose.
                                            HashMap<String, String> altMap = new HashMap<>();
                                            altMap.put(db.KEY_IS_UNLOAD, "true");
                                            HashMap<String, String> filterMap = new HashMap<>();
                                            filterMap.put(db.KEY_IS_UNLOAD, "false");
                                            db.updateData(db.LOCK_FLAGS, altMap, filterMap);
                                            pd.dismiss();
                                            Intent intent = new Intent(UnloadActivity.this, DashboardActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                                btn_notprint.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (unloadVarianceExist(App.ENDING_INVENTORY)||unloadVarianceExist(App.THEFT) || unloadVarianceExist(App.TRUCK_DAMAGE)||unloadVarianceExist(App.EXCESS)) {
                                            new postDataNew().execute();
                                        } else {
                                            clearVanStock(); //for testing purpose
                                            HashMap<String, String> altMap = new HashMap<>();
                                            altMap.put(db.KEY_IS_UNLOAD, "true");
                                            HashMap<String, String> filterMap = new HashMap<>();
                                            filterMap.put(db.KEY_IS_UNLOAD, "false");
                                            db.updateData(db.LOCK_FLAGS, altMap, filterMap);
                                            pd.dismiss();
                                            Intent intent = new Intent(UnloadActivity.this, DashboardActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });



                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();

            }
        });
        /*processUnloadInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(UnloadActivity.this);
                dialog.setContentView(R.layout.dialog_doprint);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                dialog.show();
                btn_print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Variances are recorded(Ending Inventory/Theft or Missing/Truck Damage
                        try {
                            if (unloadVarianceExist("")) {
                                //Checking any record exist for ZDRX
                                if (unloadVarianceExist(App.THEFT) || unloadVarianceExist(App.TRUCK_DAMAGE)) {
                                    //Checking does it exist for both
                                    if (unloadVarianceExist(App.THEFT) && unloadVarianceExist(App.TRUCK_DAMAGE)) {
                                        referenceCount++;
                                        new postData(App.THEFT, App.TRUCK_DAMAGE);
                                    }
                                    //Check if it exists only for Theft
                                    else if (unloadVarianceExist(App.THEFT)) {
                                        referenceCount++;
                                        new postData(App.THEFT);
                                    }
                                    //It only exist for Truck Damage
                                    else {
                                        referenceCount++;
                                        new postData(App.TRUCK_DAMAGE);
                                    }
                                }
                                //Checking if  Any excess product exist
                                if (unloadVarianceExist(App.EXCESS)) {
                                    referenceCount++;
                                    new postData(App.EXCESS);
                                }
                                //Check if any ending inventory is present
                                if (unloadVarianceExist(App.ENDING_INVENTORY)) {
                                    referenceCount++;
                                    new postData(App.ENDING_INVENTORY);
                                }
                                //Finally unloading remainder quantity
                            } else {
                                referenceCount++;
                                new postData(App.FRESHUNLOAD);
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                btn_notprint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (unloadVarianceExist("")) {
                                //Checking any record exist for ZDRX
                                if (unloadVarianceExist(App.THEFT) || unloadVarianceExist(App.TRUCK_DAMAGE)) {
                                    //Checking does it exist for both
                                    if (unloadVarianceExist(App.THEFT) && unloadVarianceExist(App.TRUCK_DAMAGE)) {
                                        referenceCount++;
                                        new postData(App.THEFT, App.TRUCK_DAMAGE);
                                    }
                                    //Check if it exists only for Theft
                                    else if (unloadVarianceExist(App.THEFT)) {
                                        referenceCount++;
                                        new postData(App.THEFT);
                                    }
                                    //It only exist for Truck Damage
                                    else {
                                        referenceCount++;
                                        new postData(App.TRUCK_DAMAGE);
                                    }
                                }
                                //Checking if  Any excess product exist
                                if (unloadVarianceExist(App.EXCESS)) {
                                    referenceCount++;
                                    new postData(App.EXCESS);
                                }
                                //Check if any ending inventory is present
                                if (unloadVarianceExist(App.ENDING_INVENTORY)) {
                                    referenceCount++;
                                    new postData(App.ENDING_INVENTORY);
                                }
                                //Finally unloading remainder quantity
                            } else {
                                referenceCount++;
                                new postData(App.FRESHUNLOAD);
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });*/
    }
    private void navigation() {
        Intent i = new Intent(UnloadActivity.this, ManageInventory.class);
        startActivity(i);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //  navigation();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public class postDataNew extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String>map = new HashMap<>();
            map.put(db.KEY_TIME_STAMP,"");
            map.put(db.KEY_VARIANCE_TYPE,"");
            map.put(db.KEY_TRIP_ID,"");
            map.put(db.KEY_ITEM_NO,"");
            map.put(db.KEY_MATERIAL_DESC1,"");
            map.put(db.KEY_MATERIAL_NO,"");
            map.put(db.KEY_IS_POSTED,"");
            HashMap<String,String>filter = new HashMap<>();
            filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
            Cursor c = db.getData(db.UNLOAD_VARIANCE,map,filter);
            if(c.getCount()>0){
                c.moveToFirst();
                setUnloadData(c);
            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            //clearVanStock();
            Log.e("I am here", "Here");
            if(Helpers.isNetworkAvailable(getApplicationContext())){
                Helpers.createBackgroundJob(getApplicationContext());
            }
            HashMap<String, String> altMap = new HashMap<>();
            altMap.put(db.KEY_IS_UNLOAD, "true");
            HashMap<String, String> filterMap = new HashMap<>();
            filterMap.put(db.KEY_IS_UNLOAD, "false");
            db.updateData(db.LOCK_FLAGS, altMap, filterMap);

            String purchaseNumber = Helpers.generateNumber(db,ConfigStore.Unload_PR_Type);
            HashMap<String,String> logMap = new HashMap<>();
            logMap.put(db.KEY_CUSTOMER_NO,Settings.getString(App.DRIVER));
            logMap.put(db.KEY_ORDER_ID,purchaseNumber);
            logMap.put(db.KEY_PURCHASE_NUMBER,purchaseNumber);
            logMap.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
            logMap.put(db.KEY_IS_PRINTED, App.DATA_MARKED_FOR_POST);
            db.addData(db.UNLOAD_TRANSACTION,logMap);

            if(isPrint){
                try{
                    JSONArray jsonArray = createPrintData();

                    JSONObject data = new JSONObject();
                    data.put("data",(JSONArray)jsonArray);

                    HashMap<String,String>map = new HashMap<>();
                    map.put(db.KEY_CUSTOMER_NO,Settings.getString(App.DRIVER));
                    map.put(db.KEY_ORDER_ID,purchaseNumber);
                    map.put(db.KEY_DOC_TYPE,ConfigStore.UnloadRequest_TR);
                    map.put(db.KEY_DATA,data.toString());
                    db.addDataPrint(db.DELAY_PRINT,map);

                    PrinterHelper object = new PrinterHelper(UnloadActivity.this,UnloadActivity.this);
                    object.execute("", jsonArray);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                /*Intent intent = new Intent(LoadVerifyActivity.this, MyCalendarActivity.class);
                startActivity(intent);*/
            }
            else{
                try{
                    JSONArray jsonArray = createPrintData();

                    JSONObject data = new JSONObject();
                    data.put("data",(JSONArray)jsonArray);

                    HashMap<String,String>map = new HashMap<>();
                    map.put(db.KEY_CUSTOMER_NO,Settings.getString(App.DRIVER));
                    map.put(db.KEY_ORDER_ID,purchaseNumber);
                    map.put(db.KEY_DOC_TYPE,ConfigStore.UnloadRequest_TR);
                    map.put(db.KEY_DATA,data.toString());
                    db.addDataPrint(db.DELAY_PRINT, map);

                    Intent intent = new Intent(UnloadActivity.this,DashboardActivity.class);
                    startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }


        }
    }
    public void setUnloadData(Cursor c){
        try{
            Cursor cursor = c;
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                do{
                    HashMap<String,String>map = new HashMap<>();
                    map.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                    HashMap<String,String>filter = new HashMap<>();
                    filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    db.updateData(db.UNLOAD_VARIANCE,map,filter);
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public class postData extends AsyncTask<String,String,String>{

        private String param1;
        private String param2;
        private String orderID;
        private String[] tokens = new String[2];
        private postData(String param1,String param2){
            this.param1 = param1;
            this.param2 = param2;
            execute();
        }

        private postData(String param1){
            this.param1 = param1;
            this.param2 = "";
            execute();
        }

        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected String doInBackground(String... params) {
            if(param2.equals("")){
                this.orderID = postData(this.param1,"");
            }
            else{
                this.orderID = postData(this.param1,this.param2);
            }
            this.tokens = orderID.split(",");
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            postCount++;
            Log.e("Refernce Count", "" + postCount + referenceCount);
            if(postCount==referenceCount){
                if(loadingSpinner.isShowing()){
                    loadingSpinner.hide();
                }
                if(this.tokens[0].toString().equals(this.tokens[1].toString())){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UnloadActivity.this);
                    alertDialogBuilder.setTitle("Message")
                            //.setMessage("Request with reference " + tokens[0].toString() + " has been saved")
                            .setMessage(getString(R.string.request_created))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    clearVanStock(); //This is for testing. Uncomment it later
                                    dialog.dismiss();
                                    HashMap<String, String> altMap = new HashMap<>();
                                    altMap.put(db.KEY_IS_UNLOAD, "true");
                                    HashMap<String, String> filterMap = new HashMap<>();
                                    filterMap.put(db.KEY_IS_UNLOAD, "false");
                                    db.updateData(db.LOCK_FLAGS, altMap, filterMap);
                                    Intent intent = new Intent(UnloadActivity.this,DashboardActivity.class);
                                    startActivity(intent);
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
                else{
                    if(this.orderID.isEmpty()||this.orderID.equals("")||this.orderID==null){
                        Toast.makeText(getApplicationContext(),getString(R.string.request_timeout),Toast.LENGTH_SHORT ).show();
                    }
                    else if(this.orderID.contains("Error")){
                        Toast.makeText(getApplicationContext(), this.orderID.replaceAll("Error","").trim(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UnloadActivity.this);
                        alertDialogBuilder.setTitle("Message")
                                .setMessage("Request " + tokens[1].toString() + " has been created")
                                        // .setMessage("Request " + tokens[0].toString() + " has been created")
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /*dialog.dismiss();
                                        finish();*/
                                        clearVanStock();

                                        HashMap<String, String> altMap = new HashMap<>();
                                        altMap.put(db.KEY_IS_UNLOAD, "true");
                                        HashMap<String, String> filterMap = new HashMap<>();
                                        filterMap.put(db.KEY_IS_UNLOAD, "false");
                                        db.updateData(db.LOCK_FLAGS, altMap, filterMap);

                                        dialog.dismiss();
                                        Intent intent = new Intent(UnloadActivity.this,DashboardActivity.class);
                                        startActivity(intent);
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                    }
                }
            }
        }
    }
    public String getDocumentType(String param){
        String docType = null;
        switch (param){
            case App.THEFT:
                docType = ConfigStore.TheftorTruckDocumentType;
                break;
            case App.TRUCK_DAMAGE:
                docType = ConfigStore.TheftorTruckDocumentType;
                break;
            case App.EXCESS:
                docType = ConfigStore.ExcessDocumentType;
                break;
            case App.ENDING_INVENTORY:
                docType = ConfigStore.EndingInventory;
                break;
            case App.FRESHUNLOAD:
                docType = ConfigStore.FreshUnload;
                break;
            default:
                break;
        }
        return docType;
    }
    private void fetchTruckTheftData(){
        for(int i=0;i<articles.size();i++){
            HashMap<String, String> itemMap = new HashMap<>();
            itemMap.put(db.KEY_ITEM_NO,"");
            itemMap.put(db.KEY_MATERIAL_DESC1,"");
            itemMap.put(db.KEY_MATERIAL_NO,"");
            itemMap.put(db.KEY_MATERIAL_GROUP,"");
            itemMap.put(db.KEY_CASE,"");
            itemMap.put(db.KEY_UNIT,"");
            itemMap.put(db.KEY_UOM,"");
            itemMap.put(db.KEY_PRICE,"");
            itemMap.put(db.KEY_ORDER_ID,"");
            itemMap.put(db.KEY_PURCHASE_NUMBER,"");
            itemMap.put(db.KEY_IS_POSTED,"");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            filter.put(db.KEY_VARIANCE_TYPE,App.TRUCK_DAMAGE);
            filter.put(db.KEY_MATERIAL_NO,articles.get(i).getMaterialNo());
            HashMap<String, String> filter2 = new HashMap<>();
            filter2.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            filter2.put(db.KEY_VARIANCE_TYPE,App.THEFT);
            filter2.put(db.KEY_MATERIAL_NO,articles.get(i).getMaterialNo());


            Cursor c1 = db.getData(db.UNLOAD_VARIANCE,itemMap,filter);
            Cursor c2 = db.getData(db.UNLOAD_VARIANCE,itemMap,filter2);
            if(c1.getCount()>0 && c2.getCount()>0){
                c1.moveToFirst();
                c2.moveToFirst();
                Unload unload = new Unload();
                unload.setItem_code(c1.getString(c1.getColumnIndex(db.KEY_ITEM_NO)));
                unload.setName(c1.getString(c1.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                unload.setMaterial_no(c1.getString(c1.getColumnIndex(db.KEY_MATERIAL_NO)));

                float cases = Float.parseFloat(c1.getString(c1.getColumnIndex(db.KEY_CASE))) + Float.parseFloat(c2.getString(c2.getColumnIndex(db.KEY_CASE)));
                float units = Float.parseFloat(c1.getString(c1.getColumnIndex(db.KEY_UNIT))) + Float.parseFloat(c2.getString(c2.getColumnIndex(db.KEY_UNIT)));

                ArticleHeader articleHeader = ArticleHeader.getArticle(articles,articles.get(i).getMaterialNo());
                unload.setCases(String.valueOf(cases));
                unload.setPic(String.valueOf(units));
                unload.setUom(articleHeader.getBaseUOM());
                unload.setPrice(c1.getString(c1.getColumnIndex(db.KEY_PRICE)));
                arrayList.add(unload);

            }
            else if(c1.getCount()>0){
                c1.moveToFirst();
                Unload unload = new Unload();
                unload.setItem_code(c1.getString(c1.getColumnIndex(db.KEY_ITEM_NO)));
                unload.setName(c1.getString(c1.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                unload.setMaterial_no(c1.getString(c1.getColumnIndex(db.KEY_MATERIAL_NO)));

                ArticleHeader articleHeader = ArticleHeader.getArticle(articles,articles.get(i).getMaterialNo());
                unload.setCases(c1.getString(c1.getColumnIndex(db.KEY_CASE)));
                unload.setPic(c1.getString(c1.getColumnIndex(db.KEY_UNIT)));
                unload.setUom(articleHeader.getBaseUOM());
                unload.setPrice(c1.getString(c1.getColumnIndex(db.KEY_PRICE)));
                arrayList.add(unload);
            }
            else if(c2.getCount()>0) {
                c2.moveToFirst();
                Unload unload = new Unload();
                unload.setItem_code(c2.getString(c2.getColumnIndex(db.KEY_ITEM_NO)));
                unload.setName(c2.getString(c2.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                unload.setMaterial_no(c2.getString(c2.getColumnIndex(db.KEY_MATERIAL_NO)));

                ArticleHeader articleHeader = ArticleHeader.getArticle(articles,articles.get(i).getMaterialNo());
                unload.setCases(c2.getString(c2.getColumnIndex(db.KEY_CASE)));
                unload.setPic(c2.getString(c2.getColumnIndex(db.KEY_UNIT)));
                unload.setUom(articleHeader.getBaseUOM());
                unload.setPrice(c2.getString(c2.getColumnIndex(db.KEY_PRICE)));
                arrayList.add(unload);
            }
        }
    }
    public String postData(final String param1, final String param2){
        //There is no post for truck and theft
        final String[] orderID = {""};
        final String[] purchaseNumber = {""};

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    if(!param2.equals("")){
                        try{
                            fetchTruckTheftData();
                            HashMap<String, String> map = new HashMap<>();
                            map.put("Function", ConfigStore.ReturnsFunction);
                            map.put("OrderId", "");
                            map.put("DocumentType",getDocumentType(param1));
                            // map.put("DocumentDate", Helpers.formatDate(new Date(),App.DATE_FORMAT_WO_SPACE));
                            // map.put("DocumentDate", null);
                            map.put("CustomerId", Settings.getString(App.DRIVER));
                            map.put("SalesOrg", Settings.getString(App.SALES_ORG));
                            map.put("DistChannel", Settings.getString(App.DIST_CHANNEL));
                            map.put("Division", Settings.getString(App.DIVISION));
                            map.put("OrderValue", "00");
                            map.put("Currency", "SAR");
                            purchaseNumber[0] = Helpers.generateNumber(db,ConfigStore.TheftorTruck_PR_Type);
                            JSONArray deepEntity = new JSONArray();

                            int itemno = 10;
                            for(Unload unload:arrayList){
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
                                    jo.put("Quantity", unload.getPic());
                                    jo.put("ItemValue", unload.getPrice());
                                    jo.put("UoM", unload.getUom());
                                    jo.put("Value", unload.getPrice());
                                    jo.put("Storagelocation", App.STORAGE_LOCATION);
                                    jo.put("Route", Settings.getString(App.ROUTE));
                                    itemno = itemno + 10;
                                    deepEntity.put(jo);
                                }

                            }
                            orderID[0] = IntegrationService.postDataBackup(UnloadActivity.this, App.POST_COLLECTION, map, deepEntity);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    //This means there is no post for both truck and theft
                    else{
                        try{
                            HashMap<String, String> map = new HashMap<>();
                            map.put("Function", ConfigStore.UnloadFunction);
                            map.put("OrderId", "");
                            map.put("DocumentType",getDocumentType(param1));
                            // map.put("DocumentDate", Helpers.formatDate(new Date(),App.DATE_FORMAT_WO_SPACE));
                            // map.put("DocumentDate", null);
                            map.put("CustomerId", Settings.getString(App.DRIVER));
                            map.put("SalesOrg", Settings.getString(App.SALES_ORG));
                            map.put("DistChannel", Settings.getString(App.DIST_CHANNEL));
                            map.put("Division", Settings.getString(App.DIVISION));
                            map.put("OrderValue", "00");
                            map.put("Currency", "SAR");

                            if(param1.equals(App.THEFT)||param1.equals(App.THEFT)){
                                purchaseNumber[0] = Helpers.generateNumber(db,ConfigStore.TheftorTruck_PR_Type);
                            }
                            else if(param1.equals(App.EXCESS)){
                                purchaseNumber[0] = Helpers.generateNumber(db,ConfigStore.Excess_PR_Type);
                            }
                            else if(param1.equals(App.ENDING_INVENTORY)){
                                purchaseNumber[0] = Helpers.generateNumber(db,ConfigStore.EndingInventory_PR_Type);
                            }
                            else if(param1.equals(App.FRESHUNLOAD)){
                                purchaseNumber[0] = Helpers.generateNumber(db,ConfigStore.FreshUnload_PR_Type);
                            }
                            map.put("PurchaseNum", purchaseNumber[0]);
                            JSONArray deepEntity = new JSONArray();
                            //Apart from Fresh unload read everything from Load Variance Table
                            if(!param1.equals(App.FRESHUNLOAD)){
                                HashMap<String, String> itemMap = new HashMap<>();
                                itemMap.put(db.KEY_ITEM_NO,"");
                                itemMap.put(db.KEY_MATERIAL_DESC1,"");
                                itemMap.put(db.KEY_MATERIAL_NO,"");
                                itemMap.put(db.KEY_MATERIAL_GROUP,"");
                                itemMap.put(db.KEY_CASE,"");
                                itemMap.put(db.KEY_UNIT,"");
                                itemMap.put(db.KEY_UOM,"");
                                itemMap.put(db.KEY_PRICE,"");
                                itemMap.put(db.KEY_ORDER_ID,"");
                                itemMap.put(db.KEY_PURCHASE_NUMBER,"");
                                itemMap.put(db.KEY_IS_POSTED,"");
                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                                filter.put(db.KEY_VARIANCE_TYPE,param1);
                                Cursor cursor = db.getData(db.UNLOAD_VARIANCE,itemMap,filter);
                                if(cursor.getCount()>0){
                                    cursor.moveToFirst();
                                    int itemno = 10;
                                    do{
                                        ArticleHeader articleHeader = ArticleHeader.getArticle(articles,cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));

                                        if (articleHeader.getBaseUOM().equals(App.CASE_UOM)||articleHeader.getBaseUOM().equals(App.BOTTLES_UOM)) {
                                            JSONObject jo = new JSONObject();
                                            jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                                            jo.put("Material", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                            jo.put("Description", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                                            jo.put("Plant", App.PLANT);
                                            jo.put("Quantity", param1.equals(App.EXCESS) ? String.valueOf(Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_CASE)))*-1) : cursor.getString(cursor.getColumnIndex(db.KEY_CASE)));
                                            jo.put("ItemValue", "0");
                                            jo.put("UoM", articleHeader.getBaseUOM());
                                            jo.put("Value", "0");
                                            jo.put("Storagelocation", App.STORAGE_LOCATION);
                                            jo.put("Route", Settings.getString(App.ROUTE));
                                            itemno = itemno + 10;
                                            deepEntity.put(jo);
                                        }
                                        else {
                                            JSONObject jo = new JSONObject();
                                            jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                                            jo.put("Material", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                            jo.put("Description", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                                            jo.put("Plant", App.PLANT);
                                            jo.put("Quantity", cursor.getString(cursor.getColumnIndex(db.KEY_UNIT)));
                                            jo.put("ItemValue", "0");
                                            jo.put("UoM", articleHeader.getBaseUOM());
                                            jo.put("Value", "0");
                                            jo.put("Storagelocation", App.STORAGE_LOCATION);
                                            jo.put("Route", Settings.getString(App.ROUTE));
                                            itemno = itemno + 10;
                                            deepEntity.put(jo);
                                        }
                                    }
                                    while (cursor.moveToNext());

                                }
                                orderID[0] = IntegrationService.postDataBackup(UnloadActivity.this, App.POST_COLLECTION, map, deepEntity);
                            }
                            else if(param1.equals(App.FRESHUNLOAD)){
                                HashMap<String,String>itemMap = new HashMap<>();
                                itemMap.put(db.KEY_ITEM_NO,"");
                                itemMap.put(db.KEY_ITEM_CATEGORY,"");
                                itemMap.put(db.KEY_CREATED_BY,"");
                                itemMap.put(db.KEY_ENTRY_TIME,"");
                                itemMap.put(db.KEY_DATE,"");
                                itemMap.put(db.KEY_MATERIAL_NO,"");
                                itemMap.put(db.KEY_MATERIAL_DESC1,"");
                                itemMap.put(db.KEY_MATERIAL_ENTERED,"");
                                itemMap.put(db.KEY_MATERIAL_GROUP,"");
                                itemMap.put(db.KEY_PLANT,"");
                                itemMap.put(db.KEY_STORAGE_LOCATION,"");
                                itemMap.put(db.KEY_BATCH,"");
                                itemMap.put(db.KEY_ACTUAL_QTY_CASE,"");
                                itemMap.put(db.KEY_ACTUAL_QTY_UNIT,"");
                                itemMap.put(db.KEY_RESERVED_QTY_CASE,"");
                                itemMap.put(db.KEY_RESERVED_QTY_UNIT,"");
                                itemMap.put(db.KEY_REMAINING_QTY_CASE,"");
                                itemMap.put(db.KEY_REMAINING_QTY_UNIT,"");
                                itemMap.put(db.KEY_UOM_CASE,"");
                                itemMap.put(db.KEY_UOM_UNIT,"");
                                itemMap.put(db.KEY_DIST_CHANNEL,"");
                                HashMap<String,String>filter=new HashMap<>();
                                Cursor c = db.getData(db.VAN_STOCK_ITEMS,itemMap,filter);
                                int itemno = 10;
                                if(c.getCount()>0){
                                    c.moveToFirst();
                                    do{
                                        Unload unload = new Unload();
                                        unload.setName(UrlBuilder.decodeString(c.getString(c.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                                        unload.setItem_code(c.getString(c.getColumnIndex(db.KEY_ITEM_NO)));
                                        unload.setMaterial_no(c.getString(c.getColumnIndex(db.KEY_MATERIAL_NO)));
                                        String uomCase = c.getString(c.getColumnIndex(db.KEY_UOM_CASE));
                                        String uomUnit = c.getString(c.getColumnIndex(db.KEY_UOM_UNIT));
                                        unload.setUom((uomCase == null || uomCase.equals("")) ? uomUnit : uomCase);
                                        unload.setCases(c.getString(c.getColumnIndex(db.KEY_REMAINING_QTY_CASE)));
                                        unload.setPic(c.getString(c.getColumnIndex(db.KEY_REMAINING_QTY_UNIT)));
                                        dataStoreList.add(unload);

                                    }
                                    while (c.moveToNext());
                                    recalculateFreshUnload(dataStoreList);
                                }

                                for(Unload unload:dataStoreList){

                                    ArticleHeader articleHeader = ArticleHeader.getArticle(articles,unload.getMaterial_no());
                                    if(articleHeader.getBaseUOM().equals(App.CASE_UOM)||articleHeader.getBaseUOM().equals(App.BOTTLES_UOM)){
                                        JSONObject jo = new JSONObject();
                                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                                        jo.put("Material", unload.getMaterial_no());
                                        jo.put("Description", unload.getName());
                                        jo.put("Plant", App.PLANT);
                                        jo.put("Quantity", unload.getCases());
                                        jo.put("ItemValue", unload.getPrice());
                                        jo.put("UoM",articleHeader.getBaseUOM());
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
                                        jo.put("Quantity", unload.getPic());
                                        jo.put("ItemValue", unload.getPrice());
                                        jo.put("UoM", articleHeader.getBaseUOM());
                                        jo.put("Value", unload.getPrice());
                                        jo.put("Storagelocation", App.STORAGE_LOCATION);
                                        jo.put("Route", Settings.getString(App.ROUTE));
                                        itemno = itemno + 10;
                                        deepEntity.put(jo);
                                    }
                                    //Log.e("Got here","" + map + deepEntity);
                                }
                                orderID[0] = IntegrationService.postDataBackup(UnloadActivity.this, App.POST_COLLECTION, map, deepEntity);

                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        //Log.e("Step","Step1" + orderID + purchaseNumber);
                       // return orderID[0].toString() + "," + purchaseNumber[0].toString();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        //Log.e("Step","Step2" + orderID + purchaseNumber);
        return orderID[0].toString() + "," + purchaseNumber[0].toString();
        //return null;
    }
    private boolean unloadVarianceExist(String varianceType){
        HashMap<String,String> map = new HashMap<>();
        map.put(db.KEY_TIME_STAMP,"");
        HashMap<String,String>filter = new HashMap<>();
        //filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
        if(varianceType.equals("")||varianceType==null){

        }
        else{
            filter.put(db.KEY_VARIANCE_TYPE,varianceType);
        }

        if(db.checkData(db.UNLOAD_VARIANCE,filter)){
            return true;
        }
        else{
            return false;
        }
    }
    private void recalculateFreshUnload(ArrayList<Unload>data){
        ArrayList<Unload>vanData = data;
        for(int i=0;i<vanData.size();i++){
            Unload unload = vanData.get(i);
            HashMap<String,String>map = new HashMap<>();
            map.put(db.KEY_CASE,"");
            map.put(db.KEY_UNIT,"");
            HashMap<String,String>checkInventoryFilter = new HashMap<>();
            checkInventoryFilter.put(db.KEY_VARIANCE_TYPE,App.ENDING_INVENTORY);
            checkInventoryFilter.put(db.KEY_MATERIAL_NO,unload.getMaterial_no());
            HashMap<String,String>truckDamageFilter = new HashMap<>();
            truckDamageFilter.put(db.KEY_VARIANCE_TYPE,App.TRUCK_DAMAGE);
            truckDamageFilter.put(db.KEY_MATERIAL_NO,unload.getMaterial_no());
            HashMap<String,String>theftFilter = new HashMap<>();
            theftFilter.put(db.KEY_VARIANCE_TYPE,App.THEFT);
            theftFilter.put(db.KEY_MATERIAL_NO,unload.getMaterial_no());
            HashMap<String,String>excessFilter = new HashMap<>();
            excessFilter.put(db.KEY_VARIANCE_TYPE,App.EXCESS);
            excessFilter.put(db.KEY_MATERIAL_NO,unload.getMaterial_no());
            //Inventory Exists
            float cases = 0;
            float units = 0;
            float excessCases = 0;
            float excessUnits = 0;

            if(db.checkData(db.UNLOAD_VARIANCE,checkInventoryFilter)){
                Cursor c = db.getData(db.UNLOAD_VARIANCE,map,checkInventoryFilter);
                if(c.getCount()>0){
                    c.moveToFirst();

                    do{
                        cases += Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CASE)));
                        units += Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_UNIT)));
                    }
                    while (c.moveToNext());
                }
            }
            if(db.checkData(db.UNLOAD_VARIANCE,truckDamageFilter)){
                Cursor c = db.getData(db.UNLOAD_VARIANCE,map,truckDamageFilter);
                if(c.getCount()>0){
                    c.moveToFirst();

                    do{
                        cases += Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CASE)));
                        units += Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_UNIT)));
                    }
                    while (c.moveToNext());
                }
            }
            if(db.checkData(db.UNLOAD_VARIANCE,theftFilter)){
                Cursor c = db.getData(db.UNLOAD_VARIANCE,map,theftFilter);
                if(c.getCount()>0){
                    c.moveToFirst();

                    do{
                        cases += Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CASE)));
                        units += Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_UNIT)));
                    }
                    while (c.moveToNext());
                }
            }
            if(db.checkData(db.UNLOAD_VARIANCE,excessFilter)){
                Cursor c = db.getData(db.UNLOAD_VARIANCE,map,excessFilter);
                if(c.getCount()>0){
                    c.moveToFirst();

                    do{
                        excessCases += Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CASE)));
                        excessUnits += Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_UNIT)));
                    }
                    while (c.moveToNext());
                }
            }
            float finalCases = Float.parseFloat(unload.getCases())-cases+(excessCases*-1);
            float finalUnits = Float.parseFloat(unload.getPic())-units+(excessUnits*-1);
            unload.setCases(String.valueOf(finalCases));
            unload.setPic(String.valueOf(finalUnits));
            vanData.remove(i);
            vanData.add(i,unload);
        }
        arrayList = vanData;
        //dataStoreList = vanData;
    }
    private void clearVanStock(){
        for(int i=0;i<articles.size();i++){
            HashMap<String,String>map = new HashMap<>();
            map.put(db.KEY_MATERIAL_NO, articles.get(i).getMaterialNo());
            //db.deleteData(db.VAN_STOCK_ITEMS, map);
        }
    }

    public JSONArray createPrintData(){
        JSONArray jArr = new JSONArray();
        try{
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST,App.UNLOAD);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE",Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), App.PRINT_DATE_FORMAT));
            mainArr.put("TIME",Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO","1234");
            mainArr.put("DOCUMENT NO","80001234");  //Load Summary No
            mainArr.put("ORDERNO","80001234");  //Load Summary No
            mainArr.put("TRIP START DATE",Helpers.formatDate(new Date(),"dd-MM-yyyy"));
            mainArr.put("supervisorname","-");
            mainArr.put("TripID",Settings.getString(App.TRIP_ID));
            mainArr.put("invheadermsg","HAPPY NEW YEAR");
            mainArr.put("LANG","en");
            mainArr.put("invoicepaymentterms","2");
            mainArr.put("invoicenumber","1300000001");
            mainArr.put("INVOICETYPE","SALES INVOICE");
            String arabicCustomer = "اللولو هايبر ماركت";
            mainArr.put("CUSTOMER","LULU HYPER MARKET" + "-" + arabicCustomer);
            mainArr.put("ADDRESS","3101, 21st Street, Riyadh");
            mainArr.put("ARBADDRESS","");
            mainArr.put("displayupc","0");
            mainArr.put("invoicepriceprint","1");
            mainArr.put("SUB TOTAL","1000");
            mainArr.put("INVOICE DISCOUNT","20");
            mainArr.put("NET SALES","980");

            mainArr.put("availvalue","+1000");
            mainArr.put("unloadvalue","+2000");

            //mainArr.put("Load Number","1");


            JSONArray HEADERS = new JSONArray();
            JSONArray TOTAL = new JSONArray();

            HEADERS.put("ITEMNO");
            HEADERS.put("DESCRIPTION");
            HEADERS.put("INVENTORY CALCULATED");  //Fresh unload
            HEADERS.put("RETURN TO STOCK");  //Summation of all
            HEADERS.put("TRUCK SPOILS");  //Truck Damage
            HEADERS.put("ACTUAL ON TRUCK");  //Truck Damage
            HEADERS.put("BAD RTRNS");  //Bad Returns
            HEADERS.put("----VARIANCE---- QTY        AMNT");
            HEADERS.put("ENDING INV.VALUE");
            //HEADERS.put("TOTAL VALUE");

            mainArr.put("HEADERS",HEADERS);


            JSONArray jData = new JSONArray();
            double totalEndingInventory = 0;
            double totalfreshUnload = 0;
            double totalTruckDamange = 0;
            double totalVanStock = 0;
            double totalBadReturns = 0;
            double totalVariance = 0;
            double totalVarianceAmount = 0;
            double totalEndingInventoryValue = 0;

            for(UnloadSummaryPrint obj:printUnloadList){
                JSONArray data = new JSONArray();
                data.put(StringUtils.stripStart(obj.getItemNo(), "0"));
                data.put(UrlBuilder.decodeString(obj.getItemDescription()));
                data.put("+" + obj.getEndingInventory());
                totalEndingInventory += Double.parseDouble(obj.getEndingInventory());
                data.put("+" + obj.getFreshUnload());
                totalfreshUnload += Double.parseDouble(obj.getFreshUnload());
                data.put("-" + obj.getTruckDamage());
                totalTruckDamange += Double.parseDouble(obj.getTruckDamage());
                data.put("+" + obj.getVanStock());
                totalVanStock += Double.parseDouble(obj.getVanStock());
                data.put("+" + obj.getBadReturns());
                totalBadReturns += Double.parseDouble(obj.getBadReturns());
                data.put(" " +obj.getVarianceQty() + "        " + String.valueOf(Double.parseDouble(obj.getVarianceQty())*Double.parseDouble(obj.getItemPrice())));
                totalVariance += Double.parseDouble(obj.getVarianceQty());
                totalVarianceAmount += Double.parseDouble(obj.getVarianceQty())*Double.parseDouble(obj.getItemPrice());
                data.put(String.valueOf(Double.parseDouble(obj.getEndingInventory())*Double.parseDouble(obj.getItemPrice())));
                totalEndingInventoryValue += Double.parseDouble(obj.getEndingInventory())*Double.parseDouble(obj.getItemPrice());
                jData.put(data);
            }

            JSONObject totalObj = new JSONObject();
            totalObj.put("INVENTORY CALCULATED",String.valueOf(totalEndingInventory));
            totalObj.put("RETURN TO STOCK",String.valueOf(totalfreshUnload));  //Summation of all
            totalObj.put("TRUCK SPOILS",String.valueOf(totalTruckDamange));  //Truck Damage
            totalObj.put("ACTUAL ON TRUCK",String.valueOf(totalVanStock));  //Truck Damage
            totalObj.put("BAD RTRNS",String.valueOf(totalBadReturns));  //Bad Returns
            totalObj.put("----VARIANCE---- QTY        AMNT"," " + String.valueOf(totalVariance) + "       " + String.valueOf(totalVarianceAmount));
            totalObj.put("ENDING INV.VALUE",String.valueOf(totalEndingInventoryValue));

            mainArr.put("closevalue","+" + String.valueOf(totalEndingInventoryValue));
            /*totalObj.put("TOTAL FRESH UNLOAD","+10");
            totalObj.put("TOTAL TRUCK DAMAGE","+5");
            totalObj.put("TOTAL CLOSING STOCK","+65");
            totalObj.put("TOTAL VARIANCE QTY","+15");
            totalObj.put("Total Value","+500");*/
            TOTAL.put(totalObj);
            mainArr.put("TOTAL",TOTAL);

            /*JSONArray jData1 = new JSONArray();
            jData1.put("14020106");
            jData1.put("Carton 48*200ml Berain Krones");
            //jData1.put("شد 48*200مل بيرين PH8");
            //jData1.put("+1");
            jData1.put("+10");
            jData1.put("+20");
            jData1.put("+30");
            jData1.put("+40");
            jData1.put("+50");
            jData1.put("+60");
            jData1.put("+50 +60");
            //jData1.put("+60");
            jData1.put("+150");

            JSONArray jData2 = new JSONArray();
            jData2.put("14020107");
            jData2.put("Carton 30*330ml Berain Krones");
            //jData2.put("+1");
            jData2.put("+10");
            jData2.put("+20");
            jData2.put("+30");
            jData2.put("+40");
            jData2.put("+50");
            jData2.put("+60");
            jData2.put("+50 +60");
            //jData2.put("+60");
            jData2.put("+150");

            JSONArray jData3 = new JSONArray();
            jData3.put("14020123");
            jData3.put("Carton 24*600 Berain PH8 Krones");
            //jData3.put("+1");
            jData3.put("+10");
            jData3.put("+20");
            jData3.put("+30");
            jData3.put("+40");
            jData3.put("+50");
            jData3.put("+60");
            jData3.put("+50 +60");
            //jData3.put("+60");
            jData3.put("+150");



            jData.put(jData1);
            jData.put(jData2);
            jData.put(jData3);*/

            mainArr.put("data",jData);
            //mainArr.put("data",jData);

            jDict.put("mainArr",mainArr);
            jInter.put(jDict);
            jArr.put(jInter);

            jArr.put(HEADERS);



        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jArr;
    }
    public class loadDataforPrint extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected String doInBackground(String... params) {
            loadData();
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            Log.e("Unload Summary","" + printUnloadList.size());
        }
    }
    private void loadData(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(ArticleHeader articleHeader:articles){
                    double vanCase = 0;
                    double vanQty = 0;
                    double totalVarianceQty = 0;
                    double totalVarianceAmount = 0;
                    double brCase = 0;
                    double brUnits = 0;
                    double eiCase = 0;
                    double eiUnits = 0;
                    double freshUnloadCase = 0;
                    double freshUnloadUnits = 0;
                    double truckDamageCase = 0;
                    double truckDamageUnits = 0;
                    double itemPrice = 0;

                    UnloadSummaryPrint unloadSummaryPrint = new UnloadSummaryPrint();
                    HashMap<String, String> map = new HashMap<>();
                    map.put(db.KEY_ITEM_NO, "");
                    map.put(db.KEY_MATERIAL_NO, "");
                    map.put(db.KEY_RESERVED_QTY_CASE, "");
                    map.put(db.KEY_RESERVED_QTY_UNIT, "");
                    map.put(db.KEY_REMAINING_QTY_CASE, "");
                    map.put(db.KEY_REMAINING_QTY_UNIT, "");
                    map.put(db.KEY_UOM_CASE, "");
                    map.put(db.KEY_UOM_UNIT, "");
                    map.put(db.KEY_DIST_CHANNEL, "");
                    HashMap<String, String> filter = new HashMap<>();
                    filter.put(db.KEY_MATERIAL_NO,articleHeader.getMaterialNo());
                    Cursor c = db.getData(db.VAN_STOCK_ITEMS, map, filter);
                    if(c.getCount()>0){
                        c.moveToFirst();
                        unloadSummaryPrint.setItemNo(articleHeader.getMaterialNo());
                        unloadSummaryPrint.setItemDescription(UrlBuilder.decodeString(articleHeader.getMaterialDesc1()));
                        String cases = String.valueOf(Double.parseDouble(c.getString(c.getColumnIndex(db.KEY_RESERVED_QTY_CASE)))
                                +Double.parseDouble(c.getString(c.getColumnIndex(db.KEY_REMAINING_QTY_CASE))));
                        String units = String.valueOf(Double.parseDouble(c.getString(c.getColumnIndex(db.KEY_RESERVED_QTY_CASE)))
                                +Double.parseDouble(c.getString(c.getColumnIndex(db.KEY_REMAINING_QTY_CASE))));
                        vanCase = Double.parseDouble(cases);
                        unloadSummaryPrint.setVanStock(cases);

                        //Searching for material bad return

                        HashMap<String, String> brMap = new HashMap<>();
                        brMap.put(db.KEY_MATERIAL_NO, "");
                        brMap.put(db.KEY_CASE, "");
                        brMap.put(db.KEY_UNIT, "");
                        HashMap<String, String> brMapFilter = new HashMap<>();
                        brMapFilter.put(db.KEY_REASON_TYPE, App.BAD_RETURN);
                        brMapFilter.put(db.KEY_MATERIAL_NO, articleHeader.getMaterialNo());
                        Cursor brCursor = db.getData(db.RETURNS, brMap, brMapFilter);
                        if(brCursor.getCount()>0){
                            brCursor.moveToFirst();
                            do{
                                brCase += Double.parseDouble(brCursor.getString(brCursor.getColumnIndex(db.KEY_CASE)));
                                brUnits += Double.parseDouble(brCursor.getString(brCursor.getColumnIndex(db.KEY_UNIT)));
                            }
                            while (brCursor.moveToNext());
                        }
                        unloadSummaryPrint.setBadReturns(String.valueOf(brCase));

                        HashMap<String, String> filterPart = new HashMap<>();
                        filterPart.put(db.KEY_MATERIAL_NO, articleHeader.getMaterialNo());
                        HashMap<String, String> priceMap = new HashMap<>();
                        priceMap.put(db.KEY_MATERIAL_NO, "");
                        priceMap.put(db.KEY_AMOUNT, "");
                        Cursor priceCursor = db.getData(db.PRICING,priceMap,filterPart);
                        if(priceCursor.getCount()>0){
                            priceCursor.moveToFirst();
                            do{
                                itemPrice = Double.parseDouble(priceCursor.getString(priceCursor.getColumnIndex(db.KEY_AMOUNT)));
                            }
                            while (priceCursor.moveToNext());
                        }
                        unloadSummaryPrint.setItemPrice(String.valueOf(itemPrice));
                        HashMap<String, String> brVariance = new HashMap<>();
                        brVariance.put(db.KEY_ITEM_NO, "");
                        brVariance.put(db.KEY_MATERIAL_DESC1, "");
                        brVariance.put(db.KEY_MATERIAL_NO, "");
                        brVariance.put(db.KEY_MATERIAL_GROUP, "");
                        brVariance.put(db.KEY_CASE, "");
                        brVariance.put(db.KEY_UNIT, "");
                        brVariance.put(db.KEY_UOM, "");
                        HashMap<String, String> brVarianceFilter = new HashMap<>();
                        brVarianceFilter.put(db.KEY_VARIANCE_TYPE, App.BAD_RETURN_VARIANCE);
                        brVarianceFilter.put(db.KEY_MATERIAL_NO, articleHeader.getMaterialNo());
                        Cursor brVarianceCursor = db.getData(db.UNLOAD_VARIANCE, brVariance, brVarianceFilter);

                        if(brVarianceCursor.getCount()>0){
                            brVarianceCursor.moveToFirst();
                            do{
                                totalVarianceQty += Double.parseDouble(brVarianceCursor.getString(brVarianceCursor.getColumnIndex(db.KEY_CASE)));
                            }
                            while (brVarianceCursor.moveToNext());
                        }

                        HashMap<String, String> theftMap = new HashMap<>();
                        theftMap.put(db.KEY_ITEM_NO, "");
                        theftMap.put(db.KEY_MATERIAL_NO, "");
                        theftMap.put(db.KEY_CASE, "");
                        theftMap.put(db.KEY_UNIT, "");
                        HashMap<String, String> theftMapFilter = new HashMap<>();
                        theftMapFilter.put(db.KEY_VARIANCE_TYPE, App.THEFT);
                        theftMapFilter.put(db.KEY_MATERIAL_NO, articleHeader.getMaterialNo());
                        Cursor theftCursor = db.getData(db.UNLOAD_VARIANCE, theftMap, theftMapFilter);
                        if (theftCursor.getCount() > 0) {
                            theftCursor.moveToFirst();
                            do{
                                totalVarianceQty += Double.parseDouble(theftCursor.getString(theftCursor.getColumnIndex(db.KEY_CASE)));
                            }
                            while (theftCursor.moveToNext());
                        }

                        unloadSummaryPrint.setVarianceQty(String.valueOf(totalVarianceQty));

                        HashMap<String, String> eiMap = new HashMap<>();
                        eiMap.put(db.KEY_CASE, "");
                        eiMap.put(db.KEY_UNIT, "");
                        HashMap<String, String> eiMapFilter = new HashMap<>();
                        eiMapFilter.put(db.KEY_VARIANCE_TYPE, App.ENDING_INVENTORY);
                        eiMapFilter.put(db.KEY_MATERIAL_NO, articleHeader.getMaterialNo());
                        Cursor eiCursor = db.getData(db.UNLOAD_VARIANCE, eiMap, eiMapFilter);
                        if(eiCursor.getCount()>0){
                            eiCursor.moveToFirst();
                            do{
                                eiCase += Double.parseDouble(eiCursor.getString(eiCursor.getColumnIndex(db.KEY_CASE)));
                            }
                            while (eiCursor.moveToNext());
                        }
                        unloadSummaryPrint.setEndingInventory(String.valueOf(eiCase));

                        HashMap<String, String> truckDamageMap = new HashMap<>();
                        truckDamageMap.put(db.KEY_MATERIAL_NO, "");
                        truckDamageMap.put(db.KEY_CASE, "");
                        truckDamageMap.put(db.KEY_UNIT, "");
                        HashMap<String, String> truckDamageFilter = new HashMap<>();
                        truckDamageFilter.put(db.KEY_VARIANCE_TYPE, App.TRUCK_DAMAGE);
                        truckDamageFilter.put(db.KEY_MATERIAL_NO, articleHeader.getMaterialNo());
                        Cursor truckDamageCursor = db.getData(db.UNLOAD_VARIANCE, truckDamageMap, truckDamageFilter);
                        if (truckDamageCursor.getCount() > 0) {
                            truckDamageCursor.moveToFirst();
                            do{
                                truckDamageCase += Double.parseDouble(truckDamageCursor.getString(truckDamageCursor.getColumnIndex(db.KEY_CASE)));
                            }
                            while (truckDamageCursor.moveToNext());
                        }

                        unloadSummaryPrint.setTruckDamage(String.valueOf(truckDamageCase));
                        freshUnloadCase = vanCase - eiCase - truckDamageCase - totalVarianceQty - brCase;
                        unloadSummaryPrint.setFreshUnload(String.valueOf(freshUnloadCase));
                        printUnloadList.add(unloadSummaryPrint);
                    }
                }
            }
        });

    }

    public void callbackFunction(){
        Intent intent = new Intent(UnloadActivity.this,DashboardActivity.class);
        startActivity(intent);
    }
}