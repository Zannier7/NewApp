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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
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

import static android.app.Activity.RESULT_OK;


public class PerfilFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener{

    @Nullable

    private FloatingActionButton pro_profile_edit;
    private TextView nameTextView;
    private TextView emailTextView;
    private ImageView photoImageView;
    private TextView phoneTextView;
    private TextView cumpleTextView;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private Button cerrarSesion;
    private TextView pro_descripcion;
    private String userID;
    private FirebaseDatabase mfirebaseDatabase;
    private FrameLayout fl;

    //PSF NEED FOR INVITE
    private static final String TAG = PerfilFragment.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;

    @Override
    public void onPause() {
        super.onPause();

        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        final View view=inflater.inflate(R.layout.fragment_perfil,container,false);


        firebaseAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mfirebaseDatabase.getReference(FirebaseReferences.USUARIO_REFERENCE);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();


        //BOTON INVITE
        view.findViewById(R.id.invite_button).setOnClickListener(this);

        photoImageView = (ImageView) view.findViewById(R.id.photoImageView);
        cumpleTextView = (TextView) view.findViewById(R.id.cumpleTextView);
        phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        pro_descripcion = (TextView)view.findViewById(R.id.pro_descripcion);

        fl = (FrameLayout) view.findViewById( R.id.mainmenu);

       fl.getForeground().setAlpha(150);




        pro_profile_edit = (FloatingActionButton) view.findViewById(R.id.pro_profile_edit);
        pro_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PopupPerfil.class);
                startActivity(intent);
            }
        });


        //Google Invite inicializando plug
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(AppInvite.API)
                .enableAutoManage(getActivity(),this).build();
        boolean autoLaunchDeepLink = true;
        AppInvite.AppInviteApi.getInvitation(googleApiClient, getActivity(), autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {
                                Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
                                if (result.getStatus().isSuccess()) {
                                    Intent intent = result.getInvitationIntent();
                                    String deepLink = AppInviteReferral.getDeepLink(intent);
                                    String invitationId = AppInviteReferral.getInvitationId(intent);
                                }
                            }
                        });

        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Usuariodb usuariodb = dataSnapshot.getValue(Usuariodb.class);
                try {


                    nameTextView.setText(usuariodb.getNombres()+" " +usuariodb.getApellidos());
                    phoneTextView.setText(usuariodb.getNumero());
                    cumpleTextView.setText(usuariodb.getFecha_nacimiento());
                    pro_descripcion.setText(usuariodb.getDescripcion());
                }

                catch ( Exception e){
                    nameTextView.setText("No hay datos");
                    phoneTextView.setText("No hay datos");
                    cumpleTextView.setText("No hay datos");
                    pro_descripcion.setText("No hay datos");
                }

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

            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();


            emailTextView.setText(email);
           // phoneTextView.setText(phone);


        } else {

            goLogInScreen();
        }

        return view;


    }


    private void setUserData(FirebaseUser user) {
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
    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // [START_EXCLUDE]
                showMessage(getString(R.string.send_failed));
                // [END_EXCLUDE]
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.invite_button:
                onInviteClicked();
                break;
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        showMessage(getString(R.string.google_play_services_error));
    }

    private void showMessage(String msg) {
        ViewGroup container = (ViewGroup) getView().findViewById(R.id.snackbar_layout);
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();
    }
}
