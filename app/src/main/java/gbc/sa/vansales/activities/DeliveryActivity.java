package gbc.sa.vansales.activities;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerStatusAdapter;
import gbc.sa.vansales.adapters.DeliveryAdapter;
import gbc.sa.vansales.adapters.DeliveryListBadgeAdapter;
import gbc.sa.vansales.adapters.OrderListBadgeAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.data.OrderReasons;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.CustomerStatus;
import gbc.sa.vansales.models.DeliveryItem;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.models.Reasons;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by eheuristic on 12/10/2016.
 */
public class DeliveryActivity extends AppCompatActivity {
    ImageView iv_back, iv_refresh;
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
    private ArrayAdapter<CustomerStatus> statusAdapter;
    private ArrayList<CustomerStatus> statusList = new ArrayList<>();
    private ArrayList<Reasons> reasonsList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_list);
        loadingSpinner = new LoadingSpinner(this);
        arrayList = new ArrayList<>();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        new loadDeliveries().execute();
        adapter = new DeliveryListBadgeAdapter(this, arrayList);
        reasonsList = OrderReasons.get();
        statusAdapter = new CustomerStatusAdapter(this, statusList);
        loadStatus();
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
            tv_customer_name.setText(StringUtils.stripStart(object.getCustomerID(), "0") + " " + object.getCustomerName().toString());
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("headerObj", object);
                intent.putExtra("msg", "all");
                startActivity(intent);
                finish();
            }
        });
        flt_button = (FloatingActionButton) findViewById(R.id.flt_presale);
        flt_button.setVisibility(View.GONE);
        list_delivery = (ListView) findViewById(R.id.list_delivery);
        iv_refresh = (ImageView) findViewById(R.id.img_refresh);
        registerForContextMenu(list_delivery);
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
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.list_delivery) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_delivery, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.remove:
                // add stuff here
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeliveryActivity.this);
                alertDialogBuilder.setTitle(getString(R.string.message))
                        .setMessage(getString(R.string.delete_msg))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                showReasonDialog(arrayList, info.position);
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
                // showReasonDialog(arrayList, info.position);
                return true;
            case R.id.cancel:
                // edit stuff here
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
            String[] token = date.split("\\.");
            orderList.setOrderDate(Helpers.getMaskedValue(token[2], 2) + "-" + Helpers.getMaskedValue(token[1], 2) + "-" + token[0]);
            if (!temp.contains(orderList.getOrderId())) {
                temp.add(orderList.getOrderId());
                arrayList.add(orderList);
            }
        }
        while (cursor.moveToNext());
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
    private void showReasonDialog(ArrayList<OrderList> list, final int position) {
        final int pos = position;
        final Dialog dialog = new Dialog(DeliveryActivity.this);
        dialog.setTitle(getString(R.string.shop_status));
        View view = getLayoutInflater().inflate(R.layout.activity_select_customer_status, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_top_header);
        tv.setText(getString(R.string.select_reason));
        ListView lv = (ListView) view.findViewById(R.id.statusList);
        Button cancel = (Button) view.findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        lv.setAdapter(statusAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteDeliveryItems(arrayList.get(pos).getOrderId(), statusList.get(pos).getReasonCode(), statusList.get(pos).getReasonDescription());
                deleteDelivery(pos);
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void deleteDeliveryItems(String deliveryNo, String reasonCode, String reasonDescription) {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_ID, "");
        map.put(db.KEY_DELIVERY_NO, "");
        map.put(db.KEY_ITEM_NO, "");
        map.put(db.KEY_ITEM_CATEGORY, "");
        map.put(db.KEY_CREATED_BY, "");
        map.put(db.KEY_ENTRY_TIME, "");
        map.put(db.KEY_DATE, "");
        map.put(db.KEY_MATERIAL_NO, "");
        map.put(db.KEY_MATERIAL_ENTERED, "");
        map.put(db.KEY_MATERIAL_GROUP, "");
        map.put(db.KEY_PLANT, "");
        map.put(db.KEY_STORAGE_LOCATION, "");
        map.put(db.KEY_BATCH, "");
        map.put(db.KEY_ACTUAL_QTY, "");
        map.put(db.KEY_REMAINING_QTY, "");
        map.put(db.KEY_UOM, "");
        map.put(db.KEY_DIST_CHANNEL, "");
        map.put(db.KEY_DIVISION, "");
        map.put(db.KEY_IS_DELIVERED, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_DELIVERY_NO, deliveryNo);
        filter.put(db.KEY_IS_DELIVERED, App.FALSE);
        Cursor cursor = db.getData(db.CUSTOMER_DELIVERY_ITEMS, map, filter);
        if (cursor.getCount() > 0) {
            String purchaseNumber = "";
            HashMap<String, String> filter1 = new HashMap<String, String>();
            filter1.put(db.KEY_DELIVERY_NO, deliveryNo);
            filter1.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
            if (db.checkData(db.CUSTOMER_DELIVERY_ITEMS_DELETE_POST, filter)) {
                HashMap<String, String> map1 = new HashMap<String, String>();
                map1.put(db.KEY_ORDER_ID, "");
                Cursor cursor1 = db.getData(db.CUSTOMER_DELIVERY_ITEMS_DELETE_POST, map1, filter1);
                cursor1.moveToFirst();
                purchaseNumber = cursor1.getString(cursor1.getColumnIndex(db.KEY_ORDER_ID));
            }
            do {
                HashMap<String, String> deleteMap = new HashMap<String, String>();
                deleteMap.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                deleteMap.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                deleteMap.put(db.KEY_DELIVERY_NO, deliveryNo);
                deleteMap.put(db.KEY_ITEM_NO, cursor.getString(cursor.getColumnIndex(db.KEY_ITEM_NO)));
                deleteMap.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                //deleteMap.put(db.KEY_MATERIAL_DESC1,deliveryItem.getItemDescription());
                deleteMap.put(db.KEY_CASE, cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)));
                deleteMap.put(db.KEY_UNIT, cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY_UNIT)));
                deleteMap.put(db.KEY_UOM, cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                deleteMap.put(db.KEY_REASON_CODE, reasonCode);
                deleteMap.put(db.KEY_REASON_DESCRIPTION, reasonDescription);
                deleteMap.put(db.KEY_ORDER_ID, purchaseNumber.equals("") ? Helpers.generateNumber(db, ConfigStore.CustomerDeliveryDelete_PR_Type) : purchaseNumber);
                deleteMap.put(db.KEY_PURCHASE_NUMBER, purchaseNumber.equals("") ? Helpers.generateNumber(db, ConfigStore.CustomerDeliveryDelete_PR_Type) : purchaseNumber);
                deleteMap.put(db.KEY_AMOUNT, "");
                deleteMap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                deleteMap.put(db.KEY_IS_PRINTED, App.DATA_MARKED_FOR_POST);
                //Adding item for delete in post
                db.addData(db.CUSTOMER_DELIVERY_ITEMS_DELETE_POST, map);
            }
            while (cursor.moveToNext());
        }
    }
    public void deleteDelivery(int pos) {
        new returnDeliveryItems(arrayList.get(pos).getOrderId());
        //Log.e("Delete Deliver","" + arrayList.get(pos).getOrderId());
    }
    public void loadStatus() {
        for (Reasons reason : reasonsList) {
            if (reason.getReasonType().equals(App.REASON_REJECT)) {
                CustomerStatus status = new CustomerStatus();
                status.setReasonCode(reason.getReasonID());
                status.setReasonDescription(UrlBuilder.decodeString(reason.getReasonDescription()));
                statusList.add(status);
            }
        }
        statusAdapter.notifyDataSetChanged();
    }
    public class returnDeliveryItems extends AsyncTask<Void, Void, Void> {
        private String deliveryID = "";
        private returnDeliveryItems(String deliveryID) {
            this.deliveryID = deliveryID;
            execute();
        }
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_ID, "");
            map.put(db.KEY_DELIVERY_NO, "");
            map.put(db.KEY_ITEM_NO, "");
            map.put(db.KEY_ITEM_CATEGORY, "");
            map.put(db.KEY_CREATED_BY, "");
            map.put(db.KEY_ENTRY_TIME, "");
            map.put(db.KEY_DATE, "");
            map.put(db.KEY_MATERIAL_NO, "");
            map.put(db.KEY_MATERIAL_ENTERED, "");
            map.put(db.KEY_MATERIAL_GROUP, "");
            map.put(db.KEY_PLANT, "");
            map.put(db.KEY_STORAGE_LOCATION, "");
            map.put(db.KEY_BATCH, "");
            map.put(db.KEY_ACTUAL_QTY, "");
            map.put(db.KEY_REMAINING_QTY, "");
            map.put(db.KEY_UOM, "");
            map.put(db.KEY_DIST_CHANNEL, "");
            map.put(db.KEY_DIVISION, "");
            map.put(db.KEY_IS_DELIVERED, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_DELIVERY_NO, this.deliveryID);
            Cursor cursor = db.getData(db.CUSTOMER_DELIVERY_ITEMS, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                returnData(cursor);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (loadingSpinner.isShowing()) {
                loadingSpinner.hide();
            }
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(db.KEY_IS_DELIVERED, "deleted");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
            filter.put(db.KEY_DELIVERY_NO, this.deliveryID);
            db.updateData(db.CUSTOMER_DELIVERY_HEADER, map, filter);
            new loadDeliveries().execute();
        }
    }
    private void returnData(Cursor cursor) {
        do {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_MATERIAL_NO, "");
            map.put(db.KEY_REMAINING_QTY_CASE, "");
            map.put(db.KEY_REMAINING_QTY_UNIT, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
            Cursor c = db.getData(db.VAN_STOCK_ITEMS, map, filter);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    HashMap<String, String> updateDataMap = new HashMap<>();
                    float remainingCase = 0;
                    float remainingUnit = 0;
                    remainingCase = Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_REMAINING_QTY_CASE)));
                    remainingUnit = Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_REMAINING_QTY_UNIT)));
                    if (!(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)).isEmpty() || cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)).equals("") || cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)) == null || cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)).equals("0"))) {
                        remainingCase = remainingCase + Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)));
                    }
                    updateDataMap.put(db.KEY_REMAINING_QTY_CASE, String.valueOf(remainingCase));
                    HashMap<String, String> filterInter = new HashMap<>();
                    filterInter.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    db.updateData(db.VAN_STOCK_ITEMS, updateDataMap, filterInter);
                }
                while (c.moveToNext());
            }
        }
        while (cursor.moveToNext());
    }
}
