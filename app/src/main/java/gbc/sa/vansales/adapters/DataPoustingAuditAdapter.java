package gbc.sa.vansales.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.DataPoustingAudit;

/**
 * Created by ehs on 22/12/16.
 */

public class DataPoustingAuditAdapter extends BaseAdapter {

    ArrayList<DataPoustingAudit> arrayList;
    Activity activity;

    public DataPoustingAuditAdapter(Activity activity, ArrayList<DataPoustingAudit> arrayList)
    {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.item_data_pousting_audit,viewGroup,false);

        }

        DataPoustingAudit dataPoustingAudit = arrayList.get(i);

        TextView txt_print_doc_customer_id = (TextView)view.findViewById(R.id.txt_print_doc_customer_id);
        TextView txt_print_doc_customer_trans = (TextView)view.findViewById(R.id.txt_print_doc_customer_trans);
        TextView txt_print_doc_customer_type = (TextView)view.findViewById(R.id.txt_print_doc_customer_type);
        ImageView ImageView = (ImageView)view.findViewById(R.id.img_item_data_pousting);

        if (i == 1){
            ImageView.setBackgroundColor(Color.parseColor("#00FF00"));
        }
        if (i == 4){
            ImageView.setBackgroundColor(Color.parseColor("#00FF00"));
        }
        if (i == 6) {
            ImageView.setBackgroundColor(Color.parseColor("#00FF00"));
        }

        txt_print_doc_customer_id.setText(""+dataPoustingAudit.getCustomer_id());
        txt_print_doc_customer_trans.setText(""+dataPoustingAudit.getCustomer_transection());
        txt_print_doc_customer_type.setText(dataPoustingAudit.getType());

        return view;
    }
}
