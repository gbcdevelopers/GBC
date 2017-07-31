package gbc.sa.vansales.Fragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.BeginTripActivity;
import gbc.sa.vansales.activities.LoadActivity;
import gbc.sa.vansales.data.DriverRouteFlags;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
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
    TextView tripID;
    View view;
    Calendar myCalendar = Calendar.getInstance();
    String stringSalesDate = "";
    String stringDeliveryDate = "";
    String stringTime = "";
    private static final String DATE_FORMAT = "dd.MM.yy";
    DatabaseHandler db;
    LoadingSpinner loadingSpinner;
    float lastValue = 0;
    String lastValueSAP = null;
    Button btn_continue;
    App.DriverRouteControl flag = new App.DriverRouteControl();
    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            //   setBeginDayVisibility();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_begin_day, container, false);
        db = new DatabaseHandler(getActivity());
        loadingSpinner = new LoadingSpinner(getActivity());
        salesDate = (TextView) view.findViewById(R.id.salesDate);
        time = (TextView) view.findViewById(R.id.time);
        //delieveryDate = (TextView) view.findViewById(R.id.delieveryDate);
        route = (TextView) view.findViewById(R.id.route);
        salesManNo = (TextView) view.findViewById(R.id.salesManNo);
        salesManName = (TextView) view.findViewById(R.id.salesManName);
       // deliveryRoute = (TextView) view.findViewById(R.id.delieveryRoute);
        vehicleNo = (TextView) view.findViewById(R.id.vehicleNo);
        day = (TextView) view.findViewById(R.id.day);
        flag = DriverRouteFlags.get();
        try {
            JSONObject data = new JSONObject(getArguments().getString("data"));
            Log.e("Data in Fragment", "" + data);
            Helpers.logData(getActivity(), "Begin Day Fragment Data" + data);
            route.setText(data.getString("route"));
            salesManNo.setText(data.getString("driver1"));
            //salesManName.setText(data.getString("driver1"));
            if(Settings.getString(App.LANGUAGE).equals("ar")){
                salesManName.setText(Settings.getString(App.DRIVER_NAME_AR));
            }
            else{
                salesManName.setText(Settings.getString(App.DRIVER_NAME_EN));
            }
            salesDate.setText(data.getString("psDate"));
            //tripID.setText(Settings.getString(App.TRIP_ID));
           // delieveryDate.setText(data.getString("asDate"));
           // deliveryRoute.setText(data.getString("route"));
            vehicleNo.setText(UrlBuilder.decodeString(data.getString("truck")));
            day.setText(Helpers.getDayofWeek(data.getString("psDate")));
            time.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        btn_continue = (Button) view.findViewById(R.id.btnBack);
        boolean isMessageClicked = ((BeginTripActivity) getActivity()).hello;
        setBeginDayVisibility();
       /* if(isMessageClicked){

        }
        else{
            btn_continue.setEnabled(false);
            btn_continue.setAlpha(.5f);
        }*/
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(flag==null)){
                    Log.e("Flag","" + flag.getIsStartOfDay());
                    if(!(flag.getIsStartOfDay()==null)){
                        if(!flag.getIsStartOfDay().equals("0")&&!flag.getIsStartOfDay().equals("")){
                            String passwordkey = flag.getIsStartOfDay();
                            String password = "";
                            if(passwordkey.equals("1")){
                                password = flag.getPassword1();
                            }
                            if(passwordkey.equals("2")){
                                password = flag.getPassword2();
                            }
                            if(passwordkey.equals("3")){
                                password = flag.getPassword3();
                            }
                            if(passwordkey.equals("4")){
                                password = flag.getPassword4();
                            }
                            if(passwordkey.equals("5")){
                                password = flag.getPassword5();
                            }
                            Helpers.logData(getActivity(), "Password prompt for begin day" + passwordkey + "/" + password);
                            final Dialog dialog = new Dialog(getActivity());
                            View view = getActivity().getLayoutInflater().inflate(R.layout.password_prompt, null);
                            final EditText userInput = (EditText) view
                                    .findViewById(R.id.password);
                            Button btn_continue = (Button)view.findViewById(R.id.btn_ok);
                            Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
                            final String finalPassword = password;
                            btn_continue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    hideKeyboard();
                                    String input = userInput.getText().toString();
                                    if (input.equals("")) {
                                        dialog.cancel();
                                        Helpers.logData(getActivity(), "User clicked continue without entering data");
                                        Toast.makeText(getActivity(), getString(R.string.valid_value), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Helpers.logData(getActivity(), "User input for begin day password" + input);
                                        if (input.equals(finalPassword)){
                                            try{
                                                dialog.dismiss();
                                                Helpers.logData(getActivity(), "Password check success");
                                                String purchaseNumber = Helpers.generateNumber(db, ConfigStore.BeginDay_PR_Type);
                                                HashMap<String, String> map = new HashMap<>();
                                                String timeStamp = Helpers.getCurrentTimeStamp();
                                                Log.e("TimeStamp","" + timeStamp);
                                                map.put(db.KEY_TIME_STAMP, timeStamp);
                                                map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                                map.put(db.KEY_FUNCTION, ConfigStore.BeginDayFunction);
                                                map.put(db.KEY_PURCHASE_NUMBER, purchaseNumber);
                                                map.put(db.KEY_DATE, new SimpleDateFormat("yyyy.MM.dd").format(new Date()));
                                                map.put(db.KEY_IS_SELECTED, "true");
                                                map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                                                db.addData(db.BEGIN_DAY, map);
                                                new postTrip(purchaseNumber, timeStamp);
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                                Crashlytics.logException(e);
                                            }
                                        }
                                        else{
                                            dialog.cancel();
                                            hideKeyboard();
                                            Toast.makeText(getActivity(), getString(R.string.password_mismatch), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();
                                }
                            });
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            lp.gravity = Gravity.CENTER;
                            dialog.getWindow().setAttributes(lp);
                            dialog.setContentView(view);
                            dialog.setCancelable(false);
                            dialog.show();
                        }
                        else {
                            try{
                                String purchaseNumber = Helpers.generateNumber(db, ConfigStore.BeginDay_PR_Type);
                                HashMap<String, String> map = new HashMap<>();
                                String timeStamp = Helpers.getCurrentTimeStamp();
                                Log.e("TimeStamp","" + timeStamp);
                                map.put(db.KEY_TIME_STAMP, timeStamp);
                                map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                map.put(db.KEY_FUNCTION, ConfigStore.BeginDayFunction);
                                map.put(db.KEY_PURCHASE_NUMBER, purchaseNumber);
                                map.put(db.KEY_DATE, new SimpleDateFormat("yyyy.MM.dd").format(new Date()));
                                map.put(db.KEY_IS_SELECTED, "true");
                                map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                                db.addData(db.BEGIN_DAY, map);
                                new postTrip(purchaseNumber, timeStamp);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                                Crashlytics.logException(e);
                            }
                        }
                    }
                    else{
                        try{
                            String purchaseNumber = Helpers.generateNumber(db, ConfigStore.BeginDay_PR_Type);
                            HashMap<String, String> map = new HashMap<>();
                            String timeStamp = Helpers.getCurrentTimeStamp();
                            Log.e("TimeStamp","" + timeStamp);
                            map.put(db.KEY_TIME_STAMP, timeStamp);
                            map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            map.put(db.KEY_FUNCTION, ConfigStore.BeginDayFunction);
                            map.put(db.KEY_PURCHASE_NUMBER, purchaseNumber);
                            map.put(db.KEY_DATE, new SimpleDateFormat("yyyy.MM.dd").format(new Date()));
                            map.put(db.KEY_IS_SELECTED, "true");
                            map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                            db.addData(db.BEGIN_DAY, map);
                            new postTrip(purchaseNumber, timeStamp);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Crashlytics.logException(e);
                        }
                    }

                }
                else{
                    try{
                        String purchaseNumber = Helpers.generateNumber(db, ConfigStore.BeginDay_PR_Type);
                        HashMap<String, String> map = new HashMap<>();
                        String timeStamp = Helpers.getCurrentTimeStamp();
                        Log.e("TimeStamp","" + timeStamp);
                        map.put(db.KEY_TIME_STAMP, timeStamp);
                        map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                        map.put(db.KEY_FUNCTION, ConfigStore.BeginDayFunction);
                        map.put(db.KEY_PURCHASE_NUMBER, purchaseNumber);
                        map.put(db.KEY_DATE, new SimpleDateFormat("yyyy.MM.dd").format(new Date()));
                        map.put(db.KEY_IS_SELECTED, "true");
                        map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                        db.addData(db.BEGIN_DAY, map);
                        new postTrip(purchaseNumber, timeStamp);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }
                }


                // showDialog();
            }
        });
        salesDate.setEnabled(true);
        return view;
    }
    private void setBeginDayVisibility() {
        Log.e("Called", "Called");
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_IS_BEGIN_DAY, "true");
        if (db.checkData(db.LOCK_FLAGS, map)) {
            btn_continue.setEnabled(false);
            btn_continue.setAlpha(.5f);
            // btnBDay.setVisibility(View.INVISIBLE);
        }
    }
    void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View focusedView = getActivity().getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            inputManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }
    public void showDialog() {
        HashMap<String, String> map1 = new HashMap<>();
        map1.put(db.KEY_ODOMETER_VALUE, "");
        HashMap<String, String> filter1 = new HashMap<>();
        filter1.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
        if (db.checkData(db.LAST_ODOMETER, filter1)) {
            Cursor cursor = db.getData(db.LAST_ODOMETER, map1, filter1);
            if (cursor.getCount() > 0) {
                Helpers.logData(getActivity(), "Last odometer value read - SAP");
                cursor.moveToFirst();
                lastValueSAP = cursor.getString(cursor.getColumnIndex(db.KEY_ODOMETER_VALUE));
                Log.e("SAP Value","" + lastValueSAP);
                Helpers.logData(getActivity(), "Last odometer value is" + lastValueSAP);
            }
        } else {
        }
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.activity_odometer_popup, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptsView);
        //Reading last save odometer
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_ODOMETER_VALUE, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
        if (db.checkData(db.ODOMETER, filter)) {
            Cursor cursor = db.getData(db.ODOMETER, map, filter);
            if (cursor.getCount() > 0) {
                Helpers.logData(getActivity(), "Last odometer value read");
                cursor.moveToFirst();
                lastValue = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_ODOMETER_VALUE)));
                Helpers.logData(getActivity(), "Last odometer value is" + lastValue);
            }
        } else {
        }
        final TextView lastOdometer = (TextView) promptsView
                .findViewById(R.id.textViewOdometer);

        lastOdometer.setText(getString(R.string.previousodomter) + lastValueSAP);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        // set dialog message
        Helpers.logData(getActivity(), "Capturing users odomter value");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String input = userInput.getText().toString();
                                if (input.equals("")) {
                                    Helpers.logData(getActivity(), "User entered blank odometer value");
                                    dialog.cancel();
                                    Toast.makeText(getContext(), getString(R.string.valid_value), Toast.LENGTH_SHORT).show();
                                } else {
                                    Helpers.logData(getActivity(), "User odometer value" + input + "for posting");
                                    postOdometer(input);
                                    /*if (Float.parseFloat(input) > lastValue) {
                                        postOdometer(input);
                                    } else {
                                        Toast.makeText(getContext(), getString(R.string.value_greater), Toast.LENGTH_SHORT).show();
                                    }*/
                                }
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Helpers.logData(getActivity(), "User cancelled odometer prompt");
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
    public void postOdometer(String value) {
        try{
            String purchaseNumber = Helpers.generateNumber(db, ConfigStore.Odometer_PR_Type);
            String timeStamp = Helpers.getCurrentTimeStamp();
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_ODOMETER_VALUE, value);
            map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
            map.put(db.KEY_PURCHASE_NUMBER, purchaseNumber);
            map.put(db.KEY_TIME_STAMP, timeStamp);
            map.put(db.KEY_ODOMETER_TYPE,App.ODOMETER_BEGIN_DAY);
            map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
            filter.put(db.KEY_ODOMETER_TYPE, App.ODOMETER_BEGIN_DAY);
            if (db.checkData(db.ODOMETER, filter)) {
                db.updateData(db.ODOMETER, map, filter);
            } else {
                db.addData(db.ODOMETER, map);
            }
            new postData(value, purchaseNumber);
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    public class postData extends AsyncTask<Void, Void, Void> {
        String flag = "";
        String value = "";
        String purchaseNumber = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingSpinner.show();
        }
        private postData(String value, String purchaseNumber) {
            Helpers.logData(getActivity(), "Posting Odometer with " + value + "with reference no" + purchaseNumber);
            this.value = value;
            this.purchaseNumber = purchaseNumber;
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("TripID", Settings.getString(App.TRIP_ID));
            map.put("Value", this.value);
            JSONArray deepEntity = new JSONArray();
            this.flag = IntegrationService.postOdometer(getActivity(), App.POST_ODOMETER_SET, map, deepEntity, purchaseNumber);
            Helpers.logData(getActivity(), "Response for Odometer" + this.flag);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (loadingSpinner.isShowing()) {
                loadingSpinner.hide();
            }
            hideKeyboard();
            if (this.flag.equals(purchaseNumber)) {
                Helpers.logData(getActivity(), "Flag value for Odometer : Offline" + this.flag);
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                map.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                HashMap<String, String> filter = new HashMap<>();
                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                filter.put(db.KEY_ODOMETER_TYPE,App.ODOMETER_BEGIN_DAY);
                db.updateData(db.ODOMETER, map, filter);
                HashMap<String, String> altMap = new HashMap<>();
                altMap.put(db.KEY_IS_BEGIN_DAY, "true");
                HashMap<String, String> filterMap = new HashMap<>();
                filterMap.put(db.KEY_IS_BEGIN_DAY, "false");
                db.updateData(db.LOCK_FLAGS, altMap, filterMap);
                Intent i = new Intent(getActivity(), LoadActivity.class);
                startActivity(i);
            } else if (this.flag.equals("Y")) {
                Helpers.logData(getActivity(), "Flag value for odometer : Online" + this.flag);
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                map.put(db.KEY_IS_POSTED, App.DATA_IS_POSTED);
                HashMap<String, String> filter = new HashMap<>();
                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                db.updateData(db.ODOMETER, map, filter);
                HashMap<String, String> altMap = new HashMap<>();
                altMap.put(db.KEY_IS_BEGIN_DAY, "true");
                HashMap<String, String> filterMap = new HashMap<>();
                filterMap.put(db.KEY_IS_BEGIN_DAY, "false");
                db.updateData(db.LOCK_FLAGS, altMap, filterMap);
                Intent i = new Intent(getActivity(), LoadActivity.class);
                startActivity(i);
            } else if (this.flag.contains("Error")) {
                Helpers.logData(getActivity(), "Error occured during odometer posting" + this.flag);
                Toast.makeText(getActivity(), this.flag.replaceAll("Error", "").trim(), Toast.LENGTH_SHORT).show();
            } else {
                Helpers.logData(getActivity(), "Some unknown error occured for odometer posting" + this.flag);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle(R.string.error_title)
                        .setMessage(R.string.error_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (loadingSpinner.isShowing()) {
                                    loadingSpinner.hide();
                                }
                                Helpers.logData(getActivity(), "Dismissing the error prompt for odometer");
                                dialog.dismiss();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        }
    }
    public class postTrip extends AsyncTask<Void, Void, Void> {
        String orderID = "";
        String value = "";
        String purchaseNumber = "";
        String timeStamp = "";
        String[] tokens = new String[2];
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingSpinner.show();
        }
        private postTrip(String purchaseNumber, String timeStamp) {
            Helpers.logData(getActivity(), "Posting Begin Day with" + purchaseNumber + timeStamp);
            this.purchaseNumber = purchaseNumber;
            this.timeStamp = timeStamp;
            this.tokens = Helpers.parseTimeStamp(this.timeStamp);
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Function", ConfigStore.BeginDayFunction);
            map.put("TripId", Settings.getString(App.TRIP_ID));
            map.put("CreatedBy", Settings.getString(App.DRIVER));
            map.put("StartDate", Settings.getString(App.LANGUAGE).equals("ar") ? Helpers.convertArabicText(tokens[0].toString()) : tokens[0].toString());
            map.put("StartTime", Settings.getString(App.LANGUAGE).equals("ar") ? Helpers.convertArabicText(tokens[1].toString()) : tokens[1].toString());
            JSONArray deepEntity = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            deepEntity.put(jsonObject);
            this.orderID = IntegrationService.postTrip(getActivity(), App.POST_COLLECTION, map, deepEntity, purchaseNumber);
            Helpers.logData(getActivity(), "Response for begin Day" + this.orderID);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (loadingSpinner.isShowing()) {
                loadingSpinner.hide();
            }
            if (!this.orderID.contains("Error") && !this.orderID.equals("")) {
                if (this.orderID.equals(this.purchaseNumber)) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    map.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                    HashMap<String, String> filter = new HashMap<>();
                    filter.put(db.KEY_FUNCTION, ConfigStore.BeginDayFunction);
                    filter.put(db.KEY_PURCHASE_NUMBER, this.purchaseNumber);
                    db.updateData(db.BEGIN_DAY, map, filter);
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    map.put(db.KEY_IS_POSTED, App.DATA_IS_POSTED);
                    HashMap<String, String> filter = new HashMap<>();
                    filter.put(db.KEY_FUNCTION, ConfigStore.BeginDayFunction);
                    filter.put(db.KEY_PURCHASE_NUMBER, this.purchaseNumber);
                    db.updateData(db.BEGIN_DAY, map, filter);
                }
                //showDialog();
                if(!flag.isPromptOdometer()){
                    Helpers.logData(getActivity(), "No Flag for Odometer Prompt. Directly going to Driver Load");
                    Intent i = new Intent(getActivity(), LoadActivity.class);
                    startActivity(i);
                }
                else{
                    Helpers.logData(getActivity(), "Showing Odometer prompt");
                    showDialog();
                }

            } else if (this.orderID.contains("Error")) {
                Helpers.logData(getActivity(), "Error in Begin Day posting" + this.orderID);
                Toast.makeText(getActivity(), this.orderID.replaceAll("Error", "").trim(), Toast.LENGTH_SHORT).show();
            } else {
                Helpers.logData(getActivity(), "Some random error on Begin Day posting. Showing Dialog to user");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle(R.string.error_title)
                        .setMessage(R.string.error_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (loadingSpinner.isShowing()) {
                                    loadingSpinner.hide();
                                }
                                dialog.dismiss();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        }
    }
}