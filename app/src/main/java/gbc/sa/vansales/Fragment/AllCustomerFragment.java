package gbc.sa.vansales.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import gbc.sa.vansales.utils.Callback;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.Settings;
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
    android.location.Location myLocation = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.visitall_fragment, container, false);
        new gbc.sa.vansales.google.Location(getActivity(), new Callback() {
            @Override
            public void callbackSuccess(android.location.Location location) {
                myLocation = location;
            }
            @Override
            public void callbackFailure() {
            }
        });
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
                Const.customerPosition = position;
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
       // dialog.setTitle(getString(R.string.shop_status));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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

                //Visit List Posting
                HashMap<String,String>newMap = new HashMap<String, String>();
                newMap.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
                newMap.put(db.KEY_START_TIMESTAMP,Helpers.getCurrentTimeStamp());
                newMap.put(db.KEY_VISITLISTID,Helpers.generateVisitList(db));
                newMap.put(db.KEY_VISIT_SERVICED_REASON,arrayList.get(position).getReasonCode());
                int activityID = 0;

                //Check if any Activity for Customer
                String activityId = "";
                HashMap<String,String>activityMap = new HashMap<String, String>();
                activityMap.put(db.KEY_ACTIVITY_ID,"");
                HashMap<String,String>filterMap = new HashMap<String, String>();
                filterMap.put(db.KEY_CUSTOMER_NO,customer.getCustomerID());
                if(db.checkData(db.VISIT_LIST_POST,filterMap)){
                    Cursor c = db.getData(db.VISIT_LIST_POST,activityMap,filterMap);
                    if(c.getCount()>0){
                        c.moveToFirst();
                        if(c.getCount()==1){
                            activityId = c.getString(c.getColumnIndex(db.KEY_ACTIVITY_ID));
                        }
                        else{
                            do{
                                activityId = c.getString(c.getColumnIndex(db.KEY_ACTIVITY_ID));
                            }
                            while (c.moveToNext());
                        }
                    }
                }
                if(!activityId.equals("")){
                    activityID = Integer.parseInt(activityId);
                }
                newMap.put(db.KEY_ACTIVITY_ID,String.valueOf(++activityID));
                newMap.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                newMap.put(db.KEY_CUSTOMER_NO,customer.getCustomerID());
                newMap.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                newMap.put(db.KEY_IS_PRINTED,App.DATA_NOT_POSTED);
                //newMap.put(db.KEY_CUSTOMER_TYPE,customer.getPaymentMethod());
                db.addData(db.VISIT_LIST_POST,newMap);


                if (customerFlagExist(customer)) {
                    loadCustomerFlag(customer);
                } else {
                    App.CustomerRouteControl obj = new App.CustomerRouteControl();
                    obj.setThresholdLimit("99");
                    obj.setIsVerifyGPS(false);
                    obj.setIsEnableIVCopy(true);
                    obj.setIsDelayPrint(true);
                    obj.setIsEditOrders(true);
                    obj.setIsEditInvoice(true);
                    obj.setIsReturns(true);
                    obj.setIsDamaged(true);
                    obj.setIsSignCapture(true);
                    obj.setIsReturnCustomer(true);
                    obj.setIsCollection(true);
                }
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
                if(Settings.getString(App.LANGUAGE).equals("en")){
                    status.setReasonDescription(UrlBuilder.decodeString(reason.getReasonDescription()));
                }
                else{
                    status.setReasonDescription(UrlBuilder.decodeString(reason.getReasonDescriptionAr()));
                }
                if(status.getReasonCode().contains("V")){
                    arrayList.add(status);
                }
                //status.setReasonDescription(UrlBuilder.decodeString(reason.getReasonDescription()));
                //arrayList.add(status);
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
    public boolean customerFlagExist(Customer customer){
        HashMap<String,String>filter = new HashMap<>();
        filter.put(db.KEY_CUSTOMER_NO, customer.getCustomerID());
        return db.checkData(db.CUSTOMER_FLAGS,filter);
    }
    public void loadCustomerFlag(Customer customer){
        HashMap<String,String>map = new HashMap<>();
        map.put(db.KEY_TRIP_ID,"");
        map.put(db.KEY_CUSTOMER_NO,"");
        map.put(db.KEY_THRESHOLD_LIMIT,"");
        map.put(db.KEY_VERIFYGPS,"");
        map.put(db.KEY_GPS_SAVE,"");
        map.put(db.KEY_ENABLE_INVOICE,"");
        map.put(db.KEY_ENABLE_DELAY_PRINT,"");
        map.put(db.KEY_ENABLE_EDIT_ORDERS,"");
        map.put(db.KEY_ENABLE_EDIT_INVOICE,"");
        map.put(db.KEY_ENABLE_RETURNS,"");
        map.put(db.KEY_ENABLE_DAMAGED,"");
        map.put(db.KEY_ENABLE_SIGN_CAPTURE,"");
        map.put(db.KEY_ENABLE_RETURN,"");
        map.put(db.KEY_ENABLE_AR_COLLECTION,"");
        map.put(db.KEY_ENABLE_POS_EQUI,"");
        map.put(db.KEY_ENABLE_SUR_AUDIT,"");
        HashMap<String,String>filter = new HashMap<>();
        filter.put(db.KEY_CUSTOMER_NO,customer.getCustomerID());
        Cursor cursor = db.getData(db.CUSTOMER_FLAGS,map,filter);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            App.CustomerRouteControl obj = new App.CustomerRouteControl();
            obj.setThresholdLimit(cursor.getString(cursor.getColumnIndex(db.KEY_THRESHOLD_LIMIT)));
            obj.setIsVerifyGPS(cursor.getString(cursor.getColumnIndex(db.KEY_VERIFYGPS)).equals("0") ? false : true);
            obj.setIsEnableIVCopy(cursor.getString(cursor.getColumnIndex(db.KEY_ENABLE_INVOICE)).equals("0") ? false : true);
            obj.setIsDelayPrint(cursor.getString(cursor.getColumnIndex(db.KEY_ENABLE_DELAY_PRINT)).equals("0") ? false : true);
            obj.setIsEditOrders(cursor.getString(cursor.getColumnIndex(db.KEY_ENABLE_EDIT_ORDERS)).equals("0") ? false : true);
            obj.setIsEditInvoice(cursor.getString(cursor.getColumnIndex(db.KEY_ENABLE_EDIT_INVOICE)).equals("0") ? false : true);
            obj.setIsReturns(cursor.getString(cursor.getColumnIndex(db.KEY_ENABLE_RETURNS)).equals("0") ? false : true);
            obj.setIsDamaged(cursor.getString(cursor.getColumnIndex(db.KEY_ENABLE_DAMAGED)).equals("0") ? false : true);
            obj.setIsSignCapture(cursor.getString(cursor.getColumnIndex(db.KEY_ENABLE_SIGN_CAPTURE)).equals("0")?false:true);
            obj.setIsReturnCustomer(cursor.getString(cursor.getColumnIndex(db.KEY_ENABLE_RETURN)).equals("0")?false:true);
            obj.setIsCollection(cursor.getString(cursor.getColumnIndex(db.KEY_ENABLE_AR_COLLECTION)).equals("0")?false:true);
        }

    }
    public boolean verifyGPS(){

       /* String customerLatitude = "24.942091";
        String customerLongitude = "46.712125";*/

        String customerLatitude = "25.042429";
        String customerLongitude = "55.137817";

        String outlet_georadius = "5500"; //Distance is in metres(5445.938)

        android.location.Location customerLocation = new android.location.Location("");
        customerLocation.setLatitude(Double.parseDouble(customerLatitude));
        customerLocation.setLongitude(Double.parseDouble(customerLongitude));
        double radius = Double.parseDouble(outlet_georadius);
        float distance = myLocation.distanceTo(customerLocation);
        Log.e("Distance", "" + distance);
        if(distance<radius){
            return true;
        }
        else{
            return false;
        }

    }

}