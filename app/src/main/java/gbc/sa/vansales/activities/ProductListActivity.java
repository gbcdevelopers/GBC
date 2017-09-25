package gbc.sa.vansales.activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.ProductListAdapter;
import gbc.sa.vansales.data.Const;
/**
 * Created by eheuristic on 12/9/2016.
 */
public class ProductListActivity extends AppCompatActivity {
    ProductListAdapter adapter;
    ListView list_product;
    ArrayList<String> arrayList;
    FloatingActionButton button;
    TextView tv_top_header;
    ImageView iv_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        list_product = (ListView) findViewById(R.id.list_product);
        button = (FloatingActionButton) findViewById(R.id.btn_float);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.product));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Const.addlist.size() > 0) {
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("product", Const.addlist);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });
        arrayList = new ArrayList<>();
        arrayList.add("pen");
        arrayList.add("van");
        arrayList.add("san");
        arrayList.add("can");
        adapter = new ProductListAdapter(ProductListActivity.this, arrayList, R.layout.checkable_productlist, "productlist");
        list_product.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("I am here","Here in product");
                if (Const.addlist.size() > 0) {
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("product", Const.addlist);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });
    }
}
