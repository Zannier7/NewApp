package pe.com.app.nose.Entidad;

public class Usuariodb {

    private String apellidos;
    private String correo;
    private String fecha_nacimiento;
    private String nombres;
    private String uid;
    private String descripcion;
    private String numero;

    public Usuariodb() {
    }

    public Usuariodb(String apellidos, String correo, String fecha_nacimiento, String nombres, String uid, String descripcion, String numero) {
        this.apellidos = apellidos;
        this.correo = correo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.nombres = nombres;
        this.uid = uid;
        this.descripcion = descripcion;
        this.numero = numero;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
