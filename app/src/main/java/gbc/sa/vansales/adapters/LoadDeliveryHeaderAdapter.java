package gbc.sa.vansales.adapters;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * Created by Muhammad Umair on 29/11/2016.
 */

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.LoadDeliveryHeader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LoadDeliveryHeaderAdapter extends ArrayAdapter<LoadDeliveryHeader> {
    ArrayList<LoadDeliveryHeader> searchArrayList;

    private LayoutInflater mInflater;

    public LoadDeliveryHeaderAdapter(Context context, ArrayList<LoadDeliveryHeader> loads) {
        super(context, R.layout.activity_single_load, loads);
        mInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_single_load, null);
            holder = new ViewHolder();
            holder.deliveryNo = (TextView) convertView.findViewById(R.id.deliveryNo);
            holder.loadingDate = (TextView) convertView
                    .findViewById(R.id.loadingDate);
          //  holder.availableLoad = (TextView) convertView.findViewById(R.id.availableLoad);
           // holder.txtStatus = (TextView) convertView.findViewById(R.id.status);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LoadDeliveryHeader load = getItem(position);
        holder.deliveryNo.setText("Load No : " + load.getDeliveryNo());
        holder.loadingDate.setText("Delivery Date : " + load.getLoadingDate());
//        holder.availableLoad.setText(load.getAvailableLoad());


        return convertView;
    }

    static class ViewHolder {
        TextView deliveryNo;
        TextView loadingDate;
        TextView availableLoad;
        TextView txtStatus;
    }
}