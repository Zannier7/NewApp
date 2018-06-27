package pe.com.app.nose.Model;


import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pe.com.app.nose.Interfaces.RegisterInterface;

public class RegisterModel implements RegisterInterface.Model{

    private RegisterInterface.Presentator presentator;
    private FirebaseAuth mAuth;

    public RegisterModel(RegisterInterface.Presentator presentator) {
        this.presentator=presentator;
    }

    @Override
    public void RegistrarUsuario(final String name, final String apellido, final String birthday, final String correo, String
            pass) {

        mAuth = FirebaseAuth.getInstance();

        final String name2 = name;
        final String apellido2 = apellido;
        final String birthday2 = birthday;
        final String correo2 = correo;
        String pass2 = pass;


        if (!TextUtils.isEmpty(name2) && !TextUtils.isEmpty(apellido2) && !TextUtils.isEmpty
                (birthday2) && !TextUtils.isEmpty(correo2)&&!TextUtils.isEmpty(pass2)){


            mAuth.createUserWithEmailAndPassword(correo2,pass2)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){

                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("user");
                                    DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
                                    currentUserDB.child("nombres").setValue(name2);
                                    currentUserDB.child("apellidos").setValue(apellido2);
                                    currentUserDB.child("fecha_nacimiento").setValue(birthday2);
                                    currentUserDB.child("correo").setValue(correo2);

                                    Log.d("error","error"+name2);
                                    presentator.ObtenDatosUserP("Usuario Registrado ;)");

                                }else {
                                    //verificando si el usuario ya existe
                                    if (task.getException() instanceof
                                            FirebaseAuthUserCollisionException){
                                        presentator.ObtenDatosUserP("El correo ya está en uso :)");
                                    }else {
                                        Log.w("Error", "signInWithEmail:failure", task
                                                .getException
                                                ());
                                        presentator.ObtenDatosUserP("Registro fallido :(");
                                    }
                                }
                        }
                    });
        }
    }
}
