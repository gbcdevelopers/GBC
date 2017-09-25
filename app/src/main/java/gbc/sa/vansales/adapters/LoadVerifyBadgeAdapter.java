package gbc.sa.vansales.adapters;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Text;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.LoadSummaryActivity;
import gbc.sa.vansales.activities.LoadVerifyActivity;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.views.TextViewWithLabel;
/**
 * Created by Rakshit on 19-Nov-16.
 */
public class LoadVerifyBadgeAdapter extends ArrayAdapter<LoadSummary> {
    private LoadVerifyActivity activity;
    private LoadVerifyActivity loadVerifyActivity;
    private ArrayList<LoadSummary> loadSummaryList;
    private ArrayList<LoadSummary> tempList;


    public LoadVerifyBadgeAdapter(Context context, ArrayList<LoadSummary> loadSummaries){
        super(context, R.layout.badge_load_verify, loadSummaries);
        this.activity = (LoadVerifyActivity)context;
        this.loadSummaryList = loadSummaries;
        this.tempList = new ArrayList<>();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.badge_load_verify,parent,false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        }
        else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        LoadSummary loadSummary = getItem(position);
       /* TextView item_code = (TextView)convertView.findViewById(R.id.lbl_item_code);
        TextView item_description = (TextView)convertView.findViewById(R.id.lbl_item_description);
        TextView quantity_cs = (TextView)convertView.findViewById(R.id.lbl_quantity_cases);
        TextView quantity_bt = (TextView)convertView.findViewById(R.id.lbl_quantity_units);*/

        holder.item_code.setText(getContext().getString(R.string.item_code) + " - " + StringUtils.stripStart(loadSummary.getMaterialNo(),"0"));
        holder.item_description.setText(loadSummary.getItemDescription());
        holder.quantity_cases.setText(loadSummary.getQuantityCases());
        holder.quantity_units.setText(loadSummary.getQuantityUnits());
        String[] caseArray = new String[2];
        caseArray = loadSummary.getQuantityCases().split("\\|");
        holder.item_price.setText(getContext().getString(R.string.price) + " - " + String.valueOf(Float.parseFloat(loadSummary.getPrice())*Float.parseFloat(caseArray[1].toString())));
        return convertView;
    }




    private class ViewHolder{
        private TextView item_code;
        private TextView item_description;
        private TextView quantity_cases;
        private TextView quantity_units;
        private TextView item_price;

        public ViewHolder(View v) {

            item_code = (TextView) v.findViewById(R.id.lbl_item_code);
            item_description = (TextView) v.findViewById(R.id.lbl_item_description);
            quantity_cases = (TextView) v.findViewById(R.id.lbl_quantity_cases);
            quantity_units = (TextView) v.findViewById(R.id.lbl_quantity_units);
            item_price = (TextView)v.findViewById(R.id.lbl_item_price);
        }
    }
}
