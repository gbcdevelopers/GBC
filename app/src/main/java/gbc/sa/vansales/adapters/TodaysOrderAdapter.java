package gbc.sa.vansales.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.TodaysOrder;

/**
 * Created by eheuristic on 22/12/2016.
 */

public class TodaysOrderAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<TodaysOrder> arrayList;

    public TodaysOrderAdapter(Activity activity, ArrayList<TodaysOrder> arrayList) {
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

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.item_price_list, viewGroup, false);
        }

        TodaysOrder itemListModel = arrayList.get(i);

        TextView txt_item_number = (TextView) view.findViewById(R.id.txt_item_number);
        TextView txt_item_dec = (TextView) view.findViewById(R.id.txt_item_dec);
        TextView txt_item_case_price = (TextView) view.findViewById(R.id.txt_item_case_price);


        txt_item_number.setText("" + itemListModel.getCustomer());
        txt_item_dec.setText(itemListModel.getOrderNo());
        txt_item_case_price.setText("" + itemListModel.getPrice());


        return view;
    }
}
