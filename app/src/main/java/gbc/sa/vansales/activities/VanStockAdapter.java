package gbc.sa.vansales.activities;

/**
 * Created by Muhammad Umair on 02/12/2016.
 */

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

public class VanStockAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<VanStockConstants> vanStockConstant = null;
    private ArrayList<VanStockConstants> arraylist;

    public VanStockAdapter(Context context,
                           List<VanStockConstants> worlddescriptionlist) {
        mContext = context;
        this.vanStockConstant = worlddescriptionlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<VanStockConstants>();
        this.arraylist.addAll(worlddescriptionlist);
    }

    public class ViewHolder {
        TextView item;
        TextView cases;
        TextView description;
        TextView units;
    }

    @Override
    public int getCount() {
        return vanStockConstant.size();
    }

    @Override
    public VanStockConstants getItem(int position) {
        return vanStockConstant.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_vanstock_items, null);
            // Locate the TextViews in listview_item.xml
            holder.item = (TextView) view.findViewById(R.id.items);
            holder.cases = (TextView) view.findViewById(R.id.cases);
            holder.description = (TextView) view.findViewById(R.id.description);
            holder.units = (TextView) view.findViewById(R.id.units);
            // Locate the ImageView in listview_item.xml
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.item.setText(vanStockConstant.get(position).getvanStockItem());
        holder.cases.setText(vanStockConstant.get(position).getvanStockCase());
        holder.description.setText(vanStockConstant.get(position)
                .getvanStockDescription());
        holder.units.setText(vanStockConstant.get(position)
                .getVanStockUnits());
        // Set the results into ImageView

        return view;
    }

}
