package gbc.sa.vansales.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import gbc.sa.vansales.Fragment.AllCustomerFragment;
import gbc.sa.vansales.Fragment.VisitAllFragment;
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

    ImageView toolbar_iv_back;
    EditText et_search;

    ListView list;
    LoadRequestAdapter adapter;

    String[] itemName;
    String[] category;
    String[] cases;
    String[] units;
    int[] categoryImage;

    String from="";

    LinearLayout ll_bottom;
    FloatingActionButton fb_print;
    FloatingActionButton fb_edit;



    HashMap<Integer,List<LoadRequestConstants>> constantsHashMap=new HashMap<>();




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
        ll_bottom=(LinearLayout) findViewById(R.id.ll_bottom);
        fb_print=(FloatingActionButton)findViewById(R.id.fab_print);
        fb_edit=(FloatingActionButton)findViewById(R.id.fab_edit);




        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("PreSale Order");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if(getIntent().getExtras()!=null)
        {
            from=getIntent().getStringExtra("from");

        }

        itemName = new String[] { "Berain_Regular", "Berain_Half_Liter", "Berain_1.5_Liter" };

        category = new String[] { "Regular", "Half Liter", "1.5 Liter"};

        cases = new String[] { "10", "20","30"};

        units = new String[] { "100", "200", "300"};

        categoryImage = new int[] { R.drawable.beraincategory, R.drawable.beraincategory,
                R.drawable.beraincategory};




        toolbar_iv_back=(ImageView)findViewById(R.id.toolbar_iv_back) ;
        if (toolbar_iv_back != null) {
            toolbar_iv_back.setVisibility(View.GONE);
        }

        iv_search=(ImageView)findViewById(R.id.iv_search);
        if (iv_search != null) {
            iv_search.setVisibility(View.GONE);
        }

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_search.setVisibility(View.GONE);
                et_search.setVisibility(View.VISIBLE);
                toolbar_iv_back.setVisibility(View.GONE);
                tv_top_header.setVisibility(View.GONE);

            }
        });

        et_search=(EditText)findViewById(R.id.et_search_customer);
        et_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_search.getRight() - et_search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        et_search.setVisibility(View.GONE);
                        iv_search.setVisibility(View.VISIBLE);
                        toolbar_iv_back.setVisibility(View.VISIBLE);
                        tv_top_header.setVisibility(View.VISIBLE);



                        return true;
                    }
                }
                return false;
            }
        });

        toolbar_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.v("addtext","change");


//                    adapter.getFilter().filter(s.toString());




                //planBadgeAdapter.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable s) {



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








        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);
        list.setItemsCanFocus(true);



        if(from.equals("button"))
        {


            ll_bottom.setVisibility(View.GONE);
            btn_confirm.setVisibility(View.VISIBLE);


            List<LoadRequestConstants> arraylist = new ArrayList<LoadRequestConstants>();

            Toast.makeText(getApplicationContext(),"button" +
                    "",Toast.LENGTH_SHORT).show();
            for (int i = 0; i < itemName.length; i++)
            {
                LoadRequestConstants lrc = new LoadRequestConstants(itemName[i], category[i],
                        cases[i],units[i], categoryImage[i]);
                // Binds all strings into an array
                arraylist.add(lrc);
            }

            LoadRequestAdapter adapter = new LoadRequestAdapter(this, arraylist,null,from);
            list.setAdapter(adapter);
            // Pass results to ListViewAdapter Class

        }
        else if(from.equals("list"))
        {


            ll_bottom.setVisibility(View.VISIBLE);
            btn_confirm.setVisibility(View.GONE);




            List<LoadRequestConstants> arraylist = new ArrayList<LoadRequestConstants>();

            int position=getIntent().getIntExtra("pos",0);


            constantsHashMap=Const.constantsHashMap;
            List<LoadRequestConstants> constantses = Const.constantsHashMap.get(position);

            Log.v("Const.constantsHashMap",""+Const.constantsHashMap.size());
            Log.v("Const.constantsHashMap",""+Const.constantsHashMap.get(position).size());
            Log.v("Const.constantsHashMap",""+Const.constantsHashMap.get(position).get(position).getItemName());


            for (int i = 0; i < constantses.size(); i++)
            {
                LoadRequestConstants lrc = new LoadRequestConstants(constantses.get(i).getItemName(),constantses.get(i).getCategory(),
                        constantses.get(i).getCases(),constantses.get(i).getUnits(), constantses.get(i).getCategoryImage());
                // Binds all strings into an array
                arraylist.add(lrc);
                Log.v("arraylist",constantses.get(i).getItemName()+"  -  " +constantses.get(i).getCategory()+"  -  "+
                        constantses.get(i).getCases()+"  -  "+constantses.get(i).getUnits()+"  -  "+ constantses.get(i).getCategoryImage());
            }

           LoadRequestAdapter adapter = new LoadRequestAdapter(this, arraylist,null,from);
            list.setAdapter(adapter);

        }






        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Const.constantsHashMap.put(++Const.id,Const.loadRequestConstantsList);
                Log.v("Const.id ","const id : "+Const.id);
                finish();





//                final Dialog dialog=new Dialog(PreSaleOrderProceedActivity.this);
//                dialog.setContentView(R.layout.dialog_doprint);
//                dialog.setCancelable(false);
//                dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//
//
//
//
//                LinearLayout btn_print=(LinearLayout) dialog.findViewById(R.id.ll_print);
//                LinearLayout btn_notprint=(LinearLayout) dialog.findViewById(R.id.ll_notprint);
//
//                dialog.show();
//
//                btn_print.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        dialog.cancel();
//                        finish();
//
//
//                    }
//                });
//                btn_notprint.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        dialog.cancel();
//                        finish();
//
//                    }
//                });
            }
        });




      fb_print.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {


          }
      });

        fb_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LoadRequestAdapter.isEnabled = "yes";
                adapter.notifyDataSetChanged();





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






    @Override
    public void onBackPressed() {


//        Log.v("hashmap",Const.constantsHashMap.size()+"");
//        for(int i=0;i<Const.constantsHashMap.size();i++)
//        {
//            List<LoadRequestConstants> constantses=Const.constantsHashMap.get(i);
//            for(int j=0;j<constantses.size();j++)
//            {
//                Log.v("itemname",constantses.get(j).getItemName());
//            }
//        }
            finish();


    }
}
