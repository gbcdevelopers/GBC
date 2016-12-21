package gbc.sa.vansales.Fragment;

import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.BeginDay;
import gbc.sa.vansales.activities.DashboardActivity;
import gbc.sa.vansales.activities.LoadActivity;
import gbc.sa.vansales.activities.LoadSummaryActivity;
import gbc.sa.vansales.activities.OdometerPopupActivity;
import gbc.sa.vansales.data.TripHeader;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.Chain;
import gbc.sa.vansales.utils.Helpers;
<<<<<<< HEAD
import gbc.sa.vansales.utils.LoadingSpinner;
=======

import static gbc.sa.vansales.R.id.day;

>>>>>>> c0c7833b255ad15ef0636bab26fe23f8c899f262
/**
 * Created by eheuristic on 12/2/2016.
 */

public class BeginDayFragment extends Fragment {

    String TAG = BeginDayFragment.class.getSimpleName();

    TextView salesDate;
    TextView time;
    TextView delieveryDate;
    TextView route;
    TextView salesManNo;
    TextView salesManName;
    TextView deliveryRoute;
    TextView vehicleNo;
    TextView day;
    View view;
    Calendar myCalendar = Calendar.getInstance();
    String stringSalesDate = "";
    String stringDeliveryDate = "";
    String stringTime = "";
    private static final String DATE_FORMAT = "dd.MM.yy";
    LoadingSpinner loadingSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.activity_begin_day, container, false);
        loadingSpinner = new LoadingSpinner(getActivity());

        salesDate = (TextView) view.findViewById(R.id.salesDate);
        time = (TextView) view.findViewById(R.id.time);
        delieveryDate = (TextView) view.findViewById(R.id.delieveryDate);
        route = (TextView)view.findViewById(R.id.route);
        salesManNo = (TextView)view.findViewById(R.id.salesManNo);
        salesManName = (TextView)view.findViewById(R.id.salesManName);
        deliveryRoute = (TextView)view.findViewById(R.id.delieveryRoute);
        vehicleNo = (TextView)view.findViewById(R.id.vehicleNo);
        day = (TextView)view.findViewById(R.id.day);

        route.setEnabled(false);
        salesManNo.setEnabled(false);
        salesManName.setEnabled(false);
        deliveryRoute.setEnabled(false);
        vehicleNo.setEnabled(false);
        time.setEnabled(false);
        day.setEnabled(false);
        delieveryDate.setEnabled(false);
        salesDate.setEnabled(false);

        try{
            JSONObject data = new JSONObject(getArguments().getString("data"));

            route.setText(data.getString("ItemNo"));
            salesManNo.setText(data.getString("Driver1"));
            salesManName.setText(data.getString("Driver1"));

            String dateStr = data.getString("Execdate");
            dateStr = dateStr.replace("/","").trim();
            dateStr = dateStr.replace("Date(","").trim();
            dateStr = dateStr.replace(")","");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(dateStr));

            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            salesDate.setText(mYear + "-" + (mMonth+1) + "-" + mDay);
            delieveryDate.setText(mYear + "-" + (mMonth+1) + "-" + mDay);
            deliveryRoute.setText(data.getString("Vlid"));
            vehicleNo.setText(data.getString("Truck"));

        }
        catch (Exception e){
            e.printStackTrace();
        }



        Button btn_start_day = (Button)view.findViewById(R.id.btn_start_day);
        btn_start_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();



            }
        });

        salesDate.setEnabled(false);
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

        delieveryDate.setEnabled(false);
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


    public void showDialog()
    {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.activity_odometer_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        final String input = userInput.getText().toString();

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text

                                /// result.setText(userInput.getText());
<<<<<<< HEAD
                                Chain chain = new Chain(new Chain.Link() {
                                    @Override
                                    public void run() {
                                        go();
                                    }
                                });

                                chain.setFail(new Chain.Link() {
                                    @Override
                                    public void run() throws Exception {
                                        fail();
                                    }
                                });

                                chain.add(new Chain.Link() {
                                    @Override
                                    public void run() {
                                        TripHeader.load("GBC012000000001", null);
                                    }
                                });
                                chain.start();


                                Intent i=new Intent(getActivity(),LoadActivity.class);
                                startActivity(i);
=======
                                TripHeader.load("GBC012000000001",null);
                                if(input.equals("")) {
                                   // Toast.makeText(getActivity(),input.toString(),Toast.LENGTH_SHORT).show();

                                    Toast.makeText(getActivity(),"Please eneter some valid value!",Toast.LENGTH_SHORT).show();
                                    dialog.cancel();

                                }
                                else
                                {
                                    Intent i = new Intent(getActivity(), LoadActivity.class);
                                    startActivity(i);
                                }
>>>>>>> c0c7833b255ad15ef0636bab26fe23f8c899f262
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
//                                Intent i=new Intent(getActivity(),DashboardActivity.class);
//                                startActivity(i);
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
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

    private void go() {
        loadingSpinner.hide();
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        startActivity(intent);

    }

    private void fail() {
        Log.e("Something Failed","Failed");
        if(loadingSpinner.isShowing()){
            loadingSpinner.hide();
        }

    }

}
