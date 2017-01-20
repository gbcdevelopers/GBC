package gbc.sa.vansales.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.AsyncTask;
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

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.Fragment.BeginDayFragment;
import gbc.sa.vansales.Fragment.MessageFragment;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.MessageListAdapter;
import gbc.sa.vansales.adapters.PagerAdapter;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by eheuristic on 12/2/2016.
 */

public class BeginTripActivity extends AppCompatActivity  {
    private LoadingSpinner loadingSpinner;
    private static final String COLLECTION_NAME = "VisitListSet";
    private static final String TRIP_ID = "ITripId";
    public boolean hello = false;
    ViewPager viewPager;
    TabLayout tabLayout;

    ImageView iv_back;
    TextView tv_top_header;
    ImageView iv_refresh;

    FloatingActionButton floatingActionButton;
    FloatingActionButton addCustomer;
    int tabPosition;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_trip);

        viewPager=(ViewPager)findViewById(R.id.pager);
        tabLayout=(TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.beginDay).toUpperCase()));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.message).toUpperCase()));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        loadingSpinner = new LoadingSpinner(this);
        new LoadTripActivityData();
        floatingActionButton=(FloatingActionButton)findViewById(R.id.float_map);
        addCustomer = (FloatingActionButton)findViewById(R.id.addCustomer);
        floatingActionButton.setVisibility(View.GONE);
        addCustomer.setVisibility(View.GONE);

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

    private void setUpTabs(Bundle bundle){
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(),"b",bundle);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private class LoadTripActivityData extends AsyncTask<Void,Void,Void> {
        private String url;
        private JSONObject data = new JSONObject();

        private LoadTripActivityData() {
            this.data = new JSONObject();
            HashMap<String, String> map = new HashMap<>();
            map.put(TRIP_ID,"Y000012000000000");
          //  map.put(TRIP_ID, Settings.getString(TRIP_ID));
            this.url = UrlBuilder.build(COLLECTION_NAME, null, map);
            execute();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingSpinner.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                //Login the user
              //  this.data =  IntegrationService.getService(BeginTripActivity.this, this.url);
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_TRIP_ID,"");
                map.put(db.KEY_VISITLISTID,"");
                map.put(db.KEY_ROUTE ,"");
                map.put(db.KEY_DRIVER  ,"");
                map.put(db.KEY_TRUCK  ,"");
                map.put(db.KEY_PS_DATE  ,"");
                map.put(db.KEY_AS_DATE ,"");
                map.put(db.KEY_TOUR_TYPE ,"");
                map.put(db.KEY_CREATED_TIME ,"");
                map.put(db.KEY_CREATED_BY  ,"");
                map.put(db.KEY_SETTLED_BY   ,"");
                map.put(db.KEY_SETTLED_BY   ,"");
                map.put(db.KEY_UP_STATUS   ,"");
                map.put(db.KEY_LOADS  ,"");

                HashMap<String, String> filters = new HashMap<>();

                Cursor cursor = db.getData(db.TRIP_HEADER,map,filters);
                if(cursor.getCount()>0){
                    cursor.moveToFirst();
                }
                int totalColumn = cursor.getColumnCount();
                for(int i=0;i<totalColumn;i++){
                    try{
                        this.data.put(cursor.getColumnName(i),cursor.getString(i));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally {
                db.close();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
                Bundle bundle = new Bundle();
                bundle.putString("data", this.data.toString());
                BeginDayFragment fragobj = new BeginDayFragment();
                fragobj.setArguments(bundle);
                setUpTabs(bundle);
            }
        }
    }


}

