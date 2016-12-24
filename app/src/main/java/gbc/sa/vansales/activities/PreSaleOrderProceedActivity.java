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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import gbc.sa.vansales.R;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.models.PreSaleProceed;

public class PreSaleOrderProceedActivity extends AppCompatActivity {

    ArrayList<PreSaleProceed> preSaleProceeds = new ArrayList<>();
    ImageView iv_back;
    TextView tv_top_header;




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
    HashMap<Integer,ArrayList<LoadRequestConstants>> constantsHashMap=new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sale_order_proceed);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        tv_date = (TextView) findViewById(R.id.tv_date);
        iv_calendar = (ImageView) findViewById(R.id.iv_calander);
        iv_search=(ImageView)findViewById(R.id.iv_search);
        iv_search.setVisibility(View.GONE);
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



        list = (ListView) findViewById(R.id.listview);
        list.setItemsCanFocus(true);


       setData();






//        for (int i = 0; i < itemName.length; i++)
//        {
//            LoadRequestConstants lrc = new LoadRequestConstants(itemName[i], category[i],
//                    cases[i],units[i], categoryImage[i]);
//            // Binds all strings into an array
//            arraylist.add(lrc);
//        }

        // Pass results to ListViewAdapter Class





        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(PreSaleOrderProceedActivity.this);
                dialog.setContentView(R.layout.dialog_doprint);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));





                LinearLayout btn_print=(LinearLayout) dialog.findViewById(R.id.ll_print);
                LinearLayout btn_notprint=(LinearLayout) dialog.findViewById(R.id.ll_notprint);

                dialog.show();

                btn_print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();
                        finish();


                    }
                });
                btn_notprint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();
                        finish();

                    }
                });
            }
        });















    }

        private void updateLabel() {

            String myFormat = "dd/MM/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

            tv_date.setText(sdf.format(myCalendar.getTime()));



            PreSaleProceed proceed=new PreSaleProceed();
            proceed.setDATE(tv_date.getText().toString());
            Const.proceedArrayList.add(Const.id,proceed);
        }




    public void setData() {
        if (arraylist != null){
            arraylist.clear();
        }
        for (int i = 0; i < itemName.length; i++) {
            Log.d("iiii","-->"+i);
//            PreSaleProceed proceed = new PreSaleProceed();
//
//            proceed.setPRODUCT_NAME(itemName[i]);
//            proceed.setCATEGORY_NAME(category[i]);
//            proceed.setCTN("");
//            proceed.setBTL("");
//            preSaleProceeds.add(proceed);



            LoadRequestConstants constants=new LoadRequestConstants();
            constants.setItemName(itemName[i]);
            constants.setCategory(category[i]);
            constants.setCategoryImage(categoryImage[i]);

            constants.setCases("");
            constants.setUnits("");
            arraylist.add(constants);

        }

        adapter = new LoadRequestAdapter(this, arraylist,constantsHashMap);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
