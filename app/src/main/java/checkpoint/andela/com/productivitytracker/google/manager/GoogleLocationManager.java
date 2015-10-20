package checkpoint.andela.com.productivitytracker.google.manager;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by andela-cj on 17/10/2015.
 */
public class GoogleLocationManager implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private Activity mActivity;
    private GoogleApiClient mGoogleClient;
    private int interval = 1000;

    public GoogleLocationManager(Activity activity) {
        mActivity = activity;
        mGoogleClient = new GoogleApiClient.Builder(activity).addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(getInterval());
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient,locationRequest,this);
    }

    public void connect(){
        mGoogleClient.connect();
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("My Current Location", String.format("Latitude: %f\n Longitude: %f", location.getLatitude(), location.getLongitude()));
    }

    public boolean loactionChanged(){
        return false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void disconnect(){
        mGoogleClient.disconnect();
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
    private int getInterval(){

        return (interval/5) * 1000;
    }
}
