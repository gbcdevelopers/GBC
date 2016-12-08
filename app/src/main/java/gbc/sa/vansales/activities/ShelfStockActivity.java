package gbc.sa.vansales.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import gbc.sa.vansales.Fragment.AllCustomerFragment;
import gbc.sa.vansales.Fragment.ShelfFragment;
import gbc.sa.vansales.Fragment.VisitAllFragment;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.PagerAdapter;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class ShelfStockActivity extends AppCompatActivity {


    ViewPager viewPager;
    TabLayout tabLayout;

    ImageView iv_back;
    TextView tv_top_header;



    ImageView toolbar_iv_back,iv_add;
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

        et_search=(EditText)findViewById(R.id.et_search_customer);



        viewPager=(ViewPager)findViewById(R.id.pager);
        tabLayout=(TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Shelf"));
        tabLayout.addTab(tabLayout.newTab().setText("Store"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);
        iv_add=(ImageView)findViewById(R.id.iv_refresh);
        iv_add.setVisibility(View.VISIBLE);
        iv_add.setImageResource(R.drawable.ic_add_black_24dp);



        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Shelf Stock");



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





        toolbar_iv_back=(ImageView)findViewById(R.id.toolbar_iv_back) ;
        if (toolbar_iv_back != null) {
            toolbar_iv_back.setVisibility(View.VISIBLE);
        }

        iv_search=(ImageView)findViewById(R.id.iv_search2);
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
                iv_add.setVisibility(View.GONE);

            }
        });

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
                        iv_add.setVisibility(View.VISIBLE);

                        return true;
                    }
                }
                return false;
            }
        });




        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.v("addtext","change");


                    ShelfFragment.adapter.getFilter().filter(s.toString());



                //planBadgeAdapter.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });





        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(),"shelf");
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
