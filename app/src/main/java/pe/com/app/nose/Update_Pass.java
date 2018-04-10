package pe.com.app.nose;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Update_Pass extends AppCompatActivity {

    private EditText pass1;
    private EditText pass2;
    private Button cambiar_pass;
    private FloatingActionButton atrasM;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepass);

        pass1 = (EditText)findViewById(R.id.pass1);
        pass2 = (EditText)findViewById(R.id.pass2);
        cambiar_pass = (Button) findViewById(R.id.cambiar_pass);
        atrasM = (FloatingActionButton)findViewById(R.id.atrasM);
        mProgress = new ProgressDialog(this);

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
                final String password1 = pass1.getText().toString().trim();
                final String password2 = pass2.getText().toString().trim();

                if (password1 != password2){
                    mProgress.setMessage("Comprobando si coinciden las contraseñas...");
                    mProgress.show();
                    Toast.makeText(Update_Pass.this,"No coinciden, ingrese nuevamente", Toast.LENGTH_LONG).show();
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() !=null){
                    Intent intent = new Intent(Update_Pass.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };



    }


}
