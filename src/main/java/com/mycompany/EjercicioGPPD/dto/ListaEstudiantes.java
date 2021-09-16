/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.EjercicioGPPD.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joseph
 */
public class ListaEstudiantes implements Serializable {
    
    private List<EstudianteDto> listaEst;

    public ListaEstudiantes() {
    }

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
