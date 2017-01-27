package gbc.sa.vansales.activities;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerInformationAdapter;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
public class TodaysScheduleActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    View view1;
    ArrayList<CustomerHeader> customers;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    String schedule;
    ArrayList<Customer> arrayList = new ArrayList<>();
    private ArrayAdapter<Customer> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_unserviced);

        schedule = getIntent().getExtras().getString("schedule");
        customers = CustomerHeaders.get();
        //Log.e("customers","" + customers.size());
        loadingSpinner = new LoadingSpinner(this);
      //  CustomerHeader customerHeader = CustomerHeader.getCustomer(customers,object.getCustomerID());

        listView = (ListView)findViewById(R.id.listView);
        adapter = new CustomerInformationAdapter(this,arrayList);
        listView.setAdapter(adapter);

        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Todays Schedule");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new loadSchedule(schedule);
    }

    public void setVisitList(Cursor cursor,String schedule){
        Cursor customerCursor = cursor;
        switch (schedule){
            case "schedule":{
                do{
                    Customer customer = new Customer();
                    customer.setCustomerID(customerCursor.getString(customerCursor.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, customer.getCustomerID());
                    if(!(customerHeader==null)){
                        customer.setCustomerName(UrlBuilder.decodeString(customerHeader.getName1()));
                        customer.setCustomerAddress(UrlBuilder.decodeString(customerHeader.getStreet()));
                    }
                    else{
                        customer.setCustomerName(cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_NO)));
                        customer.setCustomerAddress("");
                    }
                    arrayList.add(customer);
                }
                while (customerCursor.moveToNext());

                break;
            }
            case "unserviced":{
                do{

                }
                while (customerCursor.moveToNext());
                break;
            }
            case "all":{
                do{
                    Customer customer = new Customer();
                    customer.setCustomerID(cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    CustomerHeader customerHeader = CustomerHeader.getCustomer(customers,cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_NO)));

                    if(!(customerHeader==null)){
                        customer.setCustomerName(UrlBuilder.decodeString(customerHeader.getName1()));
                        customer.setCustomerAddress(UrlBuilder.decodeString(customerHeader.getStreet()));
                    }
                    else{
                        customer.setCustomerName(cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_NO)));
                        customer.setCustomerAddress("");
                    }
                    arrayList.add(customer);
                }
                while (customerCursor.moveToNext());
                break;
            }
        }
    }

    public class loadSchedule extends AsyncTask<Void,Void,Void>{

        private String schedule;

        private loadSchedule(String schedule) {
            this.schedule = schedule;
            execute();
        }
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            switch (this.schedule){
                case "schedule":{
                    HashMap<String, String> map = new HashMap<>();
                    map.put(db.KEY_TRIP_ID, "");
                    map.put(db.KEY_VISITLISTID, "");
                    map.put(db.KEY_ITEMNO, "");
                    map.put(db.KEY_CUSTOMER_NO, "");
                    map.put(db.KEY_EXEC_DATE, "");
                    map.put(db.KEY_DRIVER, "");
                    HashMap<String, String> filters = new HashMap<>();
                    filters.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    // filters.put(db.KEY_IS_VERIFIED,"false");
                    Cursor cursor = db.getData(db.VISIT_LIST, map, filters);
                    if (cursor.getCount() > 0) {
                        setVisitList(cursor,schedule);
                    }
                    break;
                }
                case "unserviced":{

                }
                case "all":{
                    HashMap<String, String> map = new HashMap<>();
                    map.put(db.KEY_TRIP_ID, "");
                    map.put(db.KEY_CUSTOMER_NO, "");
                    HashMap<String, String> filters = new HashMap<>();
                    filters.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    // filters.put(db.KEY_IS_VERIFIED,"false");
                    Cursor cursor = db.getData(db.CUSTOMER_HEADER, map, filters);
                    if (cursor.getCount() > 0) {
                        setVisitList(cursor,schedule);
                    }
                }
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
}
