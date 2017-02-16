package gbc.sa.vansales.activities;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.usb.UsbInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerOperationAdapter;
import gbc.sa.vansales.adapters.CustomerStatusAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.data.OrderReasons;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.CustomerStatus;
import gbc.sa.vansales.models.LoadDeliveryHeader;
import gbc.sa.vansales.models.Reasons;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by eheuristic on 12/3/2016.
 */
public class CustomerDetailActivity extends AppCompatActivity {
    GridView gridView;
    CustomerOperationAdapter adapter;
    String strText[] = {};/*{getString(R.string.order_request), getString(R.string.order_request), getString(R.string.sales), getString(R.string.merchandizing), getString(R.string.delivery), getString(R.string.print)};*/
    int resarr[] = {R.drawable.ic_order_request, R.drawable.ic_collection, R.drawable.ic_sales, R.drawable.ic_merchandising, R.drawable.ic_distribution, R.drawable.ic_print};
    ImageView iv_back;
    TextView tv_top_header;
    View view1;
    LinearLayout ll_updown, ll_message, ll_promotion, ll_pricelist, ll_balance;
    ImageView iv_updown;
    Customer object;
    ArrayList<CustomerHeader> customers;
    DatabaseHandler db = new DatabaseHandler(this);
    private ArrayList<CustomerStatus> arrayList = new ArrayList<>();
    private ArrayList<Reasons> reasonsList = new ArrayList<>();
    private ArrayAdapter<CustomerStatus> statusAdapter;
    String from = "";
    //    LinearLayout tv_order;
    boolean canPerformSale = true;
    boolean isLimitAvailable = true;
    double totalInvoiceAmount = 0;
    double limit = 0;
    TextView tv_credit_days;
    TextView tv_credit_limit;
    TextView tv_available_limit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        reasonsList = OrderReasons.get();
        statusAdapter = new CustomerStatusAdapter(this, arrayList);
        loadCustomerStatus();

        Log.e("Flag Test","" + App.CustomerRouteControl.getThresholdLimit());
        strText = new String[]{getString(R.string.order_request), getString(R.string.collection), getString(R.string.sales), getString(R.string.merchandizing), getString(R.string.delivery), getString(R.string.print)};
        if (getIntent().getExtras() != null) {
            from = getIntent().getStringExtra("msg");
            if (from.equals("visit") || from.equals("all")) {
                Intent i = this.getIntent();
                object = (Customer) i.getParcelableExtra("headerObj");
                if (object == null) {
                    object = Const.allCustomerdataArrayList.get(Const.customerPosition);
                }
                customers = CustomerHeaders.get();
                CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, object.getCustomerID());
                TextView tv_customer_name = (TextView) findViewById(R.id.tv_customer_id);
                TextView tv_customer_address = (TextView) findViewById(R.id.tv_customer_address);
                TextView tv_customer_pobox = (TextView) findViewById(R.id.tv_customer_pobox);
                TextView tv_customer_contact = (TextView) findViewById(R.id.tv_customer_contact);
                tv_credit_days = (TextView) findViewById(R.id.tv_digits);
                tv_credit_limit = (TextView) findViewById(R.id.tv_digits1);
                tv_available_limit = (TextView) findViewById(R.id.tv_digits2);
                if (!(customerHeader == null)) {
                    if(Settings.getString(App.LANGUAGE).equals("en")){
                        tv_customer_name.setText(StringUtils.stripStart(customerHeader.getCustomerNo(), "0") + " " + UrlBuilder.decodeString(customerHeader.getName1()));
                    }
                    else{
                        tv_customer_name.setText(StringUtils.stripStart(customerHeader.getCustomerNo(), "0") + " " + customerHeader.getName3());
                    }
                    tv_customer_address.setText(UrlBuilder.decodeString(customerHeader.getStreet()));
                    tv_customer_pobox.setText(getString(R.string.pobox) + " " + customerHeader.getPostCode());
                    tv_customer_contact.setText(customerHeader.getPhone());
                } else {
                    tv_customer_name.setText(StringUtils.stripStart(object.getCustomerID(), "0") + " " + object.getCustomerName().toString());
                    tv_customer_address.setText(object.getCustomerAddress().toString());
                    tv_customer_pobox.setText("");
                    tv_customer_contact.setText("");
                }
                if (object.getPaymentMethod().equalsIgnoreCase(App.CASH_CUSTOMER)) {
                    tv_credit_days.setText("0");
                    tv_credit_limit.setText("0");
                    tv_available_limit.setText("0");
                } else {
                    calculateAvailableLimit();
                    /*try {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(db.KEY_CUSTOMER_NO, "");
                        map.put(db.KEY_CREDIT_LIMIT, "");
                        HashMap<String, String> filters = new HashMap<>();
                        filters.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                        Cursor cursor = db.getData(db.CUSTOMER_CREDIT, map, filters);
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            tv_credit_days.setText("0");
                            tv_credit_limit.setText(cursor.getString(cursor.getColumnIndex(db.KEY_CREDIT_LIMIT)));
                            tv_available_limit.setText(cursor.getString(cursor.getColumnIndex(db.KEY_CREDIT_LIMIT)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        db.close();
                    }*/
                }
            }
        }
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        gridView = (GridView) findViewById(R.id.grid);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
//        tv_order=(LinearLayout) findViewById(R.id.ll_order);
        adapter = new CustomerOperationAdapter(CustomerDetailActivity.this, strText, resarr, "CustomerDetailActivity");
        gridView.setAdapter(adapter);
        iv_updown = (ImageView) findViewById(R.id.iv_updown);
        ll_updown = (LinearLayout) findViewById(R.id.ll_updown);
        ll_message = (LinearLayout) findViewById(R.id.ll_message);
        ll_promotion = (LinearLayout) findViewById(R.id.ll_promotion);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.customeroperation));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shouldShowDialog()) {
                    showStatusDialog();
                } else {
                    HashMap<String, String> filter = new HashMap<String, String>();
                    //filter.put(db.KEY_CUSTOMER_IN_TIMESTAMP, Helpers.getCurrentTimeStamp());
                    filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    filter.put(db.KEY_IS_VISITED, App.IS_NOT_COMPLETE);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(db.KEY_CUSTOMER_OUT_TIMESTAMP, Helpers.getCurrentTimeStamp());
                    map.put(db.KEY_IS_VISITED, App.IS_COMPLETE);
                    map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    db.updateData(db.VISIT_LIST, map, filter);

                    //Preparing for POST
                    HashMap<String,String> updateMap = new HashMap<String, String>();
                    updateMap.put(db.KEY_END_TIMESTAMP,Helpers.getCurrentTimeStamp());
                    updateMap.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                    HashMap<String,String> filterMap = new HashMap<String, String>();
                    filterMap.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                    filterMap.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    db.updateData(db.VISIT_LIST_POST,updateMap,filterMap);

                    if(Helpers.isNetworkAvailable(getApplicationContext())){
                        Helpers.createBackgroundJob(getApplicationContext());
                    }

                    Intent intent = new Intent(CustomerDetailActivity.this, SelectCustomerActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        ll_pricelist = (LinearLayout) findViewById(R.id.ll_pricelist);
        ll_balance = (LinearLayout) findViewById(R.id.ll_balance);
//        tv_order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent4=new Intent(CustomerDetailActivity.this,PreSaleOrderActivity.class);
//                startActivity(intent4);
//
//            }
//        });
        ll_pricelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerDetailActivity.this, PriceListCustomerActivity.class);
                intent.putExtra("headerObj", object);
                startActivity(intent);
            }
        });
        ll_balance = (LinearLayout) findViewById(R.id.ll_balance);
        ll_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerDetailActivity.this, BalanceActivity.class);
                intent.putExtra("headerObj", object);
                startActivity(intent);
            }
        });
        iv_updown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_updown.getVisibility() == View.VISIBLE) {
                    ll_updown.setVisibility(View.GONE);
                    iv_updown.setImageResource(R.drawable.ic_up);
                } else {
                    ll_updown.setVisibility(View.VISIBLE);
                    iv_updown.setImageResource(R.drawable.ic_down_arrow);
                }
            }
        });
        ll_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerDetailActivity.this, CustomerMessageListActivity.class);
                intent.putExtra("from", object.getCustomerID());
                intent.putExtra("headerObj", object);
                startActivity(intent);
            }
        });
        ll_promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerDetailActivity.this, PromotionListActivity.class);
                intent.putExtra("from", "review");
                intent.putExtra("headerObj", object);
                startActivity(intent);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view1 = view;
//                if(position==0)
//                {
//
//                }
                switch (position) {
                    case 0:
                        Intent intent = new Intent(CustomerDetailActivity.this, PreSaleOrderActivity.class);
                        intent.putExtra("headerObj", object);
                        startActivity(intent);
                        break;
                    case 1:
                        if(App.CustomerRouteControl.isCollection()){
                            Intent intent1 = new Intent(CustomerDetailActivity.this, CollectionsActivity.class);
                            intent1.putExtra("headerObj", object);
                            startActivity(intent1);
                            break;
                        }
                        else{
                            Toast.makeText(CustomerDetailActivity.this,getString(R.string.feature_blocked),Toast.LENGTH_SHORT).show();
                            break;
                        }

                    case 2:
                        if(canPerformSale()&&isLimitAvailable){
                            Intent intent2 = new Intent(CustomerDetailActivity.this, SalesInvoiceOptionActivity.class);
                            intent2.putExtra("from", "customerdetail");
                            intent2.putExtra("headerObj", object);
                            startActivity(intent2);
                            break;
                        }
                        else{
                            Toast.makeText(CustomerDetailActivity.this,getString(R.string.pending_invoice),Toast.LENGTH_SHORT).show();
                        }

                    case 3:
                        Intent intent3 = new Intent(CustomerDetailActivity.this, MerchandizingActivity.class);
                        intent3.putExtra("headerObj", object);
                        Toast.makeText(CustomerDetailActivity.this,getString(R.string.feature_blocked),Toast.LENGTH_SHORT).show();
                        //startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(CustomerDetailActivity.this, DeliveryActivity.class);
                        intent4.putExtra("headerObj", object);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(CustomerDetailActivity.this, PrintCustomerActivity.class);
                        intent5.putExtra("headerObj", object);
                        intent5.putExtra("from", "customer");
                        startActivity(intent5);
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private void loadCustomerStatus() {
        for (Reasons reason : reasonsList) {
            CustomerStatus status = new CustomerStatus();
            if (reason.getReasonType().equals(App.VisitReasons)) {
                status.setReasonCode(reason.getReasonID());
                if(Settings.getString(App.LANGUAGE).equals("en")){
                    status.setReasonDescription(UrlBuilder.decodeString(reason.getReasonDescription()));
                }
                else{
                    status.setReasonDescription(UrlBuilder.decodeString(reason.getReasonDescriptionAr()));
                }
                if(status.getReasonCode().contains("N")){
                    arrayList.add(status);
                }
                //arrayList.add(status);
            }
        }
        statusAdapter.notifyDataSetChanged();
    }
    private boolean shouldShowDialog() {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());

        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
        filter.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);

        if (db.checkData(db.ORDER_REQUEST, map) ||
            db.checkData(db.CAPTURE_SALES_INVOICE, map) ||
            db.checkData(db.CUSTOMER_DELIVERY_ITEMS_POST, map) ||
            db.checkData(db.COLLECTION, filter) ||
            db.checkData(db.RETURNS,filter)) {
            return false;
        } else {
            return true;
        }
    }
    private void showStatusDialog() {
        final Dialog dialog = new Dialog(CustomerDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = getLayoutInflater().inflate(R.layout.activity_select_customer_status, null);
        TextView tv_header = (TextView) view.findViewById(R.id.tv_top_header);
        tv_header.setText(getString(R.string.nonservicedreason));
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
                HashMap<String, String> filter = new HashMap<String, String>();
                //filter.put(db.KEY_CUSTOMER_IN_TIMESTAMP, Helpers.getCurrentTimeStamp());
                filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                filter.put(db.KEY_IS_VISITED, App.IS_COMPLETE);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(db.KEY_VISIT_UNSERVICED_REASON, arrayList.get(position).getReasonCode());
                map.put(db.KEY_CUSTOMER_OUT_TIMESTAMP, Helpers.getCurrentTimeStamp());
                map.put(db.KEY_IS_VISITED, App.IS_COMPLETE);
                map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                db.updateData(db.VISIT_LIST, map, filter);

                //Preparing for POST
                HashMap<String,String> updateMap = new HashMap<String, String>();
                updateMap.put(db.KEY_END_TIMESTAMP,Helpers.getCurrentTimeStamp());
                updateMap.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                updateMap.put(db.KEY_VISIT_SERVICED_REASON, arrayList.get(position).getReasonCode());
                HashMap<String,String> filterMap = new HashMap<String, String>();
                filterMap.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                filterMap.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                db.updateData(db.VISIT_LIST_POST, updateMap, filterMap);

                if(Helpers.isNetworkAvailable(CustomerDetailActivity.this)){
                    Helpers.createBackgroundJob(CustomerDetailActivity.this);
                }
                Intent intent = new Intent(CustomerDetailActivity.this, SelectCustomerActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }

    /*To check if customer has any pending invoices due by today*/
    private boolean canPerformSale(){
        /*HashMap<String,String> map = new HashMap<>();
        map.put(db.KEY_CUSTOMER_NO,"");
        map.put(db.KEY_INVOICE_NO,"");
        map.put(db.KEY_INVOICE_AMOUNT,"");
        map.put(db.KEY_DUE_DATE,"");
        map.put(db.KEY_INVOICE_DATE,"");
        map.put(db.KEY_AMOUNT_CLEARED,"");
        map.put(db.KEY_IS_INVOICE_COMPLETE,"");
        HashMap<String,String>filter = new HashMap<>();
        filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());

        Cursor cursor = db.getData(db.COLLECTION,map,filter);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            Date today = new Date();
            Log.e("Today","" + today);
            do{
                Date dueDate = Helpers.stringToDate(cursor.getString(cursor.getColumnIndex(db.KEY_DUE_DATE)),App.DATE_FORMAT_HYPHEN);
                if(dueDate.compareTo(today)<0){
                    return false;
                }
            }
            while (cursor.moveToNext());
            return false;
        }
        else{
            return true;
        }*/
        return true;
    }
    /*To calculate available limit*/
    private void calculateAvailableLimit(){
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO, "");
            map.put(db.KEY_CREDIT_LIMIT, "");
            HashMap<String, String> filters = new HashMap<>();
            filters.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
            Cursor cursor = db.getData(db.CUSTOMER_CREDIT, map, filters);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                tv_credit_days.setText("0");
                tv_credit_limit.setText(cursor.getString(cursor.getColumnIndex(db.KEY_CREDIT_LIMIT)));
                limit = Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_CREDIT_LIMIT)));
                HashMap<String,String> map1 = new HashMap<>();
                map1.put(db.KEY_CUSTOMER_NO,"");
                map1.put(db.KEY_INVOICE_NO,"");
                map1.put(db.KEY_INVOICE_AMOUNT,"");
                map1.put(db.KEY_DUE_DATE,"");
                map1.put(db.KEY_INVOICE_DATE,"");
                map1.put(db.KEY_AMOUNT_CLEARED,"");
                map1.put(db.KEY_IS_INVOICE_COMPLETE,"");
                HashMap<String,String>filter = new HashMap<>();
                filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                Cursor c = db.getData(db.COLLECTION,map1,filter);
                if(c.getCount()>0){
                    c.moveToFirst();
                    do{
                        totalInvoiceAmount+=Double.parseDouble(c.getString(c.getColumnIndex(db.KEY_INVOICE_AMOUNT)));
                    }
                    while (c.moveToNext());
                }
                Log.e("Total Invoice", "" + totalInvoiceAmount);
                if(limit-totalInvoiceAmount==0){
                    isLimitAvailable = false;
                }
                tv_available_limit.setText(String.valueOf(limit-totalInvoiceAmount));
            }
            else{
                tv_credit_days.setText("0");
                tv_credit_limit.setText("0");
                tv_available_limit.setText("0");
                isLimitAvailable = true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
        ll_updown.setVisibility(View.GONE);
    }
}
