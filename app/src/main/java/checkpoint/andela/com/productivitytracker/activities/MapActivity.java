
package checkpoint.andela.com.productivitytracker.activities;

import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import checkpoint.andela.com.productivitytracker.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<PointF> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        try {
            locations = (ArrayList<PointF>) getIntent().getSerializableExtra("list");
            setTitle(getIntent().getStringExtra("title"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = null;
        for (PointF location : locations) {
            latLng = new LatLng((double)location.x, (double)location.y);
            googleMap.addMarker(new MarkerOptions().position(latLng));
        }
        CameraUpdate updateFactory = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        googleMap.moveCamera(updateFactory);
        googleMap.animateCamera(updateFactory);
    }
}
