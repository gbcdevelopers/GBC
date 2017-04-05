package gbc.sa.vansales.activities;
/**
 * Created by Muhammad Umair on 02/12/2016.
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.crashlytics.android.Crashlytics;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.VanStockBadgeAdapter;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.models.VanStock;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.PrinterHelper;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
public class VanStockActivity extends AppCompatActivity {
    // Declare Variables
    ListView list;
    VanStockBadgeAdapter adapter;
    FloatingActionButton printVanStock;
    ArrayList<VanStock> arraylist = new ArrayList<>();
    ArrayList<VanStock> printListVanStock = new ArrayList<>();
    LoadingSpinner loadingSpinner;
    DatabaseHandler db = new DatabaseHandler(this);
    Button btnPrint;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanstock);
        setTitle(getString(R.string.vanstock));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingSpinner = new LoadingSpinner(this);
        btnPrint = (Button)findViewById(R.id.btnPrint);
        new loadItems().execute();
        printVanStock = (FloatingActionButton) findViewById(R.id.fabVanStock);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(VanStockActivity.this);
                dialog.setContentView(R.layout.dialog_doprint);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(false);
                LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                dialog.show();
                btn_print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        /*JSONArray jsonArray = createPrintData();
                        PrinterHelper object = new PrinterHelper(VanStockActivity.this,VanStockActivity.this);
                        object.execute("", jsonArray);*/
                        //finish();
                    }
                });
                btn_notprint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        });
        adapter = new VanStockBadgeAdapter(this,arraylist);
        list = (ListView) findViewById(R.id.listview);
        list.setAdapter(adapter);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class loadItems extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_ITEM_NO,"");
            map.put(db.KEY_MATERIAL_NO,"");
            map.put(db.KEY_MATERIAL_DESC1,"");
            map.put(db.KEY_ACTUAL_QTY_CASE,"");
            map.put(db.KEY_ACTUAL_QTY_UNIT,"");
            map.put(db.KEY_RESERVED_QTY_CASE,"");
            map.put(db.KEY_RESERVED_QTY_UNIT,"");
            map.put(db.KEY_REMAINING_QTY_CASE,"");
            map.put(db.KEY_REMAINING_QTY_UNIT,"");

            HashMap<String,String> filter = new HashMap<>();

            Cursor cursor = db.getData(db.VAN_STOCK_ITEMS,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                setLoadItems(cursor);
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

    private void setLoadItems(Cursor cursor){
        do{
            VanStock vanStock = new VanStock();
            vanStock.setItem_code(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
            vanStock.setItem_description(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
            String totalcase = String.valueOf(Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_RESERVED_QTY_CASE)))+
            Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_CASE))));
            String totalunit = String.valueOf(Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_RESERVED_QTY_UNIT)))+
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_UNIT))));
            vanStock.setItem_case(totalcase);
            vanStock.setItem_units(totalunit);
            vanStock.setActual_case(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY_CASE)));
            vanStock.setActual_unit(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY_UNIT)));
            if(vanStock.getItem_case().equals("0.0")&&vanStock.getItem_units().equals("0.0")){
                printListVanStock.add(vanStock);
            }
            else if(Double.parseDouble(vanStock.getItem_case())<0||Double.parseDouble(vanStock.getItem_units())<0){

            }
            else{
                arraylist.add(vanStock);
                printListVanStock.add(vanStock);
            }

        }
        while (cursor.moveToNext());
    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }

    public JSONArray createPrintData() {
        JSONArray jArr = new JSONArray();
        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.VAN_STOCK);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), App.PRINT_DATE_FORMAT));
            mainArr.put("TIME", Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER_NAME_EN));
            mainArr.put("SALESMANNO",Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO", "1234");
            mainArr.put("DOCUMENT NO", "80001234");  //Load Summary No
            mainArr.put("TRIP START DATE", Helpers.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("supervisorname", "-");
            mainArr.put("TourID", Settings.getString(App.TRIP_ID));
            mainArr.put("closevalue","100");
            //mainArr.put("Load Number","1");
            JSONArray HEADERS = new JSONArray();
            JSONArray TOTAL = new JSONArray();
            HEADERS.put("ITEM#");
            HEADERS.put("DESCRIPTION");
            HEADERS.put("LOADED QTY");
            HEADERS.put("SALE QTY");
            HEADERS.put("TRUCK STOCK");
            HEADERS.put("TOTAL");
            //HEADERS.put("Description");
            //HEADERS.put(obj1);
            // HEADERS.put(obj2);
            mainArr.put("HEADERS", HEADERS);
            JSONArray jData = new JSONArray();
            double totalPcs = 0;
            double totalSale = 0;
            double totalRemaining = 0;
            for(VanStock obj:printListVanStock){
                JSONArray data = new JSONArray();
                data.put(StringUtils.stripStart(obj.getItem_code(), "0"));
                data.put(UrlBuilder.decodeString(obj.getItem_description()));
                data.put("+" + obj.getActual_case());
                String Sale = String.valueOf(Double.parseDouble(obj.getActual_case()) - Double.parseDouble(obj.getItem_case()));
                data.put("-" + Sale);
                data.put("+" + obj.getItem_case());
                //data.put("شد 48*200مل بيرين PH8");
                data.put("1");
                totalPcs += Double.parseDouble(obj.getActual_case());
                totalSale += (Double.parseDouble(obj.getActual_case())-Double.parseDouble(obj.getItem_case()));
                totalRemaining += Double.parseDouble(obj.getItem_case());
                jData.put(data);
            }
            JSONObject totalObj = new JSONObject();
            totalObj.put("LOADED QTY", "+" + String.valueOf(totalPcs));
            totalObj.put("SALE QTY", "-" + String.valueOf(totalSale));
            totalObj.put("TRUCK STOCK", "+" + String.valueOf(totalRemaining));
            TOTAL.put(totalObj);
            mainArr.put("TOTAL", TOTAL);
            mainArr.put("data", jData);
            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);
            jArr.put(HEADERS);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return jArr;
    }
}