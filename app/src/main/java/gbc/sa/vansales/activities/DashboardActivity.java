package gbc.sa.vansales.activities;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gbc.sa.vansales.R;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.models.ArticleHeader;

import static gbc.sa.vansales.R.drawable.ic_begintrip;

/**
 * Created by Rakshit on 15-Nov-16.
 */
public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button btnBDay;
    TextView tv_dashboard;
    ImageView iv_drawer;
    DrawerLayout drawer;
    Button btn_message;


    private DrawerLayout mDrawerLayout;
    ExpanableListAdapterActivity mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        btnBDay=(Button)findViewById(R.id.btnBeginDay);
        tv_dashboard=(TextView)findViewById(R.id.tv_dashboard);
        tv_dashboard.setVisibility(View.VISIBLE);
        iv_drawer=(ImageView)findViewById(R.id.iv_drawer);
        btn_message=(Button)findViewById(R.id.btn_messages);
        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashboardActivity.this,CustomerMessageListActivity.class);
                intent.putExtra("from","dash");
                startActivity(intent);



            }
        });

        //Load all Articles
        ArticleHeaders.loadData(DashboardActivity.this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        prepareListData();
        mMenuAdapter = new ExpanableListAdapterActivity(this, listDataHeader, listDataChild, expandableList);

        // setting list adapter
        expandableList.setAdapter(mMenuAdapter);

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                String selectedItem=listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                if(selectedItem=="Load")
                {
                    Intent i=new Intent(DashboardActivity.this,LoadActivity.class);
                    startActivity(i);
                }
                else if(selectedItem=="Load Request")
                {
                    Intent i=new Intent(DashboardActivity.this,LoadRequestActivity.class);
                    startActivity(i);
                }

                else if(selectedItem=="VanStock")
                {
                    Intent i=new Intent(DashboardActivity.this,VanStockActivity.class);
                    startActivity(i);
                }

                else if(selectedItem=="Unload")
                {
                    Intent i=new Intent(DashboardActivity.this,UnloadActivity.class);
                    startActivity(i);
                }


                return false;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id)
            {


                Object item = parent.getExpandableListAdapter().getGroupId(groupPosition);
                String position = item.toString();
                if (position == "0") {
                    Intent i = new Intent(DashboardActivity.this, BeginTripActivity.class);
                    startActivity(i);
                } else if (position == "1") {
                    Intent i = new Intent(DashboardActivity.this, ManageInventory.class);
                    startActivity(i);
                } else if (position == "2") {
                    Intent i = new Intent(DashboardActivity.this, MyCalendarActivity.class);
                    startActivity(i);
                } else if (position == "3") {
                    Intent i = new Intent(DashboardActivity.this, EndTripActivity.class);
                    startActivity(i);
                } else if (position == "4") {
                    Toast.makeText(getApplicationContext(), "Information", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        iv_drawer.setVisibility(View.VISIBLE);
        iv_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        btnBDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DashboardActivity.this,BeginTripActivity.class);
                startActivity(i);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        createPieChart();
        createLineChart();
        TextView lbl_totalsales = (TextView)findViewById(R.id.lbl_totalsales);
        TextView lbl_totalreceipt = (TextView)findViewById(R.id.lbl_totalreceipt);
        TextView lbl_targetachieved = (TextView)findViewById(R.id.lbl_targetachieved);
        lbl_totalsales.setText("0.00 SAR");
        lbl_totalreceipt.setText("0.00 SAR");
        lbl_targetachieved.setText("594.00/75000.00");
    }

    void createPieChart(){
        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4, 0));
        entries.add(new Entry(8, 1));
        entries.add(new Entry(6, 2));


        PieDataSet dataset = new PieDataSet(entries, "");
        dataset.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int) value);
            }
        });

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Cash");
        labels.add("Credit");
        labels.add("TC");


        PieData data = new PieData(labels, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
      //  pieChart.setDescription("Description");
        pieChart.setDrawSliceText(false);
        pieChart.setData(data);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(50f);
        pieChart.setDescription("");
        pieChart.getLegend().setPosition(Legend.LegendPosition.PIECHART_CENTER);
       // pieChart.setUsePercentValues(true);
        pieChart.animateY(3000);

    }


    private void prepareListData() {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();

        ExpandedMenuModel beginTrip = new ExpandedMenuModel();
        beginTrip.setIconName("Begin Trip");
        beginTrip.setIconImg(R.drawable.ic_begintrip);
        // Adding data header
        listDataHeader.add(beginTrip);

        ExpandedMenuModel manageInventory = new ExpandedMenuModel();
        manageInventory.setIconName("Manage Inventory");
        manageInventory.setIconImg(R.drawable.ic_manageinventory);
        listDataHeader.add(manageInventory);

        ExpandedMenuModel customerOperations = new ExpandedMenuModel();
        customerOperations.setIconName("Customer Operations");
        customerOperations.setIconImg(R.drawable.ic_customeropt);
        listDataHeader.add(customerOperations);

        ExpandedMenuModel endTrip = new ExpandedMenuModel();
        endTrip.setIconName("End Trip");
        endTrip.setIconImg(R.drawable.ic_info);
        listDataHeader.add(endTrip);

        ExpandedMenuModel information = new ExpandedMenuModel();
        information.setIconName("Information");
        information.setIconImg(R.drawable.ic_info);
        listDataHeader.add(information);


        // Adding child data

        List<String> manageInventoryItems = new ArrayList<String>();
        manageInventoryItems.add("Load");
        manageInventoryItems.add("Load Request");
        manageInventoryItems.add("VanStock");
        manageInventoryItems.add("Unload");


        listDataChild.put(listDataHeader.get(1), manageInventoryItems);

    }



    void createLineChart(){
        LineChart lineChart = (LineChart) findViewById(R.id.lineChart);


        ArrayList<Entry> salesEntries = new ArrayList<>();
        salesEntries.add(new Entry(300f, 0));
        salesEntries.add(new Entry(853f, 1));
        salesEntries.add(new Entry(430f, 2));
        salesEntries.add(new Entry(1147f,3));
        salesEntries.add(new Entry(843f, 4));
        salesEntries.add(new Entry(888f, 5));
        salesEntries.add(new Entry(607f, 6));

        ArrayList<Entry> returnEntries = new ArrayList<>();
        returnEntries.add(new Entry(155f, 0));
        returnEntries.add(new Entry(33f, 1));
        returnEntries.add(new Entry(56f, 2));
        returnEntries.add(new Entry(23f, 3));
        returnEntries.add(new Entry(49f, 4));
        returnEntries.add(new Entry(52f, 5));
        returnEntries.add(new Entry(0f, 6));

        ArrayList<Entry> damageEntries = new ArrayList<>();
        damageEntries.add(new Entry(0f, 0));
        damageEntries.add(new Entry(0f, 1));
        damageEntries.add(new Entry(0f, 2));
        damageEntries.add(new Entry(0f, 3));
        damageEntries.add(new Entry(69f, 4));
        damageEntries.add(new Entry(0f, 5));
        damageEntries.add(new Entry(221f, 6));

        LineDataSet salesDataSet = new LineDataSet(salesEntries, "");
        LineDataSet returnDataSet = new LineDataSet(returnEntries, "");
        LineDataSet damageDataSet = new LineDataSet(damageEntries,"");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("18/11");
        labels.add("17/11");
        labels.add("16/11");
        labels.add("15/11");
        labels.add("14/11");
        labels.add("13/11");
        labels.add("12/11");

        LineData data = new LineData(labels, salesDataSet);
        data.addDataSet(returnDataSet);
        data.addDataSet(damageDataSet);


        salesDataSet.setColors(ColorTemplate.COLORFUL_COLORS); //
        salesDataSet.setDrawCubic(true);
        salesDataSet.setDrawFilled(true);

        returnDataSet.setColor(Color.GREEN);
        returnDataSet.setDrawCubic(true);

        damageDataSet.setColor(Color.BLACK);
        damageDataSet.setDrawCubic(true);

        lineChart.setDescription("");
        lineChart.setData(data);
     //   lineChart.setData(combinedData);
        lineChart.getLegend().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.animateY(2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        }
         super.onBackPressed();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        //revision: this don't works, use setOnChildClickListener() and setOnGroupClickListener() above instead
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return false;
    }
}
