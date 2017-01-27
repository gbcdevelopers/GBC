package gbc.sa.vansales.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.LoadDeliveryHeader;
import gbc.sa.vansales.views.TextViewWithLabel;
/**
 * Created by Rakshit on 13-Jan-17.
 */
public class CustomerInformationAdapter extends ArrayAdapter<Customer> {
    ArrayList<Customer> visitList;

    private LayoutInflater mInflater;

    public CustomerInformationAdapter(Context context, ArrayList<Customer> visitList) {
        super(context, R.layout.badge_customer_information, visitList);
        mInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.badge_customer_information, null);
            holder = new ViewHolder();
            holder.customerID = (TextViewWithLabel) convertView.findViewById(R.id.customer_id);
            holder.customerName = (TextViewWithLabel) convertView.findViewById(R.id.customer_name);
            holder.customerAddress = (TextViewWithLabel)convertView.findViewById(R.id.customer_address);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Customer customer = getItem(position);
        holder.customerID.setText(StringUtils.stripStart(customer.getCustomerID(),"0"));
        holder.customerName.setText(customer.getCustomerName());
        holder.customerAddress.setText(customer.getCustomerAddress());

        return convertView;
    }

    static class ViewHolder {
        TextViewWithLabel customerID;
        TextViewWithLabel customerName;
        TextViewWithLabel customerAddress;
    }
}
