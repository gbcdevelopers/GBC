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
public class UnloadAdapter extends ArrayAdapter<Unload>{
    private ArrayList<Unload> salesArrayList;
    private int pos;
    public UnloadAdapter(Context context, ArrayList<Unload> salesArrayList){

        super(context, R.layout.sales_list, salesArrayList);
        this.salesArrayList = salesArrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ArrayList<Unload> dataList = this.salesArrayList;
        pos = position;
        ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sales_list,parent,false);
            // get all UI view
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_item_code = (TextView)convertView.findViewById(R.id.tv_item_code);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_cases = (TextView) convertView.findViewById(R.id.tv_cases);
            holder.tv_cases_value = (TextView) convertView.findViewById(R.id.tv_cases_value);
            holder.tv_pcs = (TextView) convertView.findViewById(R.id.tv_pcs);
            holder.tv_pcs_value = (TextView) convertView.findViewById(R.id.tv_pcs_value);
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

        holder.tv_item_code.setText(getContext().getString(R.string.item_code) + " - " + StringUtils.stripStart(salesArrayList.get(pos).getMaterial_no(), "0"));
        //holder.tv_price.setText("Price:54.00/2.25");
        //holder.tv_price.setText(getContext().getString(R.string.price) + " - " + String.valueOf(Float.parseFloat(salesArrayList.get(pos).getPrice())*Float.parseFloat(salesArrayList.get(pos).getCases())));
        //holder.tv_price.setVisibility(View.GONE);
        holder.tv_price.setText(getContext().getString(R.string.price) + " - " + salesArrayList.get(position).getPrice());
        holder.tv_cases.setText(getContext().getString(R.string.cases));
        holder.tv_cases_value.setText(salesArrayList.get(position).getCases());
        holder.tv_pcs.setText(getContext().getString(R.string.pcs));
        holder.tv_pcs_value.setText(salesArrayList.get(position).getPic());
        return convertView;
    }

    public class ViewHolder {
        TextView tv_title, tv_price, tv_cases, tv_cases_value, tv_pcs, tv_pcs_value, tv_item_code;
    }
}
