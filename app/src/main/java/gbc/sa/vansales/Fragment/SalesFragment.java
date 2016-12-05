package gbc.sa.vansales.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.SalesAdapter;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class SalesFragment extends Fragment {


    View view;
    ListView listSales;
    SalesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_salesinvoice,container,false);
        listSales=(ListView)view.findViewById(R.id.list_sales);

        adapter=new SalesAdapter(getActivity(),3,R.layout.sales_list);
        listSales.setAdapter(adapter);


        return view;
    }
}
