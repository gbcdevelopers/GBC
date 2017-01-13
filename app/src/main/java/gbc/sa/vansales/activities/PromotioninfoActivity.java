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
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.models.Sales;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
/**
 * Created by eheuristic on 12/6/2016.
 */
public class PromotioninfoActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    TextView tv_promotion;
    LinearLayout ll_bottom;
    String str_promotion_message = "";
    Customer object;
    OrderList delivery;
    String invoiceAmount;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    float totalamnt = 0;
    TextView tv_current_invoice;
    ArrayList<Sales> arraylist = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        loadingSpinner = new LoadingSpinner(this);
        Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);



        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Promo Details");
        tv_promotion = (TextView) findViewById(R.id.tv_promotion);
        tv_current_invoice = (TextView) findViewById(R.id.tv_invoice_amount);
        if (getIntent().getExtras() != null) {
            str_promotion_message = getIntent().getExtras().getString("msg", "extra Promotion");
            tv_promotion.setText(str_promotion_message.substring(0, 1).toUpperCase() + str_promotion_message.substring(1).toLowerCase());
            int pos = getIntent().getIntExtra("pos", 10);
            if (str_promotion_message.equals("Final Invoice")) {
                ll_bottom.setVisibility(View.VISIBLE);
            } else {
                if (str_promotion_message.equals("delivery")) {
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
                if (str_promotion_message.equals("Final Invoice")) {
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
                            new postData().execute();
                            dialog.cancel();
                        }
                    });
                    btn_notprint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           /* Intent intent = new Intent(PromotioninfoActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();*/
                            dialog.dismiss();
                        }
                    });
                } else if (str_promotion_message.equals("delivery")) {
                    Intent intent = new Intent(PromotioninfoActivity.this, PaymentDetails.class);
                    intent.putExtra("msg", str_promotion_message);
                    intent.putExtra("headerObj", object);
                    intent.putExtra("delivery", delivery);
                    intent.putExtra("invoiceamount", invoiceAmount);
                    startActivity(intent);
                    finish();
                } else {
                    if (str_promotion_message.equals("")) {
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
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
        map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
        if (db.checkData(db.CAPTURE_SALES_INVOICE, map)) {
            new loadData().execute();
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
            map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
            map.put(db.KEY_ORG_CASE, "");
            map.put(db.KEY_ORG_UNITS, "");
            map.put(db.KEY_AMOUNT, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            Cursor cursor = db.getData(db.CAPTURE_SALES_INVOICE, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
            }
            do {
                totalamnt += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
            }
            while (cursor.moveToNext());
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (loadingSpinner.isShowing()) {
                loadingSpinner.hide();
            }
            tv_current_invoice.setText(String.valueOf(totalamnt));
        }
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
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            Cursor cursor = db.getData(db.CAPTURE_SALES_INVOICE, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Sales sale = new Sales();
                    sale.setMaterial_no(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
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
                    filtermap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    filtermap.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    filtermap.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                    filtermap.put(db.KEY_PURCHASE_NUMBER,tokens[1].toString());
                    db.updateData(db.CAPTURE_SALES_INVOICE, postmap, filtermap);
                }

                if(loadingSpinner.isShowing()){
                    loadingSpinner.hide();
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PromotioninfoActivity.this);
                alertDialogBuilder.setTitle("Message")
                        .setMessage("Request with reference " + tokens[0].toString() + " has been saved")
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateStockinVan();
                                dialog.dismiss();
                                finish();
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
                                    updateStockinVan();
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
            map.put("DocumentType", ConfigStore.DeliveryDocumentType);
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
            Cursor cursor = db.getData(db.CAPTURE_SALES_INVOICE, itemMap, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                map.put("PurchaseNum", cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID)));
                purchaseNumber = map.get("PurchaseNum");
                int itemno = 10;
                do {
                    if (cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)) {
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                        jo.put("Material", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant", "");
                        jo.put("Quantity", cursor.getString(cursor.getColumnIndex(db.KEY_ORG_CASE)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("UoM", App.CASE_UOM);
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("Storagelocation", "");
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno + 10;
                        deepEntity.put(jo);
                    }
                    if (cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)) {
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                        jo.put("Material", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant", "");
                        jo.put("Quantity", cursor.getString(cursor.getColumnIndex(db.KEY_ORG_UNITS)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("UoM", App.BOTTLES_UOM);
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("Storagelocation", "");
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
    public void updateStockinVan() {
        loadingSpinner.show();
        Log.e("ArrayList Size", "" + arraylist.size());
        for (Sales sale : arraylist) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_MATERIAL_NO, "");
            map.put(db.KEY_REMAINING_QTY_CASE, "");
            map.put(db.KEY_REMAINING_QTY_UNIT, "");
            HashMap<String, String> filter = new HashMap<>();
            Log.e("Filter MN", "" + sale.getMaterial_no());
            filter.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
            Cursor cursor = db.getData(db.VAN_STOCK_ITEMS, map, filter);
            Log.e("Cursor count", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
            }
            do {
                HashMap<String, String> updateDataMap = new HashMap<>();
                float remainingCase = 0;
                float remainingUnit = 0;
                remainingCase = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_CASE)));
                remainingUnit = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_UNIT)));
                Log.e("RemainingCs", "" + remainingCase + sale.getCases());
                Log.e("RemainingPc", "" + remainingUnit + sale.getPic());
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
        loadingSpinner.hide();
    }
}
