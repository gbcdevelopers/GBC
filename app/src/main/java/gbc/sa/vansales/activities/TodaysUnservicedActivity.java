package gbc.sa.vansales.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gbc.sa.vansales.R;

public class TodaysUnservicedActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    View view1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_unserviced);

        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Todays Unserviced");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }
}
