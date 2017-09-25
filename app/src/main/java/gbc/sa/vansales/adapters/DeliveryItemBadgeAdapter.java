package gbc.sa.vansales.adapters;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.DeliveryActivity;
import gbc.sa.vansales.activities.DeliveryOrderActivity;
import gbc.sa.vansales.activities.LoadSummaryActivity;
import gbc.sa.vansales.activities.PreSaleOrderActivity;
import gbc.sa.vansales.models.DeliveryItem;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.models.OrderList;
/**
 * Created by Rakshit on 03-Jan-17.
 */
public class DeliveryItemBadgeAdapter extends ArrayAdapter<DeliveryItem> {
    private ArrayList<DeliveryItem> deliveryItems;
    private DeliveryOrderActivity activity;
    private int pos;
    public DeliveryItemBadgeAdapter(Context context, ArrayList<DeliveryItem> deliveryItems) {
        super(context, R.layout.badge_delivery_list, deliveryItems);
        if (context instanceof DeliveryOrderActivity) {
            this.activity = (DeliveryOrderActivity) context;
        }
        this.deliveryItems = deliveryItems;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        pos = position;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.badge_delivery_list, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }
        DeliveryItem deliveryItem = getItem(position);
        holder.itemDescription.setText(deliveryItem.getItemDescription());
        holder.itemCase.setText(deliveryItems.get(pos).getItemCase());
        holder.itemUnit.setText(deliveryItems.get(pos).getItemUnits());
        holder.item_code.setText(getContext().getString(R.string.item_code) + " - " + StringUtils.stripStart(deliveryItem.getMaterialNo(), "0"));
        if(!deliveryItems.get(pos).getItemCase().equals("0")){
            holder.item_price.setText(getContext().getString(R.string.price) + " - " + String.valueOf(Float.parseFloat(deliveryItems.get(pos).getAmount())*Float.parseFloat(deliveryItems.get(pos).getItemCase())));
        }
        else{
            holder.item_price.setText(getContext().getString(R.string.price) + " - " + String.valueOf(Float.parseFloat(deliveryItems.get(pos).getAmount())));
        }
        return convertView;
    }
    private class ViewHolder {
        private TextView itemDescription;
        private TextView itemCase;
        private TextView itemUnit;
        private TextView item_code;
        private TextView item_price;
        public TextWatcher casestextWatcher;
        public TextWatcher unitsTextWatcher;
        public ViewHolder(View v) {
            itemDescription = (TextView) v.findViewById(R.id.tv_item_description);
            itemCase = (TextView) v.findViewById(R.id.tv_item_case);
            itemUnit = (TextView) v.findViewById(R.id.tv_item_unit);
            item_code = (TextView) v.findViewById(R.id.lbl_item_code);
            item_price = (TextView)v.findViewById(R.id.tv_price);
        }
    }
}
