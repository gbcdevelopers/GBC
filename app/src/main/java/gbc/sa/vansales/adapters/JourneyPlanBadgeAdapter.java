package gbc.sa.vansales.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.views.TextViewWithLabel;

/**
 * Created by Rakshit on 16-Nov-16.
 */
public class JourneyPlanBadgeAdapter extends BaseAdapter implements Filterable {



    ArrayList<Customer> customers;
    ArrayList<Customer> customers_one;
    ItemFilter mFilter = new ItemFilter();
    Context context;
    ListView listView;

    public JourneyPlanBadgeAdapter(Context context, ArrayList<Customer> customers, ListView listView){
//        super(context, R.layout.badge_journey_plan,customers);
        this.customers=customers;
        this.customers_one=customers;
        this.context=context;
        this.listView=listView;
    }

    @Override
    public int getCount() {
        return customers.size();
    }

    @Override
    public Object getItem(int position) {
        return customers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.badge_journey_plan,parent,false);
        }

//        Customer customer = getItem(position);
        Customer customer = customers.get(position);
        Log.v("getview",customers.size()+"--");
        TextViewWithLabel customer_id = (TextViewWithLabel)convertView.findViewById(R.id.customer_id);
        TextViewWithLabel customer_name = (TextViewWithLabel)convertView.findViewById(R.id.customer_name);
        TextViewWithLabel customer_address = (TextViewWithLabel)convertView.findViewById(R.id.customer_address);
        ImageView saleflag = (ImageView)convertView.findViewById(R.id.img_sales);
        ImageView deliveryflag = (ImageView)convertView.findViewById(R.id.img_delivery);
        ImageView collectionflag = (ImageView)convertView.findViewById(R.id.img_collection);
        ImageView returnsflag = (ImageView)convertView.findViewById(R.id.img_merchandize);

        customer_id.setText(customer.getCustomerID());
        customer_name.setText(customer.getCustomerName());
        customer_address.setText(customer.getCustomerAddress());

        if(customer.isSale()){
            saleflag.setVisibility(View.VISIBLE);
            saleflag.setImageResource(R.drawable.sell_icon);
        }
        else{
            saleflag.setVisibility(View.INVISIBLE);
        }

        if(customer.isDelivery()){
            deliveryflag.setVisibility(View.VISIBLE);
            deliveryflag.setImageResource(R.drawable.delivery_icon);
        }
        else{
            deliveryflag.setVisibility(View.INVISIBLE);
        }

        if(customer.isCollection()){
            collectionflag.setVisibility(View.VISIBLE);
            collectionflag.setImageResource(R.drawable.collection_icon);
        }
        else{
            collectionflag.setVisibility(View.INVISIBLE);
        }

        if(customer.isMerchandize()){
            returnsflag.setVisibility(View.VISIBLE);
            returnsflag.setImageResource(R.drawable.return_icon);
        }
        else{
            returnsflag.setVisibility(View.INVISIBLE);
        }



        return convertView;

    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString();
            Log.v("DataAdapter", "constratinst : " + constraint);
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {
                ArrayList<Customer> filteredItems =
                        new ArrayList<>();
                for (int i = 0, l = customers.size(); i < l; i++) {
                    // ArrayList<HashMap<String, String>> p =
                    // originalList.get(i);
                    String p = customers.get(i).getCustomerID();
                    Log.v("DataAdapter", "pppp"+p);
                    if (p.contains(constraint))
                        filteredItems.add(customers.get(i));
                }
                Log.v("DataAdapter", "not blank  : " + filteredItems.size());
                result.count = filteredItems.size();
                result.values = filteredItems;


            } else {
                synchronized (this) {
                    result.count = customers_one.size();
                    result.values = customers_one;
//                    result.values = dataList;
//                    result.count = dataList.size();
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {


            Log.v("DataAdapter", "publishResults" +results.count);
            // users = (List<GraphUser>) results.values;
            //filteredData = (ArrayList<String>) results.values;
            customers = (ArrayList<Customer>) results.values;
//            listView.invalidate();
            notifyDataSetChanged();
            notifyDataSetInvalidated();
            Log.v("DataAdapter", "publishResults" +customers.size());

            //add(productList.get(i));
            /*for (int i = 0, l = customers.size(); i < l; i++) {
                customers.get(i);
            }*/
//            notifyDataSetInvalidated();

        }
    }
}
