package pe.com.app.nose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import pe.com.app.nose.Entidad.Eventodb;
import pe.com.app.nose.Entidad.FirebaseReferences;


public class PopupDescripcion extends Activity{

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseDatabase mfirebaseDatabase;
    private TextView popup_des_email,popup_des_titulo,popup_des_descripcion,popup_des_categoria;
    private Button popup_des_btn;
    private TextView hora;
    private TextView fecha;
    private Button interesa;
    private String userID;
    int click = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_descripcion);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));
        firebaseAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mfirebaseDatabase.getReference(FirebaseReferences.EVENTO_REFERENCE);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        popup_des_email = (TextView) findViewById(R.id.popup_des_email);
        popup_des_titulo = (TextView) findViewById(R.id.popup_des_titulo);
        popup_des_descripcion = (TextView) findViewById(R.id.popup_des_descripcion);
        popup_des_categoria = (TextView) findViewById(R.id.popup_des_categoria);
        popup_des_btn = (Button) findViewById(R.id.popup_des_btn);
        fecha =(TextView) findViewById(R.id.fecha);
        hora =(TextView)        findViewById(R.id.hora);
        interesa =(Button)        findViewById(R.id.interesa);
        userID = user.getUid();


        if(bundle !=null){
           final String claveone = bundle.getString("CLAVEONE");

        myRef.child(claveone).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final Eventodb eventodb = dataSnapshot.getValue(Eventodb.class);
                try {
                    popup_des_email.setText(eventodb.getEmail());
                    popup_des_titulo.setText(eventodb.getTitulo());
                    popup_des_categoria.setText(eventodb.getCategoria());
                    fecha.setText(eventodb.getFecha());
                    hora.setText(eventodb.getHora());
                    popup_des_descripcion.setText(eventodb.getDescripcion());

                    popup_des_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double ubitlag = eventodb.getUbilat();
                            double ubitlong = eventodb.getUbilong();
                            Intent intent = new Intent(PopupDescripcion.this,HomeFragment.class);
                            intent.putExtra("DIRECCIONLAG",ubitlag);
                            intent.putExtra("DIRECCIONLONG",ubitlong);

                            setResult(PopupDescripcion.RESULT_OK,intent);
                            finish();


                        }
                    });


                    interesa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            interesa.setBackgroundResource(R.drawable.intresa_icon);

                            click++;
                            Handler handler = new Handler();
                            Runnable r = new Runnable() {

                                @Override
                                public void run() {
                                    click = 0;
                                }
                            };

                            if (click == 1) {
                                interesa.setBackgroundResource(R.drawable.intresa_icon);
                                DatabaseReference anadirfavref = mfirebaseDatabase.getReference(FirebaseReferences.FAVORITO_REFERENCE);
                                DatabaseReference currentUserDB = anadirfavref.child(UUID.randomUUID().toString());
                                currentUserDB.child("idevento").setValue(claveone);
                                currentUserDB.child("idusuario").setValue(userID);

                                Toast.makeText(PopupDescripcion.this, "haber" + claveone, Toast.LENGTH_SHORT).show();

                                // handler.postDelayed(r, 250);
                            } else if (click == 2) {
                                interesa.setBackgroundResource(R.drawable.interesa_animation);

                                click = 0;

                            }


                        }
                    });



                } catch (Exception e) {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




    }



}
