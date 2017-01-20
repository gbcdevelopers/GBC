package gbc.sa.vansales.activities;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.utils.DatabaseHandler;
public class ManageInventory extends AppCompatActivity {
    Button load, loadRequest, unload, vanStock;
    DatabaseHandler db = new DatabaseHandler(this);
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_inventery);
        setTitle(R.string.manage_inventory);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        load = (Button) findViewById(R.id.btnLoad);
        loadRequest = (Button) findViewById(R.id.btnLoadRequest);
        vanStock = (Button) findViewById(R.id.btnVanStock);
        unload = (Button) findViewById(R.id.btnUnLoad);
        setButtonVisibility();
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Load Popup", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ManageInventory.this, LoadActivity.class);
                startActivity(i);
            }
        });
        loadRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageInventory.this, LoadRequestActivity.class);
                startActivity(i);
            }
        });
        vanStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), "VanStock Popup", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ManageInventory.this, VanStockActivity.class);
                startActivity(i);
            }
        });
        unload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "Unload Popup", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ManageInventory.this, UnloadActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        setButtonVisibility();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void setButtonVisibility() {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_IS_BEGIN_DAY, "");
        map.put(db.KEY_IS_LOAD_VERIFIED, "");
        map.put(db.KEY_IS_UNLOAD,"");
        map.put(db.KEY_IS_END_DAY, "");
        HashMap<String, String> filter = new HashMap<>();
        Cursor cursor = db.getData(db.LOCK_FLAGS, map, filter);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        boolean isBeginTripEnabled = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(db.KEY_IS_BEGIN_DAY)));
        boolean isloadVerifiedEnabled = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(db.KEY_IS_LOAD_VERIFIED)));
        boolean isEndDayEnabled = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(db.KEY_IS_END_DAY)));
        boolean isUnloadenabled = setUnloadVisibility();
       // boolean isUnloadenabled = true;  //This is only for testing purpose;
        if(isBeginTripEnabled){
            if (!isloadVerifiedEnabled) {
                loadRequest.setEnabled(false);
                loadRequest.setAlpha(.5f);
                vanStock.setEnabled(false);
                vanStock.setAlpha(.5f);
            }
            if(!isUnloadenabled){
                unload.setEnabled(false);
                unload.setAlpha(.5f);
            }
        }

    }
    private boolean setUnloadVisibility(){
        HashMap<String,String> map = new HashMap<>();
        map.put(db.KEY_ORDER_ID,"");
        map.put(db.KEY_PURCHASE_NUMBER,"");
        HashMap<String,String>filter = new HashMap<>();

        Cursor preSaleCount = db.getData(db.ORDER_REQUEST,map,filter);
        Cursor saleCount = db.getData(db.CAPTURE_SALES_INVOICE,map,filter);

        boolean canActivate = preSaleCount.getCount()>0||saleCount.getCount()>0?true:false;
        return canActivate;
    }
}
