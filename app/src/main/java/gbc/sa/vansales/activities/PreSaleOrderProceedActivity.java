package gbc.sa.vansales.activities;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.LoadRequestBadgeAdapter;
import gbc.sa.vansales.adapters.OrderRequestBadgeAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.models.OrderRequest;
import gbc.sa.vansales.models.PreSaleProceed;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
public class PreSaleOrderProceedActivity extends AppCompatActivity {
    ArrayList<PreSaleProceed> preSaleProceeds = new ArrayList<>();
    ImageView iv_back;
    TextView tv_top_header;
    TextView tv_date;
    ImageView iv_calendar, iv_search;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Button btn_confirm;
    ImageView toolbar_iv_back;
    EditText et_search;
    ListView list;
    OrderRequestBadgeAdapter adapter;
    Customer object;
    LoadingSpinner loadingSpinner;
    public static String from = "";
    LinearLayout ll_bottom;
    FloatingActionButton fb_print;
    FloatingActionButton fb_edit;
    List<LoadRequestConstants> preSaleOrderArraylist;
    int position;
    HashMap<Integer, List<LoadRequestConstants>> constantsHashMap = new HashMap<>();
    DatabaseHandler db = new DatabaseHandler(this);
    ArrayList<OrderRequest> arraylist = new ArrayList<>();
    OrderList orderList;
    int orderTotalValue = 0;
    TextView tv_header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sale_order_proceed);

        View v = (View)findViewById(R.id.inc_common_header);
        tv_header=(TextView)findViewById(R.id.tv_top_header);
        tv_header.setVisibility(View.VISIBLE);
        tv_header.setText(getString(R.string.presalesorder));
//        v.setVisibility(View.INVISIBLE);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_date.setText(Helpers.formatDate(new Date(),App.DATE_PICKER_FORMAT));
        iv_calendar = (ImageView) findViewById(R.id.iv_calander);
       /* iv_search = (ImageView) findViewById(R.id.iv_search);
        iv_search.setVisibility(View.GONE);*/
        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        iv_back.setVisibility(View.VISIBLE);
        btn_confirm = (Button) findViewById(R.id.btn_confirm_delivery_presale_proceed);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        fb_print = (FloatingActionButton) findViewById(R.id.fab_print);
        fb_edit = (FloatingActionButton) findViewById(R.id.fab_edit);

        loadingSpinner = new LoadingSpinner(this);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");

        orderList = (OrderList)i.getParcelableExtra("orderList");
        if (getIntent().getExtras() != null) {
            from = getIntent().getStringExtra("from");
        }

        list = (ListView) findViewById(R.id.listview);
        list.setItemsCanFocus(true);

        if(from.equals("list"))
        {
            adapter = new OrderRequestBadgeAdapter(this, arraylist,from,"no");
        }
        else {
            adapter = new OrderRequestBadgeAdapter(this, arraylist,from,"yes");
        }




        list.setAdapter(adapter);
        setTitle(getString(R.string.presalesorder));
      //  list.setItemsCanFocus(true);
        if(from.equalsIgnoreCase("button")){
            new loadItems().execute();
        } else if(from.equalsIgnoreCase("list")){
            new loadItemsOrder(orderList.getOrderId());
        }


        myCalendar = Calendar.getInstance();
        iv_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PreSaleOrderProceedActivity.this, date, myCalendar
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
        // Locate the ListView in listview_main.xml

        if (from.equals("button")) {
            ll_bottom.setVisibility(View.GONE);
            btn_confirm.setVisibility(View.VISIBLE);

            // Pass results to ListViewAdapter Class
        } else if (from.equals("list")) {
            ll_bottom.setVisibility(View.VISIBLE);
            btn_confirm.setVisibility(View.GONE);

        }
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String purchaseNum = Helpers.generateNumber(db, ConfigStore.OrderRequest_PR_Type);
                for(OrderRequest loadRequest:arraylist){
                    try{
                        if(loadRequest.getCases().equals("")||loadRequest.getCases().isEmpty()||loadRequest.getCases()==null){
                            loadRequest.setCases("0");
                        }
                        if(loadRequest.getUnits().equals("")||loadRequest.getUnits().isEmpty()||loadRequest.getUnits()==null){
                            loadRequest.setUnits("0");
                        }
                        HashMap<String,String> map = new HashMap<String, String>();
                        map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                        map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                        map.put(db.KEY_DATE,tv_date.getText().toString());
                        map.put(db.KEY_ITEM_NO,loadRequest.getItemCode());
                        map.put(db.KEY_MATERIAL_DESC1,loadRequest.getItemName());
                        map.put(db.KEY_MATERIAL_NO,loadRequest.getMaterialNo());
                        map.put(db.KEY_MATERIAL_GROUP,loadRequest.getItemCategory());
                        map.put(db.KEY_CASE,loadRequest.getCases());
                        map.put(db.KEY_UNIT,loadRequest.getUnits());
                        map.put(db.KEY_UOM,loadRequest.getUom());
                        map.put(db.KEY_PRICE,loadRequest.getPrice());
                        map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                        map.put(db.KEY_IS_PRINTED, "");
                        map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                        map.put(db.KEY_ORDER_ID,purchaseNum);
                        map.put(db.KEY_PURCHASE_NUMBER,purchaseNum);
                        orderTotalValue = orderTotalValue + Integer.parseInt(loadRequest.getPrice());
                        if(Integer.parseInt(loadRequest.getCases())>0 || Integer.parseInt(loadRequest.getUnits())>0){
                            db.addData(db.ORDER_REQUEST,map);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



                final Dialog dialog = new Dialog(PreSaleOrderProceedActivity.this);
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
                    }
                });
                btn_notprint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });
        fb_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                final Dialog dialog = new Dialog(PreSaleOrderProceedActivity.this);
                dialog.setContentView(R.layout.dialog_doprint);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                dialog.show();
                btn_print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Const.constantsHashMap.put(position, Const.loadRequestConstantsList);
                        Log.v("Const.id ", "const id : " + Const.id);
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
        });
        fb_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                adapter = new OrderRequestBadgeAdapter(PreSaleOrderProceedActivity.this, arraylist,from,"yes");
                list.setAdapter(adapter);

                adapter.notifyDataSetChanged();

            }
        });
    }
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        tv_date.setText(sdf.format(myCalendar.getTime()));
        PreSaleProceed proceed = new PreSaleProceed();
        proceed.setDATE(tv_date.getText().toString());
        Log.e("Date","" + tv_date.getText().toString());
       // Const.proceedArrayList.add(Const.id, proceed);
    }

    @Override
    public void onBackPressed() {
//        Log.v("hashmap",Const.constantsHashMap.size()+"");
//        for(int i=0;i<Const.constantsHashMap.size();i++)
//        {
//            List<LoadRequestConstants> constantses=Const.constantsHashMap.get(i);
//            for(int j=0;j<constantses.size();j++)
//            {
//                Log.v("itemname",constantses.get(j).getItemName());
//            }
//        }
        finish();
    }

    public class loadItems extends AsyncTask<Void,Void,Void> {

//        private loadItems() {
//            execute();
//        }

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            try {

                pd = new ProgressDialog(PreSaleOrderProceedActivity.this);
                pd.setMessage("Please Wait..");
                pd.setCancelable(false);
                pd.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                pd.cancel();
            }


        }

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_TRIP_ID,"");
            map.put(db.KEY_MATERIAL_GROUPA_DESC,"");
            map.put(db.KEY_MATERIAL_GROUPB_DESC,"");
            map.put(db.KEY_MATERIAL_DESC2,"");
            map.put(db.KEY_BATCH_MANAGEMENT,"");
            map.put(db.KEY_PRODUCT_HIERARCHY,"");
            map.put(db.KEY_VOLUME_UOM,"");
            map.put(db.KEY_VOLUME,"");
            map.put(db.KEY_WEIGHT_UOM,"");
            map.put(db.KEY_NET_WEIGHT,"");
            map.put(db.KEY_GROSS_WEIGHT,"");
            map.put(db.KEY_ARTICLE_CATEGORY,"");
            map.put(db.KEY_ARTICLE_NO,"");
            map.put(db.KEY_BASE_UOM,"");
            map.put(db.KEY_MATERIAL_GROUP,"");
            map.put(db.KEY_MATERIAL_TYPE,"");
            map.put(db.KEY_MATERIAL_DESC1,"");
            map.put(db.KEY_MATERIAL_NO,"");

            HashMap<String,String> filter = new HashMap<>();
            Cursor cursor = db.getData(db.ARTICLE_HEADER,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                setLoadItems(cursor,false);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(pd!=null && pd.isShowing())
            {
                pd.cancel();
                adapter.notifyDataSetChanged();

            }


        }
    }

    public class loadItemsOrder extends AsyncTask<Void,Void,Void> {

        private String orderId;
        private loadItemsOrder(String orderId) {
            this.orderId = orderId;
            execute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_ITEM_NO,"");
            map.put(db.KEY_MATERIAL_DESC1,"");
            map.put(db.KEY_MATERIAL_NO ,"");
            map.put(db.KEY_MATERIAL_GROUP ,"");
            map.put(db.KEY_CASE ,"");
            map.put(db.KEY_UNIT ,"");
            map.put(db.KEY_UOM ,"");
            map.put(db.KEY_PRICE ,"");

            HashMap<String,String> filter = new HashMap<>();
           // filter.put(db.KEY_ORDER_ID, this.orderId);
            filter.put(db.KEY_PURCHASE_NUMBER, this.orderId);
            Cursor cursor = db.getData(db.ORDER_REQUEST,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                setLoadItems(cursor,true);
            }
            return null;
        }
    }

    public void setLoadItems(Cursor loadItemsCursor,Boolean isPosted){
        Cursor cursor = loadItemsCursor;

        do{

            OrderRequest loadRequest = new OrderRequest();
            loadRequest.setItemCode(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
            loadRequest.setItemName(UrlBuilder.decodeString(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
            if(isPosted){
                loadRequest.setCases(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM) ? cursor.getString(cursor.getColumnIndex(db.KEY_CASE)) : "0");
                loadRequest.setUnits(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM) ? cursor.getString(cursor.getColumnIndex(db.KEY_UNIT)): "0");
            }
            else{
                // loadRequest.setCases(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.CASE_UOM) ? "0" : "0");
                // loadRequest.setUnits(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.BOTTLES_UOM) ? "0" : "0");
                loadRequest.setUom(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)));
            }
            loadRequest.setMaterialNo(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
            arraylist.add(loadRequest);

        }
        while (cursor.moveToNext());

    }

    public class postData extends AsyncTask<Void, Void, Void>{
        private ArrayList<String>returnList;
        private String orderId = "";
        private String[] tokens = new String[2];
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            //this.returnList = IntegrationService.RequestToken(LoadRequestActivity.this);
            this.orderId = postData();
            this.tokens = orderId.split(",");
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

            Log.e("Order id", "" + this.orderId);
            if(this.tokens[0].toString().equals(this.tokens[1].toString())){
            for(OrderRequest loadRequest:arraylist){
                HashMap<String,String> map = new HashMap<String, String>();
                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    map.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                    map.put(db.KEY_ORDER_ID,tokens[0].toString());

                HashMap<String,String> filter = new HashMap<>();
                    filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                filter.put(db.KEY_MATERIAL_NO,loadRequest.getMaterialNo());
                    //filter.put(db.KEY_ORDER_ID,tokens[1].toString());
                    filter.put(db.KEY_PURCHASE_NUMBER,tokens[1].toString());
                db.updateData(db.ORDER_REQUEST, map, filter);
            }
                if(loadingSpinner.isShowing()){
                    loadingSpinner.hide();
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreSaleOrderProceedActivity.this);
                alertDialogBuilder.setTitle(getString(R.string.message))
                        .setMessage("Request with reference " + tokens[1].toString() + " has been saved")
                       // .setMessage("Request with reference " + tokens[0].toString() + " has been saved")
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                for (OrderRequest loadRequest : arraylist) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    map.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED);
                    map.put(db.KEY_ORDER_ID,tokens[0].toString());

                    HashMap<String, String> filter = new HashMap<>();
                    filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                    filter.put(db.KEY_MATERIAL_NO,loadRequest.getMaterialNo());
                    //filter.put(db.KEY_ORDER_ID,tokens[1].toString());
                    filter.put(db.KEY_PURCHASE_NUMBER,tokens[1].toString());

                    db.updateData(db.ORDER_REQUEST, map, filter);
                }

            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            if(this.orderId.isEmpty()||this.orderId.equals("")||this.orderId==null){
                Toast.makeText(getApplicationContext(), getString(R.string.request_timeout), Toast.LENGTH_SHORT).show();
            }
                else if(this.orderId.contains("Error")){
                    Toast.makeText(getApplicationContext(), this.orderId.replaceAll("Error","").trim(), Toast.LENGTH_SHORT).show();
                }
            else{
                //Logic to go Back
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreSaleOrderProceedActivity.this);
                alertDialogBuilder.setTitle("Message")
                            .setMessage("Request " + tokens[1].toString() + " has been created")
                            //.setMessage("Request " + tokens[0].toString() + " has been created")
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
    private String postData(){
        String orderID = "";
        String purchaseNumber = "";
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("Function", ConfigStore.CustomerOrderRequestFunction);
            map.put("OrderId", "");
            map.put("DocumentType", ConfigStore.CustomerOrderRequestDocumentType);
            // map.put("DocumentDate", Helpers.formatDate(new Date(),App.DATE_FORMAT_WO_SPACE));
            // map.put("DocumentDate", null);
            map.put("CustomerId", object.getCustomerID());
            map.put("SalesOrg", Settings.getString(App.SALES_ORG));
            map.put("DistChannel", Settings.getString(App.DIST_CHANNEL));
            map.put("Division", Settings.getString(App.DIVISION));
            map.put("OrderValue", String.valueOf(orderTotalValue));
            map.put("Currency", "SAR");
           // map.put("PurchaseNum", Helpers.generateNumber(db, ConfigStore.LoadRequest_PR_Type));
            JSONArray deepEntity = new JSONArray();

            HashMap<String, String> itemMap = new HashMap<>();
            itemMap.put(db.KEY_ITEM_NO,"");
            itemMap.put(db.KEY_MATERIAL_NO,"");
            itemMap.put(db.KEY_MATERIAL_DESC1,"");
            itemMap.put(db.KEY_CASE,"");
            itemMap.put(db.KEY_UNIT,"");
            itemMap.put(db.KEY_UOM,"");
            itemMap.put(db.KEY_PRICE,"");
            itemMap.put(db.KEY_ORDER_ID,"");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            Cursor cursor = db.getData(db.ORDER_REQUEST,itemMap,filter);
            Log.e("Cursor count","" + cursor.getCount());
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                map.put("PurchaseNum", cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID)));
                purchaseNumber = map.get("PurchaseNum");
                int itemno = 10;
                do{
                    if(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)) {
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno),4));
                        jo.put("Material",cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description",cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant","");
                        jo.put("Quantity",cursor.getString(cursor.getColumnIndex(db.KEY_CASE)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("UoM", App.CASE_UOM);
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
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
                        jo.put("Quantity",cursor.getString(cursor.getColumnIndex(db.KEY_UNIT)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("UoM", App.BOTTLES_UOM);
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("Storagelocation", "");
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno+10;
                        deepEntity.put(jo);
                    }

                }
                while (cursor.moveToNext());
            }
            orderID = IntegrationService.postData(PreSaleOrderProceedActivity.this, App.POST_COLLECTION, map, deepEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderID + "," + purchaseNumber;
    }
}
