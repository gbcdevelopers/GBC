package gbc.sa.vansales.Fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ProductListAdapter;

/**
 * Created by eheuristic on 12/21/2016.
 */

public class ProductFragment extends Fragment {



   ListView list_product;
    View view;
    View common_header;
    ArrayList<String> arrayList;
    ProductListAdapter adapter;
    FloatingActionButton button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.activity_product_list, container, false);
        list_product=(ListView)view.findViewById(R.id.list_product);

        common_header=(View)view.findViewById(R.id.inc_common_header);
        common_header.setVisibility(View.GONE);

        button=(FloatingActionButton)view.findViewById(R.id.btn_float);

        arrayList=new ArrayList<>();
        arrayList.add("pen");
        arrayList.add("van");
        arrayList.add("san");
        arrayList.add("can");

        adapter=new ProductListAdapter(getActivity(),arrayList, R.layout.checkable_productlist,"product");
        list_product.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    getActivity().finish();

            }
        });



        return view;
    }
}
