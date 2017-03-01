package gbc.sa.vansales.activities;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.BalanceAdapter;
import gbc.sa.vansales.adapters.BalanceBadgeAdapter;
import gbc.sa.vansales.adapters.PriceListAdapter;
import gbc.sa.vansales.models.BAlanceList;
import gbc.sa.vansales.models.Collection;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.ItemList;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
/************************************************************
 @ This class is called when u click on the balances by expanding
 @ the button on the customer detail screen. It gives the outstanding
 @ balances for the customer
 ************************************************************/
public class BalanceActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    View view1;
    DatabaseHandler db = new DatabaseHandler(this);
    ListView listView;
    LoadingSpinner loadingSpinner;
    ArrayList<Collection> arrayList = new ArrayList<>();
    ArrayAdapter<Collection> adapter;
    Customer object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        try{
            loadingSpinner = new LoadingSpinner(this);
            Intent i = this.getIntent();
            Helpers.logData(BalanceActivity.this, "On Balance Activity");
            object = (Customer) i.getParcelableExtra("headerObj");
            iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
            tv_top_header = (TextView) findViewById(R.id.tv_top_header);
            iv_back.setVisibility(View.VISIBLE);
            tv_top_header.setVisibility(View.VISIBLE);
            tv_top_header.setText(getString(R.string.balance));
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helpers.logData(BalanceActivity.this, "Back click on Balance Activity Screen");
                    finish();
                }
            });
            listView = (ListView) findViewById(R.id.list_price_list);
            adapter = new BalanceBadgeAdapter(this, arrayList);
            listView.setAdapter(adapter);
            new loadBalance().execute();
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }
    public class loadBalance extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try{
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_CUSTOMER_NO, "");
                map.put(db.KEY_INVOICE_NO, "");
                map.put(db.KEY_INVOICE_AMOUNT, "");
                map.put(db.KEY_DUE_DATE, "");
                map.put(db.KEY_INVOICE_DATE, "");
                map.put(db.KEY_AMOUNT_CLEARED, "");
                map.put(db.KEY_IS_INVOICE_COMPLETE, "");
                HashMap<String, String> filter = new HashMap<>();
                filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                Cursor cursor = db.getData(db.COLLECTION, map, filter);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    setCollectionData(cursor);
                }
            }
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (loadingSpinner.isShowing()) {
                loadingSpinner.hide();
            }
            adapter.notifyDataSetChanged();
        }
    }
    private void setCollectionData(Cursor cursor) {
        try{
            Cursor c = cursor;
            do {
                Collection collection = new Collection();
                collection.setInvoiceNo(c.getString(c.getColumnIndex(db.KEY_INVOICE_NO)));
                collection.setInvoiceDate(c.getString(c.getColumnIndex(db.KEY_INVOICE_DATE)));
                collection.setInvoiceAmount(c.getString(c.getColumnIndex(db.KEY_INVOICE_AMOUNT)));
                collection.setAmountCleared(c.getString(c.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                collection.setInvoiceDueDate(c.getString(c.getColumnIndex(db.KEY_DUE_DATE)));
                arrayList.add(collection);
            }
            while (c.moveToNext());
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
}
