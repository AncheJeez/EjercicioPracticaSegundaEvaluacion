/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author AndJe
 */
public class Profesor {
    
    private int id_profesor;
    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private boolean directiva;
    
    public Profesor(){
        
    }

    public Profesor(int id_profesor, String nombre, String apellidos, String email, String password, boolean directiva) {
        this.id_profesor = id_profesor;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
        this.directiva = directiva;
    }
    
    public int getIdProfesor() {
        return id_profesor;
    }

    public void setIdProfesor(int id_profesor) {
        this.id_profesor = id_profesor;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getDirectiva() {
        return directiva;
    }

    public void setDirectiva(boolean directiva) {
        this.directiva = directiva;
    }
    
    
}
