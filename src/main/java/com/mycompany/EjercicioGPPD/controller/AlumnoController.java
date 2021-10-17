/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.EjercicioGPPD.controller;

import com.logic.dto.EstudianteDto;
import com.logic.entity.Alumno;
import com.logic.service.IAlumnoService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Joseph
 */
public class AlumnoController {
    
    private IAlumnoService service;
    
    @POST
    @Path("/guardar")
    @Consumes (MediaType.APPLICATION_JSON)
    public void guardar(Alumno alumno){
        /*try {
            this.service.guardar((List<EstudianteDto>) alumno);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception ex) {
            Logger.getLogger(AlumnoController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
}
