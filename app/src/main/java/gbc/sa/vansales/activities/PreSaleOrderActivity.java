package gbc.sa.vansales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ExpandReturnAdapter;
import gbc.sa.vansales.models.PreSaleProceed;
import gbc.sa.vansales.utils.AnimatedExpandableListView;

public class PreSaleOrderActivity extends AppCompatActivity {

    ArrayList<PreSaleProceed> preSaleProceeds = new ArrayList<>();
    ImageView iv_back;
    TextView tv_top_header;



    AnimatedExpandableListView exp_list;
    public static ExpandReturnAdapter adapter;
    ArrayList<String> arrProduct;
    Button btn_proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sale_order);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        btn_proceed=(Button)findViewById(R.id.btn_confirm_delivery_presale_proceed);
        exp_list=(AnimatedExpandableListView)findViewById(R.id.exp_presale);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("PreSale Order");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        arrProduct=new ArrayList<>();
        arrProduct.add("frahn");
        arrProduct.add("bruno");
        arrProduct.add("dwayn");

        adapter=new ExpandReturnAdapter(PreSaleOrderActivity.this,arrProduct);
        exp_list.setAdapter(adapter);



        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PreSaleOrderActivity.this,PreSaleOrderProceedActivity.class);
                startActivity(intent);
            }
        });

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
