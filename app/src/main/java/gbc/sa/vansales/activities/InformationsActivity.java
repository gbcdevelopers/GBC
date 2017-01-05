package gbc.sa.vansales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerOperationAdapter;
import gbc.sa.vansales.adapters.PrintDocumentAdapter;

public class InformationsActivity extends AppCompatActivity {

    GridView gridView;
    CustomerOperationAdapter adapter;
    String strText[] = {"CUSTOMER LIST", "ITEM LIST", "TARGET/GOALS",
            "ANALYSIS", "TODAY SUMMARY", "REVIEW", "PRINT \n TRANSACTIONS", "PRINT REPORTS"};
    int resarr[] = {R.drawable.info_customer_list, R.drawable.info_item_list, R.drawable.info_target,
            R.drawable.info_analysis, R.drawable.info_todays_summery, R.drawable.info_review,
            R.drawable.info_print_transaction, R.drawable.info_print_reports};

    ImageView iv_back;
    TextView tv_top_header;
    View view1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        gridView = (GridView) findViewById(R.id.grid_information);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Information");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        adapter = new CustomerOperationAdapter(InformationsActivity.this, strText, resarr, "InformationsActivity");
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view1 = view;

                switch (position) {
                    case 0:
                        Intent customerlist = new Intent(InformationsActivity.this,CustomerListActivity.class);
                        startActivity(customerlist);
                        break;
                    case 1:
                        Intent itemlist = new Intent(InformationsActivity.this,ItemListActivity.class);
                        startActivity(itemlist);
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:
                        Intent todays = new Intent(InformationsActivity.this,TodaysSummaryActivity.class);
                        startActivity(todays);
                        break;
                    case 5:
                        Intent review = new Intent(InformationsActivity.this,ReviewActivity.class);
                        startActivity(review);
                        break;
                    case 6:
                        Intent printdoc= new Intent(InformationsActivity.this,PrintDocumentActivity.class);
                        startActivity(printdoc);

                        break;
                    case 7:
                        Intent print = new Intent(InformationsActivity.this,PrinterReportsActivity.class);
                        print.putExtra("from","info");
                        startActivity(print);

                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
