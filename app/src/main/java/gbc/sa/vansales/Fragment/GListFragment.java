package gbc.sa.vansales.Fragment;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.CategoryListActivity;
import gbc.sa.vansales.activities.SalesInvoiceActivity;
import gbc.sa.vansales.adapters.ExpandReturnAdapter;
import gbc.sa.vansales.adapters.ReasonAdapter;
import gbc.sa.vansales.adapters.SalesAdapter;
import gbc.sa.vansales.adapters.SalesInvoiceAdapter;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.OrderReasons;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.Reasons;
import gbc.sa.vansales.models.Sales;
import gbc.sa.vansales.utils.AnimatedExpandableListView;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.OnSwipeTouchListener;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by eheuristic on 12/5/2016.
 */
public class GListFragment extends Fragment {

    View viewmain;
    ListView listSales;
    public static SalesInvoiceAdapter adapter;
    public static ArrayList<Sales> arrProductList;
    FloatingActionButton fab;
    FloatingActionButton addProducts;
    LinearLayout ll_top;
    static ArrayList<ArticleHeader> articles = new ArrayList<>();
    private static DatabaseHandler db;
    private static Activity activity;
    private ArrayList<Reasons> reasonsList = new ArrayList<>();
    ArrayAdapter<Reasons> myAdapter;
    String orderID="";
    Customer object;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try{
            viewmain = inflater.inflate(R.layout.fragment_salesinvoice, container, false);
            object = getArguments().getParcelable("data");
            Log.e("Good Return Frag", "" + object.getCustomerID());
            listSales = (ListView) viewmain.findViewById(R.id.list_sales);
            ll_top = (LinearLayout) viewmain.findViewById(R.id.ll_top);
            db = new DatabaseHandler(getActivity());
            activity = getActivity();
            // OrderReasons.loadData(getActivity());
            // reasonsList = OrderReasons.get();
            // myAdapter = new ArrayAdapter<Reasons>(getActivity(), android.R.layout.simple_spinner_item, reasonsList);
            myAdapter = new ReasonAdapter(getActivity(), android.R.layout.simple_spinner_item, reasonsList);

            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
            filter.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            filter.put(db.KEY_REASON_TYPE, App.GOOD_RETURN);
            if(db.checkData(db.RETURNS,filter)){
                Log.e("I am inside","" + "Hello");
                HashMap<String,String>map = new HashMap<>();
                map.put(db.KEY_ORDER_ID,"");
                Cursor cursor = db.getData(db.RETURNS,map,filter);
                if(cursor.getCount()>0){
                    cursor.moveToFirst();
                    orderID = cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID));
                    Log.e("ORDER ID","" + orderID);
                    new loadGoodReturns(orderID);
                }
            }
            else{
                if(savedInstanceState!=null){
                    Log.e("i am here","here");
                    arrProductList = savedInstanceState.getParcelableArrayList("gr");
                    setGoodReturns(arrProductList);
                }
            }

            ll_top.setVisibility(View.GONE);
            articles = ArticleHeaders.get();
            // fab = (FloatingActionButton) viewmain.findViewById(R.id.fab);
            addProducts = (FloatingActionButton) viewmain.findViewById(R.id.fab);
            fab = (FloatingActionButton) viewmain.findViewById(R.id.add);
        /*fab.setImageDrawable(getResources().getDrawable(R.drawable.btn_select_all));
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.btn_select_all));
        addProducts.setImageDrawable(getResources().getDrawable(R.drawable.ic_white_add));
        addProducts.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_white_add));
        addProducts.setVisibility(View.VISIBLE);*/

            fab.setImageDrawable(getResources().getDrawable(R.drawable.btn_select_all));
            fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.btn_select_all));
            addProducts.setImageDrawable(getResources().getDrawable(R.drawable.ic_white_add));
            addProducts.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_white_add));
            addProducts.setVisibility(View.VISIBLE);
            if(App.CustomerRouteControl.isReturns()){
                addProducts.setEnabled(true);
            }
            else{
                addProducts.setEnabled(false);
                addProducts.setAlpha(.5f);
            }
            fab.setVisibility(View.GONE);

            String strProductname[] = {"Carton 80*150ml Fayha", "CARTON 48*600ml Fayha", "Shrink berain"};
            arrProductList = new ArrayList<>();
        /*for (int i = 0; i < strProductname.length; i++) {
            Sales product = new Sales();
            product.setName(strProductname[i]);
            product.setCases("0");
            product.setPic("0");
            arrProductList.add(product);
        }*/
            adapter = new SalesInvoiceAdapter(getActivity(), arrProductList);
            listSales.setAdapter(adapter);
            //setProductList();
            new loadReasons().execute();
            registerForContextMenu(listSales);
            listSales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                    final Sales sales = arrProductList.get(position);
                    Log.e("On List Click", "" + sales.getReasonCode() + myAdapter.getCount());
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_with_crossbutton);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    TextView tv = (TextView) dialog.findViewById(R.id.dv_title);
                    tv.setText(sales.getName());
                    ImageView iv_cancle = (ImageView) dialog.findViewById(R.id.imageView_close);
                    RelativeLayout rl_specify = (RelativeLayout) dialog.findViewById(R.id.rl_specify_reason);
                    rl_specify.setVisibility(View.VISIBLE);
                    final Spinner spin = (Spinner) dialog.findViewById(R.id.spin);
                    spin.setAdapter(myAdapter);
                    if(sales.getReasonCode()!=null){
                        spin.setSelection(getIndex(sales.getReasonCode()));
                    }


                    spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Reasons reason = myAdapter.getItem(position);
                            sales.setReasonCode(reason.getReasonID());
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
                    final EditText ed_cases = (EditText) dialog.findViewById(R.id.ed_cases);
                    final EditText ed_pcs = (EditText) dialog.findViewById(R.id.ed_pcs);
                    final EditText ed_cases_inv = (EditText) dialog.findViewById(R.id.ed_cases_inv);
                    final EditText ed_pcs_inv = (EditText) dialog.findViewById(R.id.ed_pcs_inv);
                    ed_cases_inv.setText("10");
                    ed_pcs_inv.setText("3");
                    ed_cases_inv.setEnabled(false);
                    ed_pcs_inv.setEnabled(false);
                    if (sales.isAltUOM()) {
                        ed_pcs.setEnabled(true);
                    } else {
                        ed_pcs.setEnabled(false);
                    }
                    ed_cases.setText(sales.getCases().trim().equals("0")?"":sales.getCases());
                    ed_pcs.setText(sales.getPic().trim().equals("0")?"":sales.getPic());

                    //ed_cases.setText(sales.getCases());
                    //ed_pcs.setText(sales.getPic());

                    LinearLayout ll_1 = (LinearLayout) dialog.findViewById(R.id.ll_1);
                    ll_1.setVisibility(View.GONE);
                    iv_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                    arrProductList.remove(position);
                    arrProductList.add(position,sales);
                    btn_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (spin.getSelectedItem().toString().equals("")) {
                                ((TextView) spin.getSelectedView()).setError("select reason");
                            } else {
                                String strCase = ed_cases.getText().toString();
                                String strpcs = ed_pcs.getText().toString();
                                String strcaseinv = ed_cases_inv.getText().toString();
                                String strpcsinv = ed_pcs_inv.getText().toString();
                                Log.e("Reason was","" + sales.getReasonCode());
                                if (strCase.equals("")) {
                                    strCase = "0";
                                }
                                if (strpcs.equals("")) {
                                    strpcs = "0";
                                }
                                TextView tv_cases = (TextView) view.findViewById(R.id.tv_cases_value);
                                TextView tv_pcs = (TextView) view.findViewById(R.id.tv_pcs_value);
                                //tv_cases.setText(strCase);
                                //tv_pcs.setText(strpcs);

                                tv_cases.setText(strCase.trim().equals("")?"0":strCase.trim());
                                tv_pcs.setText(strpcs.trim().equals("")?"0":strpcs.trim());

                                Sales sales = arrProductList.get(position);
                                ArticleHeader articleHeader = ArticleHeader.getArticle(articles,sales.getName());
                                sales.setPic(strpcs);
                                sales.setCases(strCase);
                                double total = 0;
                                int salesTotal = 0;
                                int pcsTotal = 0;
                                for(Sales sale:arrProductList){
                                    double itemPrice = 0;
                                    if(sale.getUom().equals(App.CASE_UOM)||sale.getUom().equals(App.CASE_UOM_NEW)||sale.getUom().equals(App.BOTTLES_UOM)){
                                        itemPrice = Double.parseDouble(sale.getCases())*Double.parseDouble(sale.getPrice());
                                    }
                                /*else if(sale.getUom().equals(App.BOTTLES_UOM)){
                                    itemPrice = Double.parseDouble(sale.getPic())*Double.parseDouble(sale.getPrice());
                                }*/
                                    total+=itemPrice;
                                    salesTotal = salesTotal + Integer.parseInt(sale.getCases());
                                    pcsTotal = pcsTotal + Integer.parseInt(sale.getPic());
                                }
                            /*for (int i = 0; i < arrProductList.size(); i++) {
                                Sales sales1 = arrProductList.get(i);
                                total = total + (Double.parseDouble(sales1.getCases()) * 54 + Double.parseDouble(sales1.getPic()) * 2.25);
                                salesTotal = salesTotal + Integer.parseInt(sales1.getCases());
                                pcsTotal = pcsTotal + Integer.parseInt(sales1.getPic());
                            }*/
                                TextView tv = (TextView) viewmain.findViewById(R.id.tv_amt);
                                tv.setText(String.valueOf(total));
                                TextView tvsales = (TextView) viewmain.findViewById(R.id.tv_sales_qty);
                                tvsales.setText(salesTotal + "/" + pcsTotal);
                                calculateCost();
                                dialog.dismiss();
                            }
                        }
                    });
                }
            });
            addProducts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.setString("from", "glist");
                    SalesInvoiceActivity.tab_position = 2;
                    Intent intent = new Intent(getActivity(), CategoryListActivity.class);
                    getActivity().startActivity(intent);
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(orderID.equals("")||orderID == null){
                        String purchaseNumber = Helpers.generateNumber(db, ConfigStore.GoodReturns_PR_Type);
                        for(Sales sale:arrProductList){
                            HashMap<String, String> map = new HashMap<>();
                            map.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
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
                            map.put(db.KEY_ORDER_ID,purchaseNumber);
                            map.put(db.KEY_PURCHASE_NUMBER,purchaseNumber);
                            map.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                            map.put(db.KEY_IS_PRINTED,App.DATA_NOT_POSTED);
                            db.addData(db.RETURNS, map);
                        }
                    }
                    else{
                        Log.e("In Update","In update");
                        for(Sales sale:arrProductList){
                            HashMap<String, String> map = new HashMap<>();
                            map.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
                            map.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
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
                            map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
                            map.put(db.KEY_IS_PRINTED, App.DATA_NOT_POSTED);
                            HashMap<String,String>filter = new HashMap<String, String>();
                            filter.put(db.KEY_PURCHASE_NUMBER,orderID);
                            filter.put(db.KEY_REASON_TYPE,App.GOOD_RETURN);
                            filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                            filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                            filter.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());
                            if(db.checkData(db.RETURNS,filter)){
                                db.updateData(db.RETURNS,map,filter);
                            }
                            else{
                                db.addData(db.RETURNS,map);
                            }
                       /* db.updateData(db.RETURNS, map, filter);*/
                        }
                    }
                    getActivity().finish();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        return viewmain;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("Step1", "Step1");
        try{
            if(savedInstanceState!=null){
                Log.e("i am here","here");
                arrProductList = savedInstanceState.getParcelableArrayList("gr");
                setGoodReturns(arrProductList);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e("GR Bundle", "" + arrProductList.size());
        super.onSaveInstanceState(outState);
        try{
            outState.putParcelableArrayList("gr", arrProductList);
            Const.grBundle = new Bundle();
            Const.grBundle.putParcelableArrayList("gr",arrProductList);
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    private int getIndex(String myString){
        int index = 0;
        for (int i=0;i<reasonsList.size();i++){
            Reasons reason = reasonsList.get(i);
            if(reason.getReasonID().equals(myString)){
                index = i;
            }
        }
        return index;
    }
    public static ArrayList<Sales> setProductList(){
        try{
            if(arrProductList.size()>0){
                for(int i=0;i<Const.addlist.size();i++){
                    HashMap<String,String>searchMap = new HashMap<>();
                    searchMap.put(db.KEY_MATERIAL_NO,"");
                    searchMap.put(db.KEY_BASE_UOM,"");
                    searchMap.put(db.KEY_MATERIAL_DESC1,"");
                    HashMap<String,String>filterSearch = new HashMap<>();
                    filterSearch.put(db.KEY_MATERIAL_DESC1, UrlBuilder.clean(Const.addlist.get(i)));

                    Cursor articleCursor = db.getData(db.ARTICLE_HEADER,searchMap,filterSearch);
                    articleCursor.moveToFirst();

                    ArticleHeader articleHeader = ArticleHeader.getArticle(articles,UrlBuilder.encodeString(Const.addlist.get(i)));
                    Sales sale = new Sales();
                    sale.setMaterial_no(articleCursor.getString(articleCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    sale.setItem_code(articleCursor.getString(articleCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    sale.setUom(articleCursor.getString(articleCursor.getColumnIndex(db.KEY_BASE_UOM)));
                    sale.setName(Const.addlist.get(i));
                    sale.setCases("0");
                    sale.setPic("0");

                    HashMap<String,String> filterPart = new HashMap<>();
                    filterPart.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());

                    HashMap<String,String> map = new HashMap<>();
                    map.put(db.KEY_MATERIAL_NO, "");
                    map.put(db.KEY_AMOUNT, "");
                    if(db.checkData(db.PRICING,filterPart)){
                        //Pricing exists for Product for customer
                        //Pricing exists for Product for customer
                        Cursor priceCursor = db.getData(db.PRICING,map,filterPart);
                        if(priceCursor.getCount()>0){
                            priceCursor.moveToFirst();
                            String price = priceCursor.getString(priceCursor.getColumnIndex(db.KEY_AMOUNT));
                            sale.setPrice(sale.getUom().equals(App.CASE_UOM)||sale.getUom().equals(App.BOTTLES_UOM)?price:price);
                        }
                    }
                    else{
                        sale.setPrice("0");
                    }

                    HashMap<String, String> altMap = new HashMap<>();
                    altMap.put(db.KEY_UOM, "");
                    HashMap<String, String> filter = new HashMap<>();
                    filter.put(db.KEY_MATERIAL_NO, articleCursor.getString(articleCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    Cursor altUOMCursor = db.getData(db.ARTICLE_UOM, altMap, filter);
                    if (altUOMCursor.getCount() > 0) {
                        altUOMCursor.moveToFirst();
                        if (articleCursor.getString(articleCursor.getColumnIndex(db.KEY_BASE_UOM)).equals(altUOMCursor.getString(altUOMCursor.getColumnIndex(db.KEY_UOM)))
                                ||articleCursor.getString(articleCursor.getColumnIndex(db.KEY_BASE_UOM)).equals(altUOMCursor.getString(altUOMCursor.getColumnIndex(db.KEY_UOM)))) {
                            sale.setIsAltUOM(false);
                        } else {
                            sale.setIsAltUOM(true);
                        }
                    } else {
                        sale.setIsAltUOM(false);
                    }


                    arrProductList.add(sale);
                }
            }
            else{
                arrProductList.clear();
                for(int i=0;i<Const.addlist.size();i++){
                    HashMap<String,String>searchMap = new HashMap<>();
                    searchMap.put(db.KEY_MATERIAL_NO,"");
                    searchMap.put(db.KEY_BASE_UOM,"");
                    searchMap.put(db.KEY_MATERIAL_DESC1,"");
                    HashMap<String,String>filterSearch = new HashMap<>();
                    filterSearch.put(db.KEY_MATERIAL_DESC1, UrlBuilder.clean(Const.addlist.get(i)));

                    Cursor articleCursor = db.getData(db.ARTICLE_HEADER,searchMap,filterSearch);
                    articleCursor.moveToFirst();

                    ArticleHeader articleHeader = ArticleHeader.getArticle(articles,UrlBuilder.encodeString(Const.addlist.get(i)));
                    Sales sale = new Sales();
                    sale.setMaterial_no(articleCursor.getString(articleCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    sale.setItem_code(articleCursor.getString(articleCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    sale.setUom(articleCursor.getString(articleCursor.getColumnIndex(db.KEY_BASE_UOM)));
                    sale.setName(Const.addlist.get(i));
                    sale.setCases("0");
                    sale.setPic("0");

                    HashMap<String,String> filterPart = new HashMap<>();
                    filterPart.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());

                    HashMap<String,String> map = new HashMap<>();
                    map.put(db.KEY_MATERIAL_NO, "");
                    map.put(db.KEY_AMOUNT, "");
                    if(db.checkData(db.PRICING,filterPart)){
                        //Pricing exists for Product for customer
                        //Pricing exists for Product for customer
                        Cursor priceCursor = db.getData(db.PRICING,map,filterPart);
                        if(priceCursor.getCount()>0){
                            priceCursor.moveToFirst();
                            String price = priceCursor.getString(priceCursor.getColumnIndex(db.KEY_AMOUNT));
                            sale.setPrice(sale.getUom().equals(App.CASE_UOM)||sale.getUom().equals(App.BOTTLES_UOM) ? price : price);
                        }
                    }
                    else{
                        sale.setPrice("0");
                    }
                    arrProductList.add(sale);
                }
            }

            Log.e("FInal","" + arrProductList.size());
            adapter.notifyDataSetChanged();
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return null;
    }
    public class loadReasons extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_REASON_TYPE,"");
            map.put(db.KEY_REASON_DESCRIPTION,"");
            map.put(db.KEY_REASON_CODE,"");
            HashMap<String,String> filter = new HashMap<>();
            Cursor cursor = db.getData(db.REASONS,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                do{
                    Reasons reasons = new Reasons();
                    reasons.setReasonID(cursor.getString(cursor.getColumnIndex(db.KEY_REASON_CODE)));
                    reasons.setReasonType(cursor.getString(cursor.getColumnIndex(db.KEY_REASON_TYPE)));
                    reasons.setReasonDescription(cursor.getString(cursor.getColumnIndex(db.KEY_REASON_DESCRIPTION)));
                    if(reasons.getReasonID().contains("ZG")){
                        reasonsList.add(reasons);
                    }

                }
                while (cursor.moveToNext());
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            myAdapter.notifyDataSetChanged();
        }
    }
    public class loadGoodReturns extends AsyncTask<Void,Void,Void>{
        private String orderID;
        private loadGoodReturns(String orderID) {
            this.orderID = orderID;
            SalesInvoiceActivity.tab_position = 2;
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_TIME_STAMP,"");
                map.put(db.KEY_TRIP_ID,"");
                map.put(db.KEY_CUSTOMER_NO,"");
                map.put(db.KEY_REASON_TYPE,"");
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
                filter.put(db.KEY_ORDER_ID,orderID);
                filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                filter.put(db.KEY_REASON_TYPE,App.GOOD_RETURN);
                Cursor cursor = db.getData(db.RETURNS, map, filter);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    setProductListGR(cursor);
                }
            }
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
            calculateCost();
        }
    }
    private void calculateCost(){
        try{
            int salesTotal = 0;
            int pcsTotal = 0;
            double total = 0;
            for(Sales sale:arrProductList){
                double itemPrice = 0;
                if(sale.getUom().equals(App.CASE_UOM)||sale.getUom().equals(App.CASE_UOM_NEW) || sale.getUom().equals(App.BOTTLES_UOM)){
                    itemPrice = Double.parseDouble(sale.getCases())*Double.parseDouble(sale.getPrice());
                }
            /*else if(sale.getUom().equals(App.BOTTLES_UOM)){
                itemPrice = Double.parseDouble(sale.getPic())*Double.parseDouble(sale.getPrice());
            }*/
                total+=itemPrice;
                salesTotal = salesTotal + Integer.parseInt(sale.getCases());
                pcsTotal = pcsTotal + Integer.parseInt(sale.getPic());
            }
            Const.grBundle = new Bundle();
            Const.grBundle.putParcelableArrayList("gr",arrProductList);
            TextView tv = (TextView) viewmain.findViewById(R.id.tv_amt);
            tv.setText(String.valueOf(total));
            TextView tvsales = (TextView) viewmain.findViewById(R.id.tv_sales_qty);
            tvsales.setText(salesTotal + "/" + pcsTotal);
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    private void setProductListGR(Cursor cursor){
        try{
            Cursor grCursor = cursor;
            grCursor.moveToFirst();
            do{
                Sales sale = new Sales();
                sale.setMaterial_no(grCursor.getString(grCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                sale.setItem_code(grCursor.getString(grCursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                sale.setUom(grCursor.getString(grCursor.getColumnIndex(db.KEY_UOM)));
                sale.setName(UrlBuilder.decodeString(grCursor.getString(grCursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
                sale.setCases(grCursor.getString(grCursor.getColumnIndex(db.KEY_CASE)));
                sale.setPic(grCursor.getString(grCursor.getColumnIndex(db.KEY_UNIT)));
                sale.setReasonCode(grCursor.getString(grCursor.getColumnIndex(db.KEY_REASON_CODE)));
                HashMap<String,String> filterPart = new HashMap<>();
                filterPart.put(db.KEY_MATERIAL_NO, sale.getMaterial_no());

                HashMap<String,String> map = new HashMap<>();
                map.put(db.KEY_MATERIAL_NO, "");
                map.put(db.KEY_AMOUNT, "");
                if(db.checkData(db.PRICING,filterPart)){
                    //Pricing exists for Product for customer
                    //Pricing exists for Product for customer
                    Cursor priceCursor = db.getData(db.PRICING,map,filterPart);
                    if(priceCursor.getCount()>0){
                        priceCursor.moveToFirst();
                        String price = priceCursor.getString(priceCursor.getColumnIndex(db.KEY_AMOUNT));
                        sale.setPrice(sale.getUom().equals(App.CASE_UOM)?String.valueOf(Float.parseFloat(price)*10):price);
                    }
                }
                else{
                    sale.setPrice("0");
                }
                arrProductList.add(sale);

            }
            while (grCursor.moveToNext());
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }
    private void setGoodReturns(ArrayList<Sales>arrayList){
        adapter = new SalesInvoiceAdapter(getActivity(), arrayList);
        Const.grBundle = new Bundle();
        Const.grBundle.putParcelableArrayList("gr",arrayList);
        listSales.setAdapter(adapter);
        calculateCost();
        adapter.notifyDataSetChanged();
    }
}
