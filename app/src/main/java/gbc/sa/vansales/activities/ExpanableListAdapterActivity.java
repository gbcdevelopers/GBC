package gbc.sa.vansales.activities;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import gbc.sa.vansales.R;
/**
 * Created by Muhammad Umair on 22/12/2016.
 */
public class ExpanableListAdapterActivity extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ExpandedMenuModel> mListDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<ExpandedMenuModel, List<String>> mListDataChild;
    ExpandableListView expandList;
    public ExpanableListAdapterActivity(Context context, List<ExpandedMenuModel> listDataHeader, HashMap<ExpandedMenuModel, List<String>> listChildData, ExpandableListView mView) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        this.expandList = mView;
    }
    @Override
    public int getGroupCount() {
        int i = mListDataHeader.size();
        Log.d("GROUPCOUNT", String.valueOf(i));
        return this.mListDataHeader.size();
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        List childList = mListDataChild.get(mListDataHeader.get(groupPosition));
        if (childList != null && !childList.isEmpty()) {
            return childList.size();
        }
        return 0;
    }
    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List childList = mListDataChild.get(mListDataHeader.get(groupPosition));
        if (childList != null && !childList.isEmpty()) {
            return childList.get(childPosition);
        }
        return null;
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
        final ExpandedMenuModel headerTitle = (ExpandedMenuModel) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_group, null);
//            if (getChildrenCount(groupPosition)==0) {
//                convertView.setVisibility(View.GONE);
//            }
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.submenu);
        ImageView headerIcon = (ImageView) convertView.findViewById(R.id.iconimage);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        if(!headerTitle.isEnabled()){
            lblListHeader.setAlpha(.5f);
        }
        lblListHeader.setText(headerTitle.getIconName());

        headerIcon.setImageResource(headerTitle.getIconImg());
        LinearLayout ll_main = (LinearLayout) convertView.findViewById(R.id.ll_main);
        Log.e("Header Title","" + headerTitle.isEnabled() + headerTitle.getIconName());

        ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("position", groupPosition + "");
                if (groupPosition == 0) {
                    if(headerTitle.isEnabled()){
                        Intent i = new Intent(mContext, BeginTripActivity.class);
                        mContext.startActivity(i);
                    }

                } else if (groupPosition == 1) {
                    if(headerTitle.isEnabled()){
                        Intent i = new Intent(mContext, ManageInventory.class);
                        mContext.startActivity(i);
                    }

                } else if (groupPosition == 2) {
                    if(headerTitle.isEnabled()){
                        Intent i = new Intent(mContext, MyCalendarActivity.class);
                        mContext.startActivity(i);
                    }

                } else if (groupPosition == 3) {
                    if(headerTitle.isEnabled()){

                        LayoutInflater li = LayoutInflater.from(mContext);
                        View promptsView = li.inflate(R.layout.password_prompt, null);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder.setView(promptsView);
                        //Reading last save odometer
                        final EditText userInput = (EditText) promptsView
                                .findViewById(R.id.editTextDialogUserInput);
                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton(mContext.getString(R.string.ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                String input = userInput.getText().toString();
                                                if (input.equals("")) {
                                                    dialog.cancel();
                                                    Toast.makeText(mContext, mContext.getString(R.string.valid_value), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Intent i = new Intent(mContext, EndTripActivity.class);
                                                    mContext.startActivity(i);
                                                }
                                            }
                                        })
                                .setNegativeButton(mContext.getString(R.string.cancel),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
//                                Intent i=new Intent(getActivity(),DashboardActivity.class);
//                                startActivity(i);
                                            }
                                        });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();


                    }

                } else if (groupPosition == 4) {
                    if(headerTitle.isEnabled()){
                        Intent i = new Intent(mContext, InformationsActivity.class);
                        mContext.startActivity(i);
                    }

                }
            }
        });
        return convertView;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText;
        if (getChild(groupPosition, childPosition) != null) {
            childText = (String) (getChild(groupPosition, childPosition));
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_list_item, null);
            }
            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.submenu);
            txtListChild.setText(childText);
        }
        return convertView;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}