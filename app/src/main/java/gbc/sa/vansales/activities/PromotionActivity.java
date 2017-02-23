package gbc.sa.vansales.activities;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.utils.DatabaseHandler;
/**
 * Created by eheuristic on 12/6/2016.
 */
public class PromotionActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    TextView tv_promotion;
    String promoCode;
    LinearLayout ll_bottom;
    String str_promotion_message = "";
    Customer object;
    OrderList delivery;
    String invoiceAmount;
    DatabaseHandler db = new DatabaseHandler(this);
    float totalamnt = 0;
    float discount = 0;
    EditText tv_invoice_amount;
    EditText tv_discount;
    EditText tv_net_invoice;
    String from;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");
        delivery = (OrderList) i.getParcelableExtra("delivery");
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        tv_invoice_amount = (EditText) findViewById(R.id.tv_invoice_amount);
        tv_discount = (EditText)findViewById(R.id.et_discount);
        tv_net_invoice = (EditText)findViewById(R.id.tv_net_invoice);
        //tv_invoice_amount.setText(i.getExtras().getString("invoiceamount"));
        invoiceAmount = i.getExtras().getString("invoiceamount");
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Promo Details");
        tv_promotion = (TextView) findViewById(R.id.tv_promotion);
        if (getIntent().getExtras() != null) {
            str_promotion_message = getIntent().getExtras().getString("msg", "extra Promotion");
            from = getIntent().getExtras().getString("from","");
            promoCode = getIntent().getExtras().getString("promocode");

            tv_promotion.setText(str_promotion_message.substring(0, 1).toUpperCase() + str_promotion_message.substring(1).toLowerCase());
            int pos = getIntent().getIntExtra("pos", 10);
            if (from.equals("Final Invoice")) {
                calculateTotal(from);
                new loadPromotions(promoCode,from);
                ll_bottom.setVisibility(View.GONE);
                //ll_bottom.setVisibility(View.VISIBLE);
                tv_top_header.setText("Final Invoice");
            } else {
                if (from.equals("delivery")) {
                    calculateTotal(from);
                    new loadPromotions(promoCode,from);
                    ll_bottom.setVisibility(View.GONE);
                    //ll_bottom.setVisibility(View.VISIBLE);
                } else {
                    ll_bottom.setVisibility(View.GONE);
                }
            }
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str_promotion_message.equals("delivery")) {
                    /*HashMap<String, String> filter = new HashMap<String, String>();
                    filter.put(db.KEY_DELIVERY_NO, delivery.getOrderId());
                    db.deleteData(db.CUSTOMER_DELIVERY_ITEMS_POST, filter);*/
                    finish();
                } else {
                    finish();
                }
            }
        });
        ll_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equals("Final Invoice")) {
                    final Dialog dialog = new Dialog(PromotionActivity.this);
                    dialog.setContentView(R.layout.dialog_doprint);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                    LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                    dialog.show();
                    btn_print.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PromotionActivity.this, SalesInvoiceOptionActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                    btn_notprint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PromotionActivity.this, SalesInvoiceOptionActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else if (from.equals("delivery")) {
                    Intent intent = new Intent(PromotionActivity.this, PaymentDetails.class);
                    intent.putExtra("msg", str_promotion_message);
                    intent.putExtra("from",from);
                    intent.putExtra("headerObj", object);
                    intent.putExtra("delivery", delivery);
                    intent.putExtra("invoiceamount", invoiceAmount);
                    startActivity(intent);
                    finish();
                } else {
                    if (str_promotion_message.equals("")) {
                        final Dialog dialog = new Dialog(PromotionActivity.this);
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
                                Intent intent = new Intent(PromotionActivity.this, DashboardActivity.class);
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
    }

    public class loadPromotions extends AsyncTask<Void,Void,Void>{
        private String promoCode;
        private String from;
        private loadPromotions(String promoCode,String from) {
            this.promoCode = promoCode;
            this.from = from;
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String>map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO, "");
            map.put(db.KEY_MATERIAL_NO,"");
            map.put(db.KEY_AMOUNT,"");
            HashMap<String,String>filter = new HashMap<>();
            HashMap<String,String>filter1 = new HashMap<>();
            filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            String blankCust = "";
            filter1.put(db.KEY_CUSTOMER_NO,blankCust);

            if(promoCode.equals(App.Promotions02)){
                filter.put(db.KEY_PROMOTION_TYPE,App.Promotions02);
                filter1.put(db.KEY_PROMOTION_TYPE,App.Promotions02);
            }
            else if(promoCode.equals(App.Promotions05)){
                filter.put(db.KEY_PROMOTION_TYPE,App.Promotions05);
                filter1.put(db.KEY_PROMOTION_TYPE,App.Promotions05);
            }
            else if(promoCode.equals(App.Promotions07)){
                filter.put(db.KEY_PROMOTION_TYPE,App.Promotions07);
                filter1.put(db.KEY_PROMOTION_TYPE,App.Promotions07);
            }
            if(db.checkData(db.PROMOTIONS, filter)){
                Cursor cursor = db.getData(db.PROMOTIONS,map,filter);
                cursor.moveToFirst();
                if(cursor.getCount()>0){
                  applyPromotions(cursor,from);
                }

            }
            else{
                Cursor cursor = db.getData(db.PROMOTIONS,map,filter1);
                cursor.moveToFirst();
                if(cursor.getCount()>0){
                  applyPromotions(cursor,from);
                }

            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            tv_discount.setText(String.valueOf(discount));
            tv_net_invoice.setText(String.valueOf(totalamnt + discount));
        }
    }
    private void applyPromotions(Cursor cursor,String from){
        Cursor promotionCursor = cursor;
        promotionCursor.moveToFirst();
        if(from.equalsIgnoreCase("delivery")){
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
        else if(from.equals("Final Invoice")){
            do{
                //Log.e("Promotion Cursor","" + promotionCursor.getCount());
                //Log.e("Customer in cursor","" + promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
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
                        else {
                            float bottles = Float.parseFloat(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_ORG_UNITS)));
                            discount += bottles*Float.parseFloat(promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_AMOUNT)));
                        }
                    }
                    while (ivCursor.moveToNext());
                }
            }
            while (promotionCursor.moveToNext());
        }


        //Log.e("Discount","" + discount);

    }
    private void calculateTotal(String from){
        if(from.equals("delivery")){
            float amount = 0;
            float case_sale = 0;
            float unit_sale = 0;

            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
            map.put(db.KEY_CASE, "");
            map.put(db.KEY_UNIT, "");
            map.put(db.KEY_AMOUNT, "");
            map.put(db.KEY_UOM,"");
            HashMap<String,String>filter = new HashMap<>();
            filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            filter.put(db.KEY_DELIVERY_NO,delivery.getOrderId());

            Cursor cursor = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                do{
                    case_sale += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_CASE)));
                    unit_sale += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_UNIT)));
                    //Log.e("UOM","" + cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                    if(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM_NEW)||cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                        amount += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)))*Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_CASE)));
                        //amount += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                    }
                    totalamnt = amount;
                }
                while (cursor.moveToNext());
            }


        }
        else if(from.equals("Final Invoice")){
            float case_sale = 0;
            float unit_sale = 0;
            float amount = 0;

            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
            map.put(db.KEY_ORG_CASE, "");
            map.put(db.KEY_ORG_UNITS, "");
            map.put(db.KEY_AMOUNT, "");
            map.put(db.KEY_UOM,"");
            HashMap<String,String>filter = new HashMap<>();
            filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);

            Cursor cursor = db.getData(db.CAPTURE_SALES_INVOICE,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                do{
                    case_sale += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_ORG_CASE)));
                    unit_sale += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_ORG_UNITS)));
                    if(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                        amount += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)))*Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_ORG_CASE)));
                        //amount += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                    }
                    else {
                        amount += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)))*Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_ORG_UNITS)));
                    }

                }
                while (cursor.moveToNext());
                totalamnt = amount;
            }


        }

        tv_invoice_amount.setText(String.valueOf(totalamnt));
    }
}
