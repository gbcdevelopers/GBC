package gbc.sa.vansales.activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import com.crashlytics.android.Crashlytics;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.LoadRequestBadgeAdapter;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.models.OrderRequest;
import gbc.sa.vansales.models.PreSaleProceed;
import gbc.sa.vansales.sap.BackgroundJob;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.PrinterHelper;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Muhammad Umair on 02/12/2016.
 */
/************************************************************
 @ This activity is used to create a load request. This activity
 @ is launched when the user clicks on Load Request from the
 @ manage inventory screen
 ************************************************************/
public class LoadRequestActivity extends AppCompatActivity {
    Button processLoadRequest;
    ListView list;
    LoadRequestBadgeAdapter adapter;
    EditText editsearch;
    ImageButton datepickerdialogbutton;
    TextView selecteddate;
    ArrayList<LoadRequest> arraylist = new ArrayList<>();
    DatabaseHandler db = new DatabaseHandler(this);
    public ArrayList<ArticleHeader> articles;
    float orderTotalValue = 0;
    LoadingSpinner loadingSpinner;
    boolean isPutOnHold;
    boolean putOnHoldValueChanged = false;
    CheckBox putOnHold;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    boolean isPrint = false;
    private static int kJobId = 0;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_request);
        setTitle(getString(R.string.loadrequest));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        articles = ArticleHeaders.get();
        myCalendar = Calendar.getInstance();
        loadingSpinner = new LoadingSpinner(LoadRequestActivity.this);
        putOnHold = (CheckBox) findViewById(R.id.putOnHold);
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_IS_POSTED, App.DATA_PUT_ON_HOLD);
        if (db.checkData(db.LOAD_REQUEST, filter)) {
            putOnHold.setChecked(true);
            isPutOnHold = true;
            new loadItems(true);
        } else {
            isPutOnHold = false;
            putOnHold.setChecked(false);
            new loadItems(false);
        }
        datepickerdialogbutton = (ImageButton) findViewById(R.id.btnDate);
        selecteddate = (TextView) findViewById(R.id.tv1);
        selecteddate.setText(Helpers.formatDate(new Date(), App.DATE_PICKER_FORMAT));
        processLoadRequest = (Button) findViewById(R.id.btnProcess);
        putOnHold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                putOnHoldValueChanged = true;
                if (isChecked) {
                    isPutOnHold = true;
                } else {
                    isPutOnHold = false;
                }
            }
        });
        datepickerdialogbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new DatePickerDialog(LoadRequestActivity.this, date, myCalendar
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
        /************************************************************
         @ This will post or save the load request data based on whether
         @ the checkbox is clicked or not. If put on hold is checked
         @ the data entered is not posted but saved in the database of the device
         @ Once its unchecked only then data will be added for post.
         ************************************************************/
        processLoadRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle("Print Activity");
                try{
                    if (putOnHoldValueChanged) {
                        if (isPutOnHold) {
                            HashMap<String, String> filter = new HashMap<>();
                            filter.put(db.KEY_IS_POSTED, App.DATA_PUT_ON_HOLD);
                            if (db.checkData(db.LOAD_REQUEST, filter)) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_PURCHASE_NUMBER, "");
                                Cursor cursor = db.getData(db.LOAD_REQUEST, map, filter);
                                String purchaseNumber = "";
                                if (cursor.getCount() > 0) {
                                    cursor.moveToFirst();
                                    purchaseNumber = cursor.getString(cursor.getColumnIndex(db.KEY_PURCHASE_NUMBER));
                                    for (LoadRequest loadRequest : arraylist) {
                                        try {
                                            if (loadRequest.getCases().equals("") || loadRequest.getCases().isEmpty() || loadRequest.getCases() == null) {
                                                loadRequest.setCases("0");
                                            }
                                            if (loadRequest.getUnits().equals("") || loadRequest.getUnits().isEmpty() || loadRequest.getUnits() == null) {
                                                loadRequest.setUnits("0");
                                            }
                                            HashMap<String, String> updateMap = new HashMap<String, String>();
                                            updateMap.put(db.KEY_DATE,selecteddate.getText().toString());
                                            updateMap.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                            updateMap.put(db.KEY_CUSTOMER_NO, Settings.getString(App.DRIVER));
                                            updateMap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                            updateMap.put(db.KEY_ITEM_NO, loadRequest.getItemCode());
                                            updateMap.put(db.KEY_MATERIAL_DESC1, loadRequest.getItemName());
                                            updateMap.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                                            updateMap.put(db.KEY_MATERIAL_GROUP, loadRequest.getItemCategory());
                                            updateMap.put(db.KEY_CASE, loadRequest.getCases());
                                            updateMap.put(db.KEY_UNIT, loadRequest.getUnits());
                                            updateMap.put(db.KEY_UOM, loadRequest.getUom());
                                            updateMap.put(db.KEY_PRICE, loadRequest.getPrice());
                                            updateMap.put(db.KEY_IS_POSTED, App.DATA_PUT_ON_HOLD);
                                            updateMap.put(db.KEY_ORDER_ID, purchaseNumber);
                                            updateMap.put(db.KEY_PURCHASE_NUMBER, purchaseNumber);
                                            HashMap<String, String> filterMap = new HashMap<>();
                                            //  filterMap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                            filterMap.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                                            // filter.put(db.KEY_ORDER_ID,tokens[1].toString());
                                            filterMap.put(db.KEY_PURCHASE_NUMBER, purchaseNumber);
                                            if (db.checkData(db.LOAD_REQUEST, filterMap)) {
                                                db.updateData(db.LOAD_REQUEST, updateMap, filterMap);
                                            } else {
                                                db.addData(db.LOAD_REQUEST, updateMap);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Crashlytics.logException(e);
                                        }
                                    }
                                }
                            } else {
                                String purchaseNum = Helpers.generateNumber(db, ConfigStore.LoadRequest_PR_Type);
                                for (LoadRequest loadRequest : arraylist) {
                                    try {
                                        if (loadRequest.getCases().equals("") || loadRequest.getCases().isEmpty() || loadRequest.getCases() == null) {
                                            loadRequest.setCases("0");
                                        }
                                        if (loadRequest.getUnits().equals("") || loadRequest.getUnits().isEmpty() || loadRequest.getUnits() == null) {
                                            loadRequest.setUnits("0");
                                        }
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put(db.KEY_DATE, selecteddate.getText().toString());
                                        map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                        map.put(db.KEY_CUSTOMER_NO, Settings.getString(App.DRIVER));
                                        map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                        map.put(db.KEY_ITEM_NO, loadRequest.getItemCode());
                                        map.put(db.KEY_MATERIAL_DESC1, loadRequest.getItemName());
                                        map.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                                        map.put(db.KEY_MATERIAL_GROUP, loadRequest.getItemCategory());
                                        map.put(db.KEY_CASE, loadRequest.getCases());
                                        map.put(db.KEY_UNIT, loadRequest.getUnits());
                                        map.put(db.KEY_UOM, loadRequest.getUom());
                                        map.put(db.KEY_PRICE, loadRequest.getPrice());
                                        map.put(db.KEY_IS_POSTED, App.DATA_PUT_ON_HOLD);
                                        map.put(db.KEY_IS_PRINTED, "");
                                        map.put(db.KEY_ORDER_ID, purchaseNum);
                                        map.put(db.KEY_PURCHASE_NUMBER, purchaseNum);
                                        orderTotalValue = orderTotalValue + Float.parseFloat(loadRequest.getPrice());
                                        if (Integer.parseInt(loadRequest.getCases()) > 0 || Integer.parseInt(loadRequest.getUnits()) > 0) {
                                            db.addData(db.LOAD_REQUEST, map);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Crashlytics.logException(e);
                                    }
                                }
                            }
                        } else {
                            HashMap<String, String> filter = new HashMap<>();
                            filter.put(db.KEY_IS_POSTED, App.DATA_PUT_ON_HOLD);
                            if (db.checkData(db.LOAD_REQUEST, filter)) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_PURCHASE_NUMBER, "");
                                Cursor cursor = db.getData(db.LOAD_REQUEST, map, filter);
                                String purchaseNumber = "";
                                if (cursor.getCount() > 0) {
                                    cursor.moveToFirst();
                                    purchaseNumber = cursor.getString(cursor.getColumnIndex(db.KEY_PURCHASE_NUMBER));
                                    for (LoadRequest loadRequest : arraylist) {
                                        try {
                                            if (loadRequest.getCases().equals("") || loadRequest.getCases().isEmpty() || loadRequest.getCases() == null) {
                                                loadRequest.setCases("0");
                                            }
                                            if (loadRequest.getUnits().equals("") || loadRequest.getUnits().isEmpty() || loadRequest.getUnits() == null) {
                                                loadRequest.setUnits("0");
                                            }
                                            HashMap<String, String> updateMap = new HashMap<String, String>();
                                            updateMap.put(db.KEY_DATE, selecteddate.getText().toString());
                                            updateMap.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                            updateMap.put(db.KEY_CUSTOMER_NO, Settings.getString(App.DRIVER));
                                            updateMap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                            updateMap.put(db.KEY_ITEM_NO, loadRequest.getItemCode());
                                            updateMap.put(db.KEY_MATERIAL_DESC1, loadRequest.getItemName());
                                            updateMap.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                                            updateMap.put(db.KEY_MATERIAL_GROUP, loadRequest.getItemCategory());
                                            updateMap.put(db.KEY_CASE, loadRequest.getCases());
                                            updateMap.put(db.KEY_UNIT, loadRequest.getUnits());
                                            updateMap.put(db.KEY_UOM, loadRequest.getUom());
                                            updateMap.put(db.KEY_PRICE, loadRequest.getPrice());
                                            updateMap.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                                            updateMap.put(db.KEY_ORDER_ID, purchaseNumber);
                                            updateMap.put(db.KEY_PURCHASE_NUMBER, purchaseNumber);
                                            HashMap<String, String> filterMap = new HashMap<>();
                                            //  filterMap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                            filterMap.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                                            // filter.put(db.KEY_ORDER_ID,tokens[1].toString());
                                            filterMap.put(db.KEY_PURCHASE_NUMBER, purchaseNumber);
                                            if (db.checkData(db.LOAD_REQUEST, filterMap)) {
                                                db.updateData(db.LOAD_REQUEST, updateMap, filterMap);
                                            } else {
                                                db.addData(db.LOAD_REQUEST, updateMap);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Crashlytics.logException(e);
                                        }
                                    }
                                }
                            } else {
                                String purchaseNum = Helpers.generateNumber(db, ConfigStore.LoadRequest_PR_Type);
                                for (LoadRequest loadRequest : arraylist) {
                                    try {
                                        if (loadRequest.getCases().equals("") || loadRequest.getCases().isEmpty() || loadRequest.getCases() == null) {
                                            loadRequest.setCases("0");
                                        }
                                        if (loadRequest.getUnits().equals("") || loadRequest.getUnits().isEmpty() || loadRequest.getUnits() == null) {
                                            loadRequest.setUnits("0");
                                        }
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put(db.KEY_DATE, selecteddate.getText().toString());
                                        map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                        map.put(db.KEY_CUSTOMER_NO, Settings.getString(App.DRIVER));
                                        map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                        map.put(db.KEY_ITEM_NO, loadRequest.getItemCode());
                                        map.put(db.KEY_MATERIAL_DESC1, loadRequest.getItemName());
                                        map.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                                        map.put(db.KEY_MATERIAL_GROUP, loadRequest.getItemCategory());
                                        map.put(db.KEY_CASE, loadRequest.getCases());
                                        map.put(db.KEY_UNIT, loadRequest.getUnits());
                                        map.put(db.KEY_UOM, loadRequest.getUom());
                                        map.put(db.KEY_PRICE, loadRequest.getPrice());
                                        map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                                        map.put(db.KEY_IS_PRINTED, "");
                                        map.put(db.KEY_ORDER_ID, purchaseNum);
                                        map.put(db.KEY_PURCHASE_NUMBER, purchaseNum);
                                        orderTotalValue = orderTotalValue + Float.parseFloat(loadRequest.getPrice());
                                        if (Integer.parseInt(loadRequest.getCases()) > 0 || Integer.parseInt(loadRequest.getUnits()) > 0) {
                                            db.addData(db.LOAD_REQUEST, map);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Crashlytics.logException(e);
                                    }
                                }
                            }
                        }
                    } else {
                        if (isPutOnHold) {
                            HashMap<String, String> filter = new HashMap<>();
                            filter.put(db.KEY_IS_POSTED, App.DATA_PUT_ON_HOLD);
                            if (db.checkData(db.LOAD_REQUEST, filter)) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_PURCHASE_NUMBER, "");
                                Cursor cursor = db.getData(db.LOAD_REQUEST, map, filter);
                                String purchaseNumber = "";
                                if (cursor.getCount() > 0) {
                                    cursor.moveToFirst();
                                    purchaseNumber = cursor.getString(cursor.getColumnIndex(db.KEY_PURCHASE_NUMBER));
                                    //Log.e("My Purchase", "" + purchaseNumber);
                                    for (LoadRequest loadRequest : arraylist) {
                                        try {
                                            if (loadRequest.getCases().equals("") || loadRequest.getCases().isEmpty() || loadRequest.getCases() == null) {
                                                loadRequest.setCases("0");
                                            }
                                            if (loadRequest.getUnits().equals("") || loadRequest.getUnits().isEmpty() || loadRequest.getUnits() == null) {
                                                loadRequest.setUnits("0");
                                            }
                                            HashMap<String, String> updateMap = new HashMap<String, String>();
                                            updateMap.put(db.KEY_DATE, selecteddate.getText().toString());
                                            updateMap.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                            updateMap.put(db.KEY_CUSTOMER_NO, Settings.getString(App.DRIVER));
                                            updateMap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                            updateMap.put(db.KEY_ITEM_NO, loadRequest.getItemCode());
                                            updateMap.put(db.KEY_MATERIAL_DESC1, loadRequest.getItemName());
                                            updateMap.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                                            updateMap.put(db.KEY_MATERIAL_GROUP, loadRequest.getItemCategory());
                                            updateMap.put(db.KEY_CASE, loadRequest.getCases());
                                            updateMap.put(db.KEY_UNIT, loadRequest.getUnits());
                                            updateMap.put(db.KEY_UOM, loadRequest.getUom());
                                            updateMap.put(db.KEY_PRICE, loadRequest.getPrice());
                                            updateMap.put(db.KEY_IS_POSTED, App.DATA_PUT_ON_HOLD);
                                            updateMap.put(db.KEY_ORDER_ID, purchaseNumber);
                                            updateMap.put(db.KEY_PURCHASE_NUMBER, purchaseNumber);
                                            HashMap<String, String> filterMap = new HashMap<>();
                                            //  filterMap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                            filterMap.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                                            // filter.put(db.KEY_ORDER_ID,tokens[1].toString());
                                            filterMap.put(db.KEY_PURCHASE_NUMBER, purchaseNumber);
                                            if (db.checkData(db.LOAD_REQUEST, filterMap)) {
                                                db.updateData(db.LOAD_REQUEST, updateMap, filterMap);
                                            } else {
                                                db.addData(db.LOAD_REQUEST, updateMap);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } else {
                                String purchaseNum = Helpers.generateNumber(db, ConfigStore.LoadRequest_PR_Type);
                                for (LoadRequest loadRequest : arraylist) {
                                    try {
                                        if (loadRequest.getCases().equals("") || loadRequest.getCases().isEmpty() || loadRequest.getCases() == null) {
                                            loadRequest.setCases("0");
                                        }
                                        if (loadRequest.getUnits().equals("") || loadRequest.getUnits().isEmpty() || loadRequest.getUnits() == null) {
                                            loadRequest.setUnits("0");
                                        }
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put(db.KEY_DATE, selecteddate.getText().toString());
                                        map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                        map.put(db.KEY_CUSTOMER_NO, Settings.getString(App.DRIVER));
                                        map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                        map.put(db.KEY_ITEM_NO, loadRequest.getItemCode());
                                        map.put(db.KEY_MATERIAL_DESC1, loadRequest.getItemName());
                                        map.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                                        map.put(db.KEY_MATERIAL_GROUP, loadRequest.getItemCategory());
                                        map.put(db.KEY_CASE, loadRequest.getCases());
                                        map.put(db.KEY_UNIT, loadRequest.getUnits());
                                        map.put(db.KEY_UOM, loadRequest.getUom());
                                        map.put(db.KEY_PRICE, loadRequest.getPrice());
                                        map.put(db.KEY_IS_POSTED, App.DATA_PUT_ON_HOLD);
                                        map.put(db.KEY_IS_PRINTED, "");
                                        map.put(db.KEY_ORDER_ID, purchaseNum);
                                        map.put(db.KEY_PURCHASE_NUMBER, purchaseNum);
                                        orderTotalValue = orderTotalValue + Float.parseFloat(loadRequest.getPrice());
                                        if (Integer.parseInt(loadRequest.getCases()) > 0 || Integer.parseInt(loadRequest.getUnits()) > 0) {
                                            db.addData(db.LOAD_REQUEST, map);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Crashlytics.logException(e);
                                    }
                                }
                            }
                        } else {
                            String purchaseNum = Helpers.generateNumber(db, ConfigStore.LoadRequest_PR_Type);
                            for (LoadRequest loadRequest : arraylist) {
                                try {
                                    if (loadRequest.getCases().equals("") || loadRequest.getCases().isEmpty() || loadRequest.getCases() == null) {
                                        loadRequest.setCases("0");
                                    }
                                    if (loadRequest.getUnits().equals("") || loadRequest.getUnits().isEmpty() || loadRequest.getUnits() == null) {
                                        loadRequest.setUnits("0");
                                    }
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put(db.KEY_DATE, selecteddate.getText().toString());
                                    map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                                    map.put(db.KEY_CUSTOMER_NO, Settings.getString(App.DRIVER));
                                    map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                    map.put(db.KEY_ITEM_NO, loadRequest.getItemCode());
                                    map.put(db.KEY_MATERIAL_DESC1, loadRequest.getItemName());
                                    map.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                                    map.put(db.KEY_MATERIAL_GROUP, loadRequest.getItemCategory());
                                    map.put(db.KEY_CASE, loadRequest.getCases());
                                    map.put(db.KEY_UNIT, loadRequest.getUnits());
                                    map.put(db.KEY_UOM, loadRequest.getUom());
                                    map.put(db.KEY_PRICE, loadRequest.getPrice());
                                    map.put(db.KEY_IS_POSTED, isPutOnHold ? App.DATA_PUT_ON_HOLD : App.DATA_NOT_POSTED);
                                    map.put(db.KEY_IS_PRINTED, "");
                                    map.put(db.KEY_ORDER_ID, purchaseNum);
                                    map.put(db.KEY_PURCHASE_NUMBER, purchaseNum);
                                    orderTotalValue = orderTotalValue + Float.parseFloat(loadRequest.getPrice());
                                    if (Integer.parseInt(loadRequest.getCases()) > 0 || Integer.parseInt(loadRequest.getUnits()) > 0) {
                                        db.addData(db.LOAD_REQUEST, map);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Crashlytics.logException(e);
                                }
                            }
                        }
                    }
                    final Dialog dialog = new Dialog(LoadRequestActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_doprint);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                    LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                    btn_print.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isPutOnHold) {
                                if(!checkforNullBeforePost()){
                                    dialog.dismiss();
                                    Toast.makeText(LoadRequestActivity.this,getString(R.string.no_data),Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    isPrint = true;
                                    dialog.dismiss();
                                    new postData().execute();
                                }

                            }
                            dialog.dismiss();
                            //finish();
                        }
                    });
                    btn_notprint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isPutOnHold) {
                                if(!checkforNullBeforePost()){
                                    dialog.dismiss();
                                    Toast.makeText(LoadRequestActivity.this,getString(R.string.no_data),Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    dialog.dismiss();
                                    new postData().execute();
                                }
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.setCancelable(false);
                    if (!isPutOnHold) {
                        dialog.show();
                    } else {
                        finish();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

            }
        });
        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);
        adapter = new LoadRequestBadgeAdapter(this, arraylist);

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);
        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.getFilter().filter(text);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                adapter.getFilter().filter(arg0.toString());
                //Log.e("Filter","" + adapter.getFilter());
            }
        });
        /************************************************************
         @ Opening dialog for changing item quantities when user selects
         @ the item
         ************************************************************/
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try{
                    final LoadRequest item = arraylist.get(position);
                    final Dialog dialog = new Dialog(LoadRequestActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_with_crossbutton);
                    dialog.setCancelable(false);
                    TextView tv = (TextView) dialog.findViewById(R.id.dv_title);
                    tv.setText(item.getItemName());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    ImageView iv_cancle = (ImageView) dialog.findViewById(R.id.imageView_close);
                    Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
                    final EditText ed_cases = (EditText) dialog.findViewById(R.id.ed_cases);
                    final EditText ed_pcs = (EditText) dialog.findViewById(R.id.ed_pcs);
                    final EditText ed_cases_inv = (EditText) dialog.findViewById(R.id.ed_cases_inv);
                    final EditText ed_pcs_inv = (EditText) dialog.findViewById(R.id.ed_pcs_inv);
                    LinearLayout ll1 = (LinearLayout) dialog.findViewById(R.id.ll_1);
                    ll1.setVisibility(View.GONE);
                    RelativeLayout rl_specify = (RelativeLayout) dialog.findViewById(R.id.rl_specify_reason);
                    rl_specify.setVisibility(View.GONE);
                    if (item.isAltUOM()) {
                        ed_pcs.setEnabled(true);
                    } else {
                        ed_pcs.setEnabled(false);
                    }
                   // ed_cases.setText(item.getCases());
                   // ed_pcs.setText(item.getUnits());

                    ed_cases.setText(item.getCases().equals("0")?"":item.getCases());
                    ed_pcs.setText(item.getUnits().equals("0")?"":item.getUnits());

                    LinearLayout ll_1 = (LinearLayout) dialog.findViewById(R.id.ll_1);
                    iv_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                    btn_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String strCase = ed_cases.getText().toString();
                            String strpcs = ed_pcs.getText().toString();
                            String strcaseinv = ed_cases_inv.getText().toString();
                            String strpcsinv = ed_pcs_inv.getText().toString();
                        /*TextView tv_cases = (TextView) view.findViewById(R.id.tv_cases_value);
                        TextView tv_pcs = (TextView) view.findViewById(R.id.tv_pcs_value);
                        tv_cases.setText(strCase);
                        tv_pcs.setText(strpcs);*/
                            if (strCase.isEmpty() || strCase == null || strCase.trim().equals("")) {
                                strCase = String.valueOf(0);
                            }
                            if (strpcs.isEmpty() || strpcs == null || strpcs.trim().equals("")) {
                                strpcs = String.valueOf(0);
                            }
                            if (strcaseinv.isEmpty() || strcaseinv == null || strcaseinv.trim().equals("")) {
                                strcaseinv = String.valueOf(0);
                            }
                            if (strpcsinv.isEmpty() || strpcsinv == null || strpcsinv.trim().equals("")) {
                                strpcsinv = String.valueOf(0);
                            }
                            //item.setCases(strCase);
                            //item.setUnits(strpcs);

                            item.setCases(strCase.trim().equals("") ? "0" : strCase);
                            item.setUnits(strpcs.trim().equals("")?"0":strpcs);

                            arraylist.remove(position);
                            arraylist.add(position, item);
                            hideSoftKeyboard();
                            dialog.dismiss();
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        selecteddate.setText(sdf.format(myCalendar.getTime()));
        // Const.proceedArrayList.add(Const.id, proceed);
    }
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) LoadRequestActivity.this.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                LoadRequestActivity.this.getCurrentFocus().getWindowToken(), 0);
    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }
    public String postData() {
        String orderID = "";
        String purchaseNumber = "";
        float orderTotalValue = 0;
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("Function", ConfigStore.LoadRequestFunction);
            map.put("OrderId", "");
            map.put("DocumentType", ConfigStore.LoadRequestDocumentType);
            // map.put("DocumentDate", Helpers.formatDate(new Date(),App.DATE_FORMAT_WO_SPACE));
            // map.put("DocumentDate", null);
           /* map.put("PurchaseNum", Helpers.generateNumber(db,ConfigStore.LoadRequest_PR_Type));
            purchaseNumber = map.get("PurchaseNum");*/
            map.put("CustomerId", Settings.getString(App.DRIVER));
            map.put("SalesOrg", Settings.getString(App.SALES_ORG));
            map.put("DistChannel", Settings.getString(App.DIST_CHANNEL));
            map.put("Division", Settings.getString(App.DIVISION));
           // map.put("OrderValue", String.valueOf(orderTotalValue));
            map.put("Currency", "SAR");
            JSONArray deepEntity = new JSONArray();
            HashMap<String, String> itemMap = new HashMap<>();
            itemMap.put(db.KEY_DATE,"");
            itemMap.put(db.KEY_ITEM_NO, "");
            itemMap.put(db.KEY_MATERIAL_NO, "");
            itemMap.put(db.KEY_MATERIAL_DESC1, "");
            itemMap.put(db.KEY_CASE, "");
            itemMap.put(db.KEY_UNIT, "");
            itemMap.put(db.KEY_UOM, "");
            itemMap.put(db.KEY_PRICE, "");
            itemMap.put(db.KEY_ORDER_ID, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            Cursor cursor = db.getData(db.LOAD_REQUEST, itemMap, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                map.put("PurchaseNum", cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID)));
                map.put("DocumentDate", Helpers.parseDateforPost(cursor.getString(cursor.getColumnIndex(db.KEY_DATE))));
                purchaseNumber = map.get("PurchaseNum");
                int itemno = 10;
                do {
                    if (cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)) {
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                        jo.put("Material", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant", "");
                        jo.put("Quantity", cursor.getString(cursor.getColumnIndex(db.KEY_CASE)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("UoM", cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("Storagelocation", "");
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno + 10;
                        orderTotalValue += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_CASE)))*Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        deepEntity.put(jo);
                    }
                    else {
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                        jo.put("Material", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant", "");
                        jo.put("Quantity", cursor.getString(cursor.getColumnIndex(db.KEY_UNIT)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("UoM", cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("Storagelocation", "");
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno + 10;
                        orderTotalValue += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_UNIT)))*Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        deepEntity.put(jo);
                    }
                }
                while (cursor.moveToNext());
                map.put("OrderValue", String.valueOf(orderTotalValue));
            }
            orderID = IntegrationService.postData(LoadRequestActivity.this, App.POST_COLLECTION, map, deepEntity);
           // orderID = IntegrationService.postDataBackup(LoadRequestActivity.this, App.POST_COLLECTION, map, deepEntity);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return orderID + "," + purchaseNumber;
    }
    public static class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day);
            return datepickerdialog;
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView textview = (TextView) getActivity().findViewById(R.id.tv1);
            textview.setText(day + "/" + (month + 1) + "/" + year);
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public class loadItems extends AsyncTask<Void, Void, Void> {
        boolean putOnHoldExists = false;
        private loadItems(boolean putOnHoldExists) {
            this.putOnHoldExists = putOnHoldExists;
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_TRIP_ID, "");
            map.put(db.KEY_MATERIAL_GROUPA_DESC, "");
            map.put(db.KEY_MATERIAL_GROUPB_DESC, "");
            map.put(db.KEY_MATERIAL_DESC2, "");
            map.put(db.KEY_BATCH_MANAGEMENT, "");
            map.put(db.KEY_PRODUCT_HIERARCHY, "");
            map.put(db.KEY_VOLUME_UOM, "");
            map.put(db.KEY_VOLUME, "");
            map.put(db.KEY_WEIGHT_UOM, "");
            map.put(db.KEY_NET_WEIGHT, "");
            map.put(db.KEY_GROSS_WEIGHT, "");
            map.put(db.KEY_ARTICLE_CATEGORY, "");
            map.put(db.KEY_ARTICLE_NO, "");
            map.put(db.KEY_BASE_UOM, "");
            map.put(db.KEY_MATERIAL_GROUP, "");
            map.put(db.KEY_MATERIAL_TYPE, "");
            map.put(db.KEY_MATERIAL_DESC1, "");
            map.put(db.KEY_MATERIAL_NO, "");
            HashMap<String, String> filter = new HashMap<>();
            Cursor cursor = db.getData(db.ARTICLE_HEADER, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                setLoadItems(cursor, this.putOnHoldExists);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (loadingSpinner.isShowing()) {
                loadingSpinner.hide();
            }
            adapter.notifyDataSetChanged();
            list.setAdapter(adapter);
        }
    }
    public void setLoadItems(Cursor loadItemsCursor, boolean putOnHoldExists) {
        try{
            Cursor cursor = loadItemsCursor;
            do {
                LoadRequest loadRequest = new LoadRequest();
                loadRequest.setItemCode(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                loadRequest.setItemName(UrlBuilder.decodeString(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                loadRequest.setItemNameAr(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC2)));
                // loadRequest.setCases(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.CASE_UOM) ? "0" : "0");
                // loadRequest.setUnits(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.BOTTLES_UOM) ? "0" : "0");
                loadRequest.setUom(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)));
                loadRequest.setCases("0");
                loadRequest.setUnits("0");
                HashMap<String, String> altMap = new HashMap<>();
                altMap.put(db.KEY_UOM, "");
                HashMap<String, String> filter = new HashMap<>();
                filter.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                Cursor altUOMCursor = db.getData(db.ARTICLE_UOM, altMap, filter);
                if (altUOMCursor.getCount() > 0) {
                    altUOMCursor.moveToFirst();
                    if (cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(altUOMCursor.getString(altUOMCursor.getColumnIndex(db.KEY_UOM)))) {
                        loadRequest.setIsAltUOM(false);
                    } else {
                        loadRequest.setIsAltUOM(true);
                    }
                } else {
                    loadRequest.setIsAltUOM(false);
                }
                HashMap<String, String> priceMap = new HashMap<>();
                priceMap.put(db.KEY_AMOUNT, "");
                HashMap<String, String> filterPrice = new HashMap<>();
                filterPrice.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                filterPrice.put(db.KEY_PRIORITY, "2");
                Cursor priceCursor = db.getData(db.PRICING, priceMap, filterPrice);
                if (priceCursor.getCount() > 0) {
                    priceCursor.moveToFirst();
                    loadRequest.setPrice(priceCursor.getString(priceCursor.getColumnIndex(db.KEY_AMOUNT)));
                } else {
                    loadRequest.setPrice("0");
                }
                if (cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.CASE_UOM) || cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.CASE_UOM_NEW)) {
                    loadRequest.setIsCaseEnabled(true);
                    loadRequest.setIsUnitEnabled(false);
                } else if (cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.BOTTLES_UOM) || cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.BOTTLES_UOM)) {
                    loadRequest.setIsCaseEnabled(false);
                    loadRequest.setIsUnitEnabled(true);
                } else {
                    loadRequest.setIsCaseEnabled(true);
                    loadRequest.setIsUnitEnabled(true);
                }
                loadRequest.setMaterialNo(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                arraylist.add(loadRequest);
            }
            while (cursor.moveToNext());
            //Log.e("Array List zie", "" + arraylist.size());
            ArrayList<LoadRequest> temp = new ArrayList<>();
            if (putOnHoldExists) {
                for (int i = 0; i < arraylist.size(); i++) {
                    LoadRequest loadRequest = arraylist.get(i);
                    HashMap<String, String> map = new HashMap<>();
                    map.put(db.KEY_ITEM_NO, "");
                    map.put(db.KEY_MATERIAL_DESC1, "");
                    map.put(db.KEY_MATERIAL_NO, "");
                    map.put(db.KEY_MATERIAL_GROUP, "");
                    map.put(db.KEY_CASE, "");
                    map.put(db.KEY_UNIT, "");
                    map.put(db.KEY_UOM, "");
                    map.put(db.KEY_PRICE, "");
                    map.put(db.KEY_ORDER_ID, "");
                    map.put(db.KEY_PURCHASE_NUMBER, "");
                    HashMap<String, String> filter = new HashMap<>();
                    filter.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                    filter.put(db.KEY_IS_POSTED, App.DATA_PUT_ON_HOLD);
                    Cursor holdCursor = db.getData(db.LOAD_REQUEST, map, filter);
                    //Log.e("HOLD Cursor", "" + holdCursor.getCount());
                    if (holdCursor.getCount() > 0) {
                        holdCursor.moveToFirst();
                        do {
                            if (holdCursor.getString(holdCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM) || holdCursor.getString(holdCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM_NEW) ||holdCursor.getString(holdCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM) ) {
                                loadRequest.setCases(holdCursor.getString(holdCursor.getColumnIndex(db.KEY_CASE)));
                            }
                            loadRequest.setUnits("0");
                        }
                        while (holdCursor.moveToNext());
                        arraylist.remove(i);
                        arraylist.add(i, loadRequest);
                    }
                }
                //Log.e("Temp", "" + temp.size());
                temp.clear();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        //  adapter.notifyDataSetChanged();
    }
    /************************************************************
     @ Creating request for posting data.
     ************************************************************/
    public class postData extends AsyncTask<Void, Void, Void> {
        private ArrayList<String> returnList;
        private String orderID = "";
        private String[] tokens = new String[2];
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            //this.returnList = IntegrationService.RequestToken(LoadRequestActivity.this);
            this.orderID = postData();
            this.tokens = orderID.split(",");
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            //Log.e("Order ID", "" + this.orderID);
            try{
                if (this.tokens[0].toString().equals(this.tokens[1].toString())) {
                    for (LoadRequest loadRequest : arraylist) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                        map.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                        map.put(db.KEY_ORDER_ID, tokens[0].toString());
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                        filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                        filter.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                        // filter.put(db.KEY_ORDER_ID,tokens[1].toString());
                        filter.put(db.KEY_PURCHASE_NUMBER, tokens[1].toString());
                        db.updateData(db.LOAD_REQUEST, map, filter);
                    }
                    if (loadingSpinner.isShowing()) {
                        loadingSpinner.hide();
                    }
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoadRequestActivity.this);
                    alertDialogBuilder/*.setTitle("Message")*/
                            //.setMessage("Request with reference " + tokens[1].toString() + " has been saved")
                            .setMessage(getString(R.string.request_created))
                                    //.setMessage("Request with reference " + tokens[0].toString() + " has been saved")
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(Helpers.isNetworkAvailable(getApplicationContext())){
                                        Helpers.createBackgroundJob(getApplicationContext());
                                    }

                                    if(isPrint){
                                        dialog.dismiss();
                                        createPrintout(selecteddate.getText().toString(), tokens[0].toString(), false);
                                       /* Intent intent = new Intent(PreSaleOrderProceedActivity.this, PreSaleOrderActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("headerObj", object);
                                        startActivity(intent);
                                        finish();*/
                                    }
                                    else{
                                        dialog.dismiss();
                                        createPrintout(selecteddate.getText().toString(), tokens[0].toString(), true);
                                    }



                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                } else if (!tokens[0].toString().equals("")) {
                    for (LoadRequest loadRequest : arraylist) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                        map.put(db.KEY_IS_POSTED, App.DATA_IS_POSTED);
                        map.put(db.KEY_ORDER_ID, tokens[0].toString());
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                        filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                        filter.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                        //filter.put(db.KEY_ORDER_ID,tokens[1].toString());
                        filter.put(db.KEY_PURCHASE_NUMBER, tokens[1].toString());
                        db.updateData(db.LOAD_REQUEST, map, filter);
                    }
                    if (loadingSpinner.isShowing()) {
                        loadingSpinner.hide();
                    }
                    if (this.orderID.isEmpty() || this.orderID.equals("") || this.orderID == null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.request_timeout), Toast.LENGTH_SHORT).show();
                    } else if (this.orderID.contains("Error")) {
                        Toast.makeText(getApplicationContext(), this.orderID.replaceAll("Error", "").trim(), Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoadRequestActivity.this);
                        alertDialogBuilder.setTitle("Message")
                                .setMessage("Request " + tokens[1].toString() + " has been created")
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), this.orderID.replaceAll("Error", "").trim(), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
            }

        }
    }
    public boolean checkforNullBeforePost(){
        HashMap<String,String>map = new HashMap<>();
        map.put(db.KEY_CUSTOMER_NO, Settings.getString(App.DRIVER));
        map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
        map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
        Log.e("Return Data","" + db.checkData(db.LOAD_REQUEST,map));
        return db.checkData(db.LOAD_REQUEST,map);
    }
    public void createBackgroundJob(){
        /************************************************************
         @ If network is available create a background job
         ************************************************************/
        if(Helpers.isNetworkAvailable(getApplicationContext())){
            Helpers.createBackgroundJob(getApplicationContext());
        }
    }
    /************************************************************
     @ This method will create a printout when post is completed
     ************************************************************/
    private void createPrintout(String orderDate,String orderNo,boolean isDelayPrint){
        if(!isDelayPrint){
            try{
                JSONArray jsonArray = createPrintData(orderDate,orderNo);

                JSONObject data = new JSONObject();
                data.put("data",(JSONArray)jsonArray);

                HashMap<String,String>map = new HashMap<>();
                map.put(db.KEY_CUSTOMER_NO,Settings.getString(App.DRIVER));
                map.put(db.KEY_ORDER_ID,orderNo);
                map.put(db.KEY_DOC_TYPE,ConfigStore.LoadRequest_TR);
                map.put(db.KEY_DATA,data.toString());
                db.addDataPrint(db.DELAY_PRINT, map);

                PrinterHelper object = new PrinterHelper(LoadRequestActivity.this,LoadRequestActivity.this);
                object.execute("", jsonArray);
            }
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
            }

        }
        else{
            try{
                JSONArray jsonArray = createPrintData(orderDate,orderNo);
                JSONObject data = new JSONObject();
                data.put("data",(JSONArray)jsonArray);

                HashMap<String,String>map = new HashMap<>();
                map.put(db.KEY_CUSTOMER_NO,Settings.getString(App.DRIVER));
                map.put(db.KEY_ORDER_ID,orderNo);
                map.put(db.KEY_DOC_TYPE,ConfigStore.LoadRequest_TR);
                map.put(db.KEY_DATA,data.toString());
                db.addDataPrint(db.DELAY_PRINT, map);
                Intent intent = new Intent(LoadRequestActivity.this, ManageInventory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
            }

        }
    }
    /************************************************************
     @ Creating a json array so that when the user selects do not
     @ print and wants to print in future we dont have to regenerate
     @ the data. Rather can save the generated json array within the
     @ database for future printing
     ************************************************************/
    public JSONArray createPrintData(String orderDate,String orderNo){
        JSONArray jArr = new JSONArray();
        try{
            double totalPcs = 0;
            double totalAmount = 0;
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST,App.LOAD_REQUEST);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE",Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(),App.PRINT_DATE_FORMAT));
            mainArr.put("LOAD DATE",orderDate);
            mainArr.put("TIME",Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO","-");
            mainArr.put("DOCUMENT NO",orderNo);  //Load Summary No
            mainArr.put("TRIP START DATE",Helpers.formatDate(new Date(),"dd-MM-yyyy"));
            mainArr.put("supervisorname","-");
            mainArr.put("LANG",Settings.getString(App.LANGUAGE));
            mainArr.put("INVOICETYPE","ORDER REQUEST");
            mainArr.put("ORDERNO",orderNo);
            mainArr.put("invoicepaymentterms","3");
            String testAr = "هذا هو اختبار النص العربي";
            mainArr.put("TripID",Settings.getString(App.TRIP_ID));
            mainArr.put("TourID",Settings.getString(App.TRIP_ID));
            //mainArr.put("Load Number","1");


            JSONArray HEADERS = new JSONArray();
            JSONArray TOTAL = new JSONArray();

            HEADERS.put("ITEM NO");
            HEADERS.put("ENGLISH DESCRIPTION");
            HEADERS.put("ARABIC DESCRIPTION");
            HEADERS.put("UPC ");
            HEADERS.put("TOTAL UNITS");
            HEADERS.put("UNIT PRICE");
            HEADERS.put("AMOUNT");
            //HEADERS.put("Description");

            //HEADERS.put(obj1);
            // HEADERS.put(obj2);
            mainArr.put("HEADERS",HEADERS);


            JSONArray jData = new JSONArray();
            for(LoadRequest obj:arraylist){
                if(Double.parseDouble(obj.getCases())> 0 || Double.parseDouble(obj.getUnits())>0){
                    JSONArray data = new JSONArray();
                    data.put(StringUtils.stripStart(obj.getMaterialNo(), "0"));
                    data.put(obj.getItemName());
                    data.put(obj.getItemNameAr().equals("")?App.ARABIC_TEXT_MISSING:obj.getItemNameAr());
                    //data.put("شد 48*200مل بيرين PH8");
                    data.put("1");
                    data.put(obj.getCases());
                    totalPcs += Double.parseDouble(obj.getCases());
                    data.put(obj.getPrice());
                    data.put(String.valueOf(Double.parseDouble(obj.getCases()) * Double.parseDouble(obj.getPrice())));
                    totalAmount += Double.parseDouble(obj.getCases())*Double.parseDouble(obj.getPrice());
                    jData.put(data);
                }

            }
            JSONObject totalObj = new JSONObject();
            totalObj.put("TOTAL UNITS","+" + String.valueOf(totalPcs));
            totalObj.put("UNIT PRICE","");
            totalObj.put("AMOUNT","+" + String.valueOf(totalAmount));
            TOTAL.put(totalObj);
            mainArr.put("TOTAL",TOTAL);
            mainArr.put("data",jData);

            jDict.put("mainArr",mainArr);
            jInter.put(jDict);
            jArr.put(jInter);

            // jArr.put(HEADERS);
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return jArr;
    }
    /************************************************************
     @ Callback method is called once printing is completed
     ************************************************************/
    public void callback(){
        Intent intent = new Intent(LoadRequestActivity.this, ManageInventory.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
