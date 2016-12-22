package gbc.sa.vansales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.utils.AnimatedExpandableListView;

/**
 * Created by eheuristic on 12/9/2016.
 */

public class ExpandReturnAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {


    Context context;
    int groupPosition;
    ArrayList<String>  arrProductList;
    public ExpandReturnAdapter(Context context,ArrayList<String > arrProductList)
    {
        this.arrProductList=arrProductList;
        this.context=context;
    }


    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        this.groupPosition=groupPosition;

        final Context context = parent.getContext();

        LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.expand_return_childview, null);

        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public int getGroupCount() {
        return arrProductList.size();
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        final Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.expand_return_groupview, null);
        RelativeLayout rl_expand=(RelativeLayout)convertView.findViewById(R.id.rl_expand);
        ImageView iv_expand=(ImageView)convertView.findViewById(R.id.iv_expand);
        TextView tv_productname=(TextView)convertView.findViewById(R.id.tv_product_name);
        tv_productname.setText(arrProductList.get(groupPosition));





        if(isExpanded)
        {
            rl_expand.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            tv_productname.setTextColor(context.getResources().getColor(R.color.white));
            iv_expand.setImageResource(R.drawable.ic_remove_black_24dp);
        }
        else {
            rl_expand.setBackgroundColor(context.getResources().getColor(R.color.lightgray));
            tv_productname.setTextColor(context.getResources().getColor(R.color.black));
            iv_expand.setImageResource(R.drawable.ic_black_add);
        }


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
