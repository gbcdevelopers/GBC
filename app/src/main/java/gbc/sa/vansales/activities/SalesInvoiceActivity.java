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

        Intent i = this.getIntent();
        object = (Customer) i.getParcelableExtra("headerObj");
        customers = CustomerHeaders.get();

        button = (FloatingActionButton) findViewById(R.id.float_map);
        button1 = (FloatingActionButton) findViewById(R.id.addCustomer);
        button.setVisibility(View.GONE);
        button1.setVisibility(View.GONE);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Sales"));
        tabLayout.addTab(tabLayout.newTab().setText("Foc"));
        tabLayout.addTab(tabLayout.newTab().setText("G.R"));
        tabLayout.addTab(tabLayout.newTab().setText("B.R"));
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
                //Log.e("Back Clicked","Back Clicked");
                ArrayList<Sales> salesarrayList = new ArrayList<>();;
                ArrayList<Sales>goodsReturnList = new ArrayList<>();
                ArrayList<Sales>badReturnList = new ArrayList<>();
                try{
                    if(Const.siBundle!=null){
                        salesarrayList = Const.siBundle.getParcelableArrayList("si");
                    }
                    if(Const.grBundle!=null){
                        goodsReturnList = Const.grBundle.getParcelableArrayList("gr");
                    }
                    if(Const.brBundle!=null){
                        badReturnList = Const.brBundle.getParcelableArrayList("br");
                    }
                    if(salesarrayList.size()>0||goodsReturnList.size()>0||badReturnList.size()>0){
                        salesInvoiceDataonBack(salesarrayList,goodsReturnList,badReturnList);
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

    @Override
    protected void onResume() {
        super.onResume();
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
                    FocFragment.salesarrayList.addAll(productArrayList);
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
                    BListFragment.adapter.notifyDataSetChanged();

                }
                break;
            default:
                break;
        }
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
                if (SalesInvoiceActivity.tab_position == 2) {
                    GListFragment.arrProductList.remove(info.position);
                    GListFragment.adapter.notifyDataSetChanged();
                } else if (SalesInvoiceActivity.tab_position == 3) {
                    BListFragment.arrProductList.remove(info.position);
                    BListFragment.adapter.notifyDataSetChanged();
                }
                return true;
            case R.id.cancel:
                // edit stuff here

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void salesInvoiceDataonBack(ArrayList<Sales>salesInvoiceList,ArrayList<Sales>grList,ArrayList<Sales>brList){
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
        filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
        if(db.checkData(db.CAPTURE_SALES_INVOICE,filter)){

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
                    }
                }
            }
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
                }
            }
        }

    }
}
