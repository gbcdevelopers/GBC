package gbc.sa.vansales.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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

import gbc.sa.vansales.Fragment.VisitAllFragment;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DataAdapter;
import gbc.sa.vansales.adapters.PagerAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.models.CustomerData;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.SwipeDetector;

/**
 * Created by eheuristic on 12/2/2016.
 */

public class SelectCustomerActivity  extends AppCompatActivity{


    ViewPager viewPager;
    TabLayout tabLayout;

    ImageView iv_back;
    TextView tv_top_header;



    DataAdapter dataAdapter;
    ArrayList<CustomerData> dataArrayList;



    ImageView toolbar_iv_back;
    ImageView iv_search;
    EditText et_search;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_trip);

        viewPager=(ViewPager)findViewById(R.id.pager);
        tabLayout=(TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Visit List"));
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);



        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Select Customer");



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        dataArrayList =new ArrayList<>();
        loadData();




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
                VisitAllFragment.dataAdapter.getFilter().filter(s.toString());

                //planBadgeAdapter.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });


        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(),"s");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    public  void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // Ignore exceptions if any
            Log.e("KeyBoardUtil", e.toString(), e);
        }
    }












    public void loadData(){
//        planBadgeAdapter.clear();

        for (int i = 0; i < 10; i++) {
//            Customer customer = createCustomerData(i);

//            journeyList.add(customer);

            CustomerData customerData=createCustomerData(i);
            dataArrayList.add(customerData);

        }
//        dataAdapter = new DataAdapter(SelectCustomerActivity.this,dataArrayList);
        Const.dataArrayList=dataArrayList;

    }

    public static CustomerData createCustomerData(int index){
        CustomerData customer=new CustomerData();
        int i=100+index;
        customer.setId(String.valueOf(i));
        customer.setName("ankit");
        customer.setAddress("rajkot");
        return customer;
    }












}