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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanstock);
        setTitle(getString(R.string.vanstock));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingSpinner = new LoadingSpinner(this);
        new loadItems().execute();
        printVanStock = (FloatingActionButton) findViewById(R.id.fabVanStock);
        printVanStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(VanStockActivity.this);
                dialog.setContentView(R.layout.activity_print);
                dialog.setCancelable(true);
                Button print = (Button) dialog.findViewById(R.id.btnPrint);
                Button cancel = (Button) dialog.findViewById(R.id.btnCancel2);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(VanStockActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(VanStockActivity.this, DashboardActivity.class);
                        startActivity(intent);
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
            map.put(db.KEY_MATERIAL_DESC1,"");
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
            vanStock.setItem_code(cursor.getString(cursor.getColumnIndex(db.KEY_ITEM_NO)));
            vanStock.setItem_description(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
            vanStock.setItem_case(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_CASE)));
            vanStock.setItem_units(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_UNIT)));
            arraylist.add(vanStock);
        }
        while (cursor.moveToNext());
    }
}