package gbc.sa.vansales.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.CustomerDetailActivity;
import gbc.sa.vansales.activities.CustomerOperationsActivity;
import gbc.sa.vansales.adapters.DataAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerData;
import gbc.sa.vansales.models.CustomerHeader;
/**
 * Created by eheuristic on 12/2/2016.
 */

public class VisitAllFragment extends Fragment {

    public static DataAdapter dataAdapter;
//    ArrayList<CustomerData> dataArrayList;

    ListView listView;
    View view;
    ArrayList<CustomerHeader> customers = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.visitall_fragment, container, false);
//        dataArrayList =new ArrayList<>();
//        loadData();
        dataAdapter = new DataAdapter(getActivity().getBaseContext(),Const.dataArrayList);
        listView = (ListView)view.findViewById(R.id.journeyPlanList);
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer customer = Const.dataArrayList.get(position);
                Intent intent=new Intent(getActivity(), CustomerDetailActivity.class);
                intent.putExtra("headerObj", customer);
                startActivity(intent);

            }
        });


        return view;
    }



}
