package gbc.sa.vansales.activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.utils.Settings;
/**
 * Created by Rakshit on 21-Feb-17.
 */
public class DriverCollectionActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    TextView tv_driver_name;
    TextView tv_driver_id;
    TextView tv_total_amount;
    TextView tv_amount_due;
    TextView tv_invoice_amount;
    FloatingActionButton fab;
    String amountdue = "200";
    String cleared = "800";
    String total = "1000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_collection);


        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.driver_collection));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_driver_name = (TextView)findViewById(R.id.tv_driver_name);
        tv_driver_id = (TextView)findViewById(R.id.tv_driver_id);
        tv_total_amount = (TextView)findViewById(R.id.tv_total_amount);
        tv_amount_due = (TextView)findViewById(R.id.tv_amount_due);
        tv_invoice_amount = (TextView)findViewById(R.id.tv_invoice_amount);

        tv_driver_name.setText(getString(R.string.driver_name) + " : " + Settings.getString(App.DRIVER_NAME_EN));
        tv_driver_id.setText(getString(R.string.driver_id) + " : " + Settings.getString(App.DRIVER));
        tv_total_amount.setText(getString(R.string.total_amount) + " : " +total);
        tv_amount_due.setText(getString(R.string.amout_due) + " : " + amountdue);
        tv_invoice_amount.setText(cleared);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverCollectionActivity.this,DriverCollectionDetailsActivity.class);
                intent.putExtra("from","driver");
                intent.putExtra("amountdue",amountdue);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }
}
