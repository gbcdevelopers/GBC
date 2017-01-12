package gbc.sa.vansales.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DataPoustingAuditAdapter;
import gbc.sa.vansales.adapters.SwipeDetector;
import gbc.sa.vansales.models.DataPoustingAudit;

public class DataPoustingAuditActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    View view1;
    public static   SwipeDetector swipeDetector;
    CheckBox checkBox;

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
        tv_top_header.setText("Data Posting Audit");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        checkBox=(CheckBox)findViewById(R.id.checkBox);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                adapter = new DataPoustingAuditAdapter(DataPoustingAuditActivity.this,arrayList,isChecked);
                listView.setAdapter(adapter);

            }
        });

        listView = (ListView)findViewById(R.id.print_document_list);



        swipeDetector = new SwipeDetector();


        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                if (swipeDetector.swipeDetected()) {

                    if (swipeDetector.getAction() == SwipeDetector.Action.RL) {



                        if(position==1 || position==3 || position==4)
                        {
                            Toast.makeText(getApplicationContext(),"return false",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            arrayList.remove(position);
                            adapter.notifyDataSetChanged();
                        }


                    } else {
//                        Toast.makeText(getApplicationContext(),"swipe right",Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {

//                    Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_SHORT).show();


                }
            }

        };


        listView.setOnTouchListener(swipeDetector);
        listView.setOnItemClickListener(listener);


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

        adapter = new DataPoustingAuditAdapter(DataPoustingAuditActivity.this,arrayList,false);
        listView.setAdapter(adapter);

    }



}
