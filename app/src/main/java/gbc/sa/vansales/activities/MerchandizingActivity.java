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
    String strText[]={"Capture Image","Distribution","Pos/Assets","Survey","Planogram","Price Survey","Advertizing","Item Complaints"};
    int resarr[]={R.drawable.ic_capture,R.drawable.ic_distribution,R.drawable.ic_posasset,R.drawable.ic_survey,R.drawable.ic_planogram,R.drawable.ic_pricesurvey,R.drawable.ic_advertising,R.drawable.ic_itemcomplaint};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_summary);
        ll_layout=(LinearLayout)findViewById(R.id.ll_bottom);
        ll_layout.setVisibility(View.GONE);




        gridView = (GridView) findViewById(R.id.grid);
        gridView.setNumColumns(3);

        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        iv_back.setVisibility(View.VISIBLE);

        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Merchandizing");

        adapter=new CustomerOperationAdapter(MerchandizingActivity.this,strText,resarr,"MerchandizingActivity");
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position)
                {
                    case 0:
                            Intent intent0=new Intent(MerchandizingActivity.this,CaptureImageActivity.class);
                            startActivity(intent0);
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



    }
}
