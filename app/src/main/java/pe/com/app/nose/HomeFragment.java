package pe.com.app.nose;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationListener,GoogleMap.OnInfoWindowClickListener {


/*  private TextView uidTextView;
  private TextView nameTextView;
  private TextView emailTextView;
  private ImageView photoImageView;
  private TextView idTextView;
  private GoogleApiClient googleApiClient;
  private FirebaseAuth firebaseAuth;
  private  FirebaseAuth.AuthStateListener firebaseAuthListener;*/

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private MapView mapView;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 15f;
    private LocationManager locationManager;
    private String provider;

    private final int FINE_LOCATION_PERMISSION = 9999;

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);

            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1,  this);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

      /*uidTextView = (TextView) view.findViewById(R.id.uidTextView);
      photoImageView = (ImageView) view.findViewById(R.id.photoImageView);
      nameTextView = (TextView) view.findViewById(R.id.nameTextView);
      emailTextView = (TextView) view.findViewById(R.id.emailTextView);
      idTextView = (TextView) view.findViewById(R.id.idTextView);*/

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getLocation2();

        return view;
    }

    public void getLocation2(){

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);

        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);

            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location!=null){

        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        mMap.setMinZoomPreference(14.0f);

        // Add a marker in Sydney and move the camera
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        /*if (mLocationPermissionsGranted){
            getLocation();
            if (ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
        }*/
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);


    }

    private void getLocation(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient( getContext());
        try{
            Task<Location> location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                    if(task.isSuccessful()){
                        Location currentLocation =(Location) task.getResult();
                        moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),
                                DEFAULT_ZOOM);
                    }
                }
            });
        }catch (SecurityException e){
            Log.d("Hey Calma:","tu error u.u"+e.getMessage());
        }
    }

    private void  moveCamera(LatLng latLng,float zoom){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat,lng;
        lat = location.getLatitude();
        lng = location.getLongitude();
        // Add a marker in Sydney and move the camera
        LatLng myPosition = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(myPosition).title("My Position"));

        Marker infoWindows = mMap.addMarker(new MarkerOptions().position(myPosition).title("My Position").snippet("This is my postion"));
        infoWindows.showInfoWindow();
        mMap.setOnInfoWindowClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
        Log.i("Log info", " Lat : "+lat+" Long : "+lng);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getActivity(),"Marker "+marker.getTitle()+" is clicked",Toast.LENGTH_SHORT).show();
    }
}
