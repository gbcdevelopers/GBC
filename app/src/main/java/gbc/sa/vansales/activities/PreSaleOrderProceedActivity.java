package gbc.sa.vansales.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.PreSaleProceed;

public class PreSaleOrderProceedActivity extends AppCompatActivity {

    ArrayList<PreSaleProceed> preSaleProceeds = new ArrayList<>();
    ImageView iv_back;
    TextView tv_top_header;
    FloatingActionButton float_presale_proceed;
    int arrSize = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sale_order_proceed);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("PreSale Order");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        float_presale_proceed=(FloatingActionButton)findViewById(R.id.float_presale_proceed);
        float_presale_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrSize = arrSize +1;
                setData();
            }
        });
        setData();

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
            text.setText(saleProceed.getSKU());
            text1.setText(saleProceed.getCTN());
            text2.setText(saleProceed.getBTL());
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
