package pe.com.app.nose;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Popup_create extends Activity {

    private EditText date;
    private EditText time;
    private EditText title;
    private EditText description;
    private SwitchCompat tipo;
    private Switch lugar;
    private Button createEvent;

    private ProgressDialog mProgress;

    private int nyear, nmonth,nday, syear,smonth,sday;
    static final int DATE_ID=0;
    Calendar C = Calendar.getInstance();
    int hour = C.get(Calendar.HOUR_OF_DAY);
    int minute = C.get(Calendar.MINUTE);

    private FirebaseAuth mAuth;

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
        lugar = (Switch)findViewById(R.id.lugar);
        createEvent = (Button)findViewById(R.id.createEvent);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

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
             DatabaseReference currentUserDB = mDatabase.child("Evento2");
             currentUserDB.child("titulo").setValue(title2);
             currentUserDB.child("descripcion").setValue(description2);
             currentUserDB.child("fecha").setValue(date2);
             currentUserDB.child("hora").setValue(time2);

                Toast.makeText(Popup_create.this,"Exito al registrar",Toast.LENGTH_SHORT).show();
             }else{
                Toast.makeText(Popup_create.this,"Error al unirte :( ...",Toast.LENGTH_SHORT).show();
             }


    }


    private void colocar_fecha(){
        date.setText((nmonth + 1)+"/"+nday+"/"+nyear+" ");
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
                return new DatePickerDialog(this, nDatesetlistener,syear,smonth,sday);
        }
        return null;

    }
}
