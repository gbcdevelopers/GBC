package gbc.sa.vansales.activities;
import android.content.Intent;
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

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ColletionAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.ColletionData;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.utils.DatabaseHandler;
public class CollectionsActivity extends AppCompatActivity {
    ListView lv_colletions_view;
    ImageView iv_back;
    TextView tv_top_header;
    ArrayList<ColletionData> colletionDatas = new ArrayList<>();
    ColletionAdapter colletionAdapter;
    TextView tv_amt_paid;
    double amount = 0.00;
    int pos = 0;
    Customer object;
    ArrayList<CustomerHeader> customers;
    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        final Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");
        if (object == null) {
            object = Const.allCustomerdataArrayList.get(Const.customerPosition);
        }
        customers = CustomerHeaders.get();
        CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, object.getCustomerID());
        TextView tv_customer_name = (TextView) findViewById(R.id.tv_customer_id);
        TextView tv_method_of_payment = (TextView) findViewById(R.id.tv_method_of_payment);
        if (!(customerHeader == null)) {
            tv_customer_name.setText(StringUtils.stripStart(customerHeader.getCustomerNo(), "0") + " " + customerHeader.getName1());
        } else {
            tv_customer_name.setText(StringUtils.stripStart(object.getCustomerID(), "0") + " " + object.getCustomerName().toString());
        }
        if (object.getPaymentMethod().equalsIgnoreCase("cash")) {
            tv_method_of_payment.setText(getString(R.string.methodofPayment) + "-" + getString(R.string.cash));
        } else {
            tv_method_of_payment.setText(getString(R.string.methodofPayment) + "-" + getString(R.string.credit));
        }
        lv_colletions_view = (ListView) findViewById(R.id.lv_colletions_view);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.collection));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Intent intent = new Intent(CollectionsActivity.this, PaymentDetails.class);
                intent.putExtra("from","collection");
                intent.putExtra("msg", "collection");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
//                startActivityForResult(intent, 1);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        setData();
        lv_colletions_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CollectionsActivity.this, PaymentDetails.class);
                intent.putExtra("msg", "collection");
                intent.putExtra("from","collection");
                intent.putExtra("pos", position);
                startActivity(intent);
//                startActivityForResult(intent, 1);
            }
        });
    }
    public void setData() {
        for (int i = 0; i < 10; i++) {
            ColletionData colletionData = new ColletionData();
            colletionData.setId("16-12-2016/132456458792" + i);
            colletionData.setSelsemanId("10000241" + i);
            colletionData.setAmoutDue(String.valueOf(100 + i));
//            if(pos==i)
//            {
//                colletionData.setAmoutAde(String.valueOf(amount));
//            }
//            else {
//                colletionData.setAmoutAde("0.00");
//            }
            colletionData.setAmoutAde("0.00");
            colletionDatas.add(colletionData);
            Const.colletionDatas = colletionDatas;
        }
        colletionAdapter = new ColletionAdapter(this, colletionDatas);
        lv_colletions_view.setAdapter(colletionAdapter);
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
                    ColletionData colletionData = colletionDatas.get(pos);
                    colletionData.setAmoutAde(amt);
                    double amountdue = Double.parseDouble(colletionData.getAmoutDue()) - amount;
                    Log.v("amountdue", colletionData.getAmoutDue() + "");
                    colletionData.setAmoutDue(String.valueOf(amountdue));
                    colletionAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
