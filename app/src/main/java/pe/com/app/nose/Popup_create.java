package pe.com.app.nose;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Popup_create extends Activity  implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {


    private EditText date;
    private EditText time;
    private EditText title;
    private EditText description;
    private SwitchCompat tipo;
    private FloatingActionButton lugar2;
    private Switch selectPlace;
    private Button createEvent;
    private Spinner categoria;
    private FloatingActionButton googleInvite;

    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fireAuthStateListener;

    private int nyear, nmonth,nday, syear,smonth,sday;
    static final int DATE_ID=0;

    Calendar C = Calendar.getInstance();
    int hour = C.get(Calendar.HOUR_OF_DAY);
    int minute = C.get(Calendar.MINUTE);

    List<String> listcat;
    ArrayAdapter<String> adapSpinner;
    private static int posicion;
    private String cate;
    String tipodet;
    String dato;
    String email;
    String uid;
    Double valorLong;
    Double valorLat;

    public final int MY_PERMISSIONS_REQUEST =1;
    private ProgressDialog mProgres;
    private static final int SECACT_REQUEST_CODE = 0;

    //Valor de ubicación desde la opción seleccionar ubicación
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==SECACT_REQUEST_CODE){
            if (resultCode==RESULT_OK){
                valorLat = data.getDoubleExtra("latitud",0);
                valorLong = data.getDoubleExtra("longitud",0);
            }
        }else{
            Toast.makeText(this, "Error al leer DATOS", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_create);


        smonth = C.get(Calendar.MONTH);
        sday = C.get(Calendar.DAY_OF_MONTH);
        syear = C.get(Calendar.YEAR);

        date = (EditText)findViewById(R.id.date);
        time = (EditText)findViewById(R.id.time);
        title =(EditText)findViewById(R.id.title);
        description = (EditText)findViewById(R.id.description);
        tipo =(SwitchCompat) findViewById(R.id.tipo);
        lugar2 = (FloatingActionButton) findViewById(R.id.lugar);
        createEvent = (Button)findViewById(R.id.createEvent);
        categoria = (Spinner)findViewById(R.id.categoria);
        selectPlace = (Switch)findViewById(R.id.selectlugar);

        mProgres = new ProgressDialog(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //getWindow().setLayout((int)(width*.8),(int)(height*.6));


        /*Obtener categoria*/
        listcat = new ArrayList<>();
        final String[] cat={"Artistico","Concierto","Tecnologia","Deporte","Conferencia","Moda","Gastronomia","Otros"};
        Collections.addAll(listcat,cat);
        adapSpinner = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listcat);
        adapSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(adapSpinner);
        String inicializarItem = "Concierto";



        categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                cate = item.toString();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*Obtener id de usuario*/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
             email = user.getEmail();
             uid = user.getUid();
        }


        /*switchTipo*/
        tipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.tipo){
                    if (tipo.isChecked()){
                        tipodet = "Privado";
                    }else{
                    tipodet = "Público";
                }
            }
            }
        });
        /*switchUbicación*/


            lugar2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),ReadUbication.class);
                    startActivityForResult(intent,SECACT_REQUEST_CODE);
                }

            });

        selectPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (selectPlace.isChecked()){
                        LocationManager locationManager = (LocationManager) Popup_create.this.getSystemService(Popup_create.this.LOCATION_SERVICE);
                        if (ActivityCompat.checkSelfPermission(Popup_create.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Popup_create.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Popup_create.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                    MY_PERMISSIONS_REQUEST);
                            return;
                        }
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            builAlertMessageNoGps();
                        } else {
                            mProgres.setMessage("Obteniendo coordenadas...");
                            mProgres.show();
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    valorLat = (location.getLatitude());
                                    valorLong = (location.getLongitude());
                                    Toast.makeText(Popup_create.this, "Éxito al obtener ubicación", Toast.LENGTH_SHORT).show();
                                    if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {
                                        mProgres.dismiss();
                                        try {
                                            Geocoder geocoder = new Geocoder(Popup_create.this, Locale.getDefault());
                                            List<Address> list = geocoder.getFromLocation(
                                                    location.getLatitude(), location.getLongitude(), 1);
                                            if (!list.isEmpty()) {
                                                Address DirCalle = list.get(0);
                                                dato = DirCalle.getAddressLine(0);
                                                Log.i("SDireccion::", dato);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }


                                @Override
                                public void onStatusChanged(String s, int i, Bundle bundle) {
                                }

                                @Override
                                public void onProviderEnabled(String s) {
                                }

                                @Override
                                public void onProviderDisabled(String s) {
                                }
                            });
                        }
                    }

            }
        });


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Popup_create.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearEvento();
                finish();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_ID);
            }
        });



    }

     private void crearEvento() {
        final String title2 = title.getText().toString().trim();
        final String description2 = description.getText().toString().trim();
        final String date2 = date.getText().toString().trim();
        final String time2 = time.getText().toString().trim();

        if (!TextUtils.isEmpty(title2) && !TextUtils.isEmpty(description2) && !TextUtils.isEmpty(date2) && !TextUtils.isEmpty(time2)){


             DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("evento");
             DatabaseReference currentUserDB = mDatabase.child(UUID.randomUUID().toString());
             currentUserDB.child("idevento").setValue(UUID.randomUUID().toString());
             currentUserDB.child("email").setValue(email);
             currentUserDB.child("idusuario").setValue(uid);
             currentUserDB.child("titulo").setValue(title2);
             currentUserDB.child("descripcion").setValue(description2);
             currentUserDB.child("categoria").setValue(cate);
             currentUserDB.child("fecha").setValue(date2);
             currentUserDB.child("hora").setValue(time2);
             currentUserDB.child("tipo").setValue(tipodet);
             currentUserDB.child("ubilat").setValue(valorLat);
             currentUserDB.child("ubilong").setValue(valorLong);

                title.setText("");
                description.setText("");
                date.setText("");
                time.setText("");

                Toast.makeText(Popup_create.this,"Exito al registrar el evento",Toast.LENGTH_SHORT).show();
             }else{
                Toast.makeText(Popup_create.this,"Error al crear el evento :( ...",Toast.LENGTH_SHORT).show();
             }


    }
    private void builAlertMessageNoGps() {
        final AlertDialog.Builder builder= new AlertDialog.Builder(Popup_create.this);
        builder.setMessage("Su Gps esta desabilitado, desea habilitarlo?")
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


    private void colocar_fecha(){
        date.setText(nday+"/"+(nmonth + 1)+"/"+nyear+" ");
    }


    private DatePickerDialog.OnDateSetListener nDatesetlistener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    nyear = year;
                    nmonth = month;
                    nday = dayOfMonth;


                    colocar_fecha();
                }

            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case DATE_ID:
                DatePicker datePicker = new DatePicker(getApplicationContext());
                datePicker.setMaxDate(System.currentTimeMillis());
                return new DatePickerDialog(    this, nDatesetlistener,syear,smonth,sday);
        }
        return null;

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //Mantener los datos del formulario crear evento al regresa desde la actividad ReadUbication

    @Override
    protected void onSaveInstanceState(Bundle outState) {


        String title3 = title.getText().toString();
        String description3 = description.getText().toString();
        String date3 = date.getText().toString();
        String time3 = time.getText().toString();

        outState.putString("title4", title3);
        outState.putString("description4", description3);
        outState.putString("date4", date3);
        outState.putString("time4", time3);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

            String titles = savedInstanceState.getString("title4");
            title.setText(titles);
            String descriptions = savedInstanceState.getString("description4");
            description.setText(descriptions);
            String dates = savedInstanceState.getString("date4");
            date.setText(dates);
            String times = savedInstanceState.getString("time4");
            time.setText(times);

            Toast.makeText(this, "LLegará "+titles, Toast.LENGTH_SHORT).show();
    }


}
