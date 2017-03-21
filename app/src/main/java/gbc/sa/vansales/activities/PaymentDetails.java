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
import android.view.Window;
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

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.BankAdapter;
import gbc.sa.vansales.adapters.ReasonAdapter;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.Banks;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.Bank;
import gbc.sa.vansales.models.Collection;
import gbc.sa.vansales.models.ColletionData;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.DeliveryItem;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.models.OrderRequest;
import gbc.sa.vansales.models.Reasons;
import gbc.sa.vansales.models.Sales;
import gbc.sa.vansales.printer.JsonRpcUtil;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.PrinterHelper;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
public class PaymentDetails extends AppCompatActivity {
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
    boolean isPaymentMade = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details_new);
        try{
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
            object = (Customer) i.getParcelableExtra("headerObj");
            if (getIntent().getExtras() != null) {
                from = getIntent().getStringExtra("from");
                if (from.equals("collection")) {
                    pos = getIntent().getIntExtra("pos", 0);
                    amountdue = getIntent().getStringExtra("amountdue");
                    object = getIntent().getParcelableExtra("headerObj");
                    collection = (Collection) i.getParcelableExtra("collection");
                    tv_due_amt.setText(amountdue);
                    customers = CustomerHeaders.get();
                    CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, object.getCustomerID());
                    TextView tv_cust_detail = (TextView) findViewById(R.id.tv_cust_detail);
                    if (customerHeader != null) {
                        tv_cust_detail.setText(StringUtils.stripStart(customerHeader.getCustomerNo(), "0") + " " + UrlBuilder.decodeString(customerHeader.getName1()));
                        customerNo = customerHeader.getCustomerNo();
                        customerName = customerHeader.getName1();
                        customerAddress = customerHeader.getAddress();
                    } else {
                        tv_cust_detail.setText(StringUtils.stripStart(object.getCustomerID().toString(), "0") + " " + UrlBuilder.decodeString(object.getCustomerName().toString()));
                        customerNo = object.getCustomerID().toString();
                        customerName = object.getCustomerName();
                        customerAddress = "";
                    }
                    if (object.getPaymentMethod().equals(App.CASH_CUSTOMER)) {
                        allowPartial = false;
                    } else {
                        allowPartial = true;
                    }
                } else if (from.equals("Final Invoice")) {
                    invoiceNo = getIntent().getStringExtra("invoiceno");
                    amountdue = getIntent().getStringExtra("amountdue");
                    object = getIntent().getParcelableExtra("headerObj");
                    tv_due_amt.setText(amountdue);
                    customers = CustomerHeaders.get();
                    CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, object.getCustomerID());
                    TextView tv_cust_detail = (TextView) findViewById(R.id.tv_cust_detail);
                    if (customerHeader != null) {
                        tv_cust_detail.setText(StringUtils.stripStart(customerHeader.getCustomerNo(), "0") + " " + UrlBuilder.decodeString(customerHeader.getName1()));
                    } else {
                        tv_cust_detail.setText(StringUtils.stripStart(object.getCustomerID().toString(), "0") + " " + UrlBuilder.decodeString(object.getCustomerName().toString()));
                    }
                } else {
                    delivery = (OrderList) i.getParcelableExtra("delivery");
                /*if (object == null) {
                    object = Const.allCustomerdataArrayList.get(Const.customerPosition);
                }*/
                    invoiceNo = i.getExtras().getString("invoiceno");
                    customers = CustomerHeaders.get();
                    articles = ArticleHeaders.get();
                    CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, object.getCustomerID());
                    invoiceAmount = i.getExtras().getString("invoiceamount");
                    amountdue = invoiceAmount;
                    tv_due_amt.setText(invoiceAmount);
                    TextView tv_cust_detail = (TextView) findViewById(R.id.tv_cust_detail);
                    if (customerHeader != null) {
                        tv_cust_detail.setText(StringUtils.stripStart(customerHeader.getCustomerNo(), "0") + " " + UrlBuilder.decodeString(customerHeader.getName1()));
                    } else {
                        tv_cust_detail.setText(StringUtils.stripStart(object.getCustomerID().toString(), "0") + " " + UrlBuilder.decodeString(object.getCustomerName().toString()));
                    }
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
                    if (from.equals("collection")) {
                        //finish();
                        Intent intent1 = new Intent(PaymentDetails.this, CollectionsActivity.class);
                        intent1.putExtra("headerObj", object);
                        startActivity(intent1);
                    } else if (from.equals("Final Invoice")) {
                        Toast.makeText(PaymentDetails.this, getString(R.string.complete_transaction), Toast.LENGTH_SHORT).show();
                    } else if (from.equals("delivery")) {
                        Toast.makeText(PaymentDetails.this, getString(R.string.complete_transaction), Toast.LENGTH_SHORT).show();
                    }
//                Intent intent = new Intent();
//                intent.putExtra("pos", pos);
//                intent.putExtra("amt", String.valueOf(total_amt));
//                setResult(RESULT_OK, intent);
                    // finish();
                }
            });
            tv_total_amount = (TextView) findViewById(R.id.tv_total_amt);
            tv_date = (TextView) findViewById(R.id.tv_date);
            iv_cal = (ImageView) findViewById(R.id.image_cal);
            if(object.getPaymentMethod().equals(App.CASH_CUSTOMER)){
                iv_cal.setEnabled(false);
            }
            else{
                iv_cal.setEnabled(true);
            }

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
                    double cash_amt = getcashamt();
                    double check_amt = getcheckamt();
                    total_amt = cash_amt + check_amt;
                    if(total_amt==0){
                        finish();
                    }
                    else{
                        if(allowPartial){
                            if (from.equals("delivery")) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_AMOUNT_CLEARED, "");
                                map.put(db.KEY_CASH_AMOUNT, "");
                                map.put(db.KEY_CHEQUE_AMOUNT, "");
                                map.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,"");
                                map.put(db.KEY_CHEQUE_NUMBER, "");
                                map.put(db.KEY_CHEQUE_DATE, "");
                                map.put(db.KEY_CHEQUE_BANK_CODE, "");
                                map.put(db.KEY_CHEQUE_BANK_NAME, "");
                                HashMap<String, String> filter1 = new HashMap<String, String>();
                                filter1.put(db.KEY_INVOICE_NO, invoiceNo);
                                filter1.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                Cursor c = db.getData(db.COLLECTION, map, filter1);
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
                                    chequeAmount = c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL));
                                    chequeNumber = c.getString(c.getColumnIndex(db.KEY_CHEQUE_NUMBER));
                                    chequeDate = c.getString(c.getColumnIndex(db.KEY_CHEQUE_DATE));
                                    bankName = c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_NAME));
                                    bankCode = c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_CODE));
                                }
                                prevAmount += Float.parseFloat(tv_total_amount.getText().toString());
                                prevCashAmount += getcashamt();
                                prevCheqAmount += getcheckamt();
                                if (getcheckamt() > 0) {
                                    chequeAmount = chequeAmount + "," + String.valueOf(getcheckamt());
                                    chequeNumber = chequeNumber + "," + edt_check_no.getText().toString();
                                    chequeDate = chequeDate + "," + tv_date.getText().toString();
                                    bankCode = bankCode + "," + bankcode;
                                    bankName = bankName + "," + bankname;
                                }

                                HashMap<String, String> updateMap = new HashMap<String, String>();
                                updateMap.put(db.KEY_COLLECTION_TYPE, App.COLLECTION_DELIVERY);
                                updateMap.put(db.KEY_CUSTOMER_TYPE, object.getPaymentMethod());
                                updateMap.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                updateMap.put(db.KEY_INVOICE_NO, invoiceNo);
                                updateMap.put(db.KEY_INVOICE_AMOUNT, tv_total_amount.getText().toString());
                                updateMap.put(db.KEY_INVOICE_DATE, Helpers.formatDate(new Date(), App.DATE_FORMAT));
                                updateMap.put(db.KEY_AMOUNT_CLEARED, String.valueOf(prevAmount));
                                updateMap.put(db.KEY_CHEQUE_NUMBER, chequeNumber);
                                updateMap.put(db.KEY_CHEQUE_DATE,chequeDate);
                                updateMap.put(db.KEY_CASH_AMOUNT, String.valueOf(prevCashAmount));
                                updateMap.put(db.KEY_CHEQUE_AMOUNT, String.valueOf(prevCheqAmount));
                                updateMap.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,chequeAmount);
                                updateMap.put(db.KEY_CHEQUE_BANK_CODE, bankCode);
                                updateMap.put(db.KEY_CHEQUE_BANK_NAME, bankName);
                                if (Float.parseFloat(tv_total_amount.getText().toString()) == Float.parseFloat(amountdue)) {
                                    updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_COMPLETE);
                                    updateMap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                                } else {
                                    updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_PARTIAL);
                                    updateMap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                                }

                                HashMap<String, String> filter = new HashMap<String, String>();
                                filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                filter.put(db.KEY_INVOICE_NO, invoiceNo);
                                filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                                if(getcashamt()>0 || getcheckamt()>0){
                                    HashMap<String,String>checkMap = new HashMap<String, String>();
                                    checkMap.put(db.KEY_INVOICE_NO, invoiceNo);
                                    checkMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_COMPLETE);
                                    if(!db.checkData(db.COLLECTION,checkMap)){
                                        db.updateData(db.COLLECTION, updateMap, filter);
                                    }
                                    //db.updateData(db.COLLECTION, updateMap, filter);
                                }
                                if (Helpers.isNetworkAvailable(getApplicationContext())) {
                                    Helpers.createBackgroundJob(getApplicationContext());
                                }
                                final Dialog dialog = new Dialog(PaymentDetails.this);
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
                                /*if (Helpers.isNetworkAvailable(getApplicationContext())) {
                                    Helpers.createBackgroundJob(getApplicationContext());
                                }*/
                                        try {
                                            JSONArray jsonArray = createPrintData("COLLECTION",invoiceNo,"");
                                            JSONObject data = new JSONObject();
                                            data.put("data", (JSONArray) jsonArray);
                                            HashMap<String, String> map = new HashMap<>();
                                            map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                            map.put(db.KEY_ORDER_ID, invoiceNo);
                                            map.put(db.KEY_DOC_TYPE, ConfigStore.CollectionRequest_TR);
                                            map.put(db.KEY_DATA, data.toString());
                                            //map.put(db.KEY_DATA,jsonArray.toString());
                                            Log.e("Print Data","Receipt" + map);
                                            Helpers.logData(PaymentDetails.this, "User clicked on print");
                                            Helpers.logData(PaymentDetails.this,"Print Data" + map);
                                            db.addDataPrint(db.DELAY_PRINT, map);
                                            PrinterHelper object = new PrinterHelper(PaymentDetails.this, PaymentDetails.this);
                                            object.execute("", jsonArray);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Crashlytics.logException(e);
                                        }
                                    }
                                });
                                btn_notprint.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        try {
                                            JSONArray jsonArray = createPrintData("COLLECTION",invoiceNo,"");
                                            JSONObject data = new JSONObject();
                                            data.put("data", (JSONArray) jsonArray);
                                            HashMap<String, String> map = new HashMap<>();
                                            map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                            map.put(db.KEY_ORDER_ID, invoiceNo);
                                            map.put(db.KEY_DOC_TYPE, ConfigStore.CollectionRequest_TR);
                                            map.put(db.KEY_DATA, data.toString());
                                            //map.put(db.KEY_DATA,jsonArray.toString());
                                            Log.e("Print Data", "Receipt" + map);
                                            Helpers.logData(PaymentDetails.this, "User clicked on do not print");
                                            Helpers.logData(PaymentDetails.this, "Print Data" + map);
                                            db.addDataPrint(db.DELAY_PRINT, map);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Crashlytics.logException(e);
                                        }
                                        Intent intent = new Intent(PaymentDetails.this, DeliveryActivity.class);
                                        intent.putExtra("headerObj", object);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                            else if (from.equals("collection")) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_AMOUNT_CLEARED, "");
                                map.put(db.KEY_CASH_AMOUNT, "");
                                map.put(db.KEY_CHEQUE_AMOUNT, "");
                                map.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,"");
                                map.put(db.KEY_CHEQUE_NUMBER, "");
                                map.put(db.KEY_CHEQUE_DATE, "");
                                map.put(db.KEY_CHEQUE_BANK_CODE, "");
                                map.put(db.KEY_CHEQUE_BANK_NAME, "");
                                HashMap<String, String> filter = new HashMap<String, String>();
                                filter.put(db.KEY_INVOICE_NO, collection.getInvoiceNo());
                                filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                Cursor c = db.getData(db.COLLECTION, map, filter);
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
                                    chequeAmount = c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL));
                                    chequeNumber = c.getString(c.getColumnIndex(db.KEY_CHEQUE_NUMBER));
                                    chequeDate = c.getString(c.getColumnIndex(db.KEY_CHEQUE_DATE));
                                    bankName = c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_NAME));
                                    bankCode = c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_CODE));
                                }
                                prevAmount += Float.parseFloat(tv_total_amount.getText().toString());
                                prevCashAmount += getcashamt();
                                prevCheqAmount += getcheckamt();
                                if (getcheckamt() > 0) {
                                    chequeAmount = chequeAmount + "," + String.valueOf(getcheckamt());
                                    chequeNumber = chequeNumber + "," + edt_check_no.getText().toString();
                                    chequeDate = chequeDate + "," + tv_date.getText().toString();
                                    bankCode = bankCode + "," + bankcode;
                                    bankName = bankName + "," + bankname;
                                }
                                HashMap<String, String> updateMap = new HashMap<String, String>();
                                updateMap.put(db.KEY_AMOUNT_CLEARED, String.valueOf(prevAmount));
                                updateMap.put(db.KEY_CHEQUE_NUMBER, chequeNumber);
                                updateMap.put(db.KEY_CHEQUE_DATE,chequeDate);
                                updateMap.put(db.KEY_CASH_AMOUNT, String.valueOf(prevCashAmount));
                                updateMap.put(db.KEY_CHEQUE_AMOUNT, String.valueOf(prevCheqAmount));
                                updateMap.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,chequeAmount);
                                updateMap.put(db.KEY_CHEQUE_BANK_CODE, bankCode);
                                updateMap.put(db.KEY_CHEQUE_BANK_NAME, bankName);
                                if (Float.parseFloat(tv_total_amount.getText().toString()) == Float.parseFloat(amountdue)) {
                                    updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_COMPLETE);
                                    updateMap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                                } else {
                                    updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_PARTIAL);
                                    updateMap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                                }
                                if(!isPaymentMade){
                                    if(getcashamt()>0 || getcheckamt()>0){
                                        HashMap<String,String>checkMap = new HashMap<String, String>();
                                        checkMap.put(db.KEY_INVOICE_NO, collection.getInvoiceNo());
                                        checkMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_COMPLETE);
                                        if(!db.checkData(db.COLLECTION,checkMap)){
                                            db.updateData(db.COLLECTION, updateMap, filter);
                                        }
                                    }
                                    //db.updateData(db.COLLECTION, updateMap, filter);
                                }
                                final Dialog dialog = new Dialog(PaymentDetails.this);
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
                                /*if (Helpers.isNetworkAvailable(getApplicationContext())) {
                                    Helpers.createBackgroundJob(getApplicationContext());
                                }*/
                                        isPaymentMade = true;
                                        try{
                                            JSONArray jsonArray = createPrintData("COLLECTION",invoiceNo,"");
                                            JSONObject data = new JSONObject();
                                            data.put("data",(JSONArray)jsonArray);

                                            HashMap<String,String>map = new HashMap<>();
                                            map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                                            map.put(db.KEY_ORDER_ID,invoiceNo);
                                            map.put(db.KEY_DOC_TYPE,ConfigStore.CollectionRequest_TR);
                                            map.put(db.KEY_DATA,data.toString());
                                            //map.put(db.KEY_DATA,jsonArray.toString());
                                            Log.e("Print Data", "Receipt" + map);
                                            Helpers.logData(PaymentDetails.this, "User clicked on print");
                                            Helpers.logData(PaymentDetails.this, "Print Data" + map);
                                            db.addDataPrint(db.DELAY_PRINT,map);

                                            PrinterHelper object = new PrinterHelper(PaymentDetails.this, PaymentDetails.this);
                                            object.execute("", jsonArray);
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                            Crashlytics.logException(e);
                                        }

                                    }
                                });
                                btn_notprint.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        isPaymentMade = true;
                                        try{
                                            JSONArray jsonArray = createPrintData("COLLECTION",invoiceNo,"");
                                            JSONObject data = new JSONObject();
                                            data.put("data",(JSONArray)jsonArray);

                                            HashMap<String,String>map = new HashMap<>();
                                            map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                                            map.put(db.KEY_ORDER_ID,invoiceNo);
                                            map.put(db.KEY_DOC_TYPE,ConfigStore.CollectionRequest_TR);
                                            map.put(db.KEY_DATA,data.toString());
                                            //map.put(db.KEY_DATA,jsonArray.toString());
                                            Log.e("Print Data", "Receipt" + map);
                                            Helpers.logData(PaymentDetails.this, "User clicked on do not print");
                                            Helpers.logData(PaymentDetails.this, "Print Data" + map);
                                            db.addDataPrint(db.DELAY_PRINT,map);
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                            Crashlytics.logException(e);
                                        }

                                /*if (Helpers.isNetworkAvailable(getApplicationContext())) {
                                    Helpers.createBackgroundJob(getApplicationContext());
                                }*/

                                        Intent intent1 = new Intent(PaymentDetails.this, CollectionsActivity.class);
                                        intent1.putExtra("headerObj", object);
                                        startActivity(intent1);
                                    }
                                });
                            } else if (from.equals("Final Invoice")) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_AMOUNT_CLEARED, "");
                                map.put(db.KEY_CASH_AMOUNT, "");
                                map.put(db.KEY_CHEQUE_AMOUNT, "");
                                map.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,"");
                                map.put(db.KEY_CHEQUE_NUMBER, "");
                                map.put(db.KEY_CHEQUE_DATE, "");
                                map.put(db.KEY_CHEQUE_BANK_CODE, "");
                                map.put(db.KEY_CHEQUE_BANK_NAME, "");
                                HashMap<String, String> filter = new HashMap<String, String>();
                                filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                filter.put(db.KEY_INVOICE_NO, invoiceNo);
                                filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                                Cursor c = db.getData(db.COLLECTION, map, filter);
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
                                    chequeAmount = c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL));
                                    chequeNumber = c.getString(c.getColumnIndex(db.KEY_CHEQUE_NUMBER));
                                    chequeDate = c.getString(c.getColumnIndex(db.KEY_CHEQUE_DATE));
                                    bankName = c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_NAME));
                                    bankCode = c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_CODE));
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
                                updateMap.put(db.KEY_CHEQUE_DATE,chequeDate);
                                updateMap.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,chequeAmount);
                                updateMap.put(db.KEY_CHEQUE_BANK_CODE, bankCode);
                                updateMap.put(db.KEY_CHEQUE_BANK_NAME, bankName);
                                if (Float.parseFloat(tv_total_amount.getText().toString()) == Float.parseFloat(amountdue)) {
                                    updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_COMPLETE);
                                    updateMap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                                } else {
                                    updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_PARTIAL);
                                    updateMap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                                }
                                if(getcashamt()>0 || getcheckamt()>0){
                                    HashMap<String,String>checkMap = new HashMap<String, String>();
                                    checkMap.put(db.KEY_INVOICE_NO, invoiceNo);
                                    checkMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_COMPLETE);
                                    if(!db.checkData(db.COLLECTION,checkMap)){
                                        db.updateData(db.COLLECTION, updateMap, filter);
                                    }
                                    //db.updateData(db.COLLECTION, updateMap, filter);
                                }
                                //db.updateData(db.COLLECTION, updateMap, filter);
                                final Dialog dialog = new Dialog(PaymentDetails.this);
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
                                        isPaymentMade = true;
                                        if (Helpers.isNetworkAvailable(getApplicationContext())) {
                                            Helpers.createBackgroundJob(getApplicationContext());
                                        }
                                        try{
                                            JSONArray jsonArray = createPrintData("COLLECTION",invoiceNo,"");

                                            JSONObject data = new JSONObject();
                                            data.put("data",(JSONArray)jsonArray);

                                            HashMap<String,String>map = new HashMap<>();
                                            map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                                            map.put(db.KEY_ORDER_ID,invoiceNo);
                                            map.put(db.KEY_DOC_TYPE,ConfigStore.CollectionRequest_TR);
                                            map.put(db.KEY_DATA,data.toString());
                                            //map.put(db.KEY_DATA,jsonArray.toString());
                                            Log.e("Print Data", "Receipt" + map);
                                            Helpers.logData(PaymentDetails.this, "User clicked on print");
                                            Helpers.logData(PaymentDetails.this, "Print Data" + map);
                                            db.addDataPrint(db.DELAY_PRINT,map);

                                            PrinterHelper object = new PrinterHelper(PaymentDetails.this, PaymentDetails.this);
                                            object.execute("", jsonArray);
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                            Crashlytics.logException(e);
                                        }
                                    }
                                });
                                btn_notprint.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        isPaymentMade = true;
                                        try{
                                            JSONArray jsonArray = createPrintData("COLLECTION",invoiceNo,"");
                                            JSONObject data = new JSONObject();
                                            data.put("data",(JSONArray)jsonArray);

                                            HashMap<String,String>map = new HashMap<>();
                                            map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                                            map.put(db.KEY_ORDER_ID,invoiceNo);
                                            map.put(db.KEY_DOC_TYPE,ConfigStore.CollectionRequest_TR);
                                            map.put(db.KEY_DATA,data.toString());
                                            //map.put(db.KEY_DATA,jsonArray.toString());
                                            Log.e("Print Data", "Receipt" + map);
                                            Helpers.logData(PaymentDetails.this, "User clicked on do not print");
                                            Helpers.logData(PaymentDetails.this, "Print Data" + map);
                                            db.addDataPrint(db.DELAY_PRINT,map);
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                            Crashlytics.logException(e);
                                        }

                                        if (Helpers.isNetworkAvailable(getApplicationContext())) {
                                            Helpers.createBackgroundJob(getApplicationContext());
                                        }
                                        Intent intent = new Intent(PaymentDetails.this, CustomerDetailActivity.class);
                                        intent.putExtra("headerObj", object);
                                        intent.putExtra("msg", "visit");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } else {
                                final Dialog dialog = new Dialog(PaymentDetails.this);
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
                                        Intent intent = new Intent(PaymentDetails.this, CustomerDetailActivity.class);
                                        intent.putExtra("headerObj", object);
                                        intent.putExtra("msg", "visit");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                btn_notprint.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(PaymentDetails.this, CustomerDetailActivity.class);
                                        intent.putExtra("headerObj", object);
                                        intent.putExtra("msg", "visit");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
//                               dialog.dismiss();
//                                Intent intent = new Intent();
//                                intent.putExtra("pos", pos);
//                                intent.putExtra("amt", String.valueOf(total_amt));
//                                setResult(RESULT_OK, intent);
//                                finish();
                                    }
                                });
                            }
                        }
                        else{
                            if (total_amt > Double.parseDouble(amountdue) || total_amt < Double.parseDouble(amountdue)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentDetails.this);
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
                            }
                            else {
                                if (from.equals("delivery")) {

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put(db.KEY_AMOUNT_CLEARED, "");
                                    map.put(db.KEY_CASH_AMOUNT, "");
                                    map.put(db.KEY_CHEQUE_AMOUNT, "");
                                    map.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,"");
                                    map.put(db.KEY_CHEQUE_NUMBER, "");
                                    map.put(db.KEY_CHEQUE_DATE, "");
                                    map.put(db.KEY_CHEQUE_BANK_CODE, "");
                                    map.put(db.KEY_CHEQUE_BANK_NAME, "");
                                    HashMap<String, String> filter1 = new HashMap<String, String>();
                                    filter1.put(db.KEY_INVOICE_NO, invoiceNo);
                                    filter1.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                    Cursor c = db.getData(db.COLLECTION, map, filter1);
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
                                        chequeAmount = c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL));
                                        chequeNumber = c.getString(c.getColumnIndex(db.KEY_CHEQUE_NUMBER));
                                        chequeDate = c.getString(c.getColumnIndex(db.KEY_CHEQUE_DATE));
                                        bankName = c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_NAME));
                                        bankCode = c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_CODE));
                                    }
                                    prevAmount += Float.parseFloat(tv_total_amount.getText().toString());
                                    prevCashAmount += getcashamt();
                                    prevCheqAmount += getcheckamt();
                                    if (getcheckamt() > 0) {
                                        chequeAmount = chequeAmount + "," + String.valueOf(getcheckamt());
                                        chequeNumber = chequeNumber + "," + edt_check_no.getText().toString();
                                        chequeDate = chequeDate + "," + tv_date.getText().toString();
                                        bankCode = bankCode + "," + bankcode;
                                        bankName = bankName + "," + bankname;
                                    }

                                    HashMap<String, String> updateMap = new HashMap<String, String>();
                                    updateMap.put(db.KEY_COLLECTION_TYPE, App.COLLECTION_DELIVERY);
                                    updateMap.put(db.KEY_CUSTOMER_TYPE, object.getPaymentMethod());
                                    updateMap.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                    updateMap.put(db.KEY_INVOICE_NO, invoiceNo);
                                    updateMap.put(db.KEY_INVOICE_AMOUNT, tv_total_amount.getText().toString());
                                    updateMap.put(db.KEY_INVOICE_DATE, Helpers.formatDate(new Date(), App.DATE_FORMAT));
                                    updateMap.put(db.KEY_AMOUNT_CLEARED, String.valueOf(prevAmount));
                                    updateMap.put(db.KEY_CHEQUE_NUMBER, chequeNumber);
                                    updateMap.put(db.KEY_CHEQUE_DATE,chequeDate);
                                    updateMap.put(db.KEY_CASH_AMOUNT, String.valueOf(prevCashAmount));
                                    updateMap.put(db.KEY_CHEQUE_AMOUNT, String.valueOf(prevCheqAmount));
                                    updateMap.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,chequeAmount);
                                    updateMap.put(db.KEY_CHEQUE_BANK_CODE, bankCode);
                                    updateMap.put(db.KEY_CHEQUE_BANK_NAME, bankName);
                                    if (Float.parseFloat(tv_total_amount.getText().toString()) == Float.parseFloat(amountdue)) {
                                        updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_COMPLETE);
                                        updateMap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                                    } else {
                                        updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_PARTIAL);
                                        updateMap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                                    }

                                    HashMap<String, String> filter = new HashMap<String, String>();
                                    filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                    filter.put(db.KEY_INVOICE_NO, invoiceNo);
                                    filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                                    //db.updateData(db.COLLECTION, updateMap, filter);

                                    if(getcashamt()>0 || getcheckamt()>0){
                                        HashMap<String,String>checkMap = new HashMap<String, String>();
                                        checkMap.put(db.KEY_INVOICE_NO, invoiceNo);
                                        checkMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_COMPLETE);
                                        if(!db.checkData(db.COLLECTION,checkMap)){
                                            db.updateData(db.COLLECTION, updateMap, filter);
                                        }
                                        //db.updateData(db.COLLECTION, updateMap, filter);
                                    }
                            /*HashMap<String, String> invoiceMap = new HashMap<>();
                            invoiceMap.put(db.KEY_COLLECTION_TYPE, App.COLLECTION_DELIVERY);
                            invoiceMap.put(db.KEY_CUSTOMER_TYPE, object.getPaymentMethod());
                            invoiceMap.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                            invoiceMap.put(db.KEY_INVOICE_NO, invoiceNo);
                            invoiceMap.put(db.KEY_INVOICE_AMOUNT, tv_total_amount.getText().toString());
                            invoiceMap.put(db.KEY_INVOICE_DATE, Helpers.formatDate(new Date(), App.DATE_FORMAT));
                            invoiceMap.put(db.KEY_AMOUNT_CLEARED, tv_total_amount.getText().toString());
                            invoiceMap.put(db.KEY_CHEQUE_AMOUNT, String.valueOf(getcheckamt()));
                            invoiceMap.put(db.KEY_CHEQUE_NUMBER, edt_check_no.getText().toString());
                            invoiceMap.put(db.KEY_CHEQUE_DATE, tv_date.getText().toString());
                            invoiceMap.put(db.KEY_CHEQUE_BANK_CODE, bankcode);
                            invoiceMap.put(db.KEY_CHEQUE_BANK_NAME, bankname);
                            invoiceMap.put(db.KEY_CASH_AMOUNT, String.valueOf(getcashamt()));
                            invoiceMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_COMPLETE);
                            invoiceMap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                            invoiceMap.put(db.KEY_IS_PRINTED, App.DATA_MARKED_FOR_POST);
                            HashMap<String, String> filter = new HashMap<String, String>();
                            filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                            filter.put(db.KEY_INVOICE_NO, invoiceNo);
                            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                            db.updateData(db.COLLECTION, invoiceMap, filter);*/
                                    if (Helpers.isNetworkAvailable(getApplicationContext())) {
                                        Helpers.createBackgroundJob(getApplicationContext());
                                    }

                                    final Dialog dialog = new Dialog(PaymentDetails.this);
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
                                /*if (Helpers.isNetworkAvailable(getApplicationContext())) {
                                    Helpers.createBackgroundJob(getApplicationContext());
                                }*/
                                            try {
                                                JSONArray jsonArray = createPrintData("COLLECTION",invoiceNo,"");
                                                JSONObject data = new JSONObject();
                                                data.put("data", (JSONArray) jsonArray);
                                                HashMap<String, String> map = new HashMap<>();
                                                map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                                map.put(db.KEY_ORDER_ID, invoiceNo);
                                                map.put(db.KEY_DOC_TYPE, ConfigStore.CollectionRequest_TR);
                                                map.put(db.KEY_DATA, data.toString());
                                                //map.put(db.KEY_DATA,jsonArray.toString());
                                                Log.e("Print Data", "Receipt" + map);
                                                Helpers.logData(PaymentDetails.this, "User clicked on print");
                                                Helpers.logData(PaymentDetails.this, "Print Data" + map);
                                                db.addDataPrint(db.DELAY_PRINT, map);
                                                PrinterHelper object = new PrinterHelper(PaymentDetails.this, PaymentDetails.this);
                                                object.execute("", jsonArray);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Crashlytics.logException(e);
                                            }
                                        }
                                    });
                                    btn_notprint.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            try {
                                                JSONArray jsonArray = createPrintData("COLLECTION",invoiceNo,"");
                                                JSONObject data = new JSONObject();
                                                data.put("data", (JSONArray) jsonArray);
                                                HashMap<String, String> map = new HashMap<>();
                                                map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                                map.put(db.KEY_ORDER_ID, invoiceNo);
                                                map.put(db.KEY_DOC_TYPE, ConfigStore.CollectionRequest_TR);
                                                map.put(db.KEY_DATA, data.toString());
                                                //map.put(db.KEY_DATA,jsonArray.toString());
                                                Log.e("Print Data", "Receipt" + map);
                                                Helpers.logData(PaymentDetails.this, "User clicked on do not print");
                                                Helpers.logData(PaymentDetails.this, "Print Data" + map);
                                                db.addDataPrint(db.DELAY_PRINT, map);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Crashlytics.logException(e);
                                            }
                                            Intent intent = new Intent(PaymentDetails.this, DeliveryActivity.class);
                                            intent.putExtra("headerObj", object);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                } else if (from.equals("collection")) {
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put(db.KEY_AMOUNT_CLEARED, "");
                                    map.put(db.KEY_CASH_AMOUNT, "");
                                    map.put(db.KEY_CHEQUE_AMOUNT, "");
                                    map.put(db.KEY_CHEQUE_NUMBER, "");
                                    map.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,"");
                                    map.put(db.KEY_CHEQUE_DATE, "");
                                    map.put(db.KEY_CHEQUE_BANK_CODE, "");
                                    map.put(db.KEY_CHEQUE_BANK_NAME, "");
                                    HashMap<String, String> filter = new HashMap<String, String>();
                                    filter.put(db.KEY_INVOICE_NO, collection.getInvoiceNo());
                                    filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                    Cursor c = db.getData(db.COLLECTION, map, filter);
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
                                    updateMap.put(db.KEY_CHEQUE_DATE,chequeDate);
                                    updateMap.put(db.KEY_CASH_AMOUNT, String.valueOf(prevCashAmount));
                                    updateMap.put(db.KEY_CHEQUE_AMOUNT, String.valueOf(prevCheqAmount));
                                    updateMap.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,chequeAmount);
                                    updateMap.put(db.KEY_CHEQUE_BANK_CODE, bankCode);
                                    updateMap.put(db.KEY_CHEQUE_BANK_NAME, bankName);
                                    if (Float.parseFloat(tv_total_amount.getText().toString()) == Float.parseFloat(amountdue)) {
                                        updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_COMPLETE);
                                    } else {
                                        updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_PARTIAL);
                                    }
                                    updateMap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                                    if(!isPaymentMade){
                                        if(getcashamt()>0 || getcheckamt()>0){
                                            HashMap<String,String>checkMap = new HashMap<String, String>();
                                            checkMap.put(db.KEY_INVOICE_NO, collection.getInvoiceNo());
                                            checkMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_COMPLETE);
                                            if(!db.checkData(db.COLLECTION,checkMap)){
                                                db.updateData(db.COLLECTION, updateMap, filter);
                                            }
                                            //db.updateData(db.COLLECTION, updateMap, filter);
                                        }
                                        //db.updateData(db.COLLECTION, updateMap, filter);
                                    }

                                    final Dialog dialog = new Dialog(PaymentDetails.this);
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
                                /*if (Helpers.isNetworkAvailable(getApplicationContext())) {
                                    Helpers.createBackgroundJob(getApplicationContext());
                                }*/
                                            isPaymentMade = true;
                                            try {
                                                JSONArray jsonArray = createPrintData("COLLECTION",invoiceNo,"");
                                                JSONObject data = new JSONObject();
                                                data.put("data", (JSONArray) jsonArray);
                                                HashMap<String, String> map = new HashMap<>();
                                                map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                                map.put(db.KEY_ORDER_ID, invoiceNo);
                                                map.put(db.KEY_DOC_TYPE, ConfigStore.CollectionRequest_TR);
                                                map.put(db.KEY_DATA, data.toString());
                                                //map.put(db.KEY_DATA,jsonArray.toString());
                                                Log.e("Print Data", "Receipt" + map);
                                                Helpers.logData(PaymentDetails.this, "User clicked on print");
                                                Helpers.logData(PaymentDetails.this, "Print Data" + map);
                                                db.addDataPrint(db.DELAY_PRINT, map);
                                                PrinterHelper object = new PrinterHelper(PaymentDetails.this, PaymentDetails.this);
                                                object.execute("", jsonArray);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Crashlytics.logException(e);
                                            }
                                        }
                                    });
                                    btn_notprint.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            isPaymentMade = true;
                                            try {
                                                JSONArray jsonArray = createPrintData("COLLECTION",invoiceNo,"");
                                                JSONObject data = new JSONObject();
                                                data.put("data", (JSONArray) jsonArray);
                                                HashMap<String, String> map = new HashMap<>();
                                                map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                                map.put(db.KEY_ORDER_ID, invoiceNo);
                                                map.put(db.KEY_DOC_TYPE, ConfigStore.CollectionRequest_TR);
                                                map.put(db.KEY_DATA, data.toString());
                                                //map.put(db.KEY_DATA,jsonArray.toString());
                                                Log.e("Print Data", "Receipt" + map);
                                                Helpers.logData(PaymentDetails.this, "User clicked on do not print");
                                                Helpers.logData(PaymentDetails.this, "Print Data" + map);
                                                db.addDataPrint(db.DELAY_PRINT, map);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Crashlytics.logException(e);
                                            }
                                            Intent intent = new Intent(PaymentDetails.this, CollectionsActivity.class);
                                            intent.putExtra("headerObj", object);
                                            //intent.putExtra("msg", "visit");
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                } else if (from.equals("Final Invoice")) {
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put(db.KEY_AMOUNT_CLEARED, "");
                                    map.put(db.KEY_CASH_AMOUNT, "");
                                    map.put(db.KEY_CHEQUE_AMOUNT, "");
                                    map.put(db.KEY_CHEQUE_NUMBER, "");
                                    map.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,"");
                                    map.put(db.KEY_CHEQUE_DATE, "");
                                    map.put(db.KEY_CHEQUE_BANK_CODE, "");
                                    map.put(db.KEY_CHEQUE_BANK_NAME, "");
                                    map.put(db.KEY_IS_POSTED,"");
                                    HashMap<String, String> filter = new HashMap<String, String>();
                                    filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                                    filter.put(db.KEY_INVOICE_NO, invoiceNo);
                                    Log.e("For Update","" + invoiceNo);
                                    filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                                    Cursor c = db.getData(db.COLLECTION, map, filter);
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
                                        chequeAmount = c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL));
                                        chequeDate = c.getString(c.getColumnIndex(db.KEY_CHEQUE_DATE));
                                        bankName = c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_NAME));
                                        bankCode = c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_CODE));
                                    }
                                    prevAmount += Float.parseFloat(tv_total_amount.getText().toString());
                                    prevCashAmount += getcashamt();
                                    prevCheqAmount += getcheckamt();
                                    if (getcheckamt() > 0 || getcheckamt()<0) {
                                        chequeNumber = chequeNumber + "," + edt_check_no.getText().toString();
                                        chequeDate = chequeDate + "," + tv_date.getText().toString();
                                        chequeAmount = chequeAmount + "," + String.valueOf(getcheckamt());
                                        bankCode = bankCode + "," + bankcode;
                                        bankName = bankName + "," + bankname;
                                    }
                                    HashMap<String, String> updateMap = new HashMap<String, String>();
                                    updateMap.put(db.KEY_AMOUNT_CLEARED, String.valueOf(prevAmount));
                                    updateMap.put(db.KEY_CHEQUE_NUMBER, chequeNumber);
                                    updateMap.put(db.KEY_CHEQUE_DATE,chequeDate);
                                    updateMap.put(db.KEY_CASH_AMOUNT, String.valueOf(prevCashAmount));
                                    updateMap.put(db.KEY_CHEQUE_AMOUNT, String.valueOf(prevCheqAmount));
                                    updateMap.put(db.KEY_CHEQUE_BANK_CODE, bankCode);
                                    updateMap.put(db.KEY_CHEQUE_BANK_NAME, bankName);
                                    if (Float.parseFloat(tv_total_amount.getText().toString()) == Float.parseFloat(amountdue)) {
                                        updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_COMPLETE);
                                    } else {
                                        updateMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_PARTIAL);
                                    }
                                    updateMap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                                    if(getcashamt()>0 || getcheckamt()>0){
                                        HashMap<String,String>checkMap = new HashMap<String, String>();
                                        checkMap.put(db.KEY_INVOICE_NO, invoiceNo);
                                        checkMap.put(db.KEY_IS_INVOICE_COMPLETE, App.INVOICE_COMPLETE);
                                        if(!db.checkData(db.COLLECTION,checkMap)){
                                            db.updateData(db.COLLECTION, updateMap, filter);
                                        }
                                        //db.updateData(db.COLLECTION, updateMap, filter);
                                    }
                                    //db.updateData(db.COLLECTION, updateMap, filter);

                                    final Dialog dialog = new Dialog(PaymentDetails.this);
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
                                            isPaymentMade = true;
                                            if (Helpers.isNetworkAvailable(getApplicationContext())) {
                                                Helpers.createBackgroundJob(getApplicationContext());
                                            }
                                            try{
                                                JSONArray jsonArray = createPrintData("COLLECTION",invoiceNo,"");
                                                JSONObject data = new JSONObject();
                                                data.put("data",(JSONArray)jsonArray);
                                                HashMap<String,String>map = new HashMap<>();
                                                map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                                                map.put(db.KEY_ORDER_ID,invoiceNo);
                                                map.put(db.KEY_DOC_TYPE,ConfigStore.CollectionRequest_TR);
                                                map.put(db.KEY_DATA,data.toString());
                                                //map.put(db.KEY_DATA,jsonArray.toString());
                                                Log.e("Print Data", "Receipt" + map);
                                                Helpers.logData(PaymentDetails.this, "User clicked on print");
                                                Helpers.logData(PaymentDetails.this, "Print Data" + map);
                                                db.addDataPrint(db.DELAY_PRINT,map);
                                                PrinterHelper object = new PrinterHelper(PaymentDetails.this, PaymentDetails.this);
                                                object.execute("", jsonArray);
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                                Crashlytics.logException(e);
                                            }
                                        }
                                    });
                                    btn_notprint.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            isPaymentMade = true;
                                            try{
                                                JSONArray jsonArray = createPrintData("COLLECTION",invoiceNo,"");
                                                JSONObject data = new JSONObject();
                                                data.put("data",(JSONArray)jsonArray);

                                                HashMap<String,String>map = new HashMap<>();
                                                map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                                                map.put(db.KEY_ORDER_ID,invoiceNo);
                                                map.put(db.KEY_DOC_TYPE,ConfigStore.CollectionRequest_TR);
                                                map.put(db.KEY_DATA,data.toString());
                                                //map.put(db.KEY_DATA,jsonArray.toString());
                                                Log.e("Print Data", "Receipt" + map);
                                                Helpers.logData(PaymentDetails.this, "User clicked on do not print");
                                                Helpers.logData(PaymentDetails.this, "Print Data" + map);
                                                db.addDataPrint(db.DELAY_PRINT,map);
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                                Crashlytics.logException(e);
                                            }
                                            if (Helpers.isNetworkAvailable(getApplicationContext())) {
                                                Helpers.createBackgroundJob(getApplicationContext());
                                            }
                                            Intent intent = new Intent(PaymentDetails.this, CustomerDetailActivity.class);
                                            intent.putExtra("headerObj", object);
                                            intent.putExtra("msg", "visit");
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                } else {
                                    final Dialog dialog = new Dialog(PaymentDetails.this);
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
                                            Intent intent = new Intent(PaymentDetails.this, CustomerDetailActivity.class);
                                            intent.putExtra("headerObj", object);
                                            intent.putExtra("msg", "visit");
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    btn_notprint.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            Intent intent = new Intent(PaymentDetails.this, CustomerDetailActivity.class);
                                            intent.putExtra("headerObj", object);
                                            intent.putExtra("msg", "visit");
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            }
                        }
                    }

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    @Override
    public void onBackPressed() {
        Toast.makeText(PaymentDetails.this, "Please complete the transaction", Toast.LENGTH_SHORT).show();
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        tv_date.setText(sdf.format(myCalendar.getTime()));
    }
    private void setDefaultDate(){
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        tv_date.setText(sdf.format(myCalendar.getTime()));
    }
    public double getcashamt() {
        try{
            if (edt_cash_amt.getText().toString().equals("")) {
                return 0;
            }
            return Double.parseDouble(edt_cash_amt.getText().toString());
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
            return 0;
        }

    }
    public double getcheckamt() {
        try{
            if (edt_check_amt.getText().toString().equals("")) {
                return 0;
            }
            return Double.parseDouble(edt_check_amt.getText().toString());
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
            return 0;
        }

    }
    private void loadBanks() {
        Banks.loadData(PaymentDetails.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(PaymentDetails.this);
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
    public String postData() {
        String orderID = "";
        String purchaseNumber = "";
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("Function", ConfigStore.CustomerDeliveryRequestFunction);
            map.put("OrderId", delivery.getOrderId());
            map.put("DocumentType", ConfigStore.DeliveryDocumentType);
            // map.put("DocumentDate", Helpers.formatDate(new Date(),App.DATE_FORMAT_WO_SPACE));
            // map.put("DocumentDate", null);
            map.put("CustomerId", object.getCustomerID());
            map.put("SalesOrg", Settings.getString(App.SALES_ORG));
            map.put("DistChannel", Settings.getString(App.DIST_CHANNEL));
            map.put("Division", Settings.getString(App.DIVISION));
            map.put("OrderValue", invoiceAmount);
            map.put("Currency", "SAR");
            /*map.put("PurchaseNum", Helpers.generateNumber(db, ConfigStore.CustomerDeliveryRequest_PR_Type));*/
            JSONArray deepEntity = new JSONArray();
            HashMap<String, String> itemMap = new HashMap<>();
            itemMap.put(db.KEY_ITEM_NO, "");
            itemMap.put(db.KEY_MATERIAL_NO, "");
            itemMap.put(db.KEY_MATERIAL_DESC1, "");
            itemMap.put(db.KEY_CASE, "");
            itemMap.put(db.KEY_UNIT, "");
            itemMap.put(db.KEY_AMOUNT, "");
            itemMap.put(db.KEY_ORDER_ID, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_DELIVERY_NO, delivery.getOrderId());
            Cursor cursor = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST, itemMap, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                map.put("PurchaseNum", cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID)));
                purchaseNumber = map.get("PurchaseNum");
                int itemno = 10;
                do {
                    ArticleHeader articleHeader = ArticleHeader.getArticle(articles, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    if (articleHeader.getBaseUOM().equals(App.CASE_UOM) || articleHeader.getBaseUOM().equals(App.BOTTLES_UOM)) {
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                        jo.put("Material", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant", "");
                        jo.put("Quantity", cursor.getString(cursor.getColumnIndex(db.KEY_CASE)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("UoM", articleHeader.getBaseUOM());
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("Storagelocation", "");
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
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("UoM", articleHeader.getBaseUOM());
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("Storagelocation", "");
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno + 10;
                        deepEntity.put(jo);
                    }
                }
                while (cursor.moveToNext());
            }
            orderID = IntegrationService.postData(PaymentDetails.this, App.POST_COLLECTION, map, deepEntity);
            //orderID = IntegrationService.postDataBackup(PaymentDetails.this, App.POST_COLLECTION, map, deepEntity);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return orderID + "," + purchaseNumber;
    }
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
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(db.KEY_TIME_STAMP, "");
            map.put(db.KEY_DELIVERY_NO, "");
            map.put(db.KEY_ITEM_NO, "");
            map.put(db.KEY_MATERIAL_NO, "");
            map.put(db.KEY_MATERIAL_DESC1, "");
            map.put(db.KEY_CASE, "");
            map.put(db.KEY_UNIT, "");
            map.put(db.KEY_UOM, "");
            map.put(db.KEY_AMOUNT, "");
            map.put(db.KEY_ORDER_ID, "");
            map.put(db.KEY_PURCHASE_NUMBER, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            Cursor cursor = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    DeliveryItem deliveryItem = new DeliveryItem();
                    deliveryItem.setItemCode(cursor.getString(cursor.getColumnIndex(db.KEY_ITEM_NO)));
                    deliveryItem.setMaterialNo(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    deliveryItem.setItemDescription(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                    deliveryItem.setItemCase(cursor.getString(cursor.getColumnIndex(db.KEY_CASE)));
                    deliveryItem.setItemUnits(cursor.getString(cursor.getColumnIndex(db.KEY_UNIT)));
                    deliveryItem.setItemUom(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                    deliveryItem.setAmount(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                    arrayList.add(deliveryItem);
                }
                while (cursor.moveToNext());
            }
            if (this.tokens[0].toString().equals(this.tokens[1].toString())) {
                for (DeliveryItem item : arrayList) {
                    HashMap<String, String> postmap = new HashMap<String, String>();
                    postmap.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    postmap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                    postmap.put(db.KEY_ORDER_ID, tokens[0].toString());
                    HashMap<String, String> filtermap = new HashMap<>();
                    filtermap.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                    // filtermap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    filtermap.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    filtermap.put(db.KEY_MATERIAL_NO, item.getMaterialNo());
                    filtermap.put(db.KEY_PURCHASE_NUMBER, tokens[1].toString());
                    db.updateData(db.CUSTOMER_DELIVERY_ITEMS_POST, postmap, filtermap);
                }
                if (loadingSpinner.isShowing()) {
                    loadingSpinner.hide();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentDetails.this);
                alertDialogBuilder/*.setTitle("Message")*/
                        //.setMessage("Request with reference " + tokens[0].toString() + " has been saved")
                        .setMessage(getString(R.string.request_created))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(db.KEY_IS_DELIVERED, "true");
                                HashMap<String, String> filter = new HashMap<>();
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_DELIVERY_NO, delivery.getOrderId());
                                db.updateData(db.CUSTOMER_DELIVERY_HEADER, map, filter);
                                dialog.dismiss();
                                if (Helpers.isNetworkAvailable(PaymentDetails.this)) {
                                    Helpers.createBackgroundJob(PaymentDetails.this);
                                }
                                Intent intent = new Intent(PaymentDetails.this, DeliveryActivity.class);
                                intent.putExtra("headerObj", object);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                //  finish();
                            }
                        });
                // create alert dialog
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            } else {
                for (DeliveryItem item : arrayList) {
                    HashMap<String, String> postmap = new HashMap<String, String>();
                    postmap.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    postmap.put(db.KEY_IS_POSTED, App.DATA_IS_POSTED);
                    postmap.put(db.KEY_ORDER_ID, tokens[0].toString());
                    HashMap<String, String> filtermap = new HashMap<>();
                    filtermap.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                    //  filtermap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    filtermap.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    filtermap.put(db.KEY_MATERIAL_NO, item.getMaterialNo());
                    filtermap.put(db.KEY_PURCHASE_NUMBER, tokens[1].toString());
                    db.updateData(db.CUSTOMER_DELIVERY_ITEMS_POST, postmap, filtermap);
                }
                if (loadingSpinner.isShowing()) {
                    loadingSpinner.hide();
                }
                if (this.orderID.isEmpty() || this.orderID.equals("") || this.orderID == null) {
                    // Toast.makeText(getApplicationContext(), getString(R.string.request_timeout), Toast.LENGTH_SHORT).show();
                } else if (this.orderID.contains("Error")) {
                    Toast.makeText(getApplicationContext(), this.orderID.replaceAll("Error", "").trim(), Toast.LENGTH_SHORT).show();
                } else {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentDetails.this);
                    alertDialogBuilder.setTitle("Message")
                            .setMessage("Request " + tokens[1].toString() + " has been created")
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put(db.KEY_IS_DELIVERED, "true");
                                    HashMap<String, String> filter = new HashMap<>();
                                    filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                    filter.put(db.KEY_DELIVERY_NO, delivery.getOrderId());
                                    db.updateData(db.CUSTOMER_DELIVERY_HEADER, map, filter);
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    // create alert dialog
                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
                Intent intent = new Intent(PaymentDetails.this, CustomerDetailActivity.class);
                intent.putExtra("headerObj", object);
                intent.putExtra("msg", "all");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }
    public JSONArray createPrintData(String orderDate,String orderNo,String invoiceAmount){
        JSONArray jArr = new JSONArray();
        try{
            double totalPcs = 0;
            double totalAmount = 0;
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST,App.COLLECTION);
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
                mainArr.put("CUSTOMERID",object.getCustomerID());
                mainArr.put("CUSTOMER", UrlBuilder.decodeString(object.getCustomerName()) + "-" + testAr);
                mainArr.put("ADDRESS",object.getCustomerAddress().equals("")?"-":object.getCustomerAddress());
                mainArr.put("ARBADDRESS",object.getCustomerAddress());
            }
            else{
                mainArr.put("CUSTOMERID",customerNo);
                mainArr.put("CUSTOMER", UrlBuilder.decodeString(customerName) + "-" + testAr);
                mainArr.put("ADDRESS",customerAddress);
                mainArr.put("ARBADDRESS",customerAddress);
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
        if(from.equals("collection")){
            Intent intent1 = new Intent(PaymentDetails.this, CollectionsActivity.class);
            intent1.putExtra("headerObj", object);
            startActivity(intent1);
        }
        else if(from.equals("delivery")){
            Intent intent = new Intent(PaymentDetails.this, DeliveryActivity.class);
            intent.putExtra("headerObj", object);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(PaymentDetails.this, CustomerDetailActivity.class);
            intent.putExtra("headerObj", object);
            intent.putExtra("msg", "visit");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }
}
