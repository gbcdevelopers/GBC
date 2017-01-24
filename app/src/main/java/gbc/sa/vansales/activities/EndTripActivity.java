package gbc.sa.vansales.activities;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ExpenseAdapter;
import gbc.sa.vansales.models.Expense;
import gbc.sa.vansales.utils.AnimatedExpandableListView;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
/**
 * Created by eheuristic on 12/10/2016.
 */
public class EndTripActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    FloatingActionButton btn_float;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    ArrayList<Expense> arrayList = new ArrayList<>();
    ArrayAdapter<Expense>adapter;
    float chequeTotal = 0;
    float cashTotal = 0;
    TextView tv_cheque_amnt;
    TextView tv_cash_amnt;
    TextView tv_total_amount;
    TextView tv_due_amount;
    LinearLayout ll_add_expense;
    ListView expenseListView;
    Typeface typeface;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_trip);
        loadingSpinner = new LoadingSpinner(this);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        btn_float = (FloatingActionButton) findViewById(R.id.btn_float);
        ll_add_expense = (LinearLayout)findViewById(R.id.add_expense);
        expenseListView = (ListView)findViewById(R.id.expenseListView);
        tv_cheque_amnt = (TextView)findViewById(R.id.tv_cheque_amnt);
        tv_cash_amnt = (TextView)findViewById(R.id.tv_cash_amnt);
        tv_total_amount = (TextView)findViewById(R.id.tv_total_amount);
        tv_due_amount = (TextView)findViewById(R.id.tv_due_amount);
        adapter = new ExpenseAdapter(this,arrayList);
        expenseListView.setAdapter(adapter);
        btn_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndTripActivity.this, PrinterReportsActivity.class);
                startActivity(intent);
            }
        });
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("End Trip");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_add_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        new loadCollectionData().execute();
        calculateDueAmount();
    }

    public class loadCollectionData extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            loadingSpinner.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String,String>map = new HashMap<>();
            map.put(db.KEY_CUSTOMER_NO,"");
            map.put(db.KEY_INVOICE_NO,"");
            map.put(db.KEY_INVOICE_AMOUNT,"");
            map.put(db.KEY_DUE_DATE,"");
            map.put(db.KEY_INVOICE_DATE,"");
            map.put(db.KEY_AMOUNT_CLEARED,"");
            map.put(db.KEY_CASH_AMOUNT,"");
            map.put(db.KEY_CHEQUE_AMOUNT,"");
            map.put(db.KEY_CHEQUE_NUMBER,"");
            map.put(db.KEY_IS_INVOICE_COMPLETE,"");
            HashMap<String,String>filter = new HashMap<>();
            Cursor c = db.getData(db.COLLECTION,map,filter);
            if(c.getCount()>0){
                c.moveToFirst();
                setCollection(c);
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            startCountAnimation(tv_cheque_amnt, (int) chequeTotal);
            startCountAnimation(tv_cash_amnt, (int) cashTotal);
            startCountAnimation(tv_total_amount, (int) (chequeTotal+cashTotal));
        }
    }
    private void setCollection(Cursor cursor){
        Cursor c = cursor;
        do {
            chequeTotal+=Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CHEQUE_AMOUNT)));
            cashTotal+=Float.parseFloat(c.getString(c.getColumnIndex(db.KEY_CASH_AMOUNT)));
        }
        while(c.moveToNext());
    }
    private void startCountAnimation(final TextView element,Integer value) {

        final Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/counterFont.ttf");
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, value);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                element.setText("" + (int) animation.getAnimatedValue());
                //element.setTypeface(custom_font);
            }
        });
        animator.start();
    }
    private void showDialog(){
        final Dialog dialog = new Dialog(EndTripActivity.this);
        View view = getLayoutInflater().inflate(R.layout.activity_add_expense, null);
        Button cancel = (Button)view.findViewById(R.id.btn_cancel);
        Button save = (Button)view.findViewById(R.id.btn_ok);
        final EditText expAmount = (EditText)view.findViewById(R.id.expAmount);
        final EditText reasons = (EditText)view.findViewById(R.id.reason);
        final String[] reasonSelected = {""};

        Spinner expenseSpinner = (Spinner)view.findViewById(R.id.expenseCategory);
        final List<String> list = new ArrayList<String>();
        list.add("Select Expense");
        list.add("Fuel");
        list.add("Food");
        list.add("Theft");
        list.add("Traffic Fine");
        list.add("Other");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseSpinner.setAdapter(dataAdapter);

        expenseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reasonSelected[0] = list.get(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Expense expense = new Expense();
                expense.setExpenseCode("00");
                expense.setExpenseDescription(reasonSelected[0].toString());
                expense.setAdditionalReason(reasons.getText().toString());
                expense.setExpenseAmount(expAmount.getText().toString());
                arrayList.add(expense);
                adapter.notifyDataSetChanged();
                calculateDueAmount();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void calculateDueAmount(){
        float dueAmount = 0;
        for(Expense expense:arrayList){
            dueAmount+= Float.parseFloat(expense.getExpenseAmount());
        }
        tv_due_amount.setText(String.valueOf(dueAmount));
    }

}
