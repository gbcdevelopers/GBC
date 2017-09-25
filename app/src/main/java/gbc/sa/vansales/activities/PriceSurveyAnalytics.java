package gbc.sa.vansales.activities;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gbc.sa.vansales.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;

/**
 * Created by Rakshit on 16-Apr-17.
 */
public class PriceSurveyAnalytics extends AppCompatActivity {
    ImageView iv_back, iv_refresh;
    TextView tv_top_header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_analytics);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Price Survey");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                finish();
            }
        });
        BarChart chart = (BarChart) findViewById(R.id.chart);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.animateXY(2000, 2000);
        chart.invalidate();
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(100.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(45.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(60.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(33.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(93.500f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(102.000f, 5); // Jun
        valueSet2.add(v2e6);

        ArrayList<BarEntry> valueSet3 = new ArrayList<>();
        BarEntry v3e1 = new BarEntry(100.000f, 0); // Jan
        valueSet3.add(v3e1);
        BarEntry v3e2 = new BarEntry(47.000f, 1); // Feb
        valueSet3.add(v3e2);
        BarEntry v3e3 = new BarEntry(61.000f, 2); // Mar
        valueSet3.add(v3e3);
        BarEntry v3e4 = new BarEntry(32.800f, 3); // Apr
        valueSet3.add(v3e4);
        BarEntry v3e5 = new BarEntry(92.700f, 4); // May
        valueSet3.add(v3e5);
        BarEntry v3e6 = new BarEntry(101.800f, 5); // Jun
        valueSet3.add(v3e6);

        ArrayList<BarEntry> valueSet4 = new ArrayList<>();
        BarEntry v4e1 = new BarEntry(105.000f, 0); // Jan
        valueSet4.add(v4e1);
        BarEntry v4e2 = new BarEntry(49.000f, 1); // Feb
        valueSet4.add(v4e2);
        BarEntry v4e3 = new BarEntry(58.000f, 2); // Mar
        valueSet4.add(v4e3);
        BarEntry v4e4 = new BarEntry(30.000f, 3); // Apr
        valueSet4.add(v4e4);
        BarEntry v4e5 = new BarEntry(90.000f, 4); // May
        valueSet4.add(v4e5);
        BarEntry v4e6 = new BarEntry(105.000f, 5); // Jun
        valueSet4.add(v4e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "AlMarai");
        barDataSet1.setColor(Color.rgb(73, 201, 49));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "AlRawabi");
        barDataSet2.setColor(Color.rgb(53, 149, 231));
        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Al Ain");
        barDataSet3.setColor(Color.rgb(248, 87, 62));
        BarDataSet barDataSet4 = new BarDataSet(valueSet4, "Nesto Products");
        barDataSet4.setColor(Color.rgb(237, 245, 8));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);
        dataSets.add(barDataSet4);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Berain 200ml");
        xAxis.add("Berain 300ml");
        xAxis.add("Berain 600ml");
        xAxis.add("Berain 1.5Ltr");
        xAxis.add("Berain 3.5Ltr");
        xAxis.add("Berain 5Ltr");
        return xAxis;
    }
}
