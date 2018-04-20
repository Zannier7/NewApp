package pe.com.app.nose;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

public class InicioActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BottomNavigationView  menuView =  findViewById(R.id.menuView);
        menuView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmento_contenedor,new HomeFragment()).commit();
    }
       private BottomNavigationView.OnNavigationItemSelectedListener navListener =
               new BottomNavigationView.OnNavigationItemSelectedListener() {
                   @Override
                   public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                       android.support.v4.app.Fragment selectedFragment = null;
                       switch (item.getItemId()){
                           case R.id.menu_home:
                               selectedFragment  = new HomeFragment();
                               break;
                           case R.id.menu_perfil:
                               selectedFragment = new PerfilFragment();
                               break;
                           case R.id.menu_notificaciones:
                               selectedFragment = new NotificacionesFragment();
                               break;
                           case R.id.menu_evento:
                               selectedFragment = new EventosFragment();
                               break;
                           case R.id.menu_configuracion:
                               selectedFragment = new ConfiguracionFragment();
                               break;
                       }
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmento_contenedor,selectedFragment).commit();
                            return true;

                   }
               };
}
