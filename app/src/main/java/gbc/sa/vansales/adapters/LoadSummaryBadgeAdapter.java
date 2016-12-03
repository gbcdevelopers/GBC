package gbc.sa.vansales.adapters;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

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
public class LoadSummaryBadgeAdapter extends ArrayAdapter<LoadSummary> {
    private LoadSummaryActivity activity;
    private LoadVerifyActivity loadVerifyActivity;
    private ArrayList<LoadSummary> loadSummaryList;
    private ArrayList<LoadSummary> tempList;


    public LoadSummaryBadgeAdapter(Context context, ArrayList<LoadSummary> loadSummaries){
        super(context, R.layout.badge_load_summary, loadSummaries);
        if(context instanceof LoadSummaryActivity){
            this.activity = (LoadSummaryActivity) context;
        }
        else if(context instanceof LoadVerifyActivity){
            this.loadVerifyActivity = (LoadVerifyActivity) context;
        }

        this.loadSummaryList = loadSummaries;
        this.tempList = new ArrayList<>();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.badge_load_summary,parent,false);
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

        holder.item_code.setText(loadSummary.getItemCode());
        holder.item_description.setText(loadSummary.getItemDescription());
        holder.quantity_cases.setText(loadSummary.getQuantityCases());
        holder.quantity_units.setText(loadSummary.getQuantityUnits());
        holder.btn_accept.setOnClickListener(onAcceptListener(position, holder));
        holder.btn_reject.setOnClickListener(onRejectListener(position,holder));

        return convertView;
    }

    private View.OnClickListener onAcceptListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showEditDialog(position, holder);
                tempList.add(loadSummaryList.get(position));
                loadSummaryList.remove(position);
                holder.swipeLayout.close();
                activity.updateAdapter(tempList);
            }
        };
    }

    private View.OnClickListener onRejectListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVarianceDialog(position, holder);
            }
        };
    }

    private void showVarianceDialog(final int position, final ViewHolder holder) {

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.alert_dialog_variance_load);

        Button btn_update = (Button)dialog.findViewById(R.id.btn_update_alert);
        Button btn_cancel = (Button)dialog.findViewById(R.id.btn_cancel_alert);

        TextView tv_item_code = (TextView)dialog.findViewById(R.id.lbl_item_code_alert_variance);
        TextView tv_item_description = (TextView)dialog.findViewById(R.id.lbl_item_description_alert_variance);
        final EditText et_quantity_case = (EditText)dialog.findViewById(R.id.et_quantitycs_alert_variance);
        final EditText et_quantity_unit = (EditText)dialog.findViewById(R.id.et_quantitybt_alert_variance);

        tv_item_code.setText(loadSummaryList.get(position).getItemCode());
        tv_item_description.setText(loadSummaryList.get(position).getItemDescription());
        et_quantity_case.setText(loadSummaryList.get(position).getQuantityCases());
        et_quantity_unit.setText(loadSummaryList.get(position).getQuantityUnits());

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSummaryList.get(position).setQuantityCases(et_quantity_case.getText().toString().trim());
                loadSummaryList.get(position).setQuantityUnits(et_quantity_unit.getText().toString().trim());

                activity.updateAdapter(tempList);
                holder.swipeLayout.close();
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.swipeLayout.close();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private class ViewHolder{
        private TextView item_code;
        private TextView item_description;
        private TextView quantity_cases;
        private TextView quantity_units;
        private View btn_accept;
        private View btn_reject;
        private SwipeLayout swipeLayout;

        public ViewHolder(View v) {
            swipeLayout = (SwipeLayout)v.findViewById(R.id.swipe_layout);
            btn_accept = v.findViewById(R.id.accept);
            btn_reject = v.findViewById(R.id.reject);
            item_code = (TextView) v.findViewById(R.id.lbl_item_code);
            item_description = (TextView) v.findViewById(R.id.lbl_item_description);
            quantity_cases = (TextView) v.findViewById(R.id.lbl_quantity_cases);
            quantity_units = (TextView) v.findViewById(R.id.lbl_quantity_units);

            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        }
    }
}
