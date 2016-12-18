package gbc.sa.vansales.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gbc.sa.vansales.R;

public class ManageInventory extends AppCompatActivity
{
    Button load,loadRequest, unload, vanStock;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_inventery);

        setTitle(R.string.manage_inventory);

        load=(Button)findViewById(R.id.btnLoad);
        loadRequest=(Button)findViewById(R.id.btnLoadRequest);
        vanStock=(Button)findViewById(R.id.btnVanStock);
        unload=(Button)findViewById(R.id.btnUnLoad);


        load.setBackgroundColor(Color.RED);
        load.setTextColor(Color.WHITE);

        loadRequest.setBackgroundColor(Color.GREEN);
        loadRequest.setTextColor(Color.WHITE);

        vanStock.setBackgroundColor(Color.BLUE);
        vanStock.setTextColor(Color.WHITE);

        unload.setBackgroundColor(Color.YELLOW);
        unload.setTextColor(Color.WHITE);

        loadRequest.setEnabled(false);
        vanStock.setEnabled(false);
        unload.setEnabled(false);

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Load Popup", Toast.LENGTH_LONG).show();

                Intent i =new Intent(ManageInventory.this,LoadActivity.class);
                startActivity(i);
            }
        });

        loadRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i =new Intent(ManageInventory.this,LoadRequestActivity.class);
                startActivity(i);


            }
        });

        vanStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), "VanStock Popup", Toast.LENGTH_LONG).show();

                Intent i=new Intent(ManageInventory.this,VanStockActivity.class);
                startActivity(i);


            }
        });

        unload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "Unload Popup", Toast.LENGTH_LONG).show();
                Intent i=new Intent(ManageInventory.this,UnloadActivity.class);
                startActivity(i);
            }
        });
    }
}
