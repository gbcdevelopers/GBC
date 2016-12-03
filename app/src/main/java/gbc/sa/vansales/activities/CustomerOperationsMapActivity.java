package gbc.sa.vansales.activities;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
/**
 * Created by Rakshit on 21-Nov-16.
 */

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import gbc.sa.vansales.R;

public class CustomerOperationsMapActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map.setMyLocationEnabled(true);

        this.map = googleMap;
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        int index = Integer.valueOf(marker.getSnippet());


        return true;
    }
}
