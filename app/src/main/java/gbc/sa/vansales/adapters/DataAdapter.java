package gbc.sa.vansales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.CustomerData;
import gbc.sa.vansales.utils.UrlBuilder;
import gbc.sa.vansales.views.TextViewWithLabel;

/**
 * Created by eheuristic on 10/10/2016.
 */

public class DataAdapter extends BaseAdapter implements Filterable {

    ArrayList<Integer> intlist;
    ItemFilter mFilter = new ItemFilter();
    private ArrayList<Customer> dataList;
    private ArrayList<Customer> dataListOne;
    private Context mContext;

    public DataAdapter(Context context, ArrayList<Customer> dataList) {
        this.dataList = dataList;
        this.dataListOne = dataList;
        this.mContext = context;

        intlist = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {


        Log.v("size",dataList.size()+"==");

        ItemRowHolder holder = null;
        if (view == null) {
            LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
            view = li.inflate(R.layout.badge_journey_plan, null);
//            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_contact, null);
            holder = new ItemRowHolder();
            holder.horizontal_view=(View)view.findViewById(R.id.view);
            holder.customer_id = (TextViewWithLabel) view.findViewById(R.id.customer_id);
            holder.customer_name = (TextViewWithLabel) view.findViewById(R.id.customer_name);
            holder.customer_address = (TextViewWithLabel) view.findViewById(R.id.customer_address);
            holder.saleflag = (ImageView) view.findViewById(R.id.img_sales);
            holder.deliveryflag = (ImageView) view.findViewById(R.id.img_delivery);
            holder.collectionflag = (ImageView) view.findViewById(R.id.img_collection);
            holder.merchandizeFlag = (ImageView) view.findViewById(R.id.img_merchandize);
            holder.orderFlag = (ImageView) view.findViewById(R.id.img_order);
            holder.newCustomer = (ImageView)view.findViewById(R.id.img_customer_new);



            view.setTag(holder);
        } else {
            holder = (ItemRowHolder) view.getTag();
        }

        Customer customer = dataList.get(i);
        holder.customer_id.setText(StringUtils.stripStart(customer.getCustomerID(), "0"));
        holder.customer_name.setText(UrlBuilder.decodeString(customer.getCustomerName()));
        holder.customer_address.setText(customer.getCustomerAddress());

        if(customer.getPaymentMethod().equals(App.TC_CUSTOMER)){
            holder.horizontal_view.setBackgroundColor(Color.rgb(255,194,0));
        }
        else if(customer.getPaymentMethod().equals(App.CASH_CUSTOMER)){
            holder.horizontal_view.setBackgroundColor(Color.BLUE);
        }
        else if(customer.getPaymentMethod().equals(App.CREDIT_CUSTOMER)){
            holder.horizontal_view.setBackgroundColor(Color.RED);
        }
        else{
            holder.horizontal_view.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.orderFlag.setAlpha(customer.isOrder()?1f:.3f);
        holder.saleflag.setAlpha(customer.isSale()?1f:.3f);
        holder.collectionflag.setAlpha(customer.isCollection()?1f:.3f);
        holder.deliveryflag.setAlpha(customer.isDelivery()?1f:.3f);
        holder.merchandizeFlag.setAlpha(customer.isMerchandize()?1f:.3f);
        holder.newCustomer.setVisibility(customer.isNewCustomer()?View.VISIBLE:View.INVISIBLE);
        return view;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString();
            Log.v("DataAdapter", "constratinst : " + constraint);
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {
                ArrayList<Customer> filteredItems =
                        new ArrayList<>();
                for (int i = 0, l = dataList.size(); i < l; i++) {
                    // ArrayList<HashMap<String, String>> p =
                    // originalList.get(i);
                    String p = dataList.get(i).getCustomerID();
                    if (p.contains(constraint))
                        filteredItems.add(dataList.get(i));
                }
                Log.v("DataAdapter", "not blank");
                result.count = filteredItems.size();
                result.values = filteredItems;

            } else {
                synchronized (this) {
                    result.count = dataListOne.size();
                    result.values = dataListOne;
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
            dataList = (ArrayList<Customer>) results.values;
            notifyDataSetChanged();

//            for (int i = 0, l = dataList.size(); i < l; i++)
//                dataList.get(i);
            //add(productList.get(i));

            notifyDataSetInvalidated();
        }
    }


    public class ItemRowHolder {
        ImageView saleflag;
        ImageView deliveryflag;
        ImageView collectionflag;
        ImageView merchandizeFlag;
        ImageView orderFlag;
        ImageView newCustomer;
        TextViewWithLabel customer_id;
        TextViewWithLabel customer_name;
        TextViewWithLabel customer_address;
        View horizontal_view;
    }

}