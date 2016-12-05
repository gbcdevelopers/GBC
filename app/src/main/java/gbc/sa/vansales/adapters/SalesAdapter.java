package gbc.sa.vansales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import gbc.sa.vansales.R;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class SalesAdapter extends BaseAdapter {

    int item;
    Context context;
    int resource;
    public SalesAdapter(Context context,int item,int resource)
    {
        this.context=context;
        this.item=item;
        this.resource=resource;
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

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView=inflater.inflate(resource,null);
        }




        return convertView;
    }
}
