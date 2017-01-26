package gbc.sa.vansales.Fragment;

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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.CustomerDetailActivity;
import gbc.sa.vansales.activities.SelectCustomerActivity;
import gbc.sa.vansales.activities.SelectCustomerStatus;
import gbc.sa.vansales.adapters.CustomerStatusAdapter;
import gbc.sa.vansales.adapters.DataAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.data.OrderReasons;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerStatus;
import gbc.sa.vansales.models.Reasons;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by eheuristic on 12/2/2016.
 */

public class AllCustomerFragment extends Fragment {

    public static DataAdapter dataAdapter1;
//    ArrayList<CustomerData> dataArrayList;
    private ArrayList<CustomerStatus> arrayList = new ArrayList<>();
    private ArrayAdapter<CustomerStatus> adapter;
    private ArrayList<Reasons> reasonsList = new ArrayList<>();
    ListView listView;
    DatabaseHandler db;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.visitall_fragment, container, false);
        reasonsList = OrderReasons.get();
        db = new DatabaseHandler(getActivity());
//        dataArrayList =new ArrayList<>();
//        loadData();
        dataAdapter1 = new DataAdapter(getActivity(), Const.allCustomerdataArrayList);
        adapter = new CustomerStatusAdapter(getActivity(),arrayList);
        loadCustomerStatus();
        listView = (ListView) view.findViewById(R.id.journeyPlanList);
        listView.setAdapter(dataAdapter1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Const.customerPosition=position;
                Customer customer = Const.allCustomerdataArrayList.get(position);
                //Intent intent=new Intent(getActivity(), CustomerDetailActivity.class);
               /* Intent intent = new Intent(getActivity(), SelectCustomerStatus.class);
                intent.putExtra("headerObj", customer);
                intent.putExtra("msg","all");
                startActivity(intent);*/
                showStatusDialog(customer);
               /* boolean inSequence = checkIfinSequence(customer);
                if(inSequence){
                    showStatusDialog(customer);
                }
                else{
                    Toast.makeText(getActivity(),getString(R.string.not_in_sequence),Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        return view;
    }

    private void showStatusDialog(final Customer customer){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle(getString(R.string.shop_status));
        View view = getActivity().getLayoutInflater().inflate(R.layout.activity_select_customer_status, null);
        ListView lv = (ListView) view.findViewById(R.id.statusList);
        Button cancel = (Button)view.findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               /* HashMap<String,String>filter = new HashMap<String, String>();
                filter.put(db.KEY_CUSTOMER_IN_TIMESTAMP, Helpers.getCurrentTimeStamp());
                filter.put(db.KEY_IS_VISITED,App.IS_COMPLETE);
                HashMap<String,String>map = new HashMap<String, String>();
                map.put(db.KEY_VISIT_SERVICED_REASON,arrayList.get(position).getReasonCode());
                map.put(db.KEY_CUSTOMER_IN_TIMESTAMP, Helpers.getCurrentTimeStamp());
                map.put(db.KEY_IS_VISITED,App.IS_COMPLETE);
                if(db.checkData(db.VISIT_LIST,filter)){
                    db.updateData(db.VISIT_LIST,map,filter);
                }
                else{
                    db.addData(db.VISIT_LIST,map);
                }*/

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
    private boolean checkIfinSequence(Customer customer){
        String itemNo = customer.getCustomerItemNo();
        int prevItemNo = Integer.parseInt(itemNo)-1;
        HashMap<String,String>map = new HashMap<>();
        map.put(db.KEY_ITEMNO, StringUtils.leftPad(String.valueOf(prevItemNo), 3, "0"));
        map.put(db.KEY_IS_VISITED, App.IS_COMPLETE);
        if(itemNo.equals("001")){
            return true;
        }
        else{
            if(db.checkData(db.VISIT_LIST,map)){
                return true;
            }
            else{
                return false;
            }
        }

       // return false;
    }
}