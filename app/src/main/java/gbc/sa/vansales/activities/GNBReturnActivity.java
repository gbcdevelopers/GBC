package gbc.sa.vansales.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import gbc.sa.vansales.R;

/**
 * Created by eheuristic on 12/8/2016.
 */

public class GNBReturnActivity extends AppCompatActivity {


    ImageView iv_back;
    TextView tv_top_header;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnbreturn);

        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);


        if(getIntent().getExtras()!=null)
        {
            if(getIntent().getExtras().getString("from","g").equals("g"))
            {
                tv_top_header.setText("Good Return");
            }
            else
            {
                tv_top_header.setText("Bad Return");
            }
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
