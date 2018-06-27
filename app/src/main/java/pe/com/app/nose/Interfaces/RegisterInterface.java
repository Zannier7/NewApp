package pe.com.app.nose.Interfaces;

public interface RegisterInterface {
    interface Model{
        void RegistrarUsuario(String name,String apellido,String birthday,String correo,String
                pass);
    }
    interface Presentator{
        void RegistrarUsuarioP(String name,String apellido,String birthday,String correo,String
                pass);
        void ObtenDatosUserP(String mensaje);
    }
    interface View{
        void ObtenDatosUserV(String mensaje);
    }
}
