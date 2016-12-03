package gbc.sa.vansales.activities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import gbc.sa.vansales.R;
import gbc.sa.vansales.views.TileView;
/**
 * Created by Rakshit on 16-Nov-16.
 */
public class SelectOperationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_operation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Customer 0");
    }

    public void navigate(View view) {
        TileView tileView = (TileView) view;

        String label = tileView.getLabel();
        Log.e("Here", "Here" + label);
        Intent intent = null;

        if (label.equals(getString(R.string.collection))) {
            intent = new Intent(this, DashboardActivity.class);
        } else if (label.equals(getString(R.string.presales))) {
            intent = new Intent(this, StockTakeActivity.class);
        } else if (label.equals(getString(R.string.spot_sales))) {
            intent = new Intent(this, DashboardActivity.class);

          //  intent.putExtra(BREAK, true);
        } else if (label.equals(getString(R.string.delivery))) {
            intent = new Intent(this, DashboardActivity.class);
        }  else if (label.equals(getString(R.string.returns))) {
            intent = new Intent(this, ReturnsActivity.class);
        } else if (label.equals(getString(R.string.merchandizing))) {
            intent = new Intent(this, DashboardActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}
