package gbc.sa.vansales.activities;

/**
 * Created by Muhammad Umair on 05/12/2016.
 */

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import gbc.sa.vansales.R;


public class UnloadActivity extends AppCompatActivity {

    GridView gridView;
    Button processUnloadInventory;

    static final String[] badReturnitems = new String[] {
            "Bad Return","Truck Damage","Fresh Unload","Ending Inventory","INV. Variance","Bad RTN. Variance"};



    @Override
    public void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unload);

        setTitle("Unload Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gridView = (GridView) findViewById(R.id.gridView1);

        processUnloadInventory=(Button)findViewById(R.id.btnUnloadInventory);
        gridView.setAdapter(new UnloadAdapter(this, badReturnitems));


        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        ((TextView) v.findViewById(R.id.badReturnLabel))
                                .getText(), Toast.LENGTH_SHORT).show();

            }
        });

        processUnloadInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(UnloadActivity.this);
                dialog.setContentView(R.layout.activity_print);
                dialog.setCancelable(true);
                Button print = (Button) dialog.findViewById(R.id.btnPrint);
                Button cancel = (Button) dialog.findViewById(R.id.btnCancel2);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();



                print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UnloadActivity.this,MyCalendarActivity.class);
                        startActivity(intent);


                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(UnloadActivity.this,MyCalendarActivity.class);
                        startActivity(intent);


                    }
                });


            }
        });

    }

    private void navigation()
    {
        Intent i=new Intent(UnloadActivity.this, ManageInventory.class);
        startActivity(i);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}