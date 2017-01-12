package gbc.sa.vansales.activities;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gbc.sa.vansales.R;
/**
 * Created by Rakshit on 12-Jan-17.
 */
public class AddCustomerActivity extends AppCompatActivity {
    FloatingActionButton addCustomer;
    ImageView iv_back;
    TextView tv_top_header;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        addCustomer = (FloatingActionButton)findViewById(R.id.addCustomer);
        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
