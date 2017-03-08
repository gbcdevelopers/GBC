package gbc.sa.vansales.activities;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.Fragment.AllCustomerFragment;
import gbc.sa.vansales.Fragment.VisitAllFragment;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerStatusAdapter;
import gbc.sa.vansales.models.CustomerStatus;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.Settings;
/**
 * Created by Rakshit on 12-Jan-17.
 */
/*
@ This activity will be called when the + button is clicked on the visit list or all customer screen.
@ This will create a customer in the backend.
*/
public class AddCustomerActivity extends AppCompatActivity {
    Button addCustomer;
    ImageView iv_back;
    TextView tv_top_header;
    TextView tv_customer_id;
    EditText et_customer_name;
    EditText et_customer_name_ar;
    EditText et_trade_name;
    EditText et_trade_name_ar;
    EditText et_customer_address1;
    EditText et_customer_address2;
    EditText et_customer_pobox;
    EditText et_customer_email;
    EditText et_customer_telephone;
    EditText et_customer_fax;
    TextView tv_customer_route;
    EditText et_cr_no;
    TextView tv_customer_sales_area;
    Button btn_sales_area;
    Button btn_distribution;
    Button btn_division;
    String salesArea;
    String distribution;
    String division;
    private ArrayList<CustomerStatus> arrayList = new ArrayList<>();
    private ArrayAdapter<CustomerStatus> adapter;

    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        adapter = new CustomerStatusAdapter(this, arrayList);

        addCustomer = (Button)findViewById(R.id.btn_add_customer);
        tv_customer_id = (TextView)findViewById(R.id.tv_customer_id);
        et_customer_name = (EditText)findViewById(R.id.et_owner_name);
        et_customer_name_ar = (EditText)findViewById(R.id.et_owner_name_ar);
        et_trade_name = (EditText)findViewById(R.id.et_trade_name);
        et_trade_name_ar = (EditText)findViewById(R.id.et_trade_name_ar);
        et_customer_address1 = (EditText)findViewById(R.id.et_customer_address1);
        et_customer_address2 = (EditText)findViewById(R.id.et_customer_address2);
        et_cr_no = (EditText)findViewById(R.id.et_cr_no);
        et_customer_pobox = (EditText)findViewById(R.id.et_customer_pobox);
        et_customer_email = (EditText)findViewById(R.id.et_customer_email);
        et_customer_telephone = (EditText)findViewById(R.id.et_customer_telephone);
        et_customer_fax = (EditText)findViewById(R.id.et_customer_fax);
        tv_customer_route = (TextView)findViewById(R.id.tv_customer_route);
        //tv_customer_sales_area = (TextView)findViewById(R.id.tv_customer_sales_area);
        btn_sales_area = (Button)findViewById(R.id.btn_sales_area);
        btn_distribution = (Button)findViewById(R.id.btn_distribution);
        btn_division = (Button)findViewById(R.id.btn_division);

        salesArea = "1000";
        btn_sales_area.setText(getString(R.string.berain));
        btn_distribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("Distribution",null);
            }

        });

        btn_division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("Division",distribution);
            }
        });

        tv_customer_id.setText(Helpers.generateCustomer(db, ConfigStore.CustomerNew_PR_Type));
        tv_customer_route.setText(Settings.getString(App.ROUTE));
     //   tv_customer_sales_area.setText(Settings.getString(App.SALES_ORG) + "/" + Settings.getString(App.DIST_CHANNEL) + "/" + Settings.getString(App.DIVISION));

        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    if(!checkNullValues()){
                        Toast.makeText(AddCustomerActivity.this,getString(R.string.requiredFields),Toast.LENGTH_SHORT).show();
                    }
                    else{
                        HashMap<String,String> map = new HashMap<String, String>();
                        map.put(db.KEY_TRIP_ID,"");
                        HashMap<String,String> filter = new HashMap<String, String>();
                        Cursor cursor = db.getData(db.VISIT_LIST,map,filter);

                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
                        params.put(db.KEY_VISITLISTID,Settings.getString(App.TRIP_ID).replaceAll(Settings.getString(App.ROUTE),"").trim());
                        params.put(db.KEY_ITEMNO, StringUtils.leftPad(String.valueOf(cursor.getCount() + 2), 3, "0"));
                        params.put(db.KEY_CUSTOMER_NO, tv_customer_id.getText().toString());
                        params.put(db.KEY_EXEC_DATE, Helpers.formatDate(new Date(), App.DATE_FORMAT));
                        // params.put(db.KEY_EXEC_DATE, object.get("Execdate").toString().substring(0,10));
                        params.put(db.KEY_DRIVER,Settings.getString(App.DRIVER));
                        params.put(db.KEY_VP_TYPE,"");

                        params.put(db.KEY_IS_DELIVERY_CAPTURED,App.IS_NOT_COMPLETE);
                        params.put(db.KEY_IS_ORDER_CAPTURED,App.IS_NOT_COMPLETE);
                        params.put(db.KEY_IS_SALES_CAPTURED,App.IS_NOT_COMPLETE);
                        params.put(db.KEY_IS_COLLECTION_CAPTURED,App.IS_NOT_COMPLETE);
                        params.put(db.KEY_IS_MERCHANDIZE_CAPTURED,App.IS_NOT_COMPLETE);
                        params.put(db.KEY_IS_VISITED,App.IS_NOT_COMPLETE);

                        params.put(db.KEY_IS_DELIVERY_POSTED,App.DATA_NOT_POSTED);
                        params.put(db.KEY_IS_ORDER_POSTED,App.DATA_NOT_POSTED);
                        params.put(db.KEY_IS_SALES_POSTED,App.DATA_NOT_POSTED);
                        params.put(db.KEY_IS_COLLECTION_POSTED, App.DATA_NOT_POSTED);
                        params.put(db.KEY_IS_MERCHANDIZE_POSTED, App.DATA_NOT_POSTED);
                        params.put(db.KEY_IS_NEW_CUSTOMER, App.TRUE);
                        /************************************************************
                        @ The customer created should become a part of the visit list
                        @ so adding the customer in the visit list table
                        ************************************************************/
                        db.addData(db.VISIT_LIST, params);

                        HashMap<String, String> headerParams = new HashMap<>();
                        headerParams.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
                        headerParams.put(db.KEY_ORDER_BLOCK  ,"");
                        headerParams.put(db.KEY_INVOICE_BLOCK ,"");
                        headerParams.put(db.KEY_DELIVERY_BLOCK,"");
                        headerParams.put(db.KEY_ROOM_NO,"");
                        headerParams.put(db.KEY_FLOOR,"");
                        headerParams.put(db.KEY_BUILDING ,"");
                        headerParams.put(db.KEY_HOME_CITY ,"");
                        headerParams.put(db.KEY_STREET5 ,"");
                        headerParams.put(db.KEY_STREET4 ,"");
                        headerParams.put(db.KEY_STREET3 ,"");
                        headerParams.put(db.KEY_STREET2 ,"");
                        headerParams.put(db.KEY_NAME4 ,"");
                        headerParams.put(db.KEY_DRIVER,Settings.getString(App.DRIVER));
                        headerParams.put(db.KEY_CUSTOMER_NO,tv_customer_id.getText().toString());
                        headerParams.put(db.KEY_COUNTRY_CODE, "SA");
                        headerParams.put(db.KEY_NAME3 ,et_customer_name.getText().toString());
                        headerParams.put(db.KEY_NAME1 ,et_customer_name.getText().toString());
                        headerParams.put(db.KEY_ADDRESS ,et_customer_address1.getText().toString());
                        headerParams.put(db.KEY_STREET ,et_customer_address2.getText().toString());
                        headerParams.put(db.KEY_NAME2 ,"");
                        headerParams.put(db.KEY_CITY ,"");
                        headerParams.put(db.KEY_DISTRICT ,"");
                        headerParams.put(db.KEY_REGION ,"001");
                        headerParams.put(db.KEY_SITE_CODE ,"");
                        headerParams.put(db.KEY_POST_CODE, et_customer_pobox.getText().toString());
                        headerParams.put(db.KEY_PHONE_NO, et_customer_telephone.getText().toString());
                        headerParams.put(db.KEY_COMPANY_CODE, "GBC");
                        headerParams.put(db.KEY_LATITUDE,"0.000000");
                        headerParams.put(db.KEY_LONGITUDE,"0.000000");
                        headerParams.put(db.KEY_TERMS , App.CASH_CUSTOMER_CODE);
                        headerParams.put(db.KEY_TERMS_DESCRIPTION ,App.CASH_CUSTOMER);
                        /************************************************************
                         @ The customer created should become a part of the customer master as well
                         @ so adding the customer in the customer table
                         ************************************************************/
                        db.addData(db.CUSTOMER_HEADER, headerParams);

                        HashMap<String, String> newCustomer = new HashMap<>();
                        newCustomer.put(db.KEY_CUSTOMER_NO,tv_customer_id.getText().toString());
                        newCustomer.put(db.KEY_OWNER_NAME,et_customer_name.getText().toString());
                        newCustomer.put(db.KEY_OWNER_NAME_AR,et_customer_name_ar.getText().toString());
                        newCustomer.put(db.KEY_TRADE_NAME,et_trade_name.getText().toString());
                        newCustomer.put(db.KEY_TRADE_NAME_AR,et_trade_name_ar.getText().toString());
                        newCustomer.put(db.KEY_AREA,et_customer_address1.getText().toString());
                        newCustomer.put(db.KEY_STREET,et_customer_address2.getText().toString());
                        newCustomer.put(db.KEY_CR_NO,et_cr_no.getText().toString());
                        newCustomer.put(db.KEY_PO_BOX,et_customer_pobox.getText().toString());
                        newCustomer.put(db.KEY_EMAIL,et_customer_email.getText().toString());
                        newCustomer.put(db.KEY_TELEPHONE,et_customer_telephone.getText().toString());
                        newCustomer.put(db.KEY_FAX,et_customer_fax.getText().toString());
                        newCustomer.put(db.KEY_SALES_AREA,"1000");
                        newCustomer.put(db.KEY_DISTRIBUTION,distribution);
                        newCustomer.put(db.KEY_DIVISION,division);
                        newCustomer.put(db.KEY_IS_POSTED,App.DATA_MARKED_FOR_POST);
                        newCustomer.put(db.KEY_IS_PRINTED, App.DATA_NOT_POSTED);
                        /************************************************************
                         @ The customer created should also be posted to the backend.
                         @ so adding the customer in the posting table
                         ************************************************************/
                        db.addData(db.NEW_CUSTOMER_POST,newCustomer);

                        /************************************************************
                         @ Checking if there is internet and if yes then create a post job
                         @ in the background using Job Scheduler or Alarm Manager
                         ************************************************************/
                        if (Helpers.isNetworkAvailable(AddCustomerActivity.this)) {
                            Helpers.createBackgroundJob(AddCustomerActivity.this);
                        }
                        Intent intent = new Intent(AddCustomerActivity.this,SelectCustomerActivity.class);
                        startActivity(intent);
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

            }
        });
    }

    private boolean checkNullValues(){
        boolean returnvalue = false;
        try{

            if(et_customer_name.getText().toString().matches("")||
                    et_customer_name_ar.getText().toString().matches("")||
                    et_trade_name.getText().toString().matches("")||
                    et_trade_name_ar.getText().toString().matches("")||
                    et_customer_address1.getText().toString().matches("")||
                    et_customer_address2.getText().toString().matches("")||
                    et_cr_no.getText().toString().matches("")||
                    et_customer_pobox.getText().toString().matches("")||
                    et_customer_email.getText().toString().matches("")||
                    et_customer_telephone.getText().toString().matches("")||
                    et_customer_fax.getText().toString().matches("")||
                    distribution==null||
                    division==null){

            }
            else{
                returnvalue = true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return returnvalue;
    }
    /***************************************************
     @ There are two drop down list for the driver to choose
     @ distribution channel and division.
     ***************************************************/
    void showDialog(String type,String param){
        try{
            if(type.equals("Distribution")){
                final Dialog dialog = new Dialog(AddCustomerActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //dialog.setTitle(getString(R.string.shop_status));
                arrayList.clear();

                //Inserting values hardcoded
                CustomerStatus directDistObj =  new CustomerStatus();
                directDistObj.setReasonCode("20");
                directDistObj.setReasonDescription("Direct Distribution");
                arrayList.add(directDistObj);

                CustomerStatus homeDelObj = new CustomerStatus();
                homeDelObj.setReasonCode("30");
                homeDelObj.setReasonDescription("Home Delivery");
                arrayList.add(homeDelObj);

                adapter = new CustomerStatusAdapter(AddCustomerActivity.this,arrayList);

                View view = getLayoutInflater().inflate(R.layout.activity_select_customer_status, null);
                TextView tv = (TextView) view.findViewById(R.id.tv_top_header);
                tv.setText(getString(R.string.select_category));
                ListView lv = (ListView) view.findViewById(R.id.statusList);
                Button cancel = (Button) view.findViewById(R.id.btnCancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        distribution = arrayList.get(position).getReasonCode();
                        btn_distribution.setText(arrayList.get(position).getReasonDescription());
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(view);
                dialog.setCancelable(false);
                dialog.show();
            }
            else{
                if(distribution.equals("20")){

                    final Dialog dialog = new Dialog(AddCustomerActivity.this);
                    //dialog.setTitle(getString(R.string.shop_status));
                    arrayList.clear();

                    //Inserting values hardcoded

                    CustomerStatus smbObj =  new CustomerStatus();
                    smbObj.setReasonCode("10");
                    smbObj.setReasonDescription("SMB");
                    arrayList.add(smbObj);

                    CustomerStatus miniMarketObj =  new CustomerStatus();
                    miniMarketObj.setReasonCode("15");
                    miniMarketObj.setReasonDescription("Mini Market");
                    arrayList.add(miniMarketObj);

                    CustomerStatus largeGrocery =  new CustomerStatus();
                    largeGrocery.setReasonCode("20");
                    largeGrocery.setReasonDescription("Large Grocery");
                    arrayList.add(largeGrocery);

                    CustomerStatus smallGrocery =  new CustomerStatus();
                    smallGrocery.setReasonCode("25");
                    smallGrocery.setReasonDescription("Small Grocery");
                    arrayList.add(smallGrocery);

                    CustomerStatus buffet =  new CustomerStatus();
                    buffet.setReasonCode("30");
                    buffet.setReasonDescription("Buffet");
                    arrayList.add(buffet);

                    CustomerStatus cafe = new CustomerStatus();
                    cafe.setReasonCode("35");
                    cafe.setReasonDescription("Cafe");
                    arrayList.add(cafe);

                    adapter = new CustomerStatusAdapter(AddCustomerActivity.this,arrayList);

                    View view = getLayoutInflater().inflate(R.layout.activity_select_customer_status, null);
                    TextView tv = (TextView) view.findViewById(R.id.tv_top_header);
                    tv.setText(getString(R.string.select_category));
                    ListView lv = (ListView) view.findViewById(R.id.statusList);
                    Button cancel = (Button) view.findViewById(R.id.btnCancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            division = arrayList.get(position).getReasonCode();
                            btn_division.setText(arrayList.get(position).getReasonDescription());
                            dialog.dismiss();
                        }
                    });
                    dialog.setContentView(view);
                    dialog.setCancelable(false);
                    dialog.show();
                }
                else if(distribution.equals("30")){

                    final Dialog dialog = new Dialog(AddCustomerActivity.this);
                    //dialog.setTitle(getString(R.string.shop_status));
                    arrayList.clear();

                    //Inserting values hardcoded

                    CustomerStatus smbObj =  new CustomerStatus();
                    smbObj.setReasonCode("40");
                    smbObj.setReasonDescription("House");
                    arrayList.add(smbObj);

                    CustomerStatus miniMarketObj =  new CustomerStatus();
                    miniMarketObj.setReasonCode("85");
                    miniMarketObj.setReasonDescription("Schools");
                    arrayList.add(miniMarketObj);

                    CustomerStatus largeGrocery =  new CustomerStatus();
                    largeGrocery.setReasonCode("90");
                    largeGrocery.setReasonDescription("Mosque");
                    arrayList.add(largeGrocery);

                    CustomerStatus smallGrocery =  new CustomerStatus();
                    smallGrocery.setReasonCode("95");
                    smallGrocery.setReasonDescription("Office");
                    arrayList.add(smallGrocery);

                    adapter = new CustomerStatusAdapter(AddCustomerActivity.this,arrayList);

                    View view = getLayoutInflater().inflate(R.layout.activity_select_customer_status, null);
                    TextView tv = (TextView) view.findViewById(R.id.tv_top_header);
                    tv.setText(getString(R.string.select_reason));
                    ListView lv = (ListView) view.findViewById(R.id.statusList);
                    Button cancel = (Button) view.findViewById(R.id.btnCancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            division = arrayList.get(position).getReasonCode();
                            btn_division.setText(arrayList.get(position).getReasonDescription());
                            dialog.dismiss();
                        }
                    });
                    dialog.setContentView(view);
                    dialog.setCancelable(false);
                    dialog.show();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }
}
