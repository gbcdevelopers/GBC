package gbc.sa.vansales.activities;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ItemComplaintAdapter;
import gbc.sa.vansales.adapters.OrderListBadgeAdapter;
import gbc.sa.vansales.adapters.PresaleAdapter;
import gbc.sa.vansales.adapters.SalesInvoiceAdapter;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.models.Sales;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Rakshit on 26-Mar-17.
 */
public class ItemComplaints extends AppCompatActivity {
    ImageView iv_back, iv_refresh;
    TextView tv_top_header;
    ListView list_items;
    public static ItemComplaintAdapter adapter;
    FloatingActionButton fab;
    ArrayList<Integer> proceedArrayList;
    Customer object;
    private static DatabaseHandler db;
    LoadingSpinner loadingSpinner;
    SwipeRefreshLayout refreshLayout;
    public static ArrayList<Sales> arrProductList;
    static ArrayList<ArticleHeader> articles = new ArrayList<>();
    private String imagePath = null;
    private String image = null;
    Button btn_imageFW;
    Button btn_imageRW;
    Button btn_imageLW;
    Button btn_imageBW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_complaints);
        arrProductList = new ArrayList<>();
        loadingSpinner = new LoadingSpinner(this);
        //refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        db = new DatabaseHandler(this);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.item_complaint));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                showCustomerSignCapture();
            }
        });
        list_items = (ListView)findViewById(R.id.list_items);
        adapter = new ItemComplaintAdapter(this, arrProductList);
        list_items.setAdapter(adapter);
        articles = ArticleHeaders.get();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalesInvoiceActivity.tab_position = 99;
                Settings.setString("from", "itemcomplaint");
                Intent intent = new Intent(ItemComplaints.this, CategoryListActivity.class);
                startActivity(intent);
            }
        });

        list_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Sales sales = arrProductList.get(position);
                final Dialog dialog = new Dialog(ItemComplaints.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_with_crossbutton_itemcomplaint);
                dialog.setCancelable(false);
                TextView tv = (TextView) dialog.findViewById(R.id.dv_title);
                tv.setText(sales.getName());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ImageView iv_cancel = (ImageView) dialog.findViewById(R.id.imageView_close);
                Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
                btn_imageFW = (Button)dialog.findViewById(R.id.capture1);
                btn_imageFW.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePath = Helpers.takePhoto(ItemComplaints.this,"1");
                    }
                });
                btn_imageLW = (Button)dialog.findViewById(R.id.capture2);
                btn_imageLW.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        imagePath = Helpers.takePhoto(ItemComplaints.this,"2");
                    }
                });
                btn_imageRW = (Button)dialog.findViewById(R.id.capture3);
                btn_imageRW.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        imagePath = Helpers.takePhoto(ItemComplaints.this,"3");
                    }
                });
                btn_imageBW = (Button)dialog.findViewById(R.id.capture4);
                btn_imageBW.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        imagePath = Helpers.takePhoto(ItemComplaints.this,"4");
                    }
                });
                iv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void showCustomerSignCapture(){
        final Dialog dialog = new Dialog(ItemComplaints.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_customer_sign);
        dialog.setCancelable(false);
        dialog.show();
    }
    public static ArrayList<Sales> setProductList(){
        try{
            if(arrProductList.size()>0){
                for(int i=0;i< Const.addlist.size();i++){
                    HashMap<String,String> searchMap = new HashMap<>();
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

            Log.e("FInal", "" + arrProductList.size());
            adapter.notifyDataSetChanged();
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Helpers.TYPE_CAMERA_B1 && resultCode == RESULT_OK) {
            try {
                image = Helpers.imageToBase64(this, imagePath, Helpers.QUALITY_HIGH, true);
                //btn_imageFW.setIcon(R.drawable.icon_camera_green);
                btn_imageFW.setBackgroundResource(R.drawable.icon_camera_prim_32);
                Toast.makeText(this, "Photo added", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == Helpers.TYPE_CAMERA_B2 && resultCode == RESULT_OK) {
            try {
                image = Helpers.imageToBase64(this, imagePath, Helpers.QUALITY_HIGH, true);
                //btn_imageFW.setIcon(R.drawable.icon_camera_green);
                btn_imageLW.setBackgroundResource(R.drawable.icon_camera_prim_32);
                Toast.makeText(this, "Photo added", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == Helpers.TYPE_CAMERA_B3 && resultCode == RESULT_OK) {
            try {
                image = Helpers.imageToBase64(this, imagePath, Helpers.QUALITY_HIGH, true);
                //btn_imageFW.setIcon(R.drawable.icon_camera_green);
                btn_imageRW.setBackgroundResource(R.drawable.icon_camera_prim_32);
                Toast.makeText(this, "Photo added", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == Helpers.TYPE_CAMERA_B4 && resultCode == RESULT_OK) {
            try {
                image = Helpers.imageToBase64(this, imagePath, Helpers.QUALITY_HIGH, true);
                //btn_imageFW.setIcon(R.drawable.icon_camera_green);
                btn_imageBW.setBackgroundResource(R.drawable.icon_camera_prim_32);
                Toast.makeText(this, "Photo added", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
