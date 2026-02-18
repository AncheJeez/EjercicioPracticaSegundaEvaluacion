/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author AndJe
 */
public class Empresa {
    
    private int id_empresa;
    private String nombre;
    private String descripcion;
    private String nombre_completo;
    private String email_tutor_laboral;
    
    public Empresa(){
        
    }

    public Empresa(int id_empresa, String nombre, String descripcion, String nombre_completo, String email_tutor_laboral) {
        this.id_empresa = id_empresa;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nombre_completo = nombre_completo;
        this.email_tutor_laboral = email_tutor_laboral;
    }
    
    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getEmail_tutor_laboral() {
        return email_tutor_laboral;
    }

    public void setEmail_tutor_laboral(String email_tutor_laboral) {
        this.email_tutor_laboral = email_tutor_laboral;
    }
    
    
}
