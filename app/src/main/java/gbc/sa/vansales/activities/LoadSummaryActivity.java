package gbc.sa.vansales.activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.LoadSummaryBadgeAdapter;
import gbc.sa.vansales.adapters.ShopStatusBadgeAdapter;
import gbc.sa.vansales.adapters.StockTakeBadgeAdapter;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.LoadDeliveryHeader;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.models.Product;
import gbc.sa.vansales.models.ShopStatus;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Rakshit on 19-Nov-16.
 */
public class LoadSummaryActivity extends AppCompatActivity {
    private ArrayList<LoadSummary> loadSummaryList;
    private ArrayList<LoadSummary> loadSummaryUnmodList;
    private ArrayAdapter<LoadSummary> adapter;
    private SwipeLayout swipeLayout;
    private ListView listView,loadListView;
    private Button verifyAll;
    private int loadSummaryCount=0;
    private final static String TAG = LoadSummaryActivity.class.getSimpleName();
    LoadDeliveryHeader object;

    private ArrayList<ArticleHeader> articles;
    DatabaseHandler db = new DatabaseHandler(this);
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_load_summary);
        Intent i=this.getIntent();
        object = (LoadDeliveryHeader)i.getParcelableExtra("headerObj");
        Log.e("Object","" + object.getDeliveryNo());
       // final int position=i.getIntExtra("headerObj",0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadSummaryList = new ArrayList<>();
        loadSummaryUnmodList = new ArrayList<>();
        articles = new ArrayList<>();
        articles = ArticleHeaders.get();
        Log.e("Articles","" + articles.size());

        adapter = new LoadSummaryBadgeAdapter(LoadSummaryActivity.this, loadSummaryList);
        listView = (ListView)findViewById(R.id.list_item);
        verifyAll=(Button)findViewById(R.id.btn_verify_all);
        loadListView=(ListView)findViewById(R.id.srListView);
        setListView();
        new loadSummary(object.getDeliveryNo().toString());

        verifyAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               listView.setAdapter(null);
               LoadActivity.fullObject.setStatus("Checked");

                String size=Integer.toString(0);
                if(size=="0")
                {
                    Toast.makeText(getApplicationContext(), "All Loads Verified",Toast.LENGTH_SHORT).show();

                    Toast.makeText(getApplicationContext(), "Going to VanStock",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(LoadSummaryActivity.this,VanStockActivity.class);
                    startActivity(i);
                }
                Toast.makeText(getApplicationContext(),"Load Verified!",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        adapter = new LoadSummaryBadgeAdapter(LoadSummaryActivity.this, loadSummaryList);
        listView = (ListView)findViewById(R.id.list_item);
        setListView();
        //new loadSummary().execute();
    }

    private void loadData(){
        adapter.clear();
        for (int i = 0; i < 2; i++) {
            LoadSummary loadSummary = createLoadSummaryData(i);
            loadSummaryList.add(loadSummary);
        }

    }

    private ArrayList<LoadSummary> loadDataOld(){
       // adapter.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_DELIVERY_NO,"");
        map.put(db.KEY_ITEM_NO,"");
        map.put(db.KEY_ITEM_CATEGORY,"");
        map.put(db.KEY_MATERIAL_NO,"");
        map.put(db.KEY_ACTUAL_QTY,"");
        map.put(db.KEY_UOM,"");


        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_DELIVERY_NO,object.getDeliveryNo().toString());

        Cursor cursor = db.getData(db.LOAD_DELIVERY_ITEMS,map,filter);

        cursor.moveToFirst();
        do {
            LoadSummary loadItem = new LoadSummary();
            loadItem.setItemCode(cursor.getString(cursor.getColumnIndex(db.KEY_ITEM_NO)));
            ArticleHeader article = ArticleHeader.getArticle(articles, cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
            Log.e("Article IF","" + article);

            if(!(article==null)){
                loadItem.setItemDescription(UrlBuilder.decodeString(article.getMaterialDesc1()));
            }
            else{
                loadItem.setItemDescription(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
            }
            // loadItem.setItemDescription(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
            loadItem.setQuantityCases(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)));
            loadItem.setQuantityUnits(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)));
            loadSummaryUnmodList.add(loadItem);

        }
        while (cursor.moveToNext());

        return loadSummaryUnmodList;

    }
    private void setListView() {
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.badge_load_summary, listView, false);
        swipeLayout = (SwipeLayout)header.findViewById(R.id.swipe_layout);
        setSwipeViewFeatures();
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
    public static LoadSummary createLoadSummaryData(int index){
        LoadSummary loadSummary = new LoadSummary();
        switch(index){
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
            map.put(db.KEY_DELIVERY_NO,"");
            map.put(db.KEY_ITEM_NO,"");
            map.put(db.KEY_ITEM_CATEGORY,"");
            map.put(db.KEY_MATERIAL_NO,"");
            map.put(db.KEY_ACTUAL_QTY,"");
            map.put(db.KEY_UOM,"");


            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_DELIVERY_NO,this.deliveryNo);

            Cursor cursor = db.getData(db.LOAD_DELIVERY_ITEMS,map,filter);
            if(cursor.getCount()>0){
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
            adapter.notifyDataSetChanged();
            // super.onPostExecute(aVoid);
        }
    }
    private void setLoadItems(Cursor loadSummary) {

        loadSummaryList.clear();
        Cursor cursor = loadSummary;
        cursor.moveToFirst();
        do {
            try{
                LoadSummary loadItem = new LoadSummary();
                loadItem.setItemCode(cursor.getString(cursor.getColumnIndex(db.KEY_ITEM_NO)));
                ArticleHeader article = ArticleHeader.getArticle(articles,cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                Log.e("Article IF", "" + article);

                if(!(article==null)){
                    loadItem.setItemDescription(UrlBuilder.decodeString(article.getMaterialDesc1()));
                }
                else{
                    loadItem.setItemDescription(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                }
                // loadItem.setItemDescription(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
                //String quantity = cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)?cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)):"0";
                //loadItem.setQuantityCases(cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)));
                loadItem.setQuantityCases(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.CASE_UOM)?cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)):"0");
                loadItem.setQuantityUnits(cursor.getString(cursor.getColumnIndex(db.KEY_UOM)).equals(App.BOTTLES_UOM)?cursor.getString(cursor.getColumnIndex(db.KEY_ACTUAL_QTY)):"0");
                loadSummaryList.add(loadItem);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        while (cursor.moveToNext());
        adapter.notifyDataSetChanged();
    }
    public void updateAdapter(ArrayList<LoadSummary>data) {
        adapter.notifyDataSetChanged();//update adapter
        if(adapter.getCount()==0&&loadSummaryCount!=0){
            //Log.e("1",""+loadSummaryList.size());
            final ArrayList<LoadSummary>loadSummaryData = data;
            AlertDialog.Builder builder = new AlertDialog.Builder(LoadSummaryActivity.this);
            builder.setTitle("Load Verified");
            builder.setMessage("Have you verified the load on the vehicle");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadSummaryUnmodList = loadDataOld();
                    
//                    Intent intent = new Intent(LoadSummaryActivity.this,LoadVerifyActivity.class);
//                    intent.putParcelableArrayListExtra("loadSummary", loadSummaryData);
//                    intent.putParcelableArrayListExtra("loadSummaryOld", loadSummaryUnmodList);
//                    startActivity(intent);

//                    Intent i=new Intent(LoadSummaryActivity.this,OdometerPopupActivity.class);
//                    startActivity(i);
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
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
}
