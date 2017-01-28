package gbc.sa.vansales.activities;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.Fragment.AllCustomerFragment;
import gbc.sa.vansales.Fragment.VisitAllFragment;
import gbc.sa.vansales.R;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.Settings;
/**
 * Created by Rakshit on 12-Jan-17.
 */
public class AddCustomerActivity extends AppCompatActivity {
    Button addCustomer;
    ImageView iv_back;
    TextView tv_top_header;
    TextView tv_customer_id;
    EditText et_customer_name;
    EditText et_customer_address1;
    EditText et_customer_address2;
    EditText et_customer_pobox;
    EditText et_customer_email;
    EditText et_customer_telephone;
    EditText et_customer_fax;
    TextView tv_customer_route;
    TextView tv_customer_sales_area;

    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        addCustomer = (Button)findViewById(R.id.btn_add_customer);

        tv_customer_id = (TextView)findViewById(R.id.tv_customer_id);
        et_customer_name = (EditText)findViewById(R.id.et_owner_name);
        et_customer_address1 = (EditText)findViewById(R.id.et_customer_address1);
        et_customer_address2 = (EditText)findViewById(R.id.et_customer_address2);
        et_customer_pobox = (EditText)findViewById(R.id.et_customer_pobox);
        et_customer_email = (EditText)findViewById(R.id.et_customer_email);
        et_customer_telephone = (EditText)findViewById(R.id.et_customer_telephone);
        et_customer_fax = (EditText)findViewById(R.id.et_customer_fax);
        tv_customer_route = (TextView)findViewById(R.id.tv_customer_route);
        tv_customer_sales_area = (TextView)findViewById(R.id.tv_customer_sales_area);


        tv_customer_id.setText(Helpers.generateCustomer(db, ConfigStore.CustomerNew_PR_Type));
        tv_customer_route.setText(Settings.getString(App.ROUTE));
        tv_customer_sales_area.setText(Settings.getString(App.SALES_ORG) + "/" + Settings.getString(App.DIST_CHANNEL) + "/" + Settings.getString(App.DIVISION));

        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                db.addData(db.CUSTOMER_HEADER, headerParams);
                /*VisitAllFragment.dataAdapter.notifyDataSetChanged();
                AllCustomerFragment.dataAdapter1.notifyDataSetChanged();
                finish();*/
                Intent intent = new Intent(AddCustomerActivity.this,SelectCustomerActivity.class);
                startActivity(intent);
            }
        });
    }
}
