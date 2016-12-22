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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import gbc.sa.vansales.R;

/**
 * Created by eheuristic on 12/6/2016.
 */

public class PromotionActivity extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;
    TextView tv_promotion;
    LinearLayout ll_bottom;
    String str_promotion_message="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);
        ll_bottom=(LinearLayout)findViewById(R.id.ll_bottom);


        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("End Invoice");


        tv_promotion=(TextView)findViewById(R.id.tv_promotion);
        if(getIntent().getExtras()!=null)
        {

            str_promotion_message = getIntent().getExtras().getString("msg","extra Promotion");
            tv_promotion.setText(str_promotion_message);
            int pos=getIntent().getIntExtra("pos",10);
            if(pos==1)
            {
                ll_bottom.setVisibility(View.GONE);
            }
            else
            {
                ll_bottom.setVisibility(View.VISIBLE);
            }
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(str_promotion_message.equals("Final Invoice"))
                {


                    final Dialog dialog=new Dialog(PromotionActivity.this);
                    dialog.setContentView(R.layout.dialog_doprint);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    LinearLayout ll_top=(LinearLayout)dialog.findViewById(R.id.llTop);


                    Button btn_print=(Button)dialog.findViewById(R.id.btn_print);
                    Button btn_notprint=(Button)dialog.findViewById(R.id.btn_donotprint);

                    ll_top.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
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

                        Intent intent=new Intent(PromotionActivity.this,DashboardActivity.class);
                            startActivity(intent);
                        finish();


                        }
                    });

                }
                else {


                    if(str_promotion_message.equals(""))
                    {
                        final Dialog dialog=new Dialog(PromotionActivity.this);
                        dialog.setContentView(R.layout.dialog_doprint);
                        dialog.setCancelable(false);
                        dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));

                        LinearLayout ll_top=(LinearLayout)dialog.findViewById(R.id.llTop);


                        Button btn_print=(Button)dialog.findViewById(R.id.btn_print);
                        Button btn_notprint=(Button)dialog.findViewById(R.id.btn_donotprint);

                        ll_top.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
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

                                Intent intent=new Intent(PromotionActivity.this,DashboardActivity.class);
                                startActivity(intent);
                                finish();


                            }
                        });

                    }
                    else {
                        finish();
                    }



                }

            }
        });






    }
}
