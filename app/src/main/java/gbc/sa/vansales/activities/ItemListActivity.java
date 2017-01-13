package gbc.sa.vansales.activities;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ItemListAdapter;
import gbc.sa.vansales.adapters.VanStockBadgeAdapter;
import gbc.sa.vansales.models.ItemList;
import gbc.sa.vansales.models.VanStock;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
public class ItemListActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    View view1;

    ListView list;
    VanStockBadgeAdapter adapter;
    FloatingActionButton printVanStock;
    ArrayList<VanStock> arraylist = new ArrayList<>();
    LoadingSpinner loadingSpinner;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        loadingSpinner = new LoadingSpinner(this);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Items List");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new loadItems().execute();
        adapter = new VanStockBadgeAdapter(this,arraylist);
        list = (ListView) findViewById(R.id.listview);
        list.setAdapter(adapter);

    }

    public class loadItems extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_ITEM_NO,"");
            map.put(db.KEY_MATERIAL_DESC1,"");
            map.put(db.KEY_RESERVED_QTY_CASE,"");
            map.put(db.KEY_RESERVED_QTY_UNIT,"");
            map.put(db.KEY_REMAINING_QTY_CASE,"");
            map.put(db.KEY_REMAINING_QTY_UNIT,"");

            HashMap<String,String> filter = new HashMap<>();

            Cursor cursor = db.getData(db.VAN_STOCK_ITEMS,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                setLoadItems(cursor);
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

    private void setLoadItems(Cursor cursor){
        do{
            VanStock vanStock = new VanStock();
            vanStock.setItem_code(cursor.getString(cursor.getColumnIndex(db.KEY_ITEM_NO)));
            vanStock.setItem_description(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
            vanStock.setItem_case(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_CASE)));
            vanStock.setItem_units(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_UNIT)));
            arraylist.add(vanStock);
        }
        while (cursor.moveToNext());
    }
}
