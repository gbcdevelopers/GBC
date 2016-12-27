package gbc.sa.vansales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.PromotionActivity;
import gbc.sa.vansales.activities.PromotionListActivity;
import gbc.sa.vansales.models.Sales;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class SalesAdapter extends BaseAdapter implements Filterable {


    Context context;
    int resource;




    ArrayList<Sales> salesArrayList;
    ArrayList<Sales> salesArrayListone;

    ItemFilter mFilter = new ItemFilter();

    String strarr[] = {"50% AMC Invoice Discount", "20% FOC Discount", "10% Other Discount"};




    public SalesAdapter(Context context, ArrayList<Sales> item) {
        this.context = context;

        this.resource =R.layout.sales_list;


        salesArrayList = item;
        salesArrayListone = item;


        Log.v("adapter", "called");
    }


    @Override
    public int getCount() {
        return salesArrayList.size();
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

        Log.v("size", getCount() + "");


        if (resource == R.layout.sales_list) {
            Holder holder;
            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null);

                holder = new Holder();
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
                holder.tv_cases = (TextView) convertView.findViewById(R.id.tv_cases);
                holder.tv_cases_value = (TextView) convertView.findViewById(R.id.tv_cases_value);
                holder.tv_pcs = (TextView) convertView.findViewById(R.id.tv_pcs);
                holder.tv_pcs_value = (TextView) convertView.findViewById(R.id.tv_pcs_value);


                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.tv_title.setText(salesArrayList.get(position).getName());
            holder.tv_price.setText("Price:54.00/2.25");
            holder.tv_cases.setText("Cases");
            holder.tv_cases_value.setText(salesArrayList.get(position).getCases());
            holder.tv_pcs.setText("Pcs");
            holder.tv_pcs_value.setText(salesArrayList.get(position).getPic());


        } else {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);
            if (resource == R.layout.custom_promotionlist) {
                TextView tv = (TextView) convertView.findViewById(R.id.tv_product);
                CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chk_product);
                LinearLayout ll_promotion = (LinearLayout) convertView.findViewById(R.id.ll_promotion);


                View view = (View) convertView.findViewById(R.id.view);
                if (position == 1) {
                    view.setBackgroundColor(Color.RED);
                    checkBox.setVisibility(View.GONE);
                } else {
                    view.setBackgroundColor(Color.BLUE);
                    checkBox.setVisibility(View.VISIBLE);

                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        }
                    });
                }



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

            constraint = constraint.toString().toLowerCase();
            Log.v("DataAdapter", "constratinst : " + constraint);
            Log.v("DataAdapter", "constratinst : " + salesArrayList.size());
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {

                ArrayList<Sales> filteredItems = new ArrayList<>();
                for (int i = 0, l = salesArrayList.size(); i < l; i++) {

                    String p = salesArrayList.get(i).getName().toLowerCase();
                    if (p.contains(constraint)) {
                        filteredItems.add(salesArrayList.get(i));
                        Log.v("DataAdapter", p + " -- " + salesArrayList.get(i));
                    }

                }

                result.count = filteredItems.size();
                result.values = filteredItems;

            } else {
                synchronized (this) {
                    result.count = salesArrayListone.size();
                    result.values = salesArrayListone;
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            salesArrayList = (ArrayList<Sales>) results.values;
            Log.v("datalist", salesArrayList.size() + "--");
            notifyDataSetChanged();
            notifyDataSetInvalidated();
        }
    }

    public class Holder {
        TextView tv_title, tv_price, tv_cases, tv_cases_value, tv_pcs, tv_pcs_value;

    }



}
