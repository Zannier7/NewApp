package pe.com.app.nose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;




public class PopupDescripcion extends Activity{

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseDatabase mfirebaseDatabase;
    private TextView popup_des_email,popup_des_titulo,popup_des_descripcion;
    private Button popup_des_btn;

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
        popup_des_btn = (Button) findViewById(R.id.popup_des_btn);

        if(bundle !=null){
            String claveone = bundle.getString("CLAVEONE");

        myRef.child(claveone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final Eventodb eventodb = dataSnapshot.getValue(Eventodb.class);
                try {
                    popup_des_email.setText(eventodb.getEmail());
                    popup_des_titulo.setText(eventodb.getTitulo());
                    popup_des_descripcion.setText(eventodb.getDescripcion());

                    popup_des_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double ubitlag = eventodb.getUbilat();
                            double ubitlong= eventodb.getUbilong();
                            intent.putExtra("DIRECCIONLAG",ubitlag);
                            intent.putExtra("DIRECCIONLONG",ubitlong);
                            startActivity(intent);

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
