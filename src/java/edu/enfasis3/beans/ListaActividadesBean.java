/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.beans;

import edu.enfasis3.entity.Lista;
import edu.enfasis3.entity.ListaJpaController;
import edu.enfasis3.entity.Proyecto;
import edu.enfasis3.entity.ProyectoJpaController;
import edu.enfasis3.entity.User;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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
public class ListaActividadesBean {

    private Lista lista;
    private List<Lista> listActividades;
    private Proyecto proyectolista;
    private Integer seleccionProyecto;
  
    public ListaActividadesBean() {
    }

    public Integer getSeleccionProyecto() {
        return seleccionProyecto;
    }

    public void setSeleccionProyecto(Integer seleccionProyecto) {
        this.seleccionProyecto = seleccionProyecto;
    }
    
    public Proyecto getProyectolista() {
        return proyectolista;
    }

    public void setProyectolista(Proyecto proyectolista) {
        this.proyectolista = proyectolista;
    }
    
    public Lista getLista() {
        return lista;
    }

    public void setLista(Lista lista) {
        this.lista = lista;
    }

    public List<Lista> getListActividades() {
        
       
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        EntityManager em = emf.createEntityManager();
        
        Query q = em.createNamedQuery("Lista.findByProyectos");
        q.setParameter("idproyecto",seleccionProyecto);
        
        listActividades = q.getResultList(); 
        return listActividades;
    }

    public void setListActividades(List<Lista> listActividades) {
        this.listActividades = listActividades;
    }
 
    public void nuevaLista(){
       lista = new Lista();
    }
    
    public void crearLista(){
        
        GregorianCalendar fecha = new GregorianCalendar();
        Date d = fecha.getTime();
        
        lista.setRegistrationdateLista(d);
        lista.setUpdatedateLista(d);
                
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        ListaJpaController ljc = new ListaJpaController(emf);
        
        ProyectoJpaController pjc = new ProyectoJpaController(emf);
        proyectolista=pjc.findProyecto(seleccionProyecto);
        
        try {
            
            lista.setIdproyecto(proyectolista);
            ljc.create(lista);
        } catch(Exception e) {
            System.out.println(e);
        }
    
    }
    
    
    public void eliminarLista(){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        ListaJpaController ljc = new ListaJpaController(emf);
        
        try {
                ljc.destroy(lista.getIdlista());
            
        } catch(Exception e) {
            System.out.println(e);
        }
        
    }
    
    public void guardarLista(){
    
        GregorianCalendar fecha = new GregorianCalendar();
        Date d = fecha.getTime();
        
        lista.setUpdatedateLista(d);
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        ListaJpaController ljc = new ListaJpaController(emf);
        
        try {
            
            ljc.edit(lista);
            
            
        } catch(Exception e) {
            System.out.println(e);
        }
    
    }
   
    public void actualizarTabla(){
    
        if(seleccionProyecto != null){
            System.out.println("SE HA SELECCIONADO UN PROYECTO");
        }
        else {
            System.out.println("NO PROYECTO");
            }
     
    }
    
}
