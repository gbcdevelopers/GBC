package gbc.sa.vansales.activities;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.data.DriverRouteFlags;
public class PrinterReportsActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    Button btn_print;
    App.DriverRouteControl flag = new App.DriverRouteControl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_reports);
        flag = DriverRouteFlags.get();
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        btn_print = (Button) findViewById(R.id.btn_print_printer_report);
        if(!(flag==null)){
            if(!flag.isEodSalesReports()){
                btn_print.setAlpha(0.5f);
                btn_print.setEnabled(false);
            }
        }
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(PrinterReportsActivity.this);
                dialog.setContentView(R.layout.dialog_doprint);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                btn_print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(PrinterReportsActivity.this, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
                btn_notprint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(PrinterReportsActivity.this, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        });
        if (getIntent().getExtras() != null) {
            String from = getIntent().getStringExtra("from");
            if (from.equals("customer")) {
                tv_top_header.setText("Print");
            } else {
                tv_top_header.setText("Print Reports");
            }
        } else {
            tv_top_header.setText("End Trip");
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrinterReportsActivity.this, InformationsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
