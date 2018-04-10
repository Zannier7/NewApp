package pe.com.app.nose;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Update_Pass extends AppCompatActivity {

    private EditText pass1;
    private Button cambiar_pass;
    private EditText correo;
    private FloatingActionButton atrasM;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepass);

        pass1 = (EditText)findViewById(R.id.pass1);
        cambiar_pass = (Button) findViewById(R.id.cambiar_pass);
        atrasM = (FloatingActionButton)findViewById(R.id.atrasM);
        correo = (EditText)findViewById(R.id.correo);
        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        pass1.setVisibility(View.GONE);

        atrasM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Update_Pass.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        cambiar_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarPassword();
            }

            private void cambiarPassword() {
                final String correo2=correo.getText().toString().trim();
                final String password1 = pass1.getText().toString().trim();


                if (correo2.equals("")){
                    Toast.makeText(Update_Pass.this, "Ingrese su correo de la aplicacion", Toast.LENGTH_SHORT).show();
                }else{

                        mProgress.setMessage("Cambiando Contraseña...");
                        mProgress.show();
                        mAuth.sendPasswordResetEmail(correo2).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mProgress.dismiss();
                                if (task.isSuccessful()){

                                    firebaseUser.updatePassword(password1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(Update_Pass.this, "Revisa tu correo ;),Te enviamos un mensaje", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }else {
                                                Toast.makeText(Update_Pass.this, "No podemos procesar el cambio de contraseña, ingrese correctamente el correo", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }

                }

        });





    }


}
