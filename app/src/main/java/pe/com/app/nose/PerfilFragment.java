package pe.com.app.nose;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;


public class PerfilFragment extends Fragment {

    @Nullable

    private FloatingActionButton pro_profile_edit;
    private TextView nameTextView;
    private TextView emailTextView;
    private ImageView photoImageView;
   // private TextView phoneTextView;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private Button cerrarSesion;
    private TextView pro_descripcion;
    private String userID;
    private FirebaseDatabase mfirebaseDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        final View view=inflater.inflate(R.layout.fragment_perfil,container,false);


        firebaseAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mfirebaseDatabase.getReference(FirebaseReferences.USUARIO_REFERENCE);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();


        photoImageView = (ImageView) view.findViewById(R.id.photoImageView);
     //   phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        pro_descripcion = (TextView)view.findViewById(R.id.pro_descripcion);

        pro_profile_edit = (FloatingActionButton) view.findViewById(R.id.pro_profile_edit);
        pro_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PopupPerfil.class);
                startActivity(intent);
            }
        });



        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuariodb usuariodb = dataSnapshot.getValue(Usuariodb.class);

                pro_descripcion.setText(usuariodb.getFecha_nacimiento());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        cerrarSesion = (Button) view.findViewById(R.id.cerrarSesion);
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.cerrarSesion:
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(getActivity(),
                                MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                }


            }
        });


        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!= null){
                    setUserData(user);
                }else{
                    goLogInScreen();
                }
            }
        };


        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            String phone = user.getPhoneNumber();

            nameTextView.setText(name);
            emailTextView.setText(email);
           // phoneTextView.setText(phone);


        } else {

            goLogInScreen();
        }

        return view;


    }


    private void setUserData(FirebaseUser user) {
        nameTextView.setText(user.getDisplayName());
        emailTextView.setText(user.getEmail());
    //    phoneTextView.setText(user.getPhoneNumber());


        Glide.with(this).load(user.getPhotoUrl()).into(photoImageView);
    }


    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }






    private void goLogInScreen() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    @Override
    public void onStop(){
        super.onStop();

        if (firebaseAuthListener != null){
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);

        }
    }



}
