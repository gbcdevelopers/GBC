package gbc.sa.vansales.activities;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ExpenseAdapter;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.ChequeCollection;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.Expense;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.AnimatedExpandableListView;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by eheuristic on 12/10/2016.
 */
/************************************************************
 @ This activity is launched when user clicks on end trip from
 @ the slide menu.
 ************************************************************/
public class EndTripActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    FloatingActionButton btn_float;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    ArrayList<Expense> arrayList = new ArrayList<>();
    ArrayList<ChequeCollection> chequeList = new ArrayList<>();
    ArrayList<ChequeCollection> driverchequeList = new ArrayList<>();
    ArrayAdapter<Expense>adapter;
    float chequeTotal = 0;
    float cashTotal = 0;
    float driverchequeTotal = 0;
    float drivercashTotal = 0;
    TextView tv_cheque_amnt;
    TextView tv_cash_amnt;
    TextView tv_total_amount;
    TextView tv_driver_cheque_amnt;
    TextView tv_driver_cash_amnt;
    TextView tv_driver_total_amount;
    String chequeAmount;
    String chequeNumber;
    String customerNo;
    String bankCodes;
    TextView tv_due_amount;
    LinearLayout ll_add_expense;
    ListView expenseListView;
    Typeface typeface;
    ArrayList<CustomerHeader> customers;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_trip);
        customers = CustomerHeaders.get();
        try{
            loadingSpinner = new LoadingSpinner(this);
            iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
            tv_top_header = (TextView) findViewById(R.id.tv_top_header);
            btn_float = (FloatingActionButton) findViewById(R.id.btn_float);
            ll_add_expense = (LinearLayout)findViewById(R.id.add_expense);
            expenseListView = (ListView)findViewById(R.id.expenseListView);
            tv_cheque_amnt = (TextView)findViewById(R.id.tv_cheque_amnt);
            tv_cash_amnt = (TextView)findViewById(R.id.tv_cash_amnt);
            tv_total_amount = (TextView)findViewById(R.id.tv_total_amount);
            tv_driver_cheque_amnt = (TextView)findViewById(R.id.tv_driver_cheque_amnt);
            tv_driver_cash_amnt = (TextView)findViewById(R.id.tv_driver_cash_amnt);
            tv_driver_total_amount = (TextView)findViewById(R.id.tv_driver_total_amount);
            tv_due_amount = (TextView)findViewById(R.id.tv_due_amount);
            adapter = new ExpenseAdapter(this,arrayList);
            expenseListView.setAdapter(adapter);
            btn_float.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String purchaseNumber = Helpers.generateNumber(db, ConfigStore.EndDay_PR_Type);
                    HashMap<String, String> map = new HashMap<>();
                    String timeStamp = Helpers.getCurrentTimeStamp();
                    map.put(db.KEY_TIME_STAMP, timeStamp);
                    map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    map.put(db.KEY_FUNCTION, ConfigStore.EndDayFunction);
                    map.put(db.KEY_PURCHASE_NUMBER, purchaseNumber);
                    map.put(db.KEY_DATE, new SimpleDateFormat("yyyy.MM.dd").format(new Date()));
                    map.put(db.KEY_IS_SELECTED, "true");
                    map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                    db.addData(db.BEGIN_DAY, map);
                    new postTrip(purchaseNumber, timeStamp);

                }
            });
            iv_back.setVisibility(View.VISIBLE);
            tv_top_header.setVisibility(View.VISIBLE);
            tv_top_header.setText(getString(R.string.endtrip));
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            ll_add_expense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showDialog();
                }
            });
            new loadCollectionData().execute();
            calculateDueAmount();
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }

    /************************************************************
     @ Fetching all the data captured by the driver from the
     @ driver collection screen
     ************************************************************/
    public class loadDriverCollectionData extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String>map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO,"");
            map.put(db.KEY_INVOICE_NO,"");
            map.put(db.KEY_INVOICE_AMOUNT,"");
            map.put(db.KEY_DUE_DATE,"");
            map.put(db.KEY_INVOICE_DATE,"");
            map.put(db.KEY_AMOUNT_CLEARED,"");
            map.put(db.KEY_CASH_AMOUNT,"");
            map.put(db.KEY_CHEQUE_AMOUNT,"");
            map.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,"");
            map.put(db.KEY_CHEQUE_NUMBER,"");
            map.put(db.KEY_CHEQUE_BANK_CODE,"");
            map.put(db.KEY_IS_INVOICE_COMPLETE,"");
            HashMap<String,String>filter = new HashMap<>();
            Cursor c = db.getData(db.DRIVER_COLLECTION,map,filter);
            if(c.getCount()>0){
                c.moveToFirst();
                setDriverCollection(c);
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            startCountAnimation(tv_driver_cheque_amnt, (int) driverchequeTotal);
            startCountAnimation(tv_driver_cash_amnt, (int) drivercashTotal);
            startCountAnimation(tv_driver_total_amount, (int) (driverchequeTotal+drivercashTotal));

        }
    }
    /************************************************************
     @ Fetching all the data captured for the customer from the
     @ customer collection screen
     ************************************************************/
    public class loadCollectionData extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String>map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO,"");
            map.put(db.KEY_INVOICE_NO,"");
            map.put(db.KEY_INVOICE_AMOUNT,"");
            map.put(db.KEY_DUE_DATE,"");
            map.put(db.KEY_INVOICE_DATE,"");
            map.put(db.KEY_AMOUNT_CLEARED,"");
            map.put(db.KEY_CASH_AMOUNT,"");
            map.put(db.KEY_CHEQUE_AMOUNT,"");
            map.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,"");
            map.put(db.KEY_CHEQUE_NUMBER,"");
            map.put(db.KEY_CHEQUE_BANK_CODE,"");
            map.put(db.KEY_IS_INVOICE_COMPLETE,"");
            HashMap<String,String>filter = new HashMap<>();
            Cursor c = db.getData(db.COLLECTION,map,filter);
            if(c.getCount()>0){
                c.moveToFirst();
                setCollection(c);
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            startCountAnimation(tv_cheque_amnt, (int) chequeTotal);
            startCountAnimation(tv_cash_amnt, (int) cashTotal);
            startCountAnimation(tv_total_amount, (int) (chequeTotal+cashTotal));
            new loadDriverCollectionData().execute();

        }
    }
    private void setCollection(Cursor cursor){
        try{
            Cursor c = cursor;
            do {
                chequeTotal+=Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT)));
                cashTotal+=Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CASH_AMOUNT)));
                ChequeCollection chequeCollection = new ChequeCollection();
                String[]cheques = UrlBuilder.decodeString(c.getString(c.getColumnIndex(db.KEY_CHEQUE_NUMBER))).split(",");
                String[]bankCode = UrlBuilder.decodeString(c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_CODE))).split(",");
                String[]chequeAmount = UrlBuilder.decodeString(c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL))).split(",");

                if(cheques.length>1){
                    chequeCollection.setChequeNo(cheques[1]);
                    chequeCollection.setBankCode(bankCode[1]);
                    chequeCollection.setCustomerNo(c.getString(c.getColumnIndex(db.KEY_CUSTOMER_NO)));
                    chequeCollection.setChequeAmount(c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT)));
                    chequeList.add(chequeCollection);
                }
            }
            while(c.moveToNext());
            Helpers.logData(EndTripActivity.this, "Cheque Total" + chequeTotal + "Cash Total" + cashTotal);
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    private void setDriverCollection(Cursor cursor){
        try{
            Cursor c = cursor;
            do {
                driverchequeTotal+=Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT)));
                drivercashTotal+=Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CASH_AMOUNT)));
                ChequeCollection chequeCollection = new ChequeCollection();
                String[]cheques = UrlBuilder.decodeString(c.getString(c.getColumnIndex(db.KEY_CHEQUE_NUMBER))).replaceAll("2C",",").split(",");
                String[]bankCode = UrlBuilder.decodeString(c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_CODE))).replaceAll("2C", ",").split(",");
                String[]chequeAmount = UrlBuilder.decodeString(c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL))).replaceAll("2C",",").split(",");

                if(cheques.length>1&&cheques.length<2){
                    chequeCollection.setChequeNo(cheques[1]);
                    chequeCollection.setBankCode(bankCode[1]);
                    chequeCollection.setCustomerNo(Settings.getString(App.DRIVER));
                    chequeCollection.setChequeAmount(c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT)));
                    driverchequeList.add(chequeCollection);
                }
                else if(cheques.length>2){
                    for(int i=0;i<cheques.length;i++){
                        ChequeCollection cQ = new ChequeCollection();
                        cQ.setChequeNo(cheques[i]);
                        cQ.setBankCode(bankCode[i]);
                        cQ.setCustomerNo(Settings.getString(App.DRIVER));
                        cQ.setChequeAmount(chequeAmount[i]);
                        if(!cheques[i].equals("0000")){
                            driverchequeList.add(cQ);
                        }

                    }
                }
            }
            while(c.moveToNext());
            Helpers.logData(EndTripActivity.this, "Driver Cheque Total" + driverchequeTotal + "Driver Cash Total" + drivercashTotal);
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    /************************************************************
     @ Adding animation to the counter
     ************************************************************/
    private void startCountAnimation(final TextView element,Integer value) {

        final Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/counterFont.ttf");
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, value);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                element.setText("" + (int) animation.getAnimatedValue());
                //element.setTypeface(custom_font);
            }
        });
        animator.start();
    }
    private void showDialog(){
        final Dialog dialog = new Dialog(EndTripActivity.this);
        View view = getLayoutInflater().inflate(R.layout.activity_add_expense, null);
        Button cancel = (Button)view.findViewById(R.id.btn_cancel);
        Button save = (Button)view.findViewById(R.id.btn_ok);
        final EditText expAmount = (EditText)view.findViewById(R.id.expAmount);
        final EditText reasons = (EditText)view.findViewById(R.id.reason);
        final String[] reasonSelected = {""};

        Spinner expenseSpinner = (Spinner)view.findViewById(R.id.expenseCategory);
        final List<String> list = new ArrayList<String>();
        list.add("Select Expense");
        list.add("Fuel");
        list.add("Food");
        list.add("Theft");
        list.add("Traffic Fine");
        list.add("Other");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseSpinner.setAdapter(dataAdapter);

        expenseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reasonSelected[0] = list.get(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Expense expense = new Expense();
                expense.setExpenseCode("00");
                expense.setExpenseDescription(reasonSelected[0].toString());
                expense.setAdditionalReason(reasons.getText().toString());
                expense.setExpenseAmount(expAmount.getText().toString());
                arrayList.add(expense);
                adapter.notifyDataSetChanged();
                calculateDueAmount();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void calculateDueAmount(){
        float dueAmount = 0;
        for(Expense expense:arrayList){
            dueAmount+= Float.parseFloat(expense.getExpenseAmount());
        }
        tv_due_amount.setText(String.valueOf(dueAmount));
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
            this.purchaseNumber = purchaseNumber;
            this.timeStamp = timeStamp;
            this.tokens = Helpers.parseTimeStamp(this.timeStamp);
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Function", ConfigStore.EndDayFunction);
            map.put("TripId", Settings.getString(App.TRIP_ID));
            map.put("CreatedBy", Settings.getString(App.DRIVER));
            map.put("EndDate", tokens[0].toString());
            map.put("EndTime", tokens[1].toString());
            JSONArray deepEntity = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            deepEntity.put(jsonObject);
            this.orderID = IntegrationService.postTrip(EndTripActivity.this, App.POST_COLLECTION, map, deepEntity, purchaseNumber);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            try{
                if (loadingSpinner.isShowing()) {
                    loadingSpinner.hide();
                }
                if (!this.orderID.contains("Error") && !this.orderID.equals("")) {
                    if (this.orderID.equals(this.purchaseNumber)) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                        map.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_FUNCTION, ConfigStore.EndDayFunction);
                        filter.put(db.KEY_PURCHASE_NUMBER, this.purchaseNumber);
                        db.updateData(db.BEGIN_DAY, map, filter);
                    } else {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                        map.put(db.KEY_IS_POSTED, App.DATA_IS_POSTED);
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_FUNCTION, ConfigStore.EndDayFunction);
                        filter.put(db.KEY_PURCHASE_NUMBER, this.purchaseNumber);
                        db.updateData(db.BEGIN_DAY, map, filter);
                    }
                    if(cashTotal>0){
                        new postEndTrip("CASH");
                    }
                    if(drivercashTotal>0){
                        new postEndTrip("DCASH");
                    }
                    else if(chequeTotal>0){
                        new postEndTrip("CHEQUE");
                    }
                    else if(driverchequeTotal>0){
                        new postEndTrip("DCHEQUE");
                    }
                    else{
                        Intent intent = new Intent(EndTripActivity.this, PrinterReportsActivity.class);
                        startActivity(intent);
                    }

                /**/

                } else if (this.orderID.contains("Error")) {
                    Toast.makeText(EndTripActivity.this, this.orderID.replaceAll("Error", "").trim(), Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EndTripActivity.this);
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
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
            }

        }
    }
    public class postEndTrip extends AsyncTask<Void,Void,Void>{
        private String source;
        private String orderId;
        private String orderIdDriver;

        private postEndTrip(String source){
            this.source = source;
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try{
                if(source.equals("CASH")){
                    HashMap<String,String>map = new HashMap<>();
                    map.put("OrderValue",String.valueOf(cashTotal));
                    map.put("VisitID",source);
                    map.put("Function",ConfigStore.ClearingFunction);
                    map.put("CustomerId",Settings.getString(App.DRIVER));

                    JSONArray deepEntity = new JSONArray();
                    JSONObject obj = new JSONObject();
                    deepEntity.put(obj);
                    this.orderId = IntegrationService.postDataBackup(EndTripActivity.this,App.POST_COLLECTION,map,deepEntity);
                    Log.e("Order ID","" + orderId);
                }
                else if(source.equals("DCASH")){

                    HashMap<String,String>driverMap = new HashMap<>();
                    driverMap.put("OrderValue",String.valueOf(drivercashTotal));
                    driverMap.put("VisitID",source);
                    driverMap.put("Function",ConfigStore.ClearingFunction);
                    driverMap.put("CustomerId",Settings.getString(App.DRIVER));

                    JSONArray deepEntity = new JSONArray();
                    JSONObject obj = new JSONObject();
                    deepEntity.put(obj);
                    //this.orderId = IntegrationService.postDataBackup(EndTripActivity.this,App.POST_COLLECTION,map,deepEntity);
                    this.orderIdDriver = IntegrationService.postDataBackup(EndTripActivity.this,App.POST_COLLECTION,driverMap,deepEntity);
                    Log.e("Order ID Driver","" + orderId);
                }
                else if(source.equals("CHEQ")){
                    try{
                        HashMap<String,String>map = new HashMap<>();
                        map.put("OrderValue",String.valueOf(chequeTotal));
                        map.put("VisitID",source);
                        map.put("Function",ConfigStore.ClearingFunction);
                        map.put("CustomerId",Settings.getString(App.DRIVER));
                        JSONArray deepEntity = new JSONArray();
                        for(ChequeCollection chequeCollection:chequeList){
                            JSONObject obj = new JSONObject();
                            obj.put("OrderId",chequeCollection.getChequeNo().toString());
                            obj.put("Material", chequeCollection.getCustomerNo().toString());
                            CustomerHeader customerHeader = CustomerHeader.getCustomer(customers,chequeCollection.getCustomerNo());
                            if(customerHeader!=null){
                                obj.put("Description",UrlBuilder.decodeString(customerHeader.getName1()));
                            }
                            obj.put("Value",chequeCollection.getChequeAmount().toString());
                            obj.put("Route",chequeCollection.getBankCode().toString());
                            deepEntity.put(obj);
                        }
                        this.orderId = IntegrationService.postDataBackup(EndTripActivity.this,App.POST_COLLECTION,map,deepEntity);
                        Log.e("Order ID","" + orderId);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }

                }
                else if(source.equals("DCHEQ")){
                    try{

                        HashMap<String,String>driverMap = new HashMap<>();
                        driverMap.put("OrderValue",String.valueOf(driverchequeTotal));
                        driverMap.put("VisitID",source);
                        driverMap.put("Function",ConfigStore.ClearingFunction);
                        driverMap.put("CustomerId",Settings.getString(App.DRIVER));
                        JSONArray deepEntityDriver = new JSONArray();
                        for(ChequeCollection chequeCollection:driverchequeList){
                            JSONObject obj = new JSONObject();
                            obj.put("OrderId",chequeCollection.getChequeNo().toString());
                            obj.put("Material", chequeCollection.getCustomerNo().toString());
                            CustomerHeader customerHeader = CustomerHeader.getCustomer(customers,chequeCollection.getCustomerNo());
                            if(customerHeader!=null){
                                obj.put("Description",UrlBuilder.decodeString(customerHeader.getName1()));
                            }
                            obj.put("Value",chequeCollection.getChequeAmount().toString());
                            obj.put("Route",chequeCollection.getBankCode().toString());
                            deepEntityDriver.put(obj);
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }

                }
            }
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }

            if(source.equals("CASH")){
                if(chequeTotal>0){
                    new postEndTrip("CHEQ");
                }
                else{
                    HashMap<String, String> altMap = new HashMap<>();
                    altMap.put(db.KEY_IS_END_DAY, "true");
                    HashMap<String, String> filterMap = new HashMap<>();
                    filterMap.put(db.KEY_IS_END_DAY, "false");
                    db.updateData(db.LOCK_FLAGS, altMap, filterMap);

                    Intent intent = new Intent(EndTripActivity.this, PrinterReportsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
            else{

                HashMap<String, String> altMap = new HashMap<>();
                altMap.put(db.KEY_IS_END_DAY, "true");
                HashMap<String, String> filterMap = new HashMap<>();
                filterMap.put(db.KEY_IS_END_DAY, "false");
                db.updateData(db.LOCK_FLAGS, altMap, filterMap);

                Intent intent = new Intent(EndTripActivity.this, PrinterReportsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }
}
