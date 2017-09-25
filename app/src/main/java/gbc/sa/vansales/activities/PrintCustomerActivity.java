package gbc.sa.vansales.activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.PriceListAdapter;
import gbc.sa.vansales.adapters.PrintAdapter;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.ItemList;
import gbc.sa.vansales.models.Print;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.PrinterHelper;
import gbc.sa.vansales.utils.UrlBuilder;
public class PrintCustomerActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    View view1;

    ListView listView;
    Customer object;
    ArrayList<CustomerHeader> customers;
    FloatingActionButton btnPrint;
    ArrayList<Print> arrayList = new ArrayList<>();
    ArrayList<Print> printArrayList = new ArrayList<>();
    PrintAdapter adapter;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_customer_list);
        loadingSpinner = new LoadingSpinner(this);
        try{
            Intent i = this.getIntent();
            object = (Customer) i.getParcelableExtra("headerObj");
            customers = CustomerHeaders.get();

            iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
            tv_top_header = (TextView) findViewById(R.id.tv_top_header);
            iv_back.setVisibility(View.VISIBLE);
            tv_top_header.setVisibility(View.VISIBLE);
            tv_top_header.setText(getString(R.string.print_items_lbl));
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            btnPrint = (FloatingActionButton) findViewById(R.id.btnPrint);
            btnPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(PrintCustomerActivity.this);
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
                                Toast.makeText(PrintCustomerActivity.this,getString(R.string.oneatattime),Toast.LENGTH_SHORT).show();
                            }
                            else if(counter==1){

                                for(Print print:arrayList){
                                    if(print.isChecked()){
                                        HashMap<String,String>map = new HashMap<String, String>();
                                        map.put(db.KEY_DATA,"");
                                        HashMap<String,String>filter = new HashMap<String, String>();
                                        filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
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
                                                PrinterHelper object = new PrinterHelper(PrintCustomerActivity.this,PrintCustomerActivity.this);
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

            listView = (ListView)findViewById(R.id.list_price_list);
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
     @ Loading all the print documents for the customer
     @ for eg order request, sales invoice, invoice receipt,
     @ delivery note, good return/bad return note
     ********************************************************/
    public class loadPrintItems extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_TIME_STAMP,"");
            map.put(db.KEY_PURCHASE_NUMBER,"");

            HashMap<String,String> filter = new HashMap<>();
            filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());

            HashMap<String,String> gRfilter = new HashMap<>();
            gRfilter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            gRfilter.put(db.KEY_REASON_TYPE, App.GOOD_RETURN);

            HashMap<String,String> bRfilter = new HashMap<>();
            bRfilter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
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
            collectionFilter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            collectionFilter1.put(db.KEY_CUSTOMER_NO,object.getCustomerID());

            collectionFilter.put(db.KEY_IS_POSTED, App.DATA_IS_POSTED);
            collectionFilter1.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);

            Cursor invoicePosted = db.getData(db.COLLECTION,collection,collectionFilter);
            Cursor invoiceMarkPosted = db.getData(db.COLLECTION,collection,collectionFilter1);

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
            if(goodReturn.getCount()>0){
                goodReturn.moveToFirst();
            }
            if(badReturn.getCount()>0){
                badReturn.moveToFirst();
            }
            setPrintItems(orderRequest,salesRequest,deliveryRequest,goodReturn,badReturn,invoicePosted,invoiceMarkPosted);
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
    private void setPrintItems(Cursor cursor1, Cursor cursor2, Cursor cursor3,Cursor cursor4, Cursor cursor5,Cursor cursor6, Cursor cursor7){
        try{
            Cursor orderRequest = cursor1;
            Cursor salesRequest = cursor2;
            Cursor deliveryRequest = cursor3;
            Cursor goodReturnsRequest = cursor4;
            Cursor badReturnsRequest = cursor5;
            Cursor invoicePosted = cursor6;
            Cursor invoiceMarkPosted = cursor7;
            ArrayList<String> temp=new ArrayList<String>();
            temp.clear();
            arrayList.clear();
            int i= 1;
            if(orderRequest.getCount()>0){
                orderRequest.moveToFirst();
                do{
                    Print print = new Print();
                    // print.setCustomer_id(object.getCustomerID());
                    print.setCustomer_id(i==1?String.valueOf(i):String.valueOf(i));
                    print.setCustomer_name(object.getCustomerName());
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
                    //print.setCustomer_id(object.getCustomerID());
                    print.setCustomer_id(i == 1 ? String.valueOf(i) : String.valueOf(i));
                    print.setCustomer_name(object.getCustomerName());
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
                    //print.setCustomer_id(object.getCustomerID());
                    print.setCustomer_id(i == 1 ? String.valueOf(i) : String.valueOf(i));
                    print.setCustomer_name(object.getCustomerName());
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
                    print.setCustomer_id(i == 1 ? String.valueOf(i) : String.valueOf(i));
                    print.setCustomer_name(object.getCustomerName());
                    print.setReferenceNumber(goodReturnsRequest.getString(goodReturnsRequest.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
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
                    print.setCustomer_id(i == 1 ? String.valueOf(i) : String.valueOf(i));
                    print.setCustomer_name(object.getCustomerName());
                    print.setReferenceNumber(badReturnsRequest.getString(badReturnsRequest.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
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
                    print.setCustomer_id(i == 1 ? String.valueOf(i) : String.valueOf(i));
                    print.setCustomer_name(object.getCustomerName());
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
                    print.setCustomer_id(i == 1 ? String.valueOf(i) : String.valueOf(i));
                    print.setCustomer_name(object.getCustomerName());
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
