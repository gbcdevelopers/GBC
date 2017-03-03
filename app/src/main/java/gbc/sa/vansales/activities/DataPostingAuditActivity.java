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

import com.crashlytics.android.Crashlytics;

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

/************************************************************
 @ This activity gives an overview of the data posting audit
 @ performed for the driver in the said day. Data posting audit
 @ logs all transactions performed by the order
 @ Below are the transactions that are logged
 @ 1.Begin Day(BDAY) 2. Begin Day Odometer(ODOBD) 3. Load Confirmation(LCON)
 @ 4.Load Variance (LV) 5. Add Customer 6. Customer Order Create(ORD)
 @ 7.Customer Delivery (DEL) 8. Sales Invoice (SI) 9. Good Returns(GR)
 @ 9.Bad Returns (BR) 10. Collection (AR) 11. Unload (UL)
 @ 12.End Trip (ED) 13. Odometer End Day(ODOED)
 ************************************************************/
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

    /************************************************************
     @ This function will load all the driver related transactions like
     @ beginday, odometer, end trip etc.
     ************************************************************/
    public class loadDriverTransactions extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try{
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
                Cursor unloadRequest = db.getData(db.UNLOAD_TRANSACTION,beginDayMap,filter);
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
                if(unloadRequest.getCount()>0){
                    unloadRequest.moveToFirst();
                }
                setDriverAuditItems(beginDay, odoMeter, loadConfirmation,endDay,odoMeterEnd,unloadRequest);
                //setAuditItems(orderRequest, salesRequest, deliveryRequest,loadRequest);
            }
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            new loadTransactions().execute();
        }
    }
    /************************************************************
     @ This function will load all the customer related transactions like
     @ customer order, customer delivery, sales invoice, good returns,
     @ bad returns, collection etc.
     ************************************************************/
    public class loadTransactions extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try{
                HashMap<String,String>map = new HashMap<>();
                map.put(db.KEY_PURCHASE_NUMBER,"");
                map.put(db.KEY_IS_POSTED,"");
                map.put(db.KEY_CUSTOMER_NO,"");

                HashMap<String,String> gRfilter = new HashMap<>();
                gRfilter.put(db.KEY_REASON_TYPE, App.GOOD_RETURN);

                HashMap<String,String> bRfilter = new HashMap<>();
                bRfilter.put(db.KEY_REASON_TYPE, App.BAD_RETURN);

                HashMap<String,String> collection = new HashMap<>();
                collection.put(db.KEY_TIME_STAMP,"");
                collection.put(db.KEY_INVOICE_NO,"");
                collection.put(db.KEY_IS_POSTED,"");
                collection.put(db.KEY_CUSTOMER_NO,"");
                HashMap<String,String> collectionFilter = new HashMap<>();
                HashMap<String,String> collectionFilter1 = new HashMap<>();
                collectionFilter.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                collectionFilter1.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);

                Cursor invoicePosted = db.getData(db.COLLECTION,collection,collectionFilter);

                Cursor invoiceMarkPosted = db.getData(db.COLLECTION,collection,collectionFilter1);

                HashMap<String,String>filter = new HashMap<>();
                Cursor orderRequest = db.getData(db.ORDER_REQUEST,map,filter);
                Cursor salesRequest = db.getData(db.CAPTURE_SALES_INVOICE,map,filter);
                Cursor deliveryRequest = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST,map,filter);
                Cursor goodReturn = db.getData(db.RETURNS,map,gRfilter);
                Cursor badReturn = db.getData(db.RETURNS,map,bRfilter);
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
                if(goodReturn.getCount()>0){
                    goodReturn.moveToFirst();
                }
                if(badReturn.getCount()>0){
                    badReturn.moveToFirst();
                }
                if(invoicePosted.getCount()>0){
                    invoicePosted.moveToFirst();
                }
                if(invoiceMarkPosted.getCount()>0){
                    invoiceMarkPosted.moveToFirst();
                }
                setAuditItems(orderRequest, salesRequest, deliveryRequest,goodReturn,badReturn,invoicePosted,invoiceMarkPosted,loadRequest);
            }
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
            }
            return null;
        }
    }
    private void setAuditItems(Cursor cursor1,Cursor cursor2,Cursor cursor3,Cursor cursor4,Cursor cursor5,Cursor cursor6, Cursor cursor7,Cursor cursor8){
        try{
            Cursor orderRequest = cursor1;
            Cursor salesRequest = cursor2;
            Cursor deliveryRequest = cursor3;
            Cursor goodReturnsRequest = cursor4;
            Cursor badReturnsRequest = cursor5;
            Cursor invoicePosted = cursor6;
            Cursor invoiceMarkPosted = cursor7;
            Cursor loadRequest = cursor8;
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

            if(goodReturnsRequest.getCount()>0){
                goodReturnsRequest.moveToFirst();
                do{
                    Print print = new Print();
                    //print.setCustomer_id(object.getCustomerID());
                    print.setCustomer_id(i == 1 ? String.valueOf(i) : String.valueOf(i));
                    print.setCustomer_id(goodReturnsRequest.getString(goodReturnsRequest.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    print.setReferenceNumber(goodReturnsRequest.getString(goodReturnsRequest.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                    print.setTransactionType(ConfigStore.GoodReturns_TR);
                    print.setIsPosted(goodReturnsRequest.getString(goodReturnsRequest.getColumnIndex(db.KEY_IS_POSTED)).equals(App.DATA_IS_POSTED) ? true : false);

                    if(!temp.contains(print.getReferenceNumber())){
                        temp.add(print.getReferenceNumber());
                        arrayList.add(print);
                        i++;
                    }

                }
                while (goodReturnsRequest.moveToNext());
            }

            if(badReturnsRequest.getCount()>0){
                badReturnsRequest.moveToFirst();
                do{
                    Print print = new Print();
                    //print.setCustomer_id(object.getCustomerID());
                    print.setCustomer_id(i == 1 ? String.valueOf(i) : String.valueOf(i));
                    print.setCustomer_id(badReturnsRequest.getString(badReturnsRequest.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    print.setReferenceNumber(badReturnsRequest.getString(badReturnsRequest.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                    print.setTransactionType(ConfigStore.BadReturns_TR);
                    print.setIsPosted(badReturnsRequest.getString(badReturnsRequest.getColumnIndex(db.KEY_IS_POSTED)).equals(App.DATA_IS_POSTED) ? true : false);

                    if(!temp.contains(print.getReferenceNumber())){
                        temp.add(print.getReferenceNumber());
                        arrayList.add(print);
                        i++;
                    }

                }
                while (badReturnsRequest.moveToNext());
            }

            if(invoicePosted.getCount()>0){
                invoicePosted.moveToFirst();
                do{
                    Print print = new Print();
                    //print.setCustomer_id(object.getCustomerID());

                    print.setCustomer_id(i == 1 ? String.valueOf(i) : String.valueOf(i));
                    print.setCustomer_id(invoicePosted.getString(invoicePosted.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    print.setReferenceNumber(invoicePosted.getString(invoicePosted.getColumnIndex(db.KEY_INVOICE_NO)));
                    print.setTransactionType(ConfigStore.CollectionRequest_TR);
                    print.setIsPosted(invoicePosted.getString(invoicePosted.getColumnIndex(db.KEY_IS_POSTED)).equals(App.DATA_IS_POSTED) ? true : false);
                    arrayList.add(print);
                    i++;
                /*if(!temp.contains(print.getReferenceNumber())){
                    temp.add(print.getReferenceNumber());
                    arrayList.add(print);
                    i++;
                }*/

                }
                while (invoicePosted.moveToNext());
            }

            if(invoiceMarkPosted.getCount()>0){
                invoiceMarkPosted.moveToFirst();
                do{
                    Print print = new Print();
                    //print.setCustomer_id(object.getCustomerID());

                    print.setCustomer_id(i == 1 ? String.valueOf(i) : String.valueOf(i));
                    print.setCustomer_id(invoiceMarkPosted.getString(invoiceMarkPosted.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    print.setReferenceNumber(invoiceMarkPosted.getString(invoiceMarkPosted.getColumnIndex(db.KEY_INVOICE_NO)));
                    print.setTransactionType(ConfigStore.CollectionRequest_TR);
                    print.setIsPosted(invoiceMarkPosted.getString(invoiceMarkPosted.getColumnIndex(db.KEY_IS_POSTED)).equals(App.DATA_IS_POSTED) ? true : false);
                    arrayList.add(print);
                    i++;
                /*if(!temp.contains(print.getReferenceNumber())){
                    temp.add(print.getReferenceNumber());
                    arrayList.add(print);
                    i++;
                }*/

                }
                while (invoiceMarkPosted.moveToNext());
            }

            if(loadRequest.getCount()>0){
                loadRequest.moveToFirst();
                do{
                    Print print = new Print();
                    //  print.setCustomer_id(loadRequest.getString(loadRequest.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    //print.setCustomer_id("-");
                    print.setCustomer_id(Settings.getString(App.DRIVER));
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
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    private void setDriverAuditItems(Cursor cursor1, Cursor cursor2, Cursor cursor3,Cursor cursor4,Cursor cursor5,Cursor cursor6){
        try{
            Cursor beginDay = cursor1;
            Cursor odometer = cursor2;
            Cursor loadConfirmation = cursor3;
            Cursor endDay = cursor4;
            Cursor odometerEnd = cursor5;
            Cursor unload = cursor6;

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

            if(unload.getCount()>0){
                unload.moveToFirst();
                do{
                    Print print = new Print();
                    print.setCustomer_id(Settings.getString(App.DRIVER));
                    print.setReferenceNumber(unload.getString(unload.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                    print.setTransactionType(ConfigStore.UnloadRequest_TR);
                    print.setIsPosted(unload.getString(unload.getColumnIndex(db.KEY_IS_POSTED)).equals(App.DATA_IS_POSTED)?true:false);

                    if(!temp.contains(print.getReferenceNumber())){
                        temp.add(print.getReferenceNumber());
                        arrayList.add(print);
                        i++;
                    }
                    //  arrayList.add(print);

                }
                while (beginDay.moveToNext());
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
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }
}
