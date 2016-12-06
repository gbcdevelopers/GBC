package gbc.sa.vansales.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.PagerAdapter;

/**
 * Created by eheuristic on 12/6/2016.
 */

public class CaptureImageActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    ImageView iv_back;
    TextView tv_top_header;

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_trip);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.float_map);
        floatingActionButton.setVisibility(View.GONE);

        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);


        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Capture Image");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        viewPager=(ViewPager)findViewById(R.id.pager);
        tabLayout=(TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Before"));
        tabLayout.addTab(tabLayout.newTab().setText("After"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(),"capture");
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
}
