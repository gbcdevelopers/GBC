package gbc.sa.vansales.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gbc.sa.vansales.App;
import gbc.sa.vansales.Fragment.BListFragment;
import gbc.sa.vansales.Fragment.FocFragment;
import gbc.sa.vansales.Fragment.GListFragment;
import gbc.sa.vansales.Fragment.SalesFragment;
import gbc.sa.vansales.Fragment.ShelfFragment;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.PagerAdapter;
import gbc.sa.vansales.adapters.SalesAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.Sales;
import gbc.sa.vansales.models.ShelfProduct;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.Settings;
/**
 * Created by eheuristic on 12/5/2016.
 */
public class SalesInvoiceActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    ImageView iv_back;
    TextView tv_top_header;
    ImageView toolbar_iv_back;
    ImageView iv_search;
    EditText et_search;
    public static int tab_position;
    FloatingActionButton button;
    FloatingActionButton button1;
    Customer object;
    ArrayList<CustomerHeader> customers;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_trip);

        try{
            Intent i = this.getIntent();
            object = (Customer) i.getParcelableExtra("headerObj");
            customers = CustomerHeaders.get();

            button = (FloatingActionButton) findViewById(R.id.float_map);
            button1 = (FloatingActionButton) findViewById(R.id.addCustomer);
            button.setVisibility(View.GONE);
            button1.setVisibility(View.GONE);
            viewPager = (ViewPager) findViewById(R.id.pager);
            tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.sales)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.promo)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.gr)));
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.br)));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            et_search = (EditText) findViewById(R.id.et_search_customer);
            iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
            tv_top_header = (TextView) findViewById(R.id.tv_top_header);
            iv_back.setVisibility(View.VISIBLE);
            tv_top_header.setVisibility(View.VISIBLE);
            tv_top_header.setText(getString(R.string.sales_invoice));
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Back Clicked","Back Clicked");
                    Helpers.logData(SalesInvoiceActivity.this, "Sales Invoice back clicked");
                    ArrayList<Sales> salesarrayList = new ArrayList<>();;
                    ArrayList<Sales>goodsReturnList = new ArrayList<>();
                    ArrayList<Sales>badReturnList = new ArrayList<>();
                    ArrayList<Sales>focList = new ArrayList<>();
                    try{
                        if(Const.siBundle!=null){
                            salesarrayList = Const.siBundle.getParcelableArrayList("si");
                            Helpers.logData(SalesInvoiceActivity.this, "Sales done are" + salesarrayList.size());
                        }
                        if(Const.grBundle!=null){
                            goodsReturnList = Const.grBundle.getParcelableArrayList("gr");
                            Helpers.logData(SalesInvoiceActivity.this, "GR done are" + goodsReturnList.size());
                        }
                        if(Const.brBundle!=null){
                            badReturnList = Const.brBundle.getParcelableArrayList("br");
                            Helpers.logData(SalesInvoiceActivity.this, "BR done are" + badReturnList.size());
                        }
                        if(Const.focBundle!=null){
                            focList = Const.focBundle.getParcelableArrayList("foc");
                            Helpers.logData(SalesInvoiceActivity.this, "FOC done are" + badReturnList.size());
                        }
                        if(salesarrayList.size()>0||goodsReturnList.size()>0||badReturnList.size()>0){
                            salesInvoiceDataonBack(salesarrayList,goodsReturnList,badReturnList,focList);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    finish();
                }
            });
            tv_top_header = (TextView) findViewById(R.id.tv_top_header);
            if (tv_top_header != null) {
                tv_top_header.setVisibility(View.VISIBLE);
                tv_top_header.setText(getString(R.string.sales_invoice));
            }
            toolbar_iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
            if (toolbar_iv_back != null) {
                toolbar_iv_back.setVisibility(View.VISIBLE);
            }

            if (toolbar_iv_back != null) {
                toolbar_iv_back.setVisibility(View.VISIBLE);
            }
            iv_search = (ImageView) findViewById(R.id.iv_search2);
            if (iv_search != null) {
                iv_search.setVisibility(View.VISIBLE);
            }
            iv_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_search.setVisibility(View.GONE);
                    et_search.setVisibility(View.VISIBLE);
                    et_search.setHint("Search Products..");
                    toolbar_iv_back.setVisibility(View.GONE);
                    tv_top_header.setVisibility(View.GONE);
                }
            });
            Bundle bundle = new Bundle();
            bundle.putParcelable("data", object);
            final PagerAdapter adapter = new PagerAdapter
                    (getSupportFragmentManager(), tabLayout.getTabCount(), "sales", bundle);
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                    tab_position = tab.getPosition();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
            et_search.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (et_search.getRight() - et_search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            et_search.setVisibility(View.GONE);
                            iv_search.setVisibility(View.VISIBLE);
                            toolbar_iv_back.setVisibility(View.VISIBLE);
                            tv_top_header.setVisibility(View.VISIBLE);
                            et_search.setText("");
                            if (tab_position == 0) {
                                SalesFragment.adapter.getFilter().filter("");
                            } else if (tab_position == 1) {
                                FocFragment.adapter.getFilter().filter("");
                            } else if (tab_position == 2) {
                                //`   GListFragment.adapter.getFilter().filter("");
                            } else if (tab_position == 3) {
                                BListFragment.adapter.getFilter().filter("");
                            }
                            return true;
                        }
                    }
                    return false;
                }
            });
            et_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.v("addtext", "change");
                    if (tab_position == 0) {
                        //          SalesFragment.adapter.getFilter().filter(s.toString());
                    } else if (tab_position == 1) {
                        FocFragment.adapter.getFilter().filter(s.toString());
                    } else if (tab_position == 2) {
                        //       GListFragment.adapter.getFilter().filter(s.toString());
                    } else if (tab_position == 3) {
                        BListFragment.adapter.getFilter().filter(s.toString());
                    }
                    //planBadgeAdapter.notifyDataSetChanged();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });


       /* iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_search.setVisibility(View.GONE);
                et_search.setVisibility(View.VISIBLE);
                toolbar_iv_back.setVisibility(View.GONE);
                tv_top_header.setVisibility(View.GONE);

            }
        });*/
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            switch (tab_position) {
                case 0:
                    button1.setVisibility(View.INVISIBLE);
                    button.setVisibility(View.INVISIBLE);
//                if (SalesFragment.salesarrayList != null && SalesFragment.adapter != null) {
//                    ArrayList<Sales> productArrayList = new ArrayList<>();
//                    for (int i = 0; i < Const.addlist.size(); i++) {
//                        Sales product = new Sales();
//                        product.setName(Const.addlist.get(i));
//                        product.setCases("0");
//                        product.setPic("0");
//                        productArrayList.add(product);
//                    }
//                    SalesFragment.salesarrayList.addAll(productArrayList);
//                    SalesFragment.adapter.notifyDataSetChanged();
//                }
                    break;
                case 1:
                    if (FocFragment.salesarrayList != null && FocFragment.adapter != null) {
                        ArrayList<Sales> productArrayList = new ArrayList<>();
                        for (int i = 0; i < Const.addlist.size(); i++) {
                            Sales sales = new Sales();
                            sales.setName(Const.addlist.get(i));
                            sales.setCases("0");
                            sales.setPic("0");
                            productArrayList.add(sales);
                        }
                        Log.e("On Resume","FOC");
                       // FocFragment.salesarrayList.addAll(productArrayList);
                        FocFragment.adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    if (GListFragment.arrProductList != null && GListFragment.adapter != null) {
                        ArrayList<Sales> productArrayList = new ArrayList<>();
                        for (int i = 0; i < Const.addlist.size(); i++) {
                            Sales sales = new Sales();
                            sales.setName(Const.addlist.get(i));
                            sales.setCases("0");
                            sales.setPic("0");
                            productArrayList.add(sales);
                        }
                        Log.e("On Resume","Good Return");
                        //GListFragment.arrProductList.addAll(productArrayList);
                        GListFragment.adapter.notifyDataSetChanged();

                    }
                    break;
                case 3:
                    if (BListFragment.arrProductList != null && BListFragment.adapter != null) {
                        ArrayList<Sales> productArrayList = new ArrayList<>();
                        for (int i = 0; i < Const.addlist.size(); i++) {
                            Sales sales = new Sales();
                            sales.setName(Const.addlist.get(i));
                            sales.setCases("0");
                            sales.setPic("0");
                            productArrayList.add(sales);
                        }
                        //BListFragment.arrProductList.addAll(productArrayList);
                        Log.e("On Resume","Bad Return");
                        BListFragment.adapter.notifyDataSetChanged();

                    }
                    break;
                default:
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.list_sales) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.remove:
                // add stuff here
                try{
                    if (SalesInvoiceActivity.tab_position == 2) {
                        Sales sales = GListFragment.arrProductList.get(info.position);
                        HashMap<String, String> grFilter = new HashMap<>();
                        grFilter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                        grFilter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                        grFilter.put(db.KEY_REASON_TYPE, App.GOOD_RETURN);
                        grFilter.put(db.KEY_MATERIAL_NO,sales.getMaterial_no());
                        db.deleteData(db.RETURNS,grFilter);
                        GListFragment.arrProductList.remove(info.position);
                        GListFragment.adapter.notifyDataSetChanged();
                    } else if (SalesInvoiceActivity.tab_position == 3) {
                        Sales sales = BListFragment.arrProductList.get(info.position);
                        HashMap<String, String> brFilter = new HashMap<>();
                        brFilter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                        brFilter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                        brFilter.put(db.KEY_REASON_TYPE, App.BAD_RETURN);
                        brFilter.put(db.KEY_MATERIAL_NO,sales.getMaterial_no());
                        db.deleteData(db.RETURNS,brFilter);

                        BListFragment.arrProductList.remove(info.position);
                        BListFragment.adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                return true;
            case R.id.cancel:
                // edit stuff here

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void salesInvoiceDataonBack(ArrayList<Sales>salesInvoiceList,ArrayList<Sales>grList,ArrayList<Sales>brList,ArrayList<Sales>focList){
        try{
            Log.e("Here on SI Back","" + grList.size());
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
            filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
            if(db.checkData(db.CAPTURE_SALES_INVOICE,filter)){
                String orderID = "";
                HashMap<String,String>searchMap = new HashMap<>();
                searchMap.put(db.KEY_ORDER_ID,"");
                Cursor cursor = db.getData(db.CAPTURE_SALES_INVOICE,searchMap,filter);
                if(cursor.getCount()>0){
                    cursor.moveToFirst();
                    orderID = cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID));
                    Log.e("ORDER ID - SI BACK","" + orderID);
                    //new loadItems(cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID)));
                }

                for (Sales sale : salesInvoiceList) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                    map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    map.put(db.KEY_ITEM_NO, sale.getItem_code());
                    map.put(db.KEY_ITEM_CATEGORY, sale.getItem_category());
                    map.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                    map.put(db.KEY_MATERIAL_GROUP, "");
                    map.put(db.KEY_MATERIAL_DESC1,sale.getName());
                    map.put(db.KEY_ORG_CASE, sale.getCases());
                    map.put(db.KEY_UOM,sale.getUom());
                    map.put(db.KEY_ORG_UNITS, sale.getPic());
                    map.put(db.KEY_AMOUNT, sale.getPrice());
                    map.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    map.put(db.KEY_IS_PRINTED,App.DATA_NOT_POSTED);
                    map.put(db.KEY_ORDER_ID,orderID);
                    map.put(db.KEY_PURCHASE_NUMBER,orderID);
                    HashMap<String, String> updateFilter = new HashMap<>();
                    updateFilter.put(db.KEY_PURCHASE_NUMBER,orderID);
                    updateFilter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    updateFilter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                    updateFilter.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                    if(db.checkData(db.CAPTURE_SALES_INVOICE,updateFilter)){
                        db.updateData(db.CAPTURE_SALES_INVOICE, map,updateFilter);
                    }
                    else{
                        if(Float.parseFloat(sale.getCases())>0||Float.parseFloat(sale.getPic())>0){
                            db.addData(db.CAPTURE_SALES_INVOICE,map);
                        }
                    }
                    Helpers.logData(SalesInvoiceActivity.this,"Sales done for ref no" + orderID + ":" + sale.getMaterial_no() + "-" + sale.getMaterial_description()
                            + "-" + sale.getCases() + "-" + sale.getPic() + "-" + sale.getUom() + "-" + sale.getPrice());

                    if(focList.size()>0){
                        for(Sales sales:focList){
                            HashMap<String,String>mapFOC = new HashMap<String, String>();
                            mapFOC.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
                            mapFOC.put(db.KEY_TRIP_ID,"");
                            mapFOC.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                            mapFOC.put(db.KEY_ORDER_ID,orderID);
                            mapFOC.put(db.KEY_PURCHASE_NUMBER,orderID);
                            mapFOC.put(db.KEY_ITEM_NO, sales.getItem_code());
                            mapFOC.put(db.KEY_ITEM_CATEGORY, sales.getItem_category());
                            mapFOC.put(db.KEY_MATERIAL_NO, sales.getMaterial_no());
                            mapFOC.put(db.KEY_MATERIAL_GROUP, "");
                            mapFOC.put(db.KEY_MATERIAL_DESC1,sales.getName());
                            mapFOC.put(db.KEY_ORG_CASE, sales.getCases());
                            mapFOC.put(db.KEY_UOM,sales.getUom());
                            mapFOC.put(db.KEY_ORG_UNITS, sales.getPic());
                            mapFOC.put(db.KEY_AMOUNT, sales.getPrice());
                            mapFOC.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                            mapFOC.put(db.KEY_IS_PRINTED,App.DATA_NOT_POSTED);
                            if(db.checkData(db.FOC_INVOICE,updateFilter)){
                                db.updateData(db.FOC_INVOICE, mapFOC,updateFilter);
                            }
                            else{
                                if(Float.parseFloat(sale.getCases())>0||Float.parseFloat(sale.getPic())>0){
                                    db.addData(db.FOC_INVOICE,mapFOC);
                                }
                            }
                        }
                    }
                }

                if(grList.size()>0){
                    Log.e("GR List","" + grList.size());
                    HashMap<String, String> grFilter = new HashMap<>();
                    grFilter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    grFilter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                    grFilter.put(db.KEY_REASON_TYPE, App.GOOD_RETURN);

                    //There were good returns in the table
                    if(db.checkData(db.RETURNS,grFilter)){
                        for(Sales sale:grList){
                            HashMap<String, String> map = new HashMap<>();
                            map.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                            map.put(db.KEY_REASON_TYPE,App.GOOD_RETURN);
                            map.put(db.KEY_REASON_CODE,sale.getReasonCode());
                            map.put(db.KEY_ITEM_NO,sale.getItem_code());
                            map.put(db.KEY_MATERIAL_DESC1,sale.getName());
                            map.put(db.KEY_MATERIAL_NO,sale.getMaterial_no());
                            map.put(db.KEY_MATERIAL_GROUP,"");
                            map.put(db.KEY_CASE,sale.getCases());
                            map.put(db.KEY_UNIT,sale.getPic());
                            map.put(db.KEY_UOM,sale.getUom());
                            map.put(db.KEY_PRICE,sale.getPrice());
                            map.put(db.KEY_ORDER_ID,orderID);
                            map.put(db.KEY_PURCHASE_NUMBER,orderID);
                            map.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                            map.put(db.KEY_IS_PRINTED, App.DATA_NOT_POSTED);
                            HashMap<String, String> updateFilter = new HashMap<>();
                            //updateFilter.put(db.KEY_PURCHASE_NUMBER,orderID);
                            updateFilter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                            updateFilter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                            updateFilter.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                            updateFilter.put(db.KEY_REASON_TYPE, App.GOOD_RETURN);
                            if(db.checkData(db.RETURNS,updateFilter)){
                                db.updateData(db.RETURNS, map, updateFilter);
                            }
                            else{
                                if(Float.parseFloat(sale.getCases())>0||Float.parseFloat(sale.getPic())>0){
                                    db.addData(db.RETURNS,map);
                                }

                            }
                            Helpers.logData(SalesInvoiceActivity.this,"GR done for referenceno" + orderID + sale.getMaterial_no() + "-" + sale.getMaterial_description()
                                    + "-" + sale.getCases() + "-" + sale.getPic() + "-" + sale.getUom() + "-" + sale.getPrice());

                        }
                    }
                    //No good return yet
                    else{
                        String grPRNo= "";
                        if(!orderID.equals("")){
                            grPRNo = orderID;
                        }
                        for(Sales sale:grList){
                            HashMap<String, String> map = new HashMap<>();
                            map.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                            map.put(db.KEY_REASON_TYPE,App.GOOD_RETURN);
                            map.put(db.KEY_REASON_CODE,sale.getReasonCode());
                            map.put(db.KEY_ITEM_NO,sale.getItem_code());
                            map.put(db.KEY_MATERIAL_DESC1,sale.getName());
                            map.put(db.KEY_MATERIAL_NO,sale.getMaterial_no());
                            map.put(db.KEY_MATERIAL_GROUP,"");
                            map.put(db.KEY_CASE,sale.getCases());
                            map.put(db.KEY_UNIT,sale.getPic());
                            map.put(db.KEY_UOM,sale.getUom());
                            map.put(db.KEY_PRICE,sale.getPrice());
                            map.put(db.KEY_ORDER_ID,grPRNo);
                            map.put(db.KEY_PURCHASE_NUMBER,grPRNo);
                            map.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                            map.put(db.KEY_IS_PRINTED, App.DATA_NOT_POSTED);
                            if(Float.parseFloat(sale.getCases())>0||Float.parseFloat(sale.getPic())>0){
                                db.addData(db.RETURNS, map);
                            }

                            Helpers.logData(SalesInvoiceActivity.this,"GR done for Reference" + orderID +":" + sale.getMaterial_no() + "-" + sale.getMaterial_description()
                                    + "-" + sale.getCases() + "-" + sale.getPic() + "-" + sale.getUom() + "-" + sale.getPrice());
                        }
                    }
                }
                if(brList.size()>0){
                    HashMap<String, String> brFilter = new HashMap<>();
                    brFilter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                    brFilter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                    brFilter.put(db.KEY_REASON_TYPE, App.BAD_RETURN);

                    //There were good returns in the table
                    if(db.checkData(db.RETURNS,brFilter)){
                        for(Sales sale:brList){
                            HashMap<String, String> map = new HashMap<>();
                            map.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                            map.put(db.KEY_REASON_TYPE,App.BAD_RETURN);
                            map.put(db.KEY_REASON_CODE,sale.getReasonCode());
                            map.put(db.KEY_ITEM_NO,sale.getItem_code());
                            map.put(db.KEY_MATERIAL_DESC1,sale.getName());
                            map.put(db.KEY_MATERIAL_NO,sale.getMaterial_no());
                            map.put(db.KEY_MATERIAL_GROUP,"");
                            map.put(db.KEY_CASE,sale.getCases());
                            map.put(db.KEY_UNIT,sale.getPic());
                            map.put(db.KEY_UOM,sale.getUom());
                            map.put(db.KEY_PRICE,sale.getPrice());
                            map.put(db.KEY_ORDER_ID,orderID);
                            map.put(db.KEY_PURCHASE_NUMBER,orderID);
                            map.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                            map.put(db.KEY_IS_PRINTED, App.DATA_NOT_POSTED);
                            HashMap<String, String> updateFilter = new HashMap<>();
                            //updateFilter.put(db.KEY_PURCHASE_NUMBER,orderID);
                            updateFilter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                            updateFilter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                            updateFilter.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                            updateFilter.put(db.KEY_REASON_TYPE, App.BAD_RETURN);
                            if(db.checkData(db.RETURNS,updateFilter)){
                                db.updateData(db.RETURNS, map, updateFilter);
                            }
                            else{
                                if(Float.parseFloat(sale.getCases())>0||Float.parseFloat(sale.getPic())>0){
                                    db.addData(db.RETURNS,map);
                                }
                            }
                            Helpers.logData(SalesInvoiceActivity.this,"BR done for Reference" + orderID +":" + sale.getMaterial_no() + "-" + sale.getMaterial_description()
                                    + "-" + sale.getCases() + "-" + sale.getPic() + "-" + sale.getUom() + "-" + sale.getPrice());
                        }
                    }
                    //No bad return yet
                    else{
                        String brPRNo= "";
                        if(!orderID.equals("")){
                            brPRNo = orderID;
                        }
                        for(Sales sale:brList){
                            HashMap<String, String> map = new HashMap<>();
                            map.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                            map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                            map.put(db.KEY_REASON_TYPE,App.BAD_RETURN);
                            map.put(db.KEY_REASON_CODE,sale.getReasonCode());
                            map.put(db.KEY_ITEM_NO,sale.getItem_code());
                            map.put(db.KEY_MATERIAL_DESC1,sale.getName());
                            map.put(db.KEY_MATERIAL_NO,sale.getMaterial_no());
                            map.put(db.KEY_MATERIAL_GROUP,"");
                            map.put(db.KEY_CASE,sale.getCases());
                            map.put(db.KEY_UNIT,sale.getPic());
                            map.put(db.KEY_UOM,sale.getUom());
                            map.put(db.KEY_PRICE,sale.getPrice());
                            map.put(db.KEY_ORDER_ID,brPRNo);
                            map.put(db.KEY_PURCHASE_NUMBER,brPRNo);
                            map.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                            map.put(db.KEY_IS_PRINTED, App.DATA_NOT_POSTED);
                            if(Float.parseFloat(sale.getCases())>0||Float.parseFloat(sale.getPic())>0){
                                db.addData(db.RETURNS, map);
                            }
                            Helpers.logData(SalesInvoiceActivity.this,"BR done for Reference" + orderID +":" + sale.getMaterial_no() + "-" + sale.getMaterial_description()
                                    + "-" + sale.getCases() + "-" + sale.getPic() + "-" + sale.getUom() + "-" + sale.getPrice());
                        }
                    }
                }
            }
            else{
                boolean value = false;
                //To check if invoice doesnt contain any data
                if(salesInvoiceList.size()>0){
                    for (Sales sale : salesInvoiceList){
                        if(Float.parseFloat(sale.getCases())>0||Float.parseFloat(sale.getPic())>0){
                            value = true;
                            break;
                        }
                    }
                }
                String purchaseNumber = "";
                if(value){
                    purchaseNumber = Helpers.generateNumber(db, ConfigStore.InvoiceRequest_PR_Type);
                }

                if(salesInvoiceList.size()>0){
                    if(value){
                        for (Sales sale : salesInvoiceList) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                            map.put(db.KEY_ITEM_NO, sale.getItem_code());
                            map.put(db.KEY_ITEM_CATEGORY, sale.getItem_category());
                            map.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                            map.put(db.KEY_MATERIAL_GROUP, "");
                            map.put(db.KEY_MATERIAL_DESC1,sale.getName());
                            map.put(db.KEY_ORG_CASE, sale.getCases());
                            map.put(db.KEY_UOM,sale.getUom());
                            map.put(db.KEY_ORG_UNITS, sale.getPic());
                            map.put(db.KEY_AMOUNT, sale.getPrice());
                            map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                            map.put(db.KEY_IS_PRINTED,App.DATA_NOT_POSTED);
                            map.put(db.KEY_ORDER_ID, purchaseNumber);
                            map.put(db.KEY_PURCHASE_NUMBER, purchaseNumber);
                            if(Float.parseFloat(sale.getCases())>0||Float.parseFloat(sale.getPic())>0){
                                db.addData(db.CAPTURE_SALES_INVOICE, map);
                            }
                            Helpers.logData(SalesInvoiceActivity.this,"Sale done for Reference" + purchaseNumber +":" + sale.getMaterial_no() + "-" + sale.getMaterial_description()
                                    + "-" + sale.getCases() + "-" + sale.getPic() + "-" + sale.getUom() + "-" + sale.getPrice());
                        }

                        if(focList.size()>0){
                            for(Sales sale:focList){
                                HashMap<String,String>map = new HashMap<String, String>();
                                map.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
                                map.put(db.KEY_TRIP_ID,"");
                                map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                                map.put(db.KEY_ORDER_ID,purchaseNumber);
                                map.put(db.KEY_PURCHASE_NUMBER,purchaseNumber);
                                map.put(db.KEY_ITEM_NO, sale.getItem_code());
                                map.put(db.KEY_ITEM_CATEGORY, sale.getItem_category());
                                map.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                                map.put(db.KEY_MATERIAL_GROUP, "");
                                map.put(db.KEY_MATERIAL_DESC1,sale.getName());
                                map.put(db.KEY_ORG_CASE, sale.getCases());
                                map.put(db.KEY_UOM,sale.getUom());
                                map.put(db.KEY_ORG_UNITS, sale.getPic());
                                map.put(db.KEY_AMOUNT, sale.getPrice());
                                map.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                                map.put(db.KEY_IS_PRINTED,App.DATA_NOT_POSTED);
                                if(Float.parseFloat(sale.getCases())>0||Float.parseFloat(sale.getPic())>0){
                                    db.addData(db.FOC_INVOICE, map);
                                }
                            }
                        }

                        //Check if any GR was done before invoice
                        HashMap<String, String> grFilter = new HashMap<>();
                        grFilter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                        grFilter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                        grFilter.put(db.KEY_REASON_TYPE, App.GOOD_RETURN);
                        grFilter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                        //There were good returns in the table
                        if(db.checkData(db.RETURNS,grFilter))
                        {
                            HashMap<String,String>grMap = new HashMap<>();
                            grMap.put(db.KEY_ORDER_ID,purchaseNumber);
                            grMap.put(db.KEY_PURCHASE_NUMBER,purchaseNumber);
                            db.updateData(db.RETURNS,grMap,grFilter);
                        }

                        //Check if any GR was done before invoice
                        HashMap<String, String> brFilter = new HashMap<>();
                        brFilter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                        brFilter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                        brFilter.put(db.KEY_REASON_TYPE, App.BAD_RETURN);
                        brFilter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                        //There were good returns in the table
                        if(db.checkData(db.RETURNS,brFilter))
                        {
                            HashMap<String,String>grMap = new HashMap<>();
                            grMap.put(db.KEY_ORDER_ID,purchaseNumber);
                            grMap.put(db.KEY_PURCHASE_NUMBER,purchaseNumber);
                            db.updateData(db.RETURNS,grMap,grFilter);
                        }
                    }
                }
            /*if(focList.size()>0){
                if(value){
                    for (Sales sale:focList){
                        HashMap<String,String>map = new HashMap<>();
                        map.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
                        map.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
                        map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                        map.put(db.KEY_ORDER_ID,purchaseNumber);
                        map.put(db.KEY_PURCHASE_NUMBER,purchaseNumber);
                        map.put(db.KEY_ITEM_NO,sale.getItem_code());
                        map.put(db.KEY_ITEM_CATEGORY,sale.getItem_category());
                        map.put(db.KEY_MATERIAL_NO,sale.getMaterial_no());
                        map.put(db.KEY_MATERIAL_DESC1,sale.getMaterial_description());
                        map.put(db.KEY_MATERIAL_GROUP,"");
                        map.put(db.KEY_ORG_CASE,sale.getCases());
                        map.put(db.KEY_ORG_UNITS,sale.getPic());
                        map.put(db.KEY_AMOUNT,sale.getPrice());
                        map.put(db.KEY_UOM,sale.getUom());
                        map.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                        map.put(db.KEY_IS_PRINTED,App.DATA_NOT_POSTED);
                        db.addData(db.FOC_INVOICE,map);
                    }
                }
            }*/
                if(grList.size()>0){
                    String grPRNo= "";
                    if(value){
                        grPRNo = purchaseNumber;
                    }
                    else{
                        grPRNo = Helpers.generateNumber(db, ConfigStore.GoodReturns_PR_Type);
                    }
                    for(Sales sale:grList){
                        HashMap<String, String> map = new HashMap<>();
                        map.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
                        map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                        map.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                        map.put(db.KEY_REASON_TYPE,App.GOOD_RETURN);
                        map.put(db.KEY_REASON_CODE,sale.getReasonCode());
                        map.put(db.KEY_ITEM_NO,sale.getItem_code());
                        map.put(db.KEY_MATERIAL_DESC1,sale.getName());
                        map.put(db.KEY_MATERIAL_NO,sale.getMaterial_no());
                        map.put(db.KEY_MATERIAL_GROUP,"");
                        map.put(db.KEY_CASE,sale.getCases());
                        map.put(db.KEY_UNIT,sale.getPic());
                        map.put(db.KEY_UOM,sale.getUom());
                        map.put(db.KEY_PRICE,sale.getPrice());
                        map.put(db.KEY_ORDER_ID,grPRNo);
                        map.put(db.KEY_PURCHASE_NUMBER,grPRNo);
                        map.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                        map.put(db.KEY_IS_PRINTED, App.DATA_NOT_POSTED);
                        if(Float.parseFloat(sale.getCases())>0||Float.parseFloat(sale.getPic())>0){
                            db.addData(db.RETURNS, map);
                        }
                        Helpers.logData(SalesInvoiceActivity.this,"GR done for Reference" + grPRNo +":" + sale.getMaterial_no() + "-" + sale.getMaterial_description()
                                + "-" + sale.getCases() + "-" + sale.getPic() + "-" + sale.getUom() + "-" + sale.getPrice());

                    }
                }
                if(brList.size()>0){
                    String brPRNo= "";
                    if(value){
                        brPRNo = purchaseNumber;
                    }
                    else{
                        brPRNo = Helpers.generateNumber(db, ConfigStore.GoodReturns_PR_Type);
                    }
                    for (Sales sale : brList) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                        map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                        map.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                        map.put(db.KEY_REASON_TYPE, App.BAD_RETURN);
                        map.put(db.KEY_REASON_CODE, sale.getReasonCode());
                        map.put(db.KEY_ITEM_NO, sale.getItem_code());
                        map.put(db.KEY_MATERIAL_DESC1, sale.getName());
                        map.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                        map.put(db.KEY_MATERIAL_GROUP, "");
                        map.put(db.KEY_CASE, sale.getCases());
                        map.put(db.KEY_UNIT, sale.getPic());
                        map.put(db.KEY_UOM, sale.getUom());
                        map.put(db.KEY_PRICE, sale.getPrice());
                        map.put(db.KEY_ORDER_ID, brPRNo);
                        map.put(db.KEY_PURCHASE_NUMBER, brPRNo);
                        map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                        map.put(db.KEY_IS_PRINTED, App.DATA_NOT_POSTED);
                        if(Float.parseFloat(sale.getCases())>0||Float.parseFloat(sale.getPic())>0){
                            db.addData(db.RETURNS, map);
                        }
                        Helpers.logData(SalesInvoiceActivity.this,"BR done for Reference" + brPRNo +":" + sale.getMaterial_no() + "-" + sale.getMaterial_description()
                                + "-" + sale.getCases() + "-" + sale.getPic() + "-" + sale.getUom() + "-" + sale.getPrice());

                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }


    }
}
