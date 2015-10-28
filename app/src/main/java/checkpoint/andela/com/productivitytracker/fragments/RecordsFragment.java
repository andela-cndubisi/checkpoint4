package checkpoint.andela.com.productivitytracker.fragments;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import checkpoint.andela.com.productivitytracker.R;

/**
 * Created by andela-cj on 14/10/2015.
 */
public class RecordsFragment extends DialogFragment implements OnMapReadyCallback  {
    private ArrayList<Location> locations;
    SupportMapFragment mapFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int height = 500;
        View v = inflater.inflate(R.layout.activity_map, container);
        mapFragment = (SupportMapFragment) ((AppCompatActivity)getActivity()).getSupportFragmentManager()
                .findFragmentById(R.id.map);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view;
        if ((view = mapFragment.getView())!=null){
            view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height));
        }
        return v;
    }

    @Override
    public void onStart() {
        mapFragment.getMapAsync(this);
        super.onStart();
    }

    public void setLocations(ArrayList<Location> locations){
        this.locations = locations;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final int zoomLevel = 16;
        LatLng latLng = null;
        for (Location location : locations) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLng));
        }
        CameraUpdate updateFactory = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel);
        googleMap.moveCamera(updateFactory);
        googleMap.animateCamera(updateFactory);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mapFragment != null)
            ((AppCompatActivity)getActivity()).getSupportFragmentManager().beginTransaction().remove(mapFragment).commit();
        super.onDismiss(dialog);
    }
}
