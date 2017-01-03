package gbc.sa.vansales.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.models.VanStock;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Rakshit on 03-Jan-17.
 */
public class VanStockBadgeAdapter extends ArrayAdapter<VanStock> {
    private ArrayList<VanStock> vanStocks;

    public VanStockBadgeAdapter(Context context, ArrayList<VanStock> vanStocks){

        super(context, R.layout.activity_vanstock_items, vanStocks);
        this.vanStocks = vanStocks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_vanstock_items,parent,false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        }
        else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        VanStock vanStock = getItem(position);

        holder.item_code.setText(vanStock.getItem_code());
        holder.item_description.setText(UrlBuilder.decodeString(vanStock.getItem_description()));
        holder.item_case.setText(vanStock.getItem_case());
        holder.item_unit.setText(vanStock.getItem_units());
        return convertView;
    }




    private class ViewHolder{
        private TextView item_code;
        private TextView item_description;
        private TextView item_case;
        private TextView item_unit;

        public ViewHolder(View v) {

            item_code = (TextView) v.findViewById(R.id.item);
            item_description = (TextView) v.findViewById(R.id.description);
            item_case = (TextView) v.findViewById(R.id.cases);
            item_unit = (TextView) v.findViewById(R.id.units);
        }
    }
}
