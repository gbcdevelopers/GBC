package gbc.sa.vansales.activities;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by Rakshit on 21-Nov-16.
 */

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerOperationAdapter;

public class CustomerOperationsMapActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    private GoogleMap map;

    ImageView iv_back;
    TextView tv_top_header;
    ImageView iv_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        iv_refresh=(ImageView)findViewById(R.id.iv_refresh);
        iv_refresh.setVisibility(View.VISIBLE);


        tv_top_header=(TextView)findViewById(R.id.tv_top_header);



        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText("Select Customer");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



        this.map = googleMap;




//        this.map.setMyLocationEnabled(true);
//        LatLng latLng=new LatLng(22.3039,70.8022);
//
//        this.map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));




        ArrayList<Double> lat=new ArrayList<>();
        lat.add(22.3039);
        lat.add(28.7041);
        lat.add(19.0760);

        ArrayList<Double> lon=new ArrayList<>();
        lon.add(70.8022);
        lon.add(77.1025);
        lon.add(72.8777);




        for(int i = 0 ; i <lat.size() ; i++ ) {

            createMarker(lat.get(i), lon.get(i),""+i,"marker"+i);
        }






        map.setOnMarkerClickListener(this);
    }

    private void drawMarkers() {
        if (map == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                map.clear();

                /*float roundOff = 360.0f / employees.size();

                for (int index = 0; index < employees.size(); index++) {
                    Employee employee = employees.get(index);

                    if (employee.getLatitude() == 0.0 || employee.getLongitude() == 0.0) {
                        continue;
                    }

                    double newLat = employee.getLatitude() + (-.00004) * Math.cos( (+roundOff * index) / 180 * Math.PI);  // x
                    double newLng = employee.getLongitude() + (-.00004) * Math.sin( (+roundOff * index) / 180 * Math.PI);  // Y

                    MarkerOptions options = new MarkerOptions();

                    options.position(new LatLng(newLat, newLng));
                    options.title(employee.getName());
                    options.snippet(String.valueOf(index));

                    map.addMarker(options);
                }*/
            }
        });
    }


    protected Marker createMarker(double latitude, double longitude, String title, String snippet) {

        return map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet));

    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        marker.showInfoWindow();

        return  true;
    }
}
