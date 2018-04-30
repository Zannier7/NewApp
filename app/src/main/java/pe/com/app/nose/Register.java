package pe.com.app.nose;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Register extends AppCompatActivity {

    private EditText name_reg;
    private EditText apelli_reg;
    private EditText birthday_reg;
    private EditText corre_reg;
    private EditText pass_reg;
    private Button buton_reg;
    private int nyear, nmonth,nday, syear,smonth,sday;
    static final int DATE_ID=0;
    Calendar C = Calendar.getInstance();

    private ProgressDialog mProgress;

    /*FirebaseAuth*/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FloatingActionButton atrasM;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        /*Calendario*/
        smonth = C.get(Calendar.MONTH);
        sday = C.get(Calendar.DAY_OF_MONTH);
        syear = C.get(Calendar.YEAR);

        /*Registro*/
        name_reg = (EditText)findViewById(R.id.name_reg);
        apelli_reg = (EditText)findViewById(R.id.apellidos_reg);
        birthday_reg = (EditText)findViewById(R.id.birthday_reg);
        corre_reg = (EditText)findViewById(R.id.correo_reg);
        pass_reg = (EditText)findViewById(R.id.pass_reg);
        buton_reg = (Button)findViewById(R.id.button_reg);
        atrasM = (FloatingActionButton) findViewById(R.id.atrasM);


        birthday_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_ID);
            }
        });
        atrasM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        /*Firebase*/

        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);

        buton_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() !=null){
                    Intent intent = new Intent(Register.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

    }

    private void startRegister() {
        final String name = name_reg.getText().toString().trim();
        final String apellido = apelli_reg.getText().toString().trim();
        final String birthday = birthday_reg.getText().toString().trim();
        final String correo = corre_reg.getText().toString().trim();
        String pass =pass_reg.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(apellido) && !TextUtils.isEmpty(birthday) && !TextUtils.isEmpty(correo)&&!TextUtils.isEmpty(pass)){
            mProgress.setMessage("Uniéndote a nuestra familia...");
            mProgress.show();
            mAuth.createUserWithEmailAndPassword(correo,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgress.dismiss();
                            if (task.isSuccessful()){

                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("user");
                                DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
                                currentUserDB.child("nombres").setValue(name);
                                currentUserDB.child("apellidos").setValue(apellido);
                                currentUserDB.child("fecha_nacimiento").setValue(birthday);
                                currentUserDB.child("correo").setValue(correo);

                                Toast.makeText(Register.this,"Exito al registrar",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Register.this,"Error al unirte :( ...",Toast.LENGTH_SHORT).show();
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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case DATE_ID:
            return new DatePickerDialog(this, nDatesetlistener,syear,smonth,sday);
        }
        return null;

    }
}
