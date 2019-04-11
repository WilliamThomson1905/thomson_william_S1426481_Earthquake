package org.me.gcu.coursework;
//
// Name                 William Thomson
// Student ID           S1426481
// Programme of Study   Computing
//
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EarthquakeDetailedActivity extends AppCompatActivity implements OnMapReadyCallback {


    TextView tvMagnitude;
    TextView tvDepth;
    TextView tvLocation;
    TextView tvDate;
    TextView tvTime;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_detailed);


        tvLocation = findViewById(R.id.tvLocation);
        String location = getIntent().getStringExtra("lvLocation");
        tvLocation.setText(location);

        tvDate = findViewById(R.id.tvDate);
        String date = getIntent().getStringExtra("lvDate");
        tvDate.setText(date);


        tvMagnitude = findViewById(R.id.tvMagnitude);
        String magnitude = getIntent().getStringExtra("lvMagnitude");
        tvMagnitude.setText(magnitude);

        tvDepth = findViewById(R.id.tvDepth);
        String depth = getIntent().getStringExtra("lvDepth");
        tvDepth.setText(depth);

        tvTime = findViewById(R.id.lvTime);
        String time = getIntent().getStringExtra("lvTime");
        tvTime.setText(time);



        SupportMapFragment mapFragment
                = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String lng = getIntent().getStringExtra("lvLong");
        String lat = getIntent().getStringExtra("lvLat");


        LatLng location = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        mMap.addMarker(new MarkerOptions()
                .position(location)
        );
        mMap.getUiSettings().setZoomControlsEnabled(true);



        moveCamera(new LatLng(Double.valueOf(lat), Double.valueOf(lng)), 15f);



        //mMap.setMyLocationEnabled(true);
        // mMap.getMaxZoomLevel();
    }

    private void moveCamera(LatLng latLng, float zoom){

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


}
