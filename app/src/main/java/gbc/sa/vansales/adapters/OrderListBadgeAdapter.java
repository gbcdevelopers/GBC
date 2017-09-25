package gbc.sa.vansales.adapters;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.DeliveryActivity;
import gbc.sa.vansales.activities.PreSaleOrderActivity;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.utils.Settings;
/**
 * Created by Rakshit on 03-Jan-17.
 */
public class OrderListBadgeAdapter extends ArrayAdapter<OrderList> {
    private ArrayList<OrderList> orders;

    public OrderListBadgeAdapter(Context context, ArrayList<OrderList> orders){

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

        OrderList order = getItem(position);
        /*SpannableString asterisk = new SpannableString(" * ");
        asterisk.setSpan(new ForegroundColorSpan(Color.RED),0, 2, 0);
        holder.order_id.setText(order.getOrderId().contains(Settings.getString(App.ROUTE))?"Order No : " + order.getOrderId() + asterisk: order.getOrderId());
        */
        holder.order_id.setText(getContext().getString(R.string.order_no) + " : " + order.getOrderId());
        holder.order_date.setText(getContext().getString(R.string.order_date) + " : " + order.getOrderDate());
        return convertView;
    }




    private class ViewHolder{
        private TextView order_id;
        private TextView order_date;

        public ViewHolder(View v) {

            order_id = (TextView) v.findViewById(R.id.tv_delivery);
            order_date = (TextView) v.findViewById(R.id.tv_del_date);
        }
    }
}
