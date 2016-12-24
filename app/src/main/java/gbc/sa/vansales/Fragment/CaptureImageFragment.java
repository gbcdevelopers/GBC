package gbc.sa.vansales.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.SalesAdapter;

/**
 * Created by eheuristic on 12/6/2016.
 */

public class CaptureImageFragment extends Fragment {

    SalesAdapter adapter;
    View view;
    GridView gridView;
    ArrayList<String> arrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_captureimage, container, false);
        gridView=(GridView)view.findViewById(R.id.grid);

        arrayList=new ArrayList<>();
        arrayList.add("A");
        arrayList.add("B");
        arrayList.add("C");

        adapter=new SalesAdapter(getActivity(),arrayList, R.layout.custom_captureimage,"");
         gridView.setAdapter(adapter);





        return view;

    }
}
