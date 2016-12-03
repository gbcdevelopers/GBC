package gbc.sa.vansales.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.CustomerOperationsActivity;
import gbc.sa.vansales.adapters.DataAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.models.CustomerData;

/**
 * Created by eheuristic on 12/2/2016.
 */

public class VisitAllFragment extends Fragment {

    public static DataAdapter dataAdapter;
//    ArrayList<CustomerData> dataArrayList;

    ListView listView;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.visitall_fragment, container, false);
//        dataArrayList =new ArrayList<>();
//        loadData();
        dataAdapter = new DataAdapter(getActivity().getBaseContext(),Const.dataArrayList);
        listView = (ListView)view.findViewById(R.id.journeyPlanList);
        listView.setAdapter(dataAdapter);
        return view;
    }
    /*public void loadData(){

        for (int i = 0; i < 10; i++) {
            CustomerData customerData=createCustomerData(i);
            dataArrayList.add(customerData);
        }

    }*/
    public static CustomerData createCustomerData(int index){
        CustomerData customer=new CustomerData();
        int i=100+index;
        customer.setId(String.valueOf(i));
        customer.setName("ankit");
        customer.setAddress("rajkot");
        return customer;
    }
}
