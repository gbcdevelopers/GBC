package gbc.sa.vansales.activities;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerOperationAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.utils.DatabaseHandler;
/**
 * Created by eheuristic on 12/5/2016.
 */
public class SalesInvoiceOptionActivity extends AppCompatActivity {
    GridView gridView;
    CustomerOperationAdapter adapter;
    String strText[] = {"Shelf Stock", "Sales Invoice", "Invoice", "End Invoice"};
    int resarr[] = {R.drawable.ic_self_stock, R.drawable.ic_sales_invoice, R.drawable.ic_invoice, R.drawable.ic_endinvoice};
    ImageView iv_back;
    TextView tv_top_header;
    ImageView iv_updown;
    Customer object;
    ArrayList<CustomerHeader> customers;
    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");
        customers = CustomerHeaders.get();
        CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, object.getCustomerID());
        TextView tv_customer_name = (TextView) findViewById(R.id.tv_customer_id);
        TextView tv_customer_address = (TextView) findViewById(R.id.tv_customer_address);
        TextView tv_customer_pobox = (TextView) findViewById(R.id.tv_customer_pobox);
        TextView tv_customer_contact = (TextView) findViewById(R.id.tv_customer_contact);
        TextView tv_credit_days = (TextView) findViewById(R.id.tv_digits);
        TextView tv_credit_limit = (TextView) findViewById(R.id.tv_digits1);
        TextView tv_available_limit = (TextView) findViewById(R.id.tv_digits2);
        if (!(customerHeader == null)) {
            tv_customer_name.setText(customerHeader.getCustomerNo() + " " + customerHeader.getName1());
            tv_customer_address.setText(customerHeader.getAddress());
            tv_customer_pobox.setText(customerHeader.getPostCode());
            tv_customer_contact.setText(customerHeader.getPhone());
        } else {
            tv_customer_name.setText(object.getCustomerID().toString() + " " + object.getCustomerName().toString());
            tv_customer_address.setText(object.getCustomerAddress().toString());
            tv_customer_pobox.setText("");
            tv_customer_contact.setText("");
        }
        if (object.getPaymentMethod().equalsIgnoreCase("cash")) {
            tv_credit_days.setText("0");
            tv_credit_limit.setText("0");
            tv_available_limit.setText("0");
        } else {
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
        }
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        gridView = (GridView) findViewById(R.id.grid);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_updown = (ImageView) findViewById(R.id.iv_updown);
        adapter = new CustomerOperationAdapter(SalesInvoiceOptionActivity.this, strText, resarr, "SalesInvoiceOptionActivity");
        gridView.setAdapter(adapter);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        iv_updown.setVisibility(View.INVISIBLE);
        tv_top_header.setText("Sales Invoice Opt.");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(SalesInvoiceOptionActivity.this, ShelfStockActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(SalesInvoiceOptionActivity.this, SalesInvoiceActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(SalesInvoiceOptionActivity.this, InvoiceSummeryActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(SalesInvoiceOptionActivity.this, PromotionListActivity.class);
                        startActivity(intent3);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
