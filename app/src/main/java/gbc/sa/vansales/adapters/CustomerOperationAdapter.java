package gbc.sa.vansales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import gbc.sa.vansales.R;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.utils.RoundedImageView;

/**
 * Created by eheuristic on 12/3/2016.
 */

public class CustomerOperationAdapter extends BaseAdapter {


    Context context;

    String strText[];
    String from = "";
    int resarr[];

    public CustomerOperationAdapter(Context context, String[] strText, int resarr[], String from) {

        this.context = context;
        this.strText = strText;
        this.resarr = resarr;
        this.from = from;

    }


    @Override
    public int getCount() {
        return strText.length;
    }

    @Override
    public Object getItem(int position) {
        return strText[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            if(from.equals("InformationsActivity"))
            {
                convertView = inflater.inflate(R.layout.information_grid, null);

            }
            else
            {
                convertView = inflater.inflate(R.layout.custom_grid, null);
            }



        }

        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.ll_items);
        ImageView roundedImageView = (ImageView) convertView.findViewById(R.id.iv_round);
        roundedImageView.setImageResource(resarr[position]);
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_iv_title);
        tv_title.setText(strText[position]);


        if (from.equals("CustomerDetailActivity")) {

           
                layout.setBackgroundColor(Color.WHITE);

        } else if (from.equals("SalesInvoiceOptionActivity")) {

        }

        return convertView;
    }
}
