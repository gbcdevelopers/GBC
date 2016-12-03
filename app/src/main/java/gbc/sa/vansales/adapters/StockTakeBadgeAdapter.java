package gbc.sa.vansales.adapters;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.SelectOperationActivity;
import gbc.sa.vansales.activities.StockTakeActivity;
import gbc.sa.vansales.models.Product;
import gbc.sa.vansales.models.ShopStatus;
/**
 * Created by Rakshit on 18-Nov-16.
 */
public class StockTakeBadgeAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> productList;
    private StockTakeActivity activity;

    public StockTakeBadgeAdapter(Context context, ArrayList<Product> products){
        super(context, R.layout.badge_shop_status,products);
     //   tempList = new ArrayList<>();
        this.activity = (StockTakeActivity)context;
        this.productList = products;
    }

    private class ViewHolder{
        private TextView product_description;
        private EditText quantitycs;
        private EditText quantitybt;

        public ViewHolder(View v){
            product_description = (TextView) v.findViewById(R.id.lbl_product_description);
            quantitycs = (EditText) v.findViewById(R.id.et_quantitycs);
            quantitybt = (EditText) v.findViewById(R.id.et_quantitybt);
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.badge_stock_take,parent,false);
            // link the cached views to the convertview
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        }
        else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = getItem(position);

        holder.product_description = (TextView)convertView.findViewById(R.id.lbl_product_description);
        holder.quantitycs = (EditText)convertView.findViewById(R.id.et_quantitycs);
        holder.quantitybt = (EditText)convertView.findViewById(R.id.et_quantitybt);

        holder.quantitycs.addTextChangedListener(onQuantityCSChanger(position, holder));
        holder.product_description.setText(product.getProductDescription());
        holder.quantitycs.setText(productList.get(position).getQuantityCS());
      //  holder.quantitybt.setText(productList.get(position).getQuantityBT());

        return convertView;
    }

    private TextWatcher onQuantityCSChanger(final int position, final ViewHolder holder) {
       return new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {
           }
           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               /*Product product = productList.get(position);
               product.setQuantityCS(s.toString());
               productList.remove(position);
               productList.add(product);
            //   Log.e("Change",""+productList.get(position).getQuantityCS());
               activity.updateAdapter();*/
           }
           @Override
           public void afterTextChanged(Editable s) {
               Product product = productList.get(position);
               product.setQuantityCS(s.toString());
               productList.remove(position);
               productList.add(position, product);
               Log.e("Change",""+productList.get(position).getQuantityCS());
               activity.updateAdapter();
           }
       };
    }

}
