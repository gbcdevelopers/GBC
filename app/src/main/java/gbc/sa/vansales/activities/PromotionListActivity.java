package gbc.sa.vansales.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.SalesAdapter;
import gbc.sa.vansales.adapters.SwipeDetector;

/**
 * Created by eheuristic on 12/7/2016.
 */

public class PromotionListActivity extends AppCompatActivity {

    ListView list_promotion;
    SalesAdapter adapter;


    ImageView iv_back;
    TextView tv_top_header;
    Button btn_apply;

    ArrayList<String> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_promotionlist);
        list_promotion = (ListView) findViewById(R.id.list_promotion);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        btn_apply = (Button) findViewById(R.id.btn_apply);

        tv_top_header = (TextView) findViewById(R.id.tv_top_header);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Promotion List");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PromotionListActivity.this);
                builder.setTitle("Promotion list");
                builder.setMessage("Applied Successfully");
                builder.setCancelable(true);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });

        arrayList=new ArrayList<>();
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("c");

        adapter = new SalesAdapter(PromotionListActivity.this, arrayList, R.layout.custom_promotionlist);
        list_promotion.setAdapter(adapter);

      /*  list_promotion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/

      final SwipeDetector swipeDetector=new SwipeDetector();


        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                if (swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == SwipeDetector.Action.RL) {


                        TextView tv = (TextView) view.findViewById(R.id.tv_promotion);
                        String str_promotion_message = tv.getText().toString();


                        Intent intent = new Intent(PromotionListActivity.this, PromotionActivity.class);
                        intent.putExtra("msg", str_promotion_message);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


                    } else {

                    }
                }
            }

        };


        list_promotion.setOnTouchListener(swipeDetector);
        list_promotion.setOnItemClickListener(listener);

    }
}