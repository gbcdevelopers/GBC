package gbc.sa.vansales.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import gbc.sa.vansales.R;
import gbc.sa.vansales.data.Const;

/**
 * Created by Muhammad Umair on 02/12/2016.
 */



public class LoadRequestAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<LoadRequestConstants> loadRequestConstants = null;
    private List<LoadRequestConstants> arraylist;
    HashMap<Integer,ArrayList<LoadRequestConstants>> constantsHashMap;

    public LoadRequestAdapter(Context context,
                           List<LoadRequestConstants> categoryLists,HashMap<Integer,ArrayList<LoadRequestConstants>> constantsHashMap) {
        mContext = context;
        this.loadRequestConstants = categoryLists;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = categoryLists;
        this.arraylist.addAll(categoryLists);
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
        // Listen for ListView Item Click


        holder.cases.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){

                    final EditText Caption = (EditText) v;
                    arraylist.get(position).setCases(Caption.getText().toString());
                    Const.loadRequestConstantsList=arraylist;
                }
            }
        });

        holder.units.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){

                    final EditText Caption = (EditText) v;
                    arraylist.get(position).setUnits(Caption.getText().toString());
                    Const.loadRequestConstantsList=arraylist;
                }
            }
        });
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        loadRequestConstants.clear();
        if (charText.length() == 0) {
            loadRequestConstants.addAll(arraylist);
        } else {
            for (LoadRequestConstants lrc : arraylist) {
                if (lrc.getCategory().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    loadRequestConstants.add(lrc);
                }
            }
        }
        notifyDataSetChanged();

    }
}