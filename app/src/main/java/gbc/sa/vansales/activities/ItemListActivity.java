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
    ItemListAdapter adapter;
    FloatingActionButton printVanStock;
    ArrayList<ItemList> arraylist = new ArrayList<>();
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
        tv_top_header.setText(getString(R.string.item_list_lbl));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new loadItems().execute();
        adapter = new ItemListAdapter(this, arraylist);
        list = (ListView) findViewById(R.id.listview);
    }
    public class loadItems extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_ARTICLE_NO, "");
            map.put(db.KEY_MATERIAL_NO, "");
            map.put(db.KEY_MATERIAL_DESC1, "");
            map.put(db.KEY_BASE_UOM, "");
            HashMap<String, String> filter = new HashMap<>();
            Cursor cursor = db.getData(db.ARTICLE_HEADER, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                setLoadItems(cursor);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (loadingSpinner.isShowing()) {
                loadingSpinner.hide();
            }
            adapter.notifyDataSetChanged();
            list.setAdapter(adapter);
        }
    }
    private void setLoadItems(Cursor cursor) {
        try{
            do {
                ItemList itemList = new ItemList();
                itemList.setItem_number(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                itemList.setItem_des(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                itemList.setCase_price(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)));
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_NUMERATOR, "");
                map.put(db.KEY_DENOMINATOR, "");
                HashMap<String, String> filter = new HashMap<>();
                filter.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                Cursor c = db.getData(db.ARTICLE_UOM, map, filter);
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    itemList.setUpc(c.getString(c.getColumnIndex(db.KEY_NUMERATOR)) + "/" + c.getString(c.getColumnIndex(db.KEY_DENOMINATOR)));
                }
                arraylist.add(itemList);
            }
            while (cursor.moveToNext());
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
