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

import java.util.ArrayList;
import java.util.List;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.PresaleAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.PreSaleProceed;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.UrlBuilder;
public class PreSaleOrderActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    ListView list_delivery;

    PresaleAdapter presaleAdapterdapter;
    FloatingActionButton flt_presale;

    ArrayList<PreSaleProceed> proceedArrayList1;
    Customer object;
    ArrayList<CustomerHeader> customers;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_list);
        Const.constantsHashMap.clear();

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
            tv_customer_address.setText(UrlBuilder.decodeString(customerHeader.getStreet()));
            tv_customer_pobox.setText("PO Code " + customerHeader.getPostCode());
            tv_customer_contact.setText(customerHeader.getPhone());
        }
        else{
            tv_customer_name.setText(object.getCustomerID().toString() + " " + object.getCustomerName().toString());
            tv_customer_address.setText(object.getCustomerAddress().toString());
            tv_customer_pobox.setText("");
            tv_customer_contact.setText("");
        }


        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        flt_presale=(FloatingActionButton)findViewById(R.id.flt_presale);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("PreSale Order");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list_delivery = (ListView) findViewById(R.id.list_delivery);



        proceedArrayList1=new ArrayList<>();


        presaleAdapterdapter = new PresaleAdapter(PreSaleOrderActivity.this, R.layout.custom_delivery, proceedArrayList1);
        list_delivery.setAdapter(presaleAdapterdapter);



        flt_presale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PreSaleOrderActivity.this, PreSaleOrderProceedActivity.class);
                intent.putExtra("from","button");
                startActivity(intent);

            }
        });





        list_delivery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(PreSaleOrderActivity.this, PreSaleOrderProceedActivity.class);
                intent.putExtra("from","list");
                intent.putExtra("pos",position);
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (proceedArrayList1 != null) {

            proceedArrayList1.clear();
        }



        Log.v("hashmap",Const.constantsHashMap.size()+"");
        for(int i=0;i<Const.constantsHashMap.size();i++)
        {

            List<LoadRequestConstants> constantses=Const.constantsHashMap.get(i);
            PreSaleProceed proceed=new PreSaleProceed();

            proceed.setDATE(Const.constantsHashMap.get(i).get(i).getDate());

            proceedArrayList1.add(proceed);
            for(int j=0;j<constantses.size();j++)
            {
                Log.v("itemname",constantses.get(j).getItemName());
            }
        }




//        for(int i=0;i<Const.constantsHashMap.size();i++)
//        {
//            proceedArrayList.add(i);
//            Log.v("size",Const.constantsHashMap.get(i).get(i).getItemName());
//
//        }
        if(Const.constantsHashMap.size()>0)
        {
            presaleAdapterdapter = new PresaleAdapter(PreSaleOrderActivity.this, R.layout.custom_delivery,proceedArrayList1);
            list_delivery.setAdapter(presaleAdapterdapter);
        }




    }


}
