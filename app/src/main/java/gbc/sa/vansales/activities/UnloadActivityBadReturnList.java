package gbc.sa.vansales.activities;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.*;
import gbc.sa.vansales.models.Reasons;
import gbc.sa.vansales.models.Unload;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Rakshit on 21-Jan-17.
 */
public class UnloadActivityBadReturnList extends AppCompatActivity {
    ListView listView;
    DatabaseHandler db = new DatabaseHandler(this);
    ArrayList<Unload>arrayList = new ArrayList<>();
    ArrayAdapter<Unload>adapter;
    private String context;
    LoadingSpinner loadingSpinner;
    ArrayList<Reasons> reasonsList = new ArrayList<>();
    FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unload_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingSpinner = new LoadingSpinner(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        new loadReasons().execute();
        Intent i = this.getIntent();
        context = i.getExtras().getString("context");
        setTitle(getString(R.string.bad_return));

        listView = (ListView) findViewById(R.id.listView);
        adapter = new UnloadBadReasonAdapter(UnloadActivityBadReturnList.this, arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UnloadActivityBadReturnList.this,UnloadDetailActivity.class);
                //Log.e("Code",arrayList.get(position).getReasonCode());
                intent.putExtra("code",arrayList.get(position).getReasonCode());
                intent.putExtra("context","badreturn");
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class loadData extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_ITEM_NO, "");
            map.put(db.KEY_MATERIAL_DESC1, "");
            map.put(db.KEY_REASON_CODE,"");
            map.put(db.KEY_MATERIAL_NO, "");
            map.put(db.KEY_MATERIAL_GROUP, "");
            map.put(db.KEY_CASE, "");
            map.put(db.KEY_UNIT, "");
            map.put(db.KEY_UOM, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_REASON_TYPE, App.BAD_RETURN);
            Cursor cursor = db.getData(db.RETURNS, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                setData(cursor);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            adapter.notifyDataSetChanged();

        }
    }

    private void setData(Cursor cursor){
        Cursor c = cursor;

        ArrayList<String>temp = new ArrayList<>();
        do{
            Unload unload = new Unload();

            if(!temp.contains(c.getString(c.getColumnIndex(db.KEY_REASON_CODE)))){
                temp.add(c.getString(c.getColumnIndex(db.KEY_REASON_CODE)));
                unload.setReasonCode(c.getString(c.getColumnIndex(db.KEY_REASON_CODE)));
                unload.setName(UrlBuilder.decodeString(reasonText(c.getString(c.getColumnIndex(db.KEY_REASON_CODE)))));
                arrayList.add(unload);
            }
        }
        while (c.moveToNext());
    }

    public String reasonText(String reasonCode){
        String test = "";
        for(Reasons reason:reasonsList){
            if(reason.getReasonID().equals(reasonCode)){
                test =  reason.getReasonDescription();
                break;
            }
        }
        //Log.e("Reason","" + test);
        return test;
    }


    public class loadReasons extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_REASON_TYPE, "");
            map.put(db.KEY_REASON_DESCRIPTION, "");
            map.put(db.KEY_REASON_CODE, "");
            HashMap<String, String> filter = new HashMap<>();
            Cursor cursor = db.getData(db.REASONS, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Reasons reasons = new Reasons();
                    reasons.setReasonID(cursor.getString(cursor.getColumnIndex(db.KEY_REASON_CODE)));
                    reasons.setReasonType(cursor.getString(cursor.getColumnIndex(db.KEY_REASON_TYPE)));
                    reasons.setReasonDescription(cursor.getString(cursor.getColumnIndex(db.KEY_REASON_DESCRIPTION)));
                    reasonsList.add(reasons);
                }
                while (cursor.moveToNext());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            new loadData().execute();
        }
    }
}
