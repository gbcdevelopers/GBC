package gbc.sa.vansales.activities;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.LoadSummaryBadgeAdapter;
import gbc.sa.vansales.adapters.LoadVerifyBadgeAdapter;
import gbc.sa.vansales.models.LoadDeliveryHeader;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.PrinterDataHelper;
import gbc.sa.vansales.utils.PrinterHelper;
import gbc.sa.vansales.utils.Settings;
/**
 * Created by Rakshit on 19-Nov-16.
 */
public class LoadVerifyActivity extends AppCompatActivity {
    ArrayList<LoadSummary> loadSummaryList;
    ArrayList<LoadSummary> varianceLoadSummaryList;
    ArrayAdapter<LoadSummary> adapter;
    private ListView listView;
    LoadDeliveryHeader object;
    DatabaseHandler db;
    private static final String TRIP_ID = "ITripId";
    ArrayList<LoadSummary> dataNew;
    ArrayList<LoadSummary> dataOld;
    LoadingSpinner loadingSpinner;
    private String tempOrderID = "";
    private boolean print = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_verify);
        db = new DatabaseHandler(LoadVerifyActivity.this);
        loadingSpinner = new LoadingSpinner(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        dataNew = new ArrayList<>();
        dataOld = new ArrayList<>();
        dataNew = intent.getParcelableArrayListExtra("loadSummary");
        dataOld = intent.getParcelableArrayListExtra("loadSummaryOld");
        object = (LoadDeliveryHeader) intent.getParcelableExtra("headerObj");
        // Log.e("****",""+dataOld.size());
        loadSummaryList = new ArrayList<>();
        varianceLoadSummaryList = new ArrayList<>();
        //  loadSummaryList = dataNew;
        adapter = new LoadVerifyBadgeAdapter(this, loadSummaryList);
        loadSummaryList = generateData(dataNew, dataOld);
        calculateCost();
        listView = (ListView) findViewById(R.id.loadSummaryList);
        listView.setAdapter(adapter);
    }
    private void calculateCost() {
        int salesTotal = 0;
        int pcsTotal = 0;
        double total = 0;
        for (LoadSummary item : loadSummaryList) {
            double itemPrice = 0;
            String[] cases = item.getQuantityCases().split("\\|");
            if (!item.isAltUOM()) {
                itemPrice = Double.parseDouble(cases[1]) * Double.parseDouble(item.getPrice());
            }
            total += itemPrice;
        }
        TextView tv = (TextView) findViewById(R.id.tv_amt);
        tv.setText(String.valueOf(total));
    }
    public void confirmLoad(View v) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(db.KEY_IS_VERIFIED, "true");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_DELIVERY_NO, object.getDeliveryNo());
        db.updateData(db.LOAD_DELIVERY_HEADER, parameters, filters);
        addItemstoVan(dataNew);
        if (!checkIfLoadExists()) {
            if (createDataForPost(dataNew, dataOld)) {
                HashMap<String, String> altMap = new HashMap<>();
                altMap.put(db.KEY_IS_LOAD_VERIFIED, "true");
                HashMap<String, String> filter = new HashMap<>();
                filter.put(db.KEY_IS_LOAD_VERIFIED, "false");
                db.updateData(db.LOCK_FLAGS, altMap, filter);
                final Dialog dialog = new Dialog(LoadVerifyActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_doprint);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                HashMap<String, String> searchMap = new HashMap<>();
                searchMap.put(db.KEY_ORDER_ID, "");
                HashMap<String, String> filterMap = new HashMap<>();
                filterMap.put(db.KEY_DELIVERY_NO, object.getDeliveryNo());
                Cursor cursor = db.getData(db.LOAD_DELIVERY_ITEMS, searchMap, filterMap);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    tempOrderID = cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID));
                }

                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                map.put(db.KEY_CUSTOMER_NO,Settings.getString(App.DRIVER));
                map.put(db.KEY_FUNCTION, ConfigStore.LoadConfirmationFunction);
                map.put(db.KEY_ORDER_ID, tempOrderID);
                map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                map.put(db.KEY_IS_PRINTED, App.DATA_NOT_POSTED);
                db.addData(db.LOAD_CONFIRMATION_HEADER, map);
                btn_print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        print = true;
                        new postData(tempOrderID);
                        dialog.dismiss();
                        //finish();
                    }
                });
                btn_notprint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        print = false;
                        new postData(tempOrderID);
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
                /*Intent intent = new Intent(LoadVerifyActivity.this,MyCalendarActivity.class);
                startActivity(intent);*/
            }
        } else {

            HashMap<String, String> searchMap = new HashMap<>();
            searchMap.put(db.KEY_ORDER_ID, "");
            HashMap<String, String> filterMap = new HashMap<>();
            filterMap.put(db.KEY_DELIVERY_NO, object.getDeliveryNo());
            Cursor cursor = db.getData(db.LOAD_DELIVERY_ITEMS, searchMap, filterMap);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                tempOrderID = cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID));
            }
            //tempOrderID = "0000000075";
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
            map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
            map.put(db.KEY_FUNCTION, ConfigStore.LoadConfirmationFunction);
            map.put(db.KEY_ORDER_ID, tempOrderID);
            map.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
            map.put(db.KEY_IS_PRINTED, App.DATA_MARKED_FOR_POST);
            db.addData(db.LOAD_CONFIRMATION_HEADER, map);

            Intent intent = new Intent(LoadVerifyActivity.this, LoadActivity.class);
            startActivity(intent);
        }
    }
    public void cancel(View v) {
        Intent intent = new Intent(LoadVerifyActivity.this, LoadSummaryActivity.class);
        intent.putExtra("headerObj", object);
        startActivity(intent);
    }
    private boolean checkIfLoadExists() {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TRIP_ID, "");
        map.put(db.KEY_DELIVERY_NO, "");
        map.put(db.KEY_DELIVERY_DATE, "");
        map.put(db.KEY_DELIVERY_TYPE, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TRIP_ID, Settings.getString(TRIP_ID));
        filters.put(db.KEY_IS_VERIFIED, "false");
        Cursor cursor = db.getData(db.LOAD_DELIVERY_HEADER, map, filters);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
    private boolean checkMaterialExists(String materialno, String uom) {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_MATERIAL_NO, "");
        if (uom.equals(App.CASE_UOM)) {
            map.put(db.KEY_UOM_CASE, "");
        } else if (uom.equals(App.BOTTLES_UOM)) {
            map.put(db.KEY_UOM_UNIT, "");
        }
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_MATERIAL_NO, materialno);
        if (uom.equals(App.CASE_UOM)) {
            filters.put(db.KEY_UOM_CASE, uom);
        } else if (uom.equals(App.BOTTLES_UOM)) {
            filters.put(db.KEY_UOM_UNIT, uom);
        }
        Cursor cursor = db.getData(db.VAN_STOCK_ITEMS, map, filters);
        //Log.e("Cursorcountcheck", "" + materialno + uom + cursor.getCount());
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
    private void addItemstoVan(ArrayList<LoadSummary> dataNew) {
        for (int i = 0; i < dataNew.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_ENTRY_TIME, Helpers.getCurrentTimeStamp());
            map.put(db.KEY_DELIVERY_NO, object.getDeliveryNo().toString());
            map.put(db.KEY_MATERIAL_NO, dataNew.get(i).getMaterialNo().toString());
            map.put(db.KEY_ITEM_NO, dataNew.get(i).getItemCode().toString());
            map.put(db.KEY_MATERIAL_DESC1, dataNew.get(i).getItemDescription().toString());
            if (!((dataNew.get(i).getQuantityCases().isEmpty()) || (dataNew.get(i).getQuantityCases().equals(""))
                    || (dataNew.get(i).getQuantityCases().equals("0")) || (dataNew.get(i).getQuantityCases().equals("")))) {
                //Check if old data exists for case
                if (checkMaterialExists(dataNew.get(i).getMaterialNo().toString(), App.CASE_UOM)) {
                    //Logic to read Customer Delivery Item and block material quantity based on UOM
                    int reserved = 0;
                    //Getting old data
                    HashMap<String, String> oldData = new HashMap<>();
                    oldData.put(db.KEY_MATERIAL_NO, "");
                    oldData.put(db.KEY_UOM_CASE, "");
                    oldData.put(db.KEY_ACTUAL_QTY_CASE, "");
                    oldData.put(db.KEY_RESERVED_QTY_CASE, "");
                    oldData.put(db.KEY_REMAINING_QTY_CASE, "");
                    HashMap<String, String> filterOldData = new HashMap<>();
                    filterOldData.put(db.KEY_MATERIAL_NO, dataNew.get(i).getMaterialNo().toString());
                    filterOldData.put(db.KEY_UOM_CASE, App.CASE_UOM);
                    Cursor cursor = db.getData(db.VAN_STOCK_ITEMS, oldData, filterOldData);
                    float actualQtyCase = 0;
                    float reservedQtyCase = 0;
                    float remainingQtyCase = 0;
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            actualQtyCase = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY_CASE)));
                            reservedQtyCase = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_RESERVED_QTY_CASE)));
                            remainingQtyCase = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_CASE)));
                            actualQtyCase += Float.parseFloat(dataNew.get(i).getQuantityCases().toString());
                            remainingQtyCase += Float.parseFloat(dataNew.get(i).getQuantityCases().toString());
                        }
                        while (cursor.moveToNext());
                        map.put(db.KEY_ACTUAL_QTY_CASE, String.valueOf(actualQtyCase));
                        map.put(db.KEY_RESERVED_QTY_CASE, String.valueOf(reservedQtyCase));
                        map.put(db.KEY_REMAINING_QTY_CASE, String.valueOf(remainingQtyCase));
                        map.put(db.KEY_UOM_CASE, App.CASE_UOM);
                        // db.updateData(db.VAN_STOCK_ITEMS, map, filterOldData);
                    }
                } else {
                    //Logic to read Customer Delivery Item and block material quantity based on UOM
                    int reservedQty = 0;
                    map.put(db.KEY_ACTUAL_QTY_CASE, dataNew.get(i).getQuantityCases().toString());
                    map.put(db.KEY_RESERVED_QTY_CASE, String.valueOf(reservedQty));
                    map.put(db.KEY_REMAINING_QTY_CASE, String.valueOf(Float.parseFloat(dataNew.get(i).getQuantityCases().toString()) - reservedQty));
                    map.put(db.KEY_UOM_CASE, App.CASE_UOM);
                    // db.addData(db.VAN_STOCK_ITEMS, map);
                }
                /*map.put(db.KEY_ACTUAL_QTY_UNIT,dataNew.get(i).getQuantityUnits().toString());
                map.put(db.KEY_RESERVED_QTY_UNIT,String.valueOf("0"));
                map.put(db.KEY_REMAINING_QTY_UNIT,String.valueOf(Float.parseFloat(dataNew.get(i).getQuantityUnits().toString())-0));
                map.put(db.KEY_UOM_UNIT, App.BOTTLES_UOM);*/
            } else {
                int reservedQty = 0;
                map.put(db.KEY_ACTUAL_QTY_CASE, dataNew.get(i).getQuantityCases().toString());
                map.put(db.KEY_RESERVED_QTY_CASE, String.valueOf(reservedQty));
                map.put(db.KEY_REMAINING_QTY_CASE, String.valueOf(Float.parseFloat(dataNew.get(i).getQuantityCases().toString()) - reservedQty));
                map.put(db.KEY_UOM_CASE, App.CASE_UOM);
            }
            if (!((dataNew.get(i).getQuantityUnits().isEmpty()) || (dataNew.get(i).getQuantityUnits().equals(""))
                    || (dataNew.get(i).getQuantityUnits().equals("0")) || (dataNew.get(i).getQuantityUnits().equals("")))) {
                //Log.e("Step 0","Step 0");
                if (checkMaterialExists(dataNew.get(i).getMaterialNo().toString(), App.BOTTLES_UOM)) {
                    //Logic to read Customer Delivery Item and block material quantity based on UOM
                    int reserved = 0;
                    //Log.e("Step 1","Step 1");
                    //Getting old data
                    HashMap<String, String> oldData = new HashMap<>();
                    oldData.put(db.KEY_MATERIAL_NO, "");
                    oldData.put(db.KEY_UOM_UNIT, "");
                    oldData.put(db.KEY_ACTUAL_QTY_UNIT, "");
                    oldData.put(db.KEY_RESERVED_QTY_UNIT, "");
                    oldData.put(db.KEY_REMAINING_QTY_UNIT, "");
                    HashMap<String, String> filterOldData = new HashMap<>();
                    filterOldData.put(db.KEY_MATERIAL_NO, dataNew.get(i).getMaterialNo().toString());
                    filterOldData.put(db.KEY_UOM_UNIT, App.BOTTLES_UOM);
                    Cursor cursor = db.getData(db.VAN_STOCK_ITEMS, oldData, filterOldData);
                    //Log.e("Step 2","Step 2" + cursor.getCount());
                    float actualQtyUnit;
                    float reservedQtyUnit;
                    float remainingQtyUnit;
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            actualQtyUnit = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY_UNIT)));
                            reservedQtyUnit = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_RESERVED_QTY_UNIT)));
                            remainingQtyUnit = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_UNIT)));
                            //Log.e("Actual Qty Unit1","" + actualQtyUnit);
                            //Log.e("REmaining Qty Unit1","" + remainingQtyUnit);
                            actualQtyUnit += Float.parseFloat(dataNew.get(i).getQuantityUnits().toString());
                            remainingQtyUnit += Float.parseFloat(dataNew.get(i).getQuantityUnits().toString());
                            //Log.e("Actual Qty Unit2","" + actualQtyUnit);
                            //Log.e("REmaining Qty Unit2","" + remainingQtyUnit);
                        }
                        while (cursor.moveToNext());
                        map.put(db.KEY_ACTUAL_QTY_UNIT, String.valueOf(actualQtyUnit));
                        map.put(db.KEY_RESERVED_QTY_UNIT, String.valueOf(reservedQtyUnit));
                        map.put(db.KEY_REMAINING_QTY_UNIT, String.valueOf(remainingQtyUnit));
                        map.put(db.KEY_UOM_UNIT, App.BOTTLES_UOM);
                        //db.updateData(db.VAN_STOCK_ITEMS, map, filterOldData);
                    }
                } else {
                    //Logic to read Customer Delivery Item and block material quantity based on UOM
                    int reservedQty = 0;
                    map.put(db.KEY_ACTUAL_QTY_UNIT, dataNew.get(i).getQuantityUnits().toString());
                    map.put(db.KEY_RESERVED_QTY_UNIT, String.valueOf(reservedQty));
                    map.put(db.KEY_REMAINING_QTY_UNIT, String.valueOf(Float.parseFloat(dataNew.get(i).getQuantityUnits().toString()) - reservedQty));
                    map.put(db.KEY_UOM_UNIT, App.BOTTLES_UOM);
                }

               /* map.put(db.KEY_ACTUAL_QTY_CASE,dataNew.get(i).getQuantityCases().toString());
                map.put(db.KEY_RESERVED_QTY_CASE,String.valueOf("0"));
                map.put(db.KEY_REMAINING_QTY_CASE,String.valueOf(Float.parseFloat(dataNew.get(i).getQuantityCases().toString())-0));
                map.put(db.KEY_UOM_CASE, App.CASE_UOM);*/
            } else {
                int reservedQty = 0;
                map.put(db.KEY_ACTUAL_QTY_UNIT, dataNew.get(i).getQuantityUnits().toString());
                map.put(db.KEY_RESERVED_QTY_UNIT, String.valueOf(reservedQty));
                map.put(db.KEY_REMAINING_QTY_UNIT, String.valueOf(Float.parseFloat(dataNew.get(i).getQuantityUnits().toString()) - reservedQty));
                map.put(db.KEY_UOM_UNIT, App.BOTTLES_UOM);
            }
            //Log.e("ITEM MAP", "" + map);
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_MATERIAL_NO, dataNew.get(i).getMaterialNo().toString());
            //Log.e("Step 3", "Step 3");
            if ((checkMaterialExists(dataNew.get(i).getMaterialNo().toString(), App.CASE_UOM)) || (checkMaterialExists(dataNew.get(i).getMaterialNo().toString(), App.BOTTLES_UOM))) {
                db.updateData(db.VAN_STOCK_ITEMS, map, filter);
            } else {
                db.addData(db.VAN_STOCK_ITEMS, map);
            }
        }
    }
    private boolean createDataForPost(ArrayList<LoadSummary> dataNew, ArrayList<LoadSummary> dataOld) {
        for (int i = 0; i < dataNew.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_ENTRY_TIME, Helpers.getCurrentTimeStamp());
            map.put(db.KEY_DELIVERY_NO, object.getDeliveryNo().toString());
            map.put(db.KEY_MATERIAL_NO, dataNew.get(i).getMaterialNo().toString());
            map.put(db.KEY_ITEM_NO, dataNew.get(i).getItemCode().toString());
            map.put(db.KEY_MATERIAL_DESC1, dataNew.get(i).getItemDescription().toString());
            if (dataNew.get(i).getItemCode().equals(dataOld.get(i).getItemCode())) {
                // Log.e("I m here","here")
                map.put(db.KEY_ORG_CASE, dataOld.get(i).getQuantityCases());
                map.put(db.KEY_VAR_CASE, dataNew.get(i).getQuantityCases());
                map.put(db.KEY_ORG_UNITS, dataOld.get(i).getQuantityUnits());
                map.put(db.KEY_VAR_UNITS, dataNew.get(i).getQuantityUnits());
            }
            db.addData(db.LOAD_DELIVERY_ITEMS_POST, map);
        }
        return true;
    }
    public ArrayList<LoadSummary> generateData(ArrayList<LoadSummary> dataNew, ArrayList<LoadSummary> dataOld) {
        for (int i = 0; i < dataNew.size(); i++) {
            LoadSummary loadSummary = new LoadSummary();
            loadSummary.setItemCode(dataNew.get(i).getItemCode());
            loadSummary.setMaterialNo(dataNew.get(i).getMaterialNo());
            loadSummary.setItemDescription(dataNew.get(i).getItemDescription());
            if (dataNew.get(i).getItemCode().equals(dataOld.get(i).getItemCode())) {
                loadSummary.setQuantityCases(dataOld.get(i).getQuantityCases() + "|" + dataNew.get(i).getQuantityCases());
            }
            if (dataNew.get(i).getItemCode().equals(dataOld.get(i).getItemCode())) {
                loadSummary.setQuantityUnits(dataOld.get(i).getQuantityUnits() + "|" + dataNew.get(i).getQuantityUnits());
            }
            loadSummary.setPrice(dataNew.get(i).getPrice());
            loadSummaryList.add(loadSummary);
        }
        adapter.notifyDataSetChanged();
        return loadSummaryList;
    }
    private boolean addVarianceforPost(ArrayList<LoadSummary> dataNew, ArrayList<LoadSummary> dataOld) {
        for (int i = 0; i < dataNew.size(); i++) {
            LoadSummary loadSummary = new LoadSummary();
            loadSummary.setItemCode(dataNew.get(i).getItemCode());
            loadSummary.setMaterialNo(dataNew.get(i).getMaterialNo());
            loadSummary.setItemDescription(dataNew.get(i).getItemDescription());
            if (dataNew.get(i).getItemCode().equals(dataOld.get(i).getItemCode())) {
                if (dataNew.get(i).getQuantityCases().equals(dataOld.get(i).getQuantityCases())) {
                    loadSummary.setQuantityCases("0");
                } else {
                    String code = Double.parseDouble(dataNew.get(i).getQuantityCases()) > Double.parseDouble(dataOld.get(i).getQuantityCases()) ? "C" : "D";
                    String quantity = code.equals("C")
                            ? String.valueOf(Double.parseDouble(dataNew.get(i).getQuantityCases()) - Double.parseDouble(dataOld.get(i).getQuantityCases()))
                            : String.valueOf(Double.parseDouble(dataOld.get(i).getQuantityCases()) - Double.parseDouble(dataNew.get(i).getQuantityCases()));
                    loadSummary.setQuantityCases(quantity + code);
                }
                //loadSummary.setQuantityCases(dataOld.get(i).getQuantityCases() + "|" + dataNew.get(i).getQuantityCases());
            }
            if (dataNew.get(i).getItemCode().equals(dataOld.get(i).getItemCode())) {
                if (dataNew.get(i).getQuantityUnits().equals(dataOld.get(i).getQuantityUnits())) {
                    loadSummary.setQuantityUnits("0");
                } else {
                    String code = Double.parseDouble(dataNew.get(i).getQuantityUnits()) > Double.parseDouble(dataOld.get(i).getQuantityUnits()) ? "C" : "D";
                    String quantity = code.equals("C")
                            ? String.valueOf(Double.parseDouble(dataNew.get(i).getQuantityUnits()) - Double.parseDouble(dataOld.get(i).getQuantityUnits()))
                            : String.valueOf(Double.parseDouble(dataOld.get(i).getQuantityUnits()) - Double.parseDouble(dataNew.get(i).getQuantityUnits()));
                    loadSummary.setQuantityUnits(quantity + code);
                }
                //loadSummary.setQuantityUnits(dataOld.get(i).getQuantityUnits() + "|" + dataNew.get(i).getQuantityUnits());
            }
            loadSummary.setPrice(dataNew.get(i).getPrice());
            loadSummary.setReasonCode(dataNew.get(i).getReasonCode());
            if (loadSummary.getQuantityCases().equals("0") && loadSummary.getQuantityUnits().equals("0")) {
            } else {
                varianceLoadSummaryList.add(loadSummary);
            }
            //loadSummaryList.add(loadSummary);
        }
        if (varianceLoadSummaryList.size() > 0) {
            String debitPRNo = "";
            String creditPRNo = "";
            for (int i = 0; i < varianceLoadSummaryList.size(); i++) {
                LoadSummary loadSummary = varianceLoadSummaryList.get(i);
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_DATE, Helpers.formatDate(new Date(), App.DATE_FORMAT));
                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                map.put(db.KEY_CUSTOMER_NO, Settings.getString(App.DRIVER));
                map.put(db.KEY_ITEM_NO, loadSummary.getItemCode());
                map.put(db.KEY_MATERIAL_DESC1, loadSummary.getItemDescription());
                map.put(db.KEY_MATERIAL_NO, loadSummary.getMaterialNo());
                map.put(db.KEY_MATERIAL_GROUP, "");
                map.put(db.KEY_CASE, loadSummary.getQuantityCases().contains("D")
                        ? loadSummary.getQuantityCases().replaceAll("D", "").trim()
                        : loadSummary.getQuantityCases().replaceAll("C", "").trim());
                // map.put(db.KEY_CASE_DIFF,"");
                map.put(db.KEY_UNIT, loadSummary.getQuantityUnits().contains("D")
                        ? loadSummary.getQuantityUnits().replaceAll("D", "").trim()
                        : loadSummary.getQuantityUnits().replaceAll("C", "").trim());
                // map.put(db.KEY_UNIT,"");
                // map.put(db.KEY_UNIT_DIFF,"");
                if (loadSummary.getQuantityCases().contains("D") || loadSummary.getQuantityUnits().contains("D")) {
                    if (debitPRNo.equals("")) {
                        debitPRNo = Helpers.generateNumber(db, ConfigStore.LoadVarianceDebit_PR_Type);
                    }
                }
                if (loadSummary.getQuantityCases().contains("C") || loadSummary.getQuantityUnits().contains("C")) {
                    if (creditPRNo.equals("")) {
                        creditPRNo = Helpers.generateNumber(db, ConfigStore.LoadVarianceCredit_PR_Type);
                    }
                }
                map.put(db.KEY_UOM, loadSummary.getUom());
                map.put(db.KEY_PRICE, loadSummary.getPrice());
                map.put(db.KEY_ORDER_ID, loadSummary.getQuantityCases().contains("D") || loadSummary.getQuantityUnits().contains("D") ? debitPRNo : creditPRNo);
                map.put(db.KEY_REASON_CODE, loadSummary.getReasonCode());
                map.put(db.KEY_REASON_DESCRIPTION, "");
                map.put(db.KEY_DOCUMENT_TYPE, loadSummary.getQuantityCases().contains("D") || loadSummary.getQuantityUnits().contains("D") ? ConfigStore.LoadVarianceDebit : ConfigStore.LoadVarianceCredit);
                map.put(db.KEY_PURCHASE_NUMBER, loadSummary.getQuantityCases().contains("D") || loadSummary.getQuantityUnits().contains("D") ? debitPRNo : creditPRNo);
                map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                map.put(db.KEY_IS_PRINTED, "");
                db.addData(db.LOAD_VARIANCE_ITEMS_POST, map);
            }
        }
        return true;
    }
    private String postData(String tempOrderID) {
        String orderID = "";
        /*HashMap<String, String> searchMap = new HashMap<>();
        searchMap.put(db.KEY_ORDER_ID, "");
        HashMap<String, String> filterMap = new HashMap<>();
        filterMap.put(db.KEY_DELIVERY_NO, object.getDeliveryNo());
        Cursor cursor = db.getData(db.LOAD_DELIVERY_ITEMS, searchMap, filterMap);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            tempOrderID = cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID));
        }*/
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("Function", ConfigStore.LoadConfirmationFunction);
            // map.put("OrderId", "0000000022");  //For testing purpose
            map.put("OrderId", tempOrderID);
            map.put("CustomerId", Settings.getString(App.DRIVER));
            JSONArray deepEntity = new JSONArray();
            JSONObject obj = new JSONObject();
            deepEntity.put(obj);
            orderID = IntegrationService.postData(LoadVerifyActivity.this, App.POST_COLLECTION, map, deepEntity);
           // orderID = IntegrationService.postDataBackup(LoadVerifyActivity.this, App.POST_COLLECTION, map, deepEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderID + "," + tempOrderID;
    }
    private String postDataVariance(String varianceType) {
        String orderID = "";
        String purchaseNumber = "";
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put(db.KEY_DATE, "");
        dataMap.put(db.KEY_TIME_STAMP, "");
        dataMap.put(db.KEY_TRIP_ID, "");
        dataMap.put(db.KEY_CUSTOMER_NO, "");
        dataMap.put(db.KEY_ITEM_NO, "");
        dataMap.put(db.KEY_MATERIAL_DESC1, "");
        dataMap.put(db.KEY_MATERIAL_NO, "");
        dataMap.put(db.KEY_MATERIAL_GROUP, "");
        dataMap.put(db.KEY_CASE, "");
        dataMap.put(db.KEY_UNIT, "");
        dataMap.put(db.KEY_UOM, "");
        dataMap.put(db.KEY_PRICE, "");
        dataMap.put(db.KEY_ORDER_ID, "");
        dataMap.put(db.KEY_REASON_CODE, "");
        dataMap.put(db.KEY_REASON_DESCRIPTION, "");
        dataMap.put(db.KEY_DOCUMENT_TYPE, "");
        dataMap.put(db.KEY_PURCHASE_NUMBER, "");
        dataMap.put(db.KEY_IS_POSTED, "");
        dataMap.put(db.KEY_IS_PRINTED, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_DOCUMENT_TYPE, varianceType);
        Cursor cursor = db.getData(db.LOAD_VARIANCE_ITEMS_POST, dataMap, filter);
        Log.e("Cursor count", "" + cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            try {
                HashMap<String, String> map = new HashMap<>();
                map.put("Function", ConfigStore.LoadVarianceFunction);
                map.put("OrderId", "");
                map.put("DocumentType", varianceType);
                // map.put("DocumentDate", Helpers.formatDate(new Date(),App.DATE_FORMAT_WO_SPACE));
                // map.put("DocumentDate", null);
           /* map.put("PurchaseNum", Helpers.generateNumber(db,ConfigStore.LoadRequest_PR_Type));
            purchaseNumber = map.get("PurchaseNum");*/
                map.put("CustomerId", Settings.getString(App.DRIVER));
                map.put("SalesOrg", Settings.getString(App.SALES_ORG));
                map.put("DistChannel", Settings.getString(App.DIST_CHANNEL));
                map.put("Division", Settings.getString(App.DIVISION));
                // map.put("OrderValue", String.valueOf(orderTotalValue));
                map.put("OrderValue", "0");
                map.put("Currency", "SAR");
                map.put("PurchaseNum", cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID)));
                purchaseNumber = map.get("PurchaseNum");
                //  map.put("DocumentDate", Helpers.parseDateforPost(cursor.getString(cursor.getColumnIndex(db.KEY_DATE))));
                JSONArray deepEntity = new JSONArray();
                int itemno = 10;
                do {
                    if (cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM) || cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)) {
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
                        //jo.put("RejReason",cursor.getString(cursor.getColumnIndex(db.KEY_REASON_CODE)));
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno + 10;
                        deepEntity.put(jo);
                    } else {
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
                        //jo.put("RejReason",cursor.getString(cursor.getColumnIndex(db.KEY_REASON_CODE)));
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno + 10;
                        deepEntity.put(jo);
                    }
                }
                while (cursor.moveToNext());
                Log.e("Variance", "" + map + deepEntity);
                orderID = IntegrationService.postData(LoadVerifyActivity.this, App.POST_COLLECTION, map, deepEntity);
               // orderID = IntegrationService.postDataBackup(LoadVerifyActivity.this, App.POST_COLLECTION, map, deepEntity);
            } catch (Exception e) {
                Log.e("Variance Error", "Variance Error");
                e.printStackTrace();
            }
        }
        Log.e("ORDERID", "" + orderID);
        return orderID + "," + purchaseNumber;
    }

    private void printData(String orderId){
        try{
            PrinterDataHelper printerDataHelper = new PrinterDataHelper();
            HashMap<String,String> header = new HashMap<>();
            header.put("ROUTE",Settings.getString(App.ROUTE));
            header.put("DOC DATE", Helpers.formatDate(new Date(), "dd-MM-yyyy"));
            header.put("TIME",Helpers.formatTime(new Date(), "hh:mm"));
            header.put("SALESMAN", Settings.getString(App.DRIVER));
            header.put("CONTACTNO","1234");
            header.put("DOCUMENT NO",orderId);  //Load Summary No
            header.put("TRIP START DATE",Helpers.formatDate(new Date(),"dd-MM-yyyy"));
            header.put("supervisorname","-");
            header.put("TripID",Settings.getString(App.TRIP_ID));
            header.put("Load Number","1");
            JSONArray jsonArray = printerDataHelper.createJSONData(App.LOAD_SUMMARY_REQUEST,header,dataNew,null);
            PrinterHelper object = new PrinterHelper(LoadVerifyActivity.this,LoadVerifyActivity.this);
            //object.execute("",createDataForPrint()); //For Load Summary
            object.execute(App.LOAD_SUMMARY_REQUEST,jsonArray);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public class postData extends AsyncTask<Void, Void, Void> {
        String orderID = "";
        String tempOrderID = "";
        private String[] tokens = new String[2];
        private postData(String tempOrderID) {
            this.tempOrderID = tempOrderID;
            execute();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            this.orderID = postData(tempOrderID);
            this.tokens = orderID.split(",");
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            try{
                if (this.tokens[0].toString().equals(this.tokens[1].toString())) {

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    map.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                    map.put(db.KEY_ORDER_ID, tokens[0].toString());
                    HashMap<String, String> filter = new HashMap<>();
                    filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                    db.updateData(db.LOAD_CONFIRMATION_HEADER, map, filter);
                    if (loadingSpinner.isShowing()) {
                        loadingSpinner.hide();
                    }
                    boolean test = addVarianceforPost(dataNew, dataOld);
                    HashMap<String, String> checkMap = new HashMap<>();
                    checkMap.put(db.KEY_DOCUMENT_TYPE, ConfigStore.LoadVarianceDebit);
                    HashMap<String, String> checkMapCredit = new HashMap<>();
                    checkMapCredit.put(db.KEY_DOCUMENT_TYPE, ConfigStore.LoadVarianceCredit);
                    if (db.checkData(db.LOAD_VARIANCE_ITEMS_POST, checkMapCredit) || db.checkData(db.LOAD_VARIANCE_ITEMS_POST, checkMap)) {
                        if (db.checkData(db.LOAD_VARIANCE_ITEMS_POST, checkMap)) {
                            new postDataVariance(ConfigStore.LoadVarianceDebit);
                        } else if (db.checkData(db.LOAD_VARIANCE_ITEMS_POST, checkMapCredit)) {
                            new postDataVariance(ConfigStore.LoadVarianceCredit);
                        }
                    } else {
                        new updateStockforCustomer().execute();
                    }
                } else if (this.orderID != null && !(this.orderID.equalsIgnoreCase(""))) {
                    if (loadingSpinner.isShowing()) {
                        loadingSpinner.hide();
                    }
                    boolean test = addVarianceforPost(dataNew, dataOld);
                    HashMap<String, String> checkMap = new HashMap<>();
                    checkMap.put(db.KEY_DOCUMENT_TYPE, ConfigStore.LoadVarianceDebit);
                    HashMap<String, String> checkMapCredit = new HashMap<>();
                    checkMapCredit.put(db.KEY_DOCUMENT_TYPE, ConfigStore.LoadVarianceCredit);
                    if (db.checkData(db.LOAD_VARIANCE_ITEMS_POST, checkMapCredit) || db.checkData(db.LOAD_VARIANCE_ITEMS_POST, checkMap)) {
                        if (db.checkData(db.LOAD_VARIANCE_ITEMS_POST, checkMap)) {
                            new postDataVariance(ConfigStore.LoadVarianceDebit);
                        } else if (db.checkData(db.LOAD_VARIANCE_ITEMS_POST, checkMapCredit)) {
                            new postDataVariance(ConfigStore.LoadVarianceCredit);
                        }
                    } else {
                        new updateStockforCustomer().execute();
                    }
               /* Intent intent = new Intent(LoadVerifyActivity.this,MyCalendarActivity.class);
                startActivity(intent);*/
                } else if (this.orderID.contains("Error")) {
                    if (loadingSpinner.isShowing()) {
                        loadingSpinner.hide();
                    }
                /*if(print){
                    printData(this.tokens[0].toString());
                }*/
                    Toast.makeText(getApplicationContext(), this.orderID.replaceAll("Error", "").trim(), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                if (loadingSpinner.isShowing()) {
                    loadingSpinner.hide();
                }
                e.printStackTrace();
                new updateStockforCustomer().execute();
            }

        }
    }
    public class postDataVariance extends AsyncTask<Void, Void, Void> {
        String varianceType = "";
        String orderID = "";
        private String[] tokens = new String[2];

        private postDataVariance(String varianceType) {
            this.varianceType = varianceType;
            execute();
        }
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            this.orderID = postDataVariance(this.varianceType);
            this.tokens = orderID.split(",");
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (loadingSpinner.isShowing()) {
                loadingSpinner.hide();
            }
            if (this.tokens[0].toString().equals(this.tokens[1].toString())) {
                if(varianceType.equals(ConfigStore.LoadVarianceDebit)){
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    map.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                    map.put(db.KEY_ORDER_ID, tokens[0].toString());
                    HashMap<String, String> filter = new HashMap<>();
                    filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                    filter.put(db.KEY_DOCUMENT_TYPE,ConfigStore.LoadVarianceDebit);

                    db.updateData(db.LOAD_VARIANCE_ITEMS_POST, map, filter);
                    if (loadingSpinner.isShowing()) {
                        loadingSpinner.hide();
                    }
                    HashMap<String, String> checkMapCredit = new HashMap<>();
                    checkMapCredit.put(db.KEY_DOCUMENT_TYPE, ConfigStore.LoadVarianceCredit);
                    if (db.checkData(db.LOAD_VARIANCE_ITEMS_POST, checkMapCredit)) {
                        new postDataVariance(ConfigStore.LoadVarianceCredit);
                    } else {
                        new updateStockforCustomer().execute();
                    }
                }
                else{

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    map.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                    map.put(db.KEY_ORDER_ID, tokens[0].toString());
                    HashMap<String, String> filter = new HashMap<>();
                    filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                    filter.put(db.KEY_DOCUMENT_TYPE,ConfigStore.LoadVarianceCredit);

                    db.updateData(db.LOAD_VARIANCE_ITEMS_POST, map, filter);

                    new updateStockforCustomer().execute();
                }
            }
            /*if (varianceType.equals(ConfigStore.LoadVarianceDebit)) {
                HashMap<String, String> checkMapCredit = new HashMap<>();
                checkMapCredit.put(db.KEY_DOCUMENT_TYPE, ConfigStore.LoadVarianceCredit);
                if (db.checkData(db.LOAD_VARIANCE_ITEMS_POST, checkMapCredit)) {
                    new postDataVariance(ConfigStore.LoadVarianceCredit);
                } else {
                    new updateStockforCustomer().execute();
                }
            } else {
                new updateStockforCustomer().execute();
            }*/
        }
    }
    public class updateStockforCustomer extends AsyncTask<Void, Void, Void> {
        LoadingSpinner updateSpinner = new LoadingSpinner(LoadVerifyActivity.this, getString(R.string.updatingdata));
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            updateSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            calculateStock();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(Helpers.isNetworkAvailable(getApplicationContext())){
                Helpers.createBackgroundJob(getApplicationContext());
            }
            if (updateSpinner.isShowing()) {
                updateSpinner.hide();
            }

            if(print){
                PrinterDataHelper dataHelper = new PrinterDataHelper();
                HashMap<String,String> header = new HashMap<>();
                //dataHelper.createJSONData(App.LOAD_SUMMARY_REQUEST,)
                Intent intent = new Intent(LoadVerifyActivity.this, MyCalendarActivity.class);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(LoadVerifyActivity.this, MyCalendarActivity.class);
                startActivity(intent);
            }

        }
    }
    void calculateStock() {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_DELIVERY_NO, "");
        map.put(db.KEY_ITEM_NO, "");
        map.put(db.KEY_ITEM_CATEGORY, "");
        map.put(db.KEY_CREATED_BY, "");
        map.put(db.KEY_ENTRY_TIME, "");
        map.put(db.KEY_DATE, "");
        map.put(db.KEY_MATERIAL_NO, "");
        map.put(db.KEY_MATERIAL_ENTERED, "");
        map.put(db.KEY_MATERIAL_GROUP, "");
        map.put(db.KEY_PLANT, "");
        map.put(db.KEY_STORAGE_LOCATION, "");
        map.put(db.KEY_BATCH, "");
        map.put(db.KEY_ACTUAL_QTY, "");
        map.put(db.KEY_REMAINING_QTY, "");
        map.put(db.KEY_UOM, "");
        map.put(db.KEY_DIST_CHANNEL, "");
        map.put(db.KEY_DIVISION, "");
        map.put(db.KEY_IS_DELIVERED, "");
        HashMap<String, String> filter = new HashMap<>();
        Cursor deliveryCursor = db.getData(db.CUSTOMER_DELIVERY_ITEMS, map, filter);
        if (deliveryCursor.getCount() > 0) {
            deliveryCursor.moveToFirst();
            do {
                HashMap<String, String> mapVanStock = new HashMap<>();
                mapVanStock.put(db.KEY_DELIVERY_NO, "");
                mapVanStock.put(db.KEY_ITEM_NO, "");
                mapVanStock.put(db.KEY_ITEM_CATEGORY, "");
                mapVanStock.put(db.KEY_CREATED_BY, "");
                mapVanStock.put(db.KEY_ENTRY_TIME, "");
                mapVanStock.put(db.KEY_DATE, "");
                mapVanStock.put(db.KEY_MATERIAL_NO, "");
                mapVanStock.put(db.KEY_MATERIAL_DESC1, "");
                mapVanStock.put(db.KEY_MATERIAL_ENTERED, "");
                mapVanStock.put(db.KEY_MATERIAL_GROUP, "");
                mapVanStock.put(db.KEY_PLANT, "");
                mapVanStock.put(db.KEY_STORAGE_LOCATION, "");
                mapVanStock.put(db.KEY_BATCH, "");
                mapVanStock.put(db.KEY_ACTUAL_QTY_CASE, "");
                mapVanStock.put(db.KEY_ACTUAL_QTY_UNIT, "");
                mapVanStock.put(db.KEY_RESERVED_QTY_CASE, "");
                mapVanStock.put(db.KEY_RESERVED_QTY_UNIT, "");
                mapVanStock.put(db.KEY_REMAINING_QTY_CASE, "");
                mapVanStock.put(db.KEY_REMAINING_QTY_UNIT, "");
                mapVanStock.put(db.KEY_UOM_CASE, "");
                mapVanStock.put(db.KEY_UOM_UNIT, "");
                mapVanStock.put(db.KEY_DIST_CHANNEL, "");
                mapVanStock.put(db.KEY_DIVISION, "");
                mapVanStock.put(db.KEY_IS_VERIFIED, "");
                HashMap<String, String> filterVanStock = new HashMap<>();
                Cursor vanStockCursor = db.getData(db.VAN_STOCK_ITEMS, mapVanStock, filterVanStock);
                if (vanStockCursor.getCount() > 0) {
                    vanStockCursor.moveToFirst();
                    do {
                        if (vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_MATERIAL_NO)).equals(deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_MATERIAL_NO)))) {
                            if (deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM) || deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM_NEW)||deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)) {
                                String deliveryCase = deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_ACTUAL_QTY));
                                String actualCase = vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_ACTUAL_QTY_CASE));
                                String reservedCase = vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_RESERVED_QTY_CASE));
                                String remainingCase = vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_REMAINING_QTY_CASE));
                                HashMap<String, String> updateMap = new HashMap<>();
                                updateMap.put(db.KEY_ACTUAL_QTY_CASE, actualCase);
                                updateMap.put(db.KEY_RESERVED_QTY_CASE, String.valueOf(Float.parseFloat(reservedCase == null ? "0" : reservedCase) + Float.parseFloat(deliveryCase == null ? "0" : deliveryCase)));
                                updateMap.put(db.KEY_REMAINING_QTY_CASE, String.valueOf(Float.parseFloat(remainingCase == null ? "0" : remainingCase) - Float.parseFloat(deliveryCase == null ? "0" : deliveryCase)));
                                HashMap<String, String> updateFilter = new HashMap<>();
                                updateFilter.put(db.KEY_MATERIAL_NO, vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                //Log.e("Update Map C/S","" + updateMap);
                                db.updateData(db.VAN_STOCK_ITEMS, updateMap, updateFilter);
                            } else {
                                String deliveryUnits = deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_ACTUAL_QTY));
                                String actualUnits = vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_ACTUAL_QTY_UNIT));
                                String reservedUnits = vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_RESERVED_QTY_UNIT));
                                String remainingUnits = vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_REMAINING_QTY_UNIT));
                                HashMap<String, String> updateMap = new HashMap<>();
                                updateMap.put(db.KEY_ACTUAL_QTY_UNIT, actualUnits);
                                updateMap.put(db.KEY_RESERVED_QTY_UNIT, String.valueOf(Float.parseFloat(reservedUnits == null ? "0" : reservedUnits) + Float.parseFloat(deliveryUnits == null ? "0" : deliveryUnits)));
                                updateMap.put(db.KEY_REMAINING_QTY_UNIT, String.valueOf(Float.parseFloat(remainingUnits == null ? "0" : remainingUnits) - Float.parseFloat(deliveryUnits == null ? "0" : deliveryUnits)));
                                //Log.e("Update Map B/T", "" + updateMap);
                                HashMap<String, String> updateFilter = new HashMap<>();
                                updateFilter.put(db.KEY_MATERIAL_NO, vanStockCursor.getString(vanStockCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                                db.updateData(db.VAN_STOCK_ITEMS, updateMap, updateFilter);
                            }
                            break;
                        }
                    }
                    while (vanStockCursor.moveToNext());
                }
            }
            while (deliveryCursor.moveToNext());
        }
    }
}
