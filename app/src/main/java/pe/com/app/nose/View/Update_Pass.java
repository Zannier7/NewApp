package pe.com.app.nose.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pe.com.app.nose.Interfaces.UpdatePass_Interface;
import pe.com.app.nose.MainActivity;
import pe.com.app.nose.Presentator.UpdatePass_Presentator;
import pe.com.app.nose.R;

public class Update_Pass extends AppCompatActivity implements UpdatePass_Interface.View{


    private UpdatePass_Interface.Presentator presentator;

    private Button cambiar_pass;
    private EditText email;
    private FloatingActionButton atrasM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepass);


        cambiar_pass = (Button) findViewById(R.id.cambiar_pass);
        atrasM = (FloatingActionButton)findViewById(R.id.atrasM);
        email = (EditText)findViewById(R.id.correo2);

        presentator = new UpdatePass_Presentator(this);


        atrasM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Update_Pass.this, MainActivity.class);
                startActivity(intent);
            }
        });

        cambiar_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String correo = email.getText().toString().trim();

                Log.e("eror",correo);

                presentator.UpdatePassP(correo);
            }
        });
    }


    @Override
    public void MensajeV(String mensaje) {
        Toast.makeText(this, mensaje , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
