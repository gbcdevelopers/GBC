package gbc.sa.vansales.activities;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ShopStatusBadgeAdapter;
import gbc.sa.vansales.adapters.StockTakeBadgeAdapter;
import gbc.sa.vansales.models.Product;
import gbc.sa.vansales.models.ShopStatus;
/**
 * Created by Rakshit on 18-Nov-16.
 */
public class StockTakeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ArrayList<Product> productList;
    private ArrayAdapter<Product> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_take);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productList = new ArrayList<>();

        adapter = new StockTakeBadgeAdapter(this, productList);

        ListView listView = (ListView) findViewById(R.id.productList);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        new loadStockData().execute();

    }

    public void proceedToOrder(View v){
        Intent intent = new Intent(StockTakeActivity.this,OrderActivity.class);
        startActivityForResult(intent, 0);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
      //  startActivity(intent);
    }

    void generateData(){
        adapter.clear();
        for (int i = 0; i < 11; i++) {
            Product product = createProductData(i);
            productList.add(product);
        }
    }

    public static Product createProductData(int index){
        Product product = new Product();
        switch(index){
            case 0:
                product.setSKU("1");
                product.setProductDescription("150 ML - Cups");
                break;
            case 1:
                product.setSKU("2");
                product.setProductDescription("250 ML - Cups");
                break;
            case 2:
                product.setSKU("3");
                product.setProductDescription("330 ML - PET Bottles");
                break;
            case 3:
                product.setSKU("4");
                product.setProductDescription("500 ML - PET Bottles");
                break;
            case 4:
                product.setSKU("5");
                product.setProductDescription("600 ML - PET Bottles");
                break;
            case 5:
                product.setSKU("6");
                product.setProductDescription("650 ML - PET Bottles");
                break;
            case 6:
                product.setSKU("7");
                product.setProductDescription("1.5L - PET Bottles");
                break;
            case 7:
                product.setSKU("8");
                product.setProductDescription("1.89L - PET Bottles");
                break;
            case 8:
                product.setSKU("6");
                product.setProductDescription("3.89 L - PET Bottles");
                break;
            case 9:
                product.setSKU("7");
                product.setProductDescription("4 Gallon Bottles");
                break;
            case 10:
                product.setSKU("8");
                product.setProductDescription("5 Gallon Bottles");
                break;
        }

        return product;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }
    private class loadStockData extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            //Logic to fetch Data
            generateData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //Log.e("I cam here", "Here");
            adapter = new StockTakeBadgeAdapter(StockTakeActivity.this, productList);
            adapter.notifyDataSetChanged();
            // super.onPostExecute(aVoid);
        }
    }
}
