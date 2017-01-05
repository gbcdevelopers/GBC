package gbc.sa.vansales.activities;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
public class PromotionActivity extends AppCompatActivity {
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

        Intent i = this.getIntent();
        object = (Customer)i.getParcelableExtra("headerObj");
        delivery = (OrderList)i.getParcelableExtra("delivery");

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);

        EditText tv_invoice_amount = (EditText) findViewById(R.id.tv_invoice_amount);
        tv_invoice_amount.setText(i.getExtras().getString("invoiceamount"));
        invoiceAmount = i.getExtras().getString("invoiceamount");
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("End Invoice");
        tv_promotion = (TextView) findViewById(R.id.tv_promotion);
        if (getIntent().getExtras() != null) {
            str_promotion_message = getIntent().getExtras().getString("msg", "extra Promotion");
            tv_promotion.setText(str_promotion_message.substring(0, 1).toUpperCase() + str_promotion_message.substring(1).toLowerCase());
            int pos = getIntent().getIntExtra("pos", 10);
            if (str_promotion_message.equals("Final Invoice")) {
                ll_bottom.setVisibility(View.VISIBLE);
            } else {
                if (str_promotion_message.equals("delivery")) {
                    ll_bottom.setVisibility(View.VISIBLE);
                } else {
                    ll_bottom.setVisibility(View.GONE);
                }
            }
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> filter = new HashMap<String, String>();
                filter.put(db.KEY_DELIVERY_NO,delivery.getOrderId());
                db.deleteData(db.CUSTOMER_DELIVERY_ITEMS_POST,filter);
                finish();
            }
        });
        ll_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str_promotion_message.equals("Final Invoice")) {
                    final Dialog dialog = new Dialog(PromotionActivity.this);
                    dialog.setContentView(R.layout.dialog_doprint);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                    LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                    dialog.show();
                    btn_print.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    btn_notprint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PromotionActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else if (str_promotion_message.equals("delivery")) {
                    Intent intent = new Intent(PromotionActivity.this, PaymentDetails.class);
                    intent.putExtra("msg", str_promotion_message);
                    intent.putExtra("headerObj", object);
                    intent.putExtra("delivery", delivery);
                    intent.putExtra("invoiceamount",invoiceAmount);
                    startActivity(intent);
                    finish();
                } else {
                    if (str_promotion_message.equals("")) {
                        final Dialog dialog = new Dialog(PromotionActivity.this);
                        dialog.setContentView(R.layout.dialog_doprint);
                        dialog.setCancelable(true);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                        LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                        dialog.show();
                        btn_print.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        btn_notprint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(PromotionActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        finish();
                    }
                }
            }
        });
    }

}
