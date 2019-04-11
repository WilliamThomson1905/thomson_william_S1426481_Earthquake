package org.me.gcu.coursework;
//
// Name                 William Thomson
// Student ID           S1426481
// Programme of Study   Computing
//
import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
// import com.google.android.gms.tasks.Task;

import java.lang.Object;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class EarthquakeMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "EarthquakeMapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    LinkedList<Earthquake> allEarthquakes;

    Button startDateBtn;
    private DatePickerDialog.OnDateSetListener startDateSetListener;


    private String[] filteredLngLat;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);

        }

        int countOfQuakes = 0;
        for(int i = 0; i < lnglatEarthquakes.length; i++){
            String[] listOfData = lnglatEarthquakes[i].split(",");
            String lat = listOfData[0];
            String lng = listOfData[1];

            LatLng location = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(locationEarthquakes[i])
                        .snippet("Mag : " + magnitudeEarthquakes[i] + "\n"
                            + "Depth: " +depthEarthquakes[i] + "\n"
                            + "Date: " +pubDateEarthquakes[i] + "\n"
                            + "Time: " +pubTimeEarthquakes[i] + "\n"
                            + "Lat/Long: " + lat + "/" + lng
                        )
            );
            mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(EarthquakeMapActivity.this));
            countOfQuakes++;

        }

        TextView tvCountOfQuakes = (TextView) findViewById(R.id.tvCountOfQuakes);
        tvCountOfQuakes.setText("Earthquakes Count: " + countOfQuakes);


        //moveCamera(glasgow, 15f);



        //mMap.setMyLocationEnabled(true);
        // mMap.getMaxZoomLevel();
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the Camera to lat" + latLng.latitude
                + "to lng" + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{

            if(mLocationPermissionGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location)task.getResult();
                            if(currentLocation != null){
                                moveCamera(new LatLng(55.859238, 	-4.296141),
                                        1f);
                            }
                            else
                            {
                                moveCamera(new LatLng(34.1234, -151.345),
                                        1f);
                            }



                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(EarthquakeMapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        } catch(SecurityException e){
            Log.e(TAG, "getDeviceLocation: security exeception" + e.getMessage());

        }
    }




    String[] lnglatEarthquakes;
    String[] locationEarthquakes;
    String[] magnitudeEarthquakes;
    String[] depthEarthquakes;
    String[] pubDateEarthquakes;
    String[] pubTimeEarthquakes;
    String urlSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_map);

        ImageView rssFeedChange = (ImageView) findViewById(R.id.btnRSSFeedChange);
        rssFeedChange.setVisibility(View.INVISIBLE);


        //allEarthquakes = getIntent().getExtra("allEarthquakes");

        // tvEarthquakesLngLat = findViewById(R.id.tvEarthquakesLngLat);
        lnglatEarthquakes = getIntent().getStringArrayExtra("earthquakesLongLat");
        locationEarthquakes = getIntent().getStringArrayExtra("earthquakesLocation");
        magnitudeEarthquakes = getIntent().getStringArrayExtra("earthquakesMagnitude");
        depthEarthquakes = getIntent().getStringArrayExtra("earthquakesDepth");
        pubDateEarthquakes = getIntent().getStringArrayExtra("earthquakesPubDate");
        pubTimeEarthquakes = getIntent().getStringArrayExtra("earthquakesPubTime");
        // tvEarthquakesLngLat.setText(lnglatEarthquakes[1].toString());

        //
        urlSource = getIntent().getStringExtra("urlSource");

        getLocationPermission();

        final ImageButton btnEarthViewOfEarthquakes = (ImageButton) findViewById(R.id.btnEarthViewOfEarthquakes);
        btnEarthViewOfEarthquakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this, "Earth View Clicked", Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());
                // startActivity(new Intent(EarthquakeMapActivity.this, EarthquakeMapActivity.class));
            }
        });
        final ImageButton btnListOfEarthquakes = (ImageButton) findViewById(R.id.btnListOfEarthquakes);
        btnListOfEarthquakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this, "List View Clicked", Toast.LENGTH_LONG).show();

                Intent in = new Intent(EarthquakeMapActivity.this, MainActivity.class);
                in.putExtra("urlSource", urlSource);



                startActivity(in);
            }
        });

        startDateBtn = (Button)findViewById(R.id.dateOne);
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month= calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =  new DatePickerDialog(EarthquakeMapActivity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog,
                        startDateSetListener, year, month, day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                startDateBtn.setText("Eathquakes After: " + dayOfMonth+"/"+month+"/"+year);
                mMap.clear();
                String date = dayOfMonth+"/"+month+"/"+year;
                Log.e(TAG, "datePicker1: " + date);

                // passing minimum date
                setMarkers(date);

                Toast.makeText(EarthquakeMapActivity.this, "Earthquakes after " + date + " shown. ", Toast.LENGTH_LONG).show();
                //Toast.makeText(EarthquakeMapActivity.this, "Earthquakes after " + date + " shown. ", Toast.LENGTH_LONG).show();

            }
        };

    }


    private void setMarkers(String date) {

        int countOfQuakes =0;

        for (int i = 0; i < lnglatEarthquakes.length; i++) {


            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date quakeDate = sdf.parse(pubDateEarthquakes[i]);
                Date filterDate = sdf.parse(date);


                if(quakeDate.compareTo(filterDate)>0){
                    Log.e(TAG, "earthquakeDate: " + quakeDate);
                    Log.e(TAG, "FilterDate: " + filterDate);


                    countOfQuakes++;


                    String[] listOfData = lnglatEarthquakes[i].split(",");
                    String lat = listOfData[0];
                    String lng = listOfData[1];

                    LatLng location = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(locationEarthquakes[i])
                            .snippet("Mag : " + magnitudeEarthquakes[i] + "\n"
                                    + "Depth: " + depthEarthquakes[i] + "\n"
                                    + "Date: " + pubDateEarthquakes[i] + "\n"
                                    + "Time: " + pubTimeEarthquakes[i] + "\n"
                                    + "Lat/Long: " + lat + "/" + lng
                            )
                    );
                }

            } catch(java.text.ParseException e){}

            // mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(EarthquakeMapActivity.this));

        }

        TextView tvCountOfQuakes = (TextView) findViewById(R.id.tvCountOfQuakes);
        tvCountOfQuakes.setText("Earthquakes Count: " + countOfQuakes);

    }


    private void getLocationPermission(){

        Log.d(TAG, "getLocationPermission: getting Location Permissions");


        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                // set
                mLocationPermissionGranted = true;
                initMap();

            }else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }


    private void initMap(){

        Log.d(TAG, "initmap: initialising map");

        SupportMapFragment mapFragment
                = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");

                            return;

                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionGranted = true;

                    //initialise map
                    //initMap();


                }
            }
        }
    }
}