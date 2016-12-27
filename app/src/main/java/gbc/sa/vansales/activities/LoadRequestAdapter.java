package gbc.sa.vansales.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

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


    public static String isEnabled = "no";

    ItemFilter mFilter=new ItemFilter();

    public LoadRequestAdapter(Context context,
                           List<LoadRequestConstants> categoryLists,
                              HashMap<Integer,ArrayList<LoadRequestConstants>> constantsHashMap,String from) {


        Toast.makeText(context,"size"+categoryLists.size(),Toast.LENGTH_SHORT).show();
        mContext = context;
        this.loadRequestConstants = categoryLists;
        this.loadRequestConstantsone = categoryLists;
        inflater = LayoutInflater.from(mContext);

        Const.loadRequestConstantsList=categoryLists;
    }

    public class ViewHolder {
        TextView itemName;
        TextView category;
        EditText cases;
        EditText units;
        ImageView categoryImage;
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

            Log.v("count",getCount()+"");
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_loadrequest_items, null);
            // Locate the TextViews in listview_item.xml
            holder.itemName = (TextView) view.findViewById(R.id.tvItemName);
            holder.category = (TextView) view.findViewById(R.id.tvCategory);
            holder.cases = (EditText) view.findViewById(R.id.tvCases);
            holder.units = (EditText) view.findViewById(R.id.tvUnit);
            // Locate the ImageView in listview_item.xml
            holder.categoryImage = (ImageView) view.findViewById(R.id.categoryImage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.itemName.setText(loadRequestConstants.get(position).getItemName());
        holder.category.setText(loadRequestConstants.get(position).getCategory());
        holder.cases.setText(loadRequestConstants.get(position)
                .getCases());
        holder.units.setText(loadRequestConstants.get(position)
                .getUnits());
        // Set the results into ImageView
        holder.categoryImage.setImageResource(loadRequestConstants.get(position)
                .getCategoryImage());



       setEnabled(holder);


        holder.cases.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    EditText et =(EditText)v.findViewById(R.id.tvCases);
                    loadRequestConstants.get(position).setCases(et.getText().toString());
                    loadRequestConstantsone.get(position).setCases(et.getText().toString());


                    Const.loadRequestConstantsList=loadRequestConstantsone;
                }
            }
        });



        holder.units.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus)
                {
                    EditText et =(EditText)v.findViewById(R.id.tvUnit);
                    loadRequestConstants.get(position).setUnits(et.getText().toString());
                    loadRequestConstantsone.get(position).setUnits(et.getText().toString());


                    Const.loadRequestConstantsList=loadRequestConstantsone;
                }
            }
        });


        return view;
    }


    private void setEnabled(ViewHolder holder)
    {
        if (isEnabled.equals("yes")) {
                holder.cases.setEnabled(false);
                holder.units.setEnabled(false);
        }else if (isEnabled.equals("no")){
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
            dataList.addAll(arraylist);
        } else {
            for (LoadRequestConstants lrc : arraylist) {
                if (lrc.getCategory().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    dataList.add(lrc);
                }
            }
        }
        notifyDataSetChanged();

    }*/
}