package gbc.sa.vansales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import gbc.sa.vansales.R;
import gbc.sa.vansales.utils.RoundedImageView;

/**
 * Created by eheuristic on 12/3/2016.
 */

public class CustomerOperationAdapter extends BaseAdapter {


    Context context;

    String strText[];
    String from="";

     public CustomerOperationAdapter(Context context,String[] strText,String from)
    {
        this.context=context;

        this.strText=strText;
        this.from=from;

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


        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView=inflater.inflate(R.layout.custom_grid,null);

        }

        LinearLayout layout=(LinearLayout)convertView.findViewById(R.id.ll_items);
        RoundedImageView roundedImageView=(RoundedImageView)convertView.findViewById(R.id.iv_round);
        roundedImageView.setImageResource(R.drawable.driver);
        TextView tv_title=(TextView)convertView.findViewById(R.id.tv_iv_title);
        tv_title.setText(strText[position]);


        if(from.equals("CustomerDetailActivity")) {

            if (position == 1) {
                layout.setBackgroundResource(R.drawable.filled_gray_back);

            } else {
                layout.setBackgroundResource(R.drawable.round_buttion);
            }
        }

        return convertView;
    }
}
