package pe.com.app.nose;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.Equalizer;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class   HomeFragment extends Fragment implements OnMapReadyCallback, DirectionFinderListener {


    private FloatingActionButton create;
    private GoogleMap mMap;
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;
    String mensaje1;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private TextView tvDistance;
    private TextView txtimeD;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseDatabase mfirebaseDatabase;

    private static int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int SECACTI_REQUEST_CODE = 0;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==SECACTI_REQUEST_CODE){
            if (resultCode==RESULT_OK){
                double ubitlagdirection = data.getDoubleExtra("DIRECCIONLAG",0);
                double ubitlongdirection = data.getDoubleExtra("DIRECCIONLONG",0);
                String origin = lat + "," + lng;
                String destination = Double.toString(ubitlagdirection) + "," + Double.toString(ubitlongdirection);

                try {
                    new DirectionFinder(this, origin, destination).execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }
        }else  if(resultCode==SECACTI_REQUEST_CODE){
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRefEvento = mfirebaseDatabase.getReference(FirebaseReferences.EVENTO_REFERENCE);
        tvDistance = (TextView) view.findViewById(R.id.tvDistance);
        txtimeD =(TextView) view.findViewById(R.id.txtimeD);
        myRefEvento.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                for(final DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                     final String llave = datasnapshot.getKey();
                     final Eventodb eventodb = datasnapshot.getValue(Eventodb.class);
                     final String titulo = eventodb.getTitulo();
                     final String hora = eventodb.getHora();
                    final double ubitlati = eventodb.getUbilat();
                    final double ubitlongi = eventodb.getUbilong();


                    Marker mMarker =  mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(ubitlati, ubitlongi))
                            .title(titulo)
                            .snippet(llave)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            if (marker.getSnippet() !=null){
                                Intent intent = new Intent(getActivity(),PopupDescripcion.class);
                                String claveone = marker.getSnippet();
                                intent.putExtra("CLAVEONE",claveone);
                                startActivityForResult(intent,SECACTI_REQUEST_CODE);
                            }

                              else {
                                Log.d("Nothing", "no hay nada varon");
                            }

                            return false;

                        }
                    });



                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        /*getLocation2();*/
        create = (FloatingActionButton)view.findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Popup_create.class);
                startActivityForResult(intent,345);
            }
        });





        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);




        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);

            LocationManager locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                builAlertMessageNoGps();
            }
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

        miUbicacion();


    }

    public void locationStart(){
        LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled){
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
    }


    private void AgregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate MiUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 10);
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Mi Ubicación"));

        mMap.animateCamera(MiUbicacion);
    }

    private void ActualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            AgregarMarcador(lat, lng);

        }
    }

    LocationListener locListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            ActualizarUbicacion(location);
            /*setLocation(location);*/
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            mensaje1 = ("GPS Activado");
            Mensaje();
        }

        @Override
        public void onProviderDisabled(String s) {
            mensaje1 = ("GPS Desactivado");
            locationStart();
            Mensaje();
        }
    };



    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
            return;
        } else {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ActualizarUbicacion(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1200,0,locListener);
        }

    }

    public void Mensaje() {
        Toast toast = Toast.makeText(getActivity(), mensaje1, Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(getActivity(), "Por favor espere",
                "Encontrando la dirección..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }



    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            txtimeD.setText(route.duration.text);
            tvDistance.setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    private void builAlertMessageNoGps() {
        final AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        builder.setMessage("Su GPS esta desabilitado, desea habilitarlo?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("Unussed")final DialogInterface dialog, @SuppressWarnings ("Unussed") final int id){
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, @SuppressWarnings ("Unussed") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert=builder.create();
        alert.show();
    }
}
