package gbc.sa.vansales.activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.LoadRequestBadgeAdapter;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Muhammad Umair on 02/12/2016.
 */
public class LoadRequestActivity extends AppCompatActivity {
    Button processLoadRequest;
    ListView list;
    LoadRequestBadgeAdapter adapter;
    EditText editsearch;
    ImageButton datepickerdialogbutton;
    TextView selecteddate;
    ArrayList<LoadRequest> arraylist = new ArrayList<>();
    DatabaseHandler db = new DatabaseHandler(this);
    public ArrayList<ArticleHeader> articles;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_request);
        setTitle(getString(R.string.loadrequest));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        articles = ArticleHeaders.get();
        new loadItems();
        datepickerdialogbutton = (ImageButton) findViewById(R.id.btnDate);
        selecteddate = (TextView) findViewById(R.id.tv1);
        processLoadRequest = (Button) findViewById(R.id.btnProcess);
        // Generate sample data
        datepickerdialogbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // setContentView(R.layout.activity_load_request);
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getFragmentManager(), "Please Select Your Date");
            }
        });
        processLoadRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new postData().execute();
                /*setTitle("Print Activity");
                Dialog dialog = new Dialog(LoadRequestActivity.this);
                dialog.setContentView(R.layout.activity_print);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();*/
            }
        });
        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);
        adapter = new LoadRequestBadgeAdapter(this, arraylist);
        list.setAdapter(adapter);
        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);
        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.getFilter().filter(text);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void postData(){

        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("Function", ConfigStore.LoadRequestFunction);
            map.put("OrderId", "");
            map.put("DocumentType", ConfigStore.DocumentType);
            map.put("DocumentDate", "20161229");
            map.put("CustomerId", "E200");
            map.put("SalesOrg", "1000");
            map.put("DistChannel", "10");
            map.put("Division", "10");
            map.put("OrderValue", "100");
            map.put("Currency", "SAR");
            map.put("PurchaseNum", "3010500001");
            JSONArray deepEntity = new JSONArray();
            JSONObject jo = new JSONObject();
            jo.put("Item", "0010");
            jo.put("Material", "000000000014020151");
            jo.put("Description", "Shrink Pad Berain Krones");
            jo.put("Plant", "");
            jo.put("Quantity", "2");
            jo.put("ItemValue", "23");
            jo.put("UoM", "CAR");
            jo.put("Value", "12");
            jo.put("Storagelocation", "");
            jo.put("Route", "GBC01");
            deepEntity.put(jo);
            JSONObject jo1 = new JSONObject();
            jo1.put("Item", "0020");
            jo1.put("Material", "000000000014020077");
            jo1.put("Description", "CRTON Fayha");
            jo1.put("Plant", "");
            jo1.put("Quantity", "2");
            jo1.put("ItemValue", "24");
            jo1.put("UoM", "CAR");
            jo1.put("Value", "12");
            jo1.put("Storagelocation", "");
            jo1.put("Route", "GBC01");

            deepEntity.put(jo1);

            IntegrationService.postData(LoadRequestActivity.this, App.POST_COLLECTION, map, deepEntity);
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
    public static class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day);
            return datepickerdialog;
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView textview = (TextView) getActivity().findViewById(R.id.tv1);
            textview.setText(day + "/" + (month + 1) + "/" + year);
        }
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
    public class loadItems extends AsyncTask<Void,Void,Void>{

        private loadItems() {
            execute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String> map = new HashMap<>();
            map.put(db.KEY_TRIP_ID,"");
            map.put(db.KEY_MATERIAL_GROUPA_DESC,"");
            map.put(db.KEY_MATERIAL_GROUPB_DESC,"");
            map.put(db.KEY_MATERIAL_DESC2,"");
            map.put(db.KEY_BATCH_MANAGEMENT,"");
            map.put(db.KEY_PRODUCT_HIERARCHY,"");
            map.put(db.KEY_VOLUME_UOM,"");
            map.put(db.KEY_VOLUME,"");
            map.put(db.KEY_WEIGHT_UOM,"");
            map.put(db.KEY_NET_WEIGHT,"");
            map.put(db.KEY_GROSS_WEIGHT,"");
            map.put(db.KEY_ARTICLE_CATEGORY,"");
            map.put(db.KEY_ARTICLE_NO,"");
            map.put(db.KEY_BASE_UOM,"");
            map.put(db.KEY_MATERIAL_GROUP,"");
            map.put(db.KEY_MATERIAL_TYPE,"");
            map.put(db.KEY_MATERIAL_DESC1,"");
            map.put(db.KEY_MATERIAL_NO,"");

            HashMap<String,String> filter = new HashMap<>();
            Cursor cursor = db.getData(db.ARTICLE_HEADER,map,filter);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                setLoadItems(cursor);
            }
            return null;
        }
    }

    public void setLoadItems(Cursor loadItemsCursor){
        Cursor cursor = loadItemsCursor;

        do{
            LoadRequest loadRequest = new LoadRequest();
            loadRequest.setItemCode(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
            loadRequest.setItemName(UrlBuilder.decodeString(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1))));
            loadRequest.setCases(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.CASE_UOM) ? "0" : "0");
            loadRequest.setUnits(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)).equals(App.BOTTLES_UOM) ? "0" : "0");
            loadRequest.setMaterialNo(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));
            arraylist.add(loadRequest);

        }
        while (cursor.moveToNext());

        adapter.notifyDataSetChanged();
    }

    public class postData extends AsyncTask<Void, Void, Void>{
        private ArrayList<String>returnList;

        @Override
        protected Void doInBackground(Void... params) {
            //this.returnList = IntegrationService.RequestToken(LoadRequestActivity.this);
            postData();
            return null;
        }

    }
}
