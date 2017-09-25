package gbc.sa.vansales.google;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import gbc.sa.vansales.utils.Callback;
/**
 * Created by Rakshit on 28-Jan-17.
 */
public class Location implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Callback callback;
    private GoogleApiClient client;
    private Context context;
    public Location(Context context, Callback callback) {
        this.callback = callback;
        this.context = context;
        client = new GoogleApiClient.Builder(context).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();
    }
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        android.location.Location location = LocationServices.FusedLocationApi.getLastLocation(client);

        callback.callbackSuccess(location);

        client.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        callback.callbackFailure();
    }

    @Override
    public void onConnectionSuspended(int i) {
        client.connect();
    }
}
