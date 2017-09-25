package gbc.sa.vansales.activities;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
public class TodaysSummaryActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    View view1;

    TextView tv_total_sales;
    TextView tv_cash_sales;
    TextView tv_credit_sales;
    TextView tv_tc_sales;
    TextView tv_item_discounts;
    TextView tv_receipt;
    TextView tv_collection;
    TextView tv_vanstock_value;
    TextView tv_vanstock_quantity;

    double vanValue = 0;
    double vanQuantity = 0;
    double cashSales = 0;
    double creditSales = 0;
    double tcSales = 0;
    double itemDiscount = 0;

    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_summary);

        loadingSpinner = new LoadingSpinner(this);
        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.todays_summary));

        tv_total_sales = (TextView)findViewById(R.id.tv_total_sales);
        tv_cash_sales = (TextView)findViewById(R.id.tv_cash_sale);
        tv_credit_sales = (TextView)findViewById(R.id.tv_credit_sales);
        tv_tc_sales = (TextView)findViewById(R.id.tv_tc_sales);
        tv_item_discounts = (TextView)findViewById(R.id.tv_item_discounts);
        tv_receipt = (TextView)findViewById(R.id.tv_receipt);
        tv_collection = (TextView)findViewById(R.id.tv_collection);
        tv_vanstock_value = (TextView)findViewById(R.id.tv_vanstock_value);
        tv_vanstock_quantity = (TextView)findViewById(R.id.tv_vanstock_quantity);

        new loadVanData().execute();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public class loadVanData extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_ITEM_NO,"");
            map.put(db.KEY_MATERIAL_NO,"");
            map.put(db.KEY_REMAINING_QTY_CASE,"");
            map.put(db.KEY_REMAINING_QTY_UNIT,"");
            HashMap<String,String>filter = new HashMap<>();
            Cursor vanCursor = db.getData(db.VAN_STOCK_ITEMS,map,filter);
            if(vanCursor.getCount()>0){
                vanCursor.moveToFirst();
                setLoadData(vanCursor);
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            tv_vanstock_quantity.setText(String.valueOf(vanQuantity));
            tv_vanstock_value.setText(String.valueOf(vanValue));

            new loadSalesData().execute();
        }
    }
    private void setLoadData(Cursor cursor){
        cursor.moveToFirst();
        do{

            HashMap<String,String>priceMap = new HashMap<>();
            priceMap.put(db.KEY_AMOUNT, "");
            HashMap<String,String>filterPrice = new HashMap<>();
            filterPrice.put(db.KEY_MATERIAL_NO,cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
            filterPrice.put(db.KEY_PRIORITY,"2");
            Cursor priceCursor = db.getData(db.PRICING,priceMap,filterPrice);
            vanQuantity += Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_CASE)));
            Log.e("Van Quantity","" + vanQuantity);
            if(priceCursor.getCount()>0){
                priceCursor.moveToFirst();
                vanValue+= Double.parseDouble(priceCursor.getString(priceCursor.getColumnIndex(db.KEY_AMOUNT)))*
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_CASE)));
            }
        }
        while (cursor.moveToNext());
    }
    public class loadSalesData extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            if(!loadingSpinner.isShowing()){
                loadingSpinner.show();
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String>map = new HashMap<>();
            map.put(db.KEY_TIME_STAMP, "");
            map.put(db.KEY_CUSTOMER_NO,"");
            map.put(db.KEY_CUSTOMER_TYPE,"");
            map.put(db.KEY_ACTIVITY_TYPE,"");
            map.put(db.KEY_ORDER_TOTAL,"");
            map.put(db.KEY_ORDER_DISCOUNT,"");
            map.put(db.KEY_ORDER_ID,"");
            HashMap<String,String>filter = new HashMap<>();
            Cursor c = db.getData(db.TODAYS_SUMMARY,map,filter);
            if(c.getCount()>0){
                c.moveToFirst();
                setSalesData(c);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            tv_cash_sales.setText(String.valueOf(cashSales));
            tv_credit_sales.setText(String.valueOf(creditSales));
            tv_tc_sales.setText(String.valueOf(tcSales));
            tv_item_discounts.setText(String.valueOf(itemDiscount));

        }
    }
    private void setSalesData(Cursor cursor){
        Cursor c = cursor;
        c.moveToFirst();
        do{
            if(c.getString(c.getColumnIndex(db.KEY_CUSTOMER_TYPE)).equals(App.CASH_CUSTOMER)){
                cashSales += Double.parseDouble(c.getString(c.getColumnIndex(db.KEY_ORDER_TOTAL)));
                itemDiscount += Double.parseDouble(c.getString(c.getColumnIndex(db.KEY_ORDER_DISCOUNT)));
            }
            if(c.getString(c.getColumnIndex(db.KEY_CUSTOMER_TYPE)).equals(App.CREDIT_CUSTOMER)){
                creditSales += Double.parseDouble(c.getString(c.getColumnIndex(db.KEY_ORDER_TOTAL)));
                itemDiscount += Double.parseDouble(c.getString(c.getColumnIndex(db.KEY_ORDER_DISCOUNT)));
            }
            if(c.getString(c.getColumnIndex(db.KEY_CUSTOMER_TYPE)).equals(App.TC_CUSTOMER)){
                tcSales += Double.parseDouble(c.getString(c.getColumnIndex(db.KEY_ORDER_TOTAL)));
                itemDiscount += Double.parseDouble(c.getString(c.getColumnIndex(db.KEY_ORDER_DISCOUNT)));
            }
        }
        while (c.moveToNext());
    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }
}
