/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.EjercicioGPPD.controller;

import com.mycompany.EjercicioGPPD.dto.EstudianteDto;
import com.mycompany.EjercicioGPPD.dto.ListaEstudiantes;
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
import javax.ejb.Stateless;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 *
 * @author Laura, Joseph
 * @version 1.0.0
 */

@Stateless
@Path("/estudiantes")
public class EstudianteController {
    
    
    //private static final String ruta="D:\\Usuarios\\la1ba\\Documents\\Octavo semestre\\Linea de profundizacion II\\CrudStudentsSeptember\\estudiantes.txt";
    private static final String ruta="C:\\Users\\josep\\Desktop\\Personal\\Universidad\\Línea de profundización 2\\Trabajos\\EjercicioGETPOSTPUTDELETE\\estudiantes.txt";
    
    /**
     * Método GET para obtener un estudiante utilizando el número de cédula
     * @param cedula
     * @return 
     */
    @GET
    @Path("/obtenerPorCedula/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPorCedula(@Valid @PathParam ("cedula") String cedula){        
        
        List<EstudianteDto> aux = leerArchivo();
        EstudianteDto encontrado = null;
        for ( EstudianteDto estudianteDto : aux) {
            
            if (estudianteDto.getCedula().equals(cedula)) {
                System.out.println(estudianteDto.toString());
                encontrado = estudianteDto;
                return Response.status(Response.Status.OK).entity(encontrado).header("Tipo dato","Estudiante").build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity(encontrado).header("Tipo dato", "Lista de estudiantes").build();

          
    }
    
    /**
     * Método GET para obtener la lista completa de estudiantes ingresados en el archivo
     * @return 
     */
    @GET
    @Path("/obtener")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtener(){    
        
        List<EstudianteDto> aux = leerArchivo();
        for (EstudianteDto estudianteDto : aux) {
            System.out.println(estudianteDto.toString());
        }
        return Response.status(Response.Status.OK).entity(aux).header("Tipo dato","Lista de estudiantes").build();
            
    }    
    
    /**
     * Método POST para insertar un estudiante (en formato JSON) a la lista en el archivo
     * @param estudiante
     * @return 
     */
    @POST
    @Path("/insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertar(@Valid EstudianteDto estudiante){
        List<EstudianteDto> listaNueva = leerArchivo();
        estudiante.getCedula();
        for (EstudianteDto estuCedula : listaNueva) {
            if (estuCedula.getCedula().equals(estudiante.getCedula())) {
              return Response.status(Response.Status.CONFLICT).entity(estudiante).header("Tipo dato","EstudianteDto").build(); 
            } 
        }
        listaNueva.add(estudiante);
        escribirEnArchivo(listaNueva);   
        return Response.status(Response.Status.CREATED).entity(estudiante).header("Tipo dato","EstudianteDto").build(); 
      
    }
    
    /**
     * Método opcional POST para ingresar una lista de estudiantes (en formato JSON) al archivo.
     * @param estudiante
     * @return 
     */
    @POST
    @Path("/insertarPorLista")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertarPorLista(List<EstudianteDto> estudiante){
        
        try {
            
            ListaEstudiantes lista = new ListaEstudiantes();
            lista.setListaEst(estudiante);
            
            File archivo = new File(ruta);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo));
            
            oos.writeObject(lista);
            
            oos.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EstudianteController.class.getName()).log(Level.SEVERE, null, ex);
            ex.getMessage();
        } catch (IOException ex) {
            Logger.getLogger(EstudianteController.class.getName()).log(Level.SEVERE, null, ex);
            ex.getMessage();
        }
            
        return Response.status(Response.Status.CREATED).entity(estudiante).header("Tipo dato","Lista de estudiantes").build();
    }

    /**
     * Método PUT para modificar 
     * @param cedula
     * @param estudianteDto
     * @return 
     */
    @PUT
    @Path("/actualizar/{cedula}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(@PathParam("cedula") String cedula,@Valid EstudianteDto estudianteDto){
        
        List<EstudianteDto> aux = leerArchivo();
        List<EstudianteDto> listaNueva = new ArrayList<EstudianteDto>();
        byte count = 0;
        for(EstudianteDto encontrado : aux){
            if(!encontrado.getCedula().equals(cedula)){
                listaNueva.add(encontrado);
            }else{
                count+=1;
            }
        }

        if (count < 1) {
            return Response.status(Response.Status.NOT_FOUND).entity("la cedula no se encuentra en el sistema: "+cedula).header("Tipo dato", "Estudiante").build();
        }
        
        for(EstudianteDto encontradoDos : listaNueva){
            if(encontradoDos.getCedula().equals(estudianteDto.getCedula())){                
                return Response.status(Response.Status.CONFLICT).entity("la cedula ya se encuentra en el sistema: "+encontradoDos).header("Tipo dato", "Estudiante").build();
            }
        }
        listaNueva.add(estudianteDto);

        escribirEnArchivo(listaNueva);
        return Response.status(Response.Status.OK).entity(listaNueva).header("Tipo dato","Estudiante").build();
    }
    
    /**
     * Método DELETE para eliminar un estudiante del archivo filtrando su cédula
     * @param cedula
     * @return 
     */
    @DELETE
    @Path("/eliminar/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@PathParam("cedula") String cedula){    
        List<EstudianteDto> aux = leerArchivo();
        List<EstudianteDto> listaNueva = new ArrayList<EstudianteDto>();
        for (EstudianteDto estudianteDto : aux) {
            
            if (!estudianteDto.getCedula().equals(cedula)) {
                listaNueva.add(estudianteDto);
            }else{
                System.out.println("Estudiante eliminada: " + estudianteDto.toString());                
                
            }
        }
        escribirEnArchivo(listaNueva);
        return Response.status(Response.Status.NO_CONTENT).entity(listaNueva).header("Tipo dato","Estudiante").build();
    }
    
    /**
     * Método para leer el archivo en donde se encuentran almacenados todos los estudiantes ingresados y regresa la lista con los mismos
     * @return
     * @return List<EstudianteDto> 
     */
    private List<EstudianteDto> leerArchivo(){
        
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
     * Método para escribir en el archivo que almacena los estudiantes
     * La lista que contiene a los estudiantes debe ser de tipo "serializable" para realizar la escritura
     * @param listaEstudiantes 
     */
    public void escribirEnArchivo(List<EstudianteDto> listaEstudiantes){
        
        try{
            
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
    
}
