package gbc.sa.vansales.activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_customer_list);
        loadingSpinner = new LoadingSpinner(this);
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
                        finish();
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
    public void getData()
    {


        /*for(int i=0;i<10;i++)
        {
            ItemList model = new ItemList();
            model.setItem_number(i);
            model.setItem_des("print items");
            model.setCase_price(100 + i);

            arrayList.add(model);
        }

        adapter = new PriceListAdapter(PrintCustomerActivity.this,arrayList);
        listView.setAdapter(adapter);*/
    }

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
            Cursor orderRequest = db.getData(db.ORDER_REQUEST,map,filter);
            Cursor salesRequest = db.getData(db.CAPTURE_SALES_INVOICE,map,filter);
            Cursor deliveryRequest = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST,map,filter);
            if(orderRequest.getCount()>0){
                orderRequest.moveToFirst();
            }
            if(salesRequest.getCount()>0){
                salesRequest.moveToFirst();
            }
            if(deliveryRequest.getCount()>0){
                deliveryRequest.moveToFirst();
            }
            setPrintItems(orderRequest,salesRequest,deliveryRequest);
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

    private void setPrintItems(Cursor cursor1, Cursor cursor2, Cursor cursor3){
        Cursor orderRequest = cursor1;
        Cursor salesRequest = cursor2;
        Cursor deliveryRequest = cursor3;
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
    }
}
