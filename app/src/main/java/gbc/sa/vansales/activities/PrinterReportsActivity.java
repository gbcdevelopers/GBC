package gbc.sa.vansales.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import gbc.sa.vansales.R;

public class PrinterReportsActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    Button btn_print;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_reports);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);

        btn_print=(Button)findViewById(R.id.btn_print_printer_report);
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PrinterReportsActivity.this,DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


            }
        });

        if(getIntent().getExtras()!=null)
        {
            String from=getIntent().getStringExtra("from");
            if(from.equals("customer"))
            {
                tv_top_header.setText("Print");
            }
            else {
                tv_top_header.setText("Print Reports");
            }

        }else {
            tv_top_header.setText("End Trip");
        }






        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
