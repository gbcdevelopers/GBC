package gbc.sa.vansales.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.PriceListAdapter;
import gbc.sa.vansales.models.ItemList;

public class PriceListCustomerActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    View view1;

    ListView listView;

    ArrayList<ItemList> arrayList = new ArrayList<>();
    PriceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_customer_list);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Price List");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView)findViewById(R.id.list_price_list);

        getData();
    }
    public void getData()
    {


        for(int i=0;i<10;i++)
        {
            ItemList model = new ItemList();
            model.setItem_number(i);
            model.setItem_des("Material item" +
                    "");
            model.setCase_price(100 + i);

            arrayList.add(model);
        }

        adapter = new PriceListAdapter(PriceListCustomerActivity.this,arrayList);
        listView.setAdapter(adapter);
    }

}