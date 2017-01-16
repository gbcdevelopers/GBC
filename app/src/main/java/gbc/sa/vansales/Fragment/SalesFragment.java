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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.CategoryListActivity;
import gbc.sa.vansales.activities.CustomerDetailActivity;
import gbc.sa.vansales.adapters.SalesAdapter;
import gbc.sa.vansales.adapters.SalesInvoiceAdapter;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.models.Sales;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by eheuristic on 12/5/2016.
 */
public class SalesFragment extends Fragment {
    View viewmain;
    ListView listSales;
    public static SalesAdapter adapter;
    ArrayAdapter<Sales> myAdapter;
    private  ArrayList<Sales> salesarrayList;
    FloatingActionButton fab;
    boolean workStarted = false;
    DatabaseHandler db;
    Customer object;
    public ArrayList<ArticleHeader> articles;
    String orderID="";
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewmain = inflater.inflate(R.layout.fragment_salesinvoice, container, false);
        object = getArguments().getParcelable("data");
        Log.e("Sales Frag", "" + object.getCustomerID());
        TextView tv = (TextView) viewmain.findViewById(R.id.tv_available_limit);
        tv.setText(Const.availableLimit);
        db = new DatabaseHandler(getActivity());
        Activity activity = getActivity();
        Log.e("Activity", "" + activity);
        articles = new ArrayList<>();
        articles = ArticleHeaders.get();
        listSales = (ListView) viewmain.findViewById(R.id.list_sales);
        fab = (FloatingActionButton) viewmain.findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.btn_select_all));
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.btn_select_all));
        //fab.hide();
        salesarrayList = new ArrayList<>();
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
        filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
        if(db.checkData(db.CAPTURE_SALES_INVOICE,filter)){
            HashMap<String,String>map = new HashMap<>();
            map.put(db.KEY_ORDER_ID,"");
            Cursor cursor = db.getData(db.CAPTURE_SALES_INVOICE,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                orderID = cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID));
                new loadItems(cursor.getString(cursor.getColumnIndex(db.KEY_ORDER_ID)));
            }
        }else{
            new loadItems("");
        }

        String strProductname[] = {"A", "B", "c", "D"};

        /*for (int i = 0; i < 4; i++) {
            Sales product = new Sales();
            product.setName(strProductname[i]);
            product.setCases("0");
            product.setPic("0");
            salesarrayList.add(product);
        }*/
       // adapter = new SalesAdapter(getActivity(), salesarrayList);
        myAdapter = new SalesInvoiceAdapter(getActivity(), salesarrayList);
        listSales.setAdapter(myAdapter);
        listSales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final Sales sales = salesarrayList.get(position);
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_with_crossbutton);
                dialog.setCancelable(false);
                TextView tv = (TextView) dialog.findViewById(R.id.dv_title);
                tv.setText(sales.getName());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ImageView iv_cancle = (ImageView) dialog.findViewById(R.id.imageView_close);
                Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
                final EditText ed_cases = (EditText) dialog.findViewById(R.id.ed_cases);
                final EditText ed_pcs = (EditText) dialog.findViewById(R.id.ed_pcs);
                final EditText ed_cases_inv = (EditText) dialog.findViewById(R.id.ed_cases_inv);
                final EditText ed_pcs_inv = (EditText) dialog.findViewById(R.id.ed_pcs_inv);

                RelativeLayout rl_specify=(RelativeLayout)dialog.findViewById(R.id.rl_specify_reason);
                rl_specify.setVisibility(View.GONE);

                ed_cases_inv.setText(sales.getInv_cases());
                ed_pcs_inv.setText(sales.getInv_piece());
                ed_cases_inv.setEnabled(false);
                ed_pcs_inv.setEnabled(false);
                if(sales.getUom().equals(App.BOTTLES_UOM)){
                    ed_cases.setEnabled(false);
                }
                else if(sales.getUom().equals(App.CASE_UOM)||sales.getUom().equals(App.CASE_UOM_NEW)){
                    ed_pcs.setEnabled(false);
                }
                ed_cases.setText(sales.getCases());
                ed_pcs.setText(sales.getPic());
                LinearLayout ll_1 = (LinearLayout) dialog.findViewById(R.id.ll_1);
                iv_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strCase = ed_cases.getText().toString();
                        String strpcs = ed_pcs.getText().toString();
                        String strcaseinv = ed_cases_inv.getText().toString();
                        String strpcsinv = ed_pcs_inv.getText().toString();
                        TextView tv_cases = (TextView) view.findViewById(R.id.tv_cases_value);
                        TextView tv_pcs = (TextView) view.findViewById(R.id.tv_pcs_value);
                        tv_cases.setText(strCase);
                        tv_pcs.setText(strpcs);
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
                        sales.setCases(strCase);
                        sales.setPic(strpcs);
                        if (Float.parseFloat(strCase) > Float.parseFloat(strcaseinv)) {
                            Toast.makeText(getActivity(), getString(R.string.input_larger), Toast.LENGTH_SHORT).show();
                            strCase = "0";
                            sales.setCases(strCase);
                        } else if (Float.parseFloat(strpcs) > Float.parseFloat(strpcsinv)) {
                            Toast.makeText(getActivity(), getString(R.string.input_larger), Toast.LENGTH_SHORT).show();
                            strpcs = "0";
                            sales.setPic(strpcs);
                        } else {
                            int salesTotal = 0;
                            int pcsTotal = 0;
                            double total = 0;
                            for(Sales sale:salesarrayList){
                                double itemPrice = 0;
                                if(sale.getUom().equals(App.CASE_UOM)||sale.getUom().equals(App.CASE_UOM_NEW)){
                                    itemPrice = Double.parseDouble(sale.getCases())*Double.parseDouble(sale.getPrice());
                                }
                                else if(sale.getUom().equals(App.BOTTLES_UOM)){
                                    itemPrice = Double.parseDouble(sale.getPic())*Double.parseDouble(sale.getPrice());
                                }
                                total+=itemPrice;
                                salesTotal = salesTotal + Integer.parseInt(sale.getCases());
                                pcsTotal = pcsTotal + Integer.parseInt(sale.getPic());
                            }
                            TextView tv = (TextView) viewmain.findViewById(R.id.tv_amt);
                            tv.setText(String.valueOf(total));
                            TextView tvsales = (TextView) viewmain.findViewById(R.id.tv_sales_qty);
                            tvsales.setText(salesTotal + "/" + pcsTotal);
                            dialog.dismiss();
                        }


                    }
                });
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderID.equals("")||orderID==null){
                    String purchaseNumber = Helpers.generateNumber(db, ConfigStore.InvoiceRequest_PR_Type);
                    for (Sales sale : salesarrayList) {
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
                        map.put(db.KEY_ORDER_ID,purchaseNumber);
                        map.put(db.KEY_PURCHASE_NUMBER,purchaseNumber);
                        db.addData(db.CAPTURE_SALES_INVOICE, map);
                    }
                }
                else{
                   // String purchaseNumber = Helpers.generateNumber(db, ConfigStore.InvoiceRequest_PR_Type);
                    for (Sales sale : salesarrayList) {
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
                        HashMap<String, String> filter = new HashMap<>();
                        filter.put(db.KEY_PURCHASE_NUMBER,orderID);
                        filter.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                        filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
                        db.addData(db.CAPTURE_SALES_INVOICE, map);
                    }
                }

              //  Const.salesarrayList = salesarrayList;

                getActivity().finish();
            }
        });
        return viewmain;
    }

    private void calculateCost(){
        int salesTotal = 0;
        int pcsTotal = 0;
        double total = 0;
        for(Sales sale:salesarrayList){
            double itemPrice = 0;
            if(sale.getUom().equals(App.CASE_UOM)||sale.getUom().equals(App.CASE_UOM_NEW)){
                itemPrice = Double.parseDouble(sale.getCases())*Double.parseDouble(sale.getPrice());
            }
            else if(sale.getUom().equals(App.BOTTLES_UOM)){
                itemPrice = Double.parseDouble(sale.getPic())*Double.parseDouble(sale.getPrice());
            }
            total+=itemPrice;
            salesTotal = salesTotal + Integer.parseInt(sale.getCases());
            pcsTotal = pcsTotal + Integer.parseInt(sale.getPic());
        }
        TextView tv = (TextView) viewmain.findViewById(R.id.tv_amt);
        tv.setText(String.valueOf(total));
        TextView tvsales = (TextView) viewmain.findViewById(R.id.tv_sales_qty);
        tvsales.setText(salesTotal + "/" + pcsTotal);
    }
    private class loadItems extends AsyncTask<Void, Void, Void> {
        private String orderID;
        private loadItems(String orderID) {
            this.orderID = orderID;
            execute();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_DELIVERY_NO, "");
                map.put(db.KEY_ITEM_NO, "");
                map.put(db.KEY_ITEM_CATEGORY, "");
                map.put(db.KEY_MATERIAL_NO, "");
                map.put(db.KEY_MATERIAL_DESC1,"");
                map.put(db.KEY_ACTUAL_QTY_CASE, "");
                map.put(db.KEY_REMAINING_QTY_CASE, "");
                map.put(db.KEY_ACTUAL_QTY_UNIT, "");
                map.put(db.KEY_REMAINING_QTY_UNIT, "");
                map.put(db.KEY_UOM_CASE, "");
                map.put(db.KEY_UOM_UNIT, "");
                map.put(db.KEY_IS_VERIFIED, "");
                HashMap<String, String> filter = new HashMap<>();
                Cursor cursor = db.getData(db.VAN_STOCK_ITEMS, map, filter);
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        if(this.orderID.equals("")||this.orderID==null){
                            setLoadItems(cursor, false, "");
                        }
                        else{
                            setLoadItems(cursor, true, this.orderID);
                        }

                    }
                }
            catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
           // adapter.notifyDataSetChanged();
            myAdapter.notifyDataSetChanged();
            calculateCost();
        }
    }
    private void setLoadItems(Cursor loadItems,boolean isSaved,String orderID) {
        workStarted = true;
        salesarrayList.clear();
        Cursor cursor = loadItems;
        cursor.moveToFirst();
        do {
            try {
                LoadSummary loadItem = new LoadSummary();
                Sales product = new Sales();
                product.setItem_code(cursor.getString(cursor.getColumnIndex(db.KEY_ITEM_NO)));
                product.setItem_category(cursor.getString(cursor.getColumnIndex(db.KEY_ITEM_CATEGORY)));
                product.setMaterial_no(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                product.setMaterial_description(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                ArticleHeader article = ArticleHeader.getArticle(articles, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                Log.e("Article IF", "" + article);
                if (!(article == null)) {
                    loadItem.setItemDescription(UrlBuilder.decodeString(article.getMaterialDesc1()));
                    product.setName(UrlBuilder.decodeString(article.getMaterialDesc1()));
                    product.setUom(article.getBaseUOM());
                } else {
                    loadItem.setItemDescription(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                    product.setName(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));

                }
                product.setInv_cases(cursor.getString(cursor.getColumnIndex(db.KEY_UOM_CASE)).equals(App.CASE_UOM) ? cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_CASE)) : "0");
                product.setInv_piece(cursor.getString(cursor.getColumnIndex(db.KEY_UOM_UNIT)).equals(App.BOTTLES_UOM) ? cursor.getString(cursor.getColumnIndex(db.KEY_REMAINING_QTY_UNIT)) : "0");
                product.setCases("0");
                product.setPic("0");

                HashMap<String,String> filterComp = new HashMap<>();
                filterComp.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
                filterComp.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));

                HashMap<String,String> filterPart = new HashMap<>();
                filterPart.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));

                HashMap<String,String> map = new HashMap<>();
                map.put(db.KEY_MATERIAL_NO,"");
                map.put(db.KEY_AMOUNT,"");

                if(db.checkData(db.PRICING,filterComp)){
                    //Pricing exists for Product for customer
                    Cursor customerPriceCursor = db.getData(db.PRICING,map,filterComp);
                    if(customerPriceCursor.getCount()>0){
                        customerPriceCursor.moveToFirst();
                        String price = customerPriceCursor.getString(customerPriceCursor.getColumnIndex(db.KEY_AMOUNT));
                        product.setPrice(product.getUom().equals(App.CASE_UOM)?String.valueOf(Float.parseFloat(price)*10):price);
//                        product.setPrice(cursor.getString(cursor.getColumnIndex(db.KEY_UOM_CASE)).equals(App.CASE_UOM) ? String.valueOf(Float.parseFloat(price) * 10) : price);
                    }
                }
                else if(db.checkData(db.PRICING,filterPart)){
                    //Pricing exists for Product for customer
                    //Pricing exists for Product for customer
                    Cursor priceCursor = db.getData(db.PRICING,map,filterPart);
                    if(priceCursor.getCount()>0){
                        priceCursor.moveToFirst();
                        String price = priceCursor.getString(priceCursor.getColumnIndex(db.KEY_AMOUNT));
                        product.setPrice(product.getUom().equals(App.CASE_UOM)?String.valueOf(Float.parseFloat(price)*10):price);
                    }
                }
                else{
                    product.setPrice("0");
                }
                salesarrayList.add(product);
            } catch (Exception e) {
                Log.e("Exception 1","Exception 1");
                e.printStackTrace();
            }
        }
        while (cursor.moveToNext());
        if(isSaved){
            HashMap<String,String> altMap = new HashMap<>();
            altMap.put(db.KEY_ITEM_NO,"");
            altMap.put(db.KEY_ITEM_CATEGORY,"");
            altMap.put(db.KEY_MATERIAL_NO,"");
            altMap.put(db.KEY_MATERIAL_DESC1,"");
            altMap.put(db.KEY_MATERIAL_GROUP,"");
            altMap.put(db.KEY_ORG_CASE,"");
            altMap.put(db.KEY_ORG_UNITS,"");
            altMap.put(db.KEY_AMOUNT,"");
            altMap.put(db.KEY_UOM,"");
            HashMap<String,String>filterMap = new HashMap<>();
            filterMap.put(db.KEY_CUSTOMER_NO, object.getCustomerID());
            filterMap.put(db.KEY_PURCHASE_NUMBER,orderID);
            filterMap.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);

            Cursor savedDataCursor = db.getData(db.CAPTURE_SALES_INVOICE,altMap,filterMap);
            if (savedDataCursor.getCount()>0){
                savedDataCursor.moveToFirst();
                do{
                    for(int i=0;i<salesarrayList.size();i++){
                        Sales sale = salesarrayList.get(i);
                        if(sale.getMaterial_no().equals(savedDataCursor.getString(savedDataCursor.getColumnIndex(db.KEY_MATERIAL_NO)))){
                            sale.setCases(savedDataCursor.getString(savedDataCursor.getColumnIndex(db.KEY_ORG_CASE)));
                            sale.setPic(savedDataCursor.getString(savedDataCursor.getColumnIndex(db.KEY_ORG_UNITS)));
                        }
                        salesarrayList.remove(i);
                        salesarrayList.add(i,sale);
                    }
                }
                while (savedDataCursor.moveToNext());
            }
        }
        // adapter.notifyDataSetChanged();
    }
}
