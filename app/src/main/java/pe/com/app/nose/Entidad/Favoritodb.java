package pe.com.app.nose.Entidad;

public class Favoritodb {
    private String idevento;
    private String idfavoritp;
    private String idusuario;

    public Favoritodb() {
    }

    public Favoritodb(String idevento, String idfavoritp, String idusuario) {
        this.idevento = idevento;
        this.idfavoritp = idfavoritp;
        this.idusuario = idusuario;
    }

    public String getIdevento() {
        return idevento;
    }

    public void setIdevento(String idevento) {
        this.idevento = idevento;
    }

    public String getIdfavoritp() {
        return idfavoritp;
    }

    public void setIdfavoritp(String idfavoritp) {
        this.idfavoritp = idfavoritp;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }
}
