package checkpoint.andela.com.productivitytracker.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import checkpoint.andela.com.productivitytracker.data.ProductivityContract;
import checkpoint.andela.com.productivitytracker.data.ProductivityDBHelper;

public class GoogleLocationManager implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private Context content;

    private Location currentLocation;
    private int recordedLocations = 0;
    private boolean once = false;
    boolean didChange = false;
    private GoogleApiClient mGoogleClient;
    private int interval = 0;

    public GoogleLocationManager(Context applicationContext) {
        this.content = applicationContext;

        mGoogleClient = new GoogleApiClient.Builder(content).addApi(LocationServices.API)
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
        float diff = currentLocation.distanceTo(location);
         if (diff >= 20){
             didChange = true;
             saveLocation();
         }
    }

    private boolean isSetOnce() {
        return once;
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
        SQLiteDatabase db = new ProductivityDBHelper(content)
                .getWritableDatabase();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = new Date();
        String today = dateFormat.format(date);
        Geocoder coder = new Geocoder(content, Locale.US);
        String address = "";
        try {
             address = coder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1).get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ContentValues values = ProductivityContract.LocationEntry.createContentFromLocation(
                currentLocation.getLongitude()
                , currentLocation.getLatitude()
                , address
                , today, interval);
        long rowId = db.insert(ProductivityContract.LocationEntry.TABLE_NAME, null, values);
        if (rowId !=-1)
            recordedLocations++;
        db.close();
    }

}
