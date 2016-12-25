package gbc.sa.vansales.activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.LoadSummaryBadgeAdapter;
import gbc.sa.vansales.adapters.LoadVerifyBadgeAdapter;
import gbc.sa.vansales.models.LoadSummary;
/**
 * Created by Rakshit on 19-Nov-16.
 */
public class LoadVerifyActivity extends AppCompatActivity {
    ArrayList<LoadSummary> loadSummaryList;
    ArrayAdapter<LoadSummary>adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_verify);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        ArrayList<LoadSummary> dataNew = intent.getParcelableArrayListExtra("loadSummary");
        ArrayList<LoadSummary> dataOld = intent.getParcelableArrayListExtra("loadSummaryOld");
        // Log.e("****",""+dataOld.size());

        loadSummaryList = new ArrayList<>();
        //  loadSummaryList = dataNew;
        adapter = new LoadVerifyBadgeAdapter(this, loadSummaryList);
        loadSummaryList = generateData(dataNew,dataOld);
        for(int i=0;i<loadSummaryList.size();i++){
            System.out.println(loadSummaryList.get(i).getQuantityCases());
        }

        listView = (ListView)findViewById(R.id.loadSummaryList);
        listView.setAdapter(adapter);

    }

    public void confirmLoad(View v){
        Intent intent = new Intent(LoadVerifyActivity.this,OdometerPopupActivity.class);
        startActivity(intent);
    }

    public void cancel(View v){
        Intent intent = new Intent(LoadVerifyActivity.this,LoadSummaryActivity.class);
        startActivity(intent);
    }

    public ArrayList<LoadSummary> generateData(ArrayList<LoadSummary>dataNew,ArrayList<LoadSummary>dataOld){

        for(int i=0;i<dataNew.size();i++){
            LoadSummary loadSummary = new LoadSummary();
            loadSummary.setItemCode(dataNew.get(i).getItemCode());
            loadSummary.setItemDescription(dataNew.get(i).getItemDescription());
            if(dataNew.get(i).getItemCode().equals(dataOld.get(i).getItemCode()))
            {
                // Log.e("I m here","here");
                loadSummary.setQuantityCases(dataOld.get(i).getQuantityCases()+"|"+dataNew.get(i).getQuantityCases());
            }
            if(dataNew.get(i).getItemCode().equals(dataOld.get(i).getItemCode())) {
                loadSummary.setQuantityUnits(dataOld.get(i).getQuantityUnits() + "|" + dataNew.get(i).getQuantityUnits());
            }
            loadSummaryList.add(loadSummary);

        }


        adapter.notifyDataSetChanged();
        return loadSummaryList;
    }

}
