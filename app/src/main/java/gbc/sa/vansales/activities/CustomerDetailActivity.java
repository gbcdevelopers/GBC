package gbc.sa.vansales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerOperationAdapter;

/**
 * Created by eheuristic on 12/3/2016.
 */

public class CustomerDetailActivity extends AppCompatActivity{
    GridView gridView;


    CustomerOperationAdapter adapter;
   String strText[]={"Order request","Collections","Sales","Merchandizing","Print","Orders","Messages","PriceList","Promotions","Balances"};

    ImageView iv_back;
    TextView tv_top_header;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        gridView=(GridView)findViewById(R.id.grid);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);

        adapter=new CustomerOperationAdapter(CustomerDetailActivity.this,strText,"CustomerDetailActivity");
        gridView.setAdapter(adapter);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Customer Opt.");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position==1)
                {

                }
                else {
                    switch (position)
                    {
                        case 2:
                            Intent intent2=new Intent(CustomerDetailActivity.this,SalesInvoiceOptionActivity.class);
                            startActivity(intent2);
                            break;

                        case 3:
                            Intent intent3=new Intent(CustomerDetailActivity.this,MerchandizingActivity.class);
                            startActivity(intent3);
                            break;
                        case 8:
                            Intent intent8=new Intent(CustomerDetailActivity.this,PromotionActivity.class);
                            startActivity(intent8);
                            break;
                        default:
                            break;
                    }
                }
            }
        });



    }
}
