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

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.CustomerDetailActivity;
import gbc.sa.vansales.adapters.DataAdapter;
import gbc.sa.vansales.data.Const;

/**
 * Created by eheuristic on 12/2/2016.
 */

public class AllCustomerFragment extends Fragment {

    public static DataAdapter dataAdapter1;
//    ArrayList<CustomerData> dataArrayList;

    ListView listView;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.visitall_fragment, container, false);
//        dataArrayList =new ArrayList<>();
//        loadData();
        dataAdapter1 = new DataAdapter(getActivity(), Const.allCustomerdataArrayList);
        listView = (ListView) view.findViewById(R.id.journeyPlanList);
        listView.setAdapter(dataAdapter1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), CustomerDetailActivity.class);
                startActivity(intent);

            }
        });
        return view;
    }
}