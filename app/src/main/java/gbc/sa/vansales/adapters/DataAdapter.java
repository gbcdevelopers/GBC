package gbc.sa.vansales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.CustomerData;
import gbc.sa.vansales.views.TextViewWithLabel;

/**
 * Created by eheuristic on 10/10/2016.
 */

public class DataAdapter extends BaseAdapter implements Filterable {

    ArrayList<Integer> intlist;
    ItemFilter mFilter = new ItemFilter();
    private ArrayList<CustomerData> dataList;
    private ArrayList<CustomerData> dataListOne;
    private Context mContext;

    public DataAdapter(Context context, ArrayList<CustomerData> dataList) {
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
        ItemRowHolder holder = null;
        if (view == null) {
            LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
            view = li.inflate(R.layout.badge_journey_plan, null);
//            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_contact, null);
            holder = new ItemRowHolder();
            holder.horizontal_view=(View)view.findViewById(R.id.view);
            holder.customer_id = (TextViewWithLabel) view.findViewById(R.id.customer_id);
            holder.customer_name = (TextViewWithLabel) view.findViewById(R.id.customer_name);
            holder.customer_address = (TextViewWithLabel) view.findViewById(R.id.customer_address);
            holder.saleflag = (ImageView) view.findViewById(R.id.img_sale);
            holder.deliveryflag = (ImageView) view.findViewById(R.id.img_delivery);
            holder.collectionflag = (ImageView) view.findViewById(R.id.img_collection);
            holder.returnsflag = (ImageView) view.findViewById(R.id.img_returns);
//            holder.ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
//            holder.check_selected = (CheckBox) view.findViewById(R.id.check_selected);



//            TextViewWithLabel customer_id = (TextViewWithLabel)convertView.findViewById(R.id.customer_id);
//            TextViewWithLabel customer_name = (TextViewWithLabel)convertView.findViewById(R.id.customer_name);
//            TextViewWithLabel customer_address = (TextViewWithLabel)convertView.findViewById(R.id.customer_address);
//            ImageView saleflag = (ImageView)convertView.findViewById(R.id.img_sale);
//            ImageView deliveryflag = (ImageView)convertView.findViewById(R.id.img_delivery);
//            ImageView collectionflag = (ImageView)convertView.findViewById(R.id.img_collection);
//            ImageView returnsflag = (ImageView)convertView.findViewById(R.id.img_returns);


            view.setTag(holder);
        } else {
            holder = (ItemRowHolder) view.getTag();
        }

        holder.customer_id.setText(dataList.get(i).getId());
        holder.customer_name.setText(dataList.get(i).getName());
        holder.customer_address.setText(dataList.get(i).getAddress());

        if(i==2 || i==5)
        {
            holder.horizontal_view.setBackgroundColor(Color.RED);
        }
        else {
            holder.horizontal_view.setBackgroundColor(Color.BLUE);
        }




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
                ArrayList<CustomerData> filteredItems =
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
            dataList = (ArrayList<CustomerData>) results.values;
            notifyDataSetChanged();

            for (int i = 0, l = dataList.size(); i < l; i++)
                dataList.get(i);
            //add(productList.get(i));

            notifyDataSetInvalidated();
        }
    }


    public class ItemRowHolder {
        ImageView saleflag;
        ImageView deliveryflag;
        ImageView collectionflag;
        ImageView returnsflag;
        TextViewWithLabel customer_id;
        TextViewWithLabel customer_name;
        TextViewWithLabel customer_address;
        View horizontal_view;
    }

}