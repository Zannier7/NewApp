package pe.com.app.nose.Entidad;

public class Favoritodb {
    private String idevento;
    private String idusuario;

    public Favoritodb() {
    }

    public Favoritodb(String idevento, String idusuario) {
        this.idevento = idevento;
        this.idusuario = idusuario;
    }

    public String getIdevento() {
        return idevento;
    }

    public void setIdevento(String idevento) {
        this.idevento = idevento;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }
}
