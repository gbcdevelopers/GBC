package gbc.sa.vansales.activities;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
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
import gbc.sa.vansales.models.PreSaleProceed;

public class DeliveryOrderActivity extends AppCompatActivity {

    ArrayList<PreSaleProceed> preSaleProceeds = new ArrayList<>();
    ImageView iv_back;
    TextView tv_top_header;
    FloatingActionButton float_presale_proceed;
    int arrSize = 3;

    Button btn_confirm_delivery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Delivery Order");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_confirm_delivery=(Button)findViewById(R.id.btn_confirm_delivery);

        float_presale_proceed=(FloatingActionButton)findViewById(R.id.float_presale_proceed);
        float_presale_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrSize = arrSize +1;
                setData();
            }
        });
        setData();

        btn_confirm_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(DeliveryOrderActivity.this);
                dialog.setContentView(R.layout.print_dialog);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ImageView iv_cancle=(ImageView)dialog.findViewById(R.id.imageView_close);
                iv_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();

            }
        });


    }

    public void setData() {
        if (preSaleProceeds != null){
            preSaleProceeds.clear();
        }
        for (int i = 0; i < arrSize; i++) {
            Log.d("iiii","-->"+i);
            PreSaleProceed proceed = new PreSaleProceed();
            proceed.setSKU("Berain 250 ml");
            proceed.setCTN("6");
            proceed.setBTL("50");
            proceed.setPRICE("1 SAR");
            preSaleProceeds.add(proceed);
        }
        setLayout();
    }

    private void setLayout() {
        LinearLayout options_layout = (LinearLayout) findViewById(R.id.ll_presale_proceed_main);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        if (options_layout != null){
            options_layout.removeAllViews();
        }
        Log.d("size","-->"+preSaleProceeds.size());
        for (int i = 0; i < preSaleProceeds.size(); i++) {
            Log.d("i","-->"+i);
            View to_add = inflater.inflate(R.layout.presale_proceed_list_item,
                    options_layout, false);

            PreSaleProceed saleProceed = preSaleProceeds.get(i);
            TextView text = (TextView) to_add.findViewById(R.id.tv_sku_pre_proceed);
            TextView text1 = (TextView) to_add.findViewById(R.id.tv_ctn_pre_proceed);
            TextView text2 = (TextView) to_add.findViewById(R.id.tv_btl_pre_proceed);
            TextView text3 = (TextView) to_add.findViewById(R.id.tv_price);
            text3.setVisibility(View.VISIBLE);
            text.setText(saleProceed.getSKU());
            text1.setText(saleProceed.getCTN());
            text2.setText(saleProceed.getBTL());
            text3.setText(saleProceed.getPRICE());
//            text.setTypeface(FontSelector.getBold(getActivity()));
            options_layout.addView(to_add);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}