package gbc.sa.vansales.adapters;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.models.Collection;
import gbc.sa.vansales.models.Reasons;
import gbc.sa.vansales.models.Sales;
/**
 * Created by Rakshit on 23-Jan-17.
 */
public class CollectionAdapter extends ArrayAdapter<Collection> {
    private Context context;
    private ArrayList<Collection> collectionsList;
    private int pos;

    public CollectionAdapter(Context context, ArrayList<Collection> values){
        super(context, R.layout.badge_collection_list_item, values);
        this.context = context;
        this.collectionsList = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ArrayList<Collection> dataList = this.collectionsList;
        pos = position;
        ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.badge_collection_list_item,parent,false);
            // get all UI view
            holder = new ViewHolder();
            holder.tv_invoice_no = (TextView) convertView.findViewById(R.id.tv_invoice_no);
            holder.tv_invoice_date = (TextView)convertView.findViewById(R.id.tv_invoice_date);
            holder.tv_invoice_due_date = (TextView) convertView.findViewById(R.id.tv_due_date);
            holder.tv_invoice_amount = (TextView) convertView.findViewById(R.id.tv_invoice_amount);
            holder.tv_amount_due = (TextView)convertView.findViewById(R.id.tv_amount_due);
            convertView.setTag(holder);

        }
        else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_invoice_no.setText(context.getString(R.string.invoice_no) + " : " + collectionsList.get(pos).getInvoiceNo());
        //holder.tv_price.setText("Price:54.00/2.25");
        holder.tv_invoice_date.setText(getContext().getString(R.string.inv_date) + " : " + collectionsList.get(pos).getInvoiceDate());
        holder.tv_invoice_due_date.setText(getContext().getString(R.string.due_date) + " : " + collectionsList.get(pos).getInvoiceDueDate());
        //holder.tv_invoice_amount.setText(collectionsList.get(pos).getInvoiceAmount() + " SAR ");
        float amountdue = Float.parseFloat(collectionsList.get(pos).getInvoiceAmount())- Float.parseFloat(collectionsList.get(pos).getAmountCleared());
        holder.tv_invoice_amount.setText(collectionsList.get(pos).getAmountCleared());
        holder.tv_amount_due.setText(getContext().getString(R.string.amout_due) + " : " + String.valueOf(amountdue) + " SAR ");
        return convertView;
    }

    public class ViewHolder {
        TextView tv_invoice_no, tv_invoice_date, tv_invoice_due_date, tv_invoice_amount, tv_amount_due;
    }
}
