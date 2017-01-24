package gbc.sa.vansales.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.Collection;
import gbc.sa.vansales.models.Expense;
/**
 * Created by Rakshit on 24-Jan-17.
 */
public class ExpenseAdapter extends ArrayAdapter<Expense> {
    private Context context;
    private ArrayList<Expense> expenseList;
    private int pos;

    public ExpenseAdapter(Context context, ArrayList<Expense> values){
        super(context, R.layout.badge_expense_list, values);
        this.context = context;
        this.expenseList = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ArrayList<Expense> dataList = this.expenseList;
        pos = position;
        ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.badge_expense_list,parent,false);
            // get all UI view
            holder = new ViewHolder();
            holder.tv_expense_category = (TextView) convertView.findViewById(R.id.tv_expense_category);
            holder.tv_expense_reason = (TextView)convertView.findViewById(R.id.tv_expense_reason);
            holder.tv_expense_amount = (TextView) convertView.findViewById(R.id.tv_expense_cost);
            convertView.setTag(holder);

        }
        else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_expense_category.setText(expenseList.get(pos).getExpenseDescription());
        //holder.tv_price.setText("Price:54.00/2.25");
        holder.tv_expense_reason.setText(expenseList.get(pos).getAdditionalReason());
        holder.tv_expense_amount.setText(expenseList.get(pos).getExpenseAmount() + " " + context.getString(R.string.currency));
        return convertView;
    }

    public class ViewHolder {
        TextView tv_expense_reason,tv_expense_category,tv_expense_amount;
    }
}
