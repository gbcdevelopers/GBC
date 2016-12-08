package gbc.sa.vansales.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class SalesAdapter extends BaseAdapter  implements Filterable {


    Context context;
    int resource;

    ArrayList<String> dataList;
   ArrayList<String> dataListOne;

   ItemFilter mFilter = new ItemFilter();

    String strarr[]={"50% AMC Invoice Discount","20% FOC Discount","10% Other Discount"};







    public SalesAdapter(Context context,ArrayList<String> item,int resource)
    {
        this.context=context;

        this.resource=resource;


        dataList=item;
        dataListOne=item;



        Log.v("adapter","called");
    }


    @Override
    public int getCount() {
        return dataList.size();
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

        Log.v("size",getCount()+"");


        if(resource== R.layout.sales_list)
        {
            Holder holder;
            if(convertView==null) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null);

                holder=new Holder();
                holder.tv_title=(TextView)convertView.findViewById(R.id.tv_title);
                holder.tv_price=(TextView)convertView.findViewById(R.id.tv_price);
                holder.tv_cases=(TextView)convertView.findViewById(R.id.tv_cases);
                holder.tv_cases_value=(TextView)convertView.findViewById(R.id.tv_cases_value);
                holder.tv_pcs=(TextView)convertView.findViewById(R.id.tv_pcs);
                holder.tv_pcs_value=(TextView)convertView.findViewById(R.id.tv_pcs_value);



                convertView.setTag(holder);
            }
            else {
                holder=(Holder)convertView.getTag();
            }

            holder.tv_title.setText(dataList.get(position));
            holder.tv_price.setText("Price:54.00/2.25");
            holder.tv_cases.setText("Cases");
            holder.tv_cases_value.setText("0");
            holder.tv_pcs.setText("Pcs");
            holder.tv_pcs_value.setText("0");



        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);
            if(resource== R.layout.custom_promotionlist) {
                TextView tv = (TextView) convertView.findViewById(R.id.tv_promotion);
                tv.setText(strarr[position]);
            }
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }


    public class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString();
            Log.v("DataAdapter", "constratinst : " + constraint);
            Log.v("DataAdapter", "constratinst : " + dataList.size());
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {

                ArrayList<String>  filteredItems=new ArrayList<>();
                for (int i = 0, l = dataList.size(); i < l; i++) {

                    String p = dataList.get(i);
                    if (p.contains(constraint)) {
                        filteredItems.add(dataList.get(i));
                        Log.v("DataAdapter",p+" -- "+ dataList.get(i));
                    }

                }

                result.count =filteredItems.size();
                result.values = filteredItems;

            } else {
                synchronized (this) {
                    result.count = dataListOne.size();
                    result.values = dataListOne;
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            dataList = (ArrayList<String>) results.values;
         Log.v("datalist",dataList.size()+"--");
            notifyDataSetChanged();
            notifyDataSetInvalidated();
        }
    }

    public class Holder
    {
        TextView tv_title,tv_price,tv_cases,tv_cases_value,tv_pcs,tv_pcs_value;

    }



}
