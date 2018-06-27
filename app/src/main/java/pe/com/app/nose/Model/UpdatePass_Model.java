package pe.com.app.nose.Model;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import pe.com.app.nose.Interfaces.UpdatePass_Interface;
import pe.com.app.nose.View.Update_Pass;

public class UpdatePass_Model implements UpdatePass_Interface.Model {

    UpdatePass_Interface.Presentator presentator;
    //Inicializar variable firebase
    private FirebaseAuth mAuth;

    //Constructor
    public UpdatePass_Model(UpdatePass_Interface.Presentator presentator){
        this.presentator = presentator;
    }



    @Override
    public void UpdatePassM(String correo) {
        mAuth = FirebaseAuth.getInstance();

            String correo2 = correo.toString().trim();

            if (correo2.equals("")){
                presentator.MensajeP("Ingrese el correo con el que se registro...");
            }else{
                mAuth.sendPasswordResetEmail(correo2).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            presentator.MensajeP("Revisa tu correo, te enviamos un enlace para " +
                                    "cambiar tu contraseña ;)");
                        }else {
                            presentator.MensajeP("No podemos procesar el cambio de contraseña, " +
                                    "Ingrese correctamente el correo");
                           }
                    }
                });
            }
    }
}
