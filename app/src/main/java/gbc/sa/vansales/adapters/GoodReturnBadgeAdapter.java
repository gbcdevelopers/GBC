package gbc.sa.vansales.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.LoadSummaryActivity;
import gbc.sa.vansales.activities.ReturnsActivity;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.GoodReturn;
import gbc.sa.vansales.views.TextViewWithLabel;
/**
 * Created by Rakshit on 21-Nov-16.
 */
public class GoodReturnBadgeAdapter extends ArrayAdapter<GoodReturn> {
    private ReturnsActivity activity;
    private ArrayList<GoodReturn> returnList;

    public GoodReturnBadgeAdapter(Context context, ArrayList<GoodReturn> items){
        super(context, R.layout.badge_item_return,items);
        this.activity = (ReturnsActivity) context;
        this.returnList = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.badge_item_return,parent,false);
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        }
        else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        GoodReturn item = getItem(position);
        holder.item_code = (TextView)convertView.findViewById(R.id.lbl_item_code);
        holder.item_description = (TextView)convertView.findViewById(R.id.lbl_item_description);
        holder.item_cases = (TextView)convertView.findViewById(R.id.lbl_quantity_cases);
        holder.item_units = (TextView)convertView.findViewById(R.id.lbl_quantity_units);
        holder.return_type = (TextView)convertView.findViewById(R.id.lbl_return_type);
        holder.return_reason = (TextView)convertView.findViewById(R.id.lbl_reason);

        holder.item_code.setText(item.getItemSKU());
        holder.item_description.setText(item.getItemDescription());
        holder.item_cases.setText(item.getQuantityCS());
        holder.item_units.setText(item.getQuantityBT());
        holder.return_type.setText(item.getReturnType());
        holder.return_reason.setText(item.getReason());

        holder.btn_edit.setOnClickListener(onEditListener(position, holder));
        holder.btn_delete.setOnClickListener(onDeleteListener(position,holder));

        return convertView;
    }

    private View.OnClickListener onEditListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showEditDialog(position, holder);

                returnList.remove(position);
                holder.swipeLayout.close();
                activity.updateAdapter();
            }
        };
    }

    private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showEditDialog(position, holder);

                returnList.remove(position);
                holder.swipeLayout.close();
                activity.updateAdapter();
            }
        };
    }

    private class ViewHolder{
        private TextView item_code;
        private TextView item_description;
        private TextView item_cases;
        private TextView item_units;
        private TextView return_type;
        private TextView return_reason;
        private View btn_edit;
        private View btn_delete;
        private SwipeLayout swipeLayout;

        public ViewHolder(View v) {
            swipeLayout = (SwipeLayout)v.findViewById(R.id.swipe_layout);
            btn_edit = v.findViewById(R.id.tv_edit);
            btn_delete = v.findViewById(R.id.tv_delete);
            item_code = (TextView) v.findViewById(R.id.lbl_item_code);
            item_description = (TextView) v.findViewById(R.id.lbl_item_description);
            item_cases = (TextView) v.findViewById(R.id.lbl_quantity_cases);
            item_units = (TextView) v.findViewById(R.id.lbl_quantity_units);
            return_type = (TextView) v.findViewById(R.id.lbl_return_type);
            return_reason = (TextView) v.findViewById(R.id.lbl_reason);

            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        }
    }
}
