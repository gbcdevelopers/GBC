package gbc.sa.vansales.Fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.SalesAdapter;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class SalesFragment extends Fragment {


    View view;
    ListView listSales;
    SalesAdapter adapter;
    ArrayList<String> arrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_salesinvoice,container,false);
        listSales=(ListView)view.findViewById(R.id.list_sales);


        arrayList = new ArrayList<>();
        arrayList.add("A");
        arrayList.add("B");
        arrayList.add("C");



        adapter=new SalesAdapter(getActivity(),arrayList, R.layout.sales_list);
        listSales.setAdapter(adapter);

        listSales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                final Dialog dialog=new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_with_crossbutton);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ImageView iv_cancle=(ImageView)dialog.findViewById(R.id.imageView_close);
                iv_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();



            }
        });

        return view;
    }
}
