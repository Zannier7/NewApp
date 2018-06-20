package pe.com.app.nose;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pe.com.app.nose.Entidad.FirebaseReferences;
import pe.com.app.nose.Entidad.Usuariodb;

public class PopupPerfil extends Activity {

    private EditText popup_nombre, popup_apellido, popup_numero, popup_fechanac, popup_descripcion;
    private TextView popup_correo;
    private Button popup_updatetbn;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseDatabase mfirebaseDatabase;

    private String userID;
    private String emailfire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_profile);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));




        popup_nombre = (EditText) findViewById(R.id.popup_nombre);
        popup_apellido = (EditText) findViewById(R.id.popup_apellido);
        popup_numero = (EditText) findViewById(R.id.popup_numero);
        popup_fechanac = (EditText) findViewById(R.id.popup_fechanac);
        popup_descripcion = (EditText) findViewById(R.id.popup_descripcion);
        popup_correo = (TextView) findViewById(R.id.popup_correo);
        popup_updatetbn = (Button) findViewById(R.id.popup_updatetbn);


        firebaseAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference myRefpopup = mfirebaseDatabase.getReference(FirebaseReferences.USUARIO_REFERENCE);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();
        emailfire = user.getEmail();
        popup_correo.setText(emailfire);


        popup_updatetbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference updatepro = myRefpopup.child(userID);

                final String nombre_s = popup_nombre.getText().toString().trim();
                final String apellido_s = popup_apellido.getText().toString().trim();
                final String popup_numeros_s = popup_numero.getText().toString().trim();
                final String popup_fechanac_s = popup_fechanac.getText().toString().trim();
                final String popup_descripcion_s = popup_descripcion.getText().toString().trim();

                if (updatepro.child("correo") != null){

                    updatepro.child("nombres").setValue(nombre_s);
                    updatepro.child("apellidos").setValue(apellido_s);
                    updatepro.child("numero").setValue(popup_numeros_s);
                    updatepro.child("fecha_nacimiento").setValue(popup_fechanac_s);
                    updatepro.child("descripcion").setValue(popup_descripcion_s);
                    updatepro.child("uid").setValue(userID);
                    updatepro.child("correo").setValue(emailfire);

                    Toast.makeText(PopupPerfil.this,"Exito al Actualizar",Toast.LENGTH_SHORT).show();
                }


                else {
                    updatepro.setValue(userID);
                    updatepro.child("nombres").setValue(nombre_s);
                    updatepro.child("apellidos").setValue(apellido_s);
                    updatepro.child("numero").setValue(popup_numeros_s);
                    updatepro.child("fecha_nacimiento").setValue(popup_fechanac_s);
                    updatepro.child("descripcion").setValue(popup_descripcion_s);
                    updatepro.child("uid").setValue(userID);
                    updatepro.child("correo").setValue(emailfire);
                    Toast.makeText(PopupPerfil.this,"Exito al Actualizar",Toast.LENGTH_SHORT).show();

                }

                finish();





            }
        });



        myRefpopup.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    Usuariodb usuariodb = dataSnapshot.getValue(Usuariodb.class);

                    popup_nombre.setText(usuariodb.getNombres());
                    popup_apellido.setText(usuariodb.getApellidos());
                    popup_numero.setText(usuariodb.getNumero());
                    popup_fechanac.setText(usuariodb.getFecha_nacimiento());
                    popup_descripcion.setText(usuariodb.getDescripcion());
                }

                catch ( Exception e){
                    popup_nombre.setText("");
                    popup_apellido.setText("");
                    popup_numero.setText("");
                    popup_fechanac.setText("");
                    popup_descripcion.setText("");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }






}