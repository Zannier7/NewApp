package pe.com.app.nose;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pe.com.app.nose.Adapters.MyEventsAdapter;


public class Myevent_fragment extends Fragment {

    ArrayList<Eventodb> ListEvent;
    RecyclerView recyclerView;
    MyEventsAdapter myEventsAdapter;
    private int idposi;
    private FirebaseAuth mAuth;
    private ImageView categorias;

    public Myevent_fragment(){
        //CONSTRUCTOR
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.myevent_fragment, container, false);

        categorias = (ImageView)view.findViewById(R.id.categoria_img);
        recyclerView= (RecyclerView) view.findViewById(R.id.admin_event);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

        ListEvent = new ArrayList<>();

        myEventsAdapter = new MyEventsAdapter(getContext(),ListEvent);
        recyclerView.setAdapter(myEventsAdapter);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String email2= user.getEmail();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("evento").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = null;
                for (final DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    final Eventodb eventodb = datasnapshot.getValue(Eventodb.class);
                    email = eventodb.getEmail();
                    String categoria = eventodb.getCategoria();

                     if (email.equals(email2)) {
                         /*switch (categoria) {

                             case "Artistico":

                                 categorias.setBackgroundResource(R.drawable.artistico2);
                                 break;
                             case "Concierto":
                                 categorias.setBackgroundResource(R.drawable.concert);
                                 break;
                             case "Tecnologia":
                                 categorias.setBackgroundResource(R.drawable.tecnologia2);
                                 break;
                             case "Deporte":
                                 categorias.setBackgroundResource(R.drawable.deportes2);
                                 break;
                             case "Conferencia":
                                 categorias.setBackgroundResource(R.drawable.conferencia2);
                                 break;
                             case "Moda":
                                 categorias.setBackgroundResource(R.drawable.moda2);
                                 break;
                             case "Gastronomia":
                                 categorias.setBackgroundResource(R.drawable.gastronomia2);
                                 break;
                             case "Otros":
                                 categorias.setBackgroundResource(R.drawable.otros2);
                                 break;
                         }*/
                         ListEvent.add(eventodb);
                     }

                }
                myEventsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myEventsAdapter.setOnItemClickListener(new MyEventsAdapter.OnItemClickListener() {
            @Override
            public void onEditEvent(int position) {
                idposi = position;
                /*int id = L.get(idposi).getIdcontact();
                String na = contactlist.get(idposi).getName();
                Log.d("no", "porque " + na);*/
                Intent intent = new Intent(getActivity(), Edit_event.class);
                intent.putExtra("idcontacto", "1212");
                startActivity(intent);
            }

            @Override
            public void onDeleteEvent(int position) {
                idposi = position;
                /*int id = L.get(idposi).getIdcontact();
                String na = contactlist.get(idposi).getName();
                Log.d("no", "porque " + na);*/
                Intent intent = new Intent(getActivity(), Delete_event.class);
                intent.putExtra("idcontacto", "1212");
                startActivity(intent);
            }
        });

    return view;
    }


}
