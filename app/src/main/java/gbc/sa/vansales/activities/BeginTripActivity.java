package gbc.sa.vansales.activities;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import gbc.sa.vansales.Fragment.BeginDayFragment;
import gbc.sa.vansales.Fragment.MessageFragment;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.MessageListAdapter;
import gbc.sa.vansales.adapters.PagerAdapter;

/**
 * Created by eheuristic on 12/2/2016.
 */

public class BeginTripActivity extends AppCompatActivity  {


    ViewPager viewPager;
    TabLayout tabLayout;

    ImageView iv_back;
    TextView tv_top_header;
    ImageView iv_refresh;

    FloatingActionButton floatingActionButton;
    int tabPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_trip);

        viewPager=(ViewPager)findViewById(R.id.pager);
        tabLayout=(TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("BEGIN DAY"));
        tabLayout.addTab(tabLayout.newTab().setText("MESSAGE"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        floatingActionButton=(FloatingActionButton)findViewById(R.id.float_map);
        floatingActionButton.setVisibility(View.GONE);

        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);
        iv_refresh=(ImageView) findViewById(R.id.iv_refresh);


        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("GBC SFA");
        iv_refresh.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabPosition == 1) {
                    MessageFragment.adapter.notifyDataSetChanged();
                }
            }
        });




        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(),"b");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tabPosition=tab.getPosition();
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

