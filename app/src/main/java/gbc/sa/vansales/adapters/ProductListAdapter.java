package gbc.sa.vansales.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.data.Const;

import static gbc.sa.vansales.data.Const.addlist;

/**
 * Created by eheuristic on 12/10/2016.
 */

public class ProductListAdapter extends BaseAdapter  {


    Context context;
    int resource;

    ArrayList<String> dataList;
    String from="";




    public ProductListAdapter(Context context,ArrayList<String> item,int resource,String from)
    {
        this.context=context;

        this.resource=resource;
        this.from=from;


        dataList=item;
        Const.addlist.clear();




        Log.v("adapter","called");
    }


    @Override
    public int getCount() {
        return dataList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        Log.v("size",getCount()+"");



         final   Holder holder;
            if(convertView==null) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null);

                holder=new Holder();
                holder.tv_product_name=(TextView)convertView.findViewById(R.id.tv_product);
                holder.chk_product=(CheckBox) convertView.findViewById(R.id.chk_product);



                if(from.equals("category"))
                {
                        holder.chk_product.setVisibility(View.GONE
                        );
                }


                convertView.setTag(holder);
            }
            else {
                holder=(Holder)convertView.getTag();
            }

            holder.tv_product_name.setText(dataList.get(position));


        if (addlist.contains(dataList.get(position))) {
            holder.chk_product.setChecked(true);

        } else {
            holder.chk_product.setChecked(false);

        }


            holder.chk_product.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked)
                    {

                        addlist.add(dataList.get(position));
                        holder.chk_product.setChecked(true);


                    }else {


                        holder.chk_product.setChecked(false);
                        addlist.remove(dataList.get(position));
                    }


                }
            });


        return convertView;
    }



    public class Holder
    {
        TextView tv_product_name;
        CheckBox chk_product;
    }



}
