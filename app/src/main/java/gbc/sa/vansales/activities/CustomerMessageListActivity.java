package gbc.sa.vansales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DeliveryAdapter;
import gbc.sa.vansales.adapters.MessageListAdapter;
import gbc.sa.vansales.utils.RoundedImageView;

/**
 * Created by eheuristic on 12/10/2016.
 */

public class CustomerMessageListActivity extends AppCompatActivity {



    ImageView iv_back;
    TextView tv_top_header;

    ListView lv_message;
    RoundedImageView iv_round;
  MessageListAdapter adapter;
    String arr[]={"Silent Meeting","Order Confirmed"};
    LinearLayout ll_common;
    ImageView img_refresh;
    ImageView iv_refresh;



    SwipeRefreshLayout refreshLayout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_list);

        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);
        ll_common=(LinearLayout)findViewById(R.id.ll_common);
        img_refresh=(ImageView)findViewById(R.id.img_refresh);
        iv_refresh=(ImageView)findViewById(R.id.iv_refresh);






        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Message List");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_message=(ListView)findViewById(R.id.lv_messages);



        if(getIntent().getExtras()!=null)
        {
            String from=getIntent().getStringExtra("from");
            if(from.equals("dash"))
            {
                ll_common.setVisibility(View.GONE);
                img_refresh.setVisibility(View.GONE);
                iv_refresh.setVisibility(View.VISIBLE);

            }
        }
        else {
            ll_common.setVisibility(View.VISIBLE);
        }



        adapter=new MessageListAdapter(CustomerMessageListActivity.this,arr);
        lv_message.setAdapter(adapter);


        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(refreshLayout.isRefreshing())
                        {
                            refreshLayout.setRefreshing(false);
                            adapter.notifyDataSetChanged();
                        }

                    }
                },2000);
            }
        });

        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchRefresh();
            }
        });


    }


    public void dispatchRefresh()
    {

        refreshLayout.setRefreshing(true);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(refreshLayout.isRefreshing())
                {
                    refreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }

            }
        },2000);
    }

}
