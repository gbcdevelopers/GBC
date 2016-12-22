package gbc.sa.vansales.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DataPoustingAuditAdapter;
import gbc.sa.vansales.models.DataPoustingAudit;

public class DataPoustingAuditActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    View view1;

    ArrayList<DataPoustingAudit> arrayList = new ArrayList<>();
    ListView listView;
    DataPoustingAuditAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pousting_audit);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Data Pousting Audit");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView)findViewById(R.id.print_document_list);

        getData();

    }

    public void getData()
    {

        for(int i=0;i<15;i++)
        {
            DataPoustingAudit model = new DataPoustingAudit();
            model.setCustomer_id(i);
            model.setCustomer_transection(1010101);
            model.setType("SI");

            arrayList.add(model);
        }

        adapter = new DataPoustingAuditAdapter(DataPoustingAuditActivity.this,arrayList);
        listView.setAdapter(adapter);

    }
}
