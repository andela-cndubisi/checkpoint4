package checkpoint.andela.com.productivitytracker.google.manager;

import android.location.Geocoder;
import android.location.Location;
import android.os.Parcelable;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by andela-cj on 17/10/2015.
 */
public class GoogleMapManager implements OnMapReadyCallback {
    private ArrayList<Location> locations = new ArrayList<>();

    MapFragment mapFragment;
    GoogleMap googleMap;

    public GoogleMapManager(MapFragment fragment) {
        this.mapFragment = fragment;
        googleMap = mapFragment.getMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng = null;
        for (Location location : locations) {
            Geocoder coder = new Geocoder(mapFragment.getActivity().getApplicationContext(), Locale.US);

            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            String address = "";
            try {
                address = coder.getFromLocation(location.getLatitude(),location.getLongitude(),1).get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            googleMap.addMarker(new MarkerOptions().position(latLng).title(address));
        }
        CameraUpdate updateFactory = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        googleMap.moveCamera(updateFactory);
        googleMap.animateCamera(updateFactory);
    }

    public void addLocation(Parcelable parcelable) {
        Location location  = (Location) parcelable;
        locations.add(location);
    }
}
