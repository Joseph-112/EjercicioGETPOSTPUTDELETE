/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.EjercicioGPPD.controller;

import com.mycompany.EjercicioGPPD.dto.EstudianteDto;
import com.mycompany.EjercicioGPPD.dto.ListaEstudiantes;
import com.mycompany.EjercicioGPPD.exception.CedulaException;
import com.mycompany.EjercicioGPPD.exception.CedulaRepetidaException;
import com.mycompany.EjercicioGPPD.exception.ExceptionWrapper;
import com.mycompany.EjercicioGPPD.service.EstudianteService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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
    private static final String ruta = "C:\\Users\\josep\\Desktop\\Personal\\Universidad\\Línea de profundización 2\\Trabajos\\EjercicioGETPOSTPUTDELETE\\estudiantes.txt";
    private static final EstudianteService est = new EstudianteService();

    /**
     * Método GET para obtener un estudiante utilizando el número de cédula
     *
     * @param cedula
     * @return
     * @throws com.mycompany.EjercicioGPPD.exception.CedulaException
     */
    @GET
    @Path("/obtenerPorCedula/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPorCedula(@Valid @PathParam("cedula") String cedula) throws CedulaException, Exception {

        try {
            EstudianteDto estudianteDto = est.buscarPorCedula(cedula);
            return Response.status(Response.Status.OK).entity(estudianteDto).header("Tipo dato", "EstudianteDto").build();
        } catch (CedulaException e) {
            System.out.println("Entro a exception");
            e.printStackTrace();
            ExceptionWrapper wrapper = new ExceptionWrapper("404", "NOT_FOUND", "Cédula no encontrada",
                    "/estudiantes/obtenerPorCedula");

            return Response.status(Response.Status.NOT_FOUND).entity(wrapper).build();
        }
    }

    /**
     * Método GET para obtener la lista completa de estudiantes ingresados en el
     * archivo
     *
     * @return
     */
    @GET
    @Path("/obtener")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtener() {

        List<EstudianteDto> aux = est.leerArchivo();
        if (aux.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).entity("Lista vacia").header("Tipo dato", "Lista de estudiantes").build();
        }
        for (EstudianteDto estudianteDto : aux) {
            System.out.println(estudianteDto.toString());
        }
        return Response.status(Response.Status.OK).entity(aux).header("Tipo dato", "Lista de estudiantes").build();

    }

    /**
     * Método POST para insertar un estudiante (en formato JSON) a la lista en
     * el archivo
     *
     * @param estudiante
     * @return
     */
    @POST
    @Path("/insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertar(@Valid EstudianteDto estudiante) {
        List<EstudianteDto> listaNueva = est.leerArchivo();
        estudiante.getCedula();
        for (EstudianteDto estuCedula : listaNueva) {
            if (estuCedula.getCedula().equals(estudiante.getCedula())) {
                return Response.status(Response.Status.CONFLICT).entity(estudiante).header("Tipo dato", "EstudianteDto").build();
            }
        }
        listaNueva.add(estudiante);
        est.escribirEnArchivo(listaNueva);
        return Response.status(Response.Status.CREATED).entity("Creado Correctamente: " + estudiante).header("Tipo dato", "EstudianteDto").build();

    }

    /**
     * Método opcional POST para ingresar una lista de estudiantes (en formato
     * JSON) al archivo.
     *
     * @param estudiante
     * @return
     */
    @POST
    @Path("/insertarPorLista")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertarPorLista(@Valid List<EstudianteDto> estudiante) {

        try {

            ListaEstudiantes lista = new ListaEstudiantes();
            lista.setListaEst(estudiante);

            List<EstudianteDto> listaArchivo = est.leerArchivo();

            for (EstudianteDto estuCedula : listaArchivo) {

                for (EstudianteDto estudianteDto : estudiante) {
                    if (estuCedula.getCedula().equals(estudianteDto.getCedula())) {
                        return Response.status(Response.Status.CONFLICT).entity("Cedula ya registrada: " + estuCedula).header("Tipo dato", "EstudianteDto").build();
                    }
                }

            }

            for (EstudianteDto estudianteDto : lista.getListaEst()) {
                listaArchivo.add(estudianteDto);
            }

            lista.setListaEst(listaArchivo);

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
        return Response.status(Response.Status.CREATED).entity("Creado Correctamente: " + estudiante).header("Tipo dato", "EstudianteDto").build();
    }

    /**
     * Método PUT para modificar
     *
     * @param cedula
     * @param estudianteDto
     * @return
     */
    @PUT
    @Path("/actualizar/{cedula}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(@Valid @NotEmpty(message = "No se permite campo vacio")
            @Size(min = 8, max = 10, message = "Minimo 8 digitos maximo 10") @PathParam("cedula") String cedula, @Valid EstudianteDto estudianteDto) throws CedulaException, CedulaRepetidaException, Exception{
    
        try {
            List<EstudianteDto> listaNueva = est.actualizar(cedula, estudianteDto);
            est.escribirEnArchivo(listaNueva);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (CedulaException e) {
            System.out.println("Entro a cedula no encontrada exception");
            e.printStackTrace();
            ExceptionWrapper wrapper = new ExceptionWrapper("404", "NOT_FOUND", "Cédula no encontrada",
                    "/estudiantes/actualizar");

            return Response.status(Response.Status.NOT_FOUND).entity(wrapper).build();
        } catch (CedulaRepetidaException e) {
            System.out.println("Entro a cedula ya existente exception");
            e.printStackTrace();
            ExceptionWrapper wrapper = new ExceptionWrapper("419", "CONFLICT", "Cédula ya resgistrada",
                    "/estudiantes/actualizar");

            return Response.status(Response.Status.CONFLICT).entity(wrapper).build();
        } 
            
        
        /*
        List<EstudianteDto> aux = est.leerArchivo();
        List<EstudianteDto> listaNueva = new ArrayList<EstudianteDto>();
        byte count = 0;
        for (EstudianteDto encontrado : aux) {
            if (!encontrado.getCedula().equals(cedula)) {
                listaNueva.add(encontrado);
            } else {
                count += 1;
            }
        }

        if (count < 1) {
            return Response.status(Response.Status.NOT_FOUND).entity("la cedula no se encuentra en el sistema: " + cedula).header("Tipo dato", "Estudiante").build();
        }

        for (EstudianteDto encontradoDos : listaNueva) {
            if (encontradoDos.getCedula().equals(estudianteDto.getCedula())) {
                return Response.status(Response.Status.CONFLICT).entity("la cedula ya se encuentra en el sistema: " + encontradoDos).header("Tipo dato", "Estudiante").build();
            }
        }
        listaNueva.add(estudianteDto);

        est.escribirEnArchivo(listaNueva);
        return Response.status(Response.Status.OK).entity("Lista actualizada: " + listaNueva).header("Tipo dato", "Estudiante").build();
    */
    }

    /**
     * Método DELETE para eliminar un estudiante del archivo filtrando su cédula
     *
     * @param cedula
     * @return
     */
    @DELETE
    @Path("/eliminar/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@Valid @NotEmpty(message = "No se permite campo vacio")
            @Size(min = 8, max = 10, message = "Minimo 8 digitos maximo 15") @PathParam("cedula") String cedula) throws Exception {

        try {
            List<EstudianteDto> listaNueva = est.eliminarPorCedula(cedula);
            est.escribirEnArchivo(listaNueva);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (CedulaException e) {
            System.out.println("Entro a exception");
            e.printStackTrace();
            ExceptionWrapper wrapper = new ExceptionWrapper("404", "NOT_FOUND", "Cédula no encontrada",
                    "/estudiantes/obtenerPorCedula");
            return Response.status(Response.Status.NOT_FOUND).entity(wrapper).build();
        }
    }

}
