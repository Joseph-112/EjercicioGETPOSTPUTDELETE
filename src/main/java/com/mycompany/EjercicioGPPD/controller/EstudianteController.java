/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.EjercicioGPPD.controller;

import com.google.gson.Gson;
import com.mycompany.EjercicioGPPD.dto.EstudianteDto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 *
 * @author Joseph
 */

@Stateless
@Path("/estudiantes")
public class EstudianteController {
    
    private static final String ruta="C:\\Users\\josep\\Desktop\\Personal\\Universidad\\Línea de profundización 2\\Ejemplos\\estudiantes.txt";
    
    /*
    @GET
    @Path("/obtener/{semestre}/{genero}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> obtener(    @PathParam ("semestre") String semestre,
                                    @PathParam ("genero") String genero){
        System.out.println(semestre+" "+genero);
        List<String> ejemplo = new ArrayList<>();
        ejemplo.add("Algo");
        ejemplo.add("Otro");
        return ejemplo;
    }
    */
    
    @GET
    @Path("/obtenerPorCedula/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> obtenerPorCedula(@PathParam ("cedula") String cedula){        
        System.out.println(cedula);
        List<String> ejemplo = new ArrayList<>();
        ejemplo.add("Algo");
        ejemplo.add("Otro");
        return ejemplo;
    }
    
    @GET
    @Path("/obtener")
    @Produces(MediaType.APPLICATION_JSON)
    public String obtener(){    
        /*
        List<String> materias = new ArrayList();
        materias.add("Inglés");
        materias.add("Matemática");
        int [] numeros = {1,2,3,4};
        EstudianteDto ejemplo = new EstudianteDto("123543","Juan","Perez",24,"juan@juan.com",materias,numeros);
        Gson gson = new Gson();
        */
        
        String json = leerArchivo();
        
        return json;
        
    }

    private String leerArchivo(){
        
        Gson gson = new Gson();
        
        String fichero = "";
        
        try (BufferedReader br = new BufferedReader(new FileReader (ruta))) {
            
            String linea;
            
            while((linea = br.readLine()) != null)
                fichero+=linea;
            
            EstudianteDto estudiante = gson.fromJson(fichero, EstudianteDto.class);
            fichero = gson.toJson(estudiante);
           
            return fichero;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e){
            System.out.println(e.getMessage());
        } catch (Exception e){
            fichero = leerArchivoLista(); 
            return fichero ;
        }
        return null;
    }
    
    
    private String leerArchivoLista(){
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(ruta));
            JSONArray array = new JSONArray();
            array.add(obj);
            for (Object o : array) {
                EstudianteDto estudiante = (EstudianteDto) o;
                System.out.println(estudiante.getNombre());
            }
            
        } catch (FileNotFoundException e) {
            e.getMessage();
        } catch (IOException ex) {
            Logger.getLogger(EstudianteController.class.getName()).log(Level.SEVERE, null, ex);
            ex.getMessage();
        } catch (ParseException ex) {
            Logger.getLogger(EstudianteController.class.getName()).log(Level.SEVERE, null, ex);
            ex.getMessage();
        }
        return null;
    }
    
    
    @POST
    @Path("/insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    public void insertar(EstudianteDto estudiante){
        try {
            File archivo = new File(ruta);
            FileWriter fw = new FileWriter(archivo);
            BufferedWriter estudianteBW = new BufferedWriter(fw);
            Gson gson = new Gson();
            String estudianteJson = gson.toJson(estudiante);
            if(!archivo.exists()){      
                
                archivo.createNewFile();                
                
                estudianteBW.write(estudianteJson);
                System.out.println("Primera vez");
            }else{
                estudianteBW.append(estudianteJson);
                System.out.println("No es primera vez" + estudianteJson);
            }
            estudianteBW.close();
            fw.close();
            
            /*estudianteBW.write(estudiante.getNombre());
                estudianteBW.write(estudiante.getApellido());
                estudianteBW.write(estudiante.getEdad().toString());
                estudianteBW.write(estudiante.getCorreo());
                for (String materia : estudiante.getListaMateria()) {
                    estudianteBW.write(materia);
                }
                for (int numero : estudiante.getNumero()) {
                    estudianteBW.write(numero);
                } */
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problema con encontrar archivo");
        }
    }
    
    @PUT
    @Path("/actualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    public void actualizar(List<String> listaEstudiantes){
        for (String le : listaEstudiantes) {
            System.out.println(le);
        }
        System.out.println("Registrado editado correctamente");
        //return listaEstudiantes;
    }
    
    @DELETE
    @Path("/eliminar/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public void eliminar(@PathParam("cedula") String cedula){    
        System.out.println("Se eliminó la cédula: " + cedula);
    }
    
}
