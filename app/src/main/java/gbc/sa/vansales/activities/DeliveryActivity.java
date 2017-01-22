package gbc.sa.vansales.activities;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DeliveryAdapter;
import gbc.sa.vansales.adapters.DeliveryListBadgeAdapter;
import gbc.sa.vansales.adapters.OrderListBadgeAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by eheuristic on 12/10/2016.
 */
public class DeliveryActivity extends AppCompatActivity {
    ImageView iv_back,iv_refresh;
    TextView tv_top_header;
    ListView list_delivery;
    DeliveryListBadgeAdapter adapter;
    FloatingActionButton flt_button;
    Customer object;
    ArrayList<CustomerHeader> customers;
    DatabaseHandler db = new DatabaseHandler(this);
    ArrayList<OrderList> arrayList;
    LoadingSpinner loadingSpinner;

    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_list);
        loadingSpinner = new LoadingSpinner(this);
        arrayList = new ArrayList<>();

        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_layout);


        new loadDeliveries().execute();
        adapter = new DeliveryListBadgeAdapter(this, arrayList);
        Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");
        customers = CustomerHeaders.get();

        CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, object.getCustomerID());
        TextView tv_customer_name = (TextView) findViewById(R.id.tv_customer_id);
        TextView tv_customer_address = (TextView) findViewById(R.id.tv_customer_address);
        TextView tv_customer_pobox = (TextView) findViewById(R.id.tv_customer_pobox);
        TextView tv_customer_contact = (TextView) findViewById(R.id.tv_customer_contact);
        if (!(customerHeader == null)) {
            tv_customer_name.setText(StringUtils.stripStart(customerHeader.getCustomerNo(), "0") + " " + customerHeader.getName1());
            tv_customer_address.setText(UrlBuilder.decodeString(customerHeader.getStreet()));
            tv_customer_pobox.setText(getString(R.string.pobox) + " " + customerHeader.getPostCode());
            tv_customer_contact.setText(customerHeader.getPhone());
        } else {
            tv_customer_name.setText(StringUtils.stripStart(object.getCustomerID(),"0") + " " + object.getCustomerName().toString());
            tv_customer_address.setText(object.getCustomerAddress().toString());
            tv_customer_pobox.setText("");
            tv_customer_contact.setText("");
        }
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.delivery_list));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryActivity.this, CustomerDetailActivity.class);
                intent.putExtra("headerObj", object);
                intent.putExtra("msg", "all");
                startActivity(intent);
                finish();
            }
        });
        flt_button = (FloatingActionButton) findViewById(R.id.flt_presale);
        flt_button.setVisibility(View.GONE);
        list_delivery = (ListView) findViewById(R.id.list_delivery);

        iv_refresh=(ImageView)findViewById(R.id.img_refresh);



     //  adapter = new DeliveryAdapter(DeliveryActivity.this, 2, R.layout.custom_delivery, "delivery");
        list_delivery.setAdapter(adapter);
        list_delivery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderList delivery = arrayList.get(position);
                Intent intent = new Intent(DeliveryActivity.this, DeliveryOrderActivity.class);
                intent.putExtra("headerObj", object);
                intent.putExtra("delivery", delivery);
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
    public class loadDeliveries extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(db.KEY_DELIVERY_NO, "");
            map.put(db.KEY_DELIVERY_DATE, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_IS_DELIVERED, "false");
            Cursor cursor = db.getData(db.CUSTOMER_DELIVERY_HEADER, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                setDeliveryList(cursor);
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
    private void setDeliveryList(Cursor cursor) {
        ArrayList<String> temp = new ArrayList<String>();
        temp.clear();
        arrayList.clear();
        do {
            OrderList orderList = new OrderList();
            orderList.setOrderId(cursor.getString(cursor.getColumnIndex(db.KEY_DELIVERY_NO)));
            String date = cursor.getString(cursor.getColumnIndex(db.KEY_DELIVERY_DATE));
            String[]token = date.split("\\.");
            orderList.setOrderDate(Helpers.getMaskedValue(token[2],2)+"-"+Helpers.getMaskedValue(token[1],2)+"-"+token[0]);
            if (!temp.contains(orderList.getOrderId())) {
                temp.add(orderList.getOrderId());
                arrayList.add(orderList);
            }
        }
        while (cursor.moveToNext());
        Log.v("arraylistsize",arrayList.size()+" -" +
                "");
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
}
