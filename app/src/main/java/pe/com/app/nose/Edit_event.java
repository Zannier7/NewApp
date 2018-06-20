package pe.com.app.nose;

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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import pe.com.app.nose.Entidad.Eventodb;
import pe.com.app.nose.Entidad.FirebaseReferences;

public class Edit_event extends AppCompatActivity {

    private EditText title_e;
    private EditText description_e;
    private Spinner categoria_e;
    private EditText date_e;
    private EditText time_e;
    private FloatingActionButton lugar_e;
    private Switch myubi_e;
    private Button edit_evento;
    private String cate;

    private FirebaseDatabase firebaseDatabase;

    private static final int SECACT_REQUEST_CODE = 0;
    public final int MY_PERMISSIONS_REQUEST =1;
    static final int DATE_ID=0;
    private ProgressDialog mProgres;

    //calendar
    private int nyear, nmonth,nday, syear,smonth,sday;
    Calendar C = Calendar.getInstance();
    int hour = C.get(Calendar.HOUR_OF_DAY);
    int minute = C.get(Calendar.MINUTE);

    //Spinner categoria
    List<String> listcat;
    ArrayAdapter<String> adapSpinner;

    //Variables glogables para obtener mi ubicación
    Double valorLong;
    Double valorLat;
    String dato;

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
        setContentView(R.layout.activity_edit_event);
        //Calendar
        smonth = C.get(Calendar.MONTH);
        sday = C.get(Calendar.DAY_OF_MONTH);
        syear = C.get(Calendar.YEAR);

        title_e = (EditText) findViewById(R.id.title_e);
        description_e = (EditText) findViewById(R.id.description_e);
        categoria_e = (Spinner) findViewById(R.id.categoria_e);
        date_e = (EditText) findViewById(R.id.date_e);
        time_e= (EditText) findViewById(R.id.time_e);
        lugar_e = (FloatingActionButton) findViewById(R.id.lugar_e);
        myubi_e = (Switch) findViewById(R.id.myubi_e);
        mProgres = new ProgressDialog(this);
        edit_evento = (Button)findViewById(R.id.edit_evento);

        //cargar DateDialog
        time_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Edit_event.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time_e.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        date_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_ID);
            }
        });

        /*Obtener categoria*/
        listcat = new ArrayList<>();
        final String[] cat={"Artistico","Concierto","Tecnologia","Deporte","Conferencia","Moda","Gastronomia","Otros"};
        Collections.addAll(listcat,cat);
        adapSpinner = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listcat);
        adapSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria_e.setAdapter(adapSpinner);
        String inicializarItem = "Concierto";

        categoria_e.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                cate = item.toString();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Obteniendo idevento
        Bundle parametros = this.getIntent().getExtras();
        final String llave = parametros.getString("idevento");

        //cargar datos desde FB
        firebaseDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference myRefedit = firebaseDatabase.getReference(FirebaseReferences.EVENTO_REFERENCE);

        edit_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference updatepro = myRefedit.child(llave);

                final String title_u = title_e.getText().toString().trim();
                final String description_u = description_e.getText().toString().trim();
                final String date_u = date_e.getText().toString().trim();
                final String time_u = time_e.getText().toString().trim();

                if (updatepro.child("correo") != null){

                    updatepro.child("titulo").setValue(title_u);
                    updatepro.child("descripcion").setValue(description_u);
                    updatepro.child("hora").setValue(time_u);
                    updatepro.child("fecha").setValue(date_u);
                    updatepro.child("categoria").setValue(cate);

                    Toast.makeText(Edit_event.this,"Exito al Actualizar",Toast.LENGTH_SHORT).show();
                }
                else {
                    updatepro.setValue(llave);
                    updatepro.child("titulo").setValue(title_u);
                    updatepro.child("descripcion").setValue(description_u);
                    updatepro.child("hora").setValue(time_u);
                    updatepro.child("fecha").setValue(date_u);
                    updatepro.child("categoria").setValue(cat);
                    Toast.makeText(Edit_event.this,"Exito al Actualizar",Toast.LENGTH_SHORT)
                            .show();

                }
                finish();
            }
        });

        myRefedit.child(llave).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                        try {
                            Eventodb eventodb = dataSnapshot.getValue(Eventodb.class);

                            title_e.setText(eventodb.getTitulo());
                            description_e.setText(eventodb.getDescripcion());
                            date_e.setText(eventodb.getFecha());
                            time_e.setText(eventodb.getHora());

                        }
                        catch ( Exception e){

                            title_e.setText("");
                            description_e.setText("");
                            date_e.setText("");
                            time_e.setText("");

                        }
                    }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Evento desde otra ubicación
        lugar_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ReadUbication.class);
                startActivityForResult(intent,SECACT_REQUEST_CODE);
            }

        });
        //Evento desde mi Ubicación
        myubi_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myubi_e.isChecked()){
                    LocationManager locationManager = (LocationManager) Edit_event.this.getSystemService
                            (Edit_event.this.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(Edit_event.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Edit_event.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Edit_event.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
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
                                Toast.makeText(Edit_event.this, "Éxito al obtener ubicación", Toast.LENGTH_SHORT).show();
                                if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {
                                    mProgres.dismiss();
                                    try {
                                        Geocoder geocoder = new Geocoder(Edit_event.this, Locale.getDefault());
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



    }

    private void colocar_fecha(){
        date_e.setText(nday+"/"+(nmonth + 1)+"/"+nyear+" ");
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



    private void builAlertMessageNoGps() {
        final AlertDialog.Builder builder= new AlertDialog.Builder(Edit_event.this);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {


        String title3 = title_e.getText().toString();
        String description3 = description_e.getText().toString();
        String date3 = date_e.getText().toString();
        String time3 = time_e.getText().toString();

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
        title_e.setText(titles);
        String descriptions = savedInstanceState.getString("description4");
        description_e.setText(descriptions);
        String dates = savedInstanceState.getString("date4");
        date_e.setText(dates);
        String times = savedInstanceState.getString("time4");
        time_e.setText(times);

        Toast.makeText(this, "LLegará "+titles, Toast.LENGTH_SHORT).show();
    }

}
