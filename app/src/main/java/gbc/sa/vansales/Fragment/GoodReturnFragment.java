package gbc.sa.vansales.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ExpandReturnAdapter;
import gbc.sa.vansales.utils.AnimatedExpandableListView;

/**
 * Created by eheuristic on 12/9/2016.
 */

public class GoodReturnFragment extends Fragment {


    View view;
    TextView tv_ctn_value,tv_ctn_value1;
    TextView tv_btl_value,tv_btl_value1;


    ImageView iv_ctn_minus,iv_ctn_minus1;
    ImageView iv_ctn_add,iv_ctn_add1;
    ImageView iv_btl_minus,iv_btl_minus1;
    ImageView iv_btl_add,iv_btl_add1;


    int tvChangeValue;




    AnimatedExpandableListView exp_list;
    ExpandReturnAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.expand_good_badreturn, container, false);


        exp_list=(AnimatedExpandableListView)view.findViewById(R.id.exp_product);
        String  strProductname[]={"Farya","Brian","Eluga"};
        adapter=new ExpandReturnAdapter(getActivity(),strProductname);
        exp_list.setAdapter(adapter);

        exp_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                return false;
            }
        });

//        tv_ctn_value=(TextView)view.findViewById(R.id.tv_ctn_value);
//        tv_ctn_value1=(TextView)view.findViewById(R.id.tv_ctn_value1);
//        tv_btl_value=(TextView)view.findViewById(R.id.tv_btl_value);
//        tv_btl_value1=(TextView)view.findViewById(R.id.tv_btl_value1);
//
//
//        iv_ctn_minus =(ImageView)view.findViewById(R.id.iv_ctn_minus);
//        iv_ctn_minus1=(ImageView)view.findViewById(R.id.iv_ctn_minus1);
//        iv_ctn_add=(ImageView)view.findViewById(R.id.iv_ctn_add);
//        iv_ctn_add1=(ImageView)view.findViewById(R.id.iv_ctn_add1);
//        iv_btl_minus=(ImageView)view.findViewById(R.id.iv_btl_minus);
//        iv_btl_minus1=(ImageView)view.findViewById(R.id.iv_btl_minus1);
//        iv_btl_add=(ImageView)view.findViewById(R.id.iv_btl_add);
//        iv_btl_add1=(ImageView)view.findViewById(R.id.iv_btl_add1);
//
//
//
//        iv_ctn_minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                tvChangeValue = Integer.parseInt(tv_ctn_value.getText().toString());
//                if(tvChangeValue >0)
//                {
//                    tvChangeValue--;
//                    tv_ctn_value.setText(String.valueOf(tvChangeValue
//                    ));
//                }
//
//            }
//        });
//
//        iv_ctn_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                tvChangeValue = Integer.parseInt(tv_ctn_value.getText().toString());
//                tvChangeValue++;
//                tv_ctn_value.setText(String.valueOf(tvChangeValue));
//            }
//        });
//
//
//
//        iv_ctn_minus1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                tvChangeValue = Integer.parseInt(tv_ctn_value1.getText().toString());
//                if(tvChangeValue >0)
//                {
//                    tvChangeValue--;
//                    tv_ctn_value1.setText(String.valueOf(tvChangeValue
//                    ));
//                }
//
//            }
//        });
//
//        iv_ctn_add1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                tvChangeValue = Integer.parseInt(tv_ctn_value1.getText().toString());
//                tvChangeValue++;
//                tv_ctn_value1.setText(String.valueOf(tvChangeValue));
//            }
//        });
//
//
//        iv_btl_minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                tvChangeValue = Integer.parseInt(tv_btl_value.getText().toString());
//                if(tvChangeValue >0)
//                {
//                    tvChangeValue--;
//                    tv_btl_value.setText(String.valueOf(tvChangeValue
//                    ));
//                }
//
//            }
//        });
//
//        iv_btl_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                tvChangeValue = Integer.parseInt(tv_btl_value.getText().toString());
//                tvChangeValue++;
//                tv_btl_value.setText(String.valueOf(tvChangeValue));
//            }
//        });
//
//
//        iv_btl_minus1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                tvChangeValue = Integer.parseInt(tv_btl_value1.getText().toString());
//                if(tvChangeValue >0)
//                {
//                    tvChangeValue--;
//                    tv_btl_value1.setText(String.valueOf(tvChangeValue
//                    ));
//                }
//
//            }
//        });
//
//        iv_btl_add1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                tvChangeValue = Integer.parseInt(tv_btl_value1.getText().toString());
//                tvChangeValue++;
//                tv_btl_value1.setText(String.valueOf(tvChangeValue));
//            }
//        });
//













        return view;
    }
}
