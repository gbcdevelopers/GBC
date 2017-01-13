package gbc.sa.vansales.activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.LoadRequestBadgeAdapter;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Muhammad Umair on 02/12/2016.
 */
public class LoadRequestActivity extends AppCompatActivity {
    Button processLoadRequest;
    ListView list;
    LoadRequestBadgeAdapter adapter;
    EditText editsearch;
    ImageButton datepickerdialogbutton;
    TextView selecteddate;
    ArrayList<LoadRequest> arraylist = new ArrayList<>();
    DatabaseHandler db = new DatabaseHandler(this);
    public ArrayList<ArticleHeader> articles;
    int orderTotalValue = 0;
    LoadingSpinner loadingSpinner;
    boolean isPutOnHold;
    boolean putOnHoldValueChanged = false;
    CheckBox putOnHold;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_request);
        setTitle(getString(R.string.loadrequest));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        articles = ArticleHeaders.get();
        loadingSpinner = new LoadingSpinner(LoadRequestActivity.this);
        putOnHold = (CheckBox)findViewById(R.id.putOnHold);

        HashMap<String,String> filter = new HashMap<>();
        filter.put(db.KEY_IS_POSTED,App.DATA_PUT_ON_HOLD);

        if(db.checkData(db.LOAD_REQUEST,filter)){
            putOnHold.setChecked(true);
            isPutOnHold = true;
            new loadItems(true);
        }
        else{
            isPutOnHold = false;
            putOnHold.setChecked(false);
            new loadItems(false);
        }

        datepickerdialogbutton = (ImageButton) findViewById(R.id.btnDate);
        selecteddate = (TextView) findViewById(R.id.tv1);
        processLoadRequest = (Button) findViewById(R.id.btnProcess);

        putOnHold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                putOnHoldValueChanged = true;
                if(isChecked){
                    isPutOnHold = true;
                    Toast.makeText(getApplicationContext(),String.valueOf(isPutOnHold),Toast.LENGTH_SHORT).show();
                }
                else{
                    isPutOnHold = false;
                    Toast.makeText(getApplicationContext(),String.valueOf(isPutOnHold),Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Generate sample data
        datepickerdialogbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // setContentView(R.layout.activity_load_request);
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getFragmentManager(), "Please Select Your Date");
            }
        });
        processLoadRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*new postData().execute();*/
                String purchaseNum = isPutOnHold?"":Helpers.generateNumber(db,ConfigStore.LoadRequest_PR_Type);
                if(putOnHoldValueChanged){
                    if(isPutOnHold){

                    }
                    else{

                    }
                }
                else{
                    for(LoadRequest loadRequest:arraylist){
                        try{
                            if(loadRequest.getCases().equals("")||loadRequest.getCases().isEmpty()||loadRequest.getCases()==null){
                                loadRequest.setCases("0");
                            }
                            if(loadRequest.getUnits().equals("")||loadRequest.getUnits().isEmpty()||loadRequest.getUnits()==null){
                                loadRequest.setUnits("0");
                            }
                            HashMap<String,String> map = new HashMap<String, String>();
                            map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_CUSTOMER_NO, Settings.getString(App.DRIVER));
                            map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            map.put(db.KEY_ITEM_NO,loadRequest.getItemCode());
                            map.put(db.KEY_MATERIAL_DESC1,loadRequest.getItemName());
                            map.put(db.KEY_MATERIAL_NO,loadRequest.getMaterialNo());
                            map.put(db.KEY_MATERIAL_GROUP,loadRequest.getItemCategory());
                            map.put(db.KEY_CASE,loadRequest.getCases());
                            map.put(db.KEY_UNIT,loadRequest.getUnits());
                            map.put(db.KEY_UOM,loadRequest.getUom());
                            map.put(db.KEY_PRICE,loadRequest.getPrice());
                            map.put(db.KEY_IS_POSTED,isPutOnHold?App.DATA_PUT_ON_HOLD:App.DATA_NOT_POSTED);
                            map.put(db.KEY_IS_PRINTED, "");
                            map.put(db.KEY_ORDER_ID,purchaseNum);
                            map.put(db.KEY_PURCHASE_NUMBER,purchaseNum);
                            orderTotalValue = orderTotalValue + Integer.parseInt(loadRequest.getPrice());
                            if(Integer.parseInt(loadRequest.getCases())>0 || Integer.parseInt(loadRequest.getUnits())>0){
                                db.addData(db.LOAD_REQUEST,map);
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                setTitle("Print Activity");
                final Dialog dialog = new Dialog(LoadRequestActivity.this);
                dialog.setContentView(R.layout.dialog_doprint);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                btn_print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isPutOnHold){
                            new postData().execute();
                        }

                        dialog.dismiss();
                        finish();
                    }
                });
                btn_notprint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }

                });
                dialog.setCancelable(false);
                if(!isPutOnHold){
                    dialog.show();
                }
                else{
                    finish();
                }
            }
        });
        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);
        adapter = new LoadRequestBadgeAdapter(this, arraylist);
        list.setAdapter(adapter);
        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);
        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.getFilter().filter(text);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    public String postData(){
        String orderID = "";
        String purchaseNumber = "";
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("Function", ConfigStore.LoadRequestFunction);
            map.put("OrderId", "");
            map.put("DocumentType", ConfigStore.DocumentType);
           // map.put("DocumentDate", Helpers.formatDate(new Date(),App.DATE_FORMAT_WO_SPACE));
           // map.put("DocumentDate", null);
           /* map.put("PurchaseNum", Helpers.generateNumber(db,ConfigStore.LoadRequest_PR_Type));
            purchaseNumber = map.get("PurchaseNum");*/
            map.put("CustomerId", Settings.getString(App.DRIVER));
            map.put("SalesOrg", Settings.getString(App.SALES_ORG));
            map.put("DistChannel", Settings.getString(App.DIST_CHANNEL));
            map.put("Division", Settings.getString(App.DIVISION));
            map.put("OrderValue", String.valueOf(orderTotalValue));
            map.put("Currency", "SAR");

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
            filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);

            Cursor cursor = db.getData(db.LOAD_REQUEST,itemMap,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                map.put("PurchaseNum", cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID)));
                purchaseNumber = map.get("PurchaseNum");
                int itemno = 10;
                do{
                    if(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)){
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                        jo.put("Material",cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description",cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant","");
                        jo.put("Quantity",cursor.getString(cursor.getColumnIndex(db.KEY_CASE)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("UoM", App.CASE_UOM);
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("Storagelocation", "");
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno+10;
                        deepEntity.put(jo);
                    }
                    if(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                        jo.put("Material",cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description",cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant","");
                        jo.put("Quantity",cursor.getString(cursor.getColumnIndex(db.KEY_UNIT)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("UoM", App.BOTTLES_UOM);
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("Storagelocation", "");
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno+10;
                        deepEntity.put(jo);
                    }
                }
                while (cursor.moveToNext());
            }
            orderID = IntegrationService.postData(LoadRequestActivity.this, App.POST_COLLECTION, map, deepEntity);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return orderID + "," + purchaseNumber;

    }
    public static class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day);
            return datepickerdialog;
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView textview = (TextView) getActivity().findViewById(R.id.tv1);
            textview.setText(day + "/" + (month + 1) + "/" + year);
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public class loadItems extends AsyncTask<Void,Void,Void>{

        boolean putOnHoldExists = false;
        private loadItems(boolean putOnHoldExists) {
            this.putOnHoldExists = putOnHoldExists;
            execute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_TRIP_ID,"");
            map.put(db.KEY_MATERIAL_GROUPA_DESC,"");
            map.put(db.KEY_MATERIAL_GROUPB_DESC,"");
            map.put(db.KEY_MATERIAL_DESC2,"");
            map.put(db.KEY_BATCH_MANAGEMENT,"");
            map.put(db.KEY_PRODUCT_HIERARCHY,"");
            map.put(db.KEY_VOLUME_UOM,"");
            map.put(db.KEY_VOLUME,"");
            map.put(db.KEY_WEIGHT_UOM,"");
            map.put(db.KEY_NET_WEIGHT,"");
            map.put(db.KEY_GROSS_WEIGHT,"");
            map.put(db.KEY_ARTICLE_CATEGORY,"");
            map.put(db.KEY_ARTICLE_NO,"");
            map.put(db.KEY_BASE_UOM,"");
            map.put(db.KEY_MATERIAL_GROUP,"");
            map.put(db.KEY_MATERIAL_TYPE,"");
            map.put(db.KEY_MATERIAL_DESC1,"");
            map.put(db.KEY_MATERIAL_NO,"");

            HashMap<String,String> filter = new HashMap<>();
            Cursor cursor = db.getData(db.ARTICLE_HEADER,map,filter);

            if(cursor.getCount()>0){
                cursor.moveToFirst();
                setLoadItems(cursor,this.putOnHoldExists);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void setLoadItems(Cursor loadItemsCursor,boolean putOnHoldExists){

        Cursor cursor = loadItemsCursor;

        do{
            LoadRequest loadRequest = new LoadRequest();
            loadRequest.setItemCode(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
            loadRequest.setItemName(UrlBuilder.decodeString(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
           // loadRequest.setCases(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.CASE_UOM) ? "0" : "0");
           // loadRequest.setUnits(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.BOTTLES_UOM) ? "0" : "0");
            loadRequest.setUom(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)));
            if(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.CASE_UOM)||cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.CASE_UOM_NEW)){
                loadRequest.setIsCaseEnabled(true);
                loadRequest.setIsUnitEnabled(false);
            }
            else if(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.BOTTLES_UOM)||cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.BOTTLES_UOM)){
                loadRequest.setIsCaseEnabled(false);
                loadRequest.setIsUnitEnabled(true);
            }
            else{
                loadRequest.setIsCaseEnabled(true);
                loadRequest.setIsUnitEnabled(true);
            }
            loadRequest.setMaterialNo(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
            arraylist.add(loadRequest);

        }
        while (cursor.moveToNext());

        if(putOnHoldExists){
            for(int i=0;i<arraylist.size();i++){
                LoadRequest loadRequest = arraylist.get(i);
                HashMap<String,String> map = new HashMap<>();
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
                HashMap<String,String> filter = new HashMap<>();
                filter.put(db.KEY_MATERIAL_NO,loadRequest.getMaterialNo());
                filter.put(db.KEY_IS_POSTED, App.DATA_PUT_ON_HOLD);
                Cursor holdCursor = db.getData(db.LOAD_REQUEST,map,filter);
                Log.e("HOLD Cursor", "" + holdCursor.getCount());
                if(holdCursor.getCount()>0){
                    holdCursor.moveToFirst();
                    do {
                        if(holdCursor.getString(holdCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||holdCursor.getString(holdCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM_NEW)){
                            loadRequest.setCases(holdCursor.getString(holdCursor.getColumnIndex(db.KEY_CASE)));
                        }
                        if(holdCursor.getString(holdCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                            loadRequest.setUnits(holdCursor.getString(holdCursor.getColumnIndex(db.KEY_UNIT)));
                        }
                    }
                    while (holdCursor.moveToNext());
                    arraylist.remove(i);
                    arraylist.add(i, loadRequest);
                }

            }
        }

      //  adapter.notifyDataSetChanged();
    }

    public class postData extends AsyncTask<Void, Void, Void>{
        private ArrayList<String>returnList;
        private String orderID = "";
        private String[] tokens = new String[2];
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            //this.returnList = IntegrationService.RequestToken(LoadRequestActivity.this);
            this.orderID = postData();
            this.tokens = orderID.split(",");

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

            Log.e("Order ID", "" + this.orderID);
            if(this.tokens[0].toString().equals(this.tokens[1].toString())){
                for(LoadRequest loadRequest:arraylist){
                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    map.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                    map.put(db.KEY_ORDER_ID,tokens[0].toString());

                    HashMap<String,String> filter = new HashMap<>();
                    filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    filter.put(db.KEY_MATERIAL_NO,loadRequest.getMaterialNo());
                   // filter.put(db.KEY_ORDER_ID,tokens[1].toString());
                    filter.put(db.KEY_PURCHASE_NUMBER,tokens[1].toString());
                    db.updateData(db.LOAD_REQUEST, map, filter);
                }
                if(loadingSpinner.isShowing()){
                    loadingSpinner.hide();
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoadRequestActivity.this);
                alertDialogBuilder.setTitle("Message")
                        .setMessage("Request with reference " + tokens[1].toString() + " has been saved")
                        //.setMessage("Request with reference " + tokens[0].toString() + " has been saved")
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
            else if(!tokens[0].toString().equals("")){
                for(LoadRequest loadRequest:arraylist){
                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                    map.put(db.KEY_ORDER_ID,tokens[0].toString());

                    HashMap<String,String> filter = new HashMap<>();
                    filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    filter.put(db.KEY_MATERIAL_NO,loadRequest.getMaterialNo());
                    //filter.put(db.KEY_ORDER_ID,tokens[1].toString());
                    filter.put(db.KEY_PURCHASE_NUMBER,tokens[1].toString());
                    db.updateData(db.LOAD_REQUEST, map, filter);
                }
                if(loadingSpinner.isShowing()){
                    loadingSpinner.hide();
                }
                if(this.orderID.isEmpty()||this.orderID.equals("")||this.orderID==null){
                    Toast.makeText(getApplicationContext(),getString(R.string.request_timeout),Toast.LENGTH_SHORT ).show();
                }
                else if(this.orderID.contains("Error")){
                    Toast.makeText(getApplicationContext(), this.orderID.replaceAll("Error","").trim(), Toast.LENGTH_SHORT).show();
                }
                else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoadRequestActivity.this);
                    alertDialogBuilder.setTitle("Message")
                            .setMessage("Request " + tokens[1].toString() + " has been created")
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), this.orderID.replaceAll("Error","").trim(), Toast.LENGTH_SHORT).show();
            }


        }
    }
}
