package gbc.sa.vansales.activities;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.PriceListAdapter;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.ItemList;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.UrlBuilder;
public class PriceListCustomerActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    View view1;
    ListView listView;
    ArrayList<ItemList> arrayList = new ArrayList<>();
    PriceListAdapter adapter;
    Customer object;
    public ArrayList<ArticleHeader> articles;
    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_customer_list);
        articles = ArticleHeaders.get();
        Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.price_items));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new loadMaterials().execute();
        listView = (ListView) findViewById(R.id.list_price_list);
        adapter = new PriceListAdapter(PriceListCustomerActivity.this, arrayList);
        listView.setAdapter(adapter);

    }
    public class loadMaterials extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String>map = new HashMap<>();
            map.put(db.KEY_MATERIAL_NO,"");
            map.put(db.KEY_AMOUNT,"");
            HashMap<String,String>filter= new HashMap<>();
            filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            Cursor cursor = db.getData(db.PRICING,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                setMaterials(cursor);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }
    }
    private void setMaterials(Cursor cursor){
        Cursor c = cursor;
        c.moveToFirst();
        do{
            ItemList model = new ItemList();
            ArticleHeader articleHeader = ArticleHeader.getArticle(articles,c.getString(c.getColumnIndex(db.KEY_MATERIAL_NO)));
            model.setItem_number(StringUtils.stripStart(c.getString(c.getColumnIndex(db.KEY_MATERIAL_NO)), "0"));
            if(articleHeader!=null){
                model.setItem_des(UrlBuilder.decodeString(articleHeader.getMaterialDesc1()));
            }
            else{
                model.setItem_des(StringUtils.stripStart(c.getString(c.getColumnIndex(db.KEY_MATERIAL_NO)), "0"));
            }
            model.setCase_price(c.getString(c.getColumnIndex(db.KEY_AMOUNT)));
            arrayList.add(model);
        }
        while (c.moveToNext());
    }
}
