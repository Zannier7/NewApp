package pe.com.app.nose;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pe.com.app.nose.Adapters.FavoritosAdapter;
import pe.com.app.nose.Adapters.MyEventsAdapter;
import pe.com.app.nose.Entidad.Eventodb;
import pe.com.app.nose.Entidad.Favoritodb;
import pe.com.app.nose.Entidad.FirebaseReferences;


public class Eventsavailable_fragment extends Fragment {
    ArrayList<Eventodb> ListEvent;
    RecyclerView recyclerView;
    FavoritosAdapter favoritosAdapter;
    private int idposi;
    private FirebaseAuth mAuth;
    private ImageView categorias;
    private String idevento;
    private FirebaseDatabase mfirebaseDatabase;
    public Eventsavailable_fragment(){
        //CONSTRUCTOR
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.eventsavailable_fragment, container, false);

        categorias = (ImageView)view.findViewById(R.id.categoria_img_fav);
        recyclerView= (RecyclerView) view.findViewById(R.id.favoritosrecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

        ListEvent = new ArrayList<>();

        favoritosAdapter = new FavoritosAdapter(getContext(),ListEvent);
        recyclerView.setAdapter(favoritosAdapter);

        mAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String email2= user.getEmail();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference myRefFavorito = mfirebaseDatabase.getReference(FirebaseReferences.FAVORITO_REFERENCE);
        myRefFavorito.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (final DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    final Favoritodb favoritodb = datasnapshot.getValue(Favoritodb.class);
                    final String keyusuario = favoritodb.getIdusuario();
                    final String idevento = favoritodb.getIdevento();

                    final DatabaseReference myRefEvent =  database.getReference(FirebaseReferences.EVENTO_REFERENCE);
                    myRefEvent.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String idusuario = null;

                            for (final DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                                final String llave = datasnapshot.getKey();

                                final Eventodb eventodb = datasnapshot.getValue(Eventodb.class);


                                if (llave.equals(idevento) ){
                                    ListEvent.add(eventodb);
                                }

                            }

                            favoritosAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }

}
