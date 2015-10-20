package checkpoint.andela.com.productivitytracker.google.manager;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import checkpoint.andela.com.productivitytracker.data.ProductivityDBHelper;

import static checkpoint.andela.com.productivitytracker.data.ProductivityContract.LocationEntry;

/**
 * Created by andela-cj on 17/10/2015.
 */
public class GoogleLocationManager implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private Location currentLocation;
    private int recordedLocations = 0;
    private boolean once = false;
    boolean didChange = false;
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, locationRequest, this);
    }

    public void connect(){
        mGoogleClient.connect();
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (!isSetOnce()){
            currentLocation = location;
            once = true;
            return;
        }
        if (currentLocation!=null){
            float diff = currentLocation.distanceTo(location);
            Toast.makeText(mActivity.getApplicationContext()
                    , String.format("long:%f\nlat:%f",location.getLongitude(),location.getLatitude()), Toast.LENGTH_SHORT).show();

            Log.i(getClass().getSimpleName(), String.format("current, long:%f\nlat:%f", currentLocation.getLongitude(), currentLocation.getLatitude()));
            Log.i(getClass().getSimpleName(),String.format("received, long:%f\nlat:%f", location.getLongitude(), location.getLatitude()));


            if (diff >= 20){
                locationChanged();
                currentLocation = location;
            }
        }
        Log.i("My Current Location", String.format("Latitude: %f\n Longitude: %f", location.getLatitude(), location.getLongitude()));
    }

    private boolean isSetOnce() {
        return once;
    }

    public boolean locationChanged(){
        didChange = !didChange;
        return didChange;
    }

    public boolean didChange() {
        return didChange;
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    public int getRecordedLocations(){
        return recordedLocations;
    }

    public void disconnect(){
        mGoogleClient.disconnect();
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    private int getInterval(){
        return (interval/5) * 10000;
    }

    public void saveLocation() {
        SQLiteDatabase db = new ProductivityDBHelper(mActivity.getApplicationContext())
                .getWritableDatabase();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS", Locale.US);
        Date date = new Date();
        String today = dateFormat.format(date).split(" ")[0];
        ContentValues values = LocationEntry.createContentFromLocation(
                currentLocation.getLongitude()
                ,currentLocation.getLatitude()
                ,""
                ,today);
        long rowId = db.insert(LocationEntry.TABLE_NAME, null, values);
        if (rowId !=-1)
            recordedLocations++;
        db.close();
    }
}
