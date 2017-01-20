package gbc.sa.vansales.adapters;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.models.Message;
import gbc.sa.vansales.models.Promotions;
import gbc.sa.vansales.utils.RoundedImageView;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Rakshit on 17-Jan-17.
 */
public class PromotionsAdapter extends ArrayAdapter<Promotions>{
    private ArrayList<Promotions> promotionList;

    public PromotionsAdapter(Context context, ArrayList<Promotions> promotionList){

        super(context, R.layout.custom_promotionlist, promotionList);
        this.promotionList = promotionList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_promotionlist,parent,false);
            // get all UI view
            holder = new ViewHolder(convertView);
            /*holder.tv_promotion = (TextView) convertView.findViewById(R.id.tv_product);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.chk_product);
            LinearLayout ll_promotion = (LinearLayout) convertView.findViewById(R.id.ll_promotion);
            holder.view = (View) convertView.findViewById(R.id.view);
            */// set tag for holder
            convertView.setTag(holder);
        }
        else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        Promotions promotions = getItem(position);

        holder.tv_promotion.setText(promotions.getPromotionDescription());
        if(promotions.isMandatory()){
            holder.view.setBackgroundColor(Color.RED);
            holder.checkBox.setVisibility(View.GONE);
        }
        else{
            holder.view.setBackgroundColor(Color.BLUE);
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        return convertView;
    }




    private class ViewHolder{
        View view;
        TextView tv_promotion;
        CheckBox checkBox;
        //  TextView tv_message_title;

        public ViewHolder(View v) {

            tv_promotion = (TextView) v.findViewById(R.id.tv_product);
            checkBox = (CheckBox) v.findViewById(R.id.chk_product);
            view = (View) v.findViewById(R.id.view);
            //   tv_message_title = (TextView) v.findViewById(R.id.tv_message_title);
        }
    }
}
