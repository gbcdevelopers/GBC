package gbc.sa.vansales.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.BAlanceList;
import gbc.sa.vansales.models.ItemList;

/**
 * Created by eheuristic on 22/12/2016.
 */

public class BalanceAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<BAlanceList> arrayList;

    public BalanceAdapter(Activity activity, ArrayList<BAlanceList> arrayList)
    {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.item_balance_list,viewGroup,false);
        }

       BAlanceList itemListModel = arrayList.get(i);

        TextView tv_cusomer= (TextView)view.findViewById(R.id.tv_customer);
        TextView tv_in_no = (TextView)view.findViewById(R.id.tv_inv_no);
        TextView tv_amount = (TextView)view.findViewById(R.id.tv_amount);
        TextView tv_due_date = (TextView)view.findViewById(R.id.tv_due_date);
        TextView tv_inv_date = (TextView)view.findViewById(R.id.tv_inv_date);


        tv_cusomer.setText(itemListModel.getCustomer());
        tv_in_no.setText(itemListModel.getInvoice_no());
        tv_amount.setText(itemListModel.getAmount());
        tv_due_date.setText(itemListModel.getDue_date());
        tv_inv_date.setText(itemListModel.getInv_date());


        return view;
    }
}
