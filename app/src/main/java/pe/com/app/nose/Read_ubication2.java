package pe.com.app.nose;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Read_ubication2 extends AppCompatActivity implements GoogleMap.OnMarkerDragListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private FloatingActionButton atrasMap2;

    private double lat;
    private double longLt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_ubication2);

        getSupportActionBar().hide();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;



        atrasMap2 = (FloatingActionButton) findViewById(R.id.atrasMap2);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map3);
        mapFragment.getMapAsync(this);

        atrasMap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Read_ubication2.this, Edit_event.class);
                intent.putExtra("latitud",lat);
                intent.putExtra("longitud",longLt);
                setResult(Read_ubication2.RESULT_OK,intent);
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerDragListener(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);

        UiSettings uiSettings = googleMap.getUiSettings();

        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        LatLng lima = new LatLng(-12.0463731, -77.042754);

        googleMap.addMarker(new MarkerOptions().position(lima)
                .title("Puedes Arrastrarme hasta la ubicación")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lima,10));

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onMarkerDragStart(Marker marker) {
        Geocoder geocoder = new Geocoder(Read_ubication2.this);
        List<Address> list = null;

        try {
            LatLng l1 = marker.getPosition();

            longLt = marker.getPosition().longitude;
            lat = marker.getPosition().latitude;

            Toast.makeText(this, "Latitud "+lat, Toast.LENGTH_SHORT).show();

            list =geocoder.getFromLocation(l1.latitude, l1.longitude,1);
            Address address = list.get(0);
            marker.setTitle(address.getAddressLine(0));
            marker.showInfoWindow();


            Log.d("Lat","your lat"+lat);
            Log.d("Long","your long"+lat);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onMarkerDrag(Marker marker) {


    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}
