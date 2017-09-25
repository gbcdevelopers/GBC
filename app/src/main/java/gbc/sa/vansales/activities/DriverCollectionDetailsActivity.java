package gbc.sa.vansales.activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.BankAdapter;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.Banks;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.Bank;
import gbc.sa.vansales.models.Collection;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Rakshit on 21-Feb-17.
 */
public class DriverCollectionDetailsActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_top_header;
    TextView tv_due_amt, tv_total_amount, tv_date;
    ImageView iv_cal;
    EditText edt_check_no, edt_check_amt, edt_cash_amt;

    CheckBox cb_cashPayment;
    CheckBox cb_chequePayment;
    LinearLayout ll_cash_payment;
    LinearLayout ll_cheque_payment;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    String from = "";
    String amountdue = "0";
    Spinner sp_item;
    private ArrayList<Bank> banksList = new ArrayList<>();
    ArrayAdapter<Bank> bankAdapter;
    String bankcode;
    String bankname;
    boolean allowPartial = false;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details_new);
        Banks.loadData(this);
        tv_due_amt = (TextView) findViewById(R.id.tv_payment__amout_due_number);
        Intent i = this.getIntent();
        cb_cashPayment = (CheckBox) findViewById(R.id.cb_cash);
        cb_chequePayment = (CheckBox) findViewById(R.id.cb_cheque);
        ll_cash_payment = (LinearLayout) findViewById(R.id.ll_cash_payment);
        ll_cheque_payment = (LinearLayout) findViewById(R.id.ll_cheque_payment);
        sp_item = (Spinner) findViewById(R.id.sp_item);
        sp_item.setEnabled(true);

        cb_chequePayment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_chequePayment.isChecked()) {
                    ll_cheque_payment.setVisibility(View.VISIBLE);
                } else {
                    ll_cheque_payment.setVisibility(View.GONE);
                }
            }
        });
        cb_cashPayment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_cashPayment.isChecked()) {
                    ll_cash_payment.setVisibility(View.VISIBLE);
                } else {
                    ll_cash_payment.setVisibility(View.GONE);
                }
            }
        });
        loadBanks();
        if (getIntent().getExtras() != null) {
            from = getIntent().getStringExtra("from");
            if (from.equals("driver")) {
                amountdue = getIntent().getStringExtra("amountdue");
                tv_due_amt.setText(amountdue);
            }
        }
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.payment_details));
    }

    private void loadBanks() {
        Banks.loadData(DriverCollectionDetailsActivity.this);
        banksList = Banks.get();
        //Log.e("Bank List", "" + banksList.size());
        bankAdapter = new BankAdapter(this, android.R.layout.simple_spinner_item, banksList);
        bankAdapter.notifyDataSetChanged();
        sp_item.setAdapter(bankAdapter);
    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }
}
