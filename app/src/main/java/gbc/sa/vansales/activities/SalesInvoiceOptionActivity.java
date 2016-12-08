package gbc.sa.vansales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerOperationAdapter;
import gbc.sa.vansales.data.Const;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class SalesInvoiceOptionActivity extends AppCompatActivity {

    GridView gridView;


    CustomerOperationAdapter adapter;
    String strText[] = {"Shelf Stock", "Sales Invoice", "Invoice", "End Invoice"};
    int resarr[]={R.drawable.ic_shelfstock,R.drawable.ic_salesinvoice,R.drawable.ic_invoice,R.drawable.ic_endinvoice};

    ImageView iv_back;
    TextView tv_top_header;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);


        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        gridView = (GridView) findViewById(R.id.grid);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);

        adapter = new CustomerOperationAdapter(SalesInvoiceOptionActivity.this, strText,resarr, "SalesInvoiceOptionActivity");
        gridView.setAdapter(adapter);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
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
                        Intent intent=new Intent(SalesInvoiceOptionActivity.this,ShelfStockActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent1=new Intent(SalesInvoiceOptionActivity.this,SalesInvoiceActivity.class);
                        startActivity(intent1);
                        break;

                    case 2:
                        Intent intent2=new Intent(SalesInvoiceOptionActivity.this,InvoiceSummaryActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:

                        Intent intent3=new Intent(SalesInvoiceOptionActivity.this,PromotionListActivity.class);
                        startActivity(intent3);
                        break;

                    default:
                        break;
                }

            }
        });
    }
}
