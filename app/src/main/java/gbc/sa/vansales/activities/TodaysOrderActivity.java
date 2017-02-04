package gbc.sa.vansales.activities;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.PriceListAdapter;
import gbc.sa.vansales.adapters.TodaysOrderAdapter;
import gbc.sa.vansales.models.ItemList;
import gbc.sa.vansales.models.TodaysOrder;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
public class TodaysOrderActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    View view1;

    ListView listView;
    ArrayList<TodaysOrder> arrayList = new ArrayList<>();
    TodaysOrderAdapter adapter;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_order);
        loadingSpinner = new LoadingSpinner(this);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.today_order));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView)findViewById(R.id.list_todays_order_list);
        adapter = new TodaysOrderAdapter(TodaysOrderActivity.this,arrayList);
        listView.setAdapter(adapter);
        new loadData().execute();
       // getData();
    }

    public void getData()
    {


        for(int i=0;i<10;i++)
        {
            TodaysOrder model = new TodaysOrder();
            model.setCustomer("Customer "+i);
            model.setOrderNo("Order "+ i);
            model.setPrice("100"+ i);

            arrayList.add(model);
        }


    }

    public class loadData extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String>map = new HashMap<>();
            map.put(db.KEY_TIME_STAMP,"");
            map.put(db.KEY_CUSTOMER_NO,"");
            map.put(db.KEY_ORDER_ID,"");
            map.put(db.KEY_PRICE,"");
            HashMap<String,String > filter = new HashMap<>();
            filter.put(db.KEY_ACTIVITY_TYPE, App.ACTIVITY_ORDER);
            Cursor cursor = db.getData(db.DAYACTIVITY,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                setTodaysActivity(cursor);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()||loadingSpinner!=null){
                loadingSpinner.hide();
            }
            adapter.notifyDataSetChanged();

        }
    }

    public void setTodaysActivity(Cursor cursor){
        Cursor activityCursor = cursor;
        do{
            TodaysOrder todaysOrder = new TodaysOrder();
            todaysOrder.setOrderNo(activityCursor.getString(activityCursor.getColumnIndex(db.KEY_ORDER_ID)));
            todaysOrder.setCustomer(activityCursor.getString(activityCursor.getColumnIndex(db.KEY_CUSTOMER_NO)));
            todaysOrder.setPrice(activityCursor.getString(activityCursor.getColumnIndex(db.KEY_PRICE)));
            arrayList.add(todaysOrder);
        }
        while (activityCursor.moveToNext());
    }
}
