package pe.com.app.nose.View;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import pe.com.app.nose.Interfaces.RegisterInterface;
import pe.com.app.nose.MainActivity;
import pe.com.app.nose.Presentator.RegisterPresentator;
import pe.com.app.nose.R;

public class Register extends AppCompatActivity implements RegisterInterface.View{

    //variables presentator
    private RegisterInterface.Presentator presentator;

    //Variables de la vista Registro
    private EditText name_reg;
    private EditText apelli_reg;
    private EditText birthday_reg;
    private EditText corre_reg;
    private EditText pass_reg;
    private Button buton_reg;
    private FloatingActionButton atrasM;

    //variables para el calendario
    Calendar C = Calendar.getInstance();
    private int nyear, nmonth,nday, syear,smonth,sday;
    static final int DATE_ID=0;

    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /*Inicializando variables del Calendario*/
        smonth = C.get(Calendar.MONTH);
        sday = C.get(Calendar.DAY_OF_MONTH);
        syear = C.get(Calendar.YEAR);

        //Inicializando variables del Registro más el progressDialog
        name_reg = (EditText)findViewById(R.id.name_reg);
        apelli_reg = (EditText)findViewById(R.id.apellidos_reg);
        birthday_reg = (EditText)findViewById(R.id.birthday_reg);
        corre_reg = (EditText)findViewById(R.id.correo_reg);
        pass_reg = (EditText)findViewById(R.id.pass_reg);
        buton_reg = (Button)findViewById(R.id.button_reg);
        atrasM = (FloatingActionButton) findViewById(R.id.atrasM);
        mProgress = new ProgressDialog(this);

        //inicializando presentador
        presentator = new RegisterPresentator(this);

        //mostrar calendario al pulsar sobre el EditText birthday_reg
        birthday_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_ID);
            }
        });

        //Boton para regresar al MainActivity
        atrasM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buton_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = name_reg.getText().toString().trim();
                final String apellido = apelli_reg.getText().toString().trim();
                final String birthday = birthday_reg.getText().toString().trim();
                final String correo = corre_reg.getText().toString().trim();
                String pass =pass_reg.getText().toString().trim();

                presentator.RegistrarUsuarioP(name,apellido,birthday,correo,pass);
            }
        });



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

    @Override
    public void ObtenDatosUserV(String mensaje) {
        Toast.makeText(this, mensaje , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
