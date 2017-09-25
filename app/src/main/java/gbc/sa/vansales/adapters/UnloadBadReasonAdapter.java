package gbc.sa.vansales.adapters;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.models.Sales;
import gbc.sa.vansales.models.Unload;
/**
 * Created by Rakshit on 18-Jan-17.
 */
public class UnloadBadReasonAdapter extends ArrayAdapter<Unload>{
    private ArrayList<Unload> salesArrayList;
    private int pos;
    public UnloadBadReasonAdapter(Context context, ArrayList<Unload> salesArrayList){

        super(context, R.layout.unload_bad_reason, salesArrayList);
        this.salesArrayList = salesArrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ArrayList<Unload> dataList = this.salesArrayList;
        pos = position;
        ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.unload_bad_reason,parent,false);
            // get all UI view
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);

        }
        else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }
        final Unload unload = salesArrayList.get(pos);
        /*if(salesArrayList.get(pos).getReasonCode().equals("3")){
            holder.tv_title.setText(salesArrayList.get(position).getName() + "(" + getContext().getString(R.string.theft) + ")");
        }
        else if(salesArrayList.get(pos).getReasonCode().equals("4")){
            holder.tv_title.setText(salesArrayList.get(position).getName() + "(" + getContext().getString(R.string.excess) + ")");
        }
        else{
            holder.tv_title.setText(salesArrayList.get(position).getName());
        }*/

        holder.tv_title.setText(salesArrayList.get(position).getName());

        return convertView;
    }

    public class ViewHolder {
        TextView tv_title;
    }
}
