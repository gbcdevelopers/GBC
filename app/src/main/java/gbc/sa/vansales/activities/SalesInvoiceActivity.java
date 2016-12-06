package gbc.sa.vansales.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DataAdapter;
import gbc.sa.vansales.adapters.PagerAdapter;
import gbc.sa.vansales.models.CustomerData;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class SalesInvoiceActivity extends AppCompatActivity {


    ViewPager viewPager;
    TabLayout tabLayout;

    ImageView iv_back;
    TextView tv_top_header;



    ImageView toolbar_iv_back;
    ImageView iv_search;
    EditText et_search;

    int tab_position;
    FloatingActionButton button;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_begin_trip);

        button=(FloatingActionButton)findViewById(R.id.float_map);
        button.setVisibility(View.GONE);

        viewPager=(ViewPager)findViewById(R.id.pager);
        tabLayout=(TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Sales"));
        tabLayout.addTab(tabLayout.newTab().setText("Foc"));
        tabLayout.addTab(tabLayout.newTab().setText("B.return"));
        tabLayout.addTab(tabLayout.newTab().setText("Foc"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);



        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Sales Invoice");



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        tv_top_header=(TextView)findViewById(R.id.tv_top_header);
        if (tv_top_header != null) {
            tv_top_header.setVisibility(View.VISIBLE);
            tv_top_header.setText("Sales Invoice");
        }


        toolbar_iv_back=(ImageView)findViewById(R.id.toolbar_iv_back) ;
        if (toolbar_iv_back != null) {
            toolbar_iv_back.setVisibility(View.VISIBLE);
        }

        iv_search=(ImageView)findViewById(R.id.iv_search);
        if (iv_search != null) {
            iv_search.setVisibility(View.VISIBLE);
        }

       /* iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_search.setVisibility(View.GONE);
                et_search.setVisibility(View.VISIBLE);
                toolbar_iv_back.setVisibility(View.GONE);
                tv_top_header.setVisibility(View.GONE);

            }
        });*/




        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(),"sales");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tab_position=tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}
