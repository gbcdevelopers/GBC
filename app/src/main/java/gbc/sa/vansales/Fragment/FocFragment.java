package gbc.sa.vansales.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.CategoryListActivity;
import gbc.sa.vansales.activities.SalesInvoiceActivity;
import gbc.sa.vansales.adapters.SalesAdapter;
import gbc.sa.vansales.adapters.SalesInvoiceAdapter;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.models.Sales;
import gbc.sa.vansales.utils.Settings;
/**
 * Created by eheuristic on 12/5/2016.
 */
public class FocFragment extends Fragment {
    View viewmain;
    ListView listSales;
    public static SalesAdapter adapter;
    public static ArrayList<Sales> salesarrayList;
    FloatingActionButton fab;
    ArrayAdapter<Sales> myAdapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try{
            if(isVisibleToUser){
                salesarrayList = Const.focList;
                if(salesarrayList.size()>0){
                    try{
                        myAdapter = new SalesInvoiceAdapter(getActivity(), salesarrayList);
                        // adapter = new SalesAdapter(getActivity(), salesarrayList);
                        listSales.setAdapter(myAdapter);
                        myAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewmain = inflater.inflate(R.layout.fragment_salesinvoice, container, false);
        try{
            listSales = (ListView) viewmain.findViewById(R.id.list_sales);
            fab = (FloatingActionButton) viewmain.findViewById(R.id.fab);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_white_add));
            fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_white_add));
            fab.setVisibility(View.GONE);
            TextView tv = (TextView) viewmain.findViewById(R.id.tv_available_limit);
            tv.setText(Const.availableLimit);
            String strProductname[] = {"Carton 80*150ml Fayha", "CARTON 48*600ml Fayha", "Shrink berain"};
            salesarrayList = new ArrayList<>();

            if(Const.focList.size()>0){
                salesarrayList = Const.focList;
                Log.e("HELOOOOOOOOOOOOOOOOOOO","0000000000000000");
            }
            if(salesarrayList.size()>0){
                myAdapter = new SalesInvoiceAdapter(getActivity(), salesarrayList);
                listSales.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            }
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.setString("from", "foc");
                    SalesInvoiceActivity.tab_position = 1;
                    Intent intent = new Intent(getActivity(), CategoryListActivity.class);
                    getActivity().startActivity(intent);
                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        return viewmain;
    }
}