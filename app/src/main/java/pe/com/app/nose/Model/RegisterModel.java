package pe.com.app.nose.Model;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import pe.com.app.nose.View.Register;

public class RegisterModel {

    //Inicializando variables para el calendario de la fecha de nacimiento
    private int nyear, nmonth,nday, syear,smonth,sday;
    static final int DATE_ID=0;
    Calendar C = Calendar.getInstance();



    //Inicializando variables FIREBASE para el registro...
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FloatingActionButton atrasM;


    private void startRegister(String name, String apellido, String birthday, String correo,
                               String pass) {
        final String nombre = name;
        final String apellidos = apellido;
        final String cumple = birthday;
        final String email = correo;
        final String contraseña = pass;

        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(apellidos) && !TextUtils.isEmpty(cumple)
                && !TextUtils.isEmpty(email)&&!TextUtils.isEmpty(contraseña)){
            mAuth.createUserWithEmailAndPassword(email,contraseña)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("user");
                                DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
                                currentUserDB.child("nombres").setValue(nombre);
                                currentUserDB.child("apellidos").setValue(apellidos);
                                currentUserDB.child("fecha_nacimiento").setValue(cumple);
                                currentUserDB.child("correo").setValue(email);
                                }
                                else {
                                Log.d("Error", "Usuario no registrado");
                            }
                        }
                    });
        }
    }

    private void colocar_fecha(){
        birthday_reg.setText((nmonth + 1)+"/"+nday+"/"+nyear+" ");
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



}
