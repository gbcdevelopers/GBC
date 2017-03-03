package gbc.sa.vansales.activities;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.adapters.CustomerOperationAdapter;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
public class CustomerOperationsMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap map;
    ImageView iv_back;
    TextView tv_top_header;
    ImageView iv_refresh;
    ArrayList<Marker> markers;
    ArrayList<Customer> arrayList = new ArrayList<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        try {
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
            iv_refresh = (ImageView) findViewById(R.id.iv_refresh);
            iv_refresh.setVisibility(View.VISIBLE);
            iv_refresh.setImageResource(R.drawable.ic_directions_black_24dp);
            tv_top_header = (TextView) findViewById(R.id.tv_top_header);
            markers = new ArrayList<>();
            iv_back.setVisibility(View.VISIBLE);
            tv_top_header.setVisibility(View.VISIBLE);
            tv_top_header.setText(getString(R.string.select_customer));
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        new loadCustomers(this, this.map);
        this.map.setMyLocationEnabled(true);
        this.map.getUiSettings().setZoomControlsEnabled(true);
        this.map.getUiSettings().setCompassEnabled(true);
        this.map.getUiSettings().setAllGesturesEnabled(true);
        ArrayList<Double> lat = new ArrayList<>();
        lat.add(22.3039);
        lat.add(28.7041);
        lat.add(19.0760);
        ArrayList<Double> lon = new ArrayList<>();
        lon.add(70.8022);
        lon.add(77.1025);
        lon.add(72.8777);
    }
    /**********************************************************
     @ Loading all the customers to plot the customers on map
     **********************************************************/
    private class loadCustomers extends AsyncTask<Void, Void, Void> {
        Context context;
        GoogleMap map;
        private loadCustomers(Context context, GoogleMap map) {
            this.context = context;
            this.map = map;
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_TRIP_ID, "");
            map.put(db.KEY_CUSTOMER_NO, "");
            map.put(db.KEY_NAME1, "");
            map.put(db.KEY_NAME3, "");
            map.put(db.KEY_LATITUDE, "");
            map.put(db.KEY_LONGITUDE, "");
            HashMap<String, String> filter = new HashMap<>();
            Cursor c = db.getData(db.CUSTOMER_HEADER, map, filter);
            if (c.getCount() > 0) {
                c.moveToFirst();
                setCustomers(c);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            try{
                for (Customer customer : arrayList) {
                    createMarker(Double.parseDouble(UrlBuilder.decodeString(customer.getLatitude()))
                            , Double.parseDouble(UrlBuilder.decodeString(customer.getLongitude()))
                            , StringUtils.stripStart(customer.getCustomerID(), "0")
                            , UrlBuilder.decodeString(customer.getCustomerName()));
                }
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : markers) {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.30);
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                this.map.moveCamera(CameraUpdateFactory.zoomTo(16));
                this.map.animateCamera(cu);
                map.setOnMarkerClickListener(CustomerOperationsMapActivity.this);
            }
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
            }
        }
    }
    private void setCustomers(Cursor c) {
        try{
            c.moveToFirst();
            do {
                Customer customer = new Customer();
                customer.setCustomerID(c.getString(c.getColumnIndex(db.KEY_CUSTOMER_NO)));
                customer.setLatitude(c.getString(c.getColumnIndex(db.KEY_LATITUDE)).equals("") ? "0.000000" : c.getString(c.getColumnIndex(db.KEY_LATITUDE)));
                customer.setLongitude(c.getString(c.getColumnIndex(db.KEY_LONGITUDE)).equals("") ? "0.000000" : c.getString(c.getColumnIndex(db.KEY_LONGITUDE)));
                customer.setCustomerName(Settings.getString(App.LANGUAGE).equals("en") ? c.getString(c.getColumnIndex(db.KEY_NAME1)) : c.getString(c.getColumnIndex(db.KEY_NAME3)));
                arrayList.add(customer);
            }
            while (c.moveToNext());
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }
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
        Marker marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet));
        markers.add(marker);
        return marker;
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CustomerOperationsMap Page") // TODO: Define a title for the content shown.
                        // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }
    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
