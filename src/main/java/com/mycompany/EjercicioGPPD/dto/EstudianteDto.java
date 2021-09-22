/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.EjercicioGPPD.dto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Clase EstudianteDTO de tipo "Serializable" para ser almacenada en archivo plano
 * @author Laura, Joseph
 * @version 1.0.0
 */
@Entity
public class EstudianteDto implements Serializable{
    
    
    @Id 
    @Min(value = 100000, message="Debe ser mayor de 8 digitos")
    @Max(value = 2000000000, message="Debe ser menor de 11 digitos")
    private String cedula;
    @Size(min=2, max=30)
    private String nombre;
    @Size(min=2, max=30)
    private String apellido;
    @Min(value=18)
    @Max(value = 120)
    private Integer edad;
    @Pattern(message = "Error, correo del medico invalido", regexp = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$")
    private String correo;
    private List<String> listaMateria;
    private int[] numero;

    /**
     * Constructor vacío
     */
    public EstudianteDto() {
    }

    /**
     * Constructor que recibe cedula, nombre, apellido, edad, correo, lista de materias y vector con números
     * @param cedula
     * @param nombre
     * @param apellido
     * @param edad
     * @param correo
     * @param listaMateria
     * @param numero 
     */
    public EstudianteDto(String cedula, String nombre, String apellido, Integer edad, String correo, List<String> listaMateria, int[] numero) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.correo = correo;
        this.listaMateria = listaMateria;
        this.numero = numero;
    }

    /**
     * Método sobreescrito que retorna de forma estructurada, toda la información de un estudiante
     * @return String
     */
    @Override
    public String toString() {
        return "EstudianteDto{" + "cedula=" + cedula + ", nombre=" + nombre + ", apellido=" + apellido + ", edad=" + edad + ", correo=" + correo + ", listaMateria=" + listaMateria + ", numero=" + Arrays.toString(numero) + '}';
    }
    
    /*
    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException{
        // perform the default de-serialization first
        aInputStream.defaultReadObject();
 
    }*/

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public List<String> getListaMateria() {
        return listaMateria;
    }

    public void setListaMateria(List<String> listaMateria) {
        this.listaMateria = listaMateria;
    }

    public int[] getNumero() {
        return numero;
    }

    public void setNumero(int[] numero) {
        this.numero = numero;
    }
    
    
}
