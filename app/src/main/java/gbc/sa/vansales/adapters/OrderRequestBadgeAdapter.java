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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.LoadSummaryActivity;
import gbc.sa.vansales.activities.LoadVerifyActivity;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.models.OrderRequest;
import gbc.sa.vansales.utils.DatabaseHandler;
/**
 * Created by Rakshit on 29-Dec-16.
 */
public class OrderRequestBadgeAdapter extends ArrayAdapter<OrderRequest> {
    private ArrayList<OrderRequest> loadRequestList;
    private String origin;
    private int pos;
    private int lastFocussedPosition = -1;
    public OrderRequestBadgeAdapter(Context context, ArrayList<OrderRequest> loadRequests,String origin){

        super(context, R.layout.activity_loadrequest_items, loadRequests);
        this.loadRequestList = loadRequests;
        this.origin = origin;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ArrayList<OrderRequest> dataList = this.loadRequestList;
        pos = position;
        final ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_loadrequest_items,parent,false);
            // get all UI view
            holder = new ViewHolder();
            holder.rl_item = (LinearLayout)convertView.findViewById(R.id.rl_item);
            holder.itemName = (TextView) convertView.findViewById(R.id.tvItemName);
            holder.cases = (EditText) convertView.findViewById(R.id.tvCases);
            holder.units = (EditText) convertView.findViewById(R.id.tvUnit);
            holder.categoryImage = (ImageView) convertView.findViewById(R.id.categoryImage);
            // set tag for holder
            convertView.setTag(holder);
        }
        else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }
        final OrderRequest loadRequest = loadRequestList.get(pos);
        holder.itemName.setText(loadRequest.getItemName());


        holder.casestextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadRequestList.get(pos).setCases(s.toString());
                int price = 1;
                if(!(s.toString().isEmpty()||s.toString()==null||s.toString().equals(""))){
                    price = price*Integer.parseInt(s.toString());
                }
                loadRequestList.get(pos).setPrice(String.valueOf(price));
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
                int price = loadRequestList.get(pos).getPrice()!=null?Integer.parseInt(loadRequestList.get(pos).getPrice().toString()):1;
                if(!(s.toString().isEmpty()||s.toString()==null||s.toString().equals(""))){
                    price = price==1?Integer.parseInt(s.toString())*2:Integer.parseInt(s.toString())*2+price;
                }
                loadRequestList.get(pos).setUnits(s.toString());
                loadRequestList.get(pos).setPrice(String.valueOf(price));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        if(this.origin.equalsIgnoreCase("list")){
            holder.cases.setEnabled(false);
            holder.units.setEnabled(false);
        }
        holder.cases.addTextChangedListener(holder.casestextWatcher);
        holder.cases.setText(loadRequestList.get(pos).getCases());
        holder.units.addTextChangedListener(holder.unitsTextWatcher);
        holder.units.setText(loadRequestList.get(pos).getUnits());

        // holder.units.setText(loadRequest.getUnits());
        // Set the results into ImageView
        holder.categoryImage.setImageResource(R.drawable.beraincategory);
        return convertView;
    }

    public class ViewHolder {
        TextView itemName;
        EditText cases;
        EditText units;
        ImageView categoryImage;
        LinearLayout rl_item;
        public TextWatcher casestextWatcher;
        public TextWatcher unitsTextWatcher;

        /*public ViewHolder(View v) {
            rl_item = (LinearLayout)v.findViewById(R.id.rl_item);
            itemName = (TextView) v.findViewById(R.id.tvItemName);
            cases = (EditText) v.findViewById(R.id.tvCases);
            units = (EditText) v.findViewById(R.id.tvUnit);
            categoryImage = (ImageView) v.findViewById(R.id.categoryImage);
        }*/
    }

}
