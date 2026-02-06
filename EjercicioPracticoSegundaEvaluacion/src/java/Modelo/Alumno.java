/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Date;

/**
 *
 * @author AndJe
 */
public class Alumno {
    
    private int id_alumno;
    private String nombre;
    private String apellidos;
    private String email;
    private String curso_matriculado;
    private Date fecha_nac;
    
    public Alumno(){
        
    }

    public Alumno(int id_alumno, String nombre, String apellidos, String email, String curso_matriculado, Date fecha_nac) {
        this.id_alumno = id_alumno;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.curso_matriculado = curso_matriculado;
        this.fecha_nac = fecha_nac;
    }
    
    public int getIdAlumno() {
        return id_alumno;
    }

    public void setIdAlumno(int id_alumno) {
        this.id_alumno = id_alumno;
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

    public String getCurso_matriculado() {
        return curso_matriculado;
    }

    public void setCurso_matriculado(String curso_matriculado) {
        this.curso_matriculado = curso_matriculado;
    }

    public Date getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(Date fecha_nac) {
        this.fecha_nac = fecha_nac;
    }
    
    
}
