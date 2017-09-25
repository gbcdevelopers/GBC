package gbc.sa.vansales.activities;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.CacheRequest;
import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DataPoustingAuditAdapter;
import gbc.sa.vansales.adapters.PrintAdapter;
import gbc.sa.vansales.adapters.PrintDocumentAdapter;
import gbc.sa.vansales.models.DataPoustingAudit;
import gbc.sa.vansales.models.Print;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.PrinterHelper;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
public class PrintDocumentActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    View view1;

    CheckBox checkBox;
    ArrayList<Print> arrayList = new ArrayList<>();
    ListView listView;
    PrintAdapter adapter;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    Button btn_print_printer_report;
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_document);
        loadingSpinner = new LoadingSpinner(this);
        try{
            iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
            tv_top_header = (TextView) findViewById(R.id.tv_top_header);
            iv_back.setVisibility(View.VISIBLE);
            tv_top_header.setVisibility(View.VISIBLE);
            tv_top_header.setText(getString(R.string.print_transactions_lbl));
            btn_print_printer_report = (Button)findViewById(R.id.btn_print_printer_report);
            btn_print_printer_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(PrintDocumentActivity.this);
                    dialog.setContentView(R.layout.dialog_doprint);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                    LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                    dialog.show();
                    btn_print.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(Print print:arrayList){
                                if(print.isChecked()){
                                    counter++;
                                }
                            }
                            if(counter>1){
                                Toast.makeText(PrintDocumentActivity.this, getString(R.string.oneatattime), Toast.LENGTH_SHORT).show();
                                counter = 0;
                            }
                            else if(counter==1){

                                for(Print print:arrayList){
                                    if(print.isChecked()){
                                        HashMap<String,String>map = new HashMap<String, String>();
                                        map.put(db.KEY_DATA,"");
                                        HashMap<String,String>filter = new HashMap<String, String>();
                                        filter.put(db.KEY_CUSTOMER_NO,print.getCustomer_id());
                                        filter.put(db.KEY_ORDER_ID,print.getReferenceNumber());
                                        filter.put(db.KEY_DOC_TYPE,print.getTransactionType());
                                        Cursor c = db.getData(db.DELAY_PRINT,map,filter);
                                        if(c.getCount()>0){
                                            c.moveToFirst();
                                            try{
                                                String jsonString = c.getString(c.getColumnIndex(db.KEY_DATA));
                                                jsonString = UrlBuilder.decodeString(jsonString);
                                                //jsonString = "{" + jsonString + "}";
                                                dialog.dismiss();
                                                JSONObject jsonObject = new JSONObject(jsonString);
                                                JSONArray jsonArray = (JSONArray)jsonObject.getJSONArray("data");
                                                PrinterHelper object = new PrinterHelper(PrintDocumentActivity.this,PrintDocumentActivity.this);
                                                object.execute("", jsonArray);
                                                counter = 0;
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                }
                            }
                            else if(counter==0){
                                Toast.makeText(getApplicationContext(),getString(R.string.please_select_report),Toast.LENGTH_SHORT).show();
                            }

                            //finish();
                        }
                    });
                    btn_notprint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


            checkBox=(CheckBox)findViewById(R.id.checkBox);

            checkBox.setVisibility(View.INVISIBLE);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*adapter = new PrintDocumentAdapter(PrintDocumentActivity.this, arrayList, isChecked);
                listView.setAdapter(adapter);*/
                    adapter = new PrintAdapter(PrintDocumentActivity.this,arrayList,isChecked);
                }
            });

            listView = (ListView)findViewById(R.id.print_document_list);
            adapter = new PrintAdapter(this,arrayList);
            listView.setAdapter(adapter);

            new loadPrintItems().execute();

        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }

    /********************************************************
     @ Reading data for all the printable activities possible
     @ on the device which were executed.
     @ Load Summary, Order Request, Load Request, Sales Invoice,
     @ Invoice Receipt, Delivery Note, Good Return, Bad Return,
     @ Unload Summary etc.
     ********************************************************/

    public class loadPrintItems extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_TIME_STAMP,"");
            map.put(db.KEY_PURCHASE_NUMBER,"");
            map.put(db.KEY_CUSTOMER_NO,"");
            HashMap<String,String> filter = new HashMap<>();
            /*filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());*/

            HashMap<String,String> gRfilter = new HashMap<>();
            gRfilter.put(db.KEY_REASON_TYPE, App.GOOD_RETURN);

            HashMap<String,String> bRfilter = new HashMap<>();
            bRfilter.put(db.KEY_REASON_TYPE, App.BAD_RETURN);


            Cursor orderRequest = db.getData(db.ORDER_REQUEST,map,filter);
            Cursor salesRequest = db.getData(db.CAPTURE_SALES_INVOICE,map,filter);
            Cursor deliveryRequest = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST,map,filter);
            Cursor goodReturn = db.getData(db.RETURNS,map,gRfilter);
            Cursor badReturn = db.getData(db.RETURNS,map,bRfilter);

            HashMap<String,String> collection = new HashMap<>();
            collection.put(db.KEY_TIME_STAMP,"");
            collection.put(db.KEY_INVOICE_NO,"");
            collection.put(db.KEY_CUSTOMER_NO,"");
            HashMap<String,String> collectionFilter = new HashMap<>();
            HashMap<String,String> collectionFilter1 = new HashMap<>();
            collectionFilter.put(db.KEY_IS_POSTED, App.DATA_IS_POSTED);
            collectionFilter1.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);

            Cursor invoicePosted = db.getData(db.COLLECTION,collection,collectionFilter);
            Cursor invoiceMarkPosted = db.getData(db.COLLECTION,collection,collectionFilter1);

            Cursor invoiceDriverPosted = db.getData(db.DRIVER_COLLECTION,collection,collectionFilter);
            Cursor invoiceDriverMarkPosted = db.getData(db.DRIVER_COLLECTION,collection,collectionFilter1);

            /*filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());*/

            HashMap<String,String>loadMap = new HashMap<>();
            loadMap.put(db.KEY_ORDER_ID,"");
            HashMap<String,String>filterMap = new HashMap<>();
            filterMap.put(db.KEY_CUSTOMER_NO, Settings.getString(App.DRIVER));

            Cursor loadSummaryCursor = db.getData(db.LOAD_CONFIRMATION_HEADER,loadMap,filterMap);
            Cursor loadRequestCursor = db.getData(db.LOAD_REQUEST,map,filterMap);
            Cursor unloadRequest = db.getData(db.UNLOAD_TRANSACTION,map,filter);

            if(loadSummaryCursor.getCount()>0){
                loadSummaryCursor.moveToFirst();
            }

            if(loadRequestCursor.getCount()>0){
                loadRequestCursor.moveToFirst();
            }
            if(orderRequest.getCount()>0){
                orderRequest.moveToFirst();
            }
            if(salesRequest.getCount()>0){
                salesRequest.moveToFirst();
            }
            if(deliveryRequest.getCount()>0){
                deliveryRequest.moveToFirst();
            }
            if(invoicePosted.getCount()>0){
                invoicePosted.moveToFirst();
            }
            if(invoiceMarkPosted.getCount()>0){
                invoiceMarkPosted.moveToFirst();
            }
            if(invoiceDriverPosted.getCount()>0){
                invoiceDriverPosted.moveToFirst();
            }
            if(invoiceDriverMarkPosted.getCount()>0){
                invoiceDriverMarkPosted.moveToFirst();
            }
            if(goodReturn.getCount()>0){
                goodReturn.moveToFirst();
            }
            if(badReturn.getCount()>0){
                badReturn.moveToFirst();
            }
            if(unloadRequest.getCount()>0){
                unloadRequest.moveToFirst();
            }

            setPrintItems(loadSummaryCursor,loadRequestCursor,orderRequest,salesRequest,deliveryRequest,goodReturn,badReturn,invoicePosted,invoiceMarkPosted,unloadRequest,invoiceDriverPosted,invoiceDriverMarkPosted);
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
    private void setPrintItems(Cursor loadSummaryCursor,Cursor loadRequestCursor,Cursor cursor1, Cursor cursor2, Cursor cursor3,Cursor cursor4, Cursor cursor5,Cursor cursor6, Cursor cursor7,Cursor cursor8,Cursor cursor9,Cursor cursor10){
        try{
            Cursor loadSummary = loadSummaryCursor;
            Cursor loadRequest = loadRequestCursor;
            Cursor orderRequest = cursor1;
            Cursor salesRequest = cursor2;
            Cursor deliveryRequest = cursor3;
            Cursor goodReturnsRequest = cursor4;
            Cursor badReturnsRequest = cursor5;
            Cursor invoicePosted = cursor6;
            Cursor invoiceMarkPosted = cursor7;
            Cursor unloadRequest = cursor8;
            Cursor driverinvoicePosted = cursor9;
            Cursor driverinvoiceMarkPosted = cursor10;

            ArrayList<String> temp=new ArrayList<String>();
            temp.clear();
            arrayList.clear();
            int i= 1;
            if(loadSummary.getCount()>0){
                loadSummary.moveToFirst();
                do{
                    Print print = new Print();
                    print.setCustomer_id(Settings.getString(App.DRIVER));
                    //print.setCustomer_id(i == 1 ? String.valueOf(i) : String.valueOf(i));
                    //  print.setCustomer_name(object.getCustomerName());
                    HashMap<String,String>map = new HashMap<>();
                    map.put(db.KEY_DELIVERY_NO, "");
                    HashMap<String,String>filter = new HashMap<>();
                    filter.put(db.KEY_ORDER_ID, loadSummary.getString(loadSummary.getColumnIndex(db.KEY_ORDER_ID)));
                    Cursor c = db.getData(db.LOAD_DELIVERY_ITEMS,map,filter);
                    if(c.getCount()>0){
                        c.moveToFirst();
                        print.setReferenceNumber(c.getString(c.getColumnIndex(db.KEY_DELIVERY_NO)));
                        print.setTransactionType(ConfigStore.LoadConfirmation_TR);
                        print.setIsChecked(false);
                        if(!temp.contains(print.getReferenceNumber())){
                            temp.add(print.getReferenceNumber());
                            arrayList.add(print);
                            i++;
                        }
                    }

                }
                while (loadSummary.moveToNext());
            }
            if(loadRequestCursor.getCount()>0){
                loadRequest.moveToFirst();
                do{
                    Print print = new Print();
                    print.setCustomer_id(Settings.getString(App.DRIVER));
                    //print.setCustomer_id(i == 1 ? String.valueOf(i) : String.valueOf(i));
                    //  print.setCustomer_name(object.getCustomerName());
                    print.setReferenceNumber(loadRequest.getString(loadRequest.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                    print.setTransactionType(ConfigStore.LoadRequest_TR);
                    print.setIsChecked(false);
                    if(!temp.contains(print.getReferenceNumber())){
                        temp.add(print.getReferenceNumber());
                        arrayList.add(print);
                        i++;
                    }
                }
                while (loadRequest.moveToNext());
            }
            if(orderRequest.getCount()>0){
                orderRequest.moveToFirst();
                do{
                    Print print = new Print();
                    print.setCustomer_id(orderRequest.getString(orderRequest.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    //print.setCustomer_id(i == 1 ? String.valueOf(i) : String.valueOf(i));
                    //  print.setCustomer_name(object.getCustomerName());
                    print.setReferenceNumber(orderRequest.getString(orderRequest.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                    print.setTransactionType(ConfigStore.OrderRequest_TR);
                    print.setIsChecked(false);
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
                    print.setIsChecked(false);
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
                    print.setIsChecked(false);
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
                    print.setCustomer_id(goodReturnsRequest.getString(goodReturnsRequest.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    print.setReferenceNumber(goodReturnsRequest.getString(salesRequest.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                    print.setTransactionType(ConfigStore.GoodReturns_TR);
                    print.setIsChecked(false);
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
                    print.setCustomer_id(badReturnsRequest.getString(badReturnsRequest.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    print.setReferenceNumber(badReturnsRequest.getString(salesRequest.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                    print.setTransactionType(ConfigStore.BadReturns_TR);
                    print.setIsChecked(false);
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
                    print.setCustomer_id(invoicePosted.getString(invoicePosted.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    print.setReferenceNumber(invoicePosted.getString(invoicePosted.getColumnIndex(db.KEY_INVOICE_NO)));
                    print.setTransactionType(ConfigStore.CollectionRequest_TR);
                    print.setIsChecked(false);
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
                    print.setCustomer_id(invoiceMarkPosted.getString(invoiceMarkPosted.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    print.setReferenceNumber(invoiceMarkPosted.getString(invoiceMarkPosted.getColumnIndex(db.KEY_INVOICE_NO)));
                    print.setTransactionType(ConfigStore.CollectionRequest_TR);
                    print.setIsChecked(false);
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
            if(driverinvoicePosted.getCount()>0){
                driverinvoicePosted.moveToFirst();
                do{
                    Print print = new Print();
                    //print.setCustomer_id(object.getCustomerID());
                    print.setCustomer_id(driverinvoicePosted.getString(driverinvoicePosted.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    print.setReferenceNumber(driverinvoicePosted.getString(driverinvoicePosted.getColumnIndex(db.KEY_INVOICE_NO)));
                    print.setTransactionType(ConfigStore.DriverCollectionRequest_TR);
                    print.setIsChecked(false);
                    arrayList.add(print);
                    i++;
                /*if(!temp.contains(print.getReferenceNumber())){
                    temp.add(print.getReferenceNumber());
                    arrayList.add(print);
                    i++;
                }*/

                }
                while (driverinvoicePosted.moveToNext());
            }
            if(driverinvoiceMarkPosted.getCount()>0){
                driverinvoiceMarkPosted.moveToFirst();
                do{
                    Print print = new Print();
                    //print.setCustomer_id(object.getCustomerID());
                    print.setCustomer_id(driverinvoiceMarkPosted.getString(driverinvoiceMarkPosted.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    print.setReferenceNumber(driverinvoiceMarkPosted.getString(driverinvoiceMarkPosted.getColumnIndex(db.KEY_INVOICE_NO)));
                    print.setTransactionType(ConfigStore.DriverCollectionRequest_TR);
                    print.setIsChecked(false);
                    arrayList.add(print);
                    i++;
                /*if(!temp.contains(print.getReferenceNumber())){
                    temp.add(print.getReferenceNumber());
                    arrayList.add(print);
                    i++;
                }*/


                }
                while (driverinvoiceMarkPosted.moveToNext());
            }
            if(unloadRequest.getCount()>0){
                unloadRequest.moveToFirst();
                do{
                    Print print = new Print();
                    print.setCustomer_id(unloadRequest.getString(unloadRequest.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    print.setReferenceNumber(unloadRequest.getString(unloadRequest.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                    print.setTransactionType(ConfigStore.UnloadRequest_TR);
                    print.setIsChecked(false);
                    if(!temp.contains(print.getReferenceNumber())){
                        temp.add(print.getReferenceNumber());
                        arrayList.add(print);
                        i++;
                    }

                }
                while (unloadRequest.moveToNext());
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }

}
