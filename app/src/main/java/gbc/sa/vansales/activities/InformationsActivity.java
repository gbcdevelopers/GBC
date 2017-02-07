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
                        Intent customerlist = new Intent(InformationsActivity.this,CustomerListActivity.class);
                        startActivity(customerlist);
                        break;
                    case 1:
                        PrinterHelper object = new PrinterHelper(InformationsActivity.this,InformationsActivity.this);
                        //object.execute("",createDataForPrint()); //For Load Summary
                        object.execute("",createDataForLoadRequest());
                       /* Intent itemlist = new Intent(InformationsActivity.this,ItemListActivity.class);
                        startActivity(itemlist);*/
                        break;
                    case 2:
                        /*new Location(InformationsActivity.this, new Callback() {
                            @Override
                            public void callbackSuccess(android.location.Location location) {
                                Log.e("COORDUNATES",String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));
                                Toast.makeText(getBaseContext(),String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()),Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void callbackFailure() {
                                Toast.makeText(getBaseContext(), "Failure", Toast.LENGTH_SHORT).show();
                            }
                        });*/
                        /*try {
                            generateInvoicePDF();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }*/
                        break;
                    case 3:

                        break;
                    case 4:
                        Intent todays = new Intent(InformationsActivity.this,TodaysSummaryActivity.class);
                        startActivity(todays);
                        break;
                    case 5:
                        Intent review = new Intent(InformationsActivity.this,ReviewActivity.class);

                        startActivity(review);
                        break;
                    case 6:
                        Intent printdoc= new Intent(InformationsActivity.this,PrintDocumentActivity.class);
                        startActivity(printdoc);

                        break;
                    case 7:
                        Intent print = new Intent(InformationsActivity.this,PrinterReportsActivity.class);
                        print.putExtra("from","info");
                        startActivity(print);

                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //User This sample for Load Summary
    public JSONArray createDataForPrint(){
        JSONArray jArr = new JSONArray();
        try{
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST,App.LOAD_SUMMARY_REQUEST);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE",Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("TIME",Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO","1234");
            mainArr.put("DOCUMENT NO","80001234");  //Load Summary No
            mainArr.put("TRIP START DATE",Helpers.formatDate(new Date(),"dd-MM-yyyy"));
            mainArr.put("supervisorname","-");
            mainArr.put("TourID",Settings.getString(App.TRIP_ID));
            mainArr.put("Load Number","1");


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
            mainArr.put("HEADERS",HEADERS);

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

            mainArr.put("data",jData);
            jDict.put("mainArr",mainArr);
            jInter.put(jDict);
            jArr.put(jInter);



            jArr.put(HEADERS);



        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jArr;
    }

    //Use this for load Request and Order Request
    public JSONArray createDataForLoadRequest(){
        JSONArray jArr = new JSONArray();
        try{
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST,App.LOAD_REQUEST);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE",Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), App.PRINT_DATE_FORMAT));
            mainArr.put("TIME",Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO","1234");
            mainArr.put("DOCUMENT NO","80001234");  //Load Summary No
            mainArr.put("TRIP START DATE",Helpers.formatDate(new Date(),"dd-MM-yyyy"));
            mainArr.put("supervisorname","-");
            mainArr.put("TourID",Settings.getString(App.TRIP_ID));
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
            mainArr.put("HEADERS",HEADERS);
            JSONObject totalObj = new JSONObject();
            totalObj.put("TOTAL UNITS","+25");
            totalObj.put("UNIT PRICE","");
            totalObj.put("AMOUNT","+2230");
            TOTAL.put(totalObj);
            mainArr.put("TOTAL",TOTAL);
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

            mainArr.put("data",jData);
            jDict.put("mainArr",mainArr);
            jInter.put(jDict);
            jArr.put(jInter);



            jArr.put(HEADERS);



        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jArr;
    }
}
