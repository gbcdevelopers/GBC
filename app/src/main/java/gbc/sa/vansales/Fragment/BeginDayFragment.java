package gbc.sa.vansales.Fragment;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.EditText;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.BeginDay;
import gbc.sa.vansales.activities.DashboardActivity;
import gbc.sa.vansales.activities.LoadActivity;
import gbc.sa.vansales.activities.LoadSummaryActivity;
import gbc.sa.vansales.activities.OdometerPopupActivity;
import gbc.sa.vansales.data.TripHeader;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static gbc.sa.vansales.R.id.day;
import static gbc.sa.vansales.R.id.editTextDialogUserInput;
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
    DatabaseHandler db = new DatabaseHandler(getActivity());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_begin_day, container, false);
        salesDate = (TextView) view.findViewById(R.id.salesDate);
        time = (TextView) view.findViewById(R.id.time);
        delieveryDate = (TextView) view.findViewById(R.id.delieveryDate);
        route = (TextView) view.findViewById(R.id.route);
        salesManNo = (TextView) view.findViewById(R.id.salesManNo);
        salesManName = (TextView) view.findViewById(R.id.salesManName);
        deliveryRoute = (TextView) view.findViewById(R.id.delieveryRoute);
        vehicleNo = (TextView) view.findViewById(R.id.vehicleNo);
        day = (TextView) view.findViewById(R.id.day);
        try {
            JSONObject data = new JSONObject(getArguments().getString("data"));
            Log.e("Data in Fragment", "" + data);
            route.setText(data.getString("route"));
            salesManNo.setText(data.getString("driver1"));
            salesManName.setText(data.getString("driver1"));
            salesDate.setText(data.getString("psDate"));
            delieveryDate.setText(data.getString("asDate"));
            deliveryRoute.setText(data.getString("route"));
            vehicleNo.setText(data.getString("truck"));
            day.setText(Helpers.getDayofWeek(data.getString("psDate")));
            time.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Button btn_continue = (Button) view.findViewById(R.id.btnBack);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        salesDate.setEnabled(false);

        return view;
    }


    void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View focusedView = getActivity().getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public void showDialog() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.activity_odometer_popup, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String input = userInput.getText().toString();
                                if (input.equals("")) {
                                    dialog.cancel();
                                    Toast.makeText(getContext(),getString(R.string.valid_value), Toast.LENGTH_SHORT).show();
                                } else {

                                    HashMap<String, String> altMap = new HashMap<>();
                                    altMap.put(db.KEY_IS_BEGIN_DAY, "true");
                                    HashMap<String, String> filter = new HashMap<>();
                                    filter.put(db.KEY_IS_BEGIN_DAY,"false");

                                    db.updateData(db.LOCK_FLAGS,altMap,filter);

                                    Intent i = new Intent(getActivity(), LoadActivity.class);
                                    startActivity(i);
                                }
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                hideKeyboard();
                                dialog.cancel();
//                                Intent i=new Intent(getActivity(),DashboardActivity.class);
//                                startActivity(i);
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    DatePickerDialog.OnDateSetListener dateSales = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            stringSalesDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            salesDate.setText(stringSalesDate);
            Log.d(TAG, "---" + stringSalesDate);
        }
    };
    DatePickerDialog.OnDateSetListener dateDelivery = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            stringDeliveryDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            delieveryDate.setText(stringDeliveryDate);
            Log.d(TAG, "---" + stringDeliveryDate);
        }
    };
}