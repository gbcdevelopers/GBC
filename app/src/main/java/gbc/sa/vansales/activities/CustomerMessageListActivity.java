package gbc.sa.vansales.activities;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DeliveryAdapter;
import gbc.sa.vansales.adapters.MessageBadgeAdapter;
import gbc.sa.vansales.adapters.MessageListAdapter;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.data.Messages;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.Message;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.RoundedImageView;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by eheuristic on 12/10/2016.
 */
public class CustomerMessageListActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    ListView lv_message;
    RoundedImageView iv_round;
    MessageListAdapter adapter1;
    MessageBadgeAdapter adapter;
    String arr[] = {"Silent Meeting", "Order Confirmed"};
    LinearLayout ll_common;
    ImageView img_refresh;
    ImageView iv_refresh;
    SwipeRefreshLayout refreshLayout;
    LoadingSpinner loadingSpinner;
    DatabaseHandler db = new DatabaseHandler(this);
    ArrayList<Message> arrayList;
    Customer object;
    ArrayList<CustomerHeader> customers;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");
        customers = CustomerHeaders.get();
        CustomerHeader customerHeader = null;
        if(object!=null){
            customerHeader = CustomerHeader.getCustomer(customers,object.getCustomerID());
        }
        loadingSpinner = new LoadingSpinner(this);
        arrayList = new ArrayList<>();
        adapter = new MessageBadgeAdapter(this,arrayList);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        ll_common = (LinearLayout) findViewById(R.id.ll_common);
        img_refresh = (ImageView) findViewById(R.id.img_refresh);
        iv_refresh = (ImageView) findViewById(R.id.iv_refresh);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.message_list));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_message = (ListView) findViewById(R.id.lv_messages);

        TextView tv_customer_name = (TextView)findViewById(R.id.tv_customer_id);
        TextView tv_customer_address = (TextView)findViewById(R.id.tv_customer_address);
        TextView tv_customer_pobox = (TextView)findViewById(R.id.tv_customer_pobox);
        TextView tv_customer_contact = (TextView)findViewById(R.id.tv_customer_contact);

        if (getIntent().getExtras() != null) {
            String from = getIntent().getStringExtra("from");
            if (from.equals("dash")) {
                ll_common.setVisibility(View.GONE);
                img_refresh.setVisibility(View.GONE);
                iv_refresh.setVisibility(View.VISIBLE);
                new loadMessages(getIntent().getStringExtra("from"));
            }
            else{

                if(!(customerHeader==null)){
                    tv_customer_name.setText(StringUtils.stripStart(customerHeader.getCustomerNo(), "0") + " " + UrlBuilder.decodeString(customerHeader.getName1()));
                    tv_customer_address.setText(UrlBuilder.decodeString(customerHeader.getStreet()));
                    tv_customer_pobox.setText(getString(R.string.pobox) + " " + customerHeader.getPostCode());
                    tv_customer_contact.setText(customerHeader.getPhone());
                }
                else{
                    tv_customer_name.setText(StringUtils.stripStart(object.getCustomerID(),"0") + " " + UrlBuilder.decodeString(object.getCustomerName().toString()));
                    tv_customer_address.setText(object.getCustomerAddress().toString());
                    tv_customer_pobox.setText("");
                    tv_customer_contact.setText("");
                }
                new loadMessages(getIntent().getStringExtra("from"));
            }
        } else {
            ll_common.setVisibility(View.VISIBLE);
        }
       // adapter = new MessageListAdapter(CustomerMessageListActivity.this, arr);
        lv_message.setAdapter(adapter);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (refreshLayout.isRefreshing()) {
                    dispatchRefresh();
                    refreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }
               /* Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (refreshLayout.isRefreshing()) {
                            refreshLayout.setRefreshing(false);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, 2000);*/
            }
        });
        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchRefresh();
            }
        });
        img_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchRefresh();
            }
        });
    }
    public void dispatchRefresh() {
        refreshLayout.setRefreshing(true);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
            HashMap<String,String> filter = new HashMap<String, String>();
            filter.put(db.KEY_STRUCTURE,App.DRIVER_MESSAGE_KEY);
            db.deleteData(db.MESSAGES,filter);
            Messages.load(CustomerMessageListActivity.this, Settings.getString(App.DRIVER), db);
            new loadMessages(getIntent().getStringExtra("from"));
            // adapter.notifyDataSetChanged();
        }
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                    HashMap<String,String> filter = new HashMap<String, String>();
                    filter.put(db.KEY_STRUCTURE,App.DRIVER_MESSAGE_KEY);
                    db.deleteData(db.MESSAGES,filter);
                    Messages.load(CustomerMessageListActivity.this, Settings.getString(App.DRIVER), db);
                    new loadMessages(getIntent().getStringExtra("from"));
                   // adapter.notifyDataSetChanged();
                }
            }
        }, 4000);*/
    }

    public class loadMessages extends AsyncTask<Void,Void,Void>{
        String from = "";
        String filter = "";
        private loadMessages(String from) {
            this.from = from;
            execute();
        }

        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_USERNAME,"");
            map.put(db.KEY_STRUCTURE,"");
            map.put(db.KEY_MESSAGE,"");
            map.put(db.KEY_DRIVER,"");

            if(from.equals("dash")){
                filter = Settings.getString(App.DRIVER);
            }
            else{
                filter = from;
            }
            Log.e("From is", "" + from);

            HashMap<String,String> filterMap = new HashMap<>();
            filterMap.put(db.KEY_USERNAME,filter);
            filterMap.put(db.KEY_STRUCTURE,App.DRIVER_MESSAGE_KEY);

            Cursor cursor = db.getData(db.MESSAGES,map,filterMap);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                setMessages(cursor);
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

    private void setMessages(Cursor cursor){
        arrayList.clear();
        do{
            Message message = new Message();
            message.setId(cursor.getString(cursor.getColumnIndex(db.KEY_USERNAME)));
            message.setDriver(cursor.getString(cursor.getColumnIndex(db.KEY_DRIVER)));
            message.setStructure(cursor.getString(cursor.getColumnIndex(db.KEY_STRUCTURE)));
            message.setMessage(cursor.getString(cursor.getColumnIndex(db.KEY_MESSAGE)));
          //  Log.e("Message","" + UrlBuilder.decodeString(message.getStructure()) +  message.getMessage());
            arrayList.add(message);
        }
        while (cursor.moveToNext());
    }
}
