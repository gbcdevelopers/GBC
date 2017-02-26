package gbc.sa.vansales.activities;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DataPoustingAuditAdapter;
import gbc.sa.vansales.adapters.PrintAdapter;
import gbc.sa.vansales.adapters.PrintDocumentAdapter;
import gbc.sa.vansales.models.DataPoustingAudit;
import gbc.sa.vansales.models.Print;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_document);
        loadingSpinner = new LoadingSpinner(this);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.print_transactions_lbl));
        btn_print_printer_report = (Button)findViewById(R.id.btn_print_printer_report);
        btn_print_printer_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Print print:arrayList){
                    if(print.isChecked()){

                    }
                }
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
            Cursor orderRequest = db.getData(db.ORDER_REQUEST,map,filter);
            Cursor salesRequest = db.getData(db.CAPTURE_SALES_INVOICE,map,filter);
            Cursor deliveryRequest = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST,map,filter);

            HashMap<String,String> collection = new HashMap<>();
            collection.put(db.KEY_TIME_STAMP,"");
            collection.put(db.KEY_PURCHASE_NUMBER,"");
            collection.put(db.KEY_CUSTOMER_NO,"");
            HashMap<String,String> collectionFilter = new HashMap<>();
            /*filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());*/



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
    }

}
