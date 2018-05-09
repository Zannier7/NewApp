package pe.com.app.nose;

public class Eventodb {
    private String categoria;
    private String descripcion;
    private String email;
    private String fecha;
    private String hora;
    private String idusuario;
    private String titulo;
    private String ubilat;
    private String ubilong;

    public Eventodb() {
    }

    public Eventodb(String categoria, String descripcion, String email, String fecha, String hora, String idusuario, String titulo, String ubilat, String ubilong) {
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.email = email;
        this.fecha = fecha;
        this.hora = hora;
        this.idusuario = idusuario;
        this.titulo = titulo;
        this.ubilat = ubilat;
        this.ubilong = ubilong;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUbilat() {
        return ubilat;
    }

    public void setUbilat(String ubilat) {
        this.ubilat = ubilat;
    }

    public String getUbilong() {
        return ubilong;
    }

    public void setUbilong(String ubilong) {
        this.ubilong = ubilong;
    }
}
