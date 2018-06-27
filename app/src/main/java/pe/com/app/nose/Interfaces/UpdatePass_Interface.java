package pe.com.app.nose.Interfaces;

public interface UpdatePass_Interface {
    interface Model{
        void UpdatePassM(String correo);
    }
    interface Presentator{
        void UpdatePassP(String correo);
        void MensajeP(String mensaje);
    }
    interface View{
        void MensajeV(String mensaje);
    }
}
