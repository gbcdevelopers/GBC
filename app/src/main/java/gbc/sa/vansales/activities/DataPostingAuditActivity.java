package gbc.sa.vansales.activities;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DataPostingAuditAdapter;
import gbc.sa.vansales.adapters.DataPoustingAuditAdapter;
import gbc.sa.vansales.adapters.PrintAdapter;
import gbc.sa.vansales.adapters.SwipeDetector;
import gbc.sa.vansales.models.DataPoustingAudit;
import gbc.sa.vansales.models.Print;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
public class DataPostingAuditActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    View view1;
    public static SwipeDetector swipeDetector;
    CheckBox checkBox;
    ArrayList<Print> arrayList = new ArrayList<>();
    ListView listView;
    DataPostingAuditAdapter adapter;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    boolean isSelectAll = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pousting_audit);
        loadingSpinner = new LoadingSpinner(this);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.data_posting_audit_lbl));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isSelectAll = true;
                    adapter = new DataPostingAuditAdapter(DataPostingAuditActivity.this,arrayList,isSelectAll);
                    listView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                }
                else{
                    isSelectAll = false;
                    adapter = new DataPostingAuditAdapter(DataPostingAuditActivity.this,arrayList,isSelectAll);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                /*adapter = new DataPoustingAuditAdapter(DataPostingAuditActivity.this, arrayList, isChecked);
                listView.setAdapter(adapter);*/
            }
        });
        listView = (ListView) findViewById(R.id.print_document_list);
        adapter = new DataPostingAuditAdapter(this,arrayList,isSelectAll);
        listView.setAdapter(adapter);
        swipeDetector = new SwipeDetector();


        //new loadTransactions().execute();
        new loadDriverTransactions().execute();
    }

    public class loadDriverTransactions extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String>beginDayMap = new HashMap<>();
            beginDayMap.put(db.KEY_PURCHASE_NUMBER,"");
            beginDayMap.put(db.KEY_IS_POSTED,"");

            HashMap<String,String>odoMeterMap = new HashMap<>();
            odoMeterMap.put(db.KEY_PURCHASE_NUMBER,"");
            odoMeterMap.put(db.KEY_IS_POSTED, "");


            HashMap<String,String>lconMap = new HashMap<>();
            lconMap.put(db.KEY_ORDER_ID,"");
            lconMap.put(db.KEY_IS_POSTED,"");

            HashMap<String,String>filter = new HashMap<>();

            HashMap<String,String>bdFilter = new HashMap<>();
            bdFilter.put(db.KEY_FUNCTION,ConfigStore.BeginDayFunction);

            HashMap<String,String>edFilter = new HashMap<>();
            edFilter.put(db.KEY_FUNCTION,ConfigStore.EndDayFunction);

            HashMap<String,String>odometerFilter = new HashMap<>();
            odometerFilter.put(db.KEY_ODOMETER_TYPE,App.ODOMETER_BEGIN_DAY);

            HashMap<String,String>odometerEndFilter = new HashMap<>();
            odometerEndFilter.put(db.KEY_ODOMETER_TYPE,App.ODOMETER_END_DAY);

            Cursor beginDay = db.getData(db.BEGIN_DAY,beginDayMap,bdFilter);
            Cursor endDay = db.getData(db.BEGIN_DAY,beginDayMap,edFilter);
            Cursor odoMeter = db.getData(db.ODOMETER,odoMeterMap,odometerFilter);
            Cursor odoMeterEnd = db.getData(db.ODOMETER,odoMeterMap,odometerEndFilter);
            Cursor loadConfirmation = db.getData(db.LOAD_CONFIRMATION_HEADER,lconMap,filter);
            if(beginDay.getCount()>0){
                beginDay.moveToFirst();
            }
            if(odoMeter.getCount()>0){
                odoMeter.moveToFirst();
            }
            if(loadConfirmation.getCount()>0){
                loadConfirmation.moveToFirst();
            }
            if(endDay.getCount()>0){
                endDay.moveToFirst();
            }
            if(odoMeterEnd.getCount()>0){
                odoMeterEnd.moveToFirst();
            }
            setDriverAuditItems(beginDay, odoMeter, loadConfirmation,endDay,odoMeterEnd);
            //setAuditItems(orderRequest, salesRequest, deliveryRequest,loadRequest);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            new loadTransactions().execute();
        }
    }

    public class loadTransactions extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String>map = new HashMap<>();
            map.put(db.KEY_PURCHASE_NUMBER,"");
            map.put(db.KEY_IS_POSTED,"");
            map.put(db.KEY_CUSTOMER_NO,"");

            HashMap<String,String>filter = new HashMap<>();
            Cursor orderRequest = db.getData(db.ORDER_REQUEST,map,filter);
            Cursor salesRequest = db.getData(db.CAPTURE_SALES_INVOICE,map,filter);
            Cursor deliveryRequest = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST,map,filter);
            Cursor loadRequest = db.getData(db.LOAD_REQUEST,map,filter);
            if(orderRequest.getCount()>0){
                orderRequest.moveToFirst();
            }
            if(salesRequest.getCount()>0){
                salesRequest.moveToFirst();
            }
            if(deliveryRequest.getCount()>0){
                deliveryRequest.moveToFirst();
            }
            setAuditItems(orderRequest, salesRequest, deliveryRequest,loadRequest);

            return null;
        }
    }

    private void setAuditItems(Cursor cursor1,Cursor cursor2,Cursor cursor3,Cursor cursor4){
        Cursor orderRequest = cursor1;
        Cursor salesRequest = cursor2;
        Cursor deliveryRequest = cursor3;
        Cursor loadRequest = cursor4;
        ArrayList<String> temp=new ArrayList<String>();
        temp.clear();
       // arrayList.clear();
        int i= 1;
        if(orderRequest.getCount()>0){
            orderRequest.moveToFirst();
            do{
                Print print = new Print();
                print.setCustomer_id(orderRequest.getString(orderRequest.getColumnIndex(db.KEY_CUSTOMER_NO)));
                print.setReferenceNumber(orderRequest.getString(orderRequest.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                print.setTransactionType(ConfigStore.OrderRequest_TR);
                print.setIsPosted(orderRequest.getString(orderRequest.getColumnIndex(db.KEY_IS_POSTED)).equals(App.DATA_IS_POSTED)?true:false);

                if(!temp.contains(print.getReferenceNumber())){
                    temp.add(print.getReferenceNumber());
                    arrayList.add(print);
                    i++;
                }
                //  arrayList.add(print);

            }
            while (orderRequest.moveToNext());
        }

        if(salesRequest.getCount()>0){
            salesRequest.moveToFirst();
            do{
                Print print = new Print();
                print.setCustomer_id(salesRequest.getString(salesRequest.getColumnIndex(db.KEY_CUSTOMER_NO)));
                print.setReferenceNumber(salesRequest.getString(salesRequest.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                print.setTransactionType(ConfigStore.SalesInvoice_TR);
                print.setIsPosted(salesRequest.getString(salesRequest.getColumnIndex(db.KEY_IS_POSTED)).equals(App.DATA_IS_POSTED) ? true : false);

                if(!temp.contains(print.getReferenceNumber())){
                    temp.add(print.getReferenceNumber());
                    arrayList.add(print);
                    i++;
                }

            }
            while (salesRequest.moveToNext());
        }

        if(deliveryRequest.getCount()>0){
            deliveryRequest.moveToFirst();
            do{
                Print print = new Print();
                print.setCustomer_id(deliveryRequest.getString(deliveryRequest.getColumnIndex(db.KEY_CUSTOMER_NO)));
                print.setReferenceNumber(deliveryRequest.getString(deliveryRequest.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                print.setTransactionType(ConfigStore.DeliveryRequest_TR);
                print.setIsPosted(deliveryRequest.getString(deliveryRequest.getColumnIndex(db.KEY_IS_POSTED)).equals(App.DATA_IS_POSTED) ? true : false);

                if(!temp.contains(print.getReferenceNumber())){
                    temp.add(print.getReferenceNumber());
                    arrayList.add(print);
                    i++;
                }

            }
            while (deliveryRequest.moveToNext());
        }

        if(loadRequest.getCount()>0){
            loadRequest.moveToFirst();
            do{
                Print print = new Print();
              //  print.setCustomer_id(loadRequest.getString(loadRequest.getColumnIndex(db.KEY_CUSTOMER_NO)));
                print.setCustomer_id("-");
                print.setReferenceNumber(loadRequest.getString(loadRequest.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                print.setTransactionType(ConfigStore.LoadRequest_TR);
                print.setIsPosted(loadRequest.getString(loadRequest.getColumnIndex(db.KEY_IS_POSTED)).equals(App.DATA_IS_POSTED) ? true : false);

                if(!temp.contains(print.getReferenceNumber())){
                    temp.add(print.getReferenceNumber());
                    arrayList.add(print);
                    i++;
                }

            }
            while (loadRequest.moveToNext());
        }
    }
    private void setDriverAuditItems(Cursor cursor1, Cursor cursor2, Cursor cursor3,Cursor cursor4,Cursor cursor5){
        Cursor beginDay = cursor1;
        Cursor odometer = cursor2;
        Cursor loadConfirmation = cursor3;
        Cursor endDay = cursor4;
        Cursor odometerEnd = cursor5;

        ArrayList<String> temp=new ArrayList<String>();
        temp.clear();
        arrayList.clear();
        int i= 1;
        if(beginDay.getCount()>0){
            beginDay.moveToFirst();
            do{
                Print print = new Print();
                print.setCustomer_id(Settings.getString(App.DRIVER));
                print.setReferenceNumber(beginDay.getString(beginDay.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                print.setTransactionType(ConfigStore.BeginDayRequest_TR);
                print.setIsPosted(beginDay.getString(beginDay.getColumnIndex(db.KEY_IS_POSTED)).equals(App.DATA_IS_POSTED)?true:false);

                if(!temp.contains(print.getReferenceNumber())){
                    temp.add(print.getReferenceNumber());
                    arrayList.add(print);
                    i++;
                }
                //  arrayList.add(print);

            }
            while (beginDay.moveToNext());
        }

        if(odometer.getCount()>0){
            odometer.moveToFirst();
            do{
                Print print = new Print();
                print.setCustomer_id(Settings.getString(App.DRIVER));
                print.setReferenceNumber(odometer.getString(odometer.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                print.setTransactionType(ConfigStore.BeginDayOdometerRequest_TR);
                print.setIsPosted(odometer.getString(odometer.getColumnIndex(db.KEY_IS_POSTED)).equals(App.DATA_IS_POSTED) ? true : false);

                if(!temp.contains(print.getReferenceNumber())){
                    temp.add(print.getReferenceNumber());
                    arrayList.add(print);
                    i++;
                }

            }
            while (odometer.moveToNext());
        }

        if(loadConfirmation.getCount()>0){
            loadConfirmation.moveToFirst();
            do{
                Print print = new Print();
                print.setCustomer_id(Settings.getString(App.DRIVER));
                print.setReferenceNumber(loadConfirmation.getString(loadConfirmation.getColumnIndex(db.KEY_ORDER_ID)));
                print.setTransactionType(ConfigStore.LoadConfirmation_TR);
                print.setIsPosted(loadConfirmation.getString(loadConfirmation.getColumnIndex(db.KEY_IS_POSTED)).equals(App.DATA_IS_POSTED) ? true : false);

                if(!temp.contains(print.getReferenceNumber())){
                    temp.add(print.getReferenceNumber());
                    arrayList.add(print);
                    i++;
                }

            }
            while (loadConfirmation.moveToNext());
        }

        if(endDay.getCount()>0){
            endDay.moveToFirst();
            do{
                Print print = new Print();
                print.setCustomer_id(Settings.getString(App.DRIVER));
                print.setReferenceNumber(endDay.getString(endDay.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                print.setTransactionType(ConfigStore.EndDayRequest_TR);
                print.setIsPosted(endDay.getString(endDay.getColumnIndex(db.KEY_IS_POSTED)).equals(App.DATA_IS_POSTED)?true:false);

                if(!temp.contains(print.getReferenceNumber())){
                    temp.add(print.getReferenceNumber());
                    arrayList.add(print);
                    i++;
                }
                //  arrayList.add(print);

            }
            while (endDay.moveToNext());
        }

        if(odometerEnd.getCount()>0){
            odometerEnd.moveToFirst();
            do{
                Print print = new Print();
                print.setCustomer_id(Settings.getString(App.DRIVER));
                print.setReferenceNumber(odometerEnd.getString(odometerEnd.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                print.setTransactionType(ConfigStore.EndDayOdometerRequest_TR);
                print.setIsPosted(odometerEnd.getString(odometerEnd.getColumnIndex(db.KEY_IS_POSTED)).equals(App.DATA_IS_POSTED)?true:false);

                if(!temp.contains(print.getReferenceNumber())){
                    temp.add(print.getReferenceNumber());
                    arrayList.add(print);
                    i++;
                }
                //  arrayList.add(print);

            }
            while (odometerEnd.moveToNext());
        }

    }
}
