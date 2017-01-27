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

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ItemListAdapter;
import gbc.sa.vansales.adapters.PriceListAdapter;
import gbc.sa.vansales.models.ItemList;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
public class PriceListActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    View view1;

    ListView listView;

    ArrayList<ItemList> arrayList = new ArrayList<>();
    PriceListAdapter adapter;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list);
        loadingSpinner = new LoadingSpinner(this);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Price List");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView)findViewById(R.id.list_price_list);
        adapter = new PriceListAdapter(PriceListActivity.this,arrayList);
        listView.setAdapter(adapter);
        new loadPricing().execute();
        //getData();
    }
    public class loadPricing extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String>map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO,"");
            map.put(db.KEY_MATERIAL_NO,"");
            map.put(db.KEY_AMOUNT,"");
            map.put(db.KEY_CURRENCY,"");
            map.put(db.KEY_PRIORITY,"");
            map.put(db.KEY_DRIVER,"");
            HashMap<String,String>filter = new HashMap<>();
            Cursor cursor = db.getData(db.PRICING,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                setPricing(cursor);
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

    private void setPricing(Cursor cursor){
        do{
            ItemList model = new ItemList();
            model.setItem_number(StringUtils.stripStart(cursor.getString(cursor.getColumnIndex(db.KEY_CUSTOMER_NO)),"0"));
            model.setItem_des(StringUtils.stripStart(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)),"0"));
            model.setCase_price(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
            arrayList.add(model);
        }
        while (cursor.moveToNext());
    }

}
