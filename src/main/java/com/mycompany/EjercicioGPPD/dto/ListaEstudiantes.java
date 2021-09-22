/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.EjercicioGPPD.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Clase ListaEstudiantes de tipo "Serializable" para ser almacenada en archivo plano
 * @author Laura Babativa, Joseph Trejos
 * @version 1.0.0
 */
public class ListaEstudiantes implements Serializable {
    
    
    private List<EstudianteDto> listaEst;

    /**
     * Constructor vacío
     */
    public ListaEstudiantes() {
    }

    /**
     * Constructor que recibe una lista de tipo EstudianteDto
     * @param listaEst 
     */
    public ListaEstudiantes(List<EstudianteDto> listaEst) {
        this.listaEst = listaEst;
    }

    public List<EstudianteDto> getListaEst() {
        return listaEst;
    }

    public void setListaEst(List<EstudianteDto> listaEst) {
        this.listaEst = listaEst;
    }
    
    
    
}
