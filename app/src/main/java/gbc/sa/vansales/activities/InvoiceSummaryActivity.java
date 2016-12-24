package gbc.sa.vansales.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.SalesAdapter;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class InvoiceSummaryActivity extends AppCompatActivity {


    GridView gridView;

    TextView tv_top_header;
    ImageView iv_back;

    SalesAdapter adapter;
    ArrayList<String> arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_summary);
        gridView = (GridView) findViewById(R.id.grid);

        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        iv_back.setVisibility(View.VISIBLE);

        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Invoice Summary");



        arrayList = new ArrayList<>();
        arrayList.add("A");
        arrayList.add("B");
        arrayList.add("C");
        arrayList.add("d");
        arrayList.add("e");
        arrayList.add("f");
        adapter=new SalesAdapter(InvoiceSummaryActivity.this,arrayList, R.layout.custom_invoice_summary,"");
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
