package pe.com.app.nose.Entidad;

public class Eventodb {
    private String categoria;
    private String descripcion;
    private String email;
    private String fecha;
    private String hora;
    private String idusuario;
    private String titulo;
    private double ubilat;
    private double ubilong;
    private String idevento;

    public Eventodb() {
    }

    public Eventodb(String categoria, String descripcion, String email, String fecha, String hora, String idusuario, String titulo, double ubilat, double ubilong) {
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

    public Eventodb(String categoria, String descripcion, String email, String fecha, String hora, String idusuario, String titulo, double ubilat, double ubilong, String idevento) {
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.email = email;
        this.fecha = fecha;
        this.hora = hora;
        this.idusuario = idusuario;
        this.titulo = titulo;
        this.ubilat = ubilat;
        this.ubilong = ubilong;
        this.idevento = idevento;
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

    public double getUbilat() {
        return ubilat;
    }

    public void setUbilat(double ubilat) {
        this.ubilat = ubilat;
    }

    public double getUbilong() {
        return ubilong;
    }

    public void setUbilong(double ubilong) {
        this.ubilong = ubilong;
    }

    public String getIdevento() {
        return idevento;
    }

    public void setIdevento(String idevento) {
        this.idevento = idevento;
    }
}