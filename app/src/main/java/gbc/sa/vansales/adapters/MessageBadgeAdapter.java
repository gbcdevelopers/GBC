package gbc.sa.vansales.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Locale;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.Message;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.utils.RoundedImageView;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Rakshit on 03-Jan-17.
 */
public class MessageBadgeAdapter extends ArrayAdapter<Message> {

    private ArrayList<Message> messages;

    public MessageBadgeAdapter(Context context, ArrayList<Message> messages){

        super(context, R.layout.message_list_adapter, messages);
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.message_list_adapter,parent,false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        }
        else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        Message message = getItem(position);

      //  holder.tv_message_title.setText(UrlBuilder.decodeString(message.getStructure()).substring(0,1).toUpperCase() + UrlBuilder.decodeString(message.getStructure()).substring(1).toLowerCase());
        holder.iv_round = (RoundedImageView) convertView.findViewById(R.id.roundedImageView);
        holder.tv_message.setText(UrlBuilder.decodeString(message.getMessage()).substring(0,1).toUpperCase() + UrlBuilder.decodeString(message.getMessage()).substring(1).toLowerCase());
        return convertView;
    }




    private class ViewHolder{
        RoundedImageView iv_round;
        TextView tv_message;
      //  TextView tv_message_title;

        public ViewHolder(View v) {

            tv_message = (TextView) v.findViewById(R.id.tv_message);
            iv_round = (RoundedImageView) v.findViewById(R.id.roundedImageView);
         //   tv_message_title = (TextView) v.findViewById(R.id.tv_message_title);
        }
    }
}
