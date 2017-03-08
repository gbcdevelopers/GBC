package gbc.sa.vansales.activities;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ShopStatusBadgeAdapter;
import gbc.sa.vansales.models.ShopStatus;
/**
 * Created by Rakshit on 16-Nov-16.
 */
public class ShopStatusActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ArrayList<ShopStatus> shopStatusList;
    private ArrayAdapter<ShopStatus> adapter;
     double latitude;
     double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shopStatusList = new ArrayList<>();

        adapter = new ShopStatusBadgeAdapter(this, shopStatusList);

        ListView listView = (ListView) findViewById(R.id.shopStatusList);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        new loadShopStatus().execute();
        compareCoordinates(latitude,longitude);
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

    void compareCoordinates(double latitude,double longitude){
        boolean isMatch = false;
        if(!isMatch){
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.alert_dialog_coordinates_mismatch);

            Button btn_positive = (Button)dialog.findViewById(R.id.btn_alert_dialog_coordinates_mismatch_yes);
            btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Button btn_negative = (Button)dialog.findViewById(R.id.btn_alert_dialog_coordinates_mismatch_no);
            btn_negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout item_view = (LinearLayout) view;
        final Object object = parent.getAdapter().getItem(position);
        final RadioButton itemcheck = (RadioButton) item_view.findViewById(R.id.radio_btn_shopstatus);
        itemcheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Log.e("", "" + ((ShopStatus) object).getStatusText());
            }
        });

        if (itemcheck.isChecked()) {
            itemcheck.setChecked(true);
        } else {
            itemcheck.setChecked(false);
        }

        itemcheck.setChecked(true);
    }

    private void loadData(){
        adapter.clear();
        for (int i = 0; i < 5; i++) {
            ShopStatus status = createStatusData(i);
            shopStatusList.add(status);
        }
    }

    public static ShopStatus createStatusData(int index){
        ShopStatus status = new ShopStatus();
        switch(index){
            case 0:
                status.setStatusID("1");
                status.setStatusText("Shop is Open");
            break;
            case 1:
                status.setStatusID("2");
                status.setStatusText("Not Visited");
            break;
            case 2:
                status.setStatusID("3");
                status.setStatusText("Shop not Found");
            break;
            case 3:
                status.setStatusID("4");
                status.setStatusText("Shop temporarily closed");
            break;
            case 4:
                status.setStatusID("5");
                status.setStatusText("Shop Permanently Closed");

            break;
        }

        return status;
    }

    private class loadShopStatus extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            //Logic to fetch Data
            loadData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //Log.e("I cam here", "Here");
            adapter = new ShopStatusBadgeAdapter(ShopStatusActivity.this, shopStatusList);
            adapter.notifyDataSetChanged();
            // super.onPostExecute(aVoid);
        }
    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }
}
