package gbc.sa.vansales.adapters;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.PrintDocumentActivity;
import gbc.sa.vansales.models.LoadDeliveryHeader;
import gbc.sa.vansales.models.Print;
/**
 * Created by Rakshit on 13-Jan-17.
 */
public class PrintAdapter extends ArrayAdapter<Print> {
    private LayoutInflater mInflater;
    private boolean isChecked = false;

    public PrintAdapter(Context context, ArrayList<Print> prints) {
        super(context, R.layout.item_print_document, prints);
        mInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public PrintAdapter(Context context, ArrayList<Print> prints, boolean isChecked) {

        super(context, R.layout.item_print_document, prints);
        this.isChecked = isChecked;
        mInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_print_document, null);
            holder = new ViewHolder();
            holder.checkbox_print_doc = (CheckBox)convertView.findViewById(R.id.checkbox_print_doc);
            holder.customer_id = (TextView) convertView.findViewById(R.id.txt_print_doc_customer_id);
            holder.referenceNumber = (TextView) convertView.findViewById(R.id.txt_print_doc_customer_trans);
            holder.transactionType = (TextView)convertView.findViewById(R.id.txt_print_doc_customer_type);
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
        Log.e("Is checked","" + isChecked);
        holder.checkbox_print_doc.setChecked(isChecked);

        return convertView;
    }

    static class ViewHolder {
        CheckBox checkbox_print_doc;
        TextView customer_id;
        TextView referenceNumber;
        TextView transactionType;
    }
}
