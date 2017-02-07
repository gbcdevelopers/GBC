package gbc.sa.vansales.activities;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.models.Sales;
import gbc.sa.vansales.sap.DataListener;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by eheuristic on 12/6/2016.
 */
public class PromotioninfoActivity extends AppCompatActivity implements DataListener {
    ImageView iv_back;
    TextView tv_top_header;
    TextView tv_promotion;
    LinearLayout ll_bottom;
    String str_promotion_message = "";
    Customer object;
    String invoiceAmount;
    String from;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    float totalamnt = 0;
    float discount = 0;
    float goodreturntotal = 0;
    float goodreturndiscount = 0;
    float badreturntotal = 0;
    float badreturndiscount = 0;
    TextView tv_current_invoice;
    ArrayList<Sales> arraylist = new ArrayList<>();
    int count=0;
    int returnCount = 0;
    int referenceCount = 0;
    EditText tv_discount;
    EditText tv_net_invoice;
    OrderList delivery;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        loadingSpinner = new LoadingSpinner(this);
        Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");
        delivery = (OrderList)i.getParcelableExtra("delivery");
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.promo_details));
        tv_promotion = (TextView) findViewById(R.id.tv_promotion);
        tv_current_invoice = (TextView) findViewById(R.id.tv_invoice_amount);
        tv_discount = (EditText)findViewById(R.id.et_discount);
        tv_net_invoice = (EditText)findViewById(R.id.tv_net_invoice);
        if (getIntent().getExtras() != null) {
            str_promotion_message = getIntent().getExtras().getString("msg", "extra Promotion");
            from = getIntent().getExtras().getString("from","");
            tv_promotion.setText(str_promotion_message.substring(0, 1).toUpperCase() + str_promotion_message.substring(1).toLowerCase());
            int pos = getIntent().getIntExtra("pos", 10);
            if (from.equals("Final Invoice")) {
                ll_bottom.setVisibility(View.VISIBLE);
            } else {
                if (from.equals("delivery")) {
                    ll_bottom.setVisibility(View.VISIBLE);
                } else {
            ll_bottom.setVisibility(View.GONE);
                }
            }
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equals("Final Invoice")) {
                    final Dialog dialog = new Dialog(PromotioninfoActivity.this);
                    dialog.setContentView(R.layout.dialog_doprint);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                    LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                    dialog.show();
                    btn_print.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (returnExist("")) {
                                if (returnExist(App.GOOD_RETURN) && returnExist(App.BAD_RETURN)) {
                                    referenceCount = 2;
                                    new postReturns(App.GOOD_RETURN);
                                    new postReturns(App.BAD_RETURN);
                                } else if (returnExist(App.GOOD_RETURN)) {
                                    referenceCount = 1;
                                    new postReturns(App.GOOD_RETURN);
                                } else if (returnExist(App.BAD_RETURN)) {
                                    referenceCount = 1;
                                    new postReturns(App.BAD_RETURN);
                                }
                            } else {
                                new postData().execute();
                                dialog.cancel();
                            }
                        }
                    });
                    btn_notprint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           /* Intent intent = new Intent(PromotioninfoActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();*/
                            if (returnExist("")) {
                                if (returnExist(App.GOOD_RETURN) && returnExist(App.BAD_RETURN)) {
                                    referenceCount = 2;
                                    new postReturns(App.GOOD_RETURN);
                                    new postReturns(App.BAD_RETURN);
                                } else if (returnExist(App.GOOD_RETURN)) {
                                    referenceCount = 1;
                                    new postReturns(App.GOOD_RETURN);
                                } else if (returnExist(App.BAD_RETURN)) {
                                    referenceCount = 1;
                                    new postReturns(App.BAD_RETURN);
                                }
                            } else {
                                new postData().execute();
                                dialog.cancel();
                            }
                        }
                    });
                } else if (from.equals("delivery")) {
                    Intent intent = new Intent(PromotioninfoActivity.this, PaymentDetails.class);
                    intent.putExtra("msg", str_promotion_message);
                    intent.putExtra("from",from);
                    intent.putExtra("headerObj", object);
                    intent.putExtra("delivery", delivery);
                    intent.putExtra("invoiceamount", tv_current_invoice.getText().toString());
                    startActivity(intent);
                    finish();
                } else {
                    if (from.equals("")) {
                        final Dialog dialog = new Dialog(PromotioninfoActivity.this);
                        dialog.setContentView(R.layout.dialog_doprint);
                        dialog.setCancelable(true);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                        LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                        dialog.show();
                        btn_print.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        btn_notprint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(PromotioninfoActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        finish();
                    }
                }
            }
        });
        if(from.equals("Final Invoice")){
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
            map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            if (db.checkData(db.CAPTURE_SALES_INVOICE, map)) {
                new loadData().execute();
            }
            else if(returnExist("")){
                if (returnExist(App.GOOD_RETURN)) {
                    new loadReturns(App.GOOD_RETURN);
                }
                else if(returnExist(App.BAD_RETURN)){
                    new loadReturns(App.BAD_RETURN);
                }
            }
        }
        else{
            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            map.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
            map.put(db.KEY_DELIVERY_NO,delivery.getOrderId());
            if(db.checkData(db.CUSTOMER_DELIVERY_ITEMS_POST,map)){
                new loadDeliveryData().execute();
            }
        }

    }
    private boolean returnExist(String returnType){
        HashMap<String,String>map = new HashMap<>();
        map.put(db.KEY_TIME_STAMP,"");
        HashMap<String,String>filter = new HashMap<>();
        filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
        filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);

        if(returnType.equals("")||returnType==null){

        }
        else{
            filter.put(db.KEY_REASON_TYPE,returnType);
        }

        if(db.checkData(db.RETURNS,filter)){
            return true;
        }
        else{
            return false;
        }
    }
    private boolean invoiceExist(){
        HashMap<String,String>map = new HashMap<>();
        map.put(db.KEY_TIME_STAMP,"");
        HashMap<String,String>filter = new HashMap<>();
        filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
        filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
        if(db.checkData(db.CAPTURE_SALES_INVOICE,filter)){
            return true;
        }
        else{
            return false;
        }
    }
    public void recalculateTotal(Cursor cursor,String from,String source){
        if(from.equals("Final Invoice")){
            if(source.equals("Sales Invoice")){
                Cursor saleCursor = cursor;
                saleCursor.moveToFirst();
                float amount=0;
                do{
                    float tempPrice = 0;
                    HashMap<String,String> filterComp = new HashMap<>();
                    filterComp.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    filterComp.put(db.KEY_MATERIAL_NO, saleCursor.getString(saleCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    HashMap<String,String> map = new HashMap<>();
                    map.put(db.KEY_MATERIAL_NO,"");
                    map.put(db.KEY_AMOUNT,"");
                    if(db.checkData(db.PRICING,filterComp)){
                        Cursor customerPriceCursor = db.getData(db.PRICING,map,filterComp);
                        if(customerPriceCursor.getCount()>0){
                            customerPriceCursor.moveToFirst();
                            tempPrice = Float.parseFloat(customerPriceCursor.getString(customerPriceCursor.getColumnIndex(db.KEY_AMOUNT)));
                        }
                        if(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                            amount += tempPrice*Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_ORG_CASE)));
                            //amount += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        }
                        else {
                            amount += tempPrice*Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_ORG_UNITS)));
                        }

                    }
                    else{
                        if(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                            amount += Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_AMOUNT)))*Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_ORG_CASE)));
                            //amount += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        }
                        else {
                            amount += Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_AMOUNT)))*Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_ORG_UNITS)));
                        }
                    }

                }
                while(saleCursor.moveToNext());
                totalamnt = amount;
            }
            else if(source.equals(App.GOOD_RETURN)){
                Cursor saleCursor = cursor;
                saleCursor.moveToFirst();
                float amount=0;
                do{
                    float tempPrice = 0;
                    HashMap<String,String> filterComp = new HashMap<>();
                    filterComp.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    filterComp.put(db.KEY_MATERIAL_NO, saleCursor.getString(saleCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    HashMap<String,String> map = new HashMap<>();
                    map.put(db.KEY_MATERIAL_NO,"");
                    map.put(db.KEY_AMOUNT,"");
                    if(db.checkData(db.PRICING,filterComp)){
                        Cursor customerPriceCursor = db.getData(db.PRICING,map,filterComp);
                        if(customerPriceCursor.getCount()>0){
                            customerPriceCursor.moveToFirst();
                            tempPrice = Float.parseFloat(customerPriceCursor.getString(customerPriceCursor.getColumnIndex(db.KEY_AMOUNT)));
                        }
                        if(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                            amount += tempPrice*Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_CASE)));
                            //amount += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        }
                        else {
                            amount += tempPrice*Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UNIT)));
                        }

                    }
                    else{
                        if(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                            amount += Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_PRICE)))*Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_CASE)));
                            //amount += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        }
                        else {
                            amount += Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_PRICE)))*Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UNIT)));
                        }
                    }

                }
                while(saleCursor.moveToNext());
                goodreturntotal = amount;

            }
            else if(source.equals(App.BAD_RETURN)){
                Cursor saleCursor = cursor;
                saleCursor.moveToFirst();
                float amount=0;
                do{
                    float tempPrice = 0;
                    HashMap<String,String> filterComp = new HashMap<>();
                    filterComp.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    filterComp.put(db.KEY_MATERIAL_NO, saleCursor.getString(saleCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    HashMap<String,String> map = new HashMap<>();
                    map.put(db.KEY_MATERIAL_NO,"");
                    map.put(db.KEY_AMOUNT,"");
                    if(db.checkData(db.PRICING,filterComp)){
                        Cursor customerPriceCursor = db.getData(db.PRICING,map,filterComp);
                        if(customerPriceCursor.getCount()>0){
                            customerPriceCursor.moveToFirst();
                            tempPrice = Float.parseFloat(customerPriceCursor.getString(customerPriceCursor.getColumnIndex(db.KEY_AMOUNT)));
                        }
                        if(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                            amount += tempPrice*Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_CASE)));
                            //amount += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        }
                        else {
                            amount += tempPrice*Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UNIT)));
                        }

                    }
                    else{
                        if(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                            amount += Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_PRICE)))*Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_CASE)));
                            //amount += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        }
                        else {
                            amount += Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_PRICE)))*Float.parseFloat(saleCursor.getString(saleCursor.getColumnIndex(db.KEY_UNIT)));
                        }
                    }

                }
                while(saleCursor.moveToNext());
                badreturntotal = amount;
            }

        }
        else{
            Cursor deliveryCursor = cursor;
            deliveryCursor.moveToFirst();
            do{
                float tempPrice = 0;
                HashMap<String,String> filterComp = new HashMap<>();
                filterComp.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                filterComp.put(db.KEY_MATERIAL_NO, deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                HashMap<String,String> map = new HashMap<>();
                map.put(db.KEY_MATERIAL_NO,"");
                map.put(db.KEY_AMOUNT,"");
                if(db.checkData(db.PRICING,filterComp)){
                    Cursor customerPriceCursor = db.getData(db.PRICING,map,filterComp);
                    if(customerPriceCursor.getCount()>0){
                        customerPriceCursor.moveToFirst();
                        tempPrice = Float.parseFloat(customerPriceCursor.getString(customerPriceCursor.getColumnIndex(db.KEY_AMOUNT)));
                    }
                    if(deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM_NEW)||deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                        totalamnt += tempPrice*Float.parseFloat(deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_CASE)));
                        //amount += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                    }


                }
                else{
                    if(deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM_NEW)||deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                        totalamnt += Float.parseFloat(deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_AMOUNT)))*Float.parseFloat(deliveryCursor.getString(deliveryCursor.getColumnIndex(db.KEY_CASE)));
                        //amount += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                    }

                }

            }
            while(deliveryCursor.moveToNext());
        }

    }
    @Override
    public void onProcessingComplete() {
        tv_discount.setText(String.valueOf(discount + goodreturndiscount + badreturndiscount));
        tv_net_invoice.setText(String.valueOf(totalamnt + discount - goodreturntotal + goodreturndiscount - badreturntotal + badreturndiscount));
    }
    @Override
    public void onProcessingComplete(String source) {
        if(source.equals("Invoice")){
            if (returnExist("")) {
                if (returnExist(App.GOOD_RETURN)) {
                    new loadReturns(App.GOOD_RETURN);
                }
                else if(returnExist(App.BAD_RETURN)){
                    new loadReturns(App.BAD_RETURN);
                }
            }
            else{
                tv_discount.setText(String.valueOf(discount + goodreturndiscount + badreturndiscount));
                tv_net_invoice.setText(String.valueOf(totalamnt + discount - goodreturntotal + goodreturndiscount - badreturntotal + badreturndiscount));
            }
        }
        else if(source.equals(App.GOOD_RETURN)){
            if (returnExist(App.BAD_RETURN)) {
                new loadReturns(App.BAD_RETURN);
            }
            else{
                tv_discount.setText(String.valueOf(discount + goodreturndiscount + badreturndiscount));
                tv_net_invoice.setText(String.valueOf(totalamnt + discount - goodreturntotal + goodreturndiscount - badreturntotal + badreturndiscount));
            }
        }
        else if(source.equals(App.BAD_RETURN)){
            tv_discount.setText(String.valueOf(discount + goodreturndiscount + badreturndiscount));
            tv_net_invoice.setText(String.valueOf(totalamnt + discount - goodreturntotal + goodreturndiscount - badreturntotal + badreturndiscount));
        }
    }
    public class loadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO,"");
            map.put(db.KEY_ORG_CASE, "");
            map.put(db.KEY_ORG_UNITS, "");
            map.put(db.KEY_AMOUNT, "");
            map.put(db.KEY_UOM,"");
            map.put(db.KEY_MATERIAL_NO,"");
            HashMap<String,String>filter = new HashMap<>();
            filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            Cursor cursor = db.getData(db.CAPTURE_SALES_INVOICE, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                recalculateTotal(cursor,from,"Sales Invoice");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            new loadPromotions(App.Promotions02,"Invoice");
            new loadPromotions(App.Promotions05,"Invoice");
            new loadPromotions(App.Promotions07,"Invoice");

            tv_current_invoice.setText(String.valueOf(totalamnt));
        }
    }
    public class loadReturns extends AsyncTask<Void, Void, Void> {
        private String returnType = "";
        private loadReturns(String returnType){
            this.returnType = returnType;
            execute();
        }

        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_TIME_STAMP,"");
            map.put(db.KEY_TRIP_ID,"");
            map.put(db.KEY_CUSTOMER_NO,"");
            map.put(db.KEY_REASON_TYPE, "");
            map.put(db.KEY_REASON_CODE,"");
            map.put(db.KEY_ITEM_NO,"");
            map.put(db.KEY_MATERIAL_DESC1,"");
            map.put(db.KEY_MATERIAL_NO,"");
            map.put(db.KEY_MATERIAL_GROUP,"");
            map.put(db.KEY_CASE,"");
            map.put(db.KEY_UNIT,"");
            map.put(db.KEY_UOM,"");
            map.put(db.KEY_PRICE,"");
            map.put(db.KEY_ORDER_ID,"");
            map.put(db.KEY_PURCHASE_NUMBER,"");
            map.put(db.KEY_IS_POSTED,"");
            map.put(db.KEY_IS_PRINTED,"");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            filter.put(db.KEY_REASON_TYPE,this.returnType);
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            Cursor cursor = db.getData(db.RETURNS, map, filter);
            recalculateTotal(cursor,from,this.returnType);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            new loadPromotions(App.Promotions02,this.returnType);
            new loadPromotions(App.Promotions05,this.returnType);
            new loadPromotions(App.Promotions07,this.returnType);
            tv_current_invoice.setText(String.valueOf(totalamnt-goodreturntotal-badreturntotal));
        }
    }
    public class loadDeliveryData extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO,"");
            map.put(db.KEY_CASE, "");
            map.put(db.KEY_UNIT, "");
            map.put(db.KEY_AMOUNT, "");
            map.put(db.KEY_UOM,"");
            map.put(db.KEY_MATERIAL_NO,"");
            HashMap<String,String>filter = new HashMap<>();
            filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            filter.put(db.KEY_DELIVERY_NO,delivery.getOrderId());
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            Cursor cursor = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                recalculateTotal(cursor,from,"Delivery");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            new loadPromotions(App.Promotions02,"Delivery");
            new loadPromotions(App.Promotions05,"Delivery");
            new loadPromotions(App.Promotions07,"Delivery");



            tv_current_invoice.setText(String.valueOf(totalamnt));
        }
    }
    public class postReturns extends AsyncTask<Void, Void, Void> {
        private ArrayList<String> returnList;
        private String orderID = "";
        private String[] tokens = new String[2];
        private String returnType = "";

        private postReturns(String returnType) {
            this.returnType = returnType;
            execute();
        }

        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            //this.returnList = IntegrationService.RequestToken(LoadRequestActivity.this);
            this.orderID = postReturns(this.returnType);
            this.tokens = orderID.split(",");
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("Order ID Post Returns", "" + this.orderID);
            returnCount++;
            arraylist.clear();
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_TIME_STAMP,"");
            map.put(db.KEY_TRIP_ID,"");
            map.put(db.KEY_CUSTOMER_NO,"");
            map.put(db.KEY_REASON_TYPE, "");
            map.put(db.KEY_REASON_CODE, "");
            map.put(db.KEY_ITEM_NO,"");
            map.put(db.KEY_MATERIAL_DESC1,"");
            map.put(db.KEY_MATERIAL_NO,"");
            map.put(db.KEY_MATERIAL_GROUP,"");
            map.put(db.KEY_CASE,"");
            map.put(db.KEY_UNIT,"");
            map.put(db.KEY_UOM,"");
            map.put(db.KEY_PRICE,"");
            map.put(db.KEY_ORDER_ID,"");
            map.put(db.KEY_PURCHASE_NUMBER,"");
            map.put(db.KEY_IS_POSTED,"");
            map.put(db.KEY_IS_PRINTED,"");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            filter.put(db.KEY_REASON_TYPE,this.returnType);
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            Cursor cursor = db.getData(db.RETURNS, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Sales sale = new Sales();
                    sale.setMaterial_no(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    sale.setMaterial_description(UrlBuilder.decodeString(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                    sale.setPic(cursor.getString(cursor.getColumnIndex(db.KEY_UNIT)));
                    sale.setCases(cursor.getString(cursor.getColumnIndex(db.KEY_CASE)));
                    sale.setUom(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                    arraylist.add(sale);
                }
                while (cursor.moveToNext());
            }
            if(this.tokens[0].toString().equals(this.tokens[1].toString())){
                for (Sales sale : arraylist) {
                    HashMap<String, String> postmap = new HashMap<String, String>();
                    postmap.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    postmap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                    postmap.put(db.KEY_ORDER_ID,tokens[0].toString());

                    HashMap<String, String> filtermap = new HashMap<>();
                    filtermap.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    filtermap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    filtermap.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    filtermap.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                    filtermap.put(db.KEY_PURCHASE_NUMBER,tokens[1].toString());
                    filtermap.put(db.KEY_REASON_TYPE,this.returnType);
                    db.updateData(db.RETURNS, postmap, filtermap);
                }

                if(returnCount==referenceCount){
                    if(invoiceExist()){
                        if(loadingSpinner.isShowing()){
                            loadingSpinner.hide();
                        }
                        if(returnType.equals(App.GOOD_RETURN)){
                            updateStockinVan(true);
                        }
                        new postData().execute();
                    }
                    else{
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PromotioninfoActivity.this);
                        alertDialogBuilder.setTitle(getString(R.string.message))
                                .setMessage(getString(R.string.request_created))
                                //.setMessage("Request with reference " + tokens[0].toString() + " has been saved")
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (returnType.equals(App.GOOD_RETURN)) {
                                            updateStockinVan(true);
                                        }
                                        dialog.dismiss();
                                        if(object.getPaymentMethod().equalsIgnoreCase(App.CREDIT_CUSTOMER)){
                                            Intent intent1 = new Intent(PromotioninfoActivity.this, CustomerDetailActivity.class);
                                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent1.putExtra("headerObj", object);
                                            intent1.putExtra("msg", "all");
                                            startActivity(intent1);
                                            finish();
                                        }
                                        else if(object.getPaymentMethod().equalsIgnoreCase(App.CASH_CUSTOMER)){
                                            //Go to payment details screen
                                            Intent intent = new Intent(PromotioninfoActivity.this, PaymentDetails.class);
                                            intent.putExtra("msg", str_promotion_message);
                                            intent.putExtra("from",from);
                                            intent.putExtra("headerObj", object);
                                            intent.putExtra("amountdue", tv_net_invoice.getText().toString());
                                            startActivity(intent);
                                            finish();
                                        }
                                        else if(object.getPaymentMethod().equalsIgnoreCase(App.TC_CUSTOMER)){
                                            Intent intent1 = new Intent(PromotioninfoActivity.this, CustomerDetailActivity.class);
                                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent1.putExtra("headerObj", object);
                                            intent1.putExtra("msg", "all");
                                            startActivity(intent1);
                                            finish();
                                        }

                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                    }
                }
                else{
                    if(returnType.equals(App.GOOD_RETURN)){
                        updateStockinVan(true);
                    }
                }


            }
            else{
                for (Sales sale : arraylist) {
                    HashMap<String, String> postmap = new HashMap<String, String>();
                    postmap.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    postmap.put(db.KEY_IS_POSTED, App.DATA_IS_POSTED);
                    postmap.put(db.KEY_ORDER_ID,tokens[0].toString());

                    HashMap<String, String> filtermap = new HashMap<>();
                    filtermap.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    filtermap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    filtermap.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    filtermap.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                    filtermap.put(db.KEY_PURCHASE_NUMBER,tokens[1].toString());
                    filtermap.put(db.KEY_REASON_TYPE,this.returnType);
                    db.updateData(db.RETURNS, postmap, filtermap);
                }
                Log.e("Return Count","" + returnCount +"/" +  referenceCount);
                if(returnCount==referenceCount){
                    if(invoiceExist()){
                        if(loadingSpinner.isShowing()){
                            loadingSpinner.hide();
                        }
                        if(returnType.equals(App.GOOD_RETURN)){
                            updateStockinVan(true);
                        }
                        new postData().execute();
                    }
                    else{
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PromotioninfoActivity.this);
                        alertDialogBuilder.setTitle("Message")
                                .setMessage("Request " + tokens[1].toString() + " has been created")
                                        // .setMessage("Request " + tokens[0].toString() + " has been created")
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (returnType.equals(App.GOOD_RETURN)) {
                                            updateStockinVan(true);
                                        }
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                    }
                }

                if(loadingSpinner.isShowing()){
                    loadingSpinner.hide();
                }

                if(this.orderID.isEmpty()||this.orderID.equals("")||this.orderID==null){
                    Toast.makeText(getApplicationContext(),getString(R.string.request_timeout),Toast.LENGTH_SHORT ).show();
                }
                else if(this.orderID.contains("Error")){
                    Toast.makeText(getApplicationContext(), this.orderID.replaceAll("Error","").trim(), Toast.LENGTH_SHORT).show();
                }
                else{

                    // show it
                  //  alertDialog.show();
                }
            }
        }
    }
    public String postReturns(String returnType) {
        String orderID = "";
        String purchaseNumber = "";

        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("Function", ConfigStore.ReturnsFunction);
            map.put("OrderId", "");
            map.put("DocumentType", returnType.equals(App.GOOD_RETURN)?ConfigStore.GoodReturnType:ConfigStore.BadReturnType);
            // map.put("DocumentDate", Helpers.formatDate(new Date(),App.DATE_FORMAT_WO_SPACE));
            // map.put("DocumentDate", null);
            map.put("CustomerId", object.getCustomerID());
            map.put("SalesOrg", Settings.getString(App.SALES_ORG));
            map.put("DistChannel", Settings.getString(App.DIST_CHANNEL));
            map.put("Division", Settings.getString(App.DIVISION));
            map.put("OrderValue", String.valueOf(totalamnt));
            map.put("Currency", "SAR");
            // map.put("PurchaseNum", Helpers.generateNumber(db, ConfigStore.InvoiceRequest_PR_Type));
            JSONArray deepEntity = new JSONArray();
            HashMap<String, String> itemMap = new HashMap<>();
            itemMap.put(db.KEY_ITEM_NO, "");
            itemMap.put(db.KEY_MATERIAL_NO, "");
            itemMap.put(db.KEY_MATERIAL_DESC1, "");
            itemMap.put(db.KEY_CASE, "");
            itemMap.put(db.KEY_UNIT, "");
            itemMap.put(db.KEY_UOM, "");
            itemMap.put(db.KEY_PRICE, "");
            itemMap.put(db.KEY_ORDER_ID,"");
            itemMap.put(db.KEY_REASON_CODE,"");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            filter.put(db.KEY_REASON_TYPE,returnType);
            Cursor cursor = db.getData(db.RETURNS, itemMap, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                map.put("PurchaseNum", cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID)));
                map.put("OrdReason",cursor.getString(cursor.getColumnIndex(db.KEY_REASON_CODE)));
                purchaseNumber = map.get("PurchaseNum");
                int itemno = 10;
                do {
                    if (cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)) {
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                        jo.put("Material", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant", App.PLANT);
                        jo.put("Quantity", cursor.getString(cursor.getColumnIndex(db.KEY_CASE)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("UoM",cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("Storagelocation", App.STORAGE_LOCATION);
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno + 10;
                        deepEntity.put(jo);
                    }
                    else{
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                        jo.put("Material", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant", App.PLANT);
                        jo.put("Quantity", cursor.getString(cursor.getColumnIndex(db.KEY_UNIT)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("UoM", cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("Storagelocation", App.STORAGE_LOCATION);
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno + 10;
                        deepEntity.put(jo);
                    }
                }
                while (cursor.moveToNext());
            }
            orderID = IntegrationService.postData(PromotioninfoActivity.this, App.POST_COLLECTION, map, deepEntity);
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
            Log.e("Order ID Post Data", "" + this.orderID);
            arraylist.clear();
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO, "");
            map.put(db.KEY_ITEM_NO, "");
            map.put(db.KEY_ITEM_CATEGORY, "");
            map.put(db.KEY_MATERIAL_NO, "");
            map.put(db.KEY_MATERIAL_GROUP, "");
            map.put(db.KEY_MATERIAL_DESC1, "");
            map.put(db.KEY_ORG_CASE, "");
            map.put(db.KEY_UOM, "");
            map.put(db.KEY_ORG_UNITS, "");
            map.put(db.KEY_AMOUNT, "");
            map.put(db.KEY_ORDER_ID,"");
            map.put(db.KEY_PURCHASE_NUMBER,"");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            Cursor cursor = db.getData(db.CAPTURE_SALES_INVOICE, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Sales sale = new Sales();
                    sale.setMaterial_no(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    sale.setMaterial_description(UrlBuilder.decodeString(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                    sale.setPic(cursor.getString(cursor.getColumnIndex(db.KEY_ORG_UNITS)));
                    sale.setCases(cursor.getString(cursor.getColumnIndex(db.KEY_ORG_CASE)));
                    sale.setUom(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                    arraylist.add(sale);
                }
                while (cursor.moveToNext());
            }
            if(this.tokens[0].toString().equals(this.tokens[1].toString())){
                for (Sales sale : arraylist) {
                    HashMap<String, String> postmap = new HashMap<String, String>();
                    postmap.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    postmap.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                    postmap.put(db.KEY_ORDER_ID,tokens[0].toString());
                    HashMap<String, String> filtermap = new HashMap<>();
                    filtermap.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                   // filtermap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    filtermap.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    filtermap.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                    filtermap.put(db.KEY_PURCHASE_NUMBER,tokens[1].toString());
                    db.updateData(db.CAPTURE_SALES_INVOICE, postmap, filtermap);
                }

                //Creating an invoice for customer
                if(object.getPaymentMethod().equals(App.CREDIT_CUSTOMER)||object.getPaymentMethod().equals(App.TC_CUSTOMER)){
                    HashMap<String,String>invoiceMap = new HashMap<>();
                    invoiceMap.put(db.KEY_COLLECTION_TYPE,App.COLLECTION_INVOICE);
                    invoiceMap.put(db.KEY_CUSTOMER_TYPE,object.getPaymentMethod());
                    invoiceMap.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                    invoiceMap.put(db.KEY_INVOICE_NO,tokens[0].toString());
                    invoiceMap.put(db.KEY_INVOICE_AMOUNT,tv_net_invoice.getText().toString());
                    invoiceMap.put(db.KEY_INVOICE_DATE,Helpers.formatDate(new Date(), App.DATE_FORMAT));
                    invoiceMap.put(db.KEY_AMOUNT_CLEARED,"0");
                    invoiceMap.put(db.KEY_CHEQUE_AMOUNT,"0");
                    invoiceMap.put(db.KEY_CHEQUE_NUMBER,"0000");
                    invoiceMap.put(db.KEY_CHEQUE_DATE,"0000");
                    invoiceMap.put(db.KEY_CHEQUE_BANK_CODE,"0000");
                    invoiceMap.put(db.KEY_CHEQUE_BANK_NAME,"0000");
                    invoiceMap.put(db.KEY_CASH_AMOUNT,"0");
                    invoiceMap.put(db.KEY_IS_INVOICE_COMPLETE,App.INVOICE_INCOMPLETE);
                    invoiceMap.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    invoiceMap.put(db.KEY_IS_PRINTED,App.DATA_NOT_POSTED);
                    db.addData(db.COLLECTION, invoiceMap);
                    Log.e("Going on","Foo");
                    HashMap<String,String>logMap = new HashMap<>();
                    logMap.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
                    logMap.put(db.KEY_ACTIVITY_TYPE, App.ACTIVITY_INVOICE);
                    logMap.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                    logMap.put(db.KEY_ORDER_ID,tokens[0].toString());
                    logMap.put(db.KEY_PRICE,tv_net_invoice.getText().toString());
                    db.addData(db.DAYACTIVITY,logMap);
                }


                if(loadingSpinner.isShowing()){
                    loadingSpinner.hide();
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PromotioninfoActivity.this);
                alertDialogBuilder/*.setTitle("Message")*/
                        .setMessage(getString(R.string.request_created))
                        //.setMessage("Request with reference " + tokens[0].toString() + " has been saved")
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateStockinVan(false);
                                if(Helpers.isNetworkAvailable(PromotioninfoActivity.this)){
                                    Helpers.createBackgroundJob(getApplicationContext());
                                }
                                dialog.dismiss();
                               /* Intent intent = new Intent(PromotioninfoActivity.this,SalesInvoiceOptionActivity.class);
                                intent.putExtra("from", "customerdetail");
                                intent.putExtra("headerObj", object);*/
                                if(object.getPaymentMethod().equalsIgnoreCase(App.CREDIT_CUSTOMER)){
                                    Intent intent1 = new Intent(PromotioninfoActivity.this, CustomerDetailActivity.class);
                                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent1.putExtra("headerObj", object);
                                    intent1.putExtra("msg", "all");
                                    startActivity(intent1);
                                    finish();
                                }
                                else if(object.getPaymentMethod().equalsIgnoreCase(App.CASH_CUSTOMER)){
                                    //Go to payment details screen
                                    Intent intent = new Intent(PromotioninfoActivity.this, PaymentDetails.class);
                                    intent.putExtra("msg", str_promotion_message);
                                    intent.putExtra("from",from);
                                    intent.putExtra("headerObj", object);
                                    intent.putExtra("amountdue", tv_net_invoice.getText().toString());
                                    startActivity(intent);
                                    finish();
                                }
                                else if(object.getPaymentMethod().equalsIgnoreCase(App.TC_CUSTOMER)){
                                    Intent intent1 = new Intent(PromotioninfoActivity.this, CustomerDetailActivity.class);
                                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent1.putExtra("headerObj", object);
                                    intent1.putExtra("msg", "all");
                                    startActivity(intent1);
                                    finish();
                                }

                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
            else{
                for (Sales sale : arraylist) {
                    HashMap<String, String> postmap = new HashMap<String, String>();
                    postmap.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    postmap.put(db.KEY_IS_POSTED, App.DATA_IS_POSTED);
                    postmap.put(db.KEY_ORDER_ID,tokens[0].toString());

                    HashMap<String, String> filtermap = new HashMap<>();
                    filtermap.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    filtermap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    filtermap.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    filtermap.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                    filtermap.put(db.KEY_PURCHASE_NUMBER,tokens[1].toString());
                    db.updateData(db.CAPTURE_SALES_INVOICE, postmap, filtermap);
                }

                if(loadingSpinner.isShowing()){
                    loadingSpinner.hide();
                }

                if(this.orderID.isEmpty()||this.orderID.equals("")||this.orderID==null){
                    Toast.makeText(getApplicationContext(),getString(R.string.request_timeout),Toast.LENGTH_SHORT ).show();
                }
                else if(this.orderID.contains("Error")){
                    Toast.makeText(getApplicationContext(), this.orderID.replaceAll("Error","").trim(), Toast.LENGTH_SHORT).show();
                }
                else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PromotioninfoActivity.this);
                    alertDialogBuilder.setTitle("Message")
                            .setMessage("Request " + tokens[1].toString() + " has been created")
                           // .setMessage("Request " + tokens[0].toString() + " has been created")
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    updateStockinVan(false);
                                    dialog.dismiss();
                                    finish();
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
    public String postData() {
        String orderID = "";
        String purchaseNumber = "";

        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("Function", ConfigStore.InvoiceRequestFunction);
            map.put("OrderId", "");
            map.put("DocumentType", ConfigStore.InvoiceDocumentType);
            // map.put("DocumentDate", Helpers.formatDate(new Date(),App.DATE_FORMAT_WO_SPACE));
            // map.put("DocumentDate", null);
            map.put("CustomerId", object.getCustomerID());
            map.put("SalesOrg", Settings.getString(App.SALES_ORG));
            map.put("DistChannel", Settings.getString(App.DIST_CHANNEL));
            map.put("Division", Settings.getString(App.DIVISION));
            map.put("OrderValue", String.valueOf(totalamnt));
            map.put("Currency", "SAR");
           // map.put("PurchaseNum", Helpers.generateNumber(db, ConfigStore.InvoiceRequest_PR_Type));
            JSONArray deepEntity = new JSONArray();
            HashMap<String, String> itemMap = new HashMap<>();
            itemMap.put(db.KEY_ITEM_NO, "");
            itemMap.put(db.KEY_MATERIAL_NO, "");
            itemMap.put(db.KEY_MATERIAL_DESC1, "");
            itemMap.put(db.KEY_ORG_CASE, "");
            itemMap.put(db.KEY_ORG_UNITS, "");
            itemMap.put(db.KEY_UOM, "");
            itemMap.put(db.KEY_AMOUNT, "");
            itemMap.put(db.KEY_ORDER_ID,"");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            Cursor cursor = db.getData(db.CAPTURE_SALES_INVOICE, itemMap, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                map.put("PurchaseNum", cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID)));
                purchaseNumber = map.get("PurchaseNum");
                int itemno = 10;
                do {
                    if (cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)) {
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                        jo.put("Material", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant", App.PLANT);
                        jo.put("Quantity", cursor.getString(cursor.getColumnIndex(db.KEY_ORG_CASE)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("UoM", cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("Storagelocation", App.STORAGE_LOCATION);
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno + 10;
                        deepEntity.put(jo);
                    }
                    else {
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                        jo.put("Material", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant", App.PLANT);
                        jo.put("Quantity", cursor.getString(cursor.getColumnIndex(db.KEY_ORG_UNITS)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("UoM", cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("Storagelocation", App.STORAGE_LOCATION);
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno + 10;
                        deepEntity.put(jo);
                    }
                }
                while (cursor.moveToNext());
            }
            orderID = IntegrationService.postData(PromotioninfoActivity.this, App.POST_COLLECTION, map, deepEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderID + "," + purchaseNumber;
    }
    public void updateStockinVan(boolean add) {
        if(add){
            loadingSpinner.show();
            //Log.e("ArrayList Size", "" + arraylist.size());
            for (Sales sale : arraylist) {
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_MATERIAL_NO, "");
                map.put(db.KEY_REMAINING_QTY_CASE, "");
                map.put(db.KEY_REMAINING_QTY_UNIT, "");
                HashMap<String, String> filter = new HashMap<>();
                //Log.e("Filter MN", "" + sale.getMaterial_no());
                filter.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                Cursor cursor = db.getData(db.VAN_STOCK_ITEMS, map, filter);
                //Log.e("Cursor count", "" + cursor.getCount());
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        HashMap<String, String> updateDataMap = new HashMap<>();
                        float remainingCase = 0;
                        float remainingUnit = 0;
                        remainingCase = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_CASE)));
                        remainingUnit = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_UNIT)));
                        //Log.e("RemainingCs", "" + remainingCase + sale.getCases());
                        //Log.e("RemainingPc", "" + remainingUnit + sale.getPic());
                        if (!(sale.getCases().isEmpty() || sale.getCases().equals("") || sale.getCases() == null || sale.getCases().equals("0"))) {
                            remainingCase = remainingCase + Float.parseFloat(sale.getCases());
                        }
                        if (!(sale.getPic().isEmpty() || sale.getPic().equals("") || sale.getPic() == null || sale.getPic().equals("0"))) {
                            remainingUnit = remainingUnit + Float.parseFloat(sale.getPic());
                        }
                        updateDataMap.put(db.KEY_REMAINING_QTY_CASE, String.valueOf(remainingCase));
                        updateDataMap.put(db.KEY_REMAINING_QTY_UNIT, String.valueOf(remainingUnit));
                        HashMap<String, String> filterInter = new HashMap<>();
                        filterInter.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        db.updateData(db.VAN_STOCK_ITEMS, updateDataMap, filterInter);
                    }
                    while (cursor.moveToNext());
                }
                //If there is no item present in van stock for that material add it
                else{
                    HashMap<String,String>addMap = new HashMap<>();
                    addMap.put(db.KEY_ENTRY_TIME, Helpers.getCurrentTimeStamp());
                    addMap.put(db.KEY_DELIVERY_NO,"");
                    addMap.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                    addMap.put(db.KEY_ITEM_NO, sale.getItem_code());
                    addMap.put(db.KEY_MATERIAL_DESC1, sale.getMaterial_description());
                    if (!(sale.getCases().isEmpty() || sale.getCases().equals("") || sale.getCases() == null || sale.getCases().equals("0"))) {
                        addMap.put(db.KEY_ACTUAL_QTY_CASE,sale.getCases());
                        addMap.put(db.KEY_RESERVED_QTY_CASE,"0");
                        addMap.put(db.KEY_REMAINING_QTY_CASE,sale.getCases());
                    }
                    else{
                        addMap.put(db.KEY_ACTUAL_QTY_CASE,"0");
                        addMap.put(db.KEY_RESERVED_QTY_CASE,"0");
                        addMap.put(db.KEY_REMAINING_QTY_CASE,"0");
                    }
                    if (!(sale.getPic().isEmpty() || sale.getPic().equals("") || sale.getPic() == null || sale.getPic().equals("0"))) {
                        addMap.put(db.KEY_ACTUAL_QTY_UNIT,sale.getPic());
                        addMap.put(db.KEY_RESERVED_QTY_UNIT,"0");
                        addMap.put(db.KEY_REMAINING_QTY_UNIT,sale.getPic());
                    }
                    else{
                        addMap.put(db.KEY_ACTUAL_QTY_UNIT,"0");
                        addMap.put(db.KEY_RESERVED_QTY_UNIT,"0");
                        addMap.put(db.KEY_REMAINING_QTY_UNIT,"0");
                    }
                    if(sale.getUom().equals(App.CASE_UOM)||sale.getUom().equals(App.CASE_UOM_NEW)||sale.getUom().equals(App.BOTTLES_UOM)){
                        addMap.put(db.KEY_UOM_CASE,App.CASE_UOM);
                        addMap.put(db.KEY_UOM_UNIT,App.BOTTLES_UOM);
                    }
                    db.addData(db.VAN_STOCK_ITEMS,addMap);
                }

            }
            loadingSpinner.hide();
        }
        else{
            loadingSpinner.show();
            //Log.e("ArrayList Size", "" + arraylist.size());
            for (Sales sale : arraylist) {
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_MATERIAL_NO, "");
                map.put(db.KEY_REMAINING_QTY_CASE, "");
                map.put(db.KEY_REMAINING_QTY_UNIT, "");
                HashMap<String, String> filter = new HashMap<>();
               // Log.e("Filter MN", "" + sale.getMaterial_no());
                filter.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                Cursor cursor = db.getData(db.VAN_STOCK_ITEMS, map, filter);
               // Log.e("Cursor count", "" + cursor.getCount());
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        HashMap<String, String> updateDataMap = new HashMap<>();
                        float remainingCase = 0;
                        float remainingUnit = 0;
                        remainingCase = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_CASE)));
                        remainingUnit = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_UNIT)));
                        //Log.e("RemainingCs", "" + remainingCase + sale.getCases());
                        //Log.e("RemainingPc", "" + remainingUnit + sale.getPic());
                        if (!(sale.getCases().isEmpty() || sale.getCases().equals("") || sale.getCases() == null || sale.getCases().equals("0"))) {
                            remainingCase = remainingCase - Float.parseFloat(sale.getCases());
                        }
                        if (!(sale.getPic().isEmpty() || sale.getPic().equals("") || sale.getPic() == null || sale.getPic().equals("0"))) {
                            remainingUnit = remainingUnit - Float.parseFloat(sale.getPic());
                        }
                        updateDataMap.put(db.KEY_REMAINING_QTY_CASE, String.valueOf(remainingCase));
                        updateDataMap.put(db.KEY_REMAINING_QTY_UNIT, String.valueOf(remainingUnit));
                        HashMap<String, String> filterInter = new HashMap<>();
                        filterInter.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        db.updateData(db.VAN_STOCK_ITEMS, updateDataMap, filterInter);
                    }
                    while (cursor.moveToNext());
                }

            }
            loadingSpinner.hide();
        }

    }
    public class loadPromotions extends AsyncTask<Void,Void,Void>{
        private String promoCode;
        private String source;
        private loadPromotions(String promoCode,String source) {
           // Log.e("I m in for Load Promotions", "" + source);
            this.promoCode = promoCode;
            this.source = source;
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String>map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO,"");
            map.put(db.KEY_MATERIAL_NO,"");
            map.put(db.KEY_AMOUNT,"");
            HashMap<String,String>filter = new HashMap<>();
            filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            if(promoCode.equals(App.Promotions02)){
                filter.put(db.KEY_PROMOTION_TYPE,App.Promotions02);
            }
            else if(promoCode.equals(App.Promotions05)){
                filter.put(db.KEY_PROMOTION_TYPE,App.Promotions05);
            }
            else if(promoCode.equals(App.Promotions07)){
                filter.put(db.KEY_PROMOTION_TYPE,App.Promotions07);
            }
            Cursor cursor = db.getData(db.PROMOTIONS,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                applyPromotions(cursor,from, source);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            count++;
            if(count==3){
                count=0;
                if(loadingSpinner.isShowing()){
                    loadingSpinner.hide();
                }
                onProcessingComplete(source);
            }
        }
    }
    private void applyPromotions(Cursor cursor,String from, String source){
        Cursor promotionCursor = cursor;
        promotionCursor.moveToFirst();
        //Log.e("I m in for Apply Promotions", "" + source);
        if(from.equals("Final Invoice")){
            if(source.equals("Invoice")){
                do{
                    HashMap<String,String>map = new HashMap<>();
                    map.put(db.KEY_CUSTOMER_NO,"");
                    map.put(db.KEY_MATERIAL_NO,"");
                    map.put(db.KEY_ORG_CASE,"");
                    map.put(db.KEY_ORG_UNITS,"");
                    map.put(db.KEY_AMOUNT,"");
                    map.put(db.KEY_UOM,"");
                    map.put(db.KEY_IS_POSTED,"");
                    map.put(db.KEY_IS_PRINTED,"");
                    HashMap<String,String>filter = new HashMap<>();
                    filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                    filter.put(db.KEY_MATERIAL_NO,promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    Cursor ivCursor = db.getData(db.CAPTURE_SALES_INVOICE,map,filter);
                    if(ivCursor.getCount()>0){
                        ivCursor.moveToFirst();
                        do{
                            if(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||ivCursor.getString(ivCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                float cases = Float.parseFloat(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_ORG_CASE)));
                                discount += cases*(Float.parseFloat(promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_AMOUNT))));
                            }
                        /* else if(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                            float bottles = Float.parseFloat(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_ORG_UNITS)));
                            discount += bottles*Float.parseFloat(promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_AMOUNT)));
                        }*/
                        }
                        while (ivCursor.moveToNext());
                    }
                }
                while (promotionCursor.moveToNext());
            }
            else if(source.equals(App.GOOD_RETURN)){
                do{
                    HashMap<String,String>map = new HashMap<>();
                    map.put(db.KEY_CUSTOMER_NO,"");
                    map.put(db.KEY_MATERIAL_NO,"");
                    map.put(db.KEY_CASE,"");
                    map.put(db.KEY_UNIT,"");
                    map.put(db.KEY_PRICE,"");
                    map.put(db.KEY_UOM,"");
                    map.put(db.KEY_IS_POSTED,"");
                    map.put(db.KEY_IS_PRINTED,"");
                    HashMap<String,String>filter = new HashMap<>();
                    filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                    filter.put(db.KEY_MATERIAL_NO,promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    filter.put(db.KEY_REASON_TYPE,source);
                    Cursor ivCursor = db.getData(db.RETURNS,map,filter);
                    if(ivCursor.getCount()>0){
                        ivCursor.moveToFirst();
                        do{
                            if(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||ivCursor.getString(ivCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                float cases = Float.parseFloat(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_CASE)));
                                goodreturndiscount += cases*(Float.parseFloat(promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_AMOUNT))));
                            }
                        /* else if(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                            float bottles = Float.parseFloat(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_ORG_UNITS)));
                            discount += bottles*Float.parseFloat(promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_AMOUNT)));
                        }*/
                        }
                        while (ivCursor.moveToNext());
                    }
                }
                while (promotionCursor.moveToNext());
            }
            else if(source.equals(App.BAD_RETURN)){
                do{
                    HashMap<String,String>map = new HashMap<>();
                    map.put(db.KEY_CUSTOMER_NO,"");
                    map.put(db.KEY_MATERIAL_NO,"");
                    map.put(db.KEY_CASE,"");
                    map.put(db.KEY_UNIT,"");
                    map.put(db.KEY_PRICE,"");
                    map.put(db.KEY_UOM,"");
                    map.put(db.KEY_IS_POSTED,"");
                    map.put(db.KEY_IS_PRINTED,"");
                    HashMap<String,String>filter = new HashMap<>();
                    filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                    filter.put(db.KEY_MATERIAL_NO,promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    filter.put(db.KEY_REASON_TYPE,source);
                    Cursor ivCursor = db.getData(db.RETURNS,map,filter);
                    if(ivCursor.getCount()>0){
                        ivCursor.moveToFirst();
                        do{
                            if(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||ivCursor.getString(ivCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                                float cases = Float.parseFloat(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_CASE)));
                                badreturndiscount += cases*(Float.parseFloat(promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_AMOUNT))));
                            }
                        /* else if(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                            float bottles = Float.parseFloat(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_ORG_UNITS)));
                            discount += bottles*Float.parseFloat(promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_AMOUNT)));
                        }*/
                        }
                        while (ivCursor.moveToNext());
                    }
                }
                while (promotionCursor.moveToNext());
            }

        }
        else if(from.equals("delivery")){
            do{
                HashMap<String,String>map = new HashMap<>();
                map.put(db.KEY_CUSTOMER_NO,"");
                map.put(db.KEY_MATERIAL_NO,"");
                map.put(db.KEY_CASE,"");
                map.put(db.KEY_UNIT,"");
                map.put(db.KEY_AMOUNT,"");
                map.put(db.KEY_UOM,"");
                map.put(db.KEY_IS_POSTED,"");
                map.put(db.KEY_IS_PRINTED,"");
                HashMap<String,String>filter = new HashMap<>();
                filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                filter.put(db.KEY_MATERIAL_NO,promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                filter.put(db.KEY_DELIVERY_NO,delivery.getOrderId());
                Cursor ivCursor = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST,map,filter);
                if(ivCursor.getCount()>0){
                    ivCursor.moveToFirst();
                    do{
                        if(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||ivCursor.getString(ivCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM_NEW)||ivCursor.getString(ivCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                            float cases = Float.parseFloat(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_CASE)));
                            discount += cases*(Float.parseFloat(promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_AMOUNT))));
                        }
                        /*else if(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                            float bottles = Float.parseFloat(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_ORG_UNITS)));
                            discount += bottles*Float.parseFloat(promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_AMOUNT)));
                        }*/
                    }
                    while (ivCursor.moveToNext());
                }
            }
            while (promotionCursor.moveToNext());
        }


        Log.e("Discount", "" + discount);

    }
}
