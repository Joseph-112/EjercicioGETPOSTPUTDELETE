/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.EjercicioGPPD.service;

import com.mycompany.EjercicioGPPD.controller.EstudianteController;
import com.mycompany.EjercicioGPPD.dto.EstudianteDto;
import com.mycompany.EjercicioGPPD.dto.ListaEstudiantes;
import com.mycompany.EjercicioGPPD.exception.CedulaException;
import com.mycompany.EjercicioGPPD.exception.CedulaRepetidaException;
import com.mycompany.EjercicioGPPD.exception.CorreoRepetidoException;
import com.mycompany.EjercicioGPPD.exception.EdadException;
import com.mycompany.EjercicioGPPD.exception.LongitudCedException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;

/**
 *
 * @author Joseph
 */
public class EstudianteService {

    private static final String ruta = "C:\\Users\\josep\\Desktop\\Personal\\Universidad\\Línea de profundización 2\\Repositorio emergencia\\RespaldoEjercicioGETPOSTPUTDELETE\\estudiantes.txt";

    /**
     * Método para leer el archivo en donde se encuentran almacenados todos los
     * estudiantes ingresados y regresa la lista con los mismos
     *
     * @return
     * @throws com.mycompany.EjercicioGPPD.exception.ListaVaciaException
     */
    public List<EstudianteDto> leerArchivo()  {

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(ruta));
            ListaEstudiantes aux = (ListaEstudiantes) ois.readObject();
            ois.close();
            
            return aux.getListaEst();
        } catch (FileNotFoundException ex) {
            ex.getMessage();
        } catch (IOException ex) {
            ex.getMessage();            
        }       
        catch (ClassNotFoundException ex) {
            ex.getMessage();
        } finally {
            try {
                ois.close();
            } catch (IOException ex) {
                ex.getMessage();
            }
        }
        return null;
    }
    
    /**
     * Método para escribir en el archivo que almacena los estudiantes La lista
     * que contiene a los estudiantes debe ser de tipo "serializable" para
     * realizar la escritura
     *
     * @param listaEstudiantes
     */
    public void escribirEnArchivo(List<EstudianteDto> listaEstudiantes) {

        try {

            File archivo = new File(ruta);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo));

            ListaEstudiantes lista = new ListaEstudiantes();
            lista.setListaEst(listaEstudiantes);

            oos.writeObject(lista);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(EstudianteController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EstudianteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertarPorLista(List<EstudianteDto> estudiante) throws EdadException , CedulaRepetidaException, LongitudCedException, CorreoRepetidoException , Exception{
        
        try {

            ListaEstudiantes lista = new ListaEstudiantes();
            lista.setListaEst(estudiante);

            List<EstudianteDto> listaArchivo = leerArchivo();

            for (EstudianteDto estuCedula : listaArchivo) {

                for (EstudianteDto estudianteDto : estudiante) {
                    if (estuCedula.getCedula().equals(estudianteDto.getCedula())) {
                        throw new CedulaRepetidaException("Cédula ya registrada");
                    }
                    
                    if (estuCedula.getCorreo().equals(estudianteDto.getCorreo())) {
                        throw new CorreoRepetidoException("Correo ya registrado");
                    }
                    
                    if (estudianteDto.getCedula().length() < 8 || estudianteDto.getCedula().length() > 11) {
                        throw new LongitudCedException("La cédula debe tener entre 8 y 11 dígitos");
                    }
                    
                    if (estudianteDto.getEdad() < 18 || estudianteDto.getEdad() > 120) {
                        throw new EdadException("Edad debe ser entre 18 y 120 años");
                    }
                }

            }

            for (EstudianteDto estudianteDto : lista.getListaEst()) {
                listaArchivo.add(estudianteDto);
            }

            escribirEnArchivo(listaArchivo);
            /*lista.setListaEst(listaArchivo);

            File archivo = new File(ruta);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo));

            oos.writeObject(lista);

            oos.close();*/

        } catch (CedulaRepetidaException | LongitudCedException | CorreoRepetidoException | EdadException e) {
            throw e;
        } catch (Exception ex){
            ex.getMessage();
        }
    }

    public EstudianteDto buscarPorCedula(String cedula) throws CedulaException, LongitudCedException , Exception {

        try {
            
            if (cedula.length() < 8 || cedula.length() > 11) {
                throw new LongitudCedException("La cédula debe tener entre 8 y 11 dígitos");
            }
            List<EstudianteDto> aux = leerArchivo();
            for (EstudianteDto estudianteDto : aux) {

                if (estudianteDto.getCedula().equals(cedula)) {
                    System.out.println(estudianteDto.toString());
                    return estudianteDto;
                }
            }            
            throw new CedulaException("Cédula no encontrada");
        } catch (CedulaException | LongitudCedException ex) {
            throw ex;
        } catch (Exception ex){
            throw new Exception("");
        }

        
    }

    public void eliminarPorCedula(String cedula) throws CedulaException, LongitudCedException , Exception{
        
        
        EstudianteDto estudiante = buscarPorCedula(cedula);
        List<EstudianteDto> lista = leerArchivo();
        List<EstudianteDto> listaNueva = new ArrayList<EstudianteDto>();
        for (EstudianteDto estudianteDto : lista) {
            if (!estudianteDto.getCedula().equals(cedula)) 
                listaNueva.add(estudianteDto);
        }
        escribirEnArchivo(listaNueva);
    }
    
    public void actualizar (String cedula, EstudianteDto estudianteDto) throws EdadException , CedulaException, CedulaRepetidaException, LongitudCedException , CorreoRepetidoException , Exception{
        
        EstudianteDto estudiante = buscarPorCedula(cedula);
        
        List<EstudianteDto> aux = leerArchivo();
        eliminarPorCedula(cedula);
        List<EstudianteDto> lista = leerArchivo();
        
        //List<EstudianteDto> listaNueva = new ArrayList<>();
        try {
            
            
            for (EstudianteDto encontradoDos : lista) {
                
                if (encontradoDos.getCedula().equals(estudianteDto.getCedula())) {
                    escribirEnArchivo(aux);
                    throw new CedulaRepetidaException("Cédula ya registrada");
                }
                
                if (encontradoDos.getCorreo().equals(estudianteDto.getCorreo())) {
                    escribirEnArchivo(aux);
                    throw new CorreoRepetidoException("Correo ya registrado");
                }
                
                if (estudianteDto.getEdad() < 18 || estudianteDto.getEdad() > 120) {
                    escribirEnArchivo(aux);
                    throw new EdadException("Edad debe ser entre 18 y 120 años");
                }
            }
            
            lista.add(estudianteDto);
            escribirEnArchivo(lista);
            
        } catch (CedulaRepetidaException | CorreoRepetidoException | EdadException e) {
            throw e;
        } catch (Exception e){
            throw new Exception("");
        }
    }
}
