package gbc.sa.vansales.activities;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CollectionAdapter;
import gbc.sa.vansales.adapters.ColletionAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.Collection;
import gbc.sa.vansales.models.ColletionData;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.UrlBuilder;
public class CollectionsActivity extends AppCompatActivity {
    ListView lv_colletions_view;
    ImageView iv_back;
    TextView tv_top_header;
    ArrayList<Collection> colletionDatas = new ArrayList<>();
    CollectionAdapter colletionAdapter;
    TextView tv_amt_paid;
    double amount_paid = 0;
    double amount = 0.00;
    int pos = 0;
    Customer object;
    ArrayList<CustomerHeader> customers;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        final Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");
        if (object == null) {
            object = Const.allCustomerdataArrayList.get(Const.customerPosition);
        }
        loadingSpinner = new LoadingSpinner(this);
        customers = CustomerHeaders.get();
        CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, object.getCustomerID());
        TextView tv_customer_name = (TextView) findViewById(R.id.tv_customer_id);
        TextView tv_method_of_payment = (TextView) findViewById(R.id.tv_method_of_payment);
        if (!(customerHeader == null)) {
            tv_customer_name.setText(StringUtils.stripStart(customerHeader.getCustomerNo(), "0") + " " + UrlBuilder.decodeString(customerHeader.getName1()));
        } else {
            tv_customer_name.setText(StringUtils.stripStart(object.getCustomerID(), "0") + " " + UrlBuilder.decodeString(object.getCustomerName().toString()));
        }
        if (object.getPaymentMethod().equalsIgnoreCase(App.CASH_CUSTOMER)) {
            tv_method_of_payment.setText(getString(R.string.methodofPayment) + "-" + getString(R.string.cash));
        } else if (object.getPaymentMethod().equalsIgnoreCase(App.TC_CUSTOMER)) {
            tv_method_of_payment.setText(getString(R.string.methodofPayment) + "-" + getString(R.string.tc_lbl));
        } else {
            tv_method_of_payment.setText(getString(R.string.methodofPayment) + "-" + getString(R.string.credit));
        }
        lv_colletions_view = (ListView) findViewById(R.id.lv_colletions_view);
        colletionAdapter = new CollectionAdapter(CollectionsActivity.this, colletionDatas);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.collection));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helpers.isNetworkAvailable(getApplicationContext())) {
                    Helpers.createBackgroundJob(getApplicationContext());
                }
                Helpers.logData(CollectionsActivity.this, "Back clicked on Collection Screen. Triggering sync");
                Intent intent = new Intent(CollectionsActivity.this, CustomerDetailActivity.class);
                intent.putExtra("headerObj", object);
                intent.putExtra("msg", "all");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        tv_amt_paid = (TextView) findViewById(R.id.tv_amt_paid);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(CollectionsActivity.this, PaymentDetails.class);
                intent.putExtra("from", "collection");
                intent.putExtra("msg", "collection");
                intent.putExtra("headerObj", object);
                intent.putExtra("amountdue","0");
                intent.putExtra("customer",object.getCustomerID());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
//                startActivityForResult(intent, 1);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        // setData();
        new loadCollections().execute();
        lv_colletions_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    double amountdue = Double.parseDouble(colletionDatas.get(position).getInvoiceAmount()) - Double.parseDouble(colletionDatas.get(position).getAmountCleared());
                    if (amountdue == 0) {
                        Helpers.logData(CollectionsActivity.this, "User clicked on already cleared invoice" + colletionDatas.get(position).getInvoiceNo());
                        Toast.makeText(CollectionsActivity.this, "Invoice already cleared", Toast.LENGTH_SHORT).show();
                    } else {
                        if (colletionDatas.get(position).getIndicator().equals(App.ADD_INDICATOR)) {
                            Helpers.logData(CollectionsActivity.this, "List item clicked. Going for collection of invoice no" + colletionDatas.get(position).getInvoiceNo());
                            Intent intent = new Intent(CollectionsActivity.this, PaymentDetails.class);
                            intent.putExtra("msg", "collection");
                            intent.putExtra("from", "collection");
                            intent.putExtra("pos", position);
                            intent.putExtra("headerObj", object);
                            float dueamount = Float.parseFloat(colletionDatas.get(position).getInvoiceAmount()) - Float.parseFloat(colletionDatas.get(position).getAmountCleared());
                            Helpers.logData(CollectionsActivity.this, "Invoice no" + colletionDatas.get(position).getInvoiceNo() + "has due amount" + dueamount);
                            intent.putExtra("collection", colletionDatas.get(position));
                            intent.putExtra("amountdue", String.valueOf(dueamount));
                            startActivity(intent);
                        } else {
                            Helpers.logData(CollectionsActivity.this, "User Clicked on debit invoice " + colletionDatas.get(position).getInvoiceNo());
                            Toast.makeText(CollectionsActivity.this, getString(R.string.debit_invoice), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    String amt = data.getStringExtra("amt");
                    pos = data.getIntExtra("pos", 0);
                    Log.v("pos", amt + "--");
                    tv_amt_paid.setText(amt);
                    amount = Double.parseDouble(amt);
                    /*ColletionData colletionData = colletionDatas.get(pos);
                    colletionData.setAmoutAde(amt);
                    double amountdue = Double.parseDouble(colletionData.getAmoutDue()) - amount;
                    Log.v("amountdue", colletionData.getAmoutDue() + "");
                    colletionData.setAmoutDue(String.valueOf(amountdue));
                    colletionAdapter.notifyDataSetChanged();*/
                }
            }
        }
    }
    public class loadCollections extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_CUSTOMER_NO, "");
                map.put(db.KEY_INVOICE_NO, "");
                map.put(db.KEY_INVOICE_AMOUNT, "");
                map.put(db.KEY_DUE_DATE, "");
                map.put(db.KEY_INVOICE_DATE, "");
                map.put(db.KEY_AMOUNT_CLEARED, "");
                map.put(db.KEY_INDICATOR, "");
                map.put(db.KEY_IS_INVOICE_COMPLETE, "");
                HashMap<String, String> filter = new HashMap<>();
                filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                //filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                Cursor cursor = db.getData(db.COLLECTION, map, filter);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    setCollectionData(cursor);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (loadingSpinner.isShowing()) {
                loadingSpinner.hide();
            }
            colletionAdapter.notifyDataSetChanged();
            lv_colletions_view.setAdapter(colletionAdapter);
            tv_amt_paid.setText(String.valueOf(amount_paid));
        }
    }
    private void setCollectionData(Cursor cursor) {
        Helpers.logData(CollectionsActivity.this, "Loading Collections for Customer" + object.getCustomerID() + " has" + cursor.getCount() + "invoices");
        try {
            Cursor c = cursor;
            do {
                int indicator = 1;
                String invoiceAmount = "";
                Collection collection = new Collection();
                collection.setInvoiceNo(c.getString(c.getColumnIndex(db.KEY_INVOICE_NO)));
                collection.setInvoiceDate(c.getString(c.getColumnIndex(db.KEY_INVOICE_DATE)));
                collection.setIndicator(c.getString(c.getColumnIndex(db.KEY_INDICATOR)));
                indicator = c.getString(c.getColumnIndex(db.KEY_INDICATOR)).equals(App.ADD_INDICATOR) ? indicator : indicator * -1;
                invoiceAmount = c.getString(c.getColumnIndex(db.KEY_INVOICE_AMOUNT)).equals("") ? "0" : c.getString(c.getColumnIndex(db.KEY_INVOICE_AMOUNT));
                collection.setInvoiceAmount(String.valueOf(Double.parseDouble(invoiceAmount) * indicator));
                collection.setAmountCleared(c.getString(c.getColumnIndex(db.KEY_AMOUNT_CLEARED)).equals("") ? "0" : c.getString(c.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                amount_paid += Double.parseDouble(collection.getAmountCleared());
                collection.setInvoiceDueDate(c.getString(c.getColumnIndex(db.KEY_DUE_DATE)));
                colletionDatas.add(collection);
            }
            while (c.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
