package gbc.sa.vansales.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ItemListAdapter;
import gbc.sa.vansales.models.ItemList;

public class ItemListActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    View view1;

    ListView listView;

    ArrayList<ItemList> arrayList = new ArrayList<>();
    ItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Items List");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView)findViewById(R.id.list_item_list);

        getData();
    }
    public void getData()
    {


        for(int i=0;i<10;i++)
        {
            ItemList model = new ItemList();
            model.setItem_number(i);
            model.setItem_des("Item dec");
            model.setCase_price(100);
            model.setUnit_price(100);
            model.setUpc(1);

            arrayList.add(model);
        }

        adapter = new ItemListAdapter(ItemListActivity.this,arrayList);
        listView.setAdapter(adapter);
    }
}
