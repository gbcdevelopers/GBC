package gbc.sa.vansales.adapters;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.DashboardActivity;
import gbc.sa.vansales.activities.SelectOperationActivity;
import gbc.sa.vansales.activities.ShopStatusActivity;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.ShopStatus;
import gbc.sa.vansales.views.TextViewWithLabel;
/**
 * Created by Rakshit on 16-Nov-16.
 */
public class ShopStatusBadgeAdapter extends ArrayAdapter<ShopStatus> {
    public ShopStatusBadgeAdapter(Context context, ArrayList<ShopStatus> shopstatus){
        super(context, R.layout.badge_shop_status,shopstatus);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.badge_shop_status,parent,false);
        }

        final ShopStatus status = getItem(position);
        TextView status_text = (TextView)convertView.findViewById(R.id.lbl_shop_status);
        RadioButton radio_selected = (RadioButton)convertView.findViewById(R.id.radio_btn_shopstatus);
        status_text.setText(status.getStatusText());
        radio_selected.setSelected(status.isSelected());

        radio_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SelectOperationActivity.class);
                getContext().startActivity(intent);
               // ShopStatusActivity.callback(status.getStatusID());
            }
        });

        return convertView;
    }
}
