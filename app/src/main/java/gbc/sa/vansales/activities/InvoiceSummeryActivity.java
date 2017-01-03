package gbc.sa.vansales.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerOperationAdapter;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.models.Sales;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
public class InvoiceSummeryActivity extends AppCompatActivity {


    ImageView iv_back;
    TextView tv_top_header;
    Customer object;
    LoadingSpinner loadingSpinner;
    DatabaseHandler db = new DatabaseHandler(this);
    int orderTotalValue = 0;
    ArrayList<Sales> arraylist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_summery);
        loadingSpinner = new LoadingSpinner(this);
        Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.invoice_summary));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tv_customer_id = (TextView)findViewById(R.id.tv_customer_id);
        TextView tv_customer_name = (TextView)findViewById(R.id.tv_customer_name);

        tv_customer_id.setText(object.getCustomerID());
        tv_customer_name.setText(object.getCustomerName());

        Button btn_complete_invoice = (Button)findViewById(R.id.btn_complete_invoice);
        btn_complete_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(InvoiceSummeryActivity.this);
                dialog.setContentView(R.layout.activity_print);
                Button print = (Button)dialog.findViewById(R.id.btnPrint);
                print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new postData().execute();
                        dialog.dismiss();
                    }
                });
                Button donotPrint = (Button)dialog.findViewById(R.id.btnCancel2);
                donotPrint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }

                });
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

    }

    public class postData extends AsyncTask<Void, Void, Void> {
        private ArrayList<String> returnList;
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

            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO, "");
            map.put(db.KEY_ITEM_NO, "");
            map.put(db.KEY_ITEM_CATEGORY, "");
            map.put(db.KEY_MATERIAL_NO, "" );
            map.put(db.KEY_MATERIAL_GROUP, "");
            map.put(db.KEY_MATERIAL_DESC1,"");
            map.put(db.KEY_ORG_CASE, "");
            map.put(db.KEY_UOM,"");
            map.put(db.KEY_ORG_UNITS, "");
            map.put(db.KEY_AMOUNT, "");

            HashMap<String,String>filter = new HashMap<>();
            filter.put(db.KEY_IS_POSTED,"N");

            Cursor cursor = db.getData(db.CAPTURE_SALES_INVOICE,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                do{
                    Sales sale = new Sales();
                    sale.setMaterial_no(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    sale.setPic(cursor.getString(cursor.getColumnIndex(db.KEY_ORG_UNITS)));
                    sale.setCases(cursor.getString(cursor.getColumnIndex(db.KEY_ORG_CASE)));
                    sale.setUom(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                    arraylist.add(sale);
                }
                while(cursor.moveToNext());
            }

            for(Sales sale:arraylist){
                HashMap<String,String> postmap = new HashMap<String, String>();
                postmap.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                postmap.put(db.KEY_IS_POSTED,"Y");

                HashMap<String,String> filtermap = new HashMap<>();
                filtermap.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                filtermap.put(db.KEY_MATERIAL_NO,sale.getMaterial_no());
                db.updateData(db.CAPTURE_SALES_INVOICE, map, filter);
            }
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            if(this.orderID.isEmpty()||this.orderID.equals("")||this.orderID==null){
                Toast.makeText(getApplicationContext(), getString(R.string.request_timeout), Toast.LENGTH_SHORT).show();
            }
            else{

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InvoiceSummeryActivity.this);
                alertDialogBuilder.setTitle("Message")
                        .setMessage("Request " + this.orderID + " has been created")
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

    public String postData(){
        String orderID = "";
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("Function", ConfigStore.LoadRequestFunction);
            map.put("OrderId", "");
            map.put("DocumentType", ConfigStore.DocumentType);
            // map.put("DocumentDate", Helpers.formatDate(new Date(),App.DATE_FORMAT_WO_SPACE));
            // map.put("DocumentDate", null);
            map.put("CustomerId", object.getCustomerID());
            map.put("SalesOrg", Settings.getString(App.SALES_ORG));
            map.put("DistChannel", Settings.getString(App.DIST_CHANNEL));
            map.put("Division", Settings.getString(App.DIVISION));
            map.put("OrderValue", String.valueOf(orderTotalValue));
            map.put("Currency", "SAR");
            map.put("PurchaseNum", Helpers.generateNumber(db,ConfigStore.InvoiceRequest_PR_Type));

            JSONArray deepEntity = new JSONArray();

            HashMap<String, String> itemMap = new HashMap<>();
            itemMap.put(db.KEY_ITEM_NO,"");
            itemMap.put(db.KEY_MATERIAL_NO,"");
            itemMap.put(db.KEY_MATERIAL_DESC1,"");
            itemMap.put(db.KEY_ORG_CASE,"");
            itemMap.put(db.KEY_ORG_UNITS,"");
            itemMap.put(db.KEY_UOM,"");
            itemMap.put(db.KEY_AMOUNT,"");

            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_IS_POSTED,"N");

            Cursor cursor = db.getData(db.CAPTURE_SALES_INVOICE,itemMap,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                int itemno = 10;
                do{
                    if(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)){
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                        jo.put("Material",cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description",cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant","");
                        jo.put("Quantity",cursor.getString(cursor.getColumnIndex(db.KEY_ORG_CASE)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("UoM", App.CASE_UOM);
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                        jo.put("Storagelocation", "");
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno+10;
                        deepEntity.put(jo);
                    }
                    if(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)){
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                        jo.put("Material",cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description",cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant","");
                        jo.put("Quantity",cursor.getString(cursor.getColumnIndex(db.KEY_ORG_UNITS)));
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
            orderID = IntegrationService.postData(InvoiceSummeryActivity.this, App.POST_COLLECTION, map, deepEntity);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return orderID;

    }

    public void updateStockinVan(){
        loadingSpinner.show();
        Log.e("ArrayList Size","" + arraylist.size());
        for(Sales sale:arraylist){
            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_MATERIAL_NO,"");
            map.put(db.KEY_REMAINING_QTY_CASE,"");
            map.put(db.KEY_REMAINING_QTY_UNIT,"");

            HashMap<String,String>filter= new HashMap<>();
            Log.e("Filter MN","" + sale.getMaterial_no());
            filter.put(db.KEY_MATERIAL_NO,sale.getMaterial_no());

            Cursor cursor = db.getData(db.VAN_STOCK_ITEMS,map,filter);
            Log.e("Cursor count","" + cursor.getCount());
            if(cursor.getCount()>0){
                cursor.moveToFirst();
            }
            do{
                HashMap<String,String> updateDataMap = new HashMap<>();
                float remainingCase = 0;
                float remainingUnit = 0;
                remainingCase = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_CASE)));
                remainingUnit = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_UNIT)));

                Log.e("RemainingCs","" + remainingCase + sale.getCases());
                Log.e("RemainingPc","" + remainingUnit + sale.getPic());

                if(!(sale.getCases().isEmpty()||sale.getCases().equals("")||sale.getCases()==null||sale.getCases().equals("0"))){
                    remainingCase = remainingCase - Float.parseFloat(sale.getCases());
                }
                if(!(sale.getPic().isEmpty()||sale.getPic().equals("")||sale.getPic()==null||sale.getPic().equals("0"))){
                    remainingUnit = remainingUnit - Float.parseFloat(sale.getPic());
                }

                updateDataMap.put(db.KEY_REMAINING_QTY_CASE, String.valueOf(remainingCase));
                updateDataMap.put(db.KEY_REMAINING_QTY_UNIT, String.valueOf(remainingUnit));

                HashMap<String,String>filterInter = new HashMap<>();
                filterInter.put(db.KEY_MATERIAL_NO,cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));

                db.updateData(db.VAN_STOCK_ITEMS,updateDataMap,filterInter);
            }
            while (cursor.moveToNext());
        }
        loadingSpinner.hide();
    }


}
