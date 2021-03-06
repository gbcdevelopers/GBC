package gbc.sa.vansales.adapters;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.DeliveryActivity;
import gbc.sa.vansales.activities.PreSaleOrderActivity;
import gbc.sa.vansales.models.DeliveryOrderList;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.models.OrderList;
/**
 * Created by Rakshit on 03-Jan-17.
 */
public class DeliveryListBadgeAdapter extends ArrayAdapter<DeliveryOrderList> {
    private ArrayList<DeliveryOrderList> orders;

    public DeliveryListBadgeAdapter(Context context, ArrayList<DeliveryOrderList> orders){

        super(context, R.layout.custom_delivery, orders);
        this.orders = orders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_delivery,parent,false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        }
        else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        DeliveryOrderList order = getItem(position);

        holder.order_id.setText("Delivery No : " + StringUtils.stripStart(order.getOrderId(), "0"));
        holder.order_date.setText("Delivery Date : " + order.getOrderDate());
        if(order.getOrderStatus().equals("true")){
            holder.red_arrow.setImageResource(R.drawable.green_tick_icon);
        }
        else if(order.getOrderStatus().equals("false")){

        }
        else if(order.getOrderStatus().equals("deleted")){
            holder.red_arrow.setImageResource(R.drawable.green_tick_icon_cross);
        }

        return convertView;
    }




    private class ViewHolder{
        private TextView order_id;
        private TextView order_date;
        private ImageView red_arrow;

        public ViewHolder(View v) {

            order_id = (TextView) v.findViewById(R.id.tv_delivery);
            order_date = (TextView) v.findViewById(R.id.tv_del_date);
            red_arrow = (ImageView)v.findViewById(R.id.red_arrow);
        }
    }
}
