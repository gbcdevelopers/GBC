package gbc.sa.vansales.activities;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.OrderSummaryBadgeAdapter;
import gbc.sa.vansales.adapters.ShopStatusBadgeAdapter;
import gbc.sa.vansales.models.OrderSummary;
import gbc.sa.vansales.models.ShopStatus;
/**
 * Created by Rakshit on 18-Nov-16.
 */
public class OrderSummaryActivity extends AppCompatActivity {
    private ArrayList<OrderSummary> orderSummaryList;
    private ArrayAdapter<OrderSummary> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderSummaryList = new ArrayList<>();

        adapter = new OrderSummaryBadgeAdapter(this, orderSummaryList);

        ListView listView = (ListView) findViewById(R.id.orderSummaryList);
        listView.setAdapter(adapter);
        new loadOrderSummary().execute();
    }

    private void loadData(){
        adapter.clear();
        for (int i = 0; i < 4; i++) {
            OrderSummary orders = createOrderSummary(i);
            orderSummaryList.add(orders);
        }
    }

    public static OrderSummary createOrderSummary(int index){
        OrderSummary orders = new OrderSummary();
        switch(index){
            case 0:
                orders.setProductDescription("150 ML - Cups");
                orders.setQuantityCS("12");
                orders.setQuantityBT("2");
                orders.setDiscount("0");
                orders.setTradeprice("30 SAR");
                break;
            case 1:
                orders.setProductDescription("500 ML - PET Bottles");
                orders.setQuantityCS("5");
                orders.setQuantityBT("7");
                orders.setDiscount("0");
                orders.setTradeprice("120 SAR");
                break;
            case 2:
                orders.setProductDescription("600 ML - PET Bottles");
                orders.setQuantityCS("3");
                orders.setQuantityBT("9");
                orders.setDiscount("0");
                orders.setTradeprice("90 SAR");
                break;
            case 3:
                orders.setProductDescription("600 ML - PET Bottles");
                orders.setQuantityCS("3");
                orders.setQuantityBT("9");
                orders.setDiscount("0");
                orders.setTradeprice("90 SAR");
                break;


        }

        return orders;
    }

    private class loadOrderSummary extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            //Logic to fetch Data
            loadData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new OrderSummaryBadgeAdapter(OrderSummaryActivity.this, orderSummaryList);
            adapter.notifyDataSetChanged();
            // super.onPostExecute(aVoid);
        }
    }
}
