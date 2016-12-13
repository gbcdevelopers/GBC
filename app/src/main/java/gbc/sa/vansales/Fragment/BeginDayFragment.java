package gbc.sa.vansales.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.OdometerPopupActivity;
/**
 * Created by eheuristic on 12/2/2016.
 */

public class BeginDayFragment extends Fragment {

    String TAG = BeginDayFragment.class.getSimpleName();

    EditText salesDate;
    EditText time;
    EditText delieveryDate;
    View view;
    Calendar myCalendar = Calendar.getInstance();
    String stringSalesDate = "";
    String stringDeliveryDate = "";
    String stringTime = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.activity_begin_day, container, false);

        salesDate = (EditText) view.findViewById(R.id.salesDate);
        time = (EditText) view.findViewById(R.id.time);
        delieveryDate = (EditText) view.findViewById(R.id.delieveryDate);

        Button btn_continue = (Button)view.findViewById(R.id.btnBack);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Here","Here");
                Intent intent = new Intent(getActivity(), OdometerPopupActivity.class);
                startActivity(intent);
            }
        });

        salesDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(getActivity(), dateSales, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        delieveryDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(getActivity(), dateDelivery, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                    int minute = myCalendar.get(Calendar.MINUTE);

                    TimePickerDialog dialog;
                    dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            String starttime = String.format("%02d:%02d", hourOfDay, minute);
                            time.setText(starttime);
                            stringTime = starttime;

                        }
                    }, hour, minute, false);
                    dialog.show();
                }
            }
        });

        salesDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateSales, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        delieveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateDelivery, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);

                TimePickerDialog dialog;
                dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String starttime=String.format("%02d:%02d",hourOfDay,minute);
                        time.setText(starttime);
                        stringTime =starttime;

                    }
                }, hour, minute, false);
                dialog.show();
            }
        });

        return view;
    }

    DatePickerDialog.OnDateSetListener dateSales = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub

            stringSalesDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            salesDate.setText(stringSalesDate);

            Log.d(TAG,"---"+ stringSalesDate);
        }

    };
    DatePickerDialog.OnDateSetListener dateDelivery = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub

            stringDeliveryDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            delieveryDate.setText(stringDeliveryDate);

            Log.d(TAG,"---"+ stringDeliveryDate);
        }

    };

}
