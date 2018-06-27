package pe.com.app.nose.Model;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pe.com.app.nose.Eventsavailable_fragment;
import pe.com.app.nose.Myevent_fragment;
import pe.com.app.nose.R;


public class EventosFragment extends Fragment {


    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_eventos, container, false);



        BottomNavigationView  menuView =  view.findViewById(R.id.menuView2);
        menuView.setOnNavigationItemSelectedListener(navListener);
        return view;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    android.support.v4.app.Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.my_events:
                            selectedFragment  = new Myevent_fragment();
                            TextView txt = getView().findViewById(R.id.textC);
                            txt.setText("");
                            break;
                        case R.id.another_events:
                            selectedFragment = new Eventsavailable_fragment();
                            TextView txt2 = getView().findViewById(R.id.textC);
                            txt2.setText("");
                            break;

                    }
                    getChildFragmentManager().beginTransaction().replace(R.id.fragmento_contenedor2,selectedFragment).commit();
                    return true;

                }
            };

}
