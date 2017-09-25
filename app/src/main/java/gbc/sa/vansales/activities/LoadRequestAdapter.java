package gbc.sa.vansales.activities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ProductListAdapter;
import gbc.sa.vansales.data.Const;
/**
 * Created by Muhammad Umair on 02/12/2016.
 */
public class LoadRequestAdapter extends BaseAdapter implements Filterable {
    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<LoadRequestConstants> loadRequestConstants = null;
    private List<LoadRequestConstants> loadRequestConstantsone = null;
    ArrayList<Integer> selectedArrayList;
    String isEnabled = "";
    ItemFilter mFilter = new ItemFilter();
    public LoadRequestAdapter(Context context,
                              List<LoadRequestConstants> categoryLists,
                              HashMap<Integer, ArrayList<LoadRequestConstants>> constantsHashMap, String isEnabled) {
        mContext = context;
        this.loadRequestConstants = categoryLists;
        this.loadRequestConstantsone = categoryLists;
        inflater = LayoutInflater.from(mContext);
        this.isEnabled = isEnabled;
        selectedArrayList = new ArrayList<>();
        Const.loadRequestConstantsList = categoryLists;
    }
    public class ViewHolder {
        TextView itemName;
        TextView category;
        TextView cases;
        TextView units;
        ImageView categoryImage;
        LinearLayout rl_item;
    }
    @Override
    public int getCount() {
        return loadRequestConstants.size();
    }
    @Override
    public LoadRequestConstants getItem(int position) {
        return loadRequestConstants.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            Log.v("count", getCount() + "");
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_loadrequest_items, null);
            // Locate the TextViews in listview_item.xml
            holder.rl_item = (LinearLayout) view.findViewById(R.id.rl_item);
            holder.itemName = (TextView) view.findViewById(R.id.tvItemName);
            // holder.category = (TextView) view.findViewById(R.id.tvCategory);
            holder.cases = (TextView) view.findViewById(R.id.tvCases);
            holder.units = (TextView) view.findViewById(R.id.tvUnit);
            // Locate the ImageView in listview_item.xml
            holder.categoryImage = (ImageView) view.findViewById(R.id.categoryImage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.itemName.setText(loadRequestConstants.get(position).getItemName());
        // holder.category.setText(loadRequestConstants.get(position).getCategory());
        holder.cases.setText(loadRequestConstants.get(position)
                .getCases());
        holder.units.setText(loadRequestConstants.get(position)
                .getUnits());
        // Set the results into ImageView
        holder.categoryImage.setImageResource(loadRequestConstants.get(position)
                .getCategoryImage());
        if (PreSaleOrderProceedActivity.from.equals("button")) {
            if (selectedArrayList.contains(position)) {
                holder.rl_item.setBackgroundColor(Color.GRAY);
            } else {
                holder.rl_item.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
            }
        }
        setEnabled(holder);
        holder.cases.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EditText et = (EditText) v.findViewById(R.id.tvCases);
                    loadRequestConstants.get(position).setCases(et.getText().toString());
                    loadRequestConstantsone.get(position).setCases(et.getText().toString());
                    Const.loadRequestConstantsList = loadRequestConstantsone;
                }
            }
        });
        holder.units.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EditText et = (EditText) v.findViewById(R.id.tvUnit);
                    loadRequestConstants.get(position).setUnits(et.getText().toString());
                    loadRequestConstantsone.get(position).setUnits(et.getText().toString());
                    Const.loadRequestConstantsList = loadRequestConstantsone;
                }
            }
        });
        holder.rl_item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreSaleOrderProceedActivity.from.equals("button")) {
                    if (selectedArrayList.contains(position)) {
                        selectedArrayList.remove(position);
                        holder.rl_item.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                    } else {
                        selectedArrayList.add(position);
                        holder.rl_item.setBackgroundColor(Color.GRAY);
                    }
                    List<LoadRequestConstants> constantses = new ArrayList<LoadRequestConstants>();
                    for (int i = 0; i < loadRequestConstantsone.size(); i++) {
                        if (selectedArrayList.contains(Integer.parseInt(loadRequestConstantsone.get(i).getId()))) {
                            LoadRequestConstants constants = loadRequestConstantsone.get(i);
                            constantses.add(constants);
                        }
                    }
                    Const.loadRequestConstantsList = constantses;
                }
            }
        });
        return view;
    }
    private void setEnabled(ViewHolder holder) {
        if (isEnabled.equals("no")) {
            holder.cases.setEnabled(false);
            holder.units.setEnabled(false);
        } else if (isEnabled.equals("yes")) {
            holder.cases.setEnabled(true);
            holder.units.setEnabled(true);
        }
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
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {
                List<LoadRequestConstants> filteredItems =
                        new ArrayList<>();
                for (int i = 0, l = loadRequestConstants.size(); i < l; i++) {
                    // ArrayList<HashMap<String, String>> p =
                    // originalList.get(i);
                    String p = loadRequestConstants.get(i).getItemName().toLowerCase();
                    if (p.contains(constraint))
                        filteredItems.add(loadRequestConstants.get(i));
                }
                Log.v("DataAdapter", "not blank");
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.count = loadRequestConstantsone.size();
                    result.values = loadRequestConstantsone;
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
            loadRequestConstants = (List<LoadRequestConstants>) results.values;
            notifyDataSetChanged();
//            for (int i = 0, l = dataList.size(); i < l; i++)
//                dataList.get(i);
            //add(productList.get(i));
            notifyDataSetInvalidated();
        }
    }




    /*// Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        dataList.clear();
        if (charText.length() == 0) {
            dataList.addAll(preSaleOrderArraylist);
        } else {
            for (LoadRequestConstants lrc : preSaleOrderArraylist) {
                if (lrc.getCategory().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    dataList.add(lrc);
                }
            }
        }
        notifyDataSetChanged();

    }*/
}