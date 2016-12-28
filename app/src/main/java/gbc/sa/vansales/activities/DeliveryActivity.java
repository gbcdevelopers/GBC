package gbc.sa.vansales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DeliveryAdapter;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.utils.DatabaseHandler;
/**
 * Created by eheuristic on 12/10/2016.
 */

public class DeliveryActivity extends AppCompatActivity {



    ImageView iv_back;
    TextView tv_top_header;
    ListView list_delivery;
    DeliveryAdapter adapter;
    FloatingActionButton flt_button;
    Customer object;
    ArrayList<CustomerHeader> customers;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_delivery_list);
        Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");
        customers = CustomerHeaders.get();

        CustomerHeader customerHeader = CustomerHeader.getCustomer(customers,object.getCustomerID());
        TextView tv_customer_name = (TextView)findViewById(R.id.tv_customer_id);
        TextView tv_customer_address = (TextView)findViewById(R.id.tv_customer_address);
        TextView tv_customer_pobox = (TextView)findViewById(R.id.tv_customer_pobox);
        TextView tv_customer_contact = (TextView)findViewById(R.id.tv_customer_contact);
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

        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Delivery list");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        flt_button=(FloatingActionButton)findViewById(R.id.flt_presale);
        flt_button.setVisibility(View.GONE);


        list_delivery=(ListView)findViewById(R.id.list_delivery);

        adapter=new DeliveryAdapter(DeliveryActivity.this,2,R.layout.custom_delivery,"delivery");
        list_delivery.setAdapter(adapter);



        list_delivery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent=new Intent(DeliveryActivity.this,DeliveryOrderActivity.class);
                startActivity(intent);


            }
        });




    }
}
