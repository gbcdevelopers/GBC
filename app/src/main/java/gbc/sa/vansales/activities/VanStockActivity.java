package gbc.sa.vansales.activities;

/**
 * Created by Muhammad Umair on 02/12/2016.
 */
import java.util.ArrayList;
import java.util.Locale;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import gbc.sa.vansales.R;

public class VanStockActivity extends AppCompatActivity {

    // Declare Variables
    ListView list;
    VanStockAdapter adapter;
    String[] vanStcokItem;
    String[] vanStcokCase;
    String[] vanStcokDescription;
    String[] vanStcokUnits;
    Button printVanStock;
    ArrayList<VanStockConstants> arraylist = new ArrayList<VanStockConstants>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanstock);

        setTitle("VanStock Activity");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        printVanStock=(Button)findViewById(R.id.btnPrintVanStock);



        printVanStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(VanStockActivity.this);
                dialog.setContentView(R.layout.activity_print);
                dialog.setCancelable(true);
                RadioButton rd1 = (RadioButton) dialog.findViewById(R.id.rd_1);
                RadioButton rd2 = (RadioButton) dialog.findViewById(R.id.rd_2);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

            }
        });
        // Generate sample data
        vanStcokItem = new String[]{"BOT150CUP", "BOT330PET"};

        vanStcokCase = new String[]{"100", "200"};

        vanStcokDescription = new String[]{"150MLCUPS", "330ML-PET BOTTLES"};

        vanStcokUnits = new String[]{"10", "20"};

        list = (ListView) findViewById(R.id.listview);

        for (int i = 0; i < vanStcokItem.length; i++) {
            VanStockConstants vsc = new VanStockConstants(vanStcokItem[i], vanStcokCase[i],
                    vanStcokDescription[i],vanStcokUnits[i]);
            arraylist.add(vsc);
        }
        adapter = new VanStockAdapter(this, arraylist);
        list.setAdapter(adapter);


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
}