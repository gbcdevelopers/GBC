package gbc.sa.vansales.activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.LoadSummaryBadgeAdapter;
import gbc.sa.vansales.adapters.ShopStatusBadgeAdapter;
import gbc.sa.vansales.adapters.StockTakeBadgeAdapter;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.models.Product;
import gbc.sa.vansales.models.ShopStatus;
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

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_load_summary);

        Intent i=getIntent();
        final int position=i.getIntExtra("1",0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadSummaryList = new ArrayList<>();
        loadSummaryUnmodList = new ArrayList<>();

        adapter = new LoadSummaryBadgeAdapter(LoadSummaryActivity.this, loadSummaryList);
        listView = (ListView)findViewById(R.id.list_item);
        verifyAll=(Button)findViewById(R.id.btn_verify_all);
        loadListView=(ListView)findViewById(R.id.srListView);
        setListView();
        new loadSummary().execute();

        verifyAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               listView.setAdapter(null);

                LoadActivity.searchResults.remove(position);

                String size=Integer.toString(LoadActivity.searchResults.size());
               // Toast.makeText(getApplicationContext(), ( Integer.toString(LoadActivity.searchResults.size())),Toast.LENGTH_SHORT).show();

                if(size=="0")
                {
                    Toast.makeText(getApplicationContext(), "All Loads Verified",Toast.LENGTH_SHORT).show();

                    Toast.makeText(getApplicationContext(), "Going to VanStock",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(LoadSummaryActivity.this,VanStockActivity.class);
                    startActivity(i);
                }

                //LoadActivity.lv.setAdapter(new SingleLoadActivity(LoadSummaryActivity.this,LoadActivity.searchResults));
               // LoadActivity.adapter.notifyAll();

                Toast.makeText(getApplicationContext(),"Load Verified!",Toast.LENGTH_SHORT).show();


                //LoadActivity obj=new LoadActivity();
                //obj.removeItems();


                /*final int position = loadListView.getPositionForView(v);

                loadListView.removeViewAt(position);

                notifyDataSetChanged();*/
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        adapter = new LoadSummaryBadgeAdapter(LoadSummaryActivity.this, loadSummaryList);
        listView = (ListView)findViewById(R.id.list_item);
        setListView();
        new loadSummary().execute();
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
        for (int i = 0; i < 2; i++) {
            LoadSummary loadSummary = createLoadSummaryData(i);
            loadSummaryUnmodList.add(loadSummary);
        }
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            //Logic to fetch Data
            loadData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadSummaryUnmodList = loadSummaryList;
            adapter = new LoadSummaryBadgeAdapter(LoadSummaryActivity.this, loadSummaryList);
            loadSummaryCount = loadSummaryList.size();
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            // super.onPostExecute(aVoid);
        }
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
