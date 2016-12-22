package gbc.sa.vansales.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.PriceListAdapter;
import gbc.sa.vansales.adapters.TodaysOrderAdapter;
import gbc.sa.vansales.models.ItemList;
import gbc.sa.vansales.models.TodaysOrder;

public class TodaysOrderActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    View view1;

    ListView listView;
    ArrayList<TodaysOrder> arrayList = new ArrayList<>();
    TodaysOrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_order);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Todays Order");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView)findViewById(R.id.list_todays_order_list);

        getData();
    }

    public void getData()
    {


        for(int i=0;i<10;i++)
        {
            TodaysOrder model = new TodaysOrder();
            model.setCustomer("Customer "+i);
            model.setOrderNo("Order "+ i);
            model.setPrice("100"+ i);

            arrayList.add(model);
        }

        adapter = new TodaysOrderAdapter(TodaysOrderActivity.this,arrayList);
        listView.setAdapter(adapter);
    }
}
