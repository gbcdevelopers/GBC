package gbc.sa.vansales.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.Fragment.ShelfFragment;
import gbc.sa.vansales.Fragment.StoreFragment;
import gbc.sa.vansales.Fragment.VisitAllFragment;
import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.ShelfStockActivity;
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
                        holder.chk_product.setVisibility(View.GONE);
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
                        Log.e("check",dataList.get(position));


                        if(ShelfStockActivity.tab_position==0)
                        {


                            boolean isExists=false;
                            for(int i=0;i<ShelfFragment.arrayList.size();i++)
                            {

                                Log.e("check",dataList.get(position)+" "+ShelfFragment.arrayList.get(i).getProductname()+" "+ShelfStockActivity.tab_position);
                                if(ShelfFragment.arrayList.get(i).getProductname().equals(dataList.get(position)))
                                {
                                    isExists=true;
                                }
                            }

                            if(isExists)
                            {
                                holder.chk_product.setChecked(false);
                                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                                builder.setMessage("Product already exists");
                                builder.setCancelable(true);
                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();

                            }
                            else {
                                Log.e("add",dataList.get(position)+" at position "+ShelfStockActivity.tab_position);
                                addlist.add(dataList.get(position));
                                holder.chk_product.setChecked(true);
                            }

                        }else {



                            boolean isExists=false;
                            for(int i = 0; i< StoreFragment.arrayList.size(); i++)
                            {
                                Log.e("check",dataList.get(position)+" "+StoreFragment.arrayList.get(i).getProductname()+" "+ShelfStockActivity.tab_position);

                                if(StoreFragment.arrayList.get(i).getProductname().equals(dataList.get(position)))
                                {
                                    isExists=true;
                                }
                            }

                            if(isExists)
                            {
                                holder.chk_product.setChecked(false);
                                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                                builder.setMessage("Product already exists");
                                builder.setCancelable(true);
                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();

                            }
                            else {
                                Log.e("add",dataList.get(position)+" at position "+ShelfStockActivity.tab_position);
                                addlist.add(dataList.get(position));
                                holder.chk_product.setChecked(true);
                            }

                        }


                    }else {
                        Log.e("uncheck",dataList.get(position));

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
