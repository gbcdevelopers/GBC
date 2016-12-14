package gbc.sa.vansales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import gbc.sa.vansales.R;
import gbc.sa.vansales.utils.RoundedImageView;
import gbc.sa.vansales.views.TextViewWithLabel;

/**
 * Created by eheuristic on 12/2/2016.
 */

public class MessageListAdapter extends BaseAdapter {


    Context context;
    String[] strArr;
    public MessageListAdapter(Context context,String strArr[])
    {
        this.context=context;

        this.strArr=strArr;
    }

    @Override
    public int getCount() {
        return strArr.length;
    }

    @Override
    public Object getItem(int position) {
        return strArr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


       ItemRowHolder holder = null;
        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            convertView = li.inflate(R.layout.message_list_adapter, null);
//            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_contact, null);
            holder = new ItemRowHolder();

            holder.tv_message = (TextView) convertView.findViewById(R.id.tv_message);
            holder.iv_round = (RoundedImageView) convertView.findViewById(R.id.roundedImageView);


            convertView.setTag(holder);
        } else {
            holder = (ItemRowHolder) convertView.getTag();
        }

        holder.iv_round.setImageResource(R.drawable.driver);
        holder.tv_message.setText(strArr[position]);

        return convertView;
    }

    public class ItemRowHolder {
        RoundedImageView iv_round;
        TextView tv_message;

    }
}
