package gbc.sa.vansales.activities;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerStatusAdapter;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerStatus;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
/**
 * Created by Rakshit on 20-Jan-17.
 */
public class SelectCustomerStatus extends AppCompatActivity {
    private DatabaseHandler db = new DatabaseHandler(this);
    private ArrayList<CustomerStatus> arrayList = new ArrayList<>();
    private ArrayAdapter<CustomerStatus> adapter;
    private ListView listView;
    ImageView iv_back;
    TextView tv_top_header;
    Customer object;
    String from;
    LoadingSpinner loadingSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_customer_status);
        loadingSpinner = new LoadingSpinner(this);
        Intent i = this.getIntent();
        from=i.getStringExtra("msg");
        object = (Customer) i.getParcelableExtra("headerObj");

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        tv_top_header.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.select_customer_status));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new loadCustomerStatus().execute();

        listView = (ListView)findViewById(R.id.statusList);
        adapter = new CustomerStatusAdapter(this,arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectCustomerStatus.this, CustomerDetailActivity.class);
                intent.putExtra("headerObj", object);
                intent.putExtra("msg", from);
                startActivity(intent);

            }
        });
    }

    private void updateCustomerVisitList(){

    }

    private class loadCustomerStatus extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String>map = new HashMap<>();
            map.put(db.KEY_REASON_TYPE,"");
            map.put(db.KEY_REASON_CODE,"");
            map.put(db.KEY_REASON_DESCRIPTION,"");
            HashMap<String,String>filter = new HashMap<>();
            filter.put(db.KEY_REASON_TYPE, App.VisitReasons);
            Cursor cursor = db.getData(db.REASONS,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                setCustomerStatus(cursor);
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

    private void setCustomerStatus(Cursor cursor){
        try{
            Cursor c = cursor;
            do{
                CustomerStatus status = new CustomerStatus();
                status.setReasonCode(c.getString(c.getColumnIndex(db.KEY_REASON_CODE)));
                status.setReasonDescription(c.getString(c.getColumnIndex(db.KEY_REASON_DESCRIPTION)));
                arrayList.add(status);
            }
            while (c.moveToNext());
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }
}
