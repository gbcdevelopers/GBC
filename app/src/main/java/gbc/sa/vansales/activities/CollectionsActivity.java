package gbc.sa.vansales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ColletionAdapter;
import gbc.sa.vansales.adapters.PagerAdapter;
import gbc.sa.vansales.models.ColletionData;

public class CollectionsActivity extends AppCompatActivity {

    ListView lv_colletions_view;
    ImageView iv_back;
    TextView tv_top_header;
    ArrayList<ColletionData> colletionDatas = new ArrayList<>();
    ColletionAdapter colletionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);

        lv_colletions_view= (ListView) findViewById(R.id.lv_colletions_view);

        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("AR Colletion");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CollectionsActivity.this, PaymentDetails.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        setData();


        lv_colletions_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    public void setData(){

        for (int i=0; i<10; i++){
            ColletionData colletionData = new ColletionData();
            colletionData.setId("16-12-2016/132456458792"+i);
            colletionData.setSelsemanId("10000241"+i);
            colletionData.setAmoutDue("0.2"+i+" AED");
            colletionData.setAmoutAde("0.00");
            colletionDatas.add(colletionData);
        }
        colletionAdapter = new ColletionAdapter(this,colletionDatas);
        lv_colletions_view.setAdapter(colletionAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
