package gbc.sa.vansales.activities;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.data.DriverRouteFlags;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.Collection;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.DamageReport;
import gbc.sa.vansales.models.DepositReport;
import gbc.sa.vansales.models.SalesSummary;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.PrinterHelper;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
public class PrinterReportsActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    Button btn_print;
    App.DriverRouteControl flag = new App.DriverRouteControl();
    CheckBox cb_deposit_report;
    CheckBox cb_sales_summary;
    CheckBox cb_damaged_reports;
    ArrayList<String> printReports = new ArrayList<>();
    ArrayList<CustomerHeader> customers = new ArrayList<>();
    ArrayList<ArticleHeader> articles = new ArrayList<>();
    ArrayList<DepositReport>depositReports = new ArrayList<>();
    ArrayList<SalesSummary> cashSales = new ArrayList<>();
    ArrayList<SalesSummary> tcSales = new ArrayList<>();
    ArrayList<SalesSummary> creditSales = new ArrayList<>();
    ArrayList<DamageReport>damageReports = new ArrayList<>();
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_reports);
        flag = DriverRouteFlags.get();
        customers = CustomerHeaders.get();
        articles = ArticleHeaders.get();
        loadingSpinner = new LoadingSpinner(this);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        btn_print = (Button) findViewById(R.id.btn_print_printer_report);
        cb_deposit_report = (CheckBox)findViewById(R.id.cb_deposit_report);
        cb_sales_summary = (CheckBox)findViewById(R.id.cb_sales_summary);
        cb_damaged_reports = (CheckBox)findViewById(R.id.cb_damaged_reports);
        cb_deposit_report.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    printReports.add(App.DEPOSIT_REPORT);
                }
                else{
                    printReports.remove(App.DEPOSIT_REPORT);
                }
            }
        });
        cb_sales_summary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    printReports.add(App.SALES_SUMMARY);
                }
                else{
                    printReports.remove(App.SALES_SUMMARY);
                }
            }
        });
        cb_damaged_reports.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    printReports.add(App.BAD_RETURN_REPORT);
                }
                else{
                    printReports.remove(App.BAD_RETURN_REPORT);
                }
            }
        });
        if(!(flag==null)){
            if(!flag.isEodSalesReports()){
                btn_print.setAlpha(0.5f);
                btn_print.setEnabled(false);
            }
        }
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(printReports.size()==0){
                    Toast.makeText(getApplicationContext(),getString(R.string.please_select_report),Toast.LENGTH_SHORT).show();
                }
                else if(printReports.size()>1){
                    Toast.makeText(getApplicationContext(),getString(R.string.oneatattime),Toast.LENGTH_SHORT).show();
                }
                else{
                    final Dialog dialog = new Dialog(PrinterReportsActivity.this);
                    dialog.setContentView(R.layout.dialog_doprint);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    LinearLayout btn_print = (LinearLayout) dialog.findViewById(R.id.ll_print);
                    LinearLayout btn_notprint = (LinearLayout) dialog.findViewById(R.id.ll_notprint);
                    btn_print.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new loadReports(printReports.get(0));
                            dialog.dismiss();
                            /*Intent intent = new Intent(PrinterReportsActivity.this, DashboardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();*/
                        }
                    });
                    btn_notprint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                           /* Intent intent = new Intent(PrinterReportsActivity.this, DashboardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();*/
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show();
                }


            }
        });
        if (getIntent().getExtras() != null) {
            String from = getIntent().getStringExtra("from");
            if (from.equals("customer")) {
                tv_top_header.setText("Print");
            } else {
                tv_top_header.setText("Print Reports");
            }
        } else {
            tv_top_header.setText("End Trip");
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrinterReportsActivity.this, InformationsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public class loadReports extends AsyncTask<String,String,String>{

        String invoker = null;

        private loadReports(String invoker){
            this.invoker = invoker;
            execute();
        }

        @Override
        protected String doInBackground(String... params) {
            switch (invoker){
                case App.DEPOSIT_REPORT:{
                    loadData(App.DEPOSIT_REPORT);
                    //printReport(App.DEPOSIT_REPORT);
                    break;
                }
                case App.SALES_SUMMARY:{
                    loadData(App.SALES_SUMMARY);
                    break;
                }
                case App.BAD_RETURN_REPORT:{
                    loadData(App.BAD_RETURN_REPORT);
                    break;
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            if(invoker.equals(App.DEPOSIT_REPORT)){
                printReport(App.DEPOSIT_REPORT);
            }
            if(invoker.equals(App.SALES_SUMMARY)){
                printReport(App.SALES_SUMMARY);
            }
            if(invoker.equals(App.BAD_RETURN_REPORT)){
                printReport(App.BAD_RETURN_REPORT);
            }
        }
    }
    public void loadData(String type){
        switch (type){
            case App.DEPOSIT_REPORT:{
                HashMap<String,String> map = new HashMap<>();
                map.put(db.KEY_COLLECTION_TYPE,"");
                map.put(db.KEY_CUSTOMER_TYPE,"");
                map.put(db.KEY_CUSTOMER_NO,"");
                map.put(db.KEY_INVOICE_NO,"");
                map.put(db.KEY_INVOICE_AMOUNT,"");
                map.put(db.KEY_DUE_DATE,"");
                map.put(db.KEY_INVOICE_DATE,"");
                map.put(db.KEY_AMOUNT_CLEARED,"");
                map.put(db.KEY_CASH_AMOUNT,"");
                map.put(db.KEY_CHEQUE_AMOUNT,"");
                map.put(db.KEY_CHEQUE_NUMBER,"");
                map.put(db.KEY_CHEQUE_DATE,"");
                map.put(db.KEY_CHEQUE_BANK_CODE,"");
                map.put(db.KEY_CHEQUE_BANK_NAME,"");
                map.put(db.KEY_SAP_INVOICE_NO,"");
                map.put(db.KEY_INVOICE_DAYS,"");
                map.put(db.KEY_INDICATOR,"");
                map.put(db.KEY_IS_POSTED,"");
                map.put(db.KEY_IS_PRINTED,"");
                map.put(db.KEY_IS_INVOICE_COMPLETE,"");
                HashMap<String,String>filter = new HashMap<>();
                filter.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                Cursor c = db.getData(db.COLLECTION,map,filter);
                if(c.getCount()>0){
                    c.moveToFirst();
                    do{
                        DepositReport depositReport = new DepositReport();
                        depositReport.setInvoiceNo(c.getString(c.getColumnIndex(db.KEY_INVOICE_NO)));
                        depositReport.setCustomerNo(c.getString(c.getColumnIndex(db.KEY_CUSTOMER_NO)));
                        CustomerHeader customerHeader = CustomerHeader.getCustomer(customers,c.getString(c.getColumnIndex(db.KEY_CUSTOMER_NO)));
                        if(customerHeader!=null){
                            depositReport.setCustomerName(customerHeader.getName1());
                        }
                        else{
                            depositReport.setCustomerName(c.getString(c.getColumnIndex(db.KEY_CUSTOMER_NO)));
                        }
                        String[]cheques = new String[10];
                        String[]chequeDate = new String[10];
                        String[]chequeAmount = new String[10];
                        String[]bankCode = new String[10];
                        String[]bankNames = new String[10];
                        Log.e("Cheq","" + UrlBuilder.decodeString(c.getString(c.getColumnIndex(db.KEY_CHEQUE_NUMBER))));
                        cheques = UrlBuilder.decodeString(c.getString(c.getColumnIndex(db.KEY_CHEQUE_NUMBER))).split(",");
                        Log.e("cheqest","" + UrlBuilder.decodeString(c.getString(c.getColumnIndex(db.KEY_CHEQUE_DATE))));
                        chequeDate = UrlBuilder.decodeString(c.getString(c.getColumnIndex(db.KEY_CHEQUE_DATE))).split(",");
                        chequeAmount = UrlBuilder.decodeString(c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT))).split(",");
                        bankCode = UrlBuilder.decodeString(c.getString(c.getColumnIndex(db.KEY_BANK_CODE))).split(",");
                        bankNames = UrlBuilder.decodeString(c.getString(c.getColumnIndex(db.KEY_BANK_NAME))).split(",");
                        if(cheques.length>2){
                            for(int j=1;j<cheques.length;j++){
                                depositReport.setChequeNo(cheques[j]);
                                depositReport.setBankName(bankNames[j]);
                                depositReport.setBankCode(bankCode[j]);
                                depositReport.setChequeDate(chequeDate[j]);
                                depositReport.setChequeAmount(chequeAmount[j]);
                                depositReport.setCashAmount(c.getString(c.getColumnIndex(db.KEY_CASH_AMOUNT)));
                                depositReports.add(depositReport);
                            }
                        }
                        else if(cheques.length==1){
                            depositReport.setChequeNo(cheques[0].equals("0000") ? "-" : cheques[0]);
                            depositReport.setBankName(bankNames[0].equals("0000") ? "-" : bankNames[0]);
                            depositReport.setBankCode(bankCode[0].equals("0000") ? "-" : bankCode[0]);
                            depositReport.setChequeDate(chequeDate[0].equals("0000") ? "-" : chequeDate[0]);
                            depositReport.setChequeAmount(chequeAmount[0].equals("0000") ? "-" : chequeAmount[0]);
                            depositReport.setCashAmount(c.getString(c.getColumnIndex(db.KEY_CASH_AMOUNT)));

                        }
                        else if(cheques.length==2){
                            depositReport.setChequeNo(cheques[1].equals("0000")?"-":cheques[1]);
                            depositReport.setBankName(bankNames[1].equals("0000")?"-":bankNames[1]);
                            depositReport.setBankCode(bankCode[1].equals("0000")?"-":bankCode[1]);
                            depositReport.setChequeDate(chequeDate[0].equals("0000")?chequeDate.length>1?chequeDate[1]:"-":Helpers.formatDate(new Date(),App.DATE_PICKER_FORMAT));
                            depositReport.setChequeAmount(chequeAmount[0].equals("0000")?"-":chequeAmount[0]);
                            depositReport.setCashAmount(c.getString(c.getColumnIndex(db.KEY_CASH_AMOUNT)));
                        }
                        depositReports.add(depositReport);
                    }
                    while (c.moveToNext());
                }
                break;
            }
            case App.SALES_SUMMARY:{
                HashMap<String,String>map = new HashMap<>();
                map.put(db.KEY_TIME_STAMP,"");
                map.put(db.KEY_CUSTOMER_NO,"");
                map.put(db.KEY_CUSTOMER_TYPE,"");
                map.put(db.KEY_ACTIVITY_TYPE,"");
                map.put(db.KEY_ORDER_TOTAL,"");
                map.put(db.KEY_ORDER_NET_TOTAL,"");
                map.put(db.KEY_RETURN_TOTAL,"");
                map.put(db.KEY_GOOD_RETURN_TOTAL,"");
                map.put(db.KEY_ORDER_DISCOUNT,"");
                map.put(db.KEY_ORDER_ID,"");
                HashMap<String,String>filter = new HashMap<>();
                Cursor c = db.getData(db.TODAYS_SUMMARY_SALES,map,filter);
                if(c.getCount()>0){
                    c.moveToFirst();
                    do{
                        SalesSummary salesSummary = new SalesSummary();
                        salesSummary.setTransactionNo(c.getString(c.getColumnIndex(db.KEY_ORDER_ID)));
                        salesSummary.setCustomerNo(c.getString(c.getColumnIndex(db.KEY_CUSTOMER_NO)));
                        CustomerHeader customerHeader = CustomerHeader.getCustomer(customers,c.getString(c.getColumnIndex(db.KEY_CUSTOMER_NO)));
                        if(customerHeader!=null){
                            salesSummary.setCustomerName(customerHeader.getName1());
                        }
                        else{
                            salesSummary.setCustomerName(c.getString(c.getColumnIndex(db.KEY_CUSTOMER_NO)));
                        }
                        HashMap<String,String>invoiceClearMap = new HashMap<>();
                        invoiceClearMap.put(db.KEY_CUSTOMER_NO,"");
                        invoiceClearMap.put(db.KEY_INVOICE_NO,"");
                        invoiceClearMap.put(db.KEY_INVOICE_AMOUNT,"");
                        invoiceClearMap.put(db.KEY_DUE_DATE,"");
                        invoiceClearMap.put(db.KEY_INVOICE_DATE,"");
                        invoiceClearMap.put(db.KEY_AMOUNT_CLEARED,"");
                        invoiceClearMap.put(db.KEY_INDICATOR,"");
                        invoiceClearMap.put(db.KEY_IS_INVOICE_COMPLETE,"");
                        HashMap<String,String>invoiceClearMapFilter = new HashMap<>();
                        invoiceClearMapFilter.put(db.KEY_CUSTOMER_NO,c.getString(c.getColumnIndex(db.KEY_CUSTOMER_NO)));
                        invoiceClearMapFilter.put(db.KEY_INVOICE_NO,c.getString(c.getColumnIndex(db.KEY_ORDER_ID)));
                        Cursor ivCursor = db.getData(db.COLLECTION,invoiceClearMap,invoiceClearMapFilter);
                        if(ivCursor.getCount()>0){
                            ivCursor.moveToFirst();
                            salesSummary.setAmountPaid(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_AMOUNT_CLEARED)));
                            salesSummary.setAmountDue(String.valueOf(Double.parseDouble(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_INVOICE_AMOUNT)))-
                            Double.parseDouble(ivCursor.getString(ivCursor.getColumnIndex(db.KEY_AMOUNT_CLEARED)))));
                        }
                        salesSummary.setTransactionType(c.getString(c.getColumnIndex(db.KEY_ACTIVITY_TYPE)).equals(App.ACTIVITY_INVOICE) ? "INV" : "DLV");
                        salesSummary.setTotalSales(c.getString(c.getColumnIndex(db.KEY_ORDER_NET_TOTAL)));
                        salesSummary.setTotalReturns(c.getString(c.getColumnIndex(db.KEY_RETURN_TOTAL)));
                        salesSummary.setTotalgoodReturns(c.getString(c.getColumnIndex(db.KEY_GOOD_RETURN_TOTAL)));
                        salesSummary.setDiscounts(c.getString(c.getColumnIndex(db.KEY_ORDER_DISCOUNT)));
                        salesSummary.setNetSales(c.getString(c.getColumnIndex(db.KEY_ORDER_TOTAL)));
                        if(c.getString(c.getColumnIndex(db.KEY_CUSTOMER_TYPE)).equals(App.CASH_CUSTOMER)){
                            cashSales.add(salesSummary);
                        }
                        else if(c.getString(c.getColumnIndex(db.KEY_CUSTOMER_TYPE)).equals(App.TC_CUSTOMER)){
                            tcSales.add(salesSummary);
                        }
                        else if(c.getString(c.getColumnIndex(db.KEY_CUSTOMER_TYPE)).equals(App.CREDIT_CUSTOMER)){
                            creditSales.add(salesSummary);
                        }
                    }
                    while (c.moveToNext());
                }
                break;
            }
            case App.BAD_RETURN_REPORT:{
                for (int i = 0; i < articles.size(); i++) {
                    DamageReport damageReport = new DamageReport();
                    float badReturnQuantity = 0;
                    float badReturnVariance = 0;
                    damageReport.setItemNo(articles.get(i).getMaterialNo());
                    ArticleHeader articleHeader = ArticleHeader.getArticle(articles,articles.get(i).getMaterialNo());
                    if(articleHeader!=null){
                        damageReport.setItemDescription(articleHeader.getMaterialDesc1());
                    }
                    else{
                        damageReport.setItemDescription(articles.get(i).getMaterialNo());
                    }
                    HashMap<String, String> map = new HashMap<>();
                    map.put(db.KEY_ITEM_NO, "");
                    map.put(db.KEY_MATERIAL_NO, "");
                    map.put(db.KEY_CASE, "");
                    map.put(db.KEY_UNIT, "");
                    HashMap<String, String> filter = new HashMap<>();
                    filter.put(db.KEY_REASON_TYPE, App.BAD_RETURN);
                    filter.put(db.KEY_MATERIAL_NO, articles.get(i).getMaterialNo());
                    Cursor cursor = db.getData(db.RETURNS, map, filter);
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do{
                            badReturnQuantity += Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.KEY_CASE)));
                        }
                        while (cursor.moveToNext());
                        HashMap<String, String> varMap = new HashMap<>();
                        varMap.put(db.KEY_MATERIAL_NO, "");
                        varMap.put(db.KEY_CASE, "");
                        varMap.put(db.KEY_UNIT, "");
                        HashMap<String, String> varFilter = new HashMap<>();
                        varFilter.put(db.KEY_VARIANCE_TYPE, App.BAD_RETURN_VARIANCE);
                        varFilter.put(db.KEY_MATERIAL_NO, articles.get(i).getMaterialNo());
                        Cursor varCursor = db.getData(db.UNLOAD_VARIANCE, varMap, varFilter);
                        if(varCursor.getCount()>0){
                            varCursor.moveToFirst();
                            do{
                                badReturnVariance+= Float.parseFloat(varCursor.getString(varCursor.getColumnIndex(db.KEY_CASE)));
                            }
                            while (varCursor.moveToNext());
                        }
                        damageReport.setItemQuantity(String.valueOf(badReturnQuantity));
                        damageReport.setItemVariance(String.valueOf(badReturnVariance));
                    }

                    HashMap<String, String> priceMap = new HashMap<>();
                    priceMap.put(db.KEY_AMOUNT, "");
                    HashMap<String, String> filterPrice = new HashMap<>();
                    filterPrice.put(db.KEY_MATERIAL_NO, articles.get(i).getMaterialNo());
                    filterPrice.put(db.KEY_PRIORITY, "2");
                    Cursor priceCursor = db.getData(db.PRICING, priceMap, filterPrice);
                    if (priceCursor.getCount() > 0) {
                        priceCursor.moveToFirst();
                        damageReport.setItemPrice(priceCursor.getString(priceCursor.getColumnIndex(db.KEY_AMOUNT)));
                    } else {
                        damageReport.setItemPrice("0");
                    }


                    if(badReturnQuantity>0){
                        damageReports.add(damageReport);
                    }
                }
                break;
            }
        }
    }
    public void printReport(String type){
        PrinterHelper object = new PrinterHelper(PrinterReportsActivity.this,PrinterReportsActivity.this);

        switch (type){
            case App.DEPOSIT_REPORT:{
                object.execute("",createDataforDeposit());
                break;
            }
            case App.SALES_SUMMARY:{
                object.execute("",createDataforSalesSummary());
                break;
            }
            case App.BAD_RETURN_REPORT:{
                object.execute("",createDataforBadReturns());
                break;
            }
        }
    }
    public JSONArray createDataforDeposit(){
        JSONArray jArr = new JSONArray();
        try{
            float totalCash = 0;
            float totalCheque = 0;

            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST,App.DEPOSIT_REPORT);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE", Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), App.PRINT_DATE_FORMAT));
            mainArr.put("TIME",Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("TRIP START DATE",Helpers.formatDate(new Date(),"dd-MM-yyyy"));
            mainArr.put("supervisorname","-");
            mainArr.put("TripID",Settings.getString(App.TRIP_ID));
            //mainArr.put("invheadermsg","HAPPY NEW YEAR");
            mainArr.put("LANG","en");


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
            mainArr.put("HEADERS",HEADERS);

            JSONArray jData = new JSONArray();
            for(DepositReport obj:depositReports){
                JSONArray jData1 = new JSONArray();
                jData1.put(obj.getInvoiceNo());
                jData1.put(obj.getCustomerNo());
                jData1.put(obj.getCustomerName());
                jData1.put(obj.getChequeNo());
                jData1.put(obj.getChequeDate());
                jData1.put(obj.getBankName());
                jData1.put(obj.getChequeAmount());
                totalCheque += Double.parseDouble(obj.getChequeAmount());
                jData1.put(obj.getCashAmount());
                totalCash += Double.parseDouble(obj.getCashAmount());
                jData.put(jData1);
            }

            JSONObject totalObj = new JSONObject();
            totalObj.put("Cheque Amount","+" + String.valueOf(totalCheque));
            totalObj.put("Cash Amount","+" + String.valueOf(totalCash));  //Summation of all
            TOTAL.put(totalObj);
            mainArr.put("TOTAL",TOTAL);
            mainArr.put("TOTAL DEPOSIT AMOUNT","+" + String.valueOf(totalCash+totalCheque));
            mainArr.put("TOTAL CASH AMOUNT","+" + String.valueOf(totalCash));
            mainArr.put("TOTAL CHEQUE AMOUNT","+" + String.valueOf(totalCheque));
            /*JSONArray jData1 = new JSONArray();
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
            jData3.put("100");*/




            /*jData.put(jData1);
            jData.put(jData2);
            jData.put(jData3);*/
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("DATA",jData);
            jsonObject.put("HEADERS",HEADERS);
            jsonObject.put("TOTAL",totalObj);
            JSONArray jDataNew = new JSONArray();
            jDataNew.put(jsonObject);
            mainArr.put("data",jDataNew);
            //mainArr.put("data",jData);

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
    public JSONArray createDataforSalesSummary(){
        JSONArray jArr = new JSONArray();
        try{
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST,App.SALES_SUMMARY);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE",Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), App.PRINT_DATE_FORMAT));
            mainArr.put("TIME",Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("TRIP START DATE",Helpers.formatDate(new Date(),"dd-MM-yyyy"));
            mainArr.put("TripID",Settings.getString(App.TRIP_ID));
            //mainArr.put("invheadermsg","HAPPY NEW YEAR");
            mainArr.put("LANG","en");
            mainArr.put("invoicepaymentterms","2");
            mainArr.put("invoicenumber","1300000001");
            mainArr.put("INVOICETYPE","SALES INVOICE");
            String arabicCustomer = "اللولو هايبر ماركت";
            mainArr.put("CUSTOMER","LULU HYPER MARKET" + "-" + arabicCustomer);
            mainArr.put("ADDRESS","3101, 21st Street, Riyadh");
            mainArr.put("ARBADDRESS","");
            mainArr.put("displayupc","0");
            mainArr.put("invoicepriceprint","1");
            mainArr.put("SUB TOTAL","1000");
            mainArr.put("INVOICE DISCOUNT","20");
            mainArr.put("NET SALES","980");
            mainArr.put("closevalue","+5000");
            mainArr.put("availvalue","+1000");
            mainArr.put("TOTAL DEPOSIT AMOUNT","+2000");

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

            mainArr.put("HEADERS",HEADERS);
            JSONObject totalObj = new JSONObject();
            totalObj.put("Cheque Amount","+200");
            totalObj.put("Cash Amount","+100");  //Summation of all
            TOTAL.put(totalObj);
            mainArr.put("TOTAL",TOTAL);
            JSONArray cashData = new JSONArray();
            JSONArray tcData = new JSONArray();
            JSONArray chequeData = new JSONArray();
            for(SalesSummary salesSummary:cashSales){
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(salesSummary.getTransactionNo());
                jsonArray.put(salesSummary.getCustomerNo());
                jsonArray.put(salesSummary.getCustomerName());
                jsonArray.put(salesSummary.getTransactionType());
                jsonArray.put(salesSummary.getTotalSales());
                jsonArray.put(salesSummary.getTotalReturns());
                jsonArray.put(salesSummary.getTotalgoodReturns());
                jsonArray.put(salesSummary.getNetSales());
                jsonArray.put(salesSummary.getDiscounts());
                jsonArray.put(salesSummary.getAmountPaid());
                jsonArray.put(" "+salesSummary.getAmountDue());
                cashData.put(jsonArray);
            }
            for(SalesSummary salesSummary:tcSales){
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(salesSummary.getTransactionNo());
                jsonArray.put(salesSummary.getCustomerNo());
                jsonArray.put(salesSummary.getCustomerName());
                jsonArray.put(salesSummary.getTransactionType());
                jsonArray.put(salesSummary.getTotalSales());
                jsonArray.put(salesSummary.getTotalReturns());
                jsonArray.put(salesSummary.getTotalgoodReturns());
                jsonArray.put(salesSummary.getNetSales());
                jsonArray.put(salesSummary.getDiscounts());
                jsonArray.put(salesSummary.getAmountPaid());
                jsonArray.put(" "+salesSummary.getAmountDue());
                tcData.put(jsonArray);
            }
            for(SalesSummary salesSummary:creditSales){
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(salesSummary.getTransactionNo());
                jsonArray.put(salesSummary.getCustomerNo());
                jsonArray.put(salesSummary.getCustomerName());
                jsonArray.put(salesSummary.getTransactionType());
                jsonArray.put(salesSummary.getTotalSales());
                jsonArray.put(salesSummary.getTotalReturns());
                jsonArray.put(salesSummary.getTotalgoodReturns());
                jsonArray.put(salesSummary.getNetSales());
                jsonArray.put(salesSummary.getDiscounts());
                jsonArray.put(salesSummary.getAmountPaid());
                jsonArray.put(" " + salesSummary.getAmountDue());
                chequeData.put(jsonArray);
            }

            JSONArray jData = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("DATA",jData);
            jsonObject.put("HEADERS",HEADERS);
            jsonObject.put("TOTAL",totalObj);
            JSONArray jDataNew = new JSONArray();
            jDataNew.put(jsonObject);
            mainArr.put("data",cashData);
            mainArr.put("tcData",tcData);
            mainArr.put("creditData",chequeData);

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
    public JSONArray createDataforBadReturns(){
        JSONArray jArr = new JSONArray();
        try{
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            float totalInventoryCredit = 0;
            float totalLoadedIn = 0;
            float totalVarianceQty = 0;
            float totalVarianceAmount = 0;
            float totalInventoryCreditValue = 0;
            float totalLoadedInValue = 0;
            jDict.put(App.REQUEST,App.BAD_RETURN_REPORT);
            JSONObject mainArr = new JSONObject();
            mainArr.put("ROUTE",Settings.getString(App.ROUTE));
            mainArr.put("DOC DATE", Helpers.formatDate(new Date(), App.PRINT_DATE_FORMAT));
            mainArr.put("TIME",Helpers.formatTime(new Date(), "hh:mm"));
            mainArr.put("SALESMAN", Settings.getString(App.DRIVER));
            mainArr.put("CONTACTNO","1234");
            mainArr.put("DOCUMENT NO","80001234");  //Load Summary No
            mainArr.put("ORDERNO","80001234");  //Load Summary No
            mainArr.put("TRIP START DATE",Helpers.formatDate(new Date(), "dd-MM-yyyy"));
            mainArr.put("supervisorname","-");
            mainArr.put("TripID",Settings.getString(App.TRIP_ID));
            //mainArr.put("invheadermsg","HAPPY NEW YEAR");
            mainArr.put("LANG","en");
            mainArr.put("invoicepaymentterms","2");
            mainArr.put("invoicenumber","1300000001");
            mainArr.put("INVOICETYPE","SALES INVOICE");
            String arabicCustomer = "اللولو هايبر ماركت";
            mainArr.put("CUSTOMER","LULU HYPER MARKET" + "-" + arabicCustomer);
            mainArr.put("ADDRESS","3101, 21st Street, Riyadh");
            mainArr.put("ARBADDRESS","");
            mainArr.put("displayupc","0");
            mainArr.put("invoicepriceprint","1");
            mainArr.put("SUB TOTAL","1000");
            mainArr.put("INVOICE DISCOUNT","20");
            mainArr.put("NET SALES","980");
            mainArr.put("closevalue","+5000");


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
            mainArr.put("HEADERS",HEADERS);
            JSONArray jData = new JSONArray();
            for(DamageReport obj:damageReports){
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(StringUtils.stripStart(obj.getItemNo(), "0"));
                jsonArray.put(UrlBuilder.decodeString(obj.getItemDescription()));
                jsonArray.put(obj.getItemQuantity());
                double inventoryCredit = Double.parseDouble(obj.getItemQuantity());
                totalInventoryCredit += Double.parseDouble(obj.getItemQuantity());
                jsonArray.put(String.valueOf(Double.parseDouble(obj.getItemQuantity()) - Double.parseDouble(obj.getItemVariance())));
                double loadedIn = Double.parseDouble(obj.getItemQuantity())-Double.parseDouble(obj.getItemVariance());
                totalLoadedIn += loadedIn;
                jsonArray.put(obj.getItemPrice());
                double itemPrice = Double.parseDouble(obj.getItemPrice());
                totalLoadedInValue += loadedIn*itemPrice;
                totalInventoryCreditValue += inventoryCredit*itemPrice;
                double varianceValue = Double.parseDouble(obj.getItemVariance())*itemPrice;
                jsonArray.put(obj.getItemVariance()+"         "+String.valueOf(varianceValue));
                totalVarianceQty += Double.parseDouble(obj.getItemVariance());
                totalVarianceAmount += varianceValue;
                jData.put(jsonArray);
            }
            JSONObject totalObj = new JSONObject();
            totalObj.put("INVOICE CREDIT","+" + String.valueOf(totalInventoryCredit));
            totalObj.put("LOADED IN","+" + String.valueOf(totalLoadedIn));  //Summation of all
            totalObj.put("-----VARIANCE----- QTY         AMOUNT",String.valueOf(totalVarianceQty)+ "         " + String.valueOf(totalVarianceAmount));  //Summation of all
            TOTAL.put(totalObj);
            mainArr.put("TOTAL",TOTAL);
            mainArr.put("damagevariance","+" + String.valueOf(totalLoadedInValue));
            mainArr.put("TOTAL_DAMAGE_VALUE","+" + String.valueOf(totalInventoryCreditValue));
            /*JSONArray jData1 = new JSONArray();
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


            jData.put(jData1);
            jData.put(jData2);
            jData.put(jData3);*/
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("DATA",jData);
            jsonObject.put("HEADERS",HEADERS);
            jsonObject.put("TOTAL",totalObj);
            JSONArray jDataNew = new JSONArray();
            jDataNew.put(jsonObject);
            mainArr.put("data",jData);
            // mainArr.put("tcData",jData);
            //  mainArr.put("creditData",jData);

            /*mainArr.put("data",jData);
            mainArr.put("data",jData);
            mainArr.put("data",jData);
*/
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
