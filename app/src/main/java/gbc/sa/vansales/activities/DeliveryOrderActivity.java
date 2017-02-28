package gbc.sa.vansales.activities;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import gbc.sa.vansales.App;
import gbc.sa.vansales.Fragment.BListFragment;
import gbc.sa.vansales.Fragment.GListFragment;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerStatusAdapter;
import gbc.sa.vansales.adapters.DeliveryItemBadgeAdapter;
import gbc.sa.vansales.adapters.ReasonAdapter;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.data.OrderReasons;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.CustomerStatus;
import gbc.sa.vansales.models.DeliveryItem;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.models.PreSaleProceed;
import gbc.sa.vansales.models.Reasons;
import gbc.sa.vansales.models.Sales;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
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
    LinearLayout custLayout;
    LinearLayout labelView;
    FloatingActionButton edit;
    FloatingActionButton ok;
    ArrayAdapter<CustomerStatus> myAdapter;
    ArrayAdapter<Reasons> reasonsAdapter;
    boolean canEdit = false;
    public ArrayList<ArticleHeader> articles;
    private ArrayList<Reasons> reasonsList = new ArrayList<>();
    private ArrayList<CustomerStatus> rejectReasonList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order);
        articles = ArticleHeaders.get();
        loadingSpinner = new LoadingSpinner(this);
        deliveryItemsList = (ListView) findViewById(R.id.list_delivery_items);
        Helpers.logData(DeliveryOrderActivity.this,"Delivery Details Opened");
        arrayList = new ArrayList<>();
        adapter = new DeliveryItemBadgeAdapter(this, arrayList);
        custLayout = (LinearLayout) findViewById(R.id.ll_common);
        labelView = (LinearLayout) findViewById(R.id.labelView);
        custLayout.setVisibility(View.GONE);
        labelView.setVisibility(View.GONE);
        reasonsList = OrderReasons.get();
        reasonsAdapter = new ReasonAdapter(DeliveryOrderActivity.this, android.R.layout.simple_spinner_item, reasonsList);
        myAdapter = new CustomerStatusAdapter(this, rejectReasonList);
        loadRejectReasons();
        edit = (FloatingActionButton) findViewById(R.id.edit);
        Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");
        delivery = (OrderList) i.getParcelableExtra("delivery");
        customers = CustomerHeaders.get();
        CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, object.getCustomerID());
        TextView tv_customer_name = (TextView) findViewById(R.id.tv_customer_id);
        TextView tv_customer_address = (TextView) findViewById(R.id.tv_customer_address);
        TextView tv_customer_pobox = (TextView) findViewById(R.id.tv_customer_pobox);
        TextView tv_customer_contact = (TextView) findViewById(R.id.tv_customer_contact);
        if (!(customerHeader == null)) {
            tv_customer_name.setText(StringUtils.stripStart(customerHeader.getCustomerNo(), "0") + " " + customerHeader.getName1());
            tv_customer_address.setText(UrlBuilder.decodeString(customerHeader.getStreet()));
            tv_customer_pobox.setText(getString(R.string.pobox) + " " + customerHeader.getPostCode());
            tv_customer_contact.setText(customerHeader.getPhone());
        } else {
            tv_customer_name.setText(StringUtils.stripStart(object.getCustomerID(), "0") + " " + object.getCustomerName().toString());
            tv_customer_address.setText(object.getCustomerAddress().toString());
            tv_customer_pobox.setText("");
            tv_customer_contact.setText("");
        }

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_date.setText(delivery.getOrderDate());
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.delivery_order) + "(" + StringUtils.stripStart(delivery.getOrderId(), "0") + ")");
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
        //registerForContextMenu(deliveryItemsList);
        btn_confirm_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helpers.logData(DeliveryOrderActivity.this,"Confirm Button for Delivery Clicked");
                saveData();
                // Intent intent = new Intent(DeliveryOrderActivity.this, PromotionActivity.class);
                Intent intent = new Intent(DeliveryOrderActivity.this, PromotionListActivity.class);
                intent.putExtra("msg", "delivery");
                intent.putExtra("from", "delivery");
                intent.putExtra("headerObj", object);
                intent.putExtra("delivery", delivery);
                intent.putExtra("invoiceamount", tv_amt.getText().toString());
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
        deliveryItemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try{
                    Helpers.logData(DeliveryOrderActivity.this,"Delivery Item Clicked");
                    final DeliveryItem item = arrayList.get(position);
                    Helpers.logData(DeliveryOrderActivity.this,"Delivery Item Selected" + item.getMaterialNo());
                    final Dialog dialog = new Dialog(DeliveryOrderActivity.this);
                    final String[] reasonCode = {""};
                    dialog.setContentView(R.layout.dialog_with_crossbutton);
                    dialog.setCancelable(false);
                    TextView tv = (TextView) dialog.findViewById(R.id.dv_title);
                    tv.setText(item.getItemDescription());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    ImageView iv_cancle = (ImageView) dialog.findViewById(R.id.imageView_close);
                    Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
                    final EditText ed_cases = (EditText) dialog.findViewById(R.id.ed_cases);
                    final EditText ed_pcs = (EditText) dialog.findViewById(R.id.ed_pcs);
                    final EditText ed_cases_inv = (EditText) dialog.findViewById(R.id.ed_cases_inv);
                    final EditText ed_pcs_inv = (EditText) dialog.findViewById(R.id.ed_pcs_inv);
                    RelativeLayout rl_specify = (RelativeLayout) dialog.findViewById(R.id.rl_specify_reason);
                    rl_specify.setVisibility(View.VISIBLE);
                    final Spinner spin = (Spinner) dialog.findViewById(R.id.spin);
                    spin.setAdapter(reasonsAdapter);
                    if (item.getReasonCode() != null) {
                        spin.setSelection(getIndex(item.getReasonCode()));
                    }
                    spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            reasonCode[0] = reasonsList.get(position).getReasonID();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(db.KEY_REMAINING_QTY_CASE, "");
                    map.put(db.KEY_REMAINING_QTY_UNIT, "");
                    HashMap<String, String> filter = new HashMap<String, String>();
                    filter.put(db.KEY_MATERIAL_NO, item.getMaterialNo());
                    Cursor c = db.getData(db.VAN_STOCK_ITEMS, map, filter);
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        if (!item.isAltUOM()) {
                            ed_cases_inv.setText(c.getString(c.getColumnIndex(db.KEY_REMAINING_QTY_CASE)));
                        } else {
                            ed_pcs_inv.setText(c.getString(c.getColumnIndex(db.KEY_REMAINING_QTY_UNIT)));
                        }
                    } else {
                        ed_cases_inv.setText("0");
                        ed_pcs_inv.setText("0");
                    }
                    ed_cases_inv.setEnabled(false);
                    ed_pcs_inv.setEnabled(false);
                    if (item.isAltUOM()) {
                        ed_pcs.setEnabled(true);
                    } else {
                        ed_pcs.setEnabled(false);
                    }
                    //ed_cases.setText(item.getItemCase());
                    //ed_pcs.setText(item.getItemUnits());

                    ed_cases.setText(item.getItemCase().equals("0")?"":item.getItemCase());
                    ed_pcs.setText(item.getItemUnits().equals("0")?"":item.getItemUnits());

                    LinearLayout ll_1 = (LinearLayout) dialog.findViewById(R.id.ll_1);
                    iv_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    if (canEdit) {
                        Helpers.logData(DeliveryOrderActivity.this,"Edit Is flagged on. Showing dialog");
                        dialog.show();
                    } else {
                    }
                    btn_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String strCase = ed_cases.getText().toString();
                            String strpcs = ed_pcs.getText().toString();
                            String strcaseinv = ed_cases_inv.getText().toString();
                            String strpcsinv = ed_pcs_inv.getText().toString();
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
                            if (Float.parseFloat(strCase) > Float.parseFloat(strcaseinv)) {
                                Toast.makeText(DeliveryOrderActivity.this, getString(R.string.input_larger), Toast.LENGTH_SHORT).show();
                                strCase = "0";
                                item.setItemCase("0");
                            } else if (Float.parseFloat(strpcs) > Float.parseFloat(strpcsinv)) {
                                Toast.makeText(DeliveryOrderActivity.this, getString(R.string.input_larger), Toast.LENGTH_SHORT).show();
                                strpcs = "0";
                                item.setItemUnits(strpcs);
                            } else {
                                item.setItemCase(strCase.trim().equals("")?"0":strCase.trim());
                                item.setItemUnits(strpcs.trim().equals("")?"0":strpcs.trim());
                                //item.setItemCase(strCase);
                                //item.setItemUnits(strpcs);
                                item.setReasonCode(reasonCode[0]);
                                arrayList.remove(position);
                                arrayList.add(position, item);
                                Helpers.logData(DeliveryOrderActivity.this, "Item details changed and saved" + item.getMaterialNo() +"-" + item.getItemCase()
                                        + "-" + item.getItemUnits() + "-" + item.getReasonCode() + "-" + item.getAmount());
                                calculatePrice();
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        deliveryItemsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helpers.logData(DeliveryOrderActivity.this, "Edit button clicked");
                if (canEdit) {
                    canEdit = false;
                } else {
                    canEdit = true;
                }
            }
        });
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.list_delivery_items) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }
    private int getIndex(String myString) {
        int index = 0;
        for (int i = 0; i < reasonsList.size(); i++) {
            Reasons reason = reasonsList.get(i);
            if (reason.getReasonID().equals(myString)) {
                index = i;
            }
        }
        return index;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.remove:
                // add stuff here
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeliveryOrderActivity.this);
                alertDialogBuilder.setTitle(getString(R.string.message))
                        .setMessage(getString(R.string.delete_msg))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                showReasonDialog(arrayList, info.position);

                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();

                return true;
            case R.id.cancel:
                // edit stuff here
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    private void loadRejectReasons() {
        try{
            for (Reasons reason : reasonsList) {
                CustomerStatus status = new CustomerStatus();
                if (reason.getReasonType().equals(App.REASON_REJECT)) {
                    status.setReasonCode(reason.getReasonID());
                    if(Settings.getString(App.LANGUAGE).equals("en")){
                        status.setReasonDescription(UrlBuilder.decodeString(reason.getReasonDescription()));
                    }
                    else{
                        status.setReasonDescription(UrlBuilder.decodeString(reason.getReasonDescriptionAr()));
                    }

                    rejectReasonList.add(status);
                }
            }
            myAdapter.notifyDataSetChanged();
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
    private void showReasonDialog(ArrayList<DeliveryItem> list, final int position) {
        try {
            final int pos = position;
            final Dialog dialog = new Dialog(DeliveryOrderActivity.this);
            //dialog.setTitle(getString(R.string.shop_status));
            View view = getLayoutInflater().inflate(R.layout.activity_select_customer_status, null);
            TextView tv_header = (TextView) view.findViewById(R.id.tv_top_header);
            tv_header.setText(getString(R.string.select_reason));
            Button cancel = (Button) view.findViewById(R.id.btnCancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ListView lv = (ListView) view.findViewById(R.id.statusList);
            lv.setAdapter(myAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Check if there is any existing entry for same delivery no for deletion for purchase number
                    String purchaseNumber = "";
                    HashMap<String,String>filter = new HashMap<String, String>();
                    filter.put(db.KEY_DELIVERY_NO,delivery.getOrderId());
                    filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                    if(db.checkData(db.CUSTOMER_DELIVERY_ITEMS_DELETE_POST,filter)){
                        HashMap<String,String>map = new HashMap<String, String>();
                        map.put(db.KEY_ORDER_ID,"");
                        Cursor cursor = db.getData(db.CUSTOMER_DELIVERY_ITEMS_DELETE_POST,map,filter);
                        cursor.moveToFirst();
                        purchaseNumber = cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID));
                    }

                    DeliveryItem deliveryItem = arrayList.get(pos);
                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
                    map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                    map.put(db.KEY_DELIVERY_NO,delivery.getOrderId());
                    map.put(db.KEY_ITEM_NO,deliveryItem.getItemCode());
                    map.put(db.KEY_MATERIAL_NO,deliveryItem.getMaterialNo());
                    map.put(db.KEY_MATERIAL_DESC1,deliveryItem.getItemDescription());
                    map.put(db.KEY_CASE,deliveryItem.getItemCase());
                    map.put(db.KEY_UNIT,deliveryItem.getItemUnits());
                    map.put(db.KEY_UOM,deliveryItem.getItemUom());
                    Log.e("Reject Reason","" + rejectReasonList.get(position).getReasonCode() + rejectReasonList.get(pos).getReasonCode());
                    map.put(db.KEY_REASON_CODE,rejectReasonList.get(position).getReasonCode());
                    map.put(db.KEY_REASON_DESCRIPTION,rejectReasonList.get(position).getReasonDescription());
                    map.put(db.KEY_ORDER_ID,purchaseNumber.equals("")?Helpers.generateNumber(db, ConfigStore.CustomerDeliveryDelete_PR_Type):purchaseNumber);
                    map.put(db.KEY_PURCHASE_NUMBER,purchaseNumber.equals("")?Helpers.generateNumber(db, ConfigStore.CustomerDeliveryDelete_PR_Type):purchaseNumber);
                    map.put(db.KEY_AMOUNT,deliveryItem.getAmount());
                    map.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                    map.put(db.KEY_IS_PRINTED, App.DATA_MARKED_FOR_POST);
                    //Adding item for delete in post
                    db.addData(db.CUSTOMER_DELIVERY_ITEMS_DELETE_POST, map);
                    //Update the same in delivery items table
                    HashMap<String,String>updateMap = new HashMap<String, String>();
                    updateMap.put(db.KEY_IS_DELIVERED, App.DELETED);
                    HashMap<String,String>filterMap = new HashMap<String, String>();
                    filterMap.put(db.KEY_MATERIAL_NO,deliveryItem.getMaterialNo());
                    filterMap.put(db.KEY_DELIVERY_NO, delivery.getOrderId());
                    db.updateData(db.CUSTOMER_DELIVERY_ITEMS,updateMap,filterMap);

                    arrayList.remove(pos);
                    dialog.dismiss();
                    if(Helpers.isNetworkAvailable(DeliveryOrderActivity.this)){
                        Helpers.createBackgroundJob(DeliveryOrderActivity.this);
                    }
                    adapter.notifyDataSetChanged();
                    calculatePrice();
                    if (arrayList.size() == 0) {
                        finish();
                    }
                }
            });
            dialog.setContentView(view);
            dialog.setCancelable(false);
            dialog.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setData(Cursor cursor) {
        try {
            if (preSaleProceeds != null) {
                preSaleProceeds.clear();
            }
            cursor.moveToFirst();
            do {
                DeliveryItem deliveryItem = new DeliveryItem();
                deliveryItem.setItemCode(cursor.getString(cursor.getColumnIndex(db.KEY_ITEM_NO)));
                deliveryItem.setMaterialNo(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                ArticleHeader article = ArticleHeader.getArticle(articles, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                if (article != null) {
                    deliveryItem.setItemDescription(UrlBuilder.decodeString(article.getMaterialDesc1()));
                } else {
                    deliveryItem.setItemDescription(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                }
    //            proceed.setPRODUCT_NAME("Berain 250 ml");
                if (cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM) || cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM_NEW) || cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)) {
                    deliveryItem.setItemCase(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)));
                } else {
                    deliveryItem.setItemCase("0");
                }
                HashMap<String, String> altMap = new HashMap<>();
                altMap.put(db.KEY_UOM, "");
                HashMap<String, String> filter = new HashMap<>();
                filter.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                Cursor altUOMCursor = db.getData(db.ARTICLE_UOM, altMap, filter);
                if (altUOMCursor.getCount() > 0) {
                    altUOMCursor.moveToFirst();
                    if (cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(altUOMCursor.getString(altUOMCursor.getColumnIndex(db.KEY_UOM)))) {
                        deliveryItem.setIsAltUOM(false);
                    } else {
                        if (cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM_NEW) && altUOMCursor.getString(altUOMCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)) {
                            deliveryItem.setIsAltUOM(false);
                        } else {
                            deliveryItem.setIsAltUOM(true);
                        }
                    }
                } else {
                    if (cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM_NEW) && altUOMCursor.getString(altUOMCursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)) {
                        deliveryItem.setIsAltUOM(false);
                    } else {
                        deliveryItem.setIsAltUOM(true);
                    }
                }
                HashMap<String, String> priceMap = new HashMap<>();
                priceMap.put(db.KEY_AMOUNT, "");
                HashMap<String, String> filterPrice = new HashMap<>();
                filterPrice.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                filterPrice.put(db.KEY_PRIORITY, "2");
                Cursor priceCursor = db.getData(db.PRICING, priceMap, filterPrice);
                if (priceCursor.getCount() > 0) {
                    priceCursor.moveToFirst();
                    deliveryItem.setAmount(priceCursor.getString(priceCursor.getColumnIndex(db.KEY_AMOUNT)));
                } else {
                    deliveryItem.setAmount("0");
                }
                /*if (cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)) {
                    deliveryItem.setItemUnits(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)));
                } else {
                    deliveryItem.setItemUnits("0");
                }*/
                deliveryItem.setItemUnits("0");
                deliveryItem.setItemUom(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                arrayList.add(deliveryItem);
            }
            while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void calculatePrice() {
        try {
            double totalamt = 0;
            for (DeliveryItem item : arrayList) {
                double itemPrice = 0;
                if (!item.isAltUOM()) {
                    itemPrice = Double.parseDouble(item.getItemCase()) * Double.parseDouble(item.getAmount());
                }
                totalamt += itemPrice;
            }
            tv_amt.setText(String.valueOf(totalamt));
            Helpers.logData(DeliveryOrderActivity.this, "Total Delivery cost" + totalamt);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    private void saveData() {
        try {
            double totalamt = 0;
            String purchaseNum = Helpers.generateNumber(db, ConfigStore.CustomerDeliveryRequest_PR_Type);
            for (int i = 0; i < arrayList.size(); i++) {
                DeliveryItem item = arrayList.get(i);
                String itemCase = item.getItemCase().equals("") || item.getItemCase().isEmpty() || item.getItemCase() == null ? "0" : item.getItemCase();
                String itemUnit = item.getItemUnits().equals("") || item.getItemUnits().isEmpty() || item.getItemUnits() == null ? "0" : item.getItemUnits();
                totalamt = totalamt + (Double.parseDouble(itemCase) * 54 + Double.parseDouble(itemUnit) * 2.25);
            }
            for (DeliveryItem item : arrayList) {
                try {
                    if ((item.getItemCase().isEmpty() || item.getItemCase().equals("") || item.getItemCase() == null)) {
                        item.setItemCase("0");
                    }
                    if ((item.getItemUnits().isEmpty() || item.getItemUnits().equals("") || item.getItemUnits() == null)) {
                        item.setItemUnits("0");
                    }
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    map.put(db.KEY_DELIVERY_NO, delivery.getOrderId());
                    map.put(db.KEY_ITEM_NO, item.getItemCode());
                    map.put(db.KEY_MATERIAL_NO, item.getMaterialNo());
                    map.put(db.KEY_MATERIAL_DESC1, item.getItemDescription());
                    map.put(db.KEY_CASE, item.getItemCase());
                    map.put(db.KEY_UNIT, item.getItemUnits());
                    map.put(db.KEY_UOM, item.getItemUom());
                    map.put(db.KEY_AMOUNT, item.getAmount());
                    map.put(db.KEY_ORDER_ID, purchaseNum);
                    map.put(db.KEY_PURCHASE_NUMBER, purchaseNum);
                    map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                    map.put(db.KEY_IS_PRINTED, "");
                    //Log.e("Map","" + map);
                    if (Float.parseFloat(item.getItemCase()) > 0 || Float.parseFloat(item.getItemUnits()) > 0) {
                        Helpers.logData(DeliveryOrderActivity.this,"Marking items for post for delivery" + map);
                        db.addData(db.CUSTOMER_DELIVERY_ITEMS_POST, map);
                    } else {
                        //   Log.e("LOOO","FOOOO");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        Helpers.logData(DeliveryOrderActivity.this,"Hardware back clicked on delivery order details screen");
        super.onBackPressed();
        finish();
    }
    public class loadDeliveryItems extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_ID, "");
            map.put(db.KEY_DELIVERY_NO, "");
            map.put(db.KEY_ITEM_NO, "");
            map.put(db.KEY_ITEM_CATEGORY, "");
            map.put(db.KEY_CREATED_BY, "");
            map.put(db.KEY_ENTRY_TIME, "");
            map.put(db.KEY_DATE, "");
            map.put(db.KEY_MATERIAL_NO, "");
            map.put(db.KEY_MATERIAL_ENTERED, "");
            map.put(db.KEY_MATERIAL_GROUP, "");
            map.put(db.KEY_PLANT, "");
            map.put(db.KEY_STORAGE_LOCATION, "");
            map.put(db.KEY_BATCH, "");
            map.put(db.KEY_ACTUAL_QTY, "");
            map.put(db.KEY_REMAINING_QTY, "");
            map.put(db.KEY_UOM, "");
            map.put(db.KEY_DIST_CHANNEL, "");
            map.put(db.KEY_DIVISION, "");
            map.put(db.KEY_IS_DELIVERED, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_DELIVERY_NO, delivery.getOrderId());
            filter.put(db.KEY_IS_DELIVERED,App.FALSE);
            Cursor cursor = db.getData(db.CUSTOMER_DELIVERY_ITEMS, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                setData(cursor);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (loadingSpinner.isShowing()) {
                loadingSpinner.hide();
            }
            calculatePrice();
            adapter.notifyDataSetChanged();
            deliveryItemsList.setAdapter(adapter);
        }
    }
}
