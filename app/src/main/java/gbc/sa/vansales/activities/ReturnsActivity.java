package gbc.sa.vansales.activities;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.GoodReturnBadgeAdapter;
import gbc.sa.vansales.adapters.ShopStatusBadgeAdapter;
import gbc.sa.vansales.adapters.StockTakeBadgeAdapter;
import gbc.sa.vansales.models.GoodReturn;
import gbc.sa.vansales.models.Product;
import gbc.sa.vansales.models.ShopStatus;
/**
 * Created by Rakshit on 21-Nov-16.
 */
public class ReturnsActivity extends AppCompatActivity {

    private ArrayList<GoodReturn> returnList;
    private ArrayList<GoodReturn> itemDropDownList;
    private ArrayAdapter<GoodReturn> adapter;
    private Spinner itemListDropdown;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returns);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        returnList = new ArrayList<>();
        itemDropDownList = new ArrayList<>();
        list = new ArrayList<String>();
        adapter = new GoodReturnBadgeAdapter(this, returnList);

        ListView listView = (ListView) findViewById(R.id.returnList);
        listView.setAdapter(adapter);
        itemListDropdown = (Spinner)findViewById(R.id.item_dropdown);

        new loadDropdownData().execute();

        itemListDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showChangeDialog(position);
                /*if (position == 0) {
                } else {
                    Log.e("Select", "" + itemDropDownList.get(position - 1).getItemDescription());
                    showDialog(position);
                }*/
                //Log.e("Select",""+itemDropDownList.get(position-1).getItemDescription());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btn_confirm_returns = (Button)findViewById(R.id.btn_confirm_returns);
        Button btn_cancel = (Button)findViewById(R.id.btn_cancel);

        btn_confirm_returns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReturnsActivity.this,SelectOperationActivity.class);
                startActivity(intent);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReturnsActivity.this,SelectOperationActivity.class);
                startActivity(intent);
            }
        });

    }

    void showChangeDialog(int position){

        GoodReturn item = new GoodReturn();
        final Dialog dialog = new Dialog(ReturnsActivity.this);
        dialog.setContentView(R.layout.alert_good_return);

        Button btn_add = (Button)dialog.findViewById(R.id.btn_add_item_return);
        Button btn_cancel = (Button)dialog.findViewById(R.id.btn_cancel_item_return);

        final TextView tv_item_code = (TextView)dialog.findViewById(R.id.lbl_item_code);
        final TextView tv_item_description = (TextView)dialog.findViewById(R.id.lbl_item_description);
        final EditText et_quantity_cs = (EditText)dialog.findViewById(R.id.et_quantitycs_return);
        final EditText et_quantity_bt = (EditText)dialog.findViewById(R.id.et_quantitybt_return);
        final RadioGroup radioGroup = (RadioGroup)dialog.findViewById(R.id.radioGroup);
        RadioButton btn_good_return = (RadioButton)dialog.findViewById(R.id.btn_radio_good_return);
        RadioButton btn_bad_return = (RadioButton)dialog.findViewById(R.id.btn_radio_bad_return);
        final EditText et_return_reason = (EditText)dialog.findViewById(R.id.et_return_reason);

        if(itemDropDownList.size()>position){
            if(position>0) {
                item = itemDropDownList.get(position);
                tv_item_code.setText(item.getItemSKU());
                tv_item_description.setText(item.getItemDescription());
            }
        }
        else{
            tv_item_code.setText("BA0001");
            tv_item_description.setText("Test");
        }
        if(position==0){

        }
        else{
            dialog.show();
        }
        final GoodReturn finalItem = item;
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalItem.setItemSKU(tv_item_code.getText().toString());
                finalItem.setItemDescription(tv_item_description.getText().toString());
                finalItem.setQuantityCS(et_quantity_cs.getText().toString());
                finalItem.setQuantityBT(et_quantity_bt.getText().toString());
                finalItem.setReason(et_return_reason.getText().toString());
                int selectedID = radioGroup.getCheckedRadioButtonId();
                RadioButton btn = (RadioButton) dialog.findViewById(selectedID);
                finalItem.setReturnType(btn.getText().toString());
                addItemstoList(finalItem);
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    void addItemstoList(GoodReturn item){
        itemListDropdown.setSelection(0);
        returnList.add(item);
        adapter.notifyDataSetChanged();
    }

    void generateDropdownData(){
        for(int i=0;i<12;i++){
            GoodReturn item = addItemsDropdown(i);
            itemDropDownList.add(item);
        }
        //Log.e("List size1 is", "" + itemDropDownList.size());

    }

    public GoodReturn addItemsDropdown(int index){
        GoodReturn item = new GoodReturn();
        switch (index){
            case 1:
                item.setItemSKU("BA150BOT");
                list.add("150 ML - Cups");
                item.setItemDescription("150 ML - Cups");
                break;
            case 2:
                item.setItemSKU("BA250BOT");
                list.add("250 ML - Cups");
                item.setItemDescription("250 ML - Cups");
                break;
            case 3:
                item.setItemSKU("BA330PET");
                list.add("330 ML - PET Bottles");
                item.setItemDescription("330 ML - PET Bottles");
                break;
            case 4:
                item.setItemSKU("BA500PET");
                list.add("500 ML - PET Bottles");
                item.setItemDescription("500 ML - PET Bottles");
                break;
            case 5:
                item.setItemSKU("BA600PET");
                list.add("600 ML - PET Bottles");
                item.setItemDescription("600 ML - PET Bottles");
                break;
            case 6:
                item.setItemSKU("BA650PET");
                list.add("650 ML - PET Bottles");
                item.setItemDescription("650 ML - PET Bottles");
                break;
            case 7:
                item.setItemSKU("BA1500PET");
                list.add("1.5 L -  PET Bottles");
                item.setItemDescription("1.5 L -  PET Bottles");
                break;
            case 8:
                item.setItemSKU("BA1890PET");
                list.add("1.89L -  PET Bottles");
                item.setItemDescription("1.89L -  PET Bottles");
                break;
            case 9:
                item.setItemSKU("BA3890PET");
                list.add("3.89L -  PET Bottles");
                item.setItemDescription("3.89L -  PET Bottles");
                break;
            case 10:
                item.setItemSKU("BA4GB");
                list.add("4 Gallon Bottles");
                item.setItemDescription("4 Gallon Bottles");
                break;
            case 11:
                item.setItemSKU("BA5GB");
                list.add("5 Gallon Bottles");
                item.setItemDescription("5 Gallon Bottles");
                break;
            }
            return item;

    }
    public void updateAdapter() {
        adapter.notifyDataSetChanged();//update adapter
    }
    private class loadDropdownData extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            //Logic to fetch Data
            list.add("Select Item");
            generateDropdownData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
           // List<String> list = new ArrayList<String>();
            // list.add("list 1");

            //Log.e("List size is",""+list.size());
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ReturnsActivity.this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            itemListDropdown.setAdapter(dataAdapter);
            // super.onPostExecute(aVoid);
        }
    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }
}
