package gbc.sa.vansales.activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.LoadSummaryBadgeAdapter;
import gbc.sa.vansales.adapters.ReasonAdapter;
import gbc.sa.vansales.adapters.ShopStatusBadgeAdapter;
import gbc.sa.vansales.adapters.StockTakeBadgeAdapter;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.OrderReasons;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.CustomerStatus;
import gbc.sa.vansales.models.LoadDeliveryHeader;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.models.Product;
import gbc.sa.vansales.models.Reasons;
import gbc.sa.vansales.models.Sales;
import gbc.sa.vansales.models.ShopStatus;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Rakshit on 19-Nov-16.
 */
public class LoadSummaryActivity extends AppCompatActivity {
    private ArrayList<LoadSummary> loadSummaryList;
    private ArrayList<LoadSummary> loadSummaryUnmodList;
    private ArrayAdapter<LoadSummary> adapter;
    private SwipeLayout swipeLayout;
    private ListView listView, loadListView;
    private Button verifyAll;
    ArrayAdapter<Reasons> myAdapter;
    private int loadSummaryCount = 0;
    private final static String TAG = LoadSummaryActivity.class.getSimpleName();
    LoadDeliveryHeader object;
    public ArrayList<ArticleHeader> articles;
    DatabaseHandler db = new DatabaseHandler(this);
    private ArrayList<Reasons> reasonsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_summary);
        Intent i = this.getIntent();
        object = (LoadDeliveryHeader) i.getParcelableExtra("headerObj");
        //Log.e("Object", "" + object.getDeliveryNo());
        reasonsList = OrderReasons.get();
        Log.e("Reasons List","" + reasonsList.size());
        myAdapter = new ReasonAdapter(LoadSummaryActivity.this, android.R.layout.simple_spinner_item, reasonsList);
        // final int position=i.getIntExtra("headerObj",0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadSummaryList = new ArrayList<>();
        loadSummaryUnmodList = new ArrayList<>();
        articles = new ArrayList<>();
        articles = ArticleHeaders.get();

        //Log.e("ReasonsList","" + reasonsList.size());
        addBlank(reasonsList);
        //Log.e("Articles", "" + articles.size());
        adapter = new LoadSummaryBadgeAdapter(LoadSummaryActivity.this, loadSummaryList);
        listView = (ListView) findViewById(R.id.list_item);
        verifyAll = (Button) findViewById(R.id.btn_verify_all);
        loadListView = (ListView) findViewById(R.id.srListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final LoadSummary item = loadSummaryList.get(position);
                final Dialog dialog = new Dialog(LoadSummaryActivity.this);
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

                LinearLayout ll1 = (LinearLayout)dialog.findViewById(R.id.ll_1);
                ll1.setVisibility(View.GONE);
                RelativeLayout rl_specify=(RelativeLayout)dialog.findViewById(R.id.rl_specify_reason);
                rl_specify.setVisibility(View.VISIBLE);
                //
                final Spinner spin = (Spinner) dialog.findViewById(R.id.spin);
                Log.e("Adapter","" + myAdapter.getCount());
                spin.setAdapter(myAdapter);

                /*if(item.getUom().equals(App.CASE_UOM)||item.getUom().equals(App.CASE_UOM_NEW)||item.getUom().equals(App.BOTTLES_UOM)){
                    ed_pcs.setEnabled(false);
                }*/
                if (item.getReasonCode() != null) {
                    spin.setSelection(getIndex(item.getReasonCode()));
                }

                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Reasons reason = myAdapter.getItem(position);
                        item.setReasonCode(reason.getReasonID());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                if(item.isAltUOM()){
                    ed_pcs.setEnabled(true);
                }
                else{
                    ed_pcs.setEnabled(false);
                }
                ed_cases.setText(item.getQuantityCases());
                ed_pcs.setText(item.getQuantityUnits());
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
                        //Log.e("Spin Select","" + spin.getSelectedItem().toString());
                        if (spin.getSelectedItem().toString().equals("Select Reason")) {
                            ((TextView) spin.getSelectedView()).setError("select reason");
                        }
                        else{
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
                        item.setQuantityCases(strCase);
                        item.setQuantityUnits(strpcs);
                        loadSummaryList.remove(position);
                        loadSummaryList.add(position, item);
                        hideSoftKeyboard();
                        calculateCost();
                        dialog.dismiss();
                        /*if (Float.parseFloat(strCase) > Float.parseFloat(strcaseinv)) {
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

                            for(LoadSummary item:loadSummaryList){
                                double itemPrice = 0;
                                if(item.getUom().equals(App.CASE_UOM)||item.getUom().equals(App.CASE_UOM_NEW)){
                                    itemPrice = Double.parseDouble(item.getQuantityCases())*Double.parseDouble(item.getPrice());
                                }
                                else if(item.getUom().equals(App.BOTTLES_UOM)){
                                    itemPrice = Double.parseDouble(item.getQuantityUnits())*Double.parseDouble(item.getPrice());
                                }
                                total+=itemPrice;
                                salesTotal = salesTotal + Integer.parseInt(item.getQuantityCases());
                                pcsTotal = pcsTotal + Integer.parseInt(item.getQuantityUnits());
                            }
                            *//*TextView tv = (TextView) viewmain.findViewById(R.id.tv_amt);
                            tv.setText(String.valueOf(total));
                            TextView tvsales = (TextView) viewmain.findViewById(R.id.tv_sales_qty);
                            tvsales.setText(salesTotal + "/" + pcsTotal);*//*
                            dialog.dismiss();
                        }*/
                        }

                    }
                });
            }
        });

        setListView();
        new loadSummary(object.getDeliveryNo().toString());
        verifyAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // listView.setAdapter(null);
                // LoadActivity.fullObject.setStatus("Checked");
                loadSummaryUnmodList = loadDataOld();
                Intent intent = new Intent(LoadSummaryActivity.this, LoadVerifyActivity.class);
                intent.putParcelableArrayListExtra("loadSummary", loadSummaryList);
                intent.putParcelableArrayListExtra("loadSummaryOld", loadSummaryUnmodList);
                intent.putExtra("headerObj", object);
                startActivity(intent);

                /*String size=Integer.toString(0);
                if(size=="0")
                {
                    Toast.makeText(getApplicationContext(), "All Loads Verified",Toast.LENGTH_SHORT).show();

                    Toast.makeText(getApplicationContext(), "Going to VanStock",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(LoadSummaryActivity.this,VanStockActivity.class);
                    startActivity(i);
                }
                Toast.makeText(getApplicationContext(),"Load Verified!",Toast.LENGTH_SHORT).show();*/
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        try {
            adapter = new LoadSummaryBadgeAdapter(LoadSummaryActivity.this, loadSummaryList);
            listView = (ListView) findViewById(R.id.list_item);
            setListView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //new loadSummary().execute();
    }

    private void loadData() {
        adapter.clear();
        for (int i = 0; i < 2; i++) {
            LoadSummary loadSummary = createLoadSummaryData(i);
            loadSummaryList.add(loadSummary);
        }
    }
    private void calculateCost(){
        int salesTotal = 0;
        int pcsTotal = 0;
        double total = 0;
        for(LoadSummary item:loadSummaryList){
            double itemPrice = 0;
            if(!item.isAltUOM()){
                itemPrice = Double.parseDouble(item.getQuantityCases())*Double.parseDouble(item.getPrice());
            }
            total+=itemPrice;
        }
        TextView tv = (TextView) findViewById(R.id.tv_amt);
        tv.setText(String.valueOf(total));
        adapter.notifyDataSetChanged();//update adapter
    }
    private ArrayList<LoadSummary> loadDataOld() {
        // adapter.clear();
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_DELIVERY_NO, "");
            map.put(db.KEY_ITEM_NO, "");
            map.put(db.KEY_ITEM_CATEGORY, "");
            map.put(db.KEY_MATERIAL_NO, "");
            map.put(db.KEY_ACTUAL_QTY, "");
            map.put(db.KEY_UOM, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_DELIVERY_NO, object.getDeliveryNo().toString());
            Cursor cursor = db.getData(db.LOAD_DELIVERY_ITEMS, map, filter);
            cursor.moveToFirst();

            do {
                LoadSummary loadItem = new LoadSummary();
                loadItem.setItemCode(cursor.getString(cursor.getColumnIndex(db.KEY_ITEM_NO)));
                ArticleHeader article = ArticleHeader.getArticle(articles, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                //Log.e("Article IF", "" + article);
                if (!(article == null)) {
                    loadItem.setItemDescription(UrlBuilder.decodeString(article.getMaterialDesc1()));
                } else {
                    loadItem.setItemDescription(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                }
                HashMap<String,String>altMap = new HashMap<>();
                altMap.put(db.KEY_UOM, "");
                HashMap<String,String>filter1 = new HashMap<>();
                filter1.put(db.KEY_MATERIAL_NO, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));

                Cursor altUOMCursor = db.getData(db.ARTICLE_UOM,altMap,filter1);
                if(altUOMCursor.getCount()>0){
                    altUOMCursor.moveToFirst();
                    if(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(altUOMCursor.getString(altUOMCursor.getColumnIndex(db.KEY_UOM)))){
                        loadItem.setIsAltUOM(false);
                    }
                    else{
                        loadItem.setIsAltUOM(true);
                    }
                }
                else{
                    loadItem.setIsAltUOM(false);
                }
                HashMap<String,String>priceMap = new HashMap<>();
                priceMap.put(db.KEY_AMOUNT, "");
                HashMap<String,String>filterPrice = new HashMap<>();
                filterPrice.put(db.KEY_MATERIAL_NO,cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                filterPrice.put(db.KEY_PRIORITY,"2");
                Cursor priceCursor = db.getData(db.PRICING,priceMap,filterPrice);
                if(priceCursor.getCount()>0){
                    priceCursor.moveToFirst();
                    loadItem.setPrice(priceCursor.getString(priceCursor.getColumnIndex(db.KEY_AMOUNT)));
                }
                else{
                    loadItem.setPrice("0");
                }
                // loadItem.setItemDescription(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                loadItem.setQuantityCases(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM) || cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM) ? cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)) : "0");
                //loadItem.setQuantityUnits(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM) ? cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)) : "0");
                loadItem.setQuantityUnits("0");
                loadItem.setMaterialNo(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                loadItem.setUom(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                loadSummaryUnmodList.add(loadItem);
            }
            while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return loadSummaryUnmodList;
    }
    private void setListView() {
        try {
            LayoutInflater inflater = getLayoutInflater();
            View header = inflater.inflate(R.layout.badge_load_summary, listView, false);
            swipeLayout = (SwipeLayout) header.findViewById(R.id.swipe_layout);
            setSwipeViewFeatures();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // listView.addHeaderView(header);
    }
    private void setSwipeViewFeatures() {
        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottom_wrapper));
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                Log.i(TAG, "onClose");
            }
            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                Log.i(TAG, "on swiping");
            }
            @Override
            public void onStartOpen(SwipeLayout layout) {
                Log.i(TAG, "on start open");
            }
            @Override
            public void onOpen(SwipeLayout layout) {
                Log.i(TAG, "the BottomView totally show");
            }
            @Override
            public void onStartClose(SwipeLayout layout) {
                Log.i(TAG, "the BottomView totally close");
            }
            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public static LoadSummary createLoadSummaryData(int index) {
        LoadSummary loadSummary = new LoadSummary();
        switch (index) {
            case 0:
                loadSummary.setItemCode("BOT150CUP");
                loadSummary.setItemDescription("150 ML CUPS");
                loadSummary.setQuantityCases("100");
                loadSummary.setQuantityUnits("50");
                break;
            case 1:
                loadSummary.setItemCode("BOT330PET");
                loadSummary.setItemDescription("330 ML - PET Bottles");
                loadSummary.setQuantityCases("200");
                loadSummary.setQuantityUnits("100");
                break;
            case 2:
                loadSummary.setItemCode("BOT600PET");
                loadSummary.setItemDescription("600 ML - PET Bottles");
                loadSummary.setQuantityCases("50");
                loadSummary.setQuantityUnits("50");
                break;
            case 3:
                loadSummary.setItemCode("BOT15LPET");
                loadSummary.setItemDescription("1.5L - PET Bottles");
                loadSummary.setQuantityCases("300");
                loadSummary.setQuantityUnits("100");
                break;
            case 4:
                loadSummary.setItemCode("BOT189LPET");
                loadSummary.setItemDescription("1.89L - PET Bottles");
                loadSummary.setQuantityCases("10");
                loadSummary.setQuantityUnits("30");
                break;
            case 5:
                loadSummary.setItemCode("BOT4GAL");
                loadSummary.setItemDescription("4 Gallon Bottles");
                loadSummary.setQuantityCases("10");
                loadSummary.setQuantityUnits("10");
                break;
        }
        return loadSummary;
    }
    private class loadSummary extends AsyncTask<String, Void, Void> {
        private String deliveryNo;
        private loadSummary(String deliveryNo) {
            this.deliveryNo = deliveryNo;
            execute();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... params) {
            //Logic to fetch Data
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_DELIVERY_NO, "");
            map.put(db.KEY_ITEM_NO, "");
            map.put(db.KEY_ITEM_CATEGORY, "");
            map.put(db.KEY_MATERIAL_NO, "");
            map.put(db.KEY_ACTUAL_QTY, "");
            map.put(db.KEY_UOM, "");
            map.put(db.KEY_IS_VERIFIED, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_DELIVERY_NO, this.deliveryNo);
            filter.put(db.KEY_IS_VERIFIED, "false");
            Cursor cursor = db.getData(db.LOAD_DELIVERY_ITEMS, map, filter);
            if (cursor.getCount() > 0) {
                setLoadItems(cursor);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            //loadSummaryUnmodList = loadSummaryList;
            adapter = new LoadSummaryBadgeAdapter(LoadSummaryActivity.this, loadSummaryList);
            loadSummaryCount = loadSummaryList.size();
            listView.setAdapter(adapter);
            calculateCost();
            adapter.notifyDataSetChanged();
            // super.onPostExecute(aVoid);
        }
    }
    private void setLoadItems(Cursor loadSummary) {
        loadSummaryList.clear();
        Cursor cursor = loadSummary;
        cursor.moveToFirst();
        do {
            try {
                LoadSummary loadItem = new LoadSummary();
                loadItem.setItemCode(cursor.getString(cursor.getColumnIndex(db.KEY_ITEM_NO)));
                ArticleHeader article = ArticleHeader.getArticle(articles, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                //Log.e("Article IF", "" + article);
                if (!(article == null)) {
                    loadItem.setItemDescription(UrlBuilder.decodeString(article.getMaterialDesc1()));
                } else {
                    loadItem.setItemDescription(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                }
                HashMap<String,String>altMap = new HashMap<>();
                altMap.put(db.KEY_UOM,"");
                HashMap<String,String>filter = new HashMap<>();
                filter.put(db.KEY_MATERIAL_NO,cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));

                Cursor altUOMCursor = db.getData(db.ARTICLE_UOM,altMap,filter);
                if(altUOMCursor.getCount()>0){
                    altUOMCursor.moveToFirst();
                    if(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(altUOMCursor.getString(altUOMCursor.getColumnIndex(db.KEY_UOM)))){
                        loadItem.setIsAltUOM(false);
                    }
                    else{
                        loadItem.setIsAltUOM(true);
                    }
                }
                else{
                    loadItem.setIsAltUOM(false);
                }

                // loadItem.setItemDescription(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                //String quantity = cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)?cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)):"0";
                //loadItem.setQuantityCases(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)));
                HashMap<String,String>priceMap = new HashMap<>();
                priceMap.put(db.KEY_AMOUNT, "");
                HashMap<String,String>filterPrice = new HashMap<>();
                filterPrice.put(db.KEY_MATERIAL_NO,cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                filterPrice.put(db.KEY_PRIORITY,"2");
                Cursor priceCursor = db.getData(db.PRICING,priceMap,filterPrice);
                if(priceCursor.getCount()>0){
                    priceCursor.moveToFirst();
                    loadItem.setPrice(priceCursor.getString(priceCursor.getColumnIndex(db.KEY_AMOUNT)));
                }
                else{
                    loadItem.setPrice("0");
                }


                loadItem.setQuantityCases(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM) || cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM) ? cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)) : "0");
                //loadItem.setQuantityUnits(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM) ? cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)) : "0");
                loadItem.setQuantityUnits("0");
                loadItem.setMaterialNo(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                loadItem.setUom(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)));
                loadSummaryList.add(loadItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        while (cursor.moveToNext());
        adapter.notifyDataSetChanged();
    }
    public void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) LoadSummaryActivity.this.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                LoadSummaryActivity.this.getCurrentFocus().getWindowToken(), 0);
    }
    public void updateAdapter(ArrayList<LoadSummary> data) {
        adapter.notifyDataSetChanged();//update adapter
        if (adapter.getCount() == 0 && loadSummaryCount != 0) {
            //Log.e("1",""+loadSummaryList.size());
            final ArrayList<LoadSummary> loadSummaryData = data;
            AlertDialog.Builder builder = new AlertDialog.Builder(LoadSummaryActivity.this);
            builder.setTitle(getString(R.string.load_verify_title_alert));
            builder.setCancelable(false);
            builder.setMessage(getString(R.string.load_verify_title_alert_msg));
            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadSummaryUnmodList = loadDataOld();
                    Intent intent = new Intent(LoadSummaryActivity.this, LoadVerifyActivity.class);
                    intent.putParcelableArrayListExtra("loadSummary", loadSummaryData);
                    intent.putParcelableArrayListExtra("loadSummaryOld", loadSummaryUnmodList);
                    intent.putExtra("headerObj", object);
                    startActivity(intent);
//                    Intent i=new Intent(LoadSummaryActivity.this,OdometerPopupActivity.class);
//                    startActivity(i);
                }
            })
                    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = builder.create();
            // show it
            alertDialog.show();
        }
        // totalClassmates.setText("(" + friendsList.size() + ")"); //update total friends in list
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
    private void addBlank(ArrayList<Reasons>arrayList){
        Reasons reasons = new Reasons();
        reasons.setReasonID("99");
        reasons.setReasonDescription("Select Reason");
        reasons.setReasonType("");
        reasonsList.add(0,reasons);
    }
}
