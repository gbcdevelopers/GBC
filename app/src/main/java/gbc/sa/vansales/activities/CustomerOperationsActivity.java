package gbc.sa.vansales.activities;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DataAdapter;
import gbc.sa.vansales.adapters.JourneyPlanBadgeAdapter;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerData;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.SwipeDetector;

/**
 * Created by Rakshit on 16-Nov-16.
 */
public class CustomerOperationsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String COLLECTION_NAME = "JourneyPlan";
    private LoadingSpinner loadingSpinner;

    private static final String JP = "VISIT LIST";
    private static final String ALLCUST = "ALL";



    ArrayList<Customer> dataArrayList;

//    private ArrayAdapter<Customer> adapter;
    JourneyPlanBadgeAdapter planBadgeAdapter;
    private TabHost tabHost;
    private SwipeDetector swipeDetector;
    private LinearLayout allCustomersView;
    float downX;
    float upX;
    ListView listView;

    DataAdapter dataAdapter;

    TextView tv_top_header;

    ImageView toolbar_iv_back;
    ImageView iv_search;

    EditText et_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_operations);
        swipeDetector = new SwipeDetector();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabHost = (TabHost) findViewById(R.id.tabHost);




        dataArrayList =new ArrayList<>();






        tv_top_header=(TextView)findViewById(R.id.tv_top_header);
        if (tv_top_header != null) {
            tv_top_header.setVisibility(View.VISIBLE);
            tv_top_header.setText("Select Customer");
        }


        toolbar_iv_back=(ImageView)findViewById(R.id.toolbar_iv_back) ;
        if (toolbar_iv_back != null) {
            toolbar_iv_back.setVisibility(View.VISIBLE);
        }

        iv_search=(ImageView)findViewById(R.id.iv_search);
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


        et_search=(EditText)findViewById(R.id.et_search_customer);
        et_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_search.getRight() - et_search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
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






       /* setupTabs();*/

        loadingSpinner = new LoadingSpinner(this);


//        adapter = new JourneyPlanBadgeAdapter(this, journeyList);
//        planBadgeAdapter = new JourneyPlanBadgeAdapter(this, journeyList,listView);


        allCustomersView = (LinearLayout)findViewById(R.id.allCustomersView);
        allCustomersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerOperationsActivity.this,"Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        allCustomersView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        downX = event.getX();}
                    case MotionEvent.ACTION_UP:{
                        upX = event.getX();

                        float deltaX = downX - upX;

                        if(Math.abs(deltaX)>0){
                            if(deltaX>=0){
                                //Log.e("SwipeRight","SwipeRight");
                                return true;
                            }else{
                                //Log.e("SwipeLeft", "SwipeLeft");
                                return  true;
                            }
                        }
                    }
                }
                return false;
            }
        });


        listView = (ListView) findViewById(R.id.journeyPlanList);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(this);
       // listView.setOnTouchListener(swipeDetector);

//        listView.setAdapter(planBadgeAdapter);
//        loadingSpinner.show();
        new loadJourneyPlan().execute();

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

                Log.v("addtext","change");
               dataAdapter.getFilter().filter(s.toString());

                //planBadgeAdapter.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable s) {
                hideKeyboard();
            }
        });


        //loadData();
    }

    public void showMap(View v){
        Intent intent = new Intent(CustomerOperationsActivity.this,CustomerOperationsMapActivity.class);
        startActivity(intent);
    }

    private void setupTabs() {
        tabHost.setup();

        TabHost.TabSpec tabJourneyPlan = tabHost.newTabSpec(JP);
        tabJourneyPlan.setIndicator(JP);
        tabJourneyPlan.setContent(R.id.journeyPlanView);



        tabHost.addTab(tabJourneyPlan);

        TabHost.TabSpec tabAllCustomer = tabHost.newTabSpec(ALLCUST);

        tabAllCustomer.setIndicator(ALLCUST);
        tabAllCustomer.setContent(R.id.allCustomersView);

        tabHost.addTab(tabAllCustomer);


        TextView tv = (TextView)tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tv.setTextColor(Color.parseColor("#ffffff"));

        tabHost.getTabWidget().setDividerDrawable(R.color.black);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {


                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                     // unselected
                    TextView tv = (TextView)tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                    tv.setTextColor(Color.parseColor("#000000"));
                }

                // selected
                TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                tv.setTextColor(Color.parseColor("#ffffff"));
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(swipeDetector.swipeDetected()){
            if(swipeDetector.getAction() == SwipeDetector.Action.RL) {
                Toast.makeText(getApplicationContext(),"RTL",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),"Something",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Object object = parent.getAdapter().getItem(position);
            if(object instanceof Customer){
              //  Toast.makeText(getApplicationContext(),((Customer) object).getCustomerName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),ShopStatusActivity.class);
                startActivity(intent);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

   /* public void loadData(){
//        planBadgeAdapter.clear();

        for (int i = 0; i < 10; i++) {
//            Customer customer = createCustomerData(i);

//            journeyList.add(customer);

            CustomerData customerData=createCustomerData(i);
            dataArrayList.add(customerData);
        }

    }*/

    public static CustomerData createCustomerData(int index){
        CustomerData customer=new CustomerData();
        int i=100+index;
        customer.setId(String.valueOf(i));
        customer.setName("ankit");
        customer.setAddress("rajkot");
        return customer;
    }

    private class loadJourneyPlan extends AsyncTask<String, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            //Logic to fetch Data
            //loadData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }

            dataAdapter = new DataAdapter(CustomerOperationsActivity.this,dataArrayList);
            listView.setAdapter(dataAdapter);
//            planBadgeAdapter= new JourneyPlanBadgeAdapter(CustomerOperationsActivity.this, journeyList,listView);

            setupTabs();
           // super.onPostExecute(aVoid);
        }
    }

    public  void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // Ignore exceptions if any
            //Log.e("KeyBoardUtil", e.toString(), e);
        }
    }
}
