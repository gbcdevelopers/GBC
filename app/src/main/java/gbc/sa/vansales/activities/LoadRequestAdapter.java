package gbc.sa.vansales.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import gbc.sa.vansales.R;

/**
 * Created by Muhammad Umair on 02/12/2016.
 */



public class LoadRequestAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<LoadRequestConstants> loadRequestConstants = null;
    private ArrayList<LoadRequestConstants> arraylist;

    public LoadRequestAdapter(Context context,
                           List<LoadRequestConstants> categoryLists) {
        mContext = context;
        this.loadRequestConstants = categoryLists;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<LoadRequestConstants>();
        this.arraylist.addAll(categoryLists);
    }

    public class ViewHolder {
        TextView itemName;
        TextView category;
        TextView cases;
        TextView units;
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
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_loadrequest_items, null);
            // Locate the TextViews in listview_item.xml
            holder.itemName = (TextView) view.findViewById(R.id.tvItemName);
            holder.category = (TextView) view.findViewById(R.id.tvCategory);
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
        holder.category.setText(loadRequestConstants.get(position).getCategory());
        holder.cases.setText(loadRequestConstants.get(position)
                .getCases());
        holder.units.setText(loadRequestConstants.get(position)
                .getUnits());
        // Set the results into ImageView
        holder.categoryImage.setImageResource(loadRequestConstants.get(position)
                .getCategoryImage());
        // Listen for ListView Item Click
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