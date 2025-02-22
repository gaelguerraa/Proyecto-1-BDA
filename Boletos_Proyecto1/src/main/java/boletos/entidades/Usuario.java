/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boletos.entidades;

import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author jalt2
 */
public class Usuario {
    private Integer idUsuario;
    private String email;
    private String nombre;
    private String contrasena;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private LocalDate fechaNacimiento;
    private Double saldo;
    private Direccion direccion;
    
    
    
    /*Constructor que recibe todos los datos del usuario*/

    public Usuario(Integer idUsuario, String email, String nombre, String contrasena, String apellidoPaterno, String apellidoMaterno, LocalDate fechaNacimiento, Direccion direccion) {
        this.idUsuario = idUsuario;
        this.email = email;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
    }
    
    /*Constructor que no recibe el id del usuario*/

    public Usuario(String email, String nombre, String contrasena, String apellidoPaterno, String apellidoMaterno, LocalDate fechaNacimiento, Direccion direccion) {
        this.email = email;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
 
    }

    public Usuario(Integer idUsuario, String contrasena) {
        this.idUsuario = idUsuario;
        this.contrasena = contrasena;
    }

    /*Getters and Setters*/

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }



    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.idUsuario);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        return true;
    }

    
    
}
