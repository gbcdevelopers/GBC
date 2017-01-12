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
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.ColletionData;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.DeliveryItem;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.models.OrderList;
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
    String invoiceAmount;
    ArrayList<CustomerHeader> customers;
    ArrayList<ArticleHeader> articles;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;

    ArrayList<DeliveryItem>arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        loadingSpinner = new LoadingSpinner(this);

        tv_due_amt = (TextView) findViewById(R.id.tv_payment__amout_due_number);



        if(getIntent().getExtras()!=null)
        {


            from=getIntent().getStringExtra("msg");
            if(from.equals("collection"))
            {
                pos = getIntent().getIntExtra("pos", 0);

                if (Const.colletionDatas.size() > 0) {
                    amountdue = Const.colletionDatas.get(pos).getAmoutDue();
                    tv_due_amt.setText(amountdue);
                }

            }
            else {
                Intent i = this.getIntent();
                object = (Customer) i.getParcelableExtra("headerObj");
                delivery = (OrderList) i.getParcelableExtra("delivery");

                if(object==null)
                {
                    object=Const.allCustomerdataArrayList.get(Const.customerPosition);
                }
                customers = CustomerHeaders.get();
                articles = ArticleHeaders.get();
                CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, object.getCustomerID());

                invoiceAmount = i.getExtras().getString("invoiceamount");

                amountdue=invoiceAmount;

                tv_due_amt.setText(invoiceAmount);
                TextView tv_cust_detail = (TextView)findViewById(R.id.tv_cust_detail);
                if(customerHeader!=null){
                    tv_cust_detail.setText(customerHeader.getCustomerNo() + " " + customerHeader.getName1());
                }
                else{
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


                if(from.equals("collection"))
                {

                }
                else
                {
                    HashMap<String,String> filter = new HashMap<String, String>();
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
        sp_item = (Spinner) findViewById(R.id.sp_item);
        sp_item.setEnabled(false);
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
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                                Intent intent = new Intent(PaymentDetails.this,CustomerDetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
                        btn_notprint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(PaymentDetails.this,CustomerDetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    public String postData(){
        String orderID = "";
        try{
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
            map.put("PurchaseNum", Helpers.generateNumber(db,ConfigStore.CustomerDeliveryRequest_PR_Type));

            JSONArray deepEntity = new JSONArray();

            HashMap<String, String> itemMap = new HashMap<>();
            itemMap.put(db.KEY_ITEM_NO,"");
            itemMap.put(db.KEY_MATERIAL_NO,"");
            itemMap.put(db.KEY_MATERIAL_DESC1,"");
            itemMap.put(db.KEY_CASE,"");
            itemMap.put(db.KEY_UNIT,"");
            itemMap.put(db.KEY_AMOUNT,"");

            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_DELIVERY_NO,delivery.getOrderId());

            Cursor cursor = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST,itemMap,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                int itemno = 10;
                do{
                    ArticleHeader articleHeader = ArticleHeader.getArticle(articles,cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    if(articleHeader.getBaseUOM().equals(App.CASE_UOM)){
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                        jo.put("Material",cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description",cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant","");
                        jo.put("Quantity",cursor.getString(cursor.getColumnIndex(db.KEY_CASE)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("UoM", App.CASE_UOM);
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("Storagelocation", "");
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno+10;
                        deepEntity.put(jo);
                    }
                    if(articleHeader.getBaseUOM().equals(App.BOTTLES_UOM)){
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                        jo.put("Material",cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description",cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant","");
                        jo.put("Quantity",cursor.getString(cursor.getColumnIndex(db.KEY_UNIT)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("UoM", App.BOTTLES_UOM);
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("Storagelocation", "");
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno+10;
                        deepEntity.put(jo);
                    }
                }
                while (cursor.moveToNext());
            }
            orderID = IntegrationService.postData(PaymentDetails.this, App.POST_COLLECTION, map, deepEntity);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return orderID;

    }

    public class
    postData extends AsyncTask<Void, Void, Void> {
        private ArrayList<String>returnList;
        private String orderID = "";
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            //this.returnList = IntegrationService.RequestToken(LoadRequestActivity.this);
            this.orderID = postData();

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

            Log.e("Order ID", "" + this.orderID);



            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            if(this.orderID.isEmpty()||this.orderID.equals("")||this.orderID==null){
               // Toast.makeText(getApplicationContext(), getString(R.string.request_timeout), Toast.LENGTH_SHORT).show();
            }
            else if(this.orderID.contains("Error")){
                Toast.makeText(getApplicationContext(), this.orderID.replaceAll("Error","").trim(), Toast.LENGTH_SHORT).show();
            }
            else{
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentDetails.this);
                alertDialogBuilder.setTitle("Message")
                        .setMessage("Request " + this.orderID + " has been created")
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap<String,String> map = new HashMap<String, String>();
                                map.put(db.KEY_IS_DELIVERED,"true");

                                HashMap<String,String> filter = new HashMap<>();
                                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                                filter.put(db.KEY_DELIVERY_NO,delivery.getOrderId());
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
               finish();

        }
    }
}
