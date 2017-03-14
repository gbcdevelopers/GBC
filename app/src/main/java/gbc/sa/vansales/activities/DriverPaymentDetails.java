package gbc.sa.vansales.activities;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Set;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.BankAdapter;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.Banks;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.Bank;
import gbc.sa.vansales.models.Collection;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.DeliveryItem;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.printer.JsonRpcUtil;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.PrinterHelper;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/************************************************************
 @ This activity is called when driver selects a line item from
 @ the driver collections screen
 ************************************************************/
public class DriverPaymentDetails extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    TextView tv_due_amt, tv_total_amount, tv_date;
    ImageView iv_cal;
    EditText edt_check_no, edt_check_amt, edt_cash_amt;
    Spinner sp_item;
    Button btn_edit1, btn_edit2;
    Calendar myCalendar;
    String invoiceNo;
    DatePickerDialog.OnDateSetListener date;
    FloatingActionButton fab;
    double total_amt = 0.00;
    int pos = 0;
    String from = "";
    String amountdue = "0";
    Customer object;
    OrderList delivery;
    Collection collection;
    String invoiceAmount;
    ArrayList<CustomerHeader> customers;
    ArrayList<ArticleHeader> articles;
    private ArrayList<Bank> banksList = new ArrayList<>();
    ArrayAdapter<Bank> bankAdapter;
    String bankcode;
    String bankname;
    boolean allowPartial = false;
    CheckBox cb_cashPayment;
    CheckBox cb_chequePayment;
    LinearLayout ll_cash_payment;
    LinearLayout ll_cheque_payment;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    ArrayList<DeliveryItem> arrayList = new ArrayList<>();
    String customerNo;
    String customerName;
    String customerAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details_new);
        loadingSpinner = new LoadingSpinner(this);
        Banks.loadData(this);
        tv_due_amt = (TextView) findViewById(R.id.tv_payment__amout_due_number);
        Intent i = this.getIntent();
        cb_cashPayment = (CheckBox) findViewById(R.id.cb_cash);
        cb_chequePayment = (CheckBox) findViewById(R.id.cb_cheque);
        ll_cash_payment = (LinearLayout) findViewById(R.id.ll_cash_payment);
        ll_cheque_payment = (LinearLayout) findViewById(R.id.ll_cheque_payment);
        sp_item = (Spinner) findViewById(R.id.sp_item);
        sp_item.setEnabled(true);
        //iv_cal.setEnabled(true);
        cb_chequePayment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_chequePayment.isChecked()) {
                    ll_cheque_payment.setVisibility(View.VISIBLE);
                } else {
                    ll_cheque_payment.setVisibility(View.GONE);
                }
            }
        });
        cb_cashPayment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_cashPayment.isChecked()) {
                    ll_cash_payment.setVisibility(View.VISIBLE);
                } else {
                    ll_cash_payment.setVisibility(View.GONE);
                }
            }
        });
        loadBanks();
        //object = (Customer) i.getParcelableExtra("headerObj");
        if (getIntent().getExtras() != null) {
            from = getIntent().getStringExtra("from");
            if (from.equals("drivercollection")) {
                pos = getIntent().getIntExtra("pos", 0);
                amountdue = getIntent().getStringExtra("amountdue");
                collection = (Collection) i.getParcelableExtra("drivercollection");
                invoiceNo = collection.getInvoiceNo();
                tv_due_amt.setText(amountdue);
                TextView tv_cust_detail = (TextView) findViewById(R.id.tv_cust_detail);
                tv_cust_detail.setText(Settings.getString(App.DRIVER) + " " + Settings.getString(App.DRIVER_NAME_EN));
                allowPartial = true;
            }
        }
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.payment_details));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equals("drivercollection")) {
                    finish();
                }
            }
        });
        tv_total_amount = (TextView) findViewById(R.id.tv_total_amt);
        tv_date = (TextView) findViewById(R.id.tv_date);
        iv_cal = (ImageView) findViewById(R.id.image_cal);
        iv_cal.setEnabled(true);
        edt_check_no = (EditText) findViewById(R.id.edt_check_no);
        edt_cash_amt = (EditText) findViewById(R.id.edt_cash_amount);
        edt_check_amt = (EditText) findViewById(R.id.edt_check_amt);
        double cashamt = getcashamt();
        double checkamt = getcheckamt();
        total_amt = cashamt + checkamt;
        tv_total_amount.setText(String.valueOf(total_amt));
        myCalendar = Calendar.getInstance();
        setDefaultDate();
        iv_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DriverPaymentDetails.this, date, myCalendar
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
                if (s.toString().startsWith("-") && s.toString().equals("-")) {
                } else {
                    setTotalText();
                }
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
                if (s.toString().startsWith("-") && s.toString().equals("-")) {
                } else {
                    setTotalText();
                }
            }
        });
        sp_item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bankcode = banksList.get(position).getBankCode();
                bankname = banksList.get(position).getBankName();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /************************************************************
                 @ Adding data in driver collection table
                 ************************************************************/
                try{
                    double cash_amt = getcashamt();
                    double check_amt = getcheckamt();
                    total_amt = cash_amt + check_amt;
                    if (allowPartial) {
                        if (from.equals("drivercollection")) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(db.KEY_AMOUNT_CLEARED, "");
                            map.put(db.KEY_CASH_AMOUNT, "");
                            map.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL, "");
                            map.put(db.KEY_CHEQUE_AMOUNT, "");
                            map.put(db.KEY_CHEQUE_NUMBER, "");
                            map.put(db.KEY_CHEQUE_DATE, "");
                            map.put(db.KEY_CHEQUE_BANK_CODE, "");
                            map.put(db.KEY_CHEQUE_BANK_NAME, "");
                            HashMap<String, String> filter = new HashMap<String, String>();
                            filter.put(db.KEY_SAP_INVOICE_NO, collection.getInvoiceNo());
                            filter.put(db.KEY_CUSTOMER_NO, Settings.getString(App.DRIVER));
                            Cursor c = db.getData(db.DRIVER_COLLECTION, map, filter);
                            float prevAmount = 0;
                            float prevCashAmount = 0;
                            float prevCheqAmount = 0;
                            String chequeNumber = "";
                            String chequeDate = "";
                            String bankName = "";
                            String bankCode = "";
                            String chequeAmount = "";
                            if (c.getCount() > 0) {
                                c.moveToFirst();
                                prevAmount = Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                                prevCashAmount = Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CASH_AMOUNT)));
                                prevCheqAmount = Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT)));
                                chequeNumber = c.getString(c.getColumnIndex(db.KEY_CHEQUE_NUMBER));
                                chequeDate = c.getString(c.getColumnIndex(db.KEY_CHEQUE_DATE));
                                bankName = c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_NAME));
                                bankCode = c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_CODE));
                                chequeAmount = c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL));
                            }
                            prevAmount += Float.parseFloat(tv_total_amount.getText().toString());
                            prevCashAmount += getcashamt();
                            prevCheqAmount += getcheckamt();
                            if (getcheckamt() > 0) {
                                chequeNumber = chequeNumber + "," + edt_check_no.getText().toString();
                                chequeAmount = chequeAmount + "," + String.valueOf(getcheckamt());
                                chequeDate = chequeDate + "," + tv_date.getText().toString();
                                bankCode = bankCode + "," + bankcode;
                                bankName = bankName + "," + bankname;
                            }
                            HashMap<String, String> updateMap = new HashMap<String, String>();
                            updateMap.put(db.KEY_AMOUNT_CLEARED, String.valueOf(prevAmount));
                            updateMap.put(db.KEY_CHEQUE_NUMBER, chequeNumber);
                            updateMap.put(db.KEY_CHEQUE_DATE, chequeDate);
                            updateMap.put(db.KEY_CASH_AMOUNT, String.valueOf(prevCashAmount));
                            updateMap.put(db.KEY_CHEQUE_AMOUNT, String.valueOf(prevCheqAmount));
                            updateMap.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL, chequeAmount);
                            updateMap.put(db.KEY_CHEQUE_BANK_CODE, bankCode);
                            updateMap.put(db.KEY_CHEQUE_BANK_NAME, bankName);
                            if (Float.parseFloat(tv_total_amount.getText().toString()) == Float.parseFloat(amountdue)) {
                                updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_COMPLETE);
                                updateMap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                            } else {
                                updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_PARTIAL);
                                updateMap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                            }
                            db.updateData(db.DRIVER_COLLECTION, updateMap, filter);

                            final Dialog dialog = new Dialog(DriverPaymentDetails.this);
                            dialog.setContentView(R.layout.dialog_doprint);
                            dialog.setCancelable(false);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                            LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                            dialog.show();
                            btn_print.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    try {
                                        JSONArray jsonArray = createPrintData("COLLECTION", invoiceNo,"");
                                        JSONObject data = new JSONObject();
                                        data.put("data", (JSONArray) jsonArray);
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put(db.KEY_CUSTOMER_NO, Settings.getString(App.DRIVER));
                                        map.put(db.KEY_ORDER_ID, invoiceNo);
                                        map.put(db.KEY_DOC_TYPE, ConfigStore.DriverCollectionRequest_TR);
                                        map.put(db.KEY_DATA, data.toString());
                                        //map.put(db.KEY_DATA,jsonArray.toString());
                                        db.addDataPrint(db.DELAY_PRINT, map);
                                        PrinterHelper object = new PrinterHelper(DriverPaymentDetails.this, DriverPaymentDetails.this);
                                        object.execute("", jsonArray);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //finish();
                                }
                            });
                            btn_notprint.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    try {
                                        JSONArray jsonArray = createPrintData("COLLECTION", "", invoiceNo);
                                        JSONObject data = new JSONObject();
                                        data.put("data", (JSONArray) jsonArray);
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put(db.KEY_CUSTOMER_NO, Settings.getString(App.DRIVER));
                                        map.put(db.KEY_ORDER_ID, invoiceNo);
                                        map.put(db.KEY_DOC_TYPE, ConfigStore.DriverCollectionRequest_TR);
                                        map.put(db.KEY_DATA, data.toString());
                                        //map.put(db.KEY_DATA,jsonArray.toString());
                                        db.addDataPrint(db.DELAY_PRINT, map);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Intent intent = new Intent(DriverPaymentDetails.this, DriverCollectionsActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                        else {
                            final Dialog dialog = new Dialog(DriverPaymentDetails.this);
                            dialog.setContentView(R.layout.dialog_doprint);
                            dialog.setCancelable(false);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                            LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                            dialog.show();
                            btn_print.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                            btn_notprint.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                        }
                    } else {
                        if (total_amt > Double.parseDouble(amountdue) || total_amt < Double.parseDouble(amountdue)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DriverPaymentDetails.this);
                            builder.setTitle("Payment Detail");
                            builder.setCancelable(true);
                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setMessage(getString(R.string.amount_larger));
                            builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        } else {
                            if (from.equals("drivercollection")) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_AMOUNT_CLEARED, "");
                                map.put(db.KEY_CASH_AMOUNT, "");
                                map.put(db.KEY_CHEQUE_AMOUNT, "");
                                map.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL, "");
                                map.put(db.KEY_CHEQUE_NUMBER, "");
                                map.put(db.KEY_CHEQUE_DATE, "");
                                map.put(db.KEY_CHEQUE_BANK_CODE, "");
                                map.put(db.KEY_CHEQUE_BANK_NAME, "");
                                HashMap<String, String> filter = new HashMap<String, String>();
                                filter.put(db.KEY_SAP_INVOICE_NO, collection.getInvoiceNo());
                                filter.put(db.KEY_CUSTOMER_NO, Settings.getString(App.DRIVER));
                                Cursor c = db.getData(db.DRIVER_COLLECTION, map, filter);
                                float prevAmount = 0;
                                float prevCashAmount = 0;
                                float prevCheqAmount = 0;
                                String chequeNumber = "";
                                String chequeDate = "";
                                String bankName = "";
                                String bankCode = "";
                                String chequeAmount = "";
                                if (c.getCount() > 0) {
                                    c.moveToFirst();
                                    prevAmount = Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                                    prevCashAmount = Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CASH_AMOUNT)));
                                    prevCheqAmount = Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT)));
                                    chequeNumber = c.getString(c.getColumnIndex(db.KEY_CHEQUE_NUMBER));
                                    chequeDate = c.getString(c.getColumnIndex(db.KEY_CHEQUE_DATE));
                                    bankName = c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_NAME));
                                    bankCode = c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_CODE));
                                    chequeAmount = c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL));
                                }
                                prevAmount += Float.parseFloat(tv_total_amount.getText().toString());
                                prevCashAmount += getcashamt();
                                prevCheqAmount += getcheckamt();
                                if (getcheckamt() > 0) {
                                    chequeNumber = chequeNumber + "," + edt_check_no.getText().toString();
                                    chequeDate = chequeDate + "," + tv_date.getText().toString();
                                    chequeAmount = chequeAmount + "," + String.valueOf(getcheckamt());
                                    bankCode = bankCode + "," + bankcode;
                                    bankName = bankName + "," + bankname;
                                }
                                HashMap<String, String> updateMap = new HashMap<String, String>();
                                updateMap.put(db.KEY_AMOUNT_CLEARED, String.valueOf(prevAmount));
                                updateMap.put(db.KEY_CHEQUE_NUMBER, chequeNumber);
                                updateMap.put(db.KEY_CASH_AMOUNT, String.valueOf(prevCashAmount));
                                updateMap.put(db.KEY_CHEQUE_AMOUNT, String.valueOf(prevCheqAmount));
                                updateMap.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL, chequeAmount);
                                updateMap.put(db.KEY_CHEQUE_DATE, chequeDate);
                                updateMap.put(db.KEY_CHEQUE_BANK_CODE, bankCode);
                                updateMap.put(db.KEY_CHEQUE_BANK_NAME, bankName);
                                if (Float.parseFloat(tv_total_amount.getText().toString()) == Float.parseFloat(amountdue)) {
                                    updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_COMPLETE);
                                } else {
                                    updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_PARTIAL);
                                }
                                updateMap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                                db.updateData(db.DRIVER_COLLECTION, updateMap, filter);
                                Intent intent1 = new Intent(DriverPaymentDetails.this, CollectionsActivity.class);
                                intent1.putExtra("headerObj", object);
                                startActivity(intent1);
                            } else {
                                final Dialog dialog = new Dialog(DriverPaymentDetails.this);
                                dialog.setContentView(R.layout.dialog_doprint);
                                dialog.setCancelable(false);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                                LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                                dialog.show();
                                btn_print.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                                btn_notprint.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                            }
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

            }
        });
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(DriverPaymentDetails.this, "Please complete the transaction", Toast.LENGTH_SHORT).show();
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        tv_date.setText(sdf.format(myCalendar.getTime()));
    }
    private void setDefaultDate() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        tv_date.setText(sdf.format(myCalendar.getTime()));
    }
    /************************************************************
     @ Returns cash amount entered by the user
     ************************************************************/
    public double getcashamt() {
        if (edt_cash_amt.getText().toString().equals("")) {
            return 0;
        }
        return Double.parseDouble(edt_cash_amt.getText().toString());
    }
    /************************************************************
     @ Returns cheque amount entered by the user
     ************************************************************/
    public double getcheckamt() {
        if (edt_check_amt.getText().toString().equals("")) {
            return 0;
        }
        return Double.parseDouble(edt_check_amt.getText().toString());
    }
    private void loadBanks() {
        Banks.loadData(DriverPaymentDetails.this);
        banksList = Banks.get();
        //Log.e("Bank List", "" + banksList.size());
        bankAdapter = new BankAdapter(this, android.R.layout.simple_spinner_item, banksList);
        bankAdapter.notifyDataSetChanged();
        sp_item.setAdapter(bankAdapter);
    }
    public void setTotalText() {
        double cash_amt = getcashamt();
        double check_amt = getcheckamt();
        total_amt = cash_amt + check_amt;
        if (total_amt > Double.parseDouble(amountdue)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DriverPaymentDetails.this);
            builder.setTitle("Payment Detail");
            builder.setCancelable(true);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("Amount should not be greater than actual amount");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            tv_total_amount.setText(String.valueOf(total_amt));
        }
    }
    public JSONArray createPrintData(String orderDate,String orderNo,String invoiceAmount){
        JSONArray jArr = new JSONArray();
        try{
            double totalPcs = 0;
            double totalAmount = 0;
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST,App.DRIVER_COLLECTION);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE",Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", tv_date.getText().toString());
            mainArr.put("TIME","00:00:00");
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO","1234");
            mainArr.put("DOCUMENT NO",orderNo);  //Load Summary No
            // mainArr.put("TRIP START DATE",Helpers.formatDate(new Date(),"dd-MM-yyyy"));
            mainArr.put("supervisorname","-");
            mainArr.put("LANG",Settings.getString(App.LANGUAGE));
            mainArr.put("INVOICETYPE","ORDER REQUEST");
            mainArr.put("ORDERNO",orderNo);
            mainArr.put("invoicepaymentterms","3");
            String testAr = "هذا هو اختبار النص العربي";
            if(object != null) {
                mainArr.put("CUSTOMER", UrlBuilder.decodeString(object.getCustomerName()) + "-" + testAr);
                mainArr.put("ADDRESS",object.getCustomerAddress().equals("")?"-":object.getCustomerAddress());
                mainArr.put("ARBADDRESS",object.getCustomerAddress());
            }
            else{
                mainArr.put("CUSTOMER","");
                mainArr.put("ADDRESS","-");
                mainArr.put("ARBADDRESS","-");
            }
            mainArr.put("TripID",Settings.getString(App.TRIP_ID));
            mainArr.put("LANG","en");
            mainArr.put("invoicepaymentterms","2");
            mainArr.put("RECEIPT","INVOICE RECEIPT");
            mainArr.put("SUB TOTAL","1000");
            mainArr.put("INVOICE DISCOUNT","20");
            mainArr.put("NET SALES","980");
            String paymentType = "";
            if(getcheckamt()>0&&getcashamt()>0){
                paymentType = "2";
            }
            else if(getcheckamt()>0){
                paymentType = "1";
            }
            else if(getcashamt()>0){
                paymentType = "0";
            }
            else if(getcashamt()<0&&getcheckamt()<0){
                paymentType = "2";
            }
            else if(getcheckamt()<0){
                paymentType = "1";
            }
            else if(getcashamt()<0){
                paymentType = "0";
            }
            Log.e("Payment Type","" + paymentType);
            mainArr.put("PaymentType",paymentType);
            //mainArr.put("Load Number","1");


            JSONArray HEADERS = new JSONArray();
            JSONArray TOTAL = new JSONArray();

            HEADERS.put("Invoice#");
            HEADERS.put("Due Date");
            HEADERS.put("Due Amount");
            HEADERS.put("Invoice Balance");
            HEADERS.put("Amount Paid");
            //HEADERS.put("Description");

            //HEADERS.put(obj1);
            // HEADERS.put(obj2);
            mainArr.put("HEADERS",HEADERS);
            JSONObject jCash = new JSONObject();
            jCash.put("Amount",String.valueOf(getcashamt()));
            mainArr.put("Cash",jCash);

            JSONArray jCheque = new JSONArray();
            JSONObject jChequeData = new JSONObject();
            jChequeData.put("Cheque Date",tv_date.getText().toString());
            jChequeData.put("Cheque No",edt_check_no.getText().toString());
            jChequeData.put("Bank",UrlBuilder.decodeString(bankname)    );
            jChequeData.put("Amount",String.valueOf(getcheckamt()));
            jCheque.put(jChequeData);
            mainArr.put("Cheque",jCheque);
            mainArr.put("expayment","");

            JSONObject totalObj = new JSONObject();
            totalObj.put("Invoice Balance","+" + amountdue);
            totalObj.put("Amount Paid", String.valueOf(getcashamt()+getcheckamt()));
            //totalObj.put("AMOUNT","+2230");
            TOTAL.put(totalObj);
            mainArr.put("TOTAL",totalObj);

            JSONArray jData = new JSONArray();
            JSONArray jData3 = new JSONArray();
            jData3.put(invoiceNo==null?"-----------":invoiceNo);
            jData3.put(Helpers.formatDate(new Date(),"dd-MM-yyyy"));
            jData3.put(amountdue);
            jData3.put("0");
            jData3.put(String.valueOf(getcashamt()+getcheckamt()));

            jData.put(jData3);
            /*for(OrderRequest obj:arraylist){
                if(Double.parseDouble(obj.getCases())> 0 || Double.parseDouble(obj.getUnits())>0){
                    JSONArray data = new JSONArray();
                    data.put(StringUtils.stripStart(obj.getMaterialNo(),"0"));
                    data.put(obj.getItemName());
                    data.put("شد 48*200مل بيرين PH8");
                    data.put("1");
                    data.put(obj.getCases());
                    totalPcs += Double.parseDouble(obj.getCases());
                    data.put(obj.getPrice());
                    data.put(String.valueOf(Double.parseDouble(obj.getCases()) * Double.parseDouble(obj.getPrice())));
                    totalAmount += Double.parseDouble(obj.getCases())*Double.parseDouble(obj.getPrice());
                    jData.put(data);
                }

            }*/
            mainArr.put("data",jData);
            mainArr.put(JsonRpcUtil.PARAM_DATA,jData);

            jDict.put("mainArr",mainArr);
            jInter.put(jDict);
            jArr.put(jInter);

            jArr.put(HEADERS);
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return jArr;
    }
    public void callback(){
        Intent intent = new Intent(DriverPaymentDetails.this, DriverCollectionsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
