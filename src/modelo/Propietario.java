/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Usuario
 */
public class Propietario {
    private String dni;
    private String nombre;
    private Integer edad;
    private String email;

    public Propietario(String dni, String nombre, Integer edad, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.edad = edad;
        this.email = email;
    }

    public Propietario() {
    }
    
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Propietario{" + "dni=" + dni + ", nombre=" + nombre + ", edad=" + edad + ", email=" + email + '}';
    }
    
}
