package pe.com.app.nose.Presentator;

import pe.com.app.nose.Interfaces.UpdatePass_Interface;
import pe.com.app.nose.Model.UpdatePass_Model;

public class UpdatePass_Presentator implements UpdatePass_Interface.Presentator {

    private UpdatePass_Interface.Model model;
    private UpdatePass_Interface.View view;

    public UpdatePass_Presentator(UpdatePass_Interface.View view){
        this.view = view;
        model = new UpdatePass_Model(this);
    }

    @Override
    public void UpdatePassP(String correo) {
        if (view!=null){
            model.UpdatePassM(correo);
        }
    }

    @Override
    public void MensajeP(String mensaje) {
        if (view!=null){
            view.MensajeV(mensaje);
        }
    }
}
