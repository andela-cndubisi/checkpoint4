package checkpoint.andela.com.productivitytracker.managers;

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
import java.util.ArrayList;
import java.util.Locale;

import checkpoint.andela.com.productivitytracker.db.ProductivityDBHelper;

public class GoogleLocationManager implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private ArrayList<Location> savedLocations = new ArrayList<>();
    private Context content;
    private Location currentLocation;
    private int recordsCount = 0;
    private boolean once = false;
    boolean didChange = false;
    private GoogleApiClient mGoogleClient;
    private int interval = 0;
    private boolean recorded = false;

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
        if (!isSetOnce() && !isRecorded()){
            currentLocation = location;
            once = true;
            return;
        }
        float diff = currentLocation.distanceTo(location);
         if (diff >= 50){
             didChange = true;
         }
    }

    private boolean isSetOnce() {
        return once;
    }


    public boolean didChange() {
        return didChange;
    }

    public void reset(){
        didChange = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    public int getRecordsCount(){
        return recordsCount;
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
        ProductivityDBHelper dbHelper = new ProductivityDBHelper(content);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String address = "";
        if (currentLocation!= null) {
            try {
                Geocoder coder = new Geocoder(content, Locale.US);
                address = coder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1).get(0).getAddressLine(0);

            } catch (IOException e) {
                address = "unknown";
                e.printStackTrace();
            }
            long val = dbHelper.saveLocationAndAddress(currentLocation, address, interval);
            if (val != -1 ) {
                recordsCount++;
                savedLocations.add(currentLocation);
            }
        }
        db.close();
    }

    public ArrayList getSavedLocations(){
        return savedLocations;
    }
    public boolean isRecorded (){
        return recorded;
    }
    public void updateWithChange(boolean didChange) {
        if (didChange){
            currentLocation = null;
            once = false;
            recorded = false;
        }else {
            recorded = true;
        }
    }
}
