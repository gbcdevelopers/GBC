package gbc.sa.vansales.adapters;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.LoadSummaryActivity;
import gbc.sa.vansales.activities.LoadVerifyActivity;
import gbc.sa.vansales.models.LoadRequest;
import gbc.sa.vansales.models.LoadSummary;
import gbc.sa.vansales.utils.DatabaseHandler;
/**
 * Created by Rakshit on 29-Dec-16.
 */
public class LoadRequestBadgeAdapter extends ArrayAdapter<LoadRequest> {
    private ArrayList<LoadRequest> loadRequestList;

    public LoadRequestBadgeAdapter(Context context, ArrayList<LoadRequest> loadRequests){

        super(context, R.layout.activity_loadrequest_items, loadRequests);
        this.loadRequestList = loadRequests;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_loadrequest_items,parent,false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        }
        else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }
        final LoadRequest loadRequest = getItem(position);
        holder.itemName.setText(loadRequest.getItemName());
        holder.cases.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                loadRequest.setCases(s.toString());
            }
        });
        holder.cases.setText(loadRequest.getCases());
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

        public ViewHolder(View v) {
            rl_item = (LinearLayout)v.findViewById(R.id.rl_item);
            itemName = (TextView) v.findViewById(R.id.tvItemName);
            cases = (EditText) v.findViewById(R.id.tvCases);
            units = (EditText) v.findViewById(R.id.tvUnit);
            categoryImage = (ImageView) v.findViewById(R.id.categoryImage);
        }
    }
}
