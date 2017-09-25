package gbc.sa.vansales.adapters;
import android.content.Context;
import android.graphics.drawable.Drawable;
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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import gbc.sa.vansales.App;
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
    private int pos;

    public LoadRequestBadgeAdapter(Context context, ArrayList<LoadRequest> loadRequests){

        super(context, R.layout.activity_loadrequest_items, loadRequests);
        this.loadRequestList = loadRequests;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ArrayList<LoadRequest> dataList = this.loadRequestList;
        pos = position;
        ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_loadrequest_items,parent,false);
            // get all UI view
            holder = new ViewHolder();
            //holder.rl_item = (LinearLayout)convertView.findViewById(R.id.rl_item);
            holder.itemCode = (TextView)convertView.findViewById(R.id.tv_item);
            holder.itemName = (TextView) convertView.findViewById(R.id.tvItemName);
            holder.cases = (TextView) convertView.findViewById(R.id.tvCases);
            holder.units = (TextView) convertView.findViewById(R.id.tvUnit);
            holder.categoryImage = (ImageView) convertView.findViewById(R.id.categoryImage);
            holder.item_price = (TextView)convertView.findViewById(R.id.tv_price);
            // set tag for holder
            convertView.setTag(holder);
        }
        else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }
        final LoadRequest loadRequest = loadRequestList.get(pos);
        holder.itemCode.setText(getContext().getString(R.string.item_code) + " - " + StringUtils.stripStart(loadRequest.getMaterialNo(), "0"));
        holder.itemName.setText(loadRequest.getItemName());

        /*else if(loadRequestList.get(pos).getUom().equals(App.BOTTLES_UOM)){
            holder.cases.setEnabled(false);
        }*/

        holder.cases.setText(loadRequestList.get(pos).getCases());
        holder.units.setText(loadRequestList.get(pos).getUnits());
        if(!loadRequestList.get(pos).getCases().equals("0")){
            holder.item_price.setText(getContext().getString(R.string.price) + " - " + String.valueOf(Float.parseFloat(loadRequestList.get(pos).getPrice())*Float.parseFloat(loadRequestList.get(pos).getCases())));
        }
        else{
            holder.item_price.setText(getContext().getString(R.string.price) + " - " + String.valueOf(Float.parseFloat(loadRequestList.get(pos).getPrice())));
        }
       // holder.units.setText(loadRequest.getUnits());
        // Set the results into ImageView
        String uri = "@drawable/a"+StringUtils.stripStart(loadRequestList.get(pos).getMaterialNo(),"0");
        int imageResource = getContext().getResources().getIdentifier(uri,null,getContext().getPackageName());
        Drawable res = getContext().getResources().getDrawable(imageResource);
        holder.categoryImage.setImageDrawable(res);
        //holder.categoryImage.setImageResource(R.drawable.beraincategory);
        return convertView;
    }

    public class ViewHolder {
        TextView itemCode;
        TextView itemName;
        TextView cases;
        TextView units;
        ImageView categoryImage;
        LinearLayout rl_item;
        public TextWatcher casestextWatcher;
        public TextWatcher unitsTextWatcher;
        TextView item_price;

        /*public ViewHolder(View v) {
            rl_item = (LinearLayout)v.findViewById(R.id.rl_item);
            itemName = (TextView) v.findViewById(R.id.tvItemName);
            cases = (EditText) v.findViewById(R.id.tvCases);
            units = (EditText) v.findViewById(R.id.tvUnit);
            categoryImage = (ImageView) v.findViewById(R.id.categoryImage);
        }*/
    }

}
