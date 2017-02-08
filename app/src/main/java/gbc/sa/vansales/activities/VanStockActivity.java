package gbc.sa.vansales.activities;
/**
 * Created by Muhammad Umair on 02/12/2016.
 */
import java.util.ArrayList;
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

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.VanStockBadgeAdapter;
import gbc.sa.vansales.models.VanStock;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
public class VanStockActivity extends AppCompatActivity {
    // Declare Variables
    ListView list;
    VanStockBadgeAdapter adapter;
    FloatingActionButton printVanStock;
    ArrayList<VanStock> arraylist = new ArrayList<>();
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
                Dialog dialog = new Dialog(VanStockActivity.this);
                dialog.setContentView(R.layout.dialog_doprint);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(false);
                LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                dialog.show();
                btn_print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
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
                onBackPressed();
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
            vanStock.setItem_case(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY_CASE)));
            vanStock.setItem_units(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY_UNIT)));
            if(vanStock.getItem_case().equals("0.0")&&vanStock.getItem_units().equals("0.0")){

            }
            else{
                arraylist.add(vanStock);
            }

        }
        while (cursor.moveToNext());
    }
}