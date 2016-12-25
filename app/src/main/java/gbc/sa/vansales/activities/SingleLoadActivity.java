package gbc.sa.vansales.activities;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * Created by Muhammad Umair on 29/11/2016.
 */

import java.util.ArrayList;

import gbc.sa.vansales.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SingleLoadActivity  extends BaseAdapter {
    private static ArrayList<LoadConstants> searchArrayList;

    private LayoutInflater mInflater;

    SingleLoadActivity()
    {

    }

    SingleLoadActivity(Context context, ArrayList<LoadConstants> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_single_load, null);
            holder = new ViewHolder();
            holder.txtLoadNumber = (TextView) convertView.findViewById(R.id.deliveryNo);
            holder.txtLoadDate = (TextView) convertView
                    .findViewById(R.id.loadingDate);
        //    holder.txtAvailableLoad = (TextView) convertView.findViewById(R.id.availableLoad);
        //    holder.txtStatus = (TextView) convertView.findViewById(R.id.status);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /*holder.txtLoadNumber.setText(searchArrayList.get(position).getloadNumber());
        holder.txtLoadDate.setText(searchArrayList.get(position)
                .getloadDate());
        holder.txtAvailableLoad.setText(searchArrayList.get(position).getavailableLoad());

        holder.txtStatus.setText(searchArrayList.get(position).getStatus());*/

        return convertView;
    }

    static class ViewHolder {
        TextView txtLoadNumber;
        TextView txtLoadDate;
        TextView txtAvailableLoad;
        TextView txtStatus;
    }
}