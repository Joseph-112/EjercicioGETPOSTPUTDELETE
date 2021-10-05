/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.EjercicioGPPD.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 *
 * @author Joseph
 */
public class ExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception ex) {
        ex.printStackTrace();
        ExceptionWrapper wrapper;
        if (ex instanceof CedulaException ) {
            wrapper = new ExceptionWrapper("404", "NOT_FOUND", ex.getMessage(),
                    "/estudiantes/obtenerPorCedula");
            return Response.status(Response.Status.NOT_FOUND).entity(wrapper).build();
        } else if (ex instanceof CedulaRepetidaException) {
            wrapper = new ExceptionWrapper("419", "CONFLICT", ex.getMessage(),
                    "/estudiantes/actualizar");
            return Response.status(Response.Status.CONFLICT).entity(wrapper).build();
        }  
        
        else {
            wrapper = new ExceptionWrapper("500", "INTERNAL_SERVER_ERROR", "",
                    "/estudiantes/obtener");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(wrapper).build();
        }
    }

}
