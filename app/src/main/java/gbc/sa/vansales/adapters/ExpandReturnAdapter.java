package gbc.sa.vansales.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.CustomerData;
import gbc.sa.vansales.utils.AnimatedExpandableListView;

/**
 * Created by eheuristic on 12/9/2016.
 */

public class ExpandReturnAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter implements Filterable {

ItemFilter mFilter = new ItemFilter();
    Context context;
    int groupPosition;
    ArrayList<String> dataList;
    ArrayList<String> dataListOne;
    boolean isexpanded=false;
    ExpandableListView exp_list;
    Spinner spinner;

    int tvChangeValue;

    public ExpandReturnAdapter(Context context, ArrayList<String > arrProductList, ExpandableListView exp_list)
    {
        this.dataList =arrProductList;
        this.dataListOne =arrProductList;
        this.context=context;
        this.exp_list=exp_list;
    }


    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {






        this.groupPosition=groupPosition;

        final Context context = parent.getContext();

        LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.expand_return_childview, null);
        RelativeLayout rl_bottom=(RelativeLayout)convertView.findViewById(R.id.rl_bottom);

        spinner = (Spinner)convertView.findViewById(R.id.spin);
      final  TextView tv_ctn_value,tv_ctn_value1;
      final  TextView tv_btl_value,tv_btl_value1;


        ImageView iv_ctn_minus,iv_ctn_minus1;
        ImageView iv_ctn_add,iv_ctn_add1;
        ImageView iv_btl_minus,iv_btl_minus1;
        ImageView iv_btl_add,iv_btl_add1;




        tv_ctn_value=(TextView)convertView.findViewById(R.id.tv_ctn_value);
        tv_ctn_value1=(TextView)convertView.findViewById(R.id.tv_ctn_value1);
        tv_btl_value=(TextView)convertView.findViewById(R.id.tv_btl_value);
        tv_btl_value1=(TextView)convertView.findViewById(R.id.tv_btl_value1);


        iv_ctn_minus =(ImageView)convertView.findViewById(R.id.iv_ctn_minus);
        iv_ctn_minus1=(ImageView)convertView.findViewById(R.id.iv_ctn_minus1);
        iv_ctn_add=(ImageView)convertView.findViewById(R.id.iv_ctn_add);
        iv_ctn_add1=(ImageView)convertView.findViewById(R.id.iv_ctn_add1);
        iv_btl_minus=(ImageView)convertView.findViewById(R.id.iv_btl_minus);
        iv_btl_minus1=(ImageView)convertView.findViewById(R.id.iv_btl_minus1);
        iv_btl_add=(ImageView)convertView.findViewById(R.id.iv_btl_add);
        iv_btl_add1=(ImageView)convertView.findViewById(R.id.iv_btl_add1);



        iv_ctn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvChangeValue = Integer.parseInt(tv_ctn_value.getText().toString());
                if(tvChangeValue >0)
                {
                    tvChangeValue--;
                    tv_ctn_value.setText(String.valueOf(tvChangeValue
                    ));
                }

            }
        });

        iv_ctn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvChangeValue = Integer.parseInt(tv_ctn_value.getText().toString());
                tvChangeValue++;
                tv_ctn_value.setText(String.valueOf(tvChangeValue));
            }
        });



        iv_ctn_minus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvChangeValue = Integer.parseInt(tv_ctn_value1.getText().toString());
                if(tvChangeValue >0)
                {
                    tvChangeValue--;
                    tv_ctn_value1.setText(String.valueOf(tvChangeValue
                    ));
                }

            }
        });

        iv_ctn_add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvChangeValue = Integer.parseInt(tv_ctn_value1.getText().toString());
                tvChangeValue++;
                tv_ctn_value1.setText(String.valueOf(tvChangeValue));
            }
        });


        iv_btl_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvChangeValue = Integer.parseInt(tv_btl_value.getText().toString());
                if(tvChangeValue >0)
                {
                    tvChangeValue--;
                    tv_btl_value.setText(String.valueOf(tvChangeValue
                    ));
                }

            }
        });

        iv_btl_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvChangeValue = Integer.parseInt(tv_btl_value.getText().toString());
                tvChangeValue++;
                tv_btl_value.setText(String.valueOf(tvChangeValue));
            }
        });


        iv_btl_minus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvChangeValue = Integer.parseInt(tv_btl_value1.getText().toString());
                if(tvChangeValue >0)
                {
                    tvChangeValue--;
                    tv_btl_value1.setText(String.valueOf(tvChangeValue
                    ));
                }

            }
        });

        iv_btl_add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvChangeValue = Integer.parseInt(tv_btl_value1.getText().toString());
                tvChangeValue++;
                tv_btl_value1.setText(String.valueOf(tvChangeValue));
            }
        });
















        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public int getGroupCount() {
        return dataList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
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
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {


        final Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.expand_return_groupview, null);
        RelativeLayout rl_expand=(RelativeLayout)convertView.findViewById(R.id.rl_expand);
        ImageView iv_expand=(ImageView)convertView.findViewById(R.id.iv_expand);
        TextView tv_productname=(TextView)convertView.findViewById(R.id.tv_product_name);
        tv_productname.setText(dataList.get(groupPosition));




        iv_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {






                if (isExpanded) {

                    if(spinner!=null)
                    {
                        if(!spinner.getSelectedItem().equals(""))
                        {
                            exp_list.collapseGroup(groupPosition);
                        }
                    }
                    else {
                        exp_list.collapseGroup(groupPosition);
                    }



                } else {

                    if(spinner!=null)
                    {

                        if(!spinner.getSelectedItem().equals(""))
                        {
                            exp_list.expandGroup(groupPosition);
                        }
                    }
                    else {
                        exp_list.expandGroup(groupPosition);
                    }


                }


            }
        });





        if(isExpanded)
        {
            rl_expand.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            tv_productname.setTextColor(context.getResources().getColor(R.color.white));
            iv_expand.setImageResource(R.drawable.ic_remove_black_24dp);
        }
        else {
            rl_expand.setBackgroundColor(context.getResources().getColor(R.color.white));
            tv_productname.setTextColor(context.getResources().getColor(R.color.black));
            iv_expand.setImageResource(R.drawable.ic_black_add);
        }


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString();
            Log.v("DataAdapter", "constratinst : " + constraint);
            FilterResults result = new FilterResults();
            try {
                if (constraint.toString().length() > 0) {
                    ArrayList<String> filteredItems =
                            new ArrayList<>();
                    for (int i = 0, l = dataList.size(); i < l; i++) {
                        // ArrayList<HashMap<String, String>> p =
                        // originalList.get(i);
                        String p = dataList.get(i);
                        if (p.contains(constraint))
                            filteredItems.add(dataList.get(i));
                    }
                    Log.v("DataAdapter", "not blank");
                    result.count = filteredItems.size();
                    result.values = filteredItems;

                } else {
                    synchronized (this) {
                        result.count = dataListOne.size();
                        result.values = dataListOne;
//                    result.values = dataList;
//                    result.count = dataList.size();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                synchronized (this) {
                    result.count = dataListOne.size();
                    result.values = dataListOne;
//                    result.values = dataList;
//                    result.count = dataList.size();
                }
            }

            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // users = (List<GraphUser>) results.values;
            //filteredData = (ArrayList<String>) results.values;
            dataList = (ArrayList<String>) results.values;
            notifyDataSetChanged();

//            for (int i = 0, l = dataList.size(); i < l; i++)
//                dataList.get(i);
            //add(productList.get(i));

            notifyDataSetInvalidated();
        }
    }
}
