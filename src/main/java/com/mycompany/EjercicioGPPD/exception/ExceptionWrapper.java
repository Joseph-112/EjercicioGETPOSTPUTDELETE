/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.EjercicioGPPD.exception;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Joseph
 */
public class ExceptionWrapper implements Serializable{
    private String codigo;

    private String error;

    private String fecha;

    private String mensaje;

    private String url;

    public ExceptionWrapper() {
    }

    public ExceptionWrapper(String codigo, String error, String mensaje, String url) {
        this.codigo = codigo;
        this.error = error;
        this.fecha = new Date().toString();
        this.mensaje = mensaje;
        this.url = url;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
}
