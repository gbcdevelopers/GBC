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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pousting_audit);
        loadingSpinner = new LoadingSpinner(this);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Data Posting Audit");
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
                /*adapter = new DataPoustingAuditAdapter(DataPostingAuditActivity.this, arrayList, isChecked);
                listView.setAdapter(adapter);*/
            }
        });
        listView = (ListView) findViewById(R.id.print_document_list);
        adapter = new DataPostingAuditAdapter(this,arrayList);
        listView.setAdapter(adapter);
        swipeDetector = new SwipeDetector();

        new loadTransactions().execute();
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
        arrayList.clear();
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
}
