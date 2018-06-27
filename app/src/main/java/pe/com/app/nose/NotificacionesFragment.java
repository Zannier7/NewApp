package pe.com.app.nose;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pe.com.app.nose.Adapters.NotificationAdapter;
import pe.com.app.nose.Entidad.Eventodb;


public class NotificacionesFragment extends Fragment {

    ArrayList<Eventodb> ListEvent;
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    private FirebaseAuth mAuth;

    public NotificacionesFragment(){
        //CONSTRUCTOR
    }

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_notificaciones, container, false);

        recyclerView= (RecyclerView) view.findViewById(R.id.notificaciones);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

        ListEvent = new ArrayList<>();

        notificationAdapter = new NotificationAdapter(getContext(),ListEvent);
        recyclerView.setAdapter(notificationAdapter);

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

                    if (email==null) {
                    }else if(email.equals(email2)) {

                        }else{
                        ListEvent.add(eventodb);
                    }

                }
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
