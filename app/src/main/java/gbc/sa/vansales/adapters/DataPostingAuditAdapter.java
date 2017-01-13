package gbc.sa.vansales.adapters;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.Print;
/**
 * Created by Rakshit on 13-Jan-17.
 */
public class DataPostingAuditAdapter extends ArrayAdapter<Print> {
    private LayoutInflater mInflater;

    public DataPostingAuditAdapter(Context context, ArrayList<Print> prints) {
        super(context, R.layout.item_data_pousting_audit, prints);
        mInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_data_pousting_audit, null);
            holder = new ViewHolder();
            holder.customer_id = (TextView)convertView.findViewById(R.id.txt_print_doc_customer_id);
            holder.referenceNumber = (TextView)convertView.findViewById(R.id.txt_print_doc_customer_trans);
            holder.transactionType = (TextView)convertView.findViewById(R.id.txt_print_doc_customer_type);
            holder.imageView = (ImageView)convertView.findViewById(R.id.img_item_data_pousting);
            holder.checkbox_print_doc=(CheckBox)convertView.findViewById(R.id.checkbox_print_doc);
            holder.ll=(LinearLayout)convertView.findViewById(R.id.ll);
            //  holder.availableLoad = (TextView) convertView.findViewById(R.id.availableLoad);
            // holder.txtStatus = (TextView) convertView.findViewById(R.id.status);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Print print = getItem(position);
        holder.customer_id.setText(print.getCustomer_id());
        holder.referenceNumber.setText(print.getReferenceNumber());
        holder.transactionType.setText(print.getTransactionType());
        if(print.isPosted()){
            holder.imageView.setBackgroundColor(Color.GREEN);
        }
        else{
            holder.imageView.setBackgroundColor(Color.RED);
        }
        holder.checkbox_print_doc.setEnabled(!print.isPosted());

        return convertView;
    }

    static class ViewHolder {
        CheckBox checkbox_print_doc;
        TextView customer_id;
        TextView referenceNumber;
        TextView transactionType;
        LinearLayout ll;
        ImageView imageView;
    }
}
