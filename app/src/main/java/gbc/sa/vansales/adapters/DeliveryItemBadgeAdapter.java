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
        if(context instanceof DeliveryOrderActivity){
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

        holder.casestextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                deliveryItems.get(pos).setItemCase(s.toString());
                float price = 1;
                if(!(s.toString().isEmpty()||s.toString()==null||s.toString().equals(""))){
                    price = price*Float.parseFloat(s.toString());
                    activity.calculatePrice();
                }
                deliveryItems.get(pos).setAmount(String.valueOf(price));

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        holder.unitsTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float price = deliveryItems.get(pos).getAmount()!=null?Float.parseFloat(deliveryItems.get(pos).getAmount().toString()):1;
                if(!(s.toString().isEmpty()||s.toString()==null||s.toString().equals(""))){
                    price = price==1?Float.parseFloat(s.toString())*2:Float.parseFloat(s.toString())*2+price;
                    activity.calculatePrice();
                }
                deliveryItems.get(pos).setItemUnits(s.toString());
                deliveryItems.get(pos).setAmount(String.valueOf(price));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        holder.itemCase.addTextChangedListener(holder.casestextWatcher);
        holder.itemCase.setText(deliveryItems.get(pos).getItemCase());
        holder.itemUnit.addTextChangedListener(holder.unitsTextWatcher);
        holder.itemUnit.setText(deliveryItems.get(pos).getItemUnits());


        return convertView;
    }
    private class ViewHolder {
        private TextView itemDescription;
        private EditText itemCase;
        private EditText itemUnit;
        public TextWatcher casestextWatcher;
        public TextWatcher unitsTextWatcher;

        public ViewHolder(View v) {
            itemDescription = (TextView) v.findViewById(R.id.tv_item_description);
            itemCase = (EditText) v.findViewById(R.id.tv_item_case);
            itemUnit = (EditText) v.findViewById(R.id.tv_item_unit);
        }
    }
}
