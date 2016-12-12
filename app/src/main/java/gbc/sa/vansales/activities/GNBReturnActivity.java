package gbc.sa.vansales.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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
    TextView tv_goodbad;

    Fragment fragment;

    ImageView iv_good;

    FloatingActionButton btn_float;
    ArrayList<String> arrBad;
    ArrayList<String> arrGood;
    Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnbreturn);

        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);

        tv_goodbad=(TextView)findViewById(R.id.tv_goodbad);


        iv_good=(ImageView)findViewById(R.id.iv_goodbad);


        btn_float=(FloatingActionButton)findViewById(R.id.btn_float);


        arrBad=new ArrayList<>();
        arrBad.add("frahn");
        arrBad.add("brian");
        arrBad.add("eluga");

        arrGood=new ArrayList<>();
        arrGood.add("frahn");
        arrGood.add("brian");
        arrGood.add("eluga");


        bundle = new Bundle();
        bundle.putStringArrayList("bad",arrBad);
        bundle.putStringArrayList("good",arrGood);

        if(getIntent().getExtras()!=null)
        {
            fromActivity=getIntent().getExtras().getString("from","");
            if(fromActivity.equals("g"))
            {
                tv_top_header.setText("Good Return");
                tv_goodbad.setText("Good Return");


                bundle.putString("from","g");
                fragment=new GoodReturnFragment();
                fragment.setArguments(bundle);
            }
            else if(fromActivity.equals("b"))
            {
                tv_top_header.setText("Bad Return");
                tv_goodbad.setText("Bad Return");


                bundle.putString("from","b");
                fragment=new BadReturnFragment();
                fragment.setArguments(bundle);
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


                Intent intent=new Intent(GNBReturnActivity.this,ProductListActivity.class);
                startActivityForResult(intent,1,bundle);
            }
        });









    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null)
        {
            if(requestCode==1)
            {
                if(resultCode==RESULT_OK)
                {


                    ArrayList<String> arrproduct=data.getStringArrayListExtra("product");
                    Log.v("arraylistbefore",arrproduct.size()+"--");


                    if(fromActivity.equals("g")) {
                        arrGood.addAll(arrproduct);
                        bundle.putStringArrayList("good",arrGood);
                        GoodReturnFragment.adapter.notifyDataSetChanged();
                    }else {
                        arrBad.addAll(arrproduct);
                        bundle.putStringArrayList("bad",arrBad);
                        BadReturnFragment.adapter.notifyDataSetChanged();
                    }

                }
            }
        }


    }
}
