package gbc.sa.vansales.adapters;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.models.Bank;
import gbc.sa.vansales.models.Reasons;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Rakshit on 17-Jan-17.
 */
public class BankAdapter extends ArrayAdapter<Bank> {
    private Context context;
    private ArrayList<Bank> reasonList;

    public BankAdapter(Context context, int textViewResourceId, ArrayList<Bank> values){
        super(context, android.R.layout.simple_spinner_item, values);
        this.context = context;
        this.reasonList = values;
    }

    public int getCount(){
        return this.reasonList.size();
    }

    public Bank getItem(int position){
        return reasonList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextSize(16);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(UrlBuilder.decodeString(reasonList.get(position).getBankName()));

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextSize(16);
        label.setTextColor(Color.BLACK);
        label.setText(UrlBuilder.decodeString(reasonList.get(position).getBankName()));
        return label;
    }
}
