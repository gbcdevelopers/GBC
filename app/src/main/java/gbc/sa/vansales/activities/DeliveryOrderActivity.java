package gbc.sa.vansales.activities;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DeliveryItemBadgeAdapter;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.DeliveryItem;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.models.PreSaleProceed;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.UrlBuilder;
public class DeliveryOrderActivity extends AppCompatActivity {
    ArrayList<PreSaleProceed> preSaleProceeds = new ArrayList<>();
    ImageView iv_back;
    TextView tv_top_header;
    FloatingActionButton float_presale_proceed;
    int arrSize = 3;
    ImageView iv_calendar;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    RelativeLayout btn_confirm_delivery;
    TextView tv_date;
    TextView tv_amt;
    ArrayList<EditText[]> editTextArrayList;
    Customer object;
    OrderList delivery;
    ArrayList<CustomerHeader> customers;
    LoadingSpinner loadingSpinner;
    DatabaseHandler db = new DatabaseHandler(this);
    ListView deliveryItemsList;
    ArrayList<DeliveryItem> arrayList;
    DeliveryItemBadgeAdapter adapter;

    public ArrayList<ArticleHeader> articles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order);
        articles = ArticleHeaders.get();
        loadingSpinner = new LoadingSpinner(this);
        deliveryItemsList = (ListView)findViewById(R.id.list_delivery_items);
        arrayList = new ArrayList<>();
        adapter = new DeliveryItemBadgeAdapter(this,arrayList);

        Intent i = this.getIntent();
        object = (Customer)i.getParcelableExtra("headerObj");
        delivery = (OrderList)i.getParcelableExtra("delivery");
        customers = CustomerHeaders.get();

        CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, object.getCustomerID());
        TextView tv_customer_name = (TextView) findViewById(R.id.tv_customer_id);
        TextView tv_customer_address = (TextView) findViewById(R.id.tv_customer_address);
        TextView tv_customer_pobox = (TextView) findViewById(R.id.tv_customer_pobox);
        TextView tv_customer_contact = (TextView) findViewById(R.id.tv_customer_contact);


        if (!(customerHeader == null)) {
            tv_customer_name.setText(customerHeader.getCustomerNo() + " " + customerHeader.getName1());
            tv_customer_address.setText(UrlBuilder.decodeString(customerHeader.getStreet()));
            tv_customer_pobox.setText("PO Code " + customerHeader.getPostCode());
            tv_customer_contact.setText(customerHeader.getPhone());
        } else {
            tv_customer_name.setText(object.getCustomerID().toString() + " " + object.getCustomerName().toString());
            tv_customer_address.setText(object.getCustomerAddress().toString());
            tv_customer_pobox.setText("");
            tv_customer_contact.setText("");
        }
        deliveryItemsList.setAdapter(adapter);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_date.setText(delivery.getOrderDate());
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.delivery_order));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_amt = (TextView) findViewById(R.id.tv_amt);
        editTextArrayList = new ArrayList<>();
        iv_calendar = (ImageView) findViewById(R.id.iv_calander_presale_proced);
        btn_confirm_delivery = (RelativeLayout) findViewById(R.id.btn_confirm_delivery);
        float_presale_proceed = (FloatingActionButton) findViewById(R.id.float_presale_proceed);
        float_presale_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrSize = arrSize + 1;
                //setData();
            }
        });
        new loadDeliveryItems().execute();


        //setData();
        btn_confirm_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent = new Intent(DeliveryOrderActivity.this, PromotionActivity.class);
                intent.putExtra("msg", "delivery");
                intent.putExtra("headerObj", object);
                intent.putExtra("delivery", delivery);
                intent.putExtra("invoiceamount",tv_amt.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        myCalendar = Calendar.getInstance();
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
        updateLabel(year, month, day);
        iv_calendar.setEnabled(false);
        iv_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DeliveryOrderActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                updateLabel(year, monthOfYear, dayOfMonth);
            }
        };
    }
    public void setData(Cursor cursor) {
        if (preSaleProceeds != null) {
            preSaleProceeds.clear();
        }
        do{
            DeliveryItem deliveryItem = new DeliveryItem();
            deliveryItem.setItemCode(cursor.getString(cursor.getColumnIndex(db.KEY_ITEM_NO)));
            deliveryItem.setMaterialNo(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));

            ArticleHeader article = ArticleHeader.getArticle(articles, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
            if(article!=null){
                deliveryItem.setItemDescription(UrlBuilder.decodeString(article.getMaterialDesc1()));
            }
            else{
                deliveryItem.setItemDescription(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
            }
//            proceed.setPRODUCT_NAME("Berain 250 ml");

            if(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)||cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM_NEW))
            {
                deliveryItem.setItemCase(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)));
            }
            else{
                deliveryItem.setItemCase("0");
            }
            if(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM))
            {
                deliveryItem.setItemUnits(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)));
            }
            else{
                deliveryItem.setItemUnits("0");
            }
            deliveryItem.setItemUom(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
            deliveryItem.setAmount("1");
            arrayList.add(deliveryItem);
        }
        while (cursor.moveToNext());
    }
    private void updateLabel(int year, int monthOfYear, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String myFormat = "dd/MM" +
                "/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        //tv_date.setText(sdf.format(myCalendar.getTime()));
    }
//    private void setLayout() {
//        double totalamt = 0;
//        LinearLayout options_layout = (LinearLayout) findViewById(R.id.ll_presale_proceed_main);
//        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
//        if (options_layout != null) {
//            options_layout.removeAllViews();
//        }
//        Log.d("size", "-->" + preSaleProceeds.size());
//        for (int i = 0; i < preSaleProceeds.size(); i++) {
//            Log.d("i", "-->" + i);
//            View to_add = inflater.inflate(R.layout.presale_proceed_list_item,
//                    options_layout, false);
//            PreSaleProceed saleProceed = preSaleProceeds.get(i);
//            EditText text = (EditText) to_add.findViewById(R.id.tv_sku_pre_proceed);
//            final EditText text1 = (EditText) to_add.findViewById(R.id.tv_ctn_pre_proceed);
//            EditText text2 = (EditText) to_add.findViewById(R.id.tv_btl_pre_proceed);
//            TextView text3 = (TextView) to_add.findViewById(R.id.tv_price);
//            text3.setVisibility(View.GONE);
//            text.setText(saleProceed.getPRODUCT_NAME());
//            text1.setText(saleProceed.getCTN());
//            text2.setText(saleProceed.getBTL());
//            text3.setText(saleProceed.getPRICE());
//            totalamt = totalamt + (Double.parseDouble(saleProceed.getCTN()) * 54 + Double.parseDouble(saleProceed.getBTL()) * 2.25);
//            EditText ed[] = {text1, text2};
//            editTextArrayList.add(ed);
////            text.setTypeface(FontSelector.getBold(getActivity()));
//            options_layout.addView(to_add);
//            text1.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                }
//                @Override
//                public void afterTextChanged(Editable s) {
//                    if (!text1.getText().toString().equals("")) {
//                        double totalamt = 0;
//                        for (int i = 0; i < editTextArrayList.size(); i++) {
//                            EditText ed[] = editTextArrayList.get(i);
//                            EditText text1 = ed[0];
//                            EditText text2 = ed[1];
//                            totalamt = totalamt + (Double.parseDouble(text1.getText().toString()) * 54 + Double.parseDouble(text2.getText().toString()) * 2.25);
//                        }
//                        tv_amt.setText(String.valueOf(totalamt));
//                    }
//                }
//            });
//        }
//        tv_amt.setText(String.valueOf(totalamt));
//    }

    public void calculatePrice() {
        double totalamt = 0;

        for (int i = 0; i < arrayList.size(); i++) {
            DeliveryItem item = arrayList.get(i);
            String itemCase = item.getItemCase().equals("")||item.getItemCase().isEmpty()||item.getItemCase()==null?"0":item.getItemCase();
            String itemUnit = item.getItemUnits().equals("")||item.getItemUnits().isEmpty()||item.getItemUnits()==null?"0":item.getItemUnits();
            totalamt = totalamt + (Double.parseDouble(itemCase) * 54 + Double.parseDouble(itemUnit) * 2.25);
        }
        tv_amt.setText(String.valueOf(totalamt));
    }
    private void saveData(){
        double totalamt = 0;
        String purchaseNum = Helpers.generateNumber(db, ConfigStore.CustomerDeliveryRequest_PR_Type);
        for (int i = 0; i < arrayList.size(); i++) {
            DeliveryItem item = arrayList.get(i);
            String itemCase = item.getItemCase().equals("")||item.getItemCase().isEmpty()||item.getItemCase()==null?"0":item.getItemCase();
            String itemUnit = item.getItemUnits().equals("")||item.getItemUnits().isEmpty()||item.getItemUnits()==null?"0":item.getItemUnits();
            totalamt = totalamt + (Double.parseDouble(itemCase) * 54 + Double.parseDouble(itemUnit) * 2.25);
        }
        for(DeliveryItem item:arrayList){
            try{
                if((item.getItemCase().isEmpty()||item.getItemCase().equals("")||item.getItemCase()==null)){
                    item.setItemCase("0");
                }
                if((item.getItemUnits().isEmpty()||item.getItemUnits().equals("")||item.getItemUnits()==null)){
                    item.setItemUnits("0");
                }

                HashMap<String,String> map = new HashMap<String, String>();
                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                map.put(db.KEY_DELIVERY_NO,delivery.getOrderId());
                map.put(db.KEY_ITEM_NO,item.getItemCode());
                map.put(db.KEY_MATERIAL_NO ,item.getMaterialNo());
                map.put(db.KEY_MATERIAL_DESC1,item.getItemDescription());
                map.put(db.KEY_CASE ,item.getItemCase());
                map.put(db.KEY_UNIT ,item.getItemUnits());
                map.put(db.KEY_UOM,item.getItemUom());
                map.put(db.KEY_AMOUNT, String.valueOf(totalamt));
                map.put(db.KEY_ORDER_ID,purchaseNum);
                map.put(db.KEY_PURCHASE_NUMBER,purchaseNum);
                map.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                map.put(db.KEY_IS_PRINTED, "");
                if(Float.parseFloat(item.getItemCase())>0 || Float.parseFloat(item.getItemUnits())>0){
                    db.addData(db.CUSTOMER_DELIVERY_ITEMS_POST,map);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class loadDeliveryItems extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_ID,"");
            map.put(db.KEY_DELIVERY_NO,"");
            map.put(db.KEY_ITEM_NO,"");
            map.put(db.KEY_ITEM_CATEGORY,"");
            map.put(db.KEY_CREATED_BY,"");
            map.put(db.KEY_ENTRY_TIME,"");
            map.put(db.KEY_DATE ,"");
            map.put(db.KEY_MATERIAL_NO ,"");
            map.put(db.KEY_MATERIAL_ENTERED ,"");
            map.put(db.KEY_MATERIAL_GROUP ,"");
            map.put(db.KEY_PLANT ,"");
            map.put(db.KEY_STORAGE_LOCATION ,"");
            map.put(db.KEY_BATCH ,"");
            map.put(db.KEY_ACTUAL_QTY,"");
            map.put(db.KEY_REMAINING_QTY,"");
            map.put(db.KEY_UOM,"");
            map.put(db.KEY_DIST_CHANNEL ,"");
            map.put(db.KEY_DIVISION ,"");
            map.put(db.KEY_IS_DELIVERED,"");

            HashMap<String,String>filter = new HashMap<>();
            filter.put(db.KEY_DELIVERY_NO,delivery.getOrderId());

            Cursor cursor = db.getData(db.CUSTOMER_DELIVERY_ITEMS,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();

                setData(cursor);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            calculatePrice();
            adapter.notifyDataSetChanged();
        }
    }


}
