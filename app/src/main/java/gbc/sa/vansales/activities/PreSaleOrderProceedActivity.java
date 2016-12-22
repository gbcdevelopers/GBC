package gbc.sa.vansales.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.PreSaleProceed;

public class PreSaleOrderProceedActivity extends AppCompatActivity {

    ArrayList<PreSaleProceed> preSaleProceeds = new ArrayList<>();
    ImageView iv_back;
    TextView tv_top_header;
//    FloatingActionButton float_presale_proceed;
    int arrSize = 5;


    TextView tv_date;
    ImageView iv_calendar,iv_search;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Button btn_confirm;








    ListView list;
    LoadRequestAdapter adapter;

    String[] itemName;
    String[] category;
    String[] cases;
    String[] units;
    int[] categoryImage;



    ArrayList<LoadRequestConstants> arraylist = new ArrayList<LoadRequestConstants>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sale_order_proceed);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        tv_date = (TextView) findViewById(R.id.tv_date);
        iv_calendar = (ImageView) findViewById(R.id.iv_calander);
        iv_search=(ImageView)findViewById(R.id.iv_search);
        iv_search.setVisibility(View.VISIBLE);
        btn_confirm=(Button)findViewById(R.id.btn_confirm_delivery_presale_proceed);


        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("PreSale Order");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        float_presale_proceed=(FloatingActionButton)findViewById(R.id.float_presale_proceed);
//        float_presale_proceed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                arrSize = arrSize +1;
//                setData();
//            }
//        });
//        setData();

        myCalendar = Calendar.getInstance();

        iv_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new DatePickerDialog(PreSaleOrderProceedActivity.this, date, myCalendar
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





        itemName = new String[] { "Berain_Regular", "Berain_Half_Liter", "Berain_1.5_Liter" };

        category = new String[] { "Regular", "Half Liter", "1.5 Liter"};

        cases = new String[] { "10", "20","30"};

        units = new String[] { "100", "200", "300"};

        categoryImage = new int[] { R.drawable.beraincategory, R.drawable.beraincategory,
                R.drawable.beraincategory};


        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        for (int i = 0; i < itemName.length; i++)
        {
            LoadRequestConstants lrc = new LoadRequestConstants(itemName[i], category[i],
                    cases[i],units[i], categoryImage[i]);
            // Binds all strings into an array
            arraylist.add(lrc);
        }

        // Pass results to ListViewAdapter Class
        adapter = new LoadRequestAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog=new Dialog(PreSaleOrderProceedActivity.this);
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

                        dialog.cancel();

                    }
                });
            }
        });















    }

        private void updateLabel() {

            String myFormat = "dd/MM" +
                    "/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

            tv_date.setText(sdf.format(myCalendar.getTime()));
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
