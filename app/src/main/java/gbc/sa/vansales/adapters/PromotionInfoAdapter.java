package gbc.sa.vansales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;

/**
 * Created by eheuristic on 12/5/2016.
 */

public class PromotionInfoAdapter extends BaseAdapter {


    Context context;
    int resource;
    int item;




    ArrayList<String> salesArrayList;

    String strarr[] = {"50% AMC Invoice Discount", "20% FOC Discount", "10% Other Discount"};


    public PromotionInfoAdapter(Context context, int item, int resource) {
        this.context = context;

        this.resource = resource;
        this.item=item;


        Log.v("adapter", "called");
    }




    @Override
    public int getCount() {
        return item;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.v("size", getCount() + "");




            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);

                TextView tv = (TextView) convertView.findViewById(R.id.tv_product);

                LinearLayout ll_promotion = (LinearLayout) convertView.findViewById(R.id.ll_promotion);


                View view = (View) convertView.findViewById(R.id.view);
                if (position == 1) {
                    view.setBackgroundColor(Color.RED);

                } else {
                    view.setBackgroundColor(Color.BLUE);

                }



                tv.setText(strarr[position]);



        return convertView;
    }







}
