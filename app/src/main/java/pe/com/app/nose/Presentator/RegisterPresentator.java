package pe.com.app.nose.Presentator;

import pe.com.app.nose.Interfaces.RegisterInterface;
import pe.com.app.nose.Model.RegisterModel;

public class RegisterPresentator implements RegisterInterface.Presentator {

    private RegisterInterface.View view;
    private RegisterInterface.Model model;

    public RegisterPresentator(RegisterInterface.View view){
        this.view = view;
        model = new RegisterModel(this);
    }

    @Override
    public void RegistrarUsuarioP(String name,String apellido,String birthday,String correo,String
            pass) {
        if (view!=null){
            model.RegistrarUsuario(name,apellido,birthday,correo,pass);
        }
    }

    @Override
    public void ObtenDatosUserP(String mensaje) {
        if (view!=null){
            view.ObtenDatosUserV(mensaje);
        }
    }

}
