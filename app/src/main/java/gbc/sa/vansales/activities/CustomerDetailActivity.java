package gbc.sa.vansales.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import gbc.sa.vansales.R;

/**
 * Created by eheuristic on 12/3/2016.
 */

public class CustomerDetailActivity extends AppCompatActivity{
    GridView gridView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        gridView=(GridView)findViewById(R.id.grid);

    }
}
