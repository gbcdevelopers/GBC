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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import gbc.sa.vansales.R;


public class LoadActivity extends AppCompatActivity
{
    public static ArrayList<LoadConstants> searchResults;
    public static ListAdapter adapter;
    public static ListView lv;

    public  static int count;

    public static Object o;
    public static LoadConstants fullObject;

    //    TextView status;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

//        status=(TextView)findViewById(R.id.status);

        setTitle("Load");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchResults= GetSearchResults();

        count=0;


        adapter=new SingleLoadActivity(this,searchResults);



        lv = (ListView) findViewById(R.id.srListView);


        lv.setAdapter(adapter);



        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                o = lv.getItemAtPosition(position);
                fullObject = (LoadConstants) o;

                // lv.invalidateViews();



                //Toast.makeText(LoadActivity.this, "You have chosen: " + " " + fullObject.getloadNumber(), Toast.LENGTH_LONG).show();

//                status.setText("Checked");

                Intent i =new Intent(LoadActivity.this, LoadSummaryActivity.class);
                i.putExtra("1",position);
                startActivityForResult(i,10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getApplicationContext(),"Load Checked!",Toast.LENGTH_SHORT).show();
        lv.setAdapter(new SingleLoadActivity(LoadActivity.this,searchResults));

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


    private void backButtonAction()
    {
        Intent i=new Intent(LoadActivity.this,DashboardActivity.class);
        startActivity(i);
    }

    private ArrayList<LoadConstants> GetSearchResults(){
        ArrayList<LoadConstants> results = new ArrayList<LoadConstants>();

        LoadConstants sr = new LoadConstants();

        sr.setloadNumber("Load # 1");
        sr.setloadDate("Loading Date");
        sr.setavailableLoad("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);


        sr = new LoadConstants();
        sr.setloadNumber("Load # 2");
        sr.setloadDate("Loading Date");
        sr.setavailableLoad("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);

        sr = new LoadConstants();
        sr.setloadNumber("Load # 3");
        sr.setloadDate("Loading Date");
        sr.setavailableLoad("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);

        sr = new LoadConstants();
        sr.setloadNumber("Load #4");
        sr.setloadDate("Loading Date");
        sr.setavailableLoad("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);

        sr = new LoadConstants();
        sr.setloadNumber("Load # 5" );
        sr.setloadDate("Loading Date");
        sr.setavailableLoad("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);

        sr = new LoadConstants();
        sr.setloadNumber("Load # 6");
        sr.setloadDate("Loading Date");
        sr.setavailableLoad("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);

        sr = new LoadConstants();
        sr.setloadNumber("Load 7");
        sr.setloadDate("Loading Date");
        sr.setavailableLoad("Available Load");
        sr.setStatus("UnChecked");
        results.add(sr);

        return results;
    }
}