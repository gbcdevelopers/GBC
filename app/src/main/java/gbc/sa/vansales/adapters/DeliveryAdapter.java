package gbc.sa.vansales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import gbc.sa.vansales.R;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class DeliveryAdapter extends BaseAdapter {


    Context context;
    int resource;
    int item;
    String from;

    public DeliveryAdapter(Context context, int item, int resource,String from)
    {
        this.context=context;

        this.resource=resource;
        this.item=item;
        this.from=from;


    }


    @Override
    public int getCount() {
        return item;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        if(from.equals("presale"))
        {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resource, null);
            TextView tv=(TextView)convertView.findViewById(R.id.tv_delivery);
            TextView tv1=(TextView)convertView.findViewById(R.id.tv_del_date);
            tv.setText("Order# 1234");
            tv1.setText("Order date:23-oct-2016");

        }else if(from.equals("delivery"))
        {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);

        }

        return convertView;
    }


}
