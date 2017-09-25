package gbc.sa.vansales.activities;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.PromotionInfoAdapter;
import gbc.sa.vansales.adapters.PromotionsAdapter;
import gbc.sa.vansales.adapters.SwipeDetector;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.OrderList;
import gbc.sa.vansales.models.Promotions;
import gbc.sa.vansales.sap.DataListener;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.LoadingSpinner;
/**
 * Created by eheuristic on 12/7/2016.
 */
/************************************************************
 @ This is launched when the promotion is clicked on the up down
 @ button.
 ************************************************************/
public class CustomerPromotionListActivity extends AppCompatActivity implements DataListener{
    ListView list_promotion;
    ArrayAdapter<Promotions> adapter;
    PromotionInfoAdapter adapter1;
    ImageView iv_back;
    TextView tv_top_header;
    Button btn_apply;
    ArrayList<Promotions> arrayList;
    String from = "promo";
    Customer object;
    OrderList delivery;
    int count = 0;
    DatabaseHandler db = new DatabaseHandler(this);
    LoadingSpinner loadingSpinner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotionlist);
        Intent i = this.getIntent();
        loadingSpinner = new LoadingSpinner(this);
        //loadingSpinner.show();
        object = (Customer) i.getParcelableExtra("headerObj");
        delivery = (OrderList)i.getParcelableExtra("delivery");
        arrayList = new ArrayList<>();
        list_promotion = (ListView) findViewById(R.id.list_promotion);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        btn_apply = (Button) findViewById(R.id.btn_apply);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.promotion));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent().getExtras() != null) {
            from = getIntent().getExtras().getString("from", "");
            if (from.equals("review")) {
                btn_apply.setVisibility(View.GONE);
            }
        }
        if (from.equals("review")) {
            /*adapter1 = new PromotionInfoAdapter(PromotionListActivity.this, 3, R.layout.custom_promotion_info_list);
            list_promotion.setAdapter(adapter1);*/
            adapter = new PromotionsAdapter(CustomerPromotionListActivity.this, arrayList);
            list_promotion.setAdapter(adapter);
        } else {
            //Log.e("Here","Here");
                adapter = new PromotionsAdapter(CustomerPromotionListActivity.this, arrayList);
                list_promotion.setAdapter(adapter);
        }

        /*arrayList.add("50% AMC Invoice Discount");
        arrayList.add("20% FOC Discount");
        arrayList.add("10% Other Discount");*/
        try{
            new loadPromotions(App.Promotions02);
         //   new loadPromotions(App.Promotions05);
         //   new loadPromotions(App.Promotions07);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerPromotionListActivity.this, PromotioninfoActivity.class);
                intent.putExtra("msg", "Final Invoice");
                intent.putExtra("delivery",delivery);
                intent.putExtra("from",from);
                intent.putExtra("headerObj", object);
                startActivity(intent);
            }
        });



        final SwipeDetector swipeDetector = new SwipeDetector();
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                if (swipeDetector.swipeDetected()) {

                } else {
                    TextView tv = (TextView) view.findViewById(R.id.tv_product);
                    String str_promotion_message = tv.getText().toString();
                    Intent intent = new Intent(CustomerPromotionListActivity.this, CustomerPromotionDetailsActivity.class);
                    intent.putExtra("headerObj", object);
                    intent.putExtra("promocode",arrayList.get(position).getPromotionCode());
                    intent.putExtra("pos", position);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            }
        };
        list_promotion.setOnTouchListener(swipeDetector);
        list_promotion.setOnItemClickListener(listener);
    }

    private void setPromotions(String promocode){
        if(promocode.equals(App.Promotions02)){
            Promotions promotions = new Promotions();
            promotions.setIsMandatory(true);
            promotions.setPromotionCode(App.Promotions02);
            promotions.setPromotionDescription(ConfigStore.getCode(App.Promotions02));
            arrayList.add(promotions);
        }
        else if(promocode.equals(App.Promotions05)){
            Promotions promotions = new Promotions();
            promotions.setIsMandatory(true);
            promotions.setPromotionCode(App.Promotions05);
            promotions.setPromotionDescription(ConfigStore.getCode(App.Promotions05));
            arrayList.add(promotions);
        }
        else if(promocode.equals(App.Promotions07)){
            Promotions promotions = new Promotions();
            promotions.setIsMandatory(true);
            promotions.setPromotionCode(App.Promotions07);
            promotions.setPromotionDescription(ConfigStore.getCode(App.Promotions07));
            arrayList.add(promotions);
        }
    }
    @Override
    public void onProcessingComplete() {
        Log.e("I never came","" + "Never Came");
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onProcessingComplete(String source) {
        adapter.notifyDataSetChanged();
    }

    private void checkPromotions(){
        String Promotions02 = App.Promotions02;
        String Promotions05 = App.Promotions05;
        String Promotions07 = App.Promotions07;

        HashMap<String,String>filter = new HashMap<>();
        filter.put(db.KEY_PROMOTION_TYPE,Promotions02);
        filter.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
        HashMap<String,String>filter1 = new HashMap<>();
        filter1.put(db.KEY_PROMOTION_TYPE,Promotions05);
        filter1.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
        HashMap<String,String>filter2 = new HashMap<>();
        filter2.put(db.KEY_PROMOTION_TYPE,Promotions07);
        filter2.put(db.KEY_CUSTOMER_NO,object.getCustomerID());
        if(db.checkData(db.PROMOTIONS,filter)) {
            setPromotions(Promotions02);
        }
        if(db.checkData(db.PROMOTIONS,filter1)) {
            setPromotions(Promotions05);
        }
        if(db.checkData(db.PROMOTIONS,filter2)) {
            setPromotions(Promotions07);
        }
    }

    public class loadPromotions extends AsyncTask<Void,Void,Void>{
        String promotionType = "";
        String Promotions02 = App.Promotions02;
        String Promotions05 = App.Promotions05;
        String Promotions07 = App.Promotions07;
        private loadPromotions(String promotionType) {
            this.promotionType = promotionType;
            execute();
        }
        @Override
        protected void onPreExecute() {
            if(!loadingSpinner.isShowing()){
                loadingSpinner.show();
            }

        }
        @Override
        protected Void doInBackground(Void... params) {
            checkPromotions();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
            }
            adapter = new PromotionsAdapter(CustomerPromotionListActivity.this, arrayList);
            list_promotion.setAdapter(adapter);
            if(arrayList.size()==0){
                if (from.equals("review")){

                }
                else{
                    /*Intent intent = new Intent(CustomerPromotionListActivity.this, PromotioninfoActivity.class);
                    intent.putExtra("msg", "Final Invoice");
                    intent.putExtra("delivery",delivery);
                    intent.putExtra("from",from);
                    intent.putExtra("headerObj", object);
                    startActivity(intent);*/
                }

            }
          //  adapter.notifyDataSetChanged();
           // onProcessingComplete();
            /*count++;
            if(count==3){
                if(loadingSpinner.isShowing()){
                    loadingSpinner.hide();
                }
                onProcessingComplete();
            }*/

        }
    }
    @Override
    public void onBackPressed() {
        // Do not allow hardware back navigation
    }
}
