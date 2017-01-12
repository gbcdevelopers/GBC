package gbc.sa.vansales.activities;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.utils.DatabaseHandler;

/**
 * Created by eheuristic on 12/6/2016.
 */
public class PromotioninfoActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    TextView tv_promotion;
    LinearLayout ll_bottom;
    String str_promotion_message = "";
    Customer object;
    OrderList delivery;
    String invoiceAmount;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);



        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);



        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Promo Details");
        tv_promotion = (TextView) findViewById(R.id.tv_promotion);




        if (getIntent().getExtras() != null) {
            str_promotion_message = getIntent().getExtras().getString("msg", "extra Promotion");
            tv_promotion.setText(str_promotion_message.substring(0, 1).toUpperCase() + str_promotion_message.substring(1).toLowerCase());
            int pos = getIntent().getIntExtra("pos", 10);
            ll_bottom.setVisibility(View.GONE);


        }


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        ll_bottom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                    if (str_promotion_message.equals("review")) {
//                        final Dialog dialog = new Dialog(PromotioninfoActivity.this);
//                        dialog.setContentView(R.layout.dialog_doprint);
//                        dialog.setCancelable(true);
//                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                        LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
//                        LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
//                        dialog.show();
//                        btn_print.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent=new Intent(PromotioninfoActivity.this,ReviewActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//                                finish();
//                            }
//                        });
//                        btn_notprint.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent=new Intent(PromotioninfoActivity.this,ReviewActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//                                finish();
//                            }
//                        });
//                    } else {
//                        finish();
//                    }
//
//            }
//        });
    }

}
