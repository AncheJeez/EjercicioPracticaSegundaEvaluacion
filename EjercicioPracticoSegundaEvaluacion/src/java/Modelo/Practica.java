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
public class Practica {
    
    private Alumno alumno;
    private Empresa empresa;
    private Date fecha_comienzo;
    private Date fecha_finalizacion;
    private String comentarios;
    
    public Practica(){
        
    }

    public Practica(Alumno alumno, Empresa empresa, Date fecha_comienzo, Date fecha_finalizacion, String comentarios) {
        this.alumno = alumno;
        this.empresa = empresa;
        this.fecha_comienzo = fecha_comienzo;
        this.fecha_finalizacion = fecha_finalizacion;
        this.comentarios = comentarios;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Date getFecha_comienzo() {
        return fecha_comienzo;
    }

    public void setFecha_comienzo(Date fecha_comienzo) {
        this.fecha_comienzo = fecha_comienzo;
    }

    public Date getFecha_finalizacion() {
        return fecha_finalizacion;
    }

    public void setFecha_finalizacion(Date fecha_finalizacion) {
        this.fecha_finalizacion = fecha_finalizacion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
    
    
}
