package gbc.sa.vansales.activities;

import java.util.ArrayList;

import gbc.sa.vansales.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import gbc.sa.vansales.R;


public class LoadActivity extends AppCompatActivity
{
    //    TextView status;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

//        status=(TextView)findViewById(R.id.status);

        setTitle("Load");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<LoadConstants> searchResults = GetSearchResults();

        final ListView lv = (ListView) findViewById(R.id.srListView);
        lv.setAdapter(new SingleLoadActivity(this,searchResults));


        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv.getItemAtPosition(position);
                LoadConstants fullObject = (LoadConstants) o;

                //Toast.makeText(LoadActivity.this, "You have chosen: " + " " + fullObject.getName(), Toast.LENGTH_LONG).show();

//                status.setText("Checked");

                Intent i =new Intent(LoadActivity.this, LoadSummaryActivity.class);
                startActivity(i);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private ArrayList<LoadConstants> GetSearchResults(){
        ArrayList<LoadConstants> results = new ArrayList<LoadConstants>();

        LoadConstants sr = new LoadConstants();

        sr.setName("Load #");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);


        sr = new LoadConstants();
        sr.setName("Load #");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);

        sr = new LoadConstants();
        sr.setName("Load #");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);

        sr = new LoadConstants();
        sr.setName("Load #");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);

        sr = new LoadConstants();
        sr.setName("Load #");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);

        sr = new LoadConstants();
        sr.setName("Load #");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);

        sr = new LoadConstants();
        sr.setName("Load #");
        sr.setCityState("Loading Date");
        sr.setPhone("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);

        return results;
    }
}