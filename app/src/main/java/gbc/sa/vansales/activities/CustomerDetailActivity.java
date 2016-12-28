package gbc.sa.vansales.activities;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.usb.UsbInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerOperationAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.LoadDeliveryHeader;
import gbc.sa.vansales.utils.DatabaseHandler;
/**
 * Created by eheuristic on 12/3/2016.
 */
public class CustomerDetailActivity extends AppCompatActivity {
    GridView gridView;
    CustomerOperationAdapter adapter;
    String strText[] = {"Order request", "Collections", "Sales", "Merchandizing", "Delivery", "Print"};
    int resarr[] = {R.drawable.ic_order_request, R.drawable.ic_collection, R.drawable.ic_sales, R.drawable.ic_merchandising, R.drawable.ic_distribution, R.drawable.ic_print};
    ImageView iv_back;
    TextView tv_top_header;
    View view1;
    LinearLayout ll_updown, ll_message, ll_promotion;
    ImageView iv_updown;
    Customer object;
    ArrayList<CustomerHeader> customers;
    DatabaseHandler db = new DatabaseHandler(this);
    //    LinearLayout tv_order;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");
        customers = CustomerHeaders.get();

        CustomerHeader customerHeader = CustomerHeader.getCustomer(customers,object.getCustomerID());
        TextView tv_customer_name = (TextView)findViewById(R.id.tv_customer_id);
        TextView tv_customer_address = (TextView)findViewById(R.id.tv_customer_address);
        TextView tv_customer_pobox = (TextView)findViewById(R.id.tv_customer_pobox);
        TextView tv_customer_contact = (TextView)findViewById(R.id.tv_customer_contact);

        TextView tv_credit_days = (TextView)findViewById(R.id.tv_digits);
        TextView tv_credit_limit = (TextView)findViewById(R.id.tv_digits1);
        TextView tv_available_limit = (TextView)findViewById(R.id.tv_digits2);

        if(!(customerHeader==null)){
            tv_customer_name.setText(customerHeader.getCustomerNo() + " " + customerHeader.getName1());
            tv_customer_address.setText(customerHeader.getAddress());
            tv_customer_pobox.setText(customerHeader.getPostCode());
            tv_customer_contact.setText(customerHeader.getPhone());
        }
        else{
            tv_customer_name.setText(object.getCustomerID().toString() + " " + object.getCustomerName().toString());
            tv_customer_address.setText(object.getCustomerAddress().toString());
            tv_customer_pobox.setText("");
            tv_customer_contact.setText("");
        }

        if(object.getPaymentMethod().equalsIgnoreCase("cash")){
            tv_credit_days.setText("0");
            tv_credit_limit.setText("0");
            tv_available_limit.setText("0");
        }
        else{
            try{
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_CUSTOMER_NO,"");
                map.put(db.KEY_CREDIT_LIMIT,"");

                HashMap<String, String>filters = new HashMap<>();
                filters.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                Cursor cursor = db.getData(db.CUSTOMER_CREDIT,map,filters);
                if(cursor.getCount()>0){
                    cursor.moveToFirst();
                    tv_credit_days.setText("0");
                    tv_credit_limit.setText(cursor.getString(cursor.getColumnIndex(db.KEY_CREDIT_LIMIT)));
                    tv_available_limit.setText(cursor.getString(cursor.getColumnIndex(db.KEY_CREDIT_LIMIT)));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                db.close();
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
        tv_top_header.setText("Customer Opt.");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        tv_order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent4=new Intent(CustomerDetailActivity.this,PreSaleOrderActivity.class);
//                startActivity(intent4);
//
//            }
//        });
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
                startActivity(intent);
            }
        });
        ll_promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerDetailActivity.this, PromotionListActivity.class);
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
                        Intent intent1 = new Intent(CustomerDetailActivity.this, CollectionsActivity.class);
                        intent1.putExtra("headerObj", object);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(CustomerDetailActivity.this, SalesInvoiceOptionActivity.class);
                        intent2.putExtra("headerObj", object);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(CustomerDetailActivity.this, MerchandizingActivity.class);
                        intent3.putExtra("headerObj", object);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(CustomerDetailActivity.this, DeliveryActivity.class);
                        intent4.putExtra("headerObj", object);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(CustomerDetailActivity.this, PrinterReportsActivity.class);
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
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
        ll_updown.setVisibility(View.GONE);
    }
}
