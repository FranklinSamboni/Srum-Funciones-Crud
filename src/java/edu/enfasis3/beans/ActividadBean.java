/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.beans;

import edu.enfasis3.entity.Actividad;
import edu.enfasis3.entity.ActividadJpaController;
import edu.enfasis3.entity.Lista;
import edu.enfasis3.entity.ListaJpaController;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Frank
 */
@ManagedBean
@ViewScoped
public class ActividadBean {

    
    private Actividad actividad;
    private List<Actividad> listaActividad;
    private Lista lista;
    private Integer seleccionListaActividades;
    
    public ActividadBean() {
    }


    public Integer getSeleccionListaActividades() {
        return seleccionListaActividades;
    }

    public void setSeleccionListaActividades(Integer seleccionListaActividades) {
        this.seleccionListaActividades = seleccionListaActividades;
    }

    
    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public List<Actividad> getListaActividad() {
        
       
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        EntityManager em = emf.createEntityManager();
        
        Query q = em.createNamedQuery("Actividad.findByActividadLista");
        q.setParameter("idlista", seleccionListaActividades);
        
        listaActividad = q.getResultList();
        
        return listaActividad;
    }

    public void setListaActividad(List<Actividad> listaActividad) {
        this.listaActividad = listaActividad;
    }


    public void nuevaActividad(){
        actividad = new Actividad();
    }
    
    public void crearActividad(){
        
        GregorianCalendar fecha = new GregorianCalendar();
        Date d = fecha.getTime();
        
        actividad.setRegistrariondateActividad(d);
        actividad.setUpdatedateActividad(d);
                
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        ActividadJpaController ajc = new ActividadJpaController(emf);
        
        ListaJpaController ljc = new ListaJpaController(emf);
        lista = ljc.findLista(seleccionListaActividades);
        
        try{
            actividad.setIdlista(lista);
            ajc.create(actividad);
        }catch(Exception e) {
            System.out.println(e);
        }
    }
    
    public void guardarActividad(){
        
        GregorianCalendar fecha = new GregorianCalendar();
        Date d = fecha.getTime();
        
        actividad.setUpdatedateActividad(d);
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        ActividadJpaController ajc = new ActividadJpaController(emf);
        try {
            
            ajc.edit(actividad);
            
            
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    
    public void eliminarActividad(){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        ActividadJpaController ajc = new ActividadJpaController(emf);
        try {
            
            ajc.destroy(actividad.getIdactividad());
            
            
        } catch(Exception e) {
            System.out.println(e);
        }
    
    
        }
   
    
}
