package gbc.sa.vansales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.Fragment.BadReturnFragment;
import gbc.sa.vansales.Fragment.GoodReturnFragment;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.DeliveryAdapter;
import gbc.sa.vansales.adapters.ExpandReturnAdapter;
import gbc.sa.vansales.models.PreSaleProceed;
import gbc.sa.vansales.utils.AnimatedExpandableListView;

public class PreSaleOrderActivity extends AppCompatActivity {

//    ArrayList<PreSaleProceed> preSaleProceeds = new ArrayList<>();
    ImageView iv_back;
    TextView tv_top_header;
    ListView list_delivery;
    DeliveryAdapter adapter;


//    AnimatedExpandableListView exp_list;
//    public static ExpandReturnAdapter adapter;
//    ArrayList<String> arrProduct;
//    Button btn_proceed;
//    FloatingActionButton btn_float;
//    Bundle  bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_list);

        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);

        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("PreSale Order");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list_delivery=(ListView)findViewById(R.id.list_delivery);

        adapter=new DeliveryAdapter(PreSaleOrderActivity.this,2,R.layout.custom_delivery,"presale");
        list_delivery.setAdapter(adapter);




        list_delivery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(PreSaleOrderActivity.this,PreSaleOrderProceedActivity.class);
                startActivity(intent);

            }
        });








  /*

     btn_proceed=(Button)findViewById(R.id.btn_confirm_delivery_presale_proceed);
        exp_list=(AnimatedExpandableListView)findViewById(R.id.exp_presale);

        arrProduct=new ArrayList<>();


        arrProduct.add("frahn");
        arrProduct.add("bruno");
        arrProduct.add("dwayn");
        bundle = new Bundle();

        btn_float=(FloatingActionButton)findViewById(R.id.float_presale_proceed);
        btn_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent=new Intent(PreSaleOrderActivity.this,ProductListActivity.class);
                startActivityForResult(intent,2,bundle);

            }
        });




        adapter=new ExpandReturnAdapter(PreSaleOrderActivity.this,arrProduct);
        exp_list.setAdapter(adapter);



        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PreSaleOrderActivity.this,PreSaleOrderProceedActivity.class);
                startActivity(intent);
            }
        });*/

    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(data!=null)
//        {
//            if(requestCode==2)
//            {
//                if(resultCode==RESULT_OK)
//                {
//
//                    ArrayList<String> arrproduct=data.getStringArrayListExtra("product");
//                    Log.v("arraylistbefore",arrproduct.size()+"--");
//
//
//                    arrProduct.addAll(arrproduct);
//                    adapter.notifyDataSetChanged();
//
//
//                }
//            }
//        }
//
//
//    }



}
