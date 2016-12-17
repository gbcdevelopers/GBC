package gbc.sa.vansales.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import gbc.sa.vansales.R;

/**
 * Created by Muhammad Umair on 02/12/2016.
 */


public class LoadRequestActivity extends AppCompatActivity
{

    Button processLoadRequest;
    ListView list;
    LoadRequestAdapter adapter;
    EditText editsearch;
    String[] itemName;
    String[] category;
    String[] cases;
    String[] units;
    int[] categoryImage;


    ImageButton datepickerdialogbutton;
    TextView selecteddate;

    ArrayList<LoadRequestConstants> arraylist = new ArrayList<LoadRequestConstants>();

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_request);

        setTitle("Load Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        datepickerdialogbutton = (ImageButton)findViewById(R.id.btnDate);
        selecteddate = (TextView)findViewById(R.id.tv1);


        processLoadRequest=(Button)findViewById(R.id.btnProcess);


        // Generate sample data
        itemName = new String[] { "Berain_Regular", "Berain_Half_Liter", "Berain_1.5_Liter" };

        category = new String[] { "Regular", "Half Liter", "1.5 Liter"};

        cases = new String[] { "10", "20","30"};

        units = new String[] { "100", "200", "300"};

        categoryImage = new int[] { R.drawable.beraincategory, R.drawable.beraincategory,
                R.drawable.beraincategory};



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
//                Intent i =new Intent(LoadRequestActivity.this,PrintActivity.class);
//                startActivity(i);

                setTitle("Print Activity");

                Dialog dialog = new Dialog(LoadRequestActivity.this);
                dialog.setContentView(R.layout.activity_print);
                dialog.setCancelable(true);
                RadioButton rd1 = (RadioButton) dialog.findViewById(R.id.rd_1);
                RadioButton rd2 = (RadioButton) dialog.findViewById(R.id.rd_2);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });




        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        for (int i = 0; i < itemName.length; i++)
        {
            LoadRequestConstants lrc = new LoadRequestConstants(itemName[i], category[i],
                    cases[i],units[i], categoryImage[i]);
            // Binds all strings into an array
            arraylist.add(lrc);
        }

        // Pass results to ListViewAdapter Class
        adapter = new LoadRequestAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
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

    public static class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,this,year,month,day);

            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){

            TextView textview = (TextView)getActivity().findViewById(R.id.tv1);

            textview.setText(day + "/" + (month+1) + "/" + year);

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

}
