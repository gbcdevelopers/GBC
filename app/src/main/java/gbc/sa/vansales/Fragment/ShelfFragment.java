package gbc.sa.vansales.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.SalesAdapter;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class ShelfFragment extends Fragment {

    View view;
    ListView listShelf;
  public static SalesAdapter adapter;


//    ArrayList<CustomerData> dataArrayList;


    ArrayList<String> arrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_shelf,container,false);
        listShelf=(ListView)view.findViewById(R.id.list_shelf);

        arrayList = new ArrayList<>();
        arrayList.add("AM5508 LARGE SWISS ROLL VANILL (24x110G)");
        arrayList.add("BM5508 SMALL SWISS ROLL VANILL (24x110G)");
        arrayList.add("CM5508 BIG SWISS ROLL VANILL (24x110G)");
        arrayList.add("DM5508 SWISS ROLL VANILL (24x110G)");
        arrayList.add("EM5508 ROLL VANILL (24x110G)");



        adapter=new SalesAdapter(getActivity().getBaseContext(),arrayList, R.layout.sales_list);
        listShelf.setAdapter(adapter);


        return view;
    }
}
