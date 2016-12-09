package gbc.sa.vansales.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ExpandReturnAdapter;
import gbc.sa.vansales.utils.AnimatedExpandableListView;

/**
 * Created by eheuristic on 12/9/2016.
 */

public class BadReturnFragment  extends Fragment{

    View view;

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




        return view;
    }
}
