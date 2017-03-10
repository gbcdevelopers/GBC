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
import gbc.sa.vansales.adapters.SalesAdapter;
/**
 * Created by eheuristic on 12/5/2016.
 */
public class MerchandizingActivity extends AppCompatActivity {
    GridView gridView;
    TextView tv_top_header;
    ImageView iv_back;
    LinearLayout ll_layout;
    CustomerOperationAdapter adapter;
    String strText[] = {"Capture Image", "Distribution", "Pos/Assets", "Survey", "Planogram", "Price Survey", "Advertizing", "Item Complaints"};
    int resarr[] = {R.drawable.ic_capture_image, R.drawable.ic_distribution, R.drawable.ic_pos_assets, R.drawable.ic_survey, R.drawable.ic_planogram, R.drawable.ic_price_survey, R.drawable.ic_advertising, R.drawable.ic_item_complete};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_summary);
        ll_layout = (LinearLayout) findViewById(R.id.ll_bottom);
        ll_layout.setVisibility(View.GONE);
        gridView = (GridView) findViewById(R.id.grid);
        gridView.setNumColumns(3);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Merchandising");
        adapter = new CustomerOperationAdapter(MerchandizingActivity.this, strText, resarr, "MerchandizingActivity");
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //Campaign Capture
                        Intent intent0 = new Intent(MerchandizingActivity.this, CaptureImageActivity.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        //Distribution/Shelf Stock
                        Intent intent1 = new Intent(MerchandizingActivity.this, CaptureImageActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        //Pos/Assets
                        Intent intent2 = new Intent(MerchandizingActivity.this, CaptureImageActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        //Survey
                        Intent intent3 = new Intent(MerchandizingActivity.this, SurveyActivity.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        //Planogram
                        Intent intent4 = new Intent(MerchandizingActivity.this, PlanogramActivity.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        //Price Survey
                        Intent intent5 = new Intent(MerchandizingActivity.this, CaptureImageActivity.class);
                        startActivity(intent5);
                        break;
                    case 6:
                        //Advertising
                        Intent intent6 = new Intent(MerchandizingActivity.this, CaptureImageActivity.class);
                        startActivity(intent6);
                        break;
                    case 7:
                        //Item Complaint
                        Intent intent7 = new Intent(MerchandizingActivity.this, CaptureImageActivity.class);
                        startActivity(intent7);
                        break;
                    default:
                        break;
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return;
    }
}
