package gbc.sa.vansales.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.PreSaleProceed;

public class DeliveryOrderActivity extends AppCompatActivity {

    ArrayList<PreSaleProceed> preSaleProceeds = new ArrayList<>();
    ImageView iv_back;
    TextView tv_top_header;
    FloatingActionButton float_presale_proceed;
    int arrSize = 3;
    ImageView iv_calendar;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    RelativeLayout btn_confirm_delivery;
    TextView tv_date;
    TextView tv_amt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        tv_date=(TextView)findViewById(R.id.tv_date);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Delivery Order");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_amt=(TextView)findViewById(R.id.tv_amt);



        iv_calendar=(ImageView)findViewById(R.id.iv_calander_presale_proced) ;

        btn_confirm_delivery=(RelativeLayout)findViewById(R.id.btn_confirm_delivery);

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

                Intent intent=new Intent(DeliveryOrderActivity.this,PromotionActivity.class);
                startActivity(intent);
                finish();


//
//
//                final Dialog dialog=new Dialog(DeliveryOrderActivity.this);
//                dialog.setContentView(R.layout.print_dialog);
//                dialog.setCancelable(false);
//                dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                ImageView iv_cancle=(ImageView)dialog.findViewById(R.id.imageView_close);
//                iv_cancle.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.cancel();
//                    }
//                });
//                dialog.show();

            }
        });

        myCalendar = Calendar.getInstance();

        iv_calendar.setEnabled(false);

        iv_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(DeliveryOrderActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });





        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

    }

    public void setData() {
        if (preSaleProceeds != null){
            preSaleProceeds.clear();
        }
        for (int i = 0; i < arrSize; i++) {
            Log.d("iiii","-->"+i);
            PreSaleProceed proceed = new PreSaleProceed();
            proceed.setPRODUCT_NAME("Berain 250 ml");
            proceed.setCTN("6");
            proceed.setBTL("50");
            proceed.setPRICE("1 SAR");
            preSaleProceeds.add(proceed);
        }
        setLayout();
    }


    private void updateLabel() {

        String myFormat = "dd/MM" +
                "/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        tv_date.setText(sdf.format(myCalendar.getTime()));
    }


    private void setLayout() {

        double totalamt=0;
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
            text3.setVisibility(View.GONE);
            text.setText(saleProceed.getPRODUCT_NAME());
            text1.setText(saleProceed.getCTN());
            text2.setText(saleProceed.getBTL());
            text3.setText(saleProceed.getPRICE());

            totalamt=totalamt+(Double.parseDouble(saleProceed.getCTN())*54+Double.parseDouble(saleProceed.getBTL())*2.25);




//            text.setTypeface(FontSelector.getBold(getActivity()));
            options_layout.addView(to_add);
        }
        tv_amt.setText(String.valueOf(totalamt));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
