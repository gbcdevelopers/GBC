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

public class ReviewActivity extends AppCompatActivity {

    GridView gridView;
    CustomerOperationAdapter adapter;
    String strText[] = {};/*{"PROMOTION LIST", "ROUTE BOOK", "PRICE LIST",
            "NEXT LOAD", "WAREHOUSE STOCK", "TODAYS ORDER", "DATA POSTING AUDIT"};*/
    int resarr[] = {R.drawable.info_pramotion_list, R.drawable.info_route_book, R.drawable.info_price_list,
            R.drawable.info_next_load, R.drawable.info_warehouse_stock, R.drawable.info_todays_order,
            R.drawable.info_data_posting_audit};

    ImageView iv_back;
    TextView tv_top_header;
    View view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        strText = new String[]{getString(R.string.promotionlist),getString(R.string.route_book),getString(R.string.price_list_lbl),
        getString(R.string.next_load_lbl),getString(R.string.warehouse_stock),getString(R.string.today_order),getString(R.string.data_posting_audit)};
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        gridView = (GridView) findViewById(R.id.grid_review);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.review_lbl));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new CustomerOperationAdapter(ReviewActivity.this, strText, resarr, "ReviewActivity");
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view1 = view;

                switch (position) {
                    case 0:
                        Intent promolist = new Intent(ReviewActivity.this, PromotionListActivity.class);
                        promolist.putExtra("from","review");
                       // startActivity(promolist);
                        break;
                    case 1:

                        break;
                    case 2:
                        Intent pricelist = new Intent(ReviewActivity.this, PriceListActivity.class);
                        startActivity(pricelist);
                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:
                        Intent todaysOrder = new Intent(ReviewActivity.this, TodaysOrderActivity.class);
                        startActivity(todaysOrder);
                        break;
                    case 6:
                        Intent data = new Intent(ReviewActivity.this, DataPostingAuditActivity.class);
                        startActivity(data);
                        break;
                    default:
                        break;
                }

            }
        });
    }
}
