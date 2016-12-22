package gbc.sa.vansales.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import gbc.sa.vansales.R;

public class PaymentDetails extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_top_header;

    TextView tv_due_amt,tv_total_amount,tv_date;
    ImageView iv_cal;
    EditText edt_check_no,edt_check_amt,edt_cash_amt;
    Spinner sp_item;

    Button btn_edit1,btn_edit2;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    FloatingActionButton fab;
    double total_amt=0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);
        fab=(FloatingActionButton)findViewById(R.id.fab);


        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Payment Details");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent();
                intent.putExtra("amt",String.valueOf(total_amt));

                setResult(RESULT_OK,intent);
                finish();

            }
        });


        tv_due_amt=(TextView)findViewById(R.id.tv_payment__amout_due_number);
        tv_total_amount=(TextView)findViewById(R.id.tv_total_amt);
        tv_date=(TextView)findViewById(R.id.tv_date);
        iv_cal=(ImageView)findViewById(R.id.image_cal);
        iv_cal.setEnabled(false);
        edt_check_no=(EditText)findViewById(R.id.edt_check_no);
        edt_cash_amt=(EditText)findViewById(R.id.edt_cash_amount);
        edt_check_amt=(EditText)findViewById(R.id.edt_check_amt);


        double cashamt= getcashamt();
        double  checkamt=getcheckamt();
        total_amt=cashamt+checkamt;
        tv_total_amount.setText(String.valueOf(total_amt));


        sp_item=(Spinner)findViewById(R.id.sp_item);
        sp_item.setEnabled(false);

        btn_edit1=(Button)findViewById(R.id.btn_edit1);
        btn_edit2=(Button)findViewById(R.id.btn_edit2);

        btn_edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt_cash_amt.setEnabled(true);

            }
        });

        btn_edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt_check_amt.setEnabled(true);
                edt_check_no.setEnabled(true);
                iv_cal.setEnabled(true);
                sp_item.setEnabled(true);

            }
        });

        myCalendar = Calendar.getInstance();
        iv_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new DatePickerDialog(PaymentDetails.this, date, myCalendar
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

        edt_cash_amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                setTotalText();

            }
        });

        edt_check_amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


               setTotalText();




            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog=new Dialog(PaymentDetails.this);
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


                        Intent intent=new Intent();
                        intent.putExtra("amt",String.valueOf(total_amt));

                        setResult(RESULT_OK,intent);
                        finish();


                    }
                });



            }
        });



    }



    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        tv_date.setText(sdf.format(myCalendar.getTime()));
    }

   public double getcashamt()
    {

        if(edt_cash_amt.getText().toString().equals(""))
        {
            return 0;
        }

        return  Double.parseDouble(edt_cash_amt.getText().toString());
    }

    public double getcheckamt()
    {

        if(edt_check_amt.getText().toString().equals(""))
        {
            return 0;
        }

        return  Double.parseDouble(edt_check_amt.getText().toString());
    }
    public void setTotalText()
    {

        double cash_amt=getcashamt();
        double check_amt=getcheckamt();

        total_amt = cash_amt+check_amt;
        tv_total_amount.setText(String.valueOf(total_amt));
    }
}
