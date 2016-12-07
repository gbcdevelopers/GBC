package gbc.sa.vansales.activities;

/**
 * Created by Muhammad Umair on 05/12/2016.
 */

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
//                Intent i =new Intent(LoadRequestActivity.this,PrintActivity.class);
//                startActivity(i);

                setTitle("Print Activity");

                Dialog dialog = new Dialog(UnloadActivity.this);
                dialog.setContentView(R.layout.activity_print);
                dialog.setTitle("Print Dialog");
                dialog.setCancelable(true);
                RadioButton rd1 = (RadioButton) dialog.findViewById(R.id.rd_1);
                RadioButton rd2 = (RadioButton) dialog.findViewById(R.id.rd_2);
                dialog.show();

            }
        });


    }
}