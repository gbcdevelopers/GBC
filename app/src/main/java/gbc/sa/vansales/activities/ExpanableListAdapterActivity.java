package gbc.sa.vansales.activities;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.data.DriverRouteFlags;
import gbc.sa.vansales.utils.Helpers;
/**
 * Created by Muhammad Umair on 22/12/2016.
 */
public class ExpanableListAdapterActivity extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ExpandedMenuModel> mListDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<ExpandedMenuModel, List<String>> mListDataChild;
    ExpandableListView expandList;
    App.DriverRouteControl flag = new App.DriverRouteControl();
    public ExpanableListAdapterActivity(Context context, List<ExpandedMenuModel> listDataHeader, HashMap<ExpandedMenuModel, List<String>> listChildData, ExpandableListView mView) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        this.expandList = mView;
        flag = DriverRouteFlags.get();
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
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.submenu);
        TextView lblCount = (TextView) convertView.findViewById(R.id.notificationcount);
        if(headerTitle.getNotificationCount().equals("0")){
            lblCount.setVisibility(View.INVISIBLE);
        }
        else{
            lblCount.setVisibility(View.VISIBLE);
        }
        lblCount.setText(headerTitle.getNotificationCount().equals("0")?"":headerTitle.getNotificationCount());
        lblCount.setTypeface(null, Typeface.BOLD);
        ImageView headerIcon = (ImageView) convertView.findViewById(R.id.iconimage);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        if(!headerTitle.isEnabled()){
            lblListHeader.setAlpha(.5f);
        }
        lblListHeader.setText(headerTitle.getIconName());
        headerIcon.setImageResource(headerTitle.getIconImg());
        LinearLayout ll_main = (LinearLayout) convertView.findViewById(R.id.ll_main);
        //Log.e("Header Title","" + headerTitle.isEnabled() + headerTitle.getIconName());

        ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.e("position", groupPosition + "");
                try{
                    if (groupPosition == 0) {
                        if(headerTitle.isEnabled()){
                            Helpers.logData(mContext, "Clicked on Begin Trip from Adapter");
                            Intent i = new Intent(mContext, BeginTripActivity.class);
                            mContext.startActivity(i);
                        }

                    } else if (groupPosition == 1) {
                        if(headerTitle.isEnabled()){
                            Helpers.logData(mContext, "Clicked on Manage Inventory from Adapter");
                            Intent i = new Intent(mContext, ManageInventory.class);
                            mContext.startActivity(i);
                        }

                    } else if (groupPosition == 2) {
                        if(headerTitle.isEnabled()){
                            Helpers.logData(mContext, "Clicked on My Calendar from Adapter");
                            Intent i = new Intent(mContext, MyCalendarActivity.class);
                            mContext.startActivity(i);
                        }

                    } else if (groupPosition == 3) {
                        if(headerTitle.isEnabled()){
                            Helpers.logData(mContext, "Clicked on End Trip from Adapter");
                            if(!(flag==null)){
                                if(!flag.getIsEndTrip().equals("")&&!flag.getIsEndTrip().equals("0")){
                                    Helpers.logData(mContext, "Prompt for End Trip Password");
                                    String passwordkey = flag.getIsEndTrip();
                                    String password = "";
                                    if(passwordkey.equals("1")){
                                        password = flag.getPassword1();
                                    }
                                    if(passwordkey.equals("2")){
                                        password = flag.getPassword2();
                                    }
                                    if(passwordkey.equals("3")){
                                        password = flag.getPassword3();
                                    }
                                    if(passwordkey.equals("4")){
                                        password = flag.getPassword4();
                                    }
                                    if(passwordkey.equals("5")){
                                        password = flag.getPassword5();
                                    }
                                    final Dialog dialog = new Dialog(mContext);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    LayoutInflater li = LayoutInflater.from(mContext);
                                    View view = li.inflate(R.layout.password_prompt, null);
                                    //View view = getLayoutInflater().inflate(R.layout.password_prompt, null);
                                    final EditText userInput = (EditText) view
                                            .findViewById(R.id.password);
                                    Button btn_continue = (Button)view.findViewById(R.id.btn_ok);
                                    Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
                                    final String finalPassword = password;
                                    btn_continue.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //hideKeyboard();
                                            String input = userInput.getText().toString();
                                            Helpers.logData(mContext, "User input password" + input + "on End trip screen");
                                            if (input.equals("")) {
                                                dialog.cancel();
                                                Toast.makeText(mContext, mContext.getString(R.string.valid_value), Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (input.equals(finalPassword)){
                                                    try{
                                                        Helpers.logData(mContext, "Password validation passed for End Trip");
                                                        dialog.dismiss();
                                                        Intent i = new Intent(mContext, EndTripActivity.class);
                                                        mContext.startActivity(i);
                                                    }
                                                    catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                                else{
                                                    Helpers.logData(mContext, "Password validation failed for End Trip");
                                                    dialog.cancel();
                                                    Toast.makeText(mContext, mContext.getString(R.string.password_mismatch), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    });
                                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Helpers.logData(mContext, "Dialog closed by user for End Trip password prompt");
                                            dialog.cancel();
                                        }
                                    });
                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    lp.copyFrom(dialog.getWindow().getAttributes());
                                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                    lp.gravity = Gravity.CENTER;
                                    dialog.getWindow().setAttributes(lp);
                                    dialog.setContentView(view);
                                    dialog.setCancelable(false);
                                    dialog.show();
                                }
                                else{
                                    Helpers.logData(mContext, "No password flag set for user for end trip. Directly going for End Trip");
                                    Intent i = new Intent(mContext, EndTripActivity.class);
                                    mContext.startActivity(i);
                                }
                            }
                            else{
                                Helpers.logData(mContext, "No password flag set for user for end trip. Directly going for End Trip");
                                Intent i = new Intent(mContext, EndTripActivity.class);
                                mContext.startActivity(i);
                            }
                        /*final Dialog dialog = new Dialog(mContext);
                        LayoutInflater li = LayoutInflater.from(mContext);
                        View view = li.inflate(R.layout.password_prompt, null);
                        final EditText userInput = (EditText) view
                                .findViewById(R.id.password);
                        Button btn_continue = (Button)view.findViewById(R.id.btn_ok);
                        Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
                        btn_continue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String input = userInput.getText().toString();
                                if (input.equals("")) {
                                    dialog.cancel();
                                    Toast.makeText(mContext, mContext.getString(R.string.valid_value), Toast.LENGTH_SHORT).show();
                                } else {
                                    dialog.dismiss();
                                    Intent i = new Intent(mContext, EndTripActivity.class);
                                    mContext.startActivity(i);
                                }
                            }
                        });
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        dialog.setContentView(view);
                        dialog.setCancelable(false);
                        dialog.show();*/
                        }

                    }
                    else if (groupPosition == 4) {
                        if(headerTitle.isEnabled()){
                            Helpers.logData(mContext, "User clicked on Driver Collection from Adapter");
                            Intent i = new Intent(mContext, DriverCollectionsActivity.class);
                            mContext.startActivity(i);
                        }

                    }
                    else if (groupPosition == 5) {
                        if(headerTitle.isEnabled()){
                            Helpers.logData(mContext, "User clicked on Information from Adapter");
                            Intent i = new Intent(mContext, InformationsActivity.class);
                            mContext.startActivity(i);
                        }

                    }
                    else if (groupPosition == 6) {
                        if(headerTitle.isEnabled()){
                            Helpers.logData(mContext, "User clicked on Information from Adapter");
                            Intent i = new Intent(mContext, DataPostingAuditActivity.class);
                            mContext.startActivity(i);
                        }

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
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