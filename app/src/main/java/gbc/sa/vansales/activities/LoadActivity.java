package gbc.sa.vansales.activities;
import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.R;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import gbc.sa.vansales.adapters.LoadDeliveryHeaderAdapter;
import gbc.sa.vansales.models.LoadDeliveryHeader;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Settings;
public class LoadActivity extends AppCompatActivity {
    private static final String TRIP_ID = "ITripId";
    public static Object o;
    public static LoadConstants fullObject;
    ArrayList<LoadDeliveryHeader> loadDeliveryHeaders;
    //LoadDeliveryHeaderAdapter adapter;
    ListView lv;
    DatabaseHandler db = new DatabaseHandler(this);
    private ArrayAdapter<LoadDeliveryHeader> adapter;
    //    TextView status;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        setTitle(getString(R.string.load));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadDeliveryHeaders = new ArrayList<>();
        // searchResults= GetSearchResults();
        adapter = new LoadDeliveryHeaderAdapter(this, loadDeliveryHeaders);
        lv = (ListView) findViewById(R.id.srListView);
        lv.setAdapter(adapter);
        new fetchLoads(Settings.getString(TRIP_ID));
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                LoadDeliveryHeader load = loadDeliveryHeaders.get(position);
                Intent i = new Intent(LoadActivity.this, LoadSummaryActivity.class);
                i.putExtra("headerObj", load);
                startActivityForResult(i, 10);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      //  Toast.makeText(getApplicationContext(), "Load Checked!", Toast.LENGTH_SHORT).show();
        lv.setAdapter(new LoadDeliveryHeaderAdapter(LoadActivity.this, loadDeliveryHeaders));
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backButtonAction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void backButtonAction() {
        Intent i = new Intent(LoadActivity.this, DashboardActivity.class);
        startActivity(i);
    }
    private ArrayList<LoadConstants> GetSearchResults() {
        ArrayList<LoadConstants> results = new ArrayList<LoadConstants>();
        LoadConstants sr = new LoadConstants();
        sr.setName("Load # 1");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);
        sr = new LoadConstants();
        sr.setName("Load # 2");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);
        sr = new LoadConstants();
        sr.setName("Load # 3");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);
        sr = new LoadConstants();
        sr.setName("Load #4");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);
        sr = new LoadConstants();
        sr.setName("Load # 5");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);
        sr = new LoadConstants();
        sr.setName("Load # 6");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);
        sr = new LoadConstants();
        sr.setName("Load 7");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);
        return results;
    }
    public void setLoadDelivery(Cursor loadCursor) {
        Cursor cursor = loadCursor;
        cursor.moveToFirst();
        Log.e("cursor", "" + cursor.getCount());
        do {
            LoadDeliveryHeader loadDeliveryHeader = new LoadDeliveryHeader();
            loadDeliveryHeader.setDeliveryNo(cursor.getString(cursor.getColumnIndex(db.KEY_DELIVERY_NO)));
            loadDeliveryHeader.setLoadingDate(cursor.getString(cursor.getColumnIndex(db.KEY_DELIVERY_DATE)));
            loadDeliveryHeader.setLoadVerified(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(db.KEY_IS_VERIFIED))));
            //loadDeliveryHeader.setAvailableLoad("1");
            loadDeliveryHeaders.add(loadDeliveryHeader);
        }
        while (cursor.moveToNext());
        Log.e("loadDeliver", "" + loadDeliveryHeaders.size());
        adapter.notifyDataSetChanged();
        Log.e("adapter", "" + adapter.getCount());
    }
    private class fetchLoads extends AsyncTask<Void, Void, Void> {
        String tripId;
        private fetchLoads(String tripId) {
            this.tripId = tripId;
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try{
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_TRIP_ID, "");
                map.put(db.KEY_DELIVERY_NO, "");
                map.put(db.KEY_DELIVERY_DATE, "");
                map.put(db.KEY_DELIVERY_TYPE, "");
                map.put(db.KEY_IS_VERIFIED, "");
                HashMap<String, String> filters = new HashMap<>();
                filters.put(db.KEY_TRIP_ID, Settings.getString(TRIP_ID));
                // filters.put(db.KEY_IS_VERIFIED,"false");
                Cursor cursor = db.getData(db.LOAD_DELIVERY_HEADER, map, filters);
                if (cursor.getCount() > 0) {
                    setLoadDelivery(cursor);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                db.close();
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