package pe.com.app.nose;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.com.app.nose.Entidad.Eventodb;
import pe.com.app.nose.Entidad.FirebaseReferences;
import pe.com.app.nose.Interfaces.DirectionFinderListener;
import pe.com.app.nose.Entidad.Route;
import pe.com.app.nose.Model.DirectionFinder;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment implements OnMapReadyCallback, DirectionFinderListener {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private FloatingActionButton create;
    private GoogleMap mMap;
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;
    String mensaje1;
    String categoriaseleccionado;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private TextView tvDistance;
    private TextView txtimeD;
    private Spinner listacategoria;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseDatabase mfirebaseDatabase;

    Marker mMarkerArtistico, mMarkerConcierto,mMarkerTecnologia, mMarkerDeporte, mMarkerConferencia, mMarkerModa, mMarkerGastronomia, mMarkerOtros;
    private List<Marker> mMarkerArtisticoList = new ArrayList<>();
    private List<Marker> mMarkerConciertoList = new ArrayList<>();
    private List<Marker> mMarkerTecnologiaList = new ArrayList<>();
    private List<Marker> mMarkerDeporteList = new ArrayList<>();
    private List<Marker> mMarkerConferenciaList = new ArrayList<>();
    private List<Marker> mMarkerModaList = new ArrayList<>();
    private List<Marker> mMarkerGastronomiaList = new ArrayList<>();
    private List<Marker> mMarkerOtrosList = new ArrayList<>();

    private static int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int SECACTI_REQUEST_CODE = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SECACTI_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                double ubitlagdirection = data.getDoubleExtra("DIRECCIONLAG", 0);
                double ubitlongdirection = data.getDoubleExtra("DIRECCIONLONG", 0);
                String origin = lat + "," + lng;
                String destination = Double.toString(ubitlagdirection) + "," + Double.toString(ubitlongdirection);

                try {
                    new DirectionFinder(this, origin, destination).execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }
        } else if (resultCode == SECACTI_REQUEST_CODE) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();

        tvDistance = (TextView) view.findViewById(R.id.tvDistance);
        txtimeD = (TextView) view.findViewById(R.id.txtimeD);
        listacategoria = (Spinner) view.findViewById(R.id.listacategoria);



        /*getLocation2();*/
        create = (FloatingActionButton) view.findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Popup_create.class);
                startActivityForResult(intent, 345);
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


        final DatabaseReference myRefEvento = mfirebaseDatabase.getReference(FirebaseReferences.EVENTO_REFERENCE);
        myRefEvento.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                for (final DataSnapshot datasnapshot : dataSnapshot.getChildren()) {

                    final String llave = datasnapshot.getKey();

                    final Eventodb eventodb = datasnapshot.getValue(Eventodb.class);

                    final String titulo = eventodb.getTitulo();
                    final String categoria = eventodb.getCategoria();
                    final String hora = eventodb.getHora();
                    final double ubitlati = eventodb.getUbilat();
                    final double ubitlongi = eventodb.getUbilong();
                    final String date = eventodb.getFecha();

                    listacategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //categoriaseleccionado = listacategoria.getSelectedItem().toString();
                            selecToPosition(listacategoria.getSelectedItem().toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    String daten = formato.format(new Date());

                    Date dateFB = null;
                    Date dateC = null;

                    if (date==null){

                    } else if (date!=null){
                        try {
                            dateFB = formato.parse(date);
                            dateC = formato.parse(daten);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (dateFB.after(dateC)) {
                            if (categoria == null && date==null) {
                                Toast.makeText(getContext(), "Cargando mapa", Toast.LENGTH_SHORT).show();
                            } else if (categoria != null && date !=null) {
                                switch (categoria) {
                                    case "Artistico":
                                        mMarkerArtistico = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(ubitlati, ubitlongi))
                                                .title(titulo)
                                                .snippet(llave)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.artistico)));
                                        mMarkerArtisticoList.add(mMarkerArtistico);
                                        break;
                                    case "Concierto":
                                        mMarkerConcierto = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(ubitlati, ubitlongi))
                                                .title(titulo)
                                                .snippet(llave)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.concierto)));
                                        mMarkerConciertoList.add(mMarkerConcierto);
                                        break;
                                    case "Tecnologia":
                                        mMarkerTecnologia =  mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(ubitlati, ubitlongi))
                                                .title(titulo)
                                                .snippet(llave)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.tecnologia)));
                                        mMarkerTecnologiaList.add(mMarkerTecnologia);
                                        break;
                                    case "Deporte":
                                        mMarkerDeporte =  mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(ubitlati, ubitlongi))
                                                .title(titulo)
                                                .snippet(llave)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.deportivo)));
                                        mMarkerDeporteList.add(mMarkerDeporte);
                                        break;
                                    case "Conferencia":
                                        mMarkerConferencia  =  mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(ubitlati, ubitlongi))
                                                .title(titulo)
                                                .snippet(llave)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.conferencia)));
                                        mMarkerConferenciaList.add(mMarkerConferencia);
                                        break;
                                    case "Moda":
                                        mMarkerModa =  mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(ubitlati, ubitlongi))
                                                .title(titulo)
                                                .snippet(llave)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.moda)));
                                        mMarkerModaList.add(mMarkerModa);
                                        break;
                                    case "Gastronomia":
                                        mMarkerGastronomia =  mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(ubitlati, ubitlongi))
                                                .title(titulo)
                                                .snippet(llave)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gastronomia)));
                                        mMarkerGastronomiaList.add(mMarkerGastronomia);
                                        break;
                                    case "Otros":
                                        mMarkerOtros =  mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(ubitlati, ubitlongi))
                                                .title(titulo)
                                                .snippet(llave)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.otros)));
                                        mMarkerOtrosList.add(mMarkerOtros);
                                        break;
                                }
                            }
                        }

                    }


                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            if (marker.getSnippet() != null) {
                                Intent intent = new Intent(getActivity(), PopupDescripcion.class);
                                String claveone = marker.getSnippet();
                                intent.putExtra("CLAVEONE", claveone);
                                startActivityForResult(intent, SECACTI_REQUEST_CODE);
                            } else {
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


    }



    public void locationStart() {
        LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1200, 0, locListener);
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
            for (Polyline polyline : polylinePaths) {
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Su GPS esta desabilitado, desea habilitarlo?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("Unussed") final DialogInterface dialog, @SuppressWarnings("Unussed") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("Unussed") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private void selecToPosition(String categoriaseleccionado) {
        Log.d(TAG, "ArtisticoLIst" + categoriaseleccionado);
        DatabaseReference myRefEvento = mfirebaseDatabase.getReference();
        Query query = myRefEvento.child(FirebaseReferences.EVENTO_REFERENCE)
                .orderByChild("categoria")
                .equalTo(categoriaseleccionado);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataResult : dataSnapshot.getChildren()) {
                        Eventodb eventodb = dataResult.getValue(Eventodb.class);
                        switch (eventodb.getCategoria()) {
                            case "Artistico":

                                mostrarMarkerArtisticoLista(mMarkerArtisticoList);
                                ocultarMarkerConciertoLista(mMarkerConciertoList);
                                ocultarMarkerTecnologiaLista(mMarkerTecnologiaList);
                                ocultarMarkerDeporteLista(mMarkerDeporteList);
                                ocultarMarkerConferenciaLista(mMarkerConferenciaList);
                                ocultarMarkerModaLista(mMarkerModaList);
                                ocultarMarkerGastronomiaLista(mMarkerGastronomiaList);
                                ocultarMarkerOtrosLista(mMarkerOtrosList);

                                break;
                            case "Concierto":
                                mostrarMarkerConciertoLista(mMarkerConciertoList);
                                ocultarMarkerArtisticoLista(mMarkerArtisticoList);
                                ocultarMarkerTecnologiaLista(mMarkerTecnologiaList);
                                ocultarMarkerDeporteLista(mMarkerDeporteList);
                                ocultarMarkerConferenciaLista(mMarkerConferenciaList);
                                ocultarMarkerModaLista(mMarkerModaList);
                                ocultarMarkerGastronomiaLista(mMarkerGastronomiaList);
                                ocultarMarkerOtrosLista(mMarkerOtrosList);
                                break;
                            case "Tecnologia":
                                mostrarMarkerTecnologicoLista(mMarkerTecnologiaList);
                                ocultarMarkerArtisticoLista(mMarkerArtisticoList);
                                ocultarMarkerConciertoLista(mMarkerConciertoList);
                                ocultarMarkerDeporteLista(mMarkerDeporteList);
                                ocultarMarkerConferenciaLista(mMarkerConferenciaList);
                                ocultarMarkerModaLista(mMarkerModaList);
                                ocultarMarkerGastronomiaLista(mMarkerGastronomiaList);
                                ocultarMarkerOtrosLista(mMarkerOtrosList);
                                break;

                            case "Deporte":
                                mostrarMarkerDeporteLista(mMarkerDeporteList);
                                ocultarMarkerArtisticoLista(mMarkerArtisticoList);
                                ocultarMarkerConciertoLista(mMarkerConciertoList);
                                ocultarMarkerTecnologiaLista(mMarkerTecnologiaList);
                                ocultarMarkerConferenciaLista(mMarkerConferenciaList);
                                ocultarMarkerModaLista(mMarkerModaList);
                                ocultarMarkerGastronomiaLista(mMarkerGastronomiaList);
                                ocultarMarkerOtrosLista(mMarkerOtrosList);
                                break;

                            case "Conferencia":
                                mostrarMarkerConferenciaLista(mMarkerConferenciaList);
                                ocultarMarkerArtisticoLista(mMarkerArtisticoList);
                                ocultarMarkerConciertoLista(mMarkerConciertoList);
                                ocultarMarkerTecnologiaLista(mMarkerTecnologiaList);
                                ocultarMarkerDeporteLista(mMarkerDeporteList);
                                ocultarMarkerModaLista(mMarkerModaList);
                                ocultarMarkerGastronomiaLista(mMarkerGastronomiaList);
                                ocultarMarkerOtrosLista(mMarkerOtrosList);
                                break;

                            case "Moda":
                                mostrarMarkerModaLista(mMarkerModaList);
                                ocultarMarkerArtisticoLista(mMarkerArtisticoList);
                                ocultarMarkerConciertoLista(mMarkerConciertoList);
                                ocultarMarkerTecnologiaLista(mMarkerTecnologiaList);
                                ocultarMarkerDeporteLista(mMarkerDeporteList);
                                ocultarMarkerConferenciaLista(mMarkerConferenciaList);
                                ocultarMarkerGastronomiaLista(mMarkerGastronomiaList);
                                ocultarMarkerOtrosLista(mMarkerOtrosList);

                                break;

                            case "Gastronomia":
                                mostrarMarkerGastronomiaLista(mMarkerGastronomiaList);
                                ocultarMarkerArtisticoLista(mMarkerArtisticoList);
                                ocultarMarkerConciertoLista(mMarkerConciertoList);
                                ocultarMarkerTecnologiaLista(mMarkerTecnologiaList);
                                ocultarMarkerDeporteLista(mMarkerDeporteList);
                                ocultarMarkerConferenciaLista(mMarkerConferenciaList);
                                ocultarMarkerModaLista(mMarkerModaList);
                                ocultarMarkerOtrosLista(mMarkerOtrosList);
                                break;

                            case "Otros":
                                mostrarMarkerOtrosLista(mMarkerOtrosList);
                                ocultarMarkerArtisticoLista(mMarkerArtisticoList);
                                ocultarMarkerConciertoLista(mMarkerConciertoList);
                                ocultarMarkerTecnologiaLista(mMarkerTecnologiaList);
                                ocultarMarkerDeporteLista(mMarkerDeporteList);
                                ocultarMarkerConferenciaLista(mMarkerConferenciaList);
                                ocultarMarkerModaLista(mMarkerModaList);
                                ocultarMarkerGastronomiaLista(mMarkerGastronomiaList);
                                break;

                            default:
                                mostrarMarkerArtisticoLista(mMarkerArtisticoList);
                                mostrarMarkerConciertoLista(mMarkerConciertoList);
                                mostrarMarkerTecnologicoLista(mMarkerTecnologiaList);
                                mostrarMarkerDeporteLista(mMarkerDeporteList);
                                mostrarMarkerConferenciaLista(mMarkerConferenciaList);
                                mostrarMarkerModaLista(mMarkerModaList);
                                mostrarMarkerGastronomiaLista(mMarkerGastronomiaList);
                                mostrarMarkerOtrosLista(mMarkerOtrosList);
                                break;

                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void mostrarMarkerOtrosLista(List<Marker> mMarkerOtrosList) {
        for(Marker marker :mMarkerOtrosList){
            marker.setVisible(true);
        }
    }

    private void mostrarMarkerGastronomiaLista(List<Marker> mMarkerGastronomiaList) {
        for(Marker marker :mMarkerGastronomiaList){
            marker.setVisible(true);
        }
    }

    private void mostrarMarkerModaLista(List<Marker> mMarkerModaList) {
        for(Marker marker :mMarkerModaList){
            marker.setVisible(true);
        }
    }

    private void mostrarMarkerConferenciaLista(List<Marker> mMarkerConferenciaList) {
        for(Marker marker :mMarkerConferenciaList){
            marker.setVisible(true);
        }
    }

    private void mostrarMarkerDeporteLista(List<Marker> mMarkerDeporteList) {
        for(Marker marker :mMarkerDeporteList){
            marker.setVisible(true);
        }
    }

    private void ocultarMarkerOtrosLista(List<Marker> mMarkerOtrosList) {
        for(Marker marker : mMarkerOtrosList){
            marker.setVisible(false);
        }
    }

    private void ocultarMarkerGastronomiaLista(List<Marker> mMarkerGastronomiaList) {
        for(Marker marker : mMarkerGastronomiaList){
            marker.setVisible(false);
        }
    }

    private void ocultarMarkerModaLista(List<Marker> mMarkerModaList) {
        for(Marker marker : mMarkerModaList){
            marker.setVisible(false);
        }
    }

    private void ocultarMarkerConferenciaLista(List<Marker> mMarkerConferenciaList) {
        for(Marker marker : mMarkerConferenciaList){
            marker.setVisible(false);
        }
    }

    private void ocultarMarkerDeporteLista(List<Marker> mMarkerDeporteList) {
        for(Marker marker : mMarkerDeporteList){
            marker.setVisible(false);
        }
    }

    private void ocultarMarkerTecnologiaLista(List<Marker> mMarkerTecnologiaList) {
        for(Marker marker : mMarkerTecnologiaList){
            marker.setVisible(false);
        }
    }

    private void mostrarMarkerTecnologicoLista(List<Marker> mMarkerTecnologiaList) {
        for(Marker marker :mMarkerTecnologiaList){
            marker.setVisible(true);
        }
    }

    private void ocultarMarkerArtisticoLista(List<Marker> mMarkerArtisticoList) {
        for (Marker marker : mMarkerArtisticoList) {
            marker.setVisible(false);
        }
    }

    private void mostrarMarkerConciertoLista(List<Marker> mMarkerConciertoList) {
        for (Marker marker : mMarkerConciertoList) {
            marker.setVisible(true);
        }
    }

    private void ocultarMarkerConciertoLista(List<Marker> mMarkerConciertoList) {
        for (Marker marker : mMarkerConciertoList) {
            marker.setVisible(false);
        }
    }

    private void mostrarMarkerArtisticoLista(List<Marker> mMarkerArtisticoList) {
        for (Marker marker : mMarkerArtisticoList) {
            marker.setVisible(true);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }
}
