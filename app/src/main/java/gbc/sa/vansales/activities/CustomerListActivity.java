package gbc.sa.vansales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gbc.sa.vansales.R;
/************************************************************
 @ This screen shows the list of customers the driver served
 @ in the particular day. This information is available from
 @ the informations tab button.
 ************************************************************/
public class CustomerListActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    View view1;

    TextView text_today_schedule_customer_list;
    TextView text_today_unserviced_customer_list;
    TextView text_today_all_route_customer_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.customer_list_title));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        text_today_schedule_customer_list = (TextView) findViewById(R.id.text_today_schedule_customer_list);
        text_today_unserviced_customer_list = (TextView) findViewById(R.id.text_today_unserviced_customer_list);
        text_today_all_route_customer_list = (TextView) findViewById(R.id.text_today_all_route_customer_list);

        text_today_schedule_customer_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerListActivity.this,TodaysScheduleActivity.class);
                intent.putExtra("schedule","schedule");
                startActivity(intent);
            }
        });
        text_today_unserviced_customer_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerListActivity.this,TodaysScheduleActivity.class);
                intent.putExtra("schedule","unserviced");
                startActivity(intent);
            }
        });
        text_today_all_route_customer_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerListActivity.this,TodaysScheduleActivity.class);
                intent.putExtra("schedule","all");
                startActivity(intent);
            }
        });

    }
}
