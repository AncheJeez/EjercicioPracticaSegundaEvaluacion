package Modelo;

import java.util.Date;

public class Alumno {

    private int idAlumno;
    private String nombre;
    private String apellidos;
    private String email;
    private String cursoMatriculado;
    private Date fechaNac;

    public Alumno() {}

    public Alumno(int idAlumno, String nombre, String apellidos,
                  String email, String cursoMatriculado, Date fechaNac) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.cursoMatriculado = cursoMatriculado;
        this.fechaNac = fechaNac;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCursoMatriculado() {
        return cursoMatriculado;
    }

    public void setCursoMatriculado(String cursoMatriculado) {
        this.cursoMatriculado = cursoMatriculado;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }
}
