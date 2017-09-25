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
import gbc.sa.vansales.models.ShelfProduct;
/**
 * Created by eheuristic on 12/5/2016.
 */
public class ShelfAdapter extends BaseAdapter implements Filterable {
    Context context;
    int resource;
    ArrayList<ShelfProduct> dataList;
    ArrayList<ShelfProduct> dataListOne;
    String from;
    ItemFilter mFilter = new ItemFilter();
    String strarr[] = {"50% AMC Invoice Discount", "20% FOC Discount", "10% Other Discount"};
    public ShelfAdapter(Context context, ArrayList<ShelfProduct> item, int resource) {
        this.context = context;
        this.resource = resource;
        this.from = from;
        dataList = item;
        dataListOne = item;
        Log.v("adapter", "called");
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
        Log.v("size", getCount() + "");
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
        holder.tv_title.setText(dataList.get(position).getProductname());
        holder.tv_price.setText("Price:54.00/2.25");
        holder.tv_cases.setText("Cases");
        holder.tv_cases_value.setText(String.valueOf(dataList.get(position).getPro_case()));
        holder.tv_pcs.setText("Pcs");
        holder.tv_pcs_value.setText(String.valueOf(dataList.get(position).getPro_pcs()));
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
            Log.v("DataAdapter", "constratinst : " + dataList.size());
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {
                ArrayList<ShelfProduct> filteredItems = new ArrayList<>();
                for (int i = 0, l = dataList.size(); i < l; i++) {
                    String p = dataList.get(i).getProductname().toLowerCase();
                    if (p.contains(constraint)) {
                        filteredItems.add(dataList.get(i));
                        Log.v("DataAdapter", p + " -- " + dataList.get(i));
                    }
                }
                result.count = filteredItems.size();
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
            dataList = (ArrayList<ShelfProduct>) results.values;
            Log.v("datalist", dataList.size() + "--");
            notifyDataSetChanged();
            notifyDataSetInvalidated();
        }
    }
    public class Holder {
        TextView tv_title, tv_price, tv_cases, tv_cases_value, tv_pcs, tv_pcs_value;
    }
}
