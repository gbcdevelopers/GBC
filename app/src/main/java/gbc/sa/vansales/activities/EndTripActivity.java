package gbc.sa.vansales.activities;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import gbc.sa.vansales.R;
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
    float chequeTotal = 0;
    float cashTotal = 0;
    EditText tv_cheque_amnt;
    EditText tv_cash_amnt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_trip);
        loadingSpinner = new LoadingSpinner(this);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        btn_float = (FloatingActionButton) findViewById(R.id.btn_float);
        tv_cheque_amnt = (EditText)findViewById(R.id.tv_cheque_amnt);
        tv_cash_amnt = (EditText)findViewById(R.id.tv_cash_amnt);
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
        new loadCollectionData().execute();

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

    private void startCountAnimation(final EditText element,Integer value) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, value);
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                element.setText("" + (int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }
}
