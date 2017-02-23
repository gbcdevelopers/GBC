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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.data.DriverRouteFlags;
import gbc.sa.vansales.models.Collection;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.DepositReport;
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
    ArrayList<String> printReports = new ArrayList<>();
    ArrayList<CustomerHeader> customers = new ArrayList<>();
    ArrayList<DepositReport>depositReports = new ArrayList<>();
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_reports);
        flag = DriverRouteFlags.get();
        customers = CustomerHeaders.get();
        loadingSpinner = new LoadingSpinner(this);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        btn_print = (Button) findViewById(R.id.btn_print_printer_report);
        cb_deposit_report = (CheckBox)findViewById(R.id.cb_deposit_report);
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
                    Toast.makeText(getApplicationContext(),"Please select atleast one report to print",Toast.LENGTH_SHORT).show();
                }
                else if(printReports.size()>1){
                    Toast.makeText(getApplicationContext(),"Try printing one at a time",Toast.LENGTH_SHORT).show();
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
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            if(invoker.equals(App.DEPOSIT_REPORT)){
                printReport(App.DEPOSIT_REPORT);
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
                            depositReport.setChequeDate(chequeDate[0].equals("0000")?"-":Helpers.formatDate(new Date(),App.DATE_PICKER_FORMAT));
                            depositReport.setChequeAmount(chequeAmount[0].equals("0000")?"-":chequeAmount[0]);
                            depositReport.setCashAmount(c.getString(c.getColumnIndex(db.KEY_CASH_AMOUNT)));
                        }
                        depositReports.add(depositReport);
                    }
                    while (c.moveToNext());
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
}
