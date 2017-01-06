package gbc.sa.vansales.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.BalanceAdapter;
import gbc.sa.vansales.adapters.PriceListAdapter;
import gbc.sa.vansales.models.BAlanceList;
import gbc.sa.vansales.models.ItemList;

public class BalanceActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    View view1;

    ListView listView;

    ArrayList<BAlanceList> arrayList = new ArrayList<>();
    BalanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Balance");
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
            BAlanceList list=new BAlanceList();


            list.setCustomer(String.valueOf(i));
            list.setInvoice_no(String.valueOf(100+i));
            list.setAmount(String.valueOf(1000+i));
            list.setDue_date("01/0"+i+"2016");
            list.setInv_date("02/0"+i+"2016");

            arrayList.add(list);
        }

        adapter = new BalanceAdapter(BalanceActivity.this,arrayList);
        listView.setAdapter(adapter);
    }

}
