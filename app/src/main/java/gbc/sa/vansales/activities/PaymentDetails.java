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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import gbc.sa.vansales.models.Reasons;
import gbc.sa.vansales.models.Sales;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
public class PaymentDetails extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    TextView tv_due_amt, tv_total_amount, tv_date;
    ImageView iv_cal;
    EditText edt_check_no, edt_check_amt, edt_cash_amt;
    Spinner sp_item;
    Button btn_edit1, btn_edit2;
    Calendar myCalendar;
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

    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    ArrayList<DeliveryItem> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        loadingSpinner = new LoadingSpinner(this);
        Banks.loadData(this);
        tv_due_amt = (TextView) findViewById(R.id.tv_payment__amout_due_number);
        Intent i = this.getIntent();

        sp_item = (Spinner) findViewById(R.id.sp_item);
        sp_item.setEnabled(false);

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
                    tv_cust_detail.setText(customerHeader.getCustomerNo() + " " + customerHeader.getName1());
                } else {
                    tv_cust_detail.setText(object.getCustomerID().toString() + " " + object.getCustomerName().toString());
                }
                /*if (Const.colletionDatas.size() > 0) {
                    amountdue = Const.colletionDatas.get(pos).getAmoutDue();
                    tv_due_amt.setText(amountdue);
                }*/
            } else {
                delivery = (OrderList) i.getParcelableExtra("delivery");
                /*if (object == null) {
                    object = Const.allCustomerdataArrayList.get(Const.customerPosition);
                }*/
                customers = CustomerHeaders.get();
                articles = ArticleHeaders.get();
                CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, object.getCustomerID());
                invoiceAmount = i.getExtras().getString("invoiceamount");
                amountdue = invoiceAmount;
                tv_due_amt.setText(invoiceAmount);
                TextView tv_cust_detail = (TextView) findViewById(R.id.tv_cust_detail);
                if (customerHeader != null) {
                    tv_cust_detail.setText(customerHeader.getCustomerNo() + " " + customerHeader.getName1());
                } else {
                    tv_cust_detail.setText(object.getCustomerID().toString() + " " + object.getCustomerName().toString());
                }
            }
        }
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Payment Details");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equals("collection")) {
                } else {
                    HashMap<String, String> filter = new HashMap<String, String>();
                    filter.put(db.KEY_DELIVERY_NO, delivery.getOrderId());
                    db.deleteData(db.CUSTOMER_DELIVERY_ITEMS_POST, filter);
                }
//                Intent intent = new Intent();
//                intent.putExtra("pos", pos);
//                intent.putExtra("amt", String.valueOf(total_amt));
//                setResult(RESULT_OK, intent);
                finish();
            }
        });
        tv_total_amount = (TextView) findViewById(R.id.tv_total_amt);
        tv_date = (TextView) findViewById(R.id.tv_date);
        iv_cal = (ImageView) findViewById(R.id.image_cal);
        iv_cal.setEnabled(false);
        edt_check_no = (EditText) findViewById(R.id.edt_check_no);
        edt_cash_amt = (EditText) findViewById(R.id.edt_cash_amount);
        edt_check_amt = (EditText) findViewById(R.id.edt_check_amt);
        double cashamt = getcashamt();
        double checkamt = getcheckamt();
        total_amt = cashamt + checkamt;
        tv_total_amount.setText(String.valueOf(total_amt));

        btn_edit1 = (Button) findViewById(R.id.btn_edit1);
        btn_edit2 = (Button) findViewById(R.id.btn_edit2);
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
                    if (from.equals("delivery")) {
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
                                new postData().execute();
                                /*Intent intent = new Intent(PaymentDetails.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();*/
                            }
                        });
                        btn_notprint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(PaymentDetails.this, CustomerDetailActivity.class);
                                intent.putExtra("headerObj", object);
                                intent.putExtra("msg", "visit");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });
                    }

                    else if (from.equals("collection")){
                        HashMap<String,String>map = new HashMap<String, String>();
                        map.put(db.KEY_AMOUNT_CLEARED,"");
                        map.put(db.KEY_CASH_AMOUNT,"");
                        map.put(db.KEY_CHEQUE_AMOUNT,"");
                        map.put(db.KEY_CHEQUE_NUMBER,"");
                        map.put(db.KEY_CHEQUE_DATE,"");
                        map.put(db.KEY_CHEQUE_BANK_NAME,"");
                        HashMap<String,String>filter = new HashMap<String, String>();
                        filter.put(db.KEY_INVOICE_NO,collection.getInvoiceNo());
                        Cursor c = db.getData(db.COLLECTION,map,filter);
                        float prevAmount = 0;
                        float prevCashAmount = 0;
                        float prevCheqAmount = 0;
                        String chequeNumber = "";
                        String chequeDate = "";
                        String bankName = "";
                        if(c.getCount()>0){
                            c.moveToFirst();
                            prevAmount = Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                            prevCashAmount = Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CASH_AMOUNT)));
                            prevCheqAmount = Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT)));
                            chequeNumber = c.getString(c.getColumnIndex(db.KEY_CHEQUE_NUMBER));
                            chequeDate = c.getString(c.getColumnIndex(db.KEY_CHEQUE_DATE));
                            bankName = c.getString(c.getColumnIndex(db.KEY_CHEQUE_BANK_NAME));
                        }
                        prevAmount+=Float.parseFloat(tv_total_amount.getText().toString());
                        prevCashAmount+= getcashamt();
                        prevCheqAmount+= getcheckamt();
                        chequeNumber = chequeNumber + "," + edt_check_no.getText().toString();
                        chequeDate = chequeDate + "," + tv_date.getText().toString();
                        HashMap<String,String>updateMap = new HashMap<String, String>();
                        updateMap.put(db.KEY_AMOUNT_CLEARED,String.valueOf(prevAmount));
                        updateMap.put(db.KEY_CHEQUE_NUMBER, chequeNumber);
                        updateMap.put(db.KEY_CASH_AMOUNT,String.valueOf(prevCashAmount));
                        updateMap.put(db.KEY_CHEQUE_AMOUNT,String.valueOf(prevCheqAmount));
                        if(Float.parseFloat(tv_total_amount.getText().toString())==Float.parseFloat(amountdue)){
                            updateMap.put(db.KEY_IS_INVOICE_COMPLETE,App.INVOICE_COMPLETE);
                        }
                        else{
                            updateMap.put(db.KEY_IS_INVOICE_COMPLETE,App.INVOICE_PARTIAL);
                        }
                        db.updateData(db.COLLECTION, updateMap, filter);
                        Intent intent1 = new Intent(PaymentDetails.this, CollectionsActivity.class);
                        intent1.putExtra("headerObj", object);
                        startActivity(intent1);
                    }
                    else {
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
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
            }
        });
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        tv_date.setText(sdf.format(myCalendar.getTime()));
    }
    public double getcashamt() {
        if (edt_cash_amt.getText().toString().equals("")) {
            return 0;
        }
        return Double.parseDouble(edt_cash_amt.getText().toString());
    }
    public double getcheckamt() {
        if (edt_check_amt.getText().toString().equals("")) {
            return 0;
        }
        return Double.parseDouble(edt_check_amt.getText().toString());
    }
    private void loadBanks(){
        Banks.loadData(PaymentDetails.this);
        banksList = Banks.get();
        Log.e("Bank List", "" + banksList.size());
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
                    if (articleHeader.getBaseUOM().equals(App.CASE_UOM)||articleHeader.getBaseUOM().equals(App.BOTTLES_UOM)) {
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
                    }
                    else {
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
        } catch (Exception e) {
            e.printStackTrace();
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
            Log.e("Order ID", "" + this.orderID);

            HashMap<String,String> map = new HashMap<String, String>();
            map.put(db.KEY_TIME_STAMP, "");
            map.put(db.KEY_DELIVERY_NO,"");
            map.put(db.KEY_ITEM_NO,"");
            map.put(db.KEY_MATERIAL_NO ,"");
            map.put(db.KEY_MATERIAL_DESC1,"");
            map.put(db.KEY_CASE ,"");
            map.put(db.KEY_UNIT ,"");
            map.put(db.KEY_UOM,"");
            map.put(db.KEY_AMOUNT, "");
            map.put(db.KEY_ORDER_ID,"");
            map.put(db.KEY_PURCHASE_NUMBER,"");

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
            if(this.tokens[0].toString().equals(this.tokens[1].toString())){
                for (DeliveryItem item : arrayList) {
                    HashMap<String, String> postmap = new HashMap<String, String>();
                    postmap.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    postmap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                    postmap.put(db.KEY_ORDER_ID,tokens[0].toString());

                    HashMap<String, String> filtermap = new HashMap<>();
                    filtermap.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                   // filtermap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    filtermap.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    filtermap.put(db.KEY_MATERIAL_NO, item.getMaterialNo());
                    filtermap.put(db.KEY_PURCHASE_NUMBER,tokens[1].toString());
                    db.updateData(db.CUSTOMER_DELIVERY_ITEMS_POST, postmap, filtermap);
                }

                if(loadingSpinner.isShowing()){
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

                                Intent intent = new Intent(PaymentDetails.this, DeliveryActivity.class);
                                intent.putExtra("headerObj", object);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                               //  finish();
                            }
                        });
                // create alert dialog
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();

            }
            else{
                for (DeliveryItem item : arrayList) {
                    HashMap<String, String> postmap = new HashMap<String, String>();
                    postmap.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    postmap.put(db.KEY_IS_POSTED, App.DATA_IS_POSTED);
                    postmap.put(db.KEY_ORDER_ID,tokens[0].toString());

                    HashMap<String, String> filtermap = new HashMap<>();
                    filtermap.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                  //  filtermap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    filtermap.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    filtermap.put(db.KEY_MATERIAL_NO, item.getMaterialNo());
                    filtermap.put(db.KEY_PURCHASE_NUMBER,tokens[1].toString());
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
                intent.putExtra("msg","all");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

        }
    }
}
