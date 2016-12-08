package gbc.sa.vansales.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gbc.sa.vansales.R;

/**
 * Created by eheuristic on 12/6/2016.
 */

public class PromotionActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    TextView tv_promotion;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Promotions");


        tv_promotion=(TextView)findViewById(R.id.tv_promotion);
        if(getIntent().getExtras()!=null)
        {
            String str_promotion_message=getIntent().getExtras().getString("msg","extra Promotion");
            tv_promotion.setText(str_promotion_message);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });








    }
}
