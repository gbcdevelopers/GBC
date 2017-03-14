package gbc.sa.vansales.activities;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DeliveryAdapter;
import gbc.sa.vansales.adapters.OrderListBadgeAdapter;
import gbc.sa.vansales.adapters.PresaleAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.models.PreSaleProceed;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;

/*****************************************************************************************
 @ This activity is called when driver clicks on order request in the
 @ Customer detail screen
 ****************************************************************************************/
public class PreSaleOrderActivity extends AppCompatActivity {
    ImageView iv_back, iv_refresh;
    TextView tv_top_header;
    ListView list_delivery;
    PresaleAdapter presaleAdapterdapter;
    OrderListBadgeAdapter adapter;
    FloatingActionButton flt_presale;
    ArrayList<Integer> proceedArrayList;
    Customer object;
    ArrayList<CustomerHeader> customers;
    ArrayList<OrderList> arrayList;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_list);
        try{
            Helpers.logData(PreSaleOrderActivity.this, "On Order Request Screen");
            Intent i = this.getIntent();
            object = (Customer) i.getParcelableExtra("headerObj");
            loadingSpinner = new LoadingSpinner(this);
            refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
            arrayList = new ArrayList<>();
            new loadOrders().execute();
            new loadOrdersLocal().execute();
            adapter = new OrderListBadgeAdapter(this, arrayList);
            Const.constantsHashMap.clear();
            customers = CustomerHeaders.get();
            CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, object.getCustomerID());
            TextView tv_customer_name = (TextView) findViewById(R.id.tv_customer_id);
            TextView tv_customer_address = (TextView) findViewById(R.id.tv_customer_address);
            TextView tv_customer_pobox = (TextView) findViewById(R.id.tv_customer_pobox);
            TextView tv_customer_contact = (TextView) findViewById(R.id.tv_customer_contact);
            if (!(customerHeader == null)) {
                tv_customer_name.setText(StringUtils.stripStart(customerHeader.getCustomerNo(), "0") + " " + UrlBuilder.decodeString(customerHeader.getName1()));
                tv_customer_address.setText(UrlBuilder.decodeString(customerHeader.getStreet()));
                tv_customer_pobox.setText(getString(R.string.pobox) + " " + customerHeader.getPostCode());
                tv_customer_contact.setText(customerHeader.getPhone());
            } else {
                tv_customer_name.setText(StringUtils.stripStart(object.getCustomerID(),"0") + " " + UrlBuilder.decodeString(object.getCustomerName().toString()));
                tv_customer_address.setText(object.getCustomerAddress().toString());
                tv_customer_pobox.setText("");
                tv_customer_contact.setText("");
            }
            iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
            tv_top_header = (TextView) findViewById(R.id.tv_top_header);
            flt_presale = (FloatingActionButton) findViewById(R.id.flt_presale);
            iv_back.setVisibility(View.VISIBLE);
            tv_top_header.setVisibility(View.VISIBLE);
            tv_top_header.setText(getString(R.string.order_request));
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            list_delivery = (ListView) findViewById(R.id.list_delivery);
            iv_refresh = (ImageView) findViewById(R.id.img_refresh);
            iv_refresh.setVisibility(View.INVISIBLE);
            proceedArrayList = new ArrayList<>();
            presaleAdapterdapter = new PresaleAdapter(PreSaleOrderActivity.this, R.layout.custom_delivery, proceedArrayList.size());
            list_delivery.setAdapter(adapter);
            flt_presale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helpers.logData(PreSaleOrderActivity.this, "Creating an order Request button clicked");
                    Intent intent = new Intent(PreSaleOrderActivity.this, PreSaleOrderProceedActivity.class);
                    intent.putExtra("from", "button");
                    intent.putExtra("headerObj", object);
                    startActivity(intent);
                }
            });
            /********************************************************
             @ Reading details of order created locally on the device
             ********************************************************/
            list_delivery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    OrderList orderList = arrayList.get(position);
                    Helpers.logData(PreSaleOrderActivity.this, "Viewing details of order from list" + orderList.getOrderId());
                    Intent intent = new Intent(PreSaleOrderActivity.this, PreSaleOrderProceedActivity.class);
                    intent.putExtra("from", "list");
                    intent.putExtra("pos", position);
                    intent.putExtra("orderList", orderList);
                    intent.putExtra("headerObj", object);
                    startActivity(intent);
                }
            });
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (refreshLayout.isRefreshing()) {
                                refreshLayout.setRefreshing(false);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }, 2000);
                }
            });
            iv_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchRefresh();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    public class loadOrders extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Helpers.logData(PreSaleOrderActivity.this, "Loading already posted orders");
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
            map.put(db.KEY_ORDER_ID, "");
            map.put(db.KEY_PURCHASE_NUMBER, "");
            map.put(db.KEY_DATE, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_IS_POSTED, App.DATA_IS_POSTED);
            filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
            Cursor cursor = db.getData(db.ORDER_REQUEST, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                setOrders(cursor);
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
    public class loadOrdersLocal extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Helpers.logData(PreSaleOrderActivity.this, "Loading marked for post orders");
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
            map.put(db.KEY_ORDER_ID, "");
            map.put(db.KEY_PURCHASE_NUMBER, "");
            map.put(db.KEY_DATE, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
            filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
            Cursor cursor = db.getData(db.ORDER_REQUEST, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                setOrdersLocal(cursor);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (loadingSpinner.isShowing()) {
                loadingSpinner.hide();
            }
            //Log.e("ArrayList","" + arrayList.size());
            adapter.notifyDataSetChanged();
        }
    }
    private void setOrders(Cursor cursor) {
        Helpers.logData(PreSaleOrderActivity.this, cursor.getCount() + "Orders are posted");
        try{
            ArrayList<String> temp = new ArrayList<String>();
            temp.clear();
            arrayList.clear();
            do {
                OrderList orderList = new OrderList();
                //orderList.setOrderId(cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID)));
                orderList.setOrderId(cursor.getString(cursor.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                orderList.setOrderDate(cursor.getString(cursor.getColumnIndex(db.KEY_DATE)));
                //Log.e("ORDER1", "" + orderList.getOrderId());
                if (!temp.contains(orderList.getOrderId())) {
                    temp.add(orderList.getOrderId());
                    Helpers.logData(PreSaleOrderActivity.this, "Already Posted orders are" + orderList.getOrderId());
                    arrayList.add(orderList);
                }
            }
            while (cursor.moveToNext());
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    private void setOrdersLocal(Cursor cursor) {
        Helpers.logData(PreSaleOrderActivity.this, cursor.getCount() + "Orders are marked for post");
        try{
            ArrayList<String> temp = new ArrayList<String>();
            temp.clear();
            //arrayList.clear();
            //Log.e("Cursor", "" + cursor.getCount());
            cursor.moveToFirst();
            do {
                OrderList orderList = new OrderList();
                //orderList.setOrderId(cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID)));
                orderList.setOrderId(cursor.getString(cursor.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                orderList.setOrderDate(cursor.getString(cursor.getColumnIndex(db.KEY_DATE)));

                if (!temp.contains(orderList.getOrderId())) {
                    //Log.e("ORDER", "" + orderList.getOrderId());
                    temp.add(orderList.getOrderId());
                    Helpers.logData(PreSaleOrderActivity.this, "Marked for Post orders are" + orderList.getOrderId());
                    arrayList.add(orderList);
                }
            }
            while (cursor.moveToNext());
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    public void dispatchRefresh() {
        refreshLayout.setRefreshing(true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }
            }
        }, 2000);
    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }
}
