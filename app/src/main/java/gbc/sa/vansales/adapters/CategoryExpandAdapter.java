package gbc.sa.vansales.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.Fragment.BListFragment;
import gbc.sa.vansales.Fragment.FocFragment;
import gbc.sa.vansales.Fragment.GListFragment;
import gbc.sa.vansales.Fragment.ShelfFragment;
import gbc.sa.vansales.Fragment.StoreFragment;
import gbc.sa.vansales.R;
import gbc.sa.vansales.activities.SalesInvoiceActivity;
import gbc.sa.vansales.activities.ShelfStockActivity;
import gbc.sa.vansales.data.Const;
import gbc.sa.vansales.utils.AnimatedExpandableListView;
import gbc.sa.vansales.utils.Settings;

import static gbc.sa.vansales.data.Const.addlist;

/**
 * Created by eheuristic on 12/9/2016.
 */

public class CategoryExpandAdapter extends BaseExpandableListAdapter {

    ArrayList<String> grouplist;
    HashMap<String,ArrayList<String>> childlist;
    Context context;
    int groupPosition;
    public CategoryExpandAdapter(Context context, ArrayList<String> grouplist,HashMap<String,ArrayList<String>> childlist )
    {
        this.grouplist=grouplist;
        this.context=context;
        this.childlist=childlist;
        Const.addlist.clear();
    }






    @Override
    public int getGroupCount() {
        return this.grouplist.size();

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childlist.get(this.grouplist.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.grouplist.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childlist.get(this.grouplist.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {


      return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        final Context context = parent.getContext();
        String headerTitle = (String) getGroup(groupPosition);
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.checkable_productlist, null);
        CheckBox checkBox=(CheckBox)convertView.findViewById(R.id.chk_product);
        checkBox.setVisibility(View.GONE);
        TextView tv_product=(TextView)convertView.findViewById(R.id.tv_product);
        tv_product.setTypeface(null, Typeface.BOLD);
        tv_product.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {



        this.groupPosition=groupPosition;

        final Context context = parent.getContext();
        final String childText = (String) getChild(groupPosition, childPosition);

        LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.checkable_productlist, null);
        TextView tv_product=(TextView)convertView.findViewById(R.id.tv_product);

       final CheckBox checkBox=(CheckBox)convertView.findViewById(R.id.chk_product);


        tv_product.setText(childText);


        if(Const.addlist.contains(childText))
        {
            checkBox.setChecked(true);
        }
        else {
            checkBox.setChecked(false);
        }



        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


//
//                if(isChecked)
//                {
//                   Const.addlist.add(childText);
//                }
//                else {
//                    Const.addlist.remove(childText);
//                }


                if(isChecked)
                {

                    if(Settings.getString("from").equals("shelf"))
                    {

                        boolean isExists=false;
                        for(int i = 0; i< ShelfFragment.arrayList.size(); i++)
                        {

                            Log.e("check",childText+" "+ShelfFragment.arrayList.get(i).getProductname()+" "+ ShelfStockActivity.tab_position);
                            if(ShelfFragment.arrayList.get(i).getProductname().equals(childText))
                            {
                                isExists=true;
                            }
                        }

                        if(isExists)
                        {
                            checkBox.setChecked(false);
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

                            addlist.add(childText);
                            checkBox.setChecked(true);
                        }

                    }else if(Settings.getString("from").equals("store")){


                        boolean isExists=false;
                        for(int i = 0; i< StoreFragment.arrayList.size(); i++)
                        {

                            Log.e("check",childText+" "+StoreFragment.arrayList.get(i).getProductname()+" "+ ShelfStockActivity.tab_position);
                            if(StoreFragment.arrayList.get(i).getProductname().equals(childText))
                            {
                                isExists=true;
                            }
                        }

                        if(isExists)
                        {
                            checkBox.setChecked(false);
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

                            addlist.add(childText);
                            checkBox.setChecked(true);
                        }


                    }
                    else if(Settings.getString("from").equals("foc"))
                    {



                        boolean isExists=false;
                        for(int i = 0; i< FocFragment.salesarrayList.size(); i++)
                        {
                            Log.e("check",childText+" "+FocFragment.salesarrayList.get(i).getName()+" "+ SalesInvoiceActivity.tab_position);

                            if(FocFragment.salesarrayList.get(i).getName().equals(childText))
                            {
                                isExists=true;
                            }
                        }

                        if(isExists)
                        {
                            checkBox.setChecked(false);
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

                            addlist.add(childText);
                            checkBox.setChecked(true);
                        }



                    }
                    else if(Settings.getString("from").equals("glist"))
                    {

                        boolean isExists=false;
                        for(int i = 0; i< GListFragment.arrProductList.size(); i++)
                        {
                            Log.e("check",childText+" "+GListFragment.arrProductList.get(i)+" "+ SalesInvoiceActivity.tab_position);

                            if(GListFragment.arrProductList.get(i).equals(childText))
                            {
                                isExists=true;
                            }
                        }

                        if(isExists)
                        {
                            checkBox.setChecked(false);
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

                           // addlist.add(childText);
                            checkBox.setChecked(true);
                        }



                    }
                    else if(Settings.getString("from").equals("blist"))
                    {



                        boolean isExists=false;
                        for(int i = 0; i< BListFragment.arrProductList.size(); i++)
                        {
                            Log.e("check",childText+" "+BListFragment.arrProductList.get(i)+" "+ SalesInvoiceActivity.tab_position);

                            if(BListFragment.arrProductList.get(i).equals(childText))
                            {
                                isExists=true;
                            }
                        }

                        if(isExists)
                        {
                            checkBox.setChecked(false);
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

                            addlist.add(childText);
                            checkBox.setChecked(true);
                        }

                    }


                }else {
                    Log.e("uncheck",childText);

                    checkBox.setChecked(false);
                    addlist.remove(childText);
                }


            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
