package gbc.sa.vansales.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.Collection;
/**
 * Created by Rakshit on 03-Feb-17.
 */
public class BalanceBadgeAdapter extends ArrayAdapter<Collection> {
    private Context context;
    private ArrayList<Collection> collectionsList;
    private int pos;

    public BalanceBadgeAdapter(Context context, ArrayList<Collection> values){
        super(context, R.layout.balance_badge, values);
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
            convertView = inflater.inflate(R.layout.balance_badge,parent,false);
            // get all UI view
            holder = new ViewHolder();
           // holder.tv_customer = (TextView) convertView.findViewById(R.id.tv_customer);
            holder.tv_invoice_no = (TextView) convertView.findViewById(R.id.tv_inv_no);
            holder.tv_invoice_date = (TextView)convertView.findViewById(R.id.tv_inv_date);
            holder.tv_invoice_due_date = (TextView) convertView.findViewById(R.id.tv_due_date);
            holder.tv_invoice_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            holder.tv_amount_due = (TextView)convertView.findViewById(R.id.tv_amount);
            convertView.setTag(holder);

        }
        else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_invoice_no.setText(StringUtils.stripStart(collectionsList.get(pos).getInvoiceNo(), "0"));
        //holder.tv_price.setText("Price:54.00/2.25");
        holder.tv_invoice_date.setText(collectionsList.get(pos).getInvoiceDate());
        holder.tv_invoice_due_date.setText(collectionsList.get(pos).getInvoiceDate());
        //holder.tv_invoice_amount.setText(collectionsList.get(pos).getInvoiceAmount() + " SAR ");
        float amountdue = Float.parseFloat(collectionsList.get(pos).getInvoiceAmount())- Float.parseFloat(collectionsList.get(pos).getAmountCleared());
        holder.tv_invoice_amount.setText(collectionsList.get(pos).getAmountCleared());
        holder.tv_amount_due.setText(String.valueOf(amountdue));
        return convertView;
    }

    public class ViewHolder {
        TextView tv_customer,tv_invoice_no, tv_invoice_date, tv_invoice_due_date, tv_invoice_amount, tv_amount_due;
    }
}
