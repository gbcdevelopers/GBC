package gbc.sa.vansales.activities;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerOperationAdapter;
import gbc.sa.vansales.adapters.PrintDocumentAdapter;
import gbc.sa.vansales.google.Location;
import gbc.sa.vansales.utils.Callback;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.PrinterHelper;
import gbc.sa.vansales.utils.Settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
public class InformationsActivity extends AppCompatActivity {
    GridView gridView;
    CustomerOperationAdapter adapter;
    String strText[] = {};/*{"CUSTOMER LIST", "ITEM LIST", "TARGET/GOALS",
            "ANALYSIS", "TODAY SUMMARY", "REVIEW", "PRINT \n TRANSACTIONS", "PRINT REPORTS"};*/
    int resarr[] = {R.drawable.info_customer_list, R.drawable.info_item_list, R.drawable.info_target,
            R.drawable.info_analysis, R.drawable.info_todays_summery, R.drawable.info_review,
            R.drawable.info_print_transaction, R.drawable.info_print_reports};
    ImageView iv_back;
    TextView tv_top_header;
    View view1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations);
        strText = new String[]{getString(R.string.customer_list), getString(R.string.item_list), getString(R.string.targetgoal),
                getString(R.string.analysis), getString(R.string.today_summary), getString(R.string.review),
                getString(R.string.printtransactions), getString(R.string.printreports)};
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        gridView = (GridView) findViewById(R.id.grid_information);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.information));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformationsActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        adapter = new CustomerOperationAdapter(InformationsActivity.this, strText, resarr, "InformationsActivity");
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view1 = view;
                switch (position) {
                    case 0:
                        Intent customerlist = new Intent(InformationsActivity.this, CustomerListActivity.class);
                        startActivity(customerlist);
                        break;
                    case 1:
                        PrinterHelper object = new PrinterHelper(InformationsActivity.this,InformationsActivity.this);
                        object.execute("",createDataforVanStock()); //For Sales Invoice
                        //object.execute("",createDataForPrint()); //For Load Summary
                        //  object.execute("",createDataForLoadRequest());
                        /*Intent itemlist = new Intent(InformationsActivity.this, ItemListActivity.class);
                        startActivity(itemlist);*/
                        break;
                    case 2:
                        Intent intent7 = new Intent(InformationsActivity.this, ItemComplaints.class);
                        startActivity(intent7);
                        new Location(InformationsActivity.this, new Callback() {
                            @Override
                            public void callbackSuccess(android.location.Location location) {
                                Log.e("COORDUNATES",String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));
                                Toast.makeText(getBaseContext(),String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()),Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void callbackFailure() {
                                Toast.makeText(getBaseContext(), "Failure", Toast.LENGTH_SHORT).show();
                            }
                        });

                        break;
                    case 3:
                        break;
                    case 4:
                        Intent todays = new Intent(InformationsActivity.this, TodaysSummaryActivity.class);
                        startActivity(todays);
                        break;
                    case 5:
                        Intent review = new Intent(InformationsActivity.this, ReviewActivity.class);
                        startActivity(review);
                        break;
                    case 6:
                        Intent printdoc = new Intent(InformationsActivity.this, PrintDocumentActivity.class);
                        startActivity(printdoc);
                        break;
                    case 7:
                        Intent print = new Intent(InformationsActivity.this, PrinterReportsActivity.class);
                        print.putExtra("from", "info");
                        startActivity(print);
                        break;
                    default:
                        break;
                }
            }
        });
    }
    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }*/
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }
    //User This sample for Load Summary
    public JSONArray createDataForPrint() {
        JSONArray jArr = new JSONArray();
        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.LOAD_SUMMARY_REQUEST);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("TIME", Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO", "1234");
            mainArr.put("DOCUMENT NO", "80001234");  //Load Summary No
            mainArr.put("TRIP START DATE", Helpers.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("supervisorname", "-");
            mainArr.put("TourID", Settings.getString(App.TRIP_ID));
            mainArr.put("Load Number", "1");
            JSONArray HEADERS = new JSONArray();
            /*obj.put("Item#","14000000");
            obj.put("Description","Carton 48*48 200ML");
            obj.put("UPO","1");
            obj.put("Open Qty","0");
            obj.put("Load Qty","100");
            obj.put("Adjust Qty","0");
            obj.put("Net Qty","100");
            obj.put("VALUE","1200");
            obj.put("Description","Carton 48*48 100ML");*/

            /*JSONObject obj1 = new JSONObject();
            obj1.put("Sl#","0010");
            obj1.put("Item#","14000000");
            obj1.put("Description","Carton 48*48 200ML");
            obj1.put("UPO","1");
            obj1.put("Open Qty","0");
            obj1.put("Load Qty","100");
            obj1.put("Adjust Qty","0");
            obj1.put("Net Qty","100");
            obj1.put("VALUE","1200");
            obj1.put("Description","Carton 48*48 100ML");*/

            /*JSONObject obj2 = new JSONObject();
            obj2.put("Sl#","0010");
            obj2.put("Item#","14000000");
            obj2.put("Description","Carton 48*48 200ML");
            obj2.put("UPO","1");
            obj2.put("Open Qty","0");
            obj2.put("Load Qty","100");
            obj2.put("Adjust Qty","0");
            obj2.put("Net Qty","100");
            obj2.put("VALUE","1200");
            obj2.put("Description","Carton 48*48 100ML");*/
            HEADERS.put("ITEM#");
            HEADERS.put("ENGLISH DESCRIPTION");
            HEADERS.put("ARABIC DESCRIPTION");
            HEADERS.put("UPC ");
            HEADERS.put("BEGIN INV");
            HEADERS.put("LOAD");
            HEADERS.put("ADJUST");
            HEADERS.put("NET LOAD");
            HEADERS.put("VALUE");
            //HEADERS.put("Description");
            //HEADERS.put(obj1);
            // HEADERS.put(obj2);
            mainArr.put("HEADERS", HEADERS);
            JSONArray jData1 = new JSONArray();
            jData1.put("14020106");
            jData1.put("Carton 48*200ml Berain Krones");
            jData1.put("شد 48*200مل بيرين PH8");
            jData1.put("1");
            jData1.put("+0");
            jData1.put("+100");
            jData1.put("+0");
            jData1.put("+100");
            jData1.put("+1200.00");
            JSONArray jData2 = new JSONArray();
            jData2.put("14020107");
            jData2.put("Carton 30*330ml Berain Krones");
            jData2.put("شد 30*330مل بيرين PH8");
            jData2.put("1");
            jData2.put("+0");
            jData2.put("+100");
            jData2.put("+0");
            jData2.put("+100");
            jData2.put("+1200.00");
            JSONArray jData3 = new JSONArray();
            jData3.put("14020123");
            jData3.put("Carton 24*600 Berain PH8 Krones");
            jData3.put("شد 24*600مل بيرين PH8");
            jData3.put("1");
            jData3.put("+0");
            jData3.put("+150");
            jData3.put("+0");
            jData3.put("+150");
            jData3.put("+2200.00");
            JSONArray jData4 = new JSONArray();
            jData4.put("14020124");
            jData4.put("Carton 40*330 Berain PH8 Krones");
            jData4.put("شد 40*330مل بيرين PH8");
            jData4.put("1");
            jData4.put("+0");
            jData4.put("+200");
            jData4.put("+0");
            jData4.put("+200");
            jData4.put("+2250.00");
            JSONArray jData5 = new JSONArray();
            jData5.put("14020125");
            jData5.put("Carton 12*1.5ltr Berain PH8 Krones");
            jData5.put("شد 12*1.5 لتر بيرين PH8");
            jData5.put("1");
            jData5.put("+0");
            jData5.put("+200");
            jData5.put("+0");
            jData5.put("+200");
            jData5.put("+2250.00");
            JSONArray jData6 = new JSONArray();
            jData6.put("14020126");
            jData6.put("Carton 24*500 ml Berain PH8 Krones");
            jData6.put("شد 24*500مل بيرين PH8");
            jData6.put("1");
            jData6.put("+0");
            jData6.put("+200");
            jData6.put("+0");
            jData6.put("+200");
            jData6.put("+2250.00");
            JSONArray jData = new JSONArray();
            jData.put(jData1);
            jData.put(jData2);
            jData.put(jData3);
            jData.put(jData4);
            jData.put(jData5);
            jData.put(jData6);
            mainArr.put("data", jData);
            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);
            jArr.put(HEADERS);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return jArr;
    }
    //Use this for load Request and Order Request
    public JSONArray createDataForLoadRequest() {
        JSONArray jArr = new JSONArray();
        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.LOAD_REQUEST);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), App.PRINT_DATE_FORMAT));
            mainArr.put("TIME", Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO", "1234");
            mainArr.put("DOCUMENT NO", "80001234");  //Load Summary No
            mainArr.put("TRIP START DATE", Helpers.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("supervisorname", "-");
            mainArr.put("TourID", Settings.getString(App.TRIP_ID));
            //mainArr.put("Load Number","1");
            JSONArray HEADERS = new JSONArray();
            JSONArray TOTAL = new JSONArray();
            /*obj.put("Item#","14000000");
            obj.put("Description","Carton 48*48 200ML");
            obj.put("UPO","1");
            obj.put("Open Qty","0");
            obj.put("Load Qty","100");
            obj.put("Adjust Qty","0");
            obj.put("Net Qty","100");
            obj.put("VALUE","1200");
            obj.put("Description","Carton 48*48 100ML");*/

            /*JSONObject obj1 = new JSONObject();
            obj1.put("Sl#","0010");
            obj1.put("Item#","14000000");
            obj1.put("Description","Carton 48*48 200ML");
            obj1.put("UPO","1");
            obj1.put("Open Qty","0");
            obj1.put("Load Qty","100");
            obj1.put("Adjust Qty","0");
            obj1.put("Net Qty","100");
            obj1.put("VALUE","1200");
            obj1.put("Description","Carton 48*48 100ML");*/

            /*JSONObject obj2 = new JSONObject();
            obj2.put("Sl#","0010");
            obj2.put("Item#","14000000");
            obj2.put("Description","Carton 48*48 200ML");
            obj2.put("UPO","1");
            obj2.put("Open Qty","0");
            obj2.put("Load Qty","100");
            obj2.put("Adjust Qty","0");
            obj2.put("Net Qty","100");
            obj2.put("VALUE","1200");
            obj2.put("Description","Carton 48*48 100ML");*/
            HEADERS.put("ITEM NO");
            HEADERS.put("ENGLISH DESCRIPTION");
            HEADERS.put("ARABIC DESCRIPTION");
            HEADERS.put("UPC ");
            HEADERS.put("TOTAL UNITS");
            HEADERS.put("UNIT PRICE");
            HEADERS.put("AMOUNT");
            //HEADERS.put("Description");
            //HEADERS.put(obj1);
            // HEADERS.put(obj2);
            mainArr.put("HEADERS", HEADERS);
            JSONObject totalObj = new JSONObject();
            totalObj.put("TOTAL UNITS", "+25");
            totalObj.put("UNIT PRICE", "");
            totalObj.put("AMOUNT", "+2230");
            TOTAL.put(totalObj);
            mainArr.put("TOTAL", TOTAL);
            JSONArray jData1 = new JSONArray();
            jData1.put("14020106");
            jData1.put("Carton 48*200ml Berain Krones");
            jData1.put("شد 48*200مل بيرين PH8");
            jData1.put("1");
            jData1.put("+15");
            jData1.put("+10");
            jData1.put("+150");
            JSONArray jData2 = new JSONArray();
            jData2.put("14020107");
            jData2.put("Carton 30*330ml Berain Krones");
            jData2.put("شد 30*330مل بيرين PH8");
            jData2.put("1");
            jData2.put("+15");
            jData2.put("+12");
            jData2.put("+1200");
            JSONArray jData3 = new JSONArray();
            jData3.put("14020123");
            jData3.put("Carton 24*600 Berain PH8 Krones");
            jData3.put("شد 24*600مل بيرين PH8");
            jData3.put("1");
            jData3.put("+10");
            jData3.put("+150");
            jData3.put("+1500");
            JSONArray jData = new JSONArray();
            jData.put(jData1);
            jData.put(jData2);
            jData.put(jData3);
            /*jData.put(jData4);
            jData.put(jData5);
            jData.put(jData6);*/
            mainArr.put("data", jData);
            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);
            jArr.put(HEADERS);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return jArr;
    }
    public JSONArray createDataforVanStock() {
        JSONArray jArr = new JSONArray();
        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.VAN_STOCK);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), App.PRINT_DATE_FORMAT));
            mainArr.put("TIME", Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER_NAME_EN));
            mainArr.put("SALESMANNO",Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO", "1234");
            mainArr.put("DOCUMENT NO", "80001234");  //Load Summary No
            mainArr.put("TRIP START DATE", Helpers.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("supervisorname", "-");
            mainArr.put("TourID", Settings.getString(App.TRIP_ID));
            mainArr.put("closevalue","100");
            //mainArr.put("Load Number","1");
            JSONArray HEADERS = new JSONArray();
            JSONArray TOTAL = new JSONArray();
            /*obj.put("Item#","14000000");
            obj.put("Description","Carton 48*48 200ML");
            obj.put("UPO","1");
            obj.put("Open Qty","0");
            obj.put("Load Qty","100");
            obj.put("Adjust Qty","0");
            obj.put("Net Qty","100");
            obj.put("VALUE","1200");
            obj.put("Description","Carton 48*48 100ML");*/

            /*JSONObject obj1 = new JSONObject();
            obj1.put("Sl#","0010");
            obj1.put("Item#","14000000");
            obj1.put("Description","Carton 48*48 200ML");
            obj1.put("UPO","1");
            obj1.put("Open Qty","0");
            obj1.put("Load Qty","100");
            obj1.put("Adjust Qty","0");
            obj1.put("Net Qty","100");
            obj1.put("VALUE","1200");
            obj1.put("Description","Carton 48*48 100ML");*/

            /*JSONObject obj2 = new JSONObject();
            obj2.put("Sl#","0010");
            obj2.put("Item#","14000000");
            obj2.put("Description","Carton 48*48 200ML");
            obj2.put("UPO","1");
            obj2.put("Open Qty","0");
            obj2.put("Load Qty","100");
            obj2.put("Adjust Qty","0");
            obj2.put("Net Qty","100");
            obj2.put("VALUE","1200");
            obj2.put("Description","Carton 48*48 100ML");*/
            HEADERS.put("ITEM#");
            HEADERS.put("DESCRIPTION");
            HEADERS.put("LOADED QTY");
            HEADERS.put("SALE QTY");
            HEADERS.put("TRUCK STOCK");
            HEADERS.put("TOTAL");
            //HEADERS.put("Description");
            //HEADERS.put(obj1);
            // HEADERS.put(obj2);
            mainArr.put("HEADERS", HEADERS);
            JSONObject totalObj = new JSONObject();
            totalObj.put("LOADED QTY", "+500");
            totalObj.put("SALE QTY", "-300");
            totalObj.put("TRUCK STOCK", "+200");
            TOTAL.put(totalObj);
            mainArr.put("TOTAL", TOTAL);
            JSONArray jData1 = new JSONArray();
            jData1.put("14020106");
            jData1.put("Carton 48*200ml Berain Krones");
            //jData1.put("شد 48*200مل بيرين PH8");
            //jData1.put("1");
            jData1.put("+15");
            jData1.put("+10");
            jData1.put("+150");
            JSONArray jData2 = new JSONArray();
            jData2.put("14020107");
            jData2.put("Carton 30*330ml Berain Krones");
            /*jData2.put("شد 30*330مل بيرين PH8");
            jData2.put("1");*/
            jData2.put("+15");
            jData2.put("+12");
            jData2.put("+1200");
            JSONArray jData3 = new JSONArray();
            jData3.put("14020123");
            jData3.put("Carton 24*600 Berain PH8 Krones");
            /*jData3.put("شد 24*600مل بيرين PH8");
            jData3.put("1");*/
            jData3.put("+10");
            jData3.put("+150");
            jData3.put("+1500");
            JSONArray jData = new JSONArray();
            jData.put(jData1);
            jData.put(jData2);
            jData.put(jData3);
            /*jData.put(jData4);
            jData.put(jData5);
            jData.put(jData6);*/
            mainArr.put("data", jData);
            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);
            jArr.put(HEADERS);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return jArr;
    }
    //Sales Invoice
    public JSONArray createDataforSI() {
        JSONArray jArr = new JSONArray();
        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.SALES_INVOICE);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), App.PRINT_DATE_FORMAT));
            mainArr.put("TIME", Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO", "1234");
            mainArr.put("DOCUMENT NO", "80001234");  //Load Summary No
            mainArr.put("ORDERNO", "80001234");  //Load Summary No
            mainArr.put("TRIP START DATE", Helpers.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("supervisorname", "-");
            mainArr.put("TripID", Settings.getString(App.TRIP_ID));
            mainArr.put("invheadermsg", "HAPPY NEW YEAR");
            mainArr.put("LANG", "en");
            mainArr.put("invoicepaymentterms", "2");
            mainArr.put("invoicenumber", "1300000001");
            mainArr.put("INVOICETYPE", "SALES INVOICE");
            String arabicCustomer = "اللولو هايبر ماركت";
            mainArr.put("CUSTOMER", "LULU HYPER MARKET" + "-" + arabicCustomer);
            mainArr.put("ADDRESS", "3101, 21st Street, Riyadh");
            mainArr.put("ARBADDRESS", "");
            mainArr.put("displayupc", "0");
            mainArr.put("invoicepriceprint", "1");
            mainArr.put("SUB TOTAL", "1000");
            mainArr.put("INVOICE DISCOUNT", "20");
            mainArr.put("NET SALES", "980");
            //mainArr.put("Load Number","1");
            JSONArray HEADERS = new JSONArray();
            JSONArray TOTAL = new JSONArray();
            /*obj.put("Item#","14000000");
            obj.put("Description","Carton 48*48 200ML");
            obj.put("UPO","1");
            obj.put("Open Qty","0");
            obj.put("Load Qty","100");
            obj.put("Adjust Qty","0");
            obj.put("Net Qty","100");
            obj.put("VALUE","1200");
            obj.put("Description","Carton 48*48 100ML");*/

            /*JSONObject obj1 = new JSONObject();
            obj1.put("Sl#","0010");
            obj1.put("Item#","14000000");
            obj1.put("Description","Carton 48*48 200ML");
            obj1.put("UPO","1");
            obj1.put("Open Qty","0");
            obj1.put("Load Qty","100");
            obj1.put("Adjust Qty","0");
            obj1.put("Net Qty","100");
            obj1.put("VALUE","1200");
            obj1.put("Description","Carton 48*48 100ML");*/

            /*JSONObject obj2 = new JSONObject();
            obj2.put("Sl#","0010");
            obj2.put("Item#","14000000");
            obj2.put("Description","Carton 48*48 200ML");
            obj2.put("UPO","1");
            obj2.put("Open Qty","0");
            obj2.put("Load Qty","100");
            obj2.put("Adjust Qty","0");
            obj2.put("Net Qty","100");
            obj2.put("VALUE","1200");
            obj2.put("Description","Carton 48*48 100ML");*/
            HEADERS.put("ITEM NO");
            HEADERS.put("ENGLISH DESCRIPTION");
            HEADERS.put("ARABIC DESCRIPTION");
            HEADERS.put("UPC ");
            HEADERS.put("TOTAL UNITS");
            HEADERS.put("UNIT PRICE");
            HEADERS.put("AMOUNT");
            //HEADERS.put("Description");
            //HEADERS.put(obj1);
            // HEADERS.put(obj2);
            mainArr.put("HEADERS", HEADERS);
            JSONObject totalObj = new JSONObject();
            totalObj.put("TOTAL UNITS", "+25");
            totalObj.put("UNIT PRICE", "");
            totalObj.put("AMOUNT", "+2230");
            TOTAL.put(totalObj);
            mainArr.put("TOTAL", TOTAL);
            JSONArray jData1 = new JSONArray();
            jData1.put("14020106");
            jData1.put("Carton 48*200ml Berain Krones");
            jData1.put("شد 48*200مل بيرين PH8");
            jData1.put("1");
            jData1.put("+15");
            jData1.put("+10");
            jData1.put("+150");
            JSONArray jData2 = new JSONArray();
            jData2.put("14020107");
            jData2.put("Carton 30*330ml Berain Krones");
            jData2.put("شد 30*330مل بيرين PH8");
            jData2.put("1");
            jData2.put("+15");
            jData2.put("+12");
            jData2.put("+1200");
            JSONArray jData3 = new JSONArray();
            jData3.put("14020123");
            jData3.put("Carton 24*600 Berain PH8 Krones");
            jData3.put("شد 24*600مل بيرين PH8");
            jData3.put("1");
            jData3.put("+10");
            jData3.put("+150");
            jData3.put("+1500");
            JSONArray jData = new JSONArray();
            jData.put(jData1);
            jData.put(jData2);
            jData.put(jData3);
            JSONArray grData = new JSONArray();
            //grData.put(jData1);
            // grData.put(jData2);
            // grData.put(jData3);
            JSONArray brData = new JSONArray();
            brData.put(jData1);
            brData.put(jData2);
            brData.put(jData3);
            /*jData.put(jData4);
            jData.put(jData5);
            jData.put(jData6);*/
            mainArr.put("data", jData);
            mainArr.put("gr", grData);
            mainArr.put("br", brData);
            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);
            jArr.put(HEADERS);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return jArr;
    }
    //Collection
    public JSONArray createDataforCollection() {
        JSONArray jArr = new JSONArray();
        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.COLLECTION);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), App.PRINT_DATE_FORMAT));
            mainArr.put("TIME", Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO", "1234");
            mainArr.put("DOCUMENT NO", "80001234");  //Load Summary No
            mainArr.put("ORDERNO", "80001234");  //Load Summary No
            mainArr.put("TRIP START DATE", Helpers.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("supervisorname", "-");
            mainArr.put("TripID", Settings.getString(App.TRIP_ID));
            mainArr.put("invheadermsg", "HAPPY NEW YEAR");
            mainArr.put("LANG", "en");
            mainArr.put("invoicepaymentterms", "2");
            mainArr.put("invoicenumber", "1300000001");
            mainArr.put("INVOICETYPE", "SALES INVOICE");
            String arabicCustomer = "اللولو هايبر ماركت";
            mainArr.put("CUSTOMER", "LULU HYPER MARKET" + "-" + arabicCustomer);
            mainArr.put("ADDRESS", "3101, 21st Street, Riyadh");
            mainArr.put("ARBADDRESS", "");
            mainArr.put("displayupc", "0");
            mainArr.put("invoicepriceprint", "1");
            mainArr.put("RECEIPT", "INVOICE RECEIPT");
            mainArr.put("SUB TOTAL", "1000");
            mainArr.put("INVOICE DISCOUNT", "20");
            mainArr.put("NET SALES", "980");
            mainArr.put("PaymentType", "2"); //0 for cash,1 for cheque, 2 for both
            //mainArr.put("Load Number","1");
            JSONArray HEADERS = new JSONArray();
            JSONArray TOTAL = new JSONArray();
            HEADERS.put("Invoice#");
            HEADERS.put("Due Date");
            HEADERS.put("Due Amount");
            HEADERS.put("Invoice Balance");
            HEADERS.put("Amount Paid");
            //HEADERS.put("Description");
            //HEADERS.put(obj1);
            // HEADERS.put(obj2);
            mainArr.put("HEADERS", HEADERS);
            JSONObject jCash = new JSONObject();
            jCash.put("Amount", "1000");
            mainArr.put("Cash", jCash);
            JSONArray jCheque = new JSONArray();
            JSONObject jChequeData = new JSONObject();
            jChequeData.put("Cheque Date", "15-02-2017");
            jChequeData.put("Cheque No", "012345");
            jChequeData.put("Bank", "Al Rajhi Bank");
            jChequeData.put("Amount", "500");
            jCheque.put(jChequeData);
            mainArr.put("Cheque", jCheque);
            mainArr.put("expayment", "");
            JSONObject totalObj = new JSONObject();
            totalObj.put("Invoice Balance", "+1000");
            totalObj.put("Amount Paid", "800");
            //totalObj.put("AMOUNT","+2230");
            TOTAL.put(totalObj);
            mainArr.put("TOTAL", totalObj);
            JSONArray jData3 = new JSONArray();
            jData3.put("140000012");
            jData3.put("13-12-2016");
            jData3.put("1000");
            jData3.put("0");
            jData3.put("1000");
            JSONArray jData = new JSONArray();
            jData.put(jData3);
            mainArr.put("data", jData);
            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);
            jArr.put(HEADERS);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return jArr;
    }
    public JSONArray createDataforUnload() {
        JSONArray jArr = new JSONArray();
        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.UNLOAD);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), App.PRINT_DATE_FORMAT));
            mainArr.put("TIME", Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO", "1234");
            mainArr.put("DOCUMENT NO", "80001234");  //Load Summary No
            mainArr.put("ORDERNO", "80001234");  //Load Summary No
            mainArr.put("TRIP START DATE", Helpers.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("supervisorname", "-");
            mainArr.put("TripID", Settings.getString(App.TRIP_ID));
            mainArr.put("invheadermsg", "HAPPY NEW YEAR");
            mainArr.put("LANG", "en");
            mainArr.put("invoicepaymentterms", "2");
            mainArr.put("invoicenumber", "1300000001");
            mainArr.put("INVOICETYPE", "SALES INVOICE");
            String arabicCustomer = "اللولو هايبر ماركت";
            mainArr.put("CUSTOMER", "LULU HYPER MARKET" + "-" + arabicCustomer);
            mainArr.put("ADDRESS", "3101, 21st Street, Riyadh");
            mainArr.put("ARBADDRESS", "");
            mainArr.put("displayupc", "0");
            mainArr.put("invoicepriceprint", "1");
            mainArr.put("SUB TOTAL", "1000");
            mainArr.put("INVOICE DISCOUNT", "20");
            mainArr.put("NET SALES", "980");
            mainArr.put("closevalue", "+5000");
            mainArr.put("availvalue", "+1000");
            mainArr.put("unloadvalue", "+2000");
            //mainArr.put("Load Number","1");
            JSONArray HEADERS = new JSONArray();
            JSONArray TOTAL = new JSONArray();
            /*obj.put("Item#","14000000");
            obj.put("Description","Carton 48*48 200ML");
            obj.put("UPO","1");
            obj.put("Open Qty","0");
            obj.put("Load Qty","100");
            obj.put("Adjust Qty","0");
            obj.put("Net Qty","100");
            obj.put("VALUE","1200");
            obj.put("Description","Carton 48*48 100ML");*/

            /*JSONObject obj1 = new JSONObject();
            obj1.put("Sl#","0010");
            obj1.put("Item#","14000000");
            obj1.put("Description","Carton 48*48 200ML");
            obj1.put("UPO","1");
            obj1.put("Open Qty","0");
            obj1.put("Load Qty","100");
            obj1.put("Adjust Qty","0");
            obj1.put("Net Qty","100");
            obj1.put("VALUE","1200");
            obj1.put("Description","Carton 48*48 100ML");*/

            /*JSONObject obj2 = new JSONObject();
            obj2.put("Sl#","0010");
            obj2.put("Item#","14000000");
            obj2.put("Description","Carton 48*48 200ML");
            obj2.put("UPO","1");
            obj2.put("Open Qty","0");
            obj2.put("Load Qty","100");
            obj2.put("Adjust Qty","0");
            obj2.put("Net Qty","100");
            obj2.put("VALUE","1200");
            obj2.put("Description","Carton 48*48 100ML");*/
            HEADERS.put("ITEMNO");
            HEADERS.put("DESCRIPTION");
            HEADERS.put("INVENTORY CALCULATED");  //Fresh unload
            HEADERS.put("RETURN TO STOCK");  //Summation of all
            HEADERS.put("TRUCK SPOILS");  //Truck Damage
            HEADERS.put("ACTUAL ON TRUCK");  //Truck Damage
            HEADERS.put("BAD RTRNS");  //Bad Returns
            HEADERS.put("----VARIANCE---- QTY AMNT");
            HEADERS.put("ENDING INV.VALUE");
            // HEADERS.put("DESCRIPTION(AR)");
            // HEADERS.put("DESCRIPTION(AR)");
            // HEADERS.put("DESCRIPTION(AR)");
            //HEADERS.put("UPC ");
            // HEADERS.put("Truck Stock");
            // HEADERS.put("Fresh Unload");
            // HEADERS.put("Truck Damage");
            // HEADERS.put("Theft");
            // HEADERS.put("Closing Stock");
            // HEADERS.put("Variance Qty");
            HEADERS.put("TOTAL VALUE");
            /*HEADERS.put("TOTAL TRUCK STOCK");
            HEADERS.put("TOTAL FRESH UNLOAD");
            HEADERS.put("TOTAL TRUCK DAMAGE");
            HEADERS.put("TOTAL CLOSING STOCK");
            HEADERS.put("TOTAL VARIANCE QTY");*/
            //HEADERS.put("Description");
            //HEADERS.put(obj1);
            // HEADERS.put(obj2);
            mainArr.put("HEADERS", HEADERS);
            JSONObject totalObj = new JSONObject();
            totalObj.put("INVENTORY CALCULATED", "+80");
            totalObj.put("RETURN TO STOCK", "+81");  //Summation of all
            totalObj.put("TRUCK SPOILS", "+82");  //Truck Damage
            totalObj.put("ACTUAL ON TRUCK", "+83");  //Truck Damage
            totalObj.put("BAD RTRNS", "+84");  //Bad Returns
            totalObj.put("----VARIANCE---- QTY AMNT", "+85");
            totalObj.put("ENDING INV.VALUE", "+86");
            /*totalObj.put("TOTAL FRESH UNLOAD","+10");
            totalObj.put("TOTAL TRUCK DAMAGE","+5");
            totalObj.put("TOTAL CLOSING STOCK","+65");
            totalObj.put("TOTAL VARIANCE QTY","+15");
            totalObj.put("Total Value","+500");*/
            TOTAL.put(totalObj);
            mainArr.put("TOTAL", TOTAL);
            JSONArray jData1 = new JSONArray();
            jData1.put("14020106");
            jData1.put("Carton 48*200ml Berain Krones");
            //jData1.put("شد 48*200مل بيرين PH8");
            //jData1.put("+1");
            jData1.put("+10");
            jData1.put("+20");
            jData1.put("+30");
            jData1.put("+40");
            jData1.put("+50");
            jData1.put("+60");
            jData1.put("+50 +60");
            //jData1.put("+60");
            jData1.put("+150");
            JSONArray jData2 = new JSONArray();
            jData2.put("14020107");
            jData2.put("Carton 30*330ml Berain Krones");
            //jData2.put("+1");
            jData2.put("+10");
            jData2.put("+20");
            jData2.put("+30");
            jData2.put("+40");
            jData2.put("+50");
            jData2.put("+60");
            jData2.put("+50 +60");
            //jData2.put("+60");
            jData2.put("+150");
            JSONArray jData3 = new JSONArray();
            jData3.put("14020123");
            jData3.put("Carton 24*600 Berain PH8 Krones");
            //jData3.put("+1");
            jData3.put("+10");
            jData3.put("+20");
            jData3.put("+30");
            jData3.put("+40");
            jData3.put("+50");
            jData3.put("+60");
            jData3.put("+50 +60");
            //jData3.put("+60");
            jData3.put("+150");
            JSONArray jData = new JSONArray();
            jData.put(jData1);
            jData.put(jData2);
            jData.put(jData3);
            mainArr.put("data", jData);
            //mainArr.put("data",jData);
            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);
            jArr.put(HEADERS);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return jArr;
    }
    public JSONArray createDataforDeposit() {
        JSONArray jArr = new JSONArray();
        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.DEPOSIT_REPORT);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), App.PRINT_DATE_FORMAT));
            mainArr.put("TIME", Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO", "1234");
            mainArr.put("DOCUMENT NO", "80001234");  //Load Summary No
            mainArr.put("ORDERNO", "80001234");  //Load Summary No
            mainArr.put("TRIP START DATE", Helpers.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("supervisorname", "-");
            mainArr.put("TripID", Settings.getString(App.TRIP_ID));
            //mainArr.put("invheadermsg","HAPPY NEW YEAR");
            mainArr.put("LANG", "en");
            mainArr.put("invoicepaymentterms", "2");
            mainArr.put("invoicenumber", "1300000001");
            mainArr.put("INVOICETYPE", "SALES INVOICE");
            String arabicCustomer = "اللولو هايبر ماركت";
            mainArr.put("CUSTOMER", "LULU HYPER MARKET" + "-" + arabicCustomer);
            mainArr.put("ADDRESS", "3101, 21st Street, Riyadh");
            mainArr.put("ARBADDRESS", "");
            mainArr.put("displayupc", "0");
            mainArr.put("invoicepriceprint", "1");
            mainArr.put("SUB TOTAL", "1000");
            mainArr.put("INVOICE DISCOUNT", "20");
            mainArr.put("NET SALES", "980");
            mainArr.put("closevalue", "+5000");
            mainArr.put("availvalue", "+1000");
            mainArr.put("TOTAL DEPOSIT AMOUNT", "+2000");
            //mainArr.put("Load Number","1");
            JSONArray HEADERS = new JSONArray();
            JSONArray TOTAL = new JSONArray();
            HEADERS.put("Transaction Number");
            HEADERS.put("Customer Code");
            HEADERS.put("Customer Name");  //Fresh unload
            HEADERS.put("Cheque No");  //Summation of all
            HEADERS.put("Cheque Date");  //Truck Damage
            HEADERS.put("Bank Name");  //Truck Damage
            HEADERS.put("Cheque Amount");  //Bad Returns
            HEADERS.put("Cash Amount");
            //HEADERS.put("Description");
            //HEADERS.put(obj1);
            // HEADERS.put(obj2);
            mainArr.put("HEADERS", HEADERS);
            JSONObject totalObj = new JSONObject();
            totalObj.put("Cheque Amount", "+200");
            totalObj.put("Cash Amount", "+100");  //Summation of all
            TOTAL.put(totalObj);
            mainArr.put("TOTAL", TOTAL);
            JSONArray jData1 = new JSONArray();
            jData1.put("14020106");
            jData1.put("200001");
            //jData1.put("شد 48*200مل بيرين PH8");
            //jData1.put("+1");
            jData1.put("Test Customer");
            jData1.put("1234");
            jData1.put("20/02/2016");
            jData1.put("Emirates NBD");
            jData1.put("100");
            jData1.put("0");
            JSONArray jData2 = new JSONArray();
            jData2.put("14020107");
            jData2.put("200001");
            //jData1.put("شد 48*200مل بيرين PH8");
            //jData1.put("+1");
            jData2.put("Test Customer");
            jData2.put("1234");
            jData2.put("20/02/2016");
            jData2.put("Emirates NBD");
            jData2.put("100");
            jData2.put("0");
            JSONArray jData3 = new JSONArray();
            jData3.put("14020106");
            jData3.put("200001");
            //jData1.put("شد 48*200مل بيرين PH8");
            //jData1.put("+1");
            jData3.put("Test Customer");
            jData3.put("-");
            jData3.put("-");
            jData3.put("-");
            jData3.put("0");
            jData3.put("100");
            JSONArray jData = new JSONArray();
            jData.put(jData1);
            jData.put(jData2);
            jData.put(jData3);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("DATA", jData);
            jsonObject.put("HEADERS", HEADERS);
            jsonObject.put("TOTAL", totalObj);
            JSONArray jDataNew = new JSONArray();
            jDataNew.put(jsonObject);
            mainArr.put("data", jDataNew);
            //mainArr.put("data",jData);
            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);
            jArr.put(HEADERS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jArr;
    }
    public JSONArray createDataforSalesSummary() {
        JSONArray jArr = new JSONArray();
        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.SALES_SUMMARY);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), App.PRINT_DATE_FORMAT));
            mainArr.put("TIME", Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO", "1234");
            mainArr.put("DOCUMENT NO", "80001234");  //Load Summary No
            mainArr.put("ORDERNO", "80001234");  //Load Summary No
            mainArr.put("TRIP START DATE", Helpers.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("supervisorname", "-");
            mainArr.put("TripID", Settings.getString(App.TRIP_ID));
            //mainArr.put("invheadermsg","HAPPY NEW YEAR");
            mainArr.put("LANG", "en");
            mainArr.put("invoicepaymentterms", "2");
            mainArr.put("invoicenumber", "1300000001");
            mainArr.put("INVOICETYPE", "SALES INVOICE");
            String arabicCustomer = "اللولو هايبر ماركت";
            mainArr.put("CUSTOMER", "LULU HYPER MARKET" + "-" + arabicCustomer);
            mainArr.put("ADDRESS", "3101, 21st Street, Riyadh");
            mainArr.put("ARBADDRESS", "");
            mainArr.put("displayupc", "0");
            mainArr.put("invoicepriceprint", "1");
            mainArr.put("SUB TOTAL", "1000");
            mainArr.put("INVOICE DISCOUNT", "20");
            mainArr.put("NET SALES", "980");
            mainArr.put("closevalue", "+5000");
            mainArr.put("availvalue", "+1000");
            mainArr.put("TOTAL DEPOSIT AMOUNT", "+2000");
            //mainArr.put("Load Number","1");
            JSONArray HEADERS = new JSONArray();
            JSONArray TOTAL = new JSONArray();
            HEADERS.put("Transaction No.");
            HEADERS.put("Cust. No.");
            HEADERS.put("Customer");
            HEADERS.put("Type");
            HEADERS.put("Sales");
            HEADERS.put("Returns");//Summation of all
            HEADERS.put("Good Rtns");  //Truck Damage
            HEADERS.put("Net Sales");  //Truck Damage
            HEADERS.put("Discounts");  //Bad Returns
            HEADERS.put("Amount Paid");
            HEADERS.put("T.C");
            //HEADERS.put("Description");
            //HEADERS.put(obj1);
            // HEADERS.put(obj2);
            mainArr.put("HEADERS", HEADERS);
            JSONObject totalObj = new JSONObject();
            totalObj.put("Cheque Amount", "+200");
            totalObj.put("Cash Amount", "+100");  //Summation of all
            TOTAL.put(totalObj);
            mainArr.put("TOTAL", TOTAL);
            JSONArray jData1 = new JSONArray();
            jData1.put("14020106");
            jData1.put("200001");
            jData1.put("Test Customer");
            jData1.put("INV");
            jData1.put("+200");
            jData1.put("-30");
            jData1.put("+10");
            jData1.put("+170");
            jData1.put("-30");
            jData1.put("+100");
            jData1.put("+20");
            JSONArray jData2 = new JSONArray();
            jData2.put("14020107");
            jData2.put("200001");
            jData2.put("Test Customer");
            jData2.put("INV");
            jData2.put("+200");
            jData2.put("-30");
            jData2.put("+10");
            jData2.put("+170");
            jData2.put("-30");
            jData2.put("+100");
            jData2.put("+20");
            JSONArray jData3 = new JSONArray();
            jData3.put("14020108");
            jData3.put("200001");
            jData3.put("Test Customer");
            jData3.put("INV");
            jData3.put("+200");
            jData3.put("-30");
            jData3.put("+10");
            jData3.put("+170");
            jData3.put("-30");
            jData3.put("+100");
            jData3.put("+20");
            JSONArray jData = new JSONArray();
            jData.put(jData1);
            jData.put(jData2);
            jData.put(jData3);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("DATA", jData);
            jsonObject.put("HEADERS", HEADERS);
            jsonObject.put("TOTAL", totalObj);
            JSONArray jDataNew = new JSONArray();
            jDataNew.put(jsonObject);
            mainArr.put("data", jData);
            mainArr.put("tcData", jData);
            mainArr.put("creditData", jData);

            /*mainArr.put("data",jData);
            mainArr.put("data",jData);
            mainArr.put("data",jData);
*/
            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);
            jArr.put(HEADERS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jArr;
    }
    public JSONArray createDataforBadReturns() {
        JSONArray jArr = new JSONArray();
        try {
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST, App.BAD_RETURN_REPORT);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), App.PRINT_DATE_FORMAT));
            mainArr.put("TIME", Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO", "1234");
            mainArr.put("DOCUMENT NO", "80001234");  //Load Summary No
            mainArr.put("ORDERNO", "80001234");  //Load Summary No
            mainArr.put("TRIP START DATE", Helpers.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("supervisorname", "-");
            mainArr.put("TripID", Settings.getString(App.TRIP_ID));
            //mainArr.put("invheadermsg","HAPPY NEW YEAR");
            mainArr.put("LANG", "en");
            mainArr.put("invoicepaymentterms", "2");
            mainArr.put("invoicenumber", "1300000001");
            mainArr.put("INVOICETYPE", "SALES INVOICE");
            String arabicCustomer = "اللولو هايبر ماركت";
            mainArr.put("CUSTOMER", "LULU HYPER MARKET" + "-" + arabicCustomer);
            mainArr.put("ADDRESS", "3101, 21st Street, Riyadh");
            mainArr.put("ARBADDRESS", "");
            mainArr.put("displayupc", "0");
            mainArr.put("invoicepriceprint", "1");
            mainArr.put("SUB TOTAL", "1000");
            mainArr.put("INVOICE DISCOUNT", "20");
            mainArr.put("NET SALES", "980");
            mainArr.put("closevalue", "+5000");
            mainArr.put("damagevariance", "+1000");
            mainArr.put("TOTAL_DAMAGE_VALUE", "+2000");
            //mainArr.put("Load Number","1");
            JSONArray HEADERS = new JSONArray();
            JSONArray TOTAL = new JSONArray();
            HEADERS.put("ITEM#");
            HEADERS.put("DESCRIPTION");
            HEADERS.put("INVOICE CREDIT");
            HEADERS.put("LOADED IN");
            HEADERS.put("PRICE");//Summation of all
            HEADERS.put("-----VARIANCE----- QTY         AMOUNT");  //Truck Damage
            //HEADERS.put("Description");
            //HEADERS.put(obj1);
            // HEADERS.put(obj2);
            mainArr.put("HEADERS", HEADERS);
            JSONObject totalObj = new JSONObject();
            totalObj.put("INVOICE CREDIT", "+200");
            totalObj.put("LOADED IN", "+100");  //Summation of all
            totalObj.put("-----VARIANCE----- QTY         AMOUNT", "+100");  //Summation of all
            TOTAL.put(totalObj);
            mainArr.put("TOTAL", TOTAL);
            JSONArray jData1 = new JSONArray();
            jData1.put("14020106");
            jData1.put("Test Material");
            jData1.put("+10");
            jData1.put("+9");
            jData1.put("+12");
            jData1.put("-1         +12");
            JSONArray jData2 = new JSONArray();
            jData2.put("14020106");
            jData2.put("Test Material");
            jData2.put("+10");
            jData2.put("+9");
            jData2.put("+12");
            jData2.put("-1         +12");
            JSONArray jData3 = new JSONArray();
            jData3.put("14020106");
            jData3.put("Test Material");
            jData3.put("+10");
            jData3.put("+9");
            jData3.put("+12");
            jData3.put("-1         +12");
            JSONArray jData = new JSONArray();
            jData.put(jData1);
            jData.put(jData2);
            jData.put(jData3);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("DATA", jData);
            jsonObject.put("HEADERS", HEADERS);
            jsonObject.put("TOTAL", totalObj);
            JSONArray jDataNew = new JSONArray();
            jDataNew.put(jsonObject);
            mainArr.put("data", jData);
            // mainArr.put("tcData",jData);
            //  mainArr.put("creditData",jData);

            /*mainArr.put("data",jData);
            mainArr.put("data",jData);
            mainArr.put("data",jData);
*/
            jDict.put("mainArr", mainArr);
            jInter.put(jDict);
            jArr.put(jInter);
            jArr.put(HEADERS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jArr;
    }
}


