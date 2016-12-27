package gbc.sa.vansales.activities;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.Fragment.AllCustomerFragment;
import gbc.sa.vansales.Fragment.VisitAllFragment;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DataAdapter;
import gbc.sa.vansales.adapters.PagerAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerData;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.LoadDeliveryHeader;
import gbc.sa.vansales.models.VisitList;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.SwipeDetector;
/**
 * Created by eheuristic on 12/2/2016.
 */
public class SelectCustomerActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    ImageView iv_back;
    TextView tv_top_header;
    DataAdapter dataAdapter;
    ArrayList<Customer> dataArrayList;
    ImageView toolbar_iv_back;
    ImageView iv_search;
    EditText et_search;
    int tab_position;
    FloatingActionButton floatButton;
    public static final int MY_PERMISSIONS_LOCATION = 1;
    DatabaseHandler db = new DatabaseHandler(this);
    public ArrayList<CustomerHeader> customers = new ArrayList<>();
    LoadingSpinner loadingSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_trip);

        loadingSpinner = new LoadingSpinner(this);
        customers = CustomerHeaders.get();
        new loadVisitList(Settings.getString(App.TRIP_ID));
        new loadAllCustomers(Settings.getString(App.TRIP_ID));
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Sequence"));
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        floatButton = (FloatingActionButton) findViewById(R.id.float_map);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Select Customer");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dataArrayList = new ArrayList<>();
        //loadData();
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        if (tv_top_header != null) {
            tv_top_header.setVisibility(View.VISIBLE);
            tv_top_header.setText("Select Customer");
        }
        toolbar_iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        if (toolbar_iv_back != null) {
            toolbar_iv_back.setVisibility(View.VISIBLE);
        }
        iv_search = (ImageView) findViewById(R.id.iv_search);
        if (iv_search != null) {
            iv_search.setVisibility(View.VISIBLE);
        }
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_search.setVisibility(View.GONE);
                et_search.setVisibility(View.VISIBLE);
                toolbar_iv_back.setVisibility(View.GONE);
                tv_top_header.setVisibility(View.GONE);
            }
        });
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SelectCustomerActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SelectCustomerActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_LOCATION);
                } else {
                    Intent intent = new Intent(SelectCustomerActivity.this, CustomerOperationsMapActivity.class);
                    startActivity(intent);
                }
            }
        });
        et_search = (EditText) findViewById(R.id.et_search_customer);
        et_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (et_search.getRight() - et_search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        et_search.setVisibility(View.GONE);
                        iv_search.setVisibility(View.VISIBLE);
                        toolbar_iv_back.setVisibility(View.VISIBLE);
                        tv_top_header.setVisibility(View.VISIBLE);
                        return true;
                    }
                }
                return false;
            }
        });
        toolbar_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.v("addtext", "change");
                if (tab_position == 0) {
                    VisitAllFragment.dataAdapter.getFilter().filter(s.toString());
                } else {
                    AllCustomerFragment.dataAdapter1.getFilter().filter(s.toString());
                }
                //planBadgeAdapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), "s");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tab_position = tab.getPosition();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    public void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // Ignore exceptions if any
            Log.e("KeyBoardUtil", e.toString(), e);
        }
    }
    /*public void loadData() {
//        planBadgeAdapter.clear();
        for (int i = 0; i < 10; i++) {
//            Customer customer = createCustomerData(i);
//            journeyList.add(customer);
            CustomerData customerData = createCustomerData(i);
            dataArrayList.add(customerData);
        }
//        dataAdapter = new DataAdapter(SelectCustomerActivity.this,dataArrayList);
        //Const.dataArrayList = dataArrayList;
    }*/
    public static CustomerData createCustomerData(int index) {
        CustomerData customer = new CustomerData();
        int i = 100 + index;
        customer.setId(String.valueOf(i));
        customer.setName("ankit");
        customer.setAddress("rajkot");
        return customer;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(SelectCustomerActivity.this, CustomerOperationsMapActivity.class);
                    startActivity(intent);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void setVisitList(Cursor visitListCursor,boolean isVisitList) {
        Cursor cursor = visitListCursor;
        cursor.moveToFirst();
        Log.e("Cursor count", "" + cursor.getCount());
        if(isVisitList){
            dataArrayList.clear();
            ArrayList<Customer> data = new ArrayList<>();
            do {
                Customer customer = new Customer();
                customer.setCustomerID(cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_NO)));
                CustomerHeader customerHeader = CustomerHeader.getCustomer(customers,cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_NO)));

                if(!(customerHeader==null)){
                    customer.setCustomerName(customerHeader.getName1());
                    customer.setCustomerAddress(customerHeader.getAddress());
                }
                else{
                    customer.setCustomerName(cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    customer.setCustomerAddress("");
                }
                HashMap<String,String> map = new HashMap<>();
                map.put(db.KEY_CUSTOMER_NO,cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_NO)));
                if(db.checkData(db.CUSTOMER_CREDIT,map)){
                    Log.e("Credit Exist","Credit Exist");
                    customer.setPaymentMethod("Credit");
                }
                else{
                    customer.setPaymentMethod("Cash");
                }
                data.add(customer);
            }
            while (cursor.moveToNext());

            Const.dataArrayList = data;
            Log.e("Data Array","" + Const.dataArrayList.size());
        }
        else{
            dataArrayList.clear();
            ArrayList<Customer> data = new ArrayList<>();

            do {
                Customer customer = new Customer();
                Log.e("Cursor count","" + cursor.getCount());
                customer.setCustomerID(cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_NO)));
                CustomerHeader customerHeader = CustomerHeader.getCustomer(customers,cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_NO)));

                if(!(customerHeader==null)){
                    customer.setCustomerName(customerHeader.getName1());
                    customer.setCustomerAddress(customerHeader.getAddress());
                }
                else{
                    customer.setCustomerName(cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    customer.setCustomerAddress("");
                }
                HashMap<String,String> map = new HashMap<>();
                map.put(db.KEY_CUSTOMER_NO,cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_NO)));
                if(db.checkData(db.CUSTOMER_CREDIT,map)){
                    Log.e("Credit Exist", "Credit Exist");
                    customer.setPaymentMethod("Credit");
                }
                else{
                     customer.setPaymentMethod("Cash");
                }
                data.add(customer);
            }
            while (cursor.moveToNext());

            Const.allCustomerdataArrayList = data;
            Log.e("All Array","" + Const.allCustomerdataArrayList.size());
        }

    }
    private class loadVisitList extends AsyncTask<Void, Void, Void> {
        String tripId;
        private loadVisitList(String tripId) {
            this.tripId = tripId;
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_TRIP_ID, "");
            map.put(db.KEY_VISITLISTID, "");
            map.put(db.KEY_ITEMNO, "");
            map.put(db.KEY_CUSTOMER_NO, "");
            map.put(db.KEY_EXEC_DATE, "");
            map.put(db.KEY_DRIVER, "");
            map.put(db.KEY_VP_TYPE, "");
            HashMap<String, String> filters = new HashMap<>();
            filters.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
            // filters.put(db.KEY_IS_VERIFIED,"false");
            Cursor cursor = db.getData(db.VISIT_LIST, map, filters);
            if (cursor.getCount() > 0) {
                setVisitList(cursor,true);
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingSpinner.show();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
        }
    }
    private class loadAllCustomers extends AsyncTask<Void, Void, Void> {
        String tripId;
        private loadAllCustomers(String tripId) {
            this.tripId = tripId;
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_TRIP_ID, "");
            map.put(db.KEY_CUSTOMER_NO, "");
            HashMap<String, String> filters = new HashMap<>();
            filters.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
            // filters.put(db.KEY_IS_VERIFIED,"false");
            Cursor cursor = db.getData(db.CUSTOMER_HEADER, map, filters);
            if (cursor.getCount() > 0) {
                setVisitList(cursor,false);
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}