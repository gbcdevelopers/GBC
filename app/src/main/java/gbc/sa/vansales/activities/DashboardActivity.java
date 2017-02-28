package gbc.sa.vansales.activities;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.views.MarqueeLayout;
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
    Button btn_settings;
    Button btn_logout;
    private DrawerLayout mDrawerLayout;
    ExpanableListAdapterActivity mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    float salesCount = 0;
    float goodReturnsCount = 0;
    float badReturnsCount = 0;
    int cashCustomerCount = 0;
    int creditCustomerCount = 0;
    int tcCustomerCount = 0;
    int postCount = 0;
    TextView lbl_totalsales;
    TextView tv_route;
    TextView tv_driver_no;
    TextView lbl_totalreceipt;
    TextView tv_tripid;
    TextView tv_driver_name;
    ArrayList<CustomerHeader> customers;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        try {
            CustomerHeaders.loadData(getApplicationContext());
            customers = CustomerHeaders.get();
            Helpers.logData(DashboardActivity.this, "On Dashboard Screen");
        /*ArticleHeaders.loadData(getApplicationContext());
        CustomerHeaders.loadData(getApplicationContext());*/
            //loadingSpinner = new LoadingSpinner(this, getString(R.string.changinglanguage));
            loadingSpinner = new LoadingSpinner(this, "");
            Helpers.loadData(getApplicationContext());
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            btnBDay = (Button) findViewById(R.id.btnBeginDay);
            tv_route = (TextView) findViewById(R.id.tv_route);
            tv_tripid = (TextView) findViewById(R.id.tv_trip_id);
            tv_driver_no = (TextView) findViewById(R.id.tv_driver_no);
            tv_dashboard = (TextView) findViewById(R.id.tv_dashboard);
            tv_dashboard.setVisibility(View.VISIBLE);
            lbl_totalsales = (TextView) findViewById(R.id.lbl_totalsales);
            lbl_totalreceipt = (TextView) findViewById(R.id.lbl_totalreceipt);
            iv_drawer = (ImageView) findViewById(R.id.iv_drawer);
            btn_message = (Button) findViewById(R.id.btn_messages);
            tv_route.setText(getString(R.string.route_code_101) + Settings.getString(App.ROUTE));
            try {
                String driverName = Settings.getString(App.LANGUAGE).equals("en") ? Settings.getString(App.DRIVER_NAME_EN) : Settings.getString(App.DRIVER_NAME_AR);
                tv_driver_no.setText(getString(R.string.welcome_john_doe_1000002445) + Settings.getString(App.DRIVER) + " , " + driverName);
                tv_tripid.setText(getString(R.string.trip) + Settings.getString(App.TRIP_ID));
            } catch (Exception e) {
                e.printStackTrace();
            }
            btn_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helpers.logData(DashboardActivity.this, "Clicked on message button on side menu");
                    Intent intent = new Intent(DashboardActivity.this, CustomerMessageListActivity.class);
                    intent.putExtra("from", "dash");
                    startActivity(intent);
                }
            });
            btn_settings = (Button) findViewById(R.id.btn_settings);
            btn_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helpers.logData(DashboardActivity.this, "Clicked on settings button on side menu");
                    mDrawerLayout.closeDrawers();
                    drawer.closeDrawers();
                    Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
            });
            btn_logout = (Button) findViewById(R.id.btn_logout);
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helpers.logData(DashboardActivity.this, "Clicked on log out button on side menu");
                    mDrawerLayout.closeDrawers();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                    alertDialogBuilder.setTitle(getString(R.string.log_out))
                            .setMessage(getString(R.string.log_out_msg))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.proceed), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            });
            //Load all Articles
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
                    String selectedItem = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                    if (selectedItem == "Load") {
                        Intent i = new Intent(DashboardActivity.this, LoadActivity.class);
                        startActivity(i);
                    } else if (selectedItem == "Load Request") {
                        Intent i = new Intent(DashboardActivity.this, LoadRequestActivity.class);
                        startActivity(i);
                    } else if (selectedItem == "VanStock") {
                        Intent i = new Intent(DashboardActivity.this, VanStockActivity.class);
                        startActivity(i);
                    } else if (selectedItem == "Unload") {
                        Intent i = new Intent(DashboardActivity.this, UnloadActivity.class);
                        startActivity(i);
                    }
                    return false;
                }
            });
            expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    Log.v("Group", "click");
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
            setBeginDayVisibility();
            btnBDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helpers.logData(DashboardActivity.this, "Clicked on Begin Day");
                    Helpers.loadData(getApplicationContext());
                    Intent i = new Intent(DashboardActivity.this, BeginTripActivity.class);
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
            //createPieChart();
            // createLineChart();
            //createBarChart();
            new loadBarChartData(App.SALES);
            TextView lbl_targetachieved = (TextView) findViewById(R.id.lbl_targetachieved);
            lbl_targetachieved.setText("0.00/0.00");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadTotalSales() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_ACTIVITY_TYPE, "");
            map.put(db.KEY_CUSTOMER_NO, "");
            map.put(db.KEY_ORDER_ID, "");
            map.put(db.KEY_PRICE, "");
            HashMap<String, String> filter = new HashMap<>();
            Cursor c = db.getData(db.DAYACTIVITY, map, filter);
            double totalSales = 0;
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    totalSales += Double.parseDouble(c.getString(c.getColumnIndex(db.KEY_PRICE)));
                }
                while (c.moveToNext());
            }
            lbl_totalsales.setText(String.valueOf(totalSales) + " " + getString(R.string.currency));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadTotalReceipt() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO, "");
            map.put(db.KEY_INVOICE_NO, "");
            map.put(db.KEY_INVOICE_AMOUNT, "");
            map.put(db.KEY_DUE_DATE, "");
            map.put(db.KEY_INVOICE_DATE, "");
            map.put(db.KEY_AMOUNT_CLEARED, "");
            map.put(db.KEY_IS_INVOICE_COMPLETE, "");
            HashMap<String, String> filter = new HashMap<>();
            double totalReceipt = 0;
            Cursor c = db.getData(db.COLLECTION, map, filter);
            if (c.getCount() > 0) {
                do {
                    totalReceipt += Double.parseDouble(c.getString(c.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                }
                while (c.moveToNext());
            }
            lbl_totalreceipt.setText(String.valueOf(totalReceipt) + " " + getString(R.string.currency));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setBeginDayVisibility() {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_IS_BEGIN_DAY, "true");
        if (db.checkData(db.LOCK_FLAGS, map)) {
            /*btnBDay.setEnabled(false);
            btnBDay.setAlpha(.5f);*/
            // btnBDay.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setBeginDayVisibility();
    }
    void createPieChart() {
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
        labels.add(getString(R.string.cash));
        labels.add(getString(R.string.credit));
        labels.add("TC");
        PieData data = new PieData(labels, dataset);
        List<Integer> colorCodes = new ArrayList<Integer>();
        colorCodes.add(Color.parseColor("#c15525"));
        colorCodes.add(Color.parseColor("#ffc502"));
        colorCodes.add(Color.parseColor("#ff9201"));
        dataset.setColors(colorCodes); //
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
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_IS_BEGIN_DAY, "");
            map.put(db.KEY_IS_LOAD_VERIFIED, "");
            map.put(db.KEY_IS_END_DAY, "");
            map.put(db.KEY_IS_UNLOAD, "");
            HashMap<String, String> filter = new HashMap<>();
            Cursor cursor = db.getData(db.LOCK_FLAGS, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
            }
            boolean isBeginTripEnabled = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(db.KEY_IS_BEGIN_DAY)));
            boolean isloadVerifiedEnabled = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(db.KEY_IS_LOAD_VERIFIED)));
            boolean isUnloadEnabled = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(db.KEY_IS_UNLOAD)));
            boolean isEndDayEnabled = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(db.KEY_IS_END_DAY)));
            listDataHeader = new ArrayList<ExpandedMenuModel>();
            listDataChild = new HashMap<ExpandedMenuModel, List<String>>();
            ExpandedMenuModel beginTrip = new ExpandedMenuModel();
            beginTrip.setIconName(getString(R.string.begintrip));
            beginTrip.setIconImg(R.drawable.ic_begintrip);
            beginTrip.setIsEnabled(isEndDayEnabled ? false : true);
            listDataHeader.add(beginTrip);
            ExpandedMenuModel manageInventory = new ExpandedMenuModel();
            manageInventory.setIconName(getString(R.string.manageinventory));
            manageInventory.setIconImg(R.drawable.ic_manageinventory);
            manageInventory.setIsEnabled(isBeginTripEnabled && !isEndDayEnabled ? true : false);
            //manageInventory.setIsEnabled(true);
            listDataHeader.add(manageInventory);
            ExpandedMenuModel customerOperations = new ExpandedMenuModel();
            customerOperations.setIconName(getString(R.string.customeroperation));
            customerOperations.setIconImg(R.drawable.ic_customeropt);
            customerOperations.setIsEnabled(isloadVerifiedEnabled && !isEndDayEnabled ? true : false);
            // customerOperations.setIsEnabled(true);
            listDataHeader.add(customerOperations);
            ExpandedMenuModel endTrip = new ExpandedMenuModel();
            endTrip.setIconName(getString(R.string.endtrip));
            endTrip.setIconImg(R.drawable.ic_info);
            endTrip.setIsEnabled(isUnloadEnabled && !isEndDayEnabled ? true : false);
            //endTrip.setIsEnabled(true);
            listDataHeader.add(endTrip);
            ExpandedMenuModel driverbalance = new ExpandedMenuModel();
            driverbalance.setIconName(getString(R.string.driver_collection));
            driverbalance.setIconImg(R.drawable.ic_driver_collection);
            //driverbalance.setIsEnabled(true);
            driverbalance.setIsEnabled(isEndDayEnabled ? false : true);
            listDataHeader.add(driverbalance);
            ExpandedMenuModel information = new ExpandedMenuModel();
            information.setIconName(getString(R.string.information));
            information.setIconImg(R.drawable.ic_info);
            information.setIsEnabled(true);
            listDataHeader.add(information);
            // Adding child data
            List<String> manageInventoryItems = new ArrayList<String>();
            manageInventoryItems.add(getString(R.string.load));
            manageInventoryItems.add(getString(R.string.loadrequest));
            manageInventoryItems.add(getString(R.string.vanstock));
            manageInventoryItems.add(getString(R.string.unload));
            listDataChild.put(listDataHeader.get(1), manageInventoryItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*void createLineChart() {
        LineChart lineChart = (LineChart) findViewById(R.id.lineChart);
        ArrayList<Entry> salesEntries = new ArrayList<>();
        salesEntries.add(new Entry(300f, 0));
        salesEntries.add(new Entry(853f, 1));
        salesEntries.add(new Entry(430f, 2));
        salesEntries.add(new Entry(1147f, 3));
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
        LineDataSet damageDataSet = new LineDataSet(damageEntries, "");
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
    }*/
    void createBarChart() {
        BarChart barChart = (BarChart) findViewById(R.id.barChart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        BarDataSet dataset = new BarDataSet(entries, "");
        // dataset.setColors(ColorTemplate.PASTEL_COLORS);
        List<Integer> colorCodes = new ArrayList<Integer>();
        colorCodes.add(Color.parseColor("#82d173"));
        colorCodes.add(Color.parseColor("#3e7aae"));
        colorCodes.add(Color.parseColor("#ff715b"));
        dataset.setColors(colorCodes);
        ArrayList<String> labels = new ArrayList<String>();
        labels.add(getString(R.string.sales));
        labels.add(getString(R.string.good_return));
        labels.add(getString(R.string.bad_return));
        BarData data = new BarData(labels, dataset);
        barChart.setData(data);
        barChart.animateY(2000);
        barChart.setDescription("");
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    }
    void createBarChartFromLiveData(float salesCount, float goodreturnsCount, float badreturnsCount) {
        try {
            Log.e("Count in Live Data", "" + salesCount + "/" + goodreturnsCount + "/" + badreturnsCount);
            BarChart barChart = (BarChart) findViewById(R.id.barChart);
            barChart.setDrawBarShadow(false);
            barChart.setDrawValueAboveBar(true);
            barChart.setPinchZoom(false);
            barChart.setDrawGridBackground(false);
            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(salesCount, 0));
            entries.add(new BarEntry(goodreturnsCount, 1));
            entries.add(new BarEntry(badreturnsCount, 2));
        /*entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));*/
            BarDataSet dataset = new BarDataSet(entries, "");
            // dataset.setColors(ColorTemplate.PASTEL_COLORS);
            List<Integer> colorCodes = new ArrayList<Integer>();
            colorCodes.add(Color.parseColor("#82d173"));
            colorCodes.add(Color.parseColor("#3e7aae"));
            colorCodes.add(Color.parseColor("#ff715b"));
            dataset.setColors(colorCodes);
            ArrayList<String> labels = new ArrayList<String>();
            labels.add(getString(R.string.sales));
            labels.add(getString(R.string.good_return));
            labels.add(getString(R.string.bad_return));
            BarData data = new BarData(labels, dataset);
            barChart.setData(data);
            barChart.animateY(2000);
            barChart.setDescription("");
            barChart.getAxisRight().setEnabled(false);
            barChart.getLegend().setEnabled(false);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void createPieChartFromLiveData(int cashCustomerCount, int creditCustomerCount, int tcCustomerCount) {
        try {
            if (cashCustomerCount == 0 && creditCustomerCount == 0 && tcCustomerCount == 0) {
                PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
                ArrayList<Entry> entries = new ArrayList<>();
                entries.add(new Entry(1, 0));
                PieDataSet dataset = new PieDataSet(entries, "");
                dataset.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return String.valueOf((int) value);
                    }
                });
                ArrayList<String> labels = new ArrayList<String>();
                labels.add(getString(R.string.no_visit));
                // labels.add(getString(R.string.credit));
                //  labels.add(getString(R.string.tc_lbl));
                PieData data = new PieData(labels, dataset);
                List<Integer> colorCodes = new ArrayList<Integer>();
                colorCodes.add(Color.parseColor("#06A899"));
                // colorCodes.add(Color.parseColor("#ffc502"));
                //  colorCodes.add(Color.parseColor("#ff9201"));
                dataset.setColors(colorCodes); //
                //  pieChart.setDescription("Description");
                pieChart.setDrawSliceText(false);
                pieChart.setData(data);
                pieChart.setHoleRadius(50f);
                pieChart.setTransparentCircleRadius(50f);
                pieChart.setDescription("");
                pieChart.getLegend().setPosition(Legend.LegendPosition.PIECHART_CENTER);
                pieChart.setDrawSliceText(false);
                pieChart.setDrawCenterText(false);
                pieChart.setCenterTextColor(Color.TRANSPARENT);
                // pieChart.setUsePercentValues(true);
                pieChart.animateY(1000);
            } else {
                PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
                ArrayList<Entry> entries = new ArrayList<>();
                entries.add(new Entry(cashCustomerCount, 0));
                entries.add(new Entry(creditCustomerCount, 1));
                entries.add(new Entry(tcCustomerCount, 2));
                PieDataSet dataset = new PieDataSet(entries, "");
                dataset.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return String.valueOf((int) value);
                    }
                });
                ArrayList<String> labels = new ArrayList<String>();
                labels.add(getString(R.string.cash));
                labels.add(getString(R.string.credit));
                labels.add(getString(R.string.tc_lbl));
                PieData data = new PieData(labels, dataset);
                List<Integer> colorCodes = new ArrayList<Integer>();
                colorCodes.add(Color.parseColor("#c15525"));
                colorCodes.add(Color.parseColor("#ffc502"));
                colorCodes.add(Color.parseColor("#ff9201"));
                dataset.setColors(colorCodes); //
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    private void createBarChartData(final String var) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (var.equals(App.SALES)) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(db.KEY_TIME_STAMP, "");
                        map.put(db.KEY_ORG_CASE, "");
                        HashMap<String, String> filterPostedSales = new HashMap<>();
                        filterPostedSales.put(db.KEY_IS_POSTED, App.DATA_IS_POSTED); //Count of all invoices posted in SAP
                        HashMap<String, String> filterMarkedforPostSales = new HashMap<>();
                        filterMarkedforPostSales.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                        Cursor salesMarkforPostCursor = null;
                        Cursor salesPostedCursor = null;
                        if (db.checkData(db.CAPTURE_SALES_INVOICE, filterMarkedforPostSales)) {
                            salesMarkforPostCursor = db.getData(db.CAPTURE_SALES_INVOICE, map, filterMarkedforPostSales);
                            if (salesMarkforPostCursor.getCount() > 0) {
                                salesMarkforPostCursor.moveToFirst();
                            }
                        }
                        if (db.checkData(db.CAPTURE_SALES_INVOICE, filterPostedSales)) {
                            salesPostedCursor = db.getData(db.CAPTURE_SALES_INVOICE, map, filterPostedSales);
                            if (salesPostedCursor.getCount() > 0) {
                                salesPostedCursor.moveToFirst();
                            }
                        }
                        salesCount = calculateData(var, salesPostedCursor, salesMarkforPostCursor);
                    } else if (var.equals(App.GOOD_RETURN)) {
                        HashMap<String, String> returnMap = new HashMap<>();
                        returnMap.put(db.KEY_TIME_STAMP, "");
                        returnMap.put(db.KEY_CASE, "");
                        HashMap<String, String> filterPostedGoodReturn = new HashMap<>();
                        filterPostedGoodReturn.put(db.KEY_IS_POSTED, App.DATA_IS_POSTED);//Count of all good REturns posted in SAP
                        filterPostedGoodReturn.put(db.KEY_REASON_TYPE, App.GOOD_RETURN);
                        HashMap<String, String> filterMarkedforPostGoodReturn = new HashMap<>();
                        filterMarkedforPostGoodReturn.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                        filterMarkedforPostGoodReturn.put(db.KEY_REASON_TYPE, App.GOOD_RETURN);
                        Cursor goodReturnPostedCursor = null;
                        if (db.checkData(db.RETURNS, filterPostedGoodReturn)) {
                            goodReturnPostedCursor = db.getData(db.RETURNS, returnMap, filterPostedGoodReturn);
                        }
                        Cursor goodReturnMarkforPostCursor = null;
                        if (db.checkData(db.RETURNS, filterMarkedforPostGoodReturn)) {
                            goodReturnMarkforPostCursor = db.getData(db.RETURNS, returnMap, filterMarkedforPostGoodReturn);
                        }
                        goodReturnsCount = calculateData(var, goodReturnPostedCursor, goodReturnMarkforPostCursor);
                    } else if (var.equals(App.BAD_RETURN)) {
                        HashMap<String, String> returnMap = new HashMap<>();
                        returnMap.put(db.KEY_TIME_STAMP, "");
                        returnMap.put(db.KEY_CASE, "");
                        HashMap<String, String> filterPostedBadReturn = new HashMap<>();
                        filterPostedBadReturn.put(db.KEY_IS_POSTED, App.DATA_IS_POSTED);//Count of all good REturns posted in SAP
                        filterPostedBadReturn.put(db.KEY_REASON_TYPE, App.BAD_RETURN);
                        HashMap<String, String> filterMarkedforPostBadReturn = new HashMap<>();
                        filterMarkedforPostBadReturn.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                        filterMarkedforPostBadReturn.put(db.KEY_REASON_TYPE, App.BAD_RETURN);
                        Cursor badReturnPostedCursor = null;
                        Cursor badReturnMarkforPostCursor = null;
                        if (db.checkData(db.RETURNS, filterPostedBadReturn)) {
                            badReturnPostedCursor = db.getData(db.RETURNS, returnMap, filterPostedBadReturn);
                        }
                        if (db.checkData(db.RETURNS, filterMarkedforPostBadReturn)) {
                            badReturnMarkforPostCursor = db.getData(db.RETURNS, returnMap, filterMarkedforPostBadReturn);
                        }
                        badReturnsCount = calculateData(var, badReturnPostedCursor, badReturnMarkforPostCursor);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public class loadBarChartData extends AsyncTask<Void, Void, Void> {
        private String var;
        private loadBarChartData(String var) {
            this.var = var;
            execute();
        }
        @Override
        protected void onPreExecute() {
            if (!loadingSpinner.isShowing()) {
                loadingSpinner.show();
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
            createBarChartData(var);
            /*if(var.equals(App.SALES)){
                HashMap<String, String>map = new HashMap<>();
                map.put(db.KEY_TIME_STAMP,"");
                map.put(db.KEY_ORG_CASE,"");
                HashMap<String,String>filterPostedSales = new HashMap<>();
                filterPostedSales.put(db.KEY_IS_POSTED,App.DATA_IS_POSTED); //Count of all invoices posted in SAP
                HashMap<String,String>filterMarkedforPostSales = new HashMap<>();
                filterMarkedforPostSales.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                Cursor salesPostedCursor = null;
                *//*if(db.checkData(db.CAPTURE_SALES_INVOICE,filterPostedSales)){
                    salesPostedCursor  = db.getData(db.CAPTURE_SALES_INVOICE,map,filterPostedSales);
                }*//*
                Cursor salesMarkforPostCursor = null;
                if(db.checkData(db.CAPTURE_SALES_INVOICE,filterMarkedforPostSales)){
                    salesMarkforPostCursor = db.getData(db.CAPTURE_SALES_INVOICE,map,filterMarkedforPostSales);
                }
                salesCount = calculateData(var,salesPostedCursor,salesMarkforPostCursor);
            }*/
            // badReturnsCount = GoodReturnPostedCursor.getCount() + GoodReturnMarkforPostCursor.getCount();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            postCount++;
            if (var.equals(App.SALES)) {
                new loadBarChartData(App.GOOD_RETURN);
            } else if (var.equals(App.GOOD_RETURN)) {
                new loadBarChartData(App.BAD_RETURN);
            } else {
                if (postCount == 3) {
                    if (loadingSpinner.isShowing()) {
                        loadingSpinner.hide();
                    }
                    createBarChartFromLiveData(salesCount, goodReturnsCount, badReturnsCount);
                    new loadPieChartData().execute();
                }
            }
        }
    }
    public class loadPieChartData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            if (!loadingSpinner.isShowing()) {
                loadingSpinner.show();
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(db.KEY_END_TIMESTAMP, "");
            map.put(db.KEY_CUSTOMER_NO, "");
            HashMap<String, String> filter = new HashMap<>();
            // filter.put(db.KEY_IS_VISITED, App.IS_COMPLETE);
            Cursor c = db.getData(db.VISIT_LIST_POST, map, filter);
            if (c.getCount() > 0) {
                c.moveToFirst();
                createPieChart(c);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (loadingSpinner.isShowing()) {
                loadingSpinner.hide();
            }
            loadTotalSales();
            createPieChartFromLiveData(cashCustomerCount, creditCustomerCount, tcCustomerCount);
            loadTotalReceipt();
        }
    }
    private void createPieChart(final Cursor c) {
        c.moveToFirst();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<String> tempCust = new ArrayList<String>();
                    do {
                        CustomerHeader customerHeader = CustomerHeader.getCustomer(customers, c.getString(c.getColumnIndex(db.KEY_CUSTOMER_NO)));
                        HashMap<String, String> filter = new HashMap<String, String>();
                        filter.put(db.KEY_CUSTOMER_NO, c.getString(c.getColumnIndex(db.KEY_CUSTOMER_NO)));
                        if (!tempCust.contains(c.getString(c.getColumnIndex(db.KEY_CUSTOMER_NO)))) {
                            tempCust.add(c.getString(c.getColumnIndex(db.KEY_CUSTOMER_NO)));
                            if (customerHeader != null) {
                                if (customerHeader.getTerms().equals(App.CASH_CUSTOMER_CODE)) {
                                    cashCustomerCount++;
                                } else if (customerHeader.getTerms().equals(App.TC_CUSTOMER_CODE)) {
                                    tcCustomerCount++;
                                } else if (!customerHeader.getTerms().equals("")/*&&db.checkData(db.CUSTOMER_CREDIT,filter)*/) {
                                    creditCustomerCount++;
                                } else {
                                    creditCustomerCount++;
                                }
                            }
                        }
                    }
                    while (c.moveToNext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private float calculateData(String var, Cursor c1, Cursor c2) {
        try {
            float count = 0;
            if (var.equals(App.SALES)) {
                if (c1 != null) {
                    if (c1.getCount() > 0) {
                        do {
                            count += Float.parseFloat(c1.getString(c1.getColumnIndex(db.KEY_ORG_CASE)));
                        }
                        while (c1.moveToNext());
                    }
                }
                if (c2 != null) {
                    if (c2.getCount() > 0) {
                        do {
                            count += Float.parseFloat(c2.getString(c2.getColumnIndex(db.KEY_ORG_CASE)));
                        }
                        while (c2.moveToNext());
                    }
                }
            } else if (var.equals(App.GOOD_RETURN)) {
                if (c1 != null) {
                    if (c1.getCount() > 0) {
                        do {
                            count += Float.parseFloat(c1.getString(c1.getColumnIndex(db.KEY_CASE)));
                        }
                        while (c1.moveToNext());
                    }
                }
                if (c2 != null) {
                    if (c2.getCount() > 0) {
                        do {
                            count += Float.parseFloat(c2.getString(c2.getColumnIndex(db.KEY_CASE)));
                        }
                        while (c2.moveToNext());
                    }
                }
            } else if (var.equals(App.BAD_RETURN)) {
                if (c1 != null) {
                    if (c1.getCount() > 0) {
                        do {
                            count += Float.parseFloat(c1.getString(c1.getColumnIndex(db.KEY_CASE)));
                        }
                        while (c1.moveToNext());
                    }
                }
                if (c2 != null) {
                    if (c2.getCount() > 0) {
                        do {
                            count += Float.parseFloat(c2.getString(c2.getColumnIndex(db.KEY_CASE)));
                        }
                        while (c2.moveToNext());
                    }
                }
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
