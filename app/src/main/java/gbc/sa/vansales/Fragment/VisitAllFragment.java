package gbc.sa.vansales.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.CustomerDetailActivity;
import gbc.sa.vansales.activities.CustomerOperationsActivity;
import gbc.sa.vansales.activities.SelectCustomerStatus;
import gbc.sa.vansales.adapters.CustomerStatusAdapter;
import gbc.sa.vansales.adapters.DataAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.data.OrderReasons;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerData;
import gbc.sa.vansales.models.CustomerHeader;
import gbc.sa.vansales.models.CustomerStatus;
import gbc.sa.vansales.models.Reasons;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by eheuristic on 12/2/2016.
 */

public class VisitAllFragment extends Fragment {

    public static DataAdapter dataAdapter;
    private ArrayList<CustomerStatus> arrayList = new ArrayList<>();
    private ArrayAdapter<CustomerStatus> adapter;
    private ArrayList<Reasons> reasonsList = new ArrayList<>();
//    ArrayList<CustomerData> dataArrayList;

    ListView listView;
    View view;
    ArrayList<CustomerHeader> customers = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.visitall_fragment, container, false);
        reasonsList = OrderReasons.get();

//        dataArrayList =new ArrayList<>();
//        loadData();
        dataAdapter = new DataAdapter(getActivity().getBaseContext(),Const.dataArrayList);
        adapter = new CustomerStatusAdapter(getActivity(),arrayList);
        loadCustomerStatus();
        listView = (ListView)view.findViewById(R.id.journeyPlanList);
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Const.customerPosition=position;
                Customer customer = Const.dataArrayList.get(position);
               // Intent intent=new Intent(getActivity(), CustomerDetailActivity.class);
                /*Intent intent = new Intent(getActivity(), SelectCustomerStatus.class);
                intent.putExtra("headerObj", customer);
                intent.putExtra("msg","visit");
                startActivity(intent);*/
                showStatusDialog(customer);

            }
        });


        return view;
    }

    private void showStatusDialog(final Customer customer){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle(getString(R.string.shop_status));
        View view = getActivity().getLayoutInflater().inflate(R.layout.activity_select_customer_status, null);
        Button cancel = (Button)view.findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ListView lv = (ListView) view.findViewById(R.id.statusList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CustomerDetailActivity.class);
                intent.putExtra("headerObj", customer);
                intent.putExtra("msg", "visit");
                startActivity(intent);
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();

    }
    private void loadCustomerStatus(){
        for(Reasons reason:reasonsList){
            CustomerStatus status = new CustomerStatus();
            if(reason.getReasonType().equals(App.VisitReasons)){
                status.setReasonCode(reason.getReasonID());
                status.setReasonDescription(UrlBuilder.decodeString(reason.getReasonDescription()));
                arrayList.add(status);
            }
        }
        adapter.notifyDataSetChanged();
    }



}
