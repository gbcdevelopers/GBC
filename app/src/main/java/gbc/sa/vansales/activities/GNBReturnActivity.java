package gbc.sa.vansales.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import gbc.sa.vansales.Fragment.BadReturnFragment;
import gbc.sa.vansales.Fragment.GoodReturnFragment;
import gbc.sa.vansales.R;

/**
 * Created by eheuristic on 12/8/2016.
 */

public class GNBReturnActivity extends AppCompatActivity {


    ImageView iv_back;
    TextView tv_top_header;
    String fromActivity="";
    TextView tv_good,tv_bad;

    Fragment fragment;

    ImageView iv_good,iv_bad;

    FloatingActionButton btn_float;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnbreturn);

        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);

        tv_good=(TextView)findViewById(R.id.tv_good);
        tv_bad=(TextView)findViewById(R.id.tv_bad);

        iv_good=(ImageView)findViewById(R.id.iv_good);
        iv_bad=(ImageView)findViewById(R.id.iv_bad);

        btn_float=(FloatingActionButton)findViewById(R.id.btn_float);


        if(getIntent().getExtras()!=null)
        {
            fromActivity=getIntent().getExtras().getString("from","");
            if(fromActivity.equals("g"))
            {
                tv_top_header.setText("Good Return");
                tv_good.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv_bad.setBackground(getResources().getDrawable(R.drawable.gray_border));
                iv_bad.setVisibility(View.GONE);
                iv_good.setVisibility(View.VISIBLE);
                tv_good.setTextColor(Color.WHITE);
                tv_bad.setTextColor(getResources().getColor(R.color.gray));



                fragment=new GoodReturnFragment();
            }
            else if(fromActivity.equals("b"))
            {
                tv_top_header.setText("Bad Return");
                tv_good.setBackground(getResources().getDrawable(R.drawable.gray_border));
                tv_bad.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                fragment=new BadReturnFragment();
                iv_bad.setVisibility(View.VISIBLE);
                iv_good.setVisibility(View.GONE);
                tv_bad.setTextColor(Color.WHITE);
                tv_good.setTextColor(getResources().getColor(R.color.gray));
            }
            else {
                tv_top_header.setText("Sales Invoice");
            }
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();






        btn_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });









        tv_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_good.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv_bad.setBackground(getResources().getDrawable(R.drawable.gray_border));
                tv_top_header.setText("Good Return");

                iv_bad.setVisibility(View.GONE);
                iv_good.setVisibility(View.VISIBLE);
                tv_good.setTextColor(Color.WHITE);
                tv_bad.setTextColor(getResources().getColor(R.color.gray));


                fragment=new GoodReturnFragment();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.container,fragment);

                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        tv_bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_good.setBackground(getResources().getDrawable(R.drawable.gray_border));
                tv_bad.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv_top_header.setText("Bad Return");

                iv_bad.setVisibility(View.VISIBLE);
                iv_good.setVisibility(View.GONE);
                tv_bad.setTextColor(Color.WHITE);
                tv_good.setTextColor(getResources().getColor(R.color.gray));

                fragment=new BadReturnFragment();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.container,fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });




    }
}
