package gbc.sa.vansales.activities;
import android.app.Activity;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
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
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.DriverRouteFlags;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.models.OrderRequest;
import gbc.sa.vansales.models.PreSaleProceed;
import gbc.sa.vansales.sap.DataListener;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.PrinterHelper;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
public class PreSaleOrderProceedActivity extends AppCompatActivity implements DataListener{
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
    float orderTotalValue = 0;
    float totalamnt = 0;
    float discount = 0;
    int count=0;
    TextView tv_header;
    public ArrayList<ArticleHeader> articles;
    boolean isPrint = false;
    App.DriverRouteControl flag = new App.DriverRouteControl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sale_order_proceed);
        View v = (View) findViewById(R.id.inc_common_header);
        tv_header = (TextView) findViewById(R.id.tv_top_header);
        myCalendar = Calendar.getInstance();
        flag = DriverRouteFlags.get();
        tv_header.setVisibility(View.VISIBLE);
        tv_header.setText(getString(R.string.presalesorder));
        articles = new ArrayList<>();
        articles = ArticleHeaders.get();
//        v.setVisibility(View.INVISIBLE);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_date.setText(Helpers.formatDate(new Date(), App.DATE_PICKER_FORMAT));
        iv_calendar = (ImageView) findViewById(R.id.iv_calander);
       /* iv_search = (ImageView) findViewById(R.id.iv_search);
        iv_search.setVisibility(View.GONE);*/
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        iv_back.setVisibility(View.VISIBLE);
        btn_confirm = (Button) findViewById(R.id.btn_confirm_delivery_presale_proceed);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        fb_print = (FloatingActionButton) findViewById(R.id.fab_print);
        fb_edit = (FloatingActionButton) findViewById(R.id.fab_edit);
        fb_edit.setVisibility(View.GONE);
        loadingSpinner = new LoadingSpinner(this);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");
        orderList = (OrderList) i.getParcelableExtra("orderList");
        if (getIntent().getExtras() != null) {
            from = getIntent().getStringExtra("from");
        }
        list = (ListView) findViewById(R.id.listview);
        list.setItemsCanFocus(true);
        if (from.equals("list")) {
            adapter = new OrderRequestBadgeAdapter(this, arraylist, from, "no");
        } else {
            adapter = new OrderRequestBadgeAdapter(this, arraylist, from, "yes");
        }

        setTitle(getString(R.string.presalesorder));
        //  list.setItemsCanFocus(true);
        if (from.equalsIgnoreCase("button")) {
            if(!flag.getDefaultDeliveryDays().equals("")&&!flag.getDefaultDeliveryDays().equals("0")){
                iv_calendar.setEnabled(false);
                String days = flag.getDefaultDeliveryDays();
                myCalendar.setTime(new Date());
                myCalendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(days));
                updateLabel();
                new loadItems().execute();
            }
            else{
                new loadItems().execute();
            }

            //list.setEnabled(true);
        } else if (from.equalsIgnoreCase("list")) {
            new loadItemsOrder(orderList.getOrderId());
            iv_calendar.setEnabled(false);
            //list.setEnabled(false);
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try{
                    final OrderRequest item = arraylist.get(position);
                    final Dialog dialog = new Dialog(PreSaleOrderProceedActivity.this);
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_with_crossbutton);
                    dialog.setCancelable(false);
                    TextView tv = (TextView) dialog.findViewById(R.id.dv_title);
                    tv.setText(item.getItemName());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    ImageView iv_cancle = (ImageView) dialog.findViewById(R.id.imageView_close);
                    Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
                    final EditText ed_cases = (EditText) dialog.findViewById(R.id.ed_cases);
                    final EditText ed_pcs = (EditText) dialog.findViewById(R.id.ed_pcs);
                    final EditText ed_cases_inv = (EditText) dialog.findViewById(R.id.ed_cases_inv);
                    final EditText ed_pcs_inv = (EditText) dialog.findViewById(R.id.ed_pcs_inv);
                    LinearLayout ll1 = (LinearLayout) dialog.findViewById(R.id.ll_1);
                    ll1.setVisibility(View.GONE);
                    RelativeLayout rl_specify = (RelativeLayout) dialog.findViewById(R.id.rl_specify_reason);
                    rl_specify.setVisibility(View.GONE);
                    if (item.isAltUOM()) {
                        ed_pcs.setEnabled(true);
                    } else {
                        ed_pcs.setEnabled(false);
                    }

                    ed_cases.setText(item.getCases().equals("0") ? "" : item.getCases());
                    ed_pcs.setText(item.getUnits().equals("0")?"":item.getUnits());

                    //ed_cases.setText(item.getCases());
                   // ed_pcs.setText(item.getUnits());

                    LinearLayout ll_1 = (LinearLayout) dialog.findViewById(R.id.ll_1);
                    iv_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    if(!from.equalsIgnoreCase("list")){
                        dialog.show();
                    }

                    btn_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String strCase = ed_cases.getText().toString();
                            String strpcs = ed_pcs.getText().toString();
                            String strcaseinv = ed_cases_inv.getText().toString();
                            String strpcsinv = ed_pcs_inv.getText().toString();
                        /*TextView tv_cases = (TextView) view.findViewById(R.id.tv_cases_value);
                        TextView tv_pcs = (TextView) view.findViewById(R.id.tv_pcs_value);
                        tv_cases.setText(strCase);
                        tv_pcs.setText(strpcs);*/
                            if (strCase.isEmpty() || strCase == null || strCase.trim().equals("")) {
                                strCase = String.valueOf(0);
                            }
                            if (strpcs.isEmpty() || strpcs == null || strpcs.trim().equals("")) {
                                strpcs = String.valueOf(0);
                            }
                            if (strcaseinv.isEmpty() || strcaseinv == null || strcaseinv.trim().equals("")) {
                                strcaseinv = String.valueOf(0);
                            }
                            if (strpcsinv.isEmpty() || strpcsinv == null || strpcsinv.trim().equals("")) {
                                strpcsinv = String.valueOf(0);
                            }
                            //item.setCases(strCase);
                            //item.setUnits(strpcs);

                            item.setCases(strCase.trim().equals("")?"0":strCase);
                            item.setUnits(strpcs.trim().equals("")?"0":strpcs);


                            arraylist.remove(position);
                            arraylist.add(position, item);
                            hideSoftKeyboard();
                            dialog.dismiss();
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

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
                try{
                    String purchaseNum = Helpers.generateNumber(db, ConfigStore.OrderRequest_PR_Type);
                    for (OrderRequest loadRequest : arraylist) {
                        try {
                            if (loadRequest.getCases().equals("") || loadRequest.getCases().isEmpty() || loadRequest.getCases() == null) {
                                loadRequest.setCases("0");
                            }
                            if (loadRequest.getUnits().equals("") || loadRequest.getUnits().isEmpty() || loadRequest.getUnits() == null) {
                                loadRequest.setUnits("0");
                            }
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            map.put(db.KEY_DATE, tv_date.getText().toString());
                            map.put(db.KEY_ITEM_NO, loadRequest.getItemCode());
                            map.put(db.KEY_MATERIAL_DESC1, loadRequest.getItemName());
                            map.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                            map.put(db.KEY_MATERIAL_GROUP, loadRequest.getItemCategory());
                            map.put(db.KEY_CASE, loadRequest.getCases());
                            map.put(db.KEY_UNIT, loadRequest.getUnits());
                            map.put(db.KEY_UOM, loadRequest.getUom());
                            map.put(db.KEY_PRICE, loadRequest.getPrice());
                            map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                            map.put(db.KEY_IS_PRINTED, "");
                            map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                            map.put(db.KEY_ORDER_ID, purchaseNum);
                            map.put(db.KEY_PURCHASE_NUMBER, purchaseNum);
                            orderTotalValue = orderTotalValue + Float.parseFloat(loadRequest.getPrice());
                            if (Float.parseFloat(loadRequest.getCases()) > 0 || Float.parseFloat(loadRequest.getUnits()) > 0) {
                                //Log.e("Insert","BROOOOOOO");
                                db.addData(db.ORDER_REQUEST, map);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    final Dialog dialog = new Dialog(PreSaleOrderProceedActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_doprint);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                    LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                    dialog.show();
                    btn_print.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //new postData().execute();
                            if (!checkforNullBeforePost()) {
                                dialog.dismiss();
                                Toast.makeText(PreSaleOrderProceedActivity.this,getString(R.string.no_data),Toast.LENGTH_SHORT).show();
                            } else {
                                isPrint = true;
                                dialog.dismiss();
                                new loadData().execute();
                            }
                        }
                    });
                    btn_notprint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //new postData().execute();
                            if (!checkforNullBeforePost()) {
                                dialog.dismiss();
                                Toast.makeText(PreSaleOrderProceedActivity.this,getString(R.string.no_data),Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                new loadData().execute();
                            }
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }

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
                        createPrintout(true,orderList.getOrderDate(),orderList.getOrderId(),false);
                        //finish();
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
                adapter = new OrderRequestBadgeAdapter(PreSaleOrderProceedActivity.this, arraylist, from, "yes");
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }


    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) PreSaleOrderProceedActivity.this.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                PreSaleOrderProceedActivity.this.getCurrentFocus().getWindowToken(), 0);
    }
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        tv_date.setText(sdf.format(myCalendar.getTime()));
        PreSaleProceed proceed = new PreSaleProceed();
        proceed.setDATE(tv_date.getText().toString());
        //Log.e("Date", "" + tv_date.getText().toString());
        // Const.proceedArrayList.add(Const.id, proceed);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    @Override
    public void onProcessingComplete() {
        new postData().execute();
    }
    @Override
    public void onProcessingComplete(String source) {
    }
    public class loadItems extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_TRIP_ID, "");
            map.put(db.KEY_MATERIAL_GROUPA_DESC, "");
            map.put(db.KEY_MATERIAL_GROUPB_DESC, "");
            map.put(db.KEY_MATERIAL_DESC2, "");
            map.put(db.KEY_BATCH_MANAGEMENT, "");
            map.put(db.KEY_PRODUCT_HIERARCHY, "");
            map.put(db.KEY_VOLUME_UOM, "");
            map.put(db.KEY_VOLUME, "");
            map.put(db.KEY_WEIGHT_UOM, "");
            map.put(db.KEY_NET_WEIGHT, "");
            map.put(db.KEY_GROSS_WEIGHT, "");
            map.put(db.KEY_ARTICLE_CATEGORY, "");
            map.put(db.KEY_ARTICLE_NO, "");
            map.put(db.KEY_BASE_UOM, "");
            map.put(db.KEY_MATERIAL_GROUP, "");
            map.put(db.KEY_MATERIAL_TYPE, "");
            map.put(db.KEY_MATERIAL_DESC1, "");
            map.put(db.KEY_MATERIAL_NO, "");
            HashMap<String, String> filter = new HashMap<>();
            Cursor cursor = db.getData(db.ARTICLE_HEADER, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                setLoadItems(cursor, false);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    public class loadItemsOrder extends AsyncTask<Void, Void, Void> {
        private String orderId;
        private loadItemsOrder(String orderId) {
            this.orderId = orderId;
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_DATE,"");
            map.put(db.KEY_ITEM_NO, "");
            map.put(db.KEY_MATERIAL_DESC1, "");
            map.put(db.KEY_MATERIAL_NO, "");
            map.put(db.KEY_MATERIAL_GROUP, "");
            map.put(db.KEY_CASE, "");
            map.put(db.KEY_UNIT, "");
            map.put(db.KEY_UOM, "");
            map.put(db.KEY_PRICE, "");
            HashMap<String, String> filter = new HashMap<>();
            // filter.put(db.KEY_ORDER_ID, this.orderId);
            filter.put(db.KEY_PURCHASE_NUMBER, this.orderId);
            Cursor cursor = db.getData(db.ORDER_REQUEST, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                setLoadItems(cursor, true);
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
           loadingSpinner.show();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            adapter.notifyDataSetChanged();
            list.setAdapter(adapter);

        }
    }
    public void setLoadItems(Cursor loadItemsCursor, Boolean isPosted) {
        try{
            Cursor cursor = loadItemsCursor;
            if(isPosted){
                tv_date.setText(cursor.getString(cursor.getColumnIndex(db.KEY_DATE)));
            }
            do {
                OrderRequest loadRequest = new OrderRequest();
                loadRequest.setItemCode(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                loadRequest.setItemName(UrlBuilder.decodeString(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                ArticleHeader article = ArticleHeader.getArticle(articles, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                if (isPosted) {

                    loadRequest.setCases(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM) || cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM) ? cursor.getString(cursor.getColumnIndex(db.KEY_CASE)) : "0");
                    //loadRequest.setUnits(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM) ? cursor.getString(cursor.getColumnIndex(db.KEY_UNIT)) : "0");
                    loadRequest.setUnits("0");
                    loadRequest.setUom(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                    HashMap<String, String> priceMap = new HashMap<>();
                    priceMap.put(db.KEY_AMOUNT, "");
                    HashMap<String, String> filterPrice = new HashMap<>();
                    filterPrice.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    filterPrice.put(db.KEY_PRIORITY, "2");
                    Cursor priceCursor = db.getData(db.PRICING, priceMap, filterPrice);
                    if (priceCursor.getCount() > 0) {
                        priceCursor.moveToFirst();
                        loadRequest.setPrice(priceCursor.getString(priceCursor.getColumnIndex(db.KEY_AMOUNT)));
                    } else {
                        loadRequest.setPrice("0");
                    }
                } else {
                    // loadRequest.setCases(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.CASE_UOM) ? "0" : "0");
                    // loadRequest.setUnits(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.BOTTLES_UOM) ? "0" : "0");
                    loadRequest.setUom(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)));
                    loadRequest.setCases("0");
                    loadRequest.setUnits("0");
                    HashMap<String, String> altMap = new HashMap<>();
                    altMap.put(db.KEY_UOM, "");
                    HashMap<String, String> filter = new HashMap<>();
                    filter.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    Cursor altUOMCursor = db.getData(db.ARTICLE_UOM, altMap, filter);
                    if (altUOMCursor.getCount() > 0) {
                        altUOMCursor.moveToFirst();
                        if (cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(altUOMCursor.getString(altUOMCursor.getColumnIndex(db.KEY_UOM)))) {
                            loadRequest.setIsAltUOM(false);
                        } else {
                            loadRequest.setIsAltUOM(true);
                        }
                    } else {
                        loadRequest.setIsAltUOM(false);
                    }
                    HashMap<String, String> priceMap = new HashMap<>();
                    priceMap.put(db.KEY_AMOUNT, "");
                    HashMap<String, String> filterPrice = new HashMap<>();
                    filterPrice.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    filterPrice.put(db.KEY_PRIORITY, "2");
                    Cursor priceCursor = db.getData(db.PRICING, priceMap, filterPrice);
                    if (priceCursor.getCount() > 0) {
                        priceCursor.moveToFirst();
                        loadRequest.setPrice(priceCursor.getString(priceCursor.getColumnIndex(db.KEY_AMOUNT)));
                    } else {
                        loadRequest.setPrice("0");
                    }
                }
                loadRequest.setMaterialNo(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                arraylist.add(loadRequest);
            }
            while (cursor.moveToNext());
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public class postData extends AsyncTask<Void, Void, Void> {
        private ArrayList<String> returnList;
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
            //Log.e("Order id", "" + this.orderId);
            try{
                if (this.tokens[0].toString().equals(this.tokens[1].toString())) {
                    for (OrderRequest loadRequest : arraylist) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                        map.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                        map.put(db.KEY_ORDER_ID, tokens[0].toString());
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                        filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                        filter.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                        //filter.put(db.KEY_ORDER_ID,tokens[1].toString());
                        filter.put(db.KEY_PURCHASE_NUMBER, tokens[1].toString());
                        db.updateData(db.ORDER_REQUEST, map, filter);
                    }
                    if (loadingSpinner.isShowing()) {
                        loadingSpinner.hide();
                    }
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreSaleOrderProceedActivity.this);
                    alertDialogBuilder  /*.setTitle(getString(R.string.message))*/
                            //.setMessage("Request with reference " + tokens[1].toString() + " has been saved")
                            .setMessage(getString(R.string.request_created))
                                    // .setMessage("Request with reference " + tokens[0].toString() + " has been saved")
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(Helpers.isNetworkAvailable(PreSaleOrderProceedActivity.this)){
                                        Helpers.createBackgroundJob(getApplicationContext());
                                    }
                                    if(isPrint){
                                        dialog.dismiss();
                                        createPrintout(false,tv_date.getText().toString(),tokens[0].toString(),false);
                                       /* Intent intent = new Intent(PreSaleOrderProceedActivity.this, PreSaleOrderActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("headerObj", object);
                                        startActivity(intent);
                                        finish();*/
                                    }
                                    else{

                                        dialog.dismiss();
                                        createPrintout(false,tv_date.getText().toString(),tokens[0].toString(),true);
                                        Intent intent = new Intent(PreSaleOrderProceedActivity.this, PreSaleOrderActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("headerObj", object);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                } else {
                    for (OrderRequest loadRequest : arraylist) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                        map.put(db.KEY_IS_POSTED, App.DATA_IS_POSTED);
                        map.put(db.KEY_ORDER_ID, tokens[0].toString());
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                        filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                        filter.put(db.KEY_MATERIAL_NO, loadRequest.getMaterialNo());
                        //filter.put(db.KEY_ORDER_ID,tokens[1].toString());
                        filter.put(db.KEY_PURCHASE_NUMBER, tokens[1].toString());
                        db.updateData(db.ORDER_REQUEST, map, filter);
                    }
                    if (loadingSpinner.isShowing()) {
                        loadingSpinner.hide();
                    }
                    if (this.orderId.isEmpty() || this.orderId.equals("") || this.orderId == null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.request_timeout), Toast.LENGTH_SHORT).show();
                    } else if (this.orderId.contains("Error")) {
                        Toast.makeText(getApplicationContext(), this.orderId.replaceAll("Error", "").trim(), Toast.LENGTH_SHORT).show();
                    } else {
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
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    private String postData() {
        String orderID = "";
        String purchaseNumber = "";
        float orderTotalValue = 0;
        try {
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

            map.put("Currency", "SAR");
            // map.put("PurchaseNum", Helpers.generateNumber(db, ConfigStore.LoadRequest_PR_Type));
            JSONArray deepEntity = new JSONArray();
            HashMap<String, String> itemMap = new HashMap<>();
            itemMap.put(db.KEY_DATE,"");
            itemMap.put(db.KEY_ITEM_NO, "");
            itemMap.put(db.KEY_MATERIAL_NO, "");
            itemMap.put(db.KEY_MATERIAL_DESC1, "");
            itemMap.put(db.KEY_CASE, "");
            itemMap.put(db.KEY_UNIT, "");
            itemMap.put(db.KEY_UOM, "");
            itemMap.put(db.KEY_PRICE, "");
            itemMap.put(db.KEY_ORDER_ID, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            Cursor cursor = db.getData(db.ORDER_REQUEST, itemMap, filter);
            //Log.e("Cursor count", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                map.put("PurchaseNum", cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID)));
                map.put("DocumentDate", Helpers.parseDateforPost(cursor.getString(cursor.getColumnIndex(db.KEY_DATE))));
                purchaseNumber = map.get("PurchaseNum");
                int itemno = 10;
                do {
                    if (cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)) {
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                        jo.put("Material", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant", "");
                        jo.put("Quantity", cursor.getString(cursor.getColumnIndex(db.KEY_CASE)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("UoM", cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("Storagelocation", "");
                        jo.put("Route", Settings.getString(App.ROUTE));
                        itemno = itemno + 10;
                        orderTotalValue+= Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_CASE)))*Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        deepEntity.put(jo);
                    }
                    else {
                        JSONObject jo = new JSONObject();
                        jo.put("Item", Helpers.getMaskedValue(String.valueOf(itemno), 4));
                        jo.put("Material", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                        jo.put("Description", cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                        jo.put("Plant", "");
                        jo.put("Quantity", cursor.getString(cursor.getColumnIndex(db.KEY_UNIT)));
                        jo.put("ItemValue", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("UoM", cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                        jo.put("Value", cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        jo.put("Storagelocation", "");
                        jo.put("Route", Settings.getString(App.ROUTE));
                        orderTotalValue+= Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_UNIT)))*Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_PRICE)));
                        itemno = itemno + 10;
                        deepEntity.put(jo);
                    }
                }
                while (cursor.moveToNext());
                //map.put("OrderValue", String.valueOf(orderTotalValue));
                map.put("OrderValue",discount<=0?String.valueOf(totalamnt+discount):String.valueOf(totalamnt-discount));
                //map.put("OrderValue", String.valueOf(totalamnt-discount));
            }
            Log.e("Map", "" + map);
            //Log.e("Deep Entity", "" + deepEntity);
            orderID = IntegrationService.postData(PreSaleOrderProceedActivity.this, App.POST_COLLECTION, map, deepEntity);
           // orderID = IntegrationService.postDataBackup(PreSaleOrderProceedActivity.this, App.POST_COLLECTION, map, deepEntity);
            //Storing Order Activity for Logging
            HashMap<String,String>logMap = new HashMap<>();
            logMap.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
            logMap.put(db.KEY_ACTIVITY_TYPE, App.ACTIVITY_ORDER);
            logMap.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
            logMap.put(db.KEY_ORDER_ID,orderID);
            logMap.put(db.KEY_PRICE,map.get("OrderValue"));
            db.addData(db.DAYACTIVITY,logMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderID + "," + purchaseNumber;
    }
    public class loadData extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            recalculateTotal();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            new loadPromotions(App.Promotions02);
            new loadPromotions(App.Promotions05);
            new loadPromotions(App.Promotions07);
        }
    }
    public class loadPromotions extends AsyncTask<Void,Void,Void>{
        private String promoCode;
        private loadPromotions(String promoCode) {
            // Log.e("I m in for Load Promotions", "" + source);
            this.promoCode = promoCode;
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
                applyPromotions(cursor);
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
                onProcessingComplete();
            }
        }
    }
    private void applyPromotions(Cursor cursor){
        try{
            Cursor promotionCursor = cursor;
            promotionCursor.moveToFirst();
            do{
                for(OrderRequest request:arraylist){
                    if(request.getMaterialNo().equals(promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_MATERIAL_NO)))){
                        if(request.getUom().equals(App.CASE_UOM)||request.getUom().equals(App.BOTTLES_UOM)){
                            float cases = Float.parseFloat(request.getCases());
                            discount += cases*(Float.parseFloat(promotionCursor.getString(promotionCursor.getColumnIndex(db.KEY_AMOUNT))));
                        }
                    }
                }
            }
            while (promotionCursor.moveToNext());
            Log.e("Discount","" + discount);
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
    public void recalculateTotal(){
        try{
            float amount=0;
            for(OrderRequest order:arraylist){
                float tempPrice = 0;
                HashMap<String,String> filterComp = new HashMap<>();
                filterComp.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                filterComp.put(db.KEY_MATERIAL_NO, order.getMaterialNo());
                HashMap<String,String> map = new HashMap<>();
                map.put(db.KEY_MATERIAL_NO,"");
                map.put(db.KEY_AMOUNT,"");
                if(db.checkData(db.PRICING,filterComp)){
                    Cursor customerPriceCursor = db.getData(db.PRICING,map,filterComp);
                    if(customerPriceCursor.getCount()>0){
                        customerPriceCursor.moveToFirst();
                        tempPrice = Float.parseFloat(customerPriceCursor.getString(customerPriceCursor.getColumnIndex(db.KEY_AMOUNT)));
                    }
                    if(order.getUom().equals(App.CASE_UOM)||order.getUom().equals(App.BOTTLES_UOM)){
                        amount += tempPrice*Float.parseFloat(order.getCases());
                    }
                    else{
                        amount += tempPrice*Float.parseFloat(order.getUnits());
                    }
                }
                else{
                    if(order.getUom().equals(App.CASE_UOM)||order.getUom().equals(App.BOTTLES_UOM)){
                        amount += Float.parseFloat(order.getPrice())*Float.parseFloat(order.getCases());
                        //amount += Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_AMOUNT)));
                    }
                    else {
                        amount += Float.parseFloat(order.getPrice())*Float.parseFloat(order.getUnits());
                    }
                }
            }
            totalamnt = amount;
            Log.e("Total Amount","" + totalamnt);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public boolean checkforNullBeforePost(){
        HashMap<String,String>map = new HashMap<>();
        map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
        map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
        map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
        return db.checkData(db.ORDER_REQUEST,map);
    }
    private void createPrintout(boolean fromList,String orderDate,String orderNo,boolean isDelayPrint){
        Log.e("Came for Print", "Came for");
        if(!isDelayPrint){
            if(fromList){
                JSONArray jsonArray = createPrintData(orderDate,orderNo);
                PrinterHelper object = new PrinterHelper(PreSaleOrderProceedActivity.this,PreSaleOrderProceedActivity.this);
                object.execute("", jsonArray);
            }
            else{
                JSONArray jsonArray = createPrintData(orderDate,orderNo);
                PrinterHelper object = new PrinterHelper(PreSaleOrderProceedActivity.this,PreSaleOrderProceedActivity.this);
                object.execute("", jsonArray);
            }
        }
        else{
            if(fromList){
                try{
                    JSONArray jsonArray = createPrintData(orderDate,orderNo);
                    JSONObject data = new JSONObject();
                    data.put("data",(JSONArray)jsonArray);

                    HashMap<String,String>map = new HashMap<>();
                    map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                    map.put(db.KEY_ORDER_ID,orderNo);
                    map.put(db.KEY_DOC_TYPE,ConfigStore.OrderRequest_TR);
                    map.put(db.KEY_DATA,data.toString());
                    db.addDataPrint(db.DELAY_PRINT,map);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
            else{
                try{
                    JSONArray jsonArray = createPrintData(orderDate,orderNo);
                    JSONObject data = new JSONObject();
                    data.put("data",(JSONArray)jsonArray);

                    HashMap<String,String>map = new HashMap<>();
                    map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                    map.put(db.KEY_ORDER_ID,orderNo);
                    map.put(db.KEY_DOC_TYPE,ConfigStore.OrderRequest_TR);
                    map.put(db.KEY_DATA,data.toString());
                    //map.put(db.KEY_DATA,jsonArray.toString());
                    db.addDataPrint(db.DELAY_PRINT,map);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                /*JSONArray jsonArray = createPrintData(orderDate,orderNo);
                PrinterHelper object = new PrinterHelper(PreSaleOrderProceedActivity.this,PreSaleOrderProceedActivity.this);
                object.execute("", jsonArray);*/
            }
        }

    }
    public JSONArray createPrintData(String orderDate,String orderNo){
        JSONArray jArr = new JSONArray();
        try{
            double totalPcs = 0;
            double totalAmount = 0;
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST,App.ORDER_REQUEST);
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
            mainArr.put("CUSTOMER", object.getCustomerName() + "-" + (object.getCustomer_name_ar()==null||object.getCustomer_name_ar().equals("")?testAr: object.getCustomer_name_ar()));
            mainArr.put("ADDRESS",object.getCustomerAddress().equals("")?object.getCustomerAddress():"This is just test address");
            mainArr.put("ARBADDRESS",object.getCustomerAddress());
            mainArr.put("TripID",Settings.getString(App.TRIP_ID));
            //mainArr.put("Load Number","1");


            JSONArray HEADERS = new JSONArray();
            JSONArray TOTAL = new JSONArray();

            HEADERS.put("ITEM NO");
            HEADERS.put("ENGLISH DESCRIPTION");
            HEADERS.put("ARABIC DESCRIPTION");
            HEADERS.put("UPC ");
            HEADERS.put("TOTAL UNITS");
            HEADERS.put("UNIT PRICE");
            HEADERS.put("AMOUNT");
            //HEADERS.put("Description");

            //HEADERS.put(obj1);
            // HEADERS.put(obj2);
            mainArr.put("HEADERS",HEADERS);


            JSONArray jData = new JSONArray();
            for(OrderRequest obj:arraylist){
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

            }
            JSONObject totalObj = new JSONObject();
            totalObj.put("TOTAL UNITS","+" + String.valueOf(totalPcs));
            totalObj.put("UNIT PRICE","");
            totalObj.put("AMOUNT","+" + String.valueOf(totalAmount));
            TOTAL.put(totalObj);
            mainArr.put("TOTAL",TOTAL);
            mainArr.put("data",jData);

            jDict.put("mainArr",mainArr);
            jInter.put(jDict);
            jArr.put(jInter);

            // jArr.put(HEADERS);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jArr;
    }
    public void callback(){
        Intent intent = new Intent(PreSaleOrderProceedActivity.this, PreSaleOrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("headerObj", object);
        startActivity(intent);
        finish();
    }
}
