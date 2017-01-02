package gbc.sa.vansales.activities;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.LoadSummaryBadgeAdapter;
import gbc.sa.vansales.adapters.LoadVerifyBadgeAdapter;
import gbc.sa.vansales.models.LoadDeliveryHeader;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_verify);
        db = new DatabaseHandler(LoadVerifyActivity.this);

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

     //   addItemstoVan(dataNew);

        if(!checkIfLoadExists()){
            if(createDataForPost(dataNew,dataOld)){
                Intent intent = new Intent(LoadVerifyActivity.this,MyCalendarActivity.class);
                startActivity(intent);
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
        filters.put(db.KEY_IS_VERIFIED,"false");

        Cursor cursor = db.getData(db.LOAD_DELIVERY_HEADER,map,filters);
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

            //Logic to read Customer Delivery Item and block material quantity based on UOM

            db.addData(db.VAN_STOCK_ITEMS,map);
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

}
