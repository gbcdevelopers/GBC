package gbc.sa.vansales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.ColletionData;

/**
 * Created by eheuristic on 10/10/2016.
 */

public class ColletionAdapter extends BaseAdapter implements Filterable {

    ArrayList<Integer> intlist;
    ItemFilter mFilter = new ItemFilter();
    private ArrayList<ColletionData> dataList;
    private ArrayList<ColletionData> dataListOne;
    private Context mContext;

    public ColletionAdapter(Context context, ArrayList<ColletionData> dataList) {
        this.dataList = dataList;
        this.dataListOne = dataList;
        this.mContext = context;

        intlist = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {


        Log.v("size", dataList.size() + "==");

        ItemRowHolder holder = null;
        if (view == null) {
            LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
            view = li.inflate(R.layout.colletion_list_item, null);
            holder = new ItemRowHolder();
            holder.ll_items = (LinearLayout) view.findViewById(R.id.ll_items);
            holder.tv_colletion_number = (TextView) view.findViewById(R.id.tv_colletion_number);
            holder.tv_colletion__selsemen_number = (TextView) view.findViewById(R.id.tv_colletion__selsemen_number);
            holder.tv_colletion__amout_due_number = (TextView) view.findViewById(R.id.tv_colletion__amout_due_number);
            holder.tv_colletion__ade_number = (TextView) view.findViewById(R.id.tv_colletion__ade_number);
            holder.check_selected = (CheckBox) view.findViewById(R.id.check_selected);
            view.setTag(holder);
        } else {
            holder = (ItemRowHolder) view.getTag();
        }

        holder.tv_colletion_number.setText(dataList.get(i).getId());
        holder.tv_colletion__selsemen_number.setText(dataList.get(i).getSelsemanId());
        holder.tv_colletion__amout_due_number.setText(dataList.get(i).getAmoutDue());
        holder.tv_colletion__ade_number.setText(dataList.get(i).getAmoutAde());

        if (intlist.contains(i)) {
            holder.check_selected.setChecked(true);
            holder.ll_items.setBackgroundColor(Color.parseColor("#FFC0C0C0"));
        } else {
            holder.check_selected.setChecked(false);
            holder.ll_items.setBackgroundColor(0);
        }

        holder.check_selected.setClickable(false);
        holder.tv_colletion_number.setClickable(false);
        holder.tv_colletion__amout_due_number.setClickable(false);
        holder.tv_colletion__ade_number.setClickable(false);



        final ItemRowHolder finalHolder = holder;
        holder.ll_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("DataAdapter", i + "-- item click");

                if (finalHolder.check_selected.isChecked()) {
                    finalHolder.check_selected.setChecked(false);
                    finalHolder.ll_items.setBackgroundColor(0);
                    intlist.remove((Integer) i);
                } else {
                    finalHolder.check_selected.setChecked(true);
                    finalHolder.ll_items.setBackgroundColor(Color.parseColor("#FFC0C0C0"));
                    intlist.add(i);
                }
            }
        });


        return view;
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
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {
                ArrayList<ColletionData> filteredItems =
                        new ArrayList<>();
                for (int i = 0, l = dataList.size(); i < l; i++) {
                    // ArrayList<HashMap<String, String>> p =
                    // originalList.get(i);
                    String p = dataList.get(i).getId();
                    if (p.contains(constraint))
                        filteredItems.add(dataList.get(i));
                }
                Log.v("DataAdapter", "not blank");
                result.count = filteredItems.size();
                result.values = filteredItems;

            } else {
                synchronized (this) {
                    result.count = dataListOne.size();
                    result.values = dataListOne;
//                    result.values = dataList;
//                    result.count = dataList.size();
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // users = (List<GraphUser>) results.values;
            //filteredData = (ArrayList<String>) results.values;
            dataList = (ArrayList<ColletionData>) results.values;
            notifyDataSetChanged();

            for (int i = 0, l = dataList.size(); i < l; i++)
                dataList.get(i);
            //add(productList.get(i));

            notifyDataSetInvalidated();
        }
    }


    public class ItemRowHolder {
        LinearLayout ll_items;
        TextView tv_colletion_number;
        TextView tv_colletion__selsemen_number;
        TextView tv_colletion__amout_due_number;
        TextView tv_colletion__ade_number;
        CheckBox check_selected;
    }

}