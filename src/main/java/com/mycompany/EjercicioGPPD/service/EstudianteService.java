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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;

/**
 *
 * @author Joseph
 */
public class EstudianteService {

    private static final String ruta = "C:\\Users\\josep\\Desktop\\Personal\\Universidad\\Línea de profundización 2\\Trabajos\\EjercicioGETPOSTPUTDELETE\\estudiantes.txt";

    /**
     * Método para leer el archivo en donde se encuentran almacenados todos los
     * estudiantes ingresados y regresa la lista con los mismos
     *
     * @return
     * @return List<EstudianteDto>
     */
    public List<EstudianteDto> leerArchivo() {

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(ruta));
            ListaEstudiantes aux = (ListaEstudiantes) ois.readObject();
            ois.close();
            return aux.getListaEst();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EstudianteController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EstudianteController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EstudianteController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(EstudianteController.class.getName()).log(Level.SEVERE, null, ex);
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

    public EstudianteDto buscarPorCedula(String cedula) throws CedulaException, Exception {

        try {
            List<EstudianteDto> aux = leerArchivo();
            for (EstudianteDto estudianteDto : aux) {

                if (estudianteDto.getCedula().equals(cedula)) {
                    System.out.println(estudianteDto.toString());
                    return estudianteDto;
                }
            }
            throw new CedulaException("Cédula no encontrada");
        } catch (CedulaException ex) {
            throw ex;
        } catch (Exception ex){
            throw new Exception("");
        }

        
    }

    public List<EstudianteDto> eliminarPorCedula(String cedula) throws CedulaException, Exception{
        EstudianteDto estudiante = buscarPorCedula(cedula);
        List<EstudianteDto> lista = leerArchivo();
        List<EstudianteDto> listaNueva = new ArrayList<EstudianteDto>();
        for (EstudianteDto estudianteDto : lista) {
            if (!estudianteDto.getCedula().equals(cedula)) 
                listaNueva.add(estudianteDto);
        }
        return listaNueva;
    }
    
    public List<EstudianteDto> actualizar (String cedula, EstudianteDto estudianteDto) throws CedulaException, CedulaRepetidaException, Exception{
        
        List<EstudianteDto> listaNueva = eliminarPorCedula(cedula);
        try {
            for (EstudianteDto encontradoDos : listaNueva) {
                if (encontradoDos.getCedula().equals(estudianteDto.getCedula())) {
                    throw new CedulaRepetidaException("Cédula ya registrada");
                }
            }
            listaNueva.add(estudianteDto);

            return listaNueva;
            
        } catch (CedulaRepetidaException e) {
            throw e;
        } catch (Exception e){
            throw new Exception("");
        }
    }
}
