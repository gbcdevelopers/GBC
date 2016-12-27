package gbc.sa.vansales.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.CategoryListActivity;
import gbc.sa.vansales.adapters.ExpandReturnAdapter;
import gbc.sa.vansales.utils.AnimatedExpandableListView;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class GListFragment extends Fragment {


    public static ExpandReturnAdapter adapter;
    public static ArrayList<String> arrProductList;
    View view;
    FloatingActionButton btn_float;

    AnimatedExpandableListView exp_list;
   public static RelativeLayout rl_middle;







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.activity_gnbreturn,container,false);
        btn_float=(FloatingActionButton)view.findViewById(R.id.fab);
        exp_list=(AnimatedExpandableListView)view.findViewById(R.id.exp_product);
        rl_middle=(RelativeLayout)view.findViewById(R.id.rl_middle);

        arrProductList=new ArrayList<>();


        if(arrProductList.size()==0)
        {
            rl_middle.setVisibility(View.VISIBLE);
        }else {
            rl_middle.setVisibility(View.GONE);
        }

        adapter=new ExpandReturnAdapter(getActivity(),arrProductList,exp_list);
        exp_list.setAdapter(adapter);

        btn_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), CategoryListActivity.class);
                getActivity().startActivity(intent);
            }
        });

       exp_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
           @Override
           public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
               return true;
           }
       });

        return view;
    }
}
