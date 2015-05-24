/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.beans;


import edu.enfasis3.entity.Proyecto;
import edu.enfasis3.entity.ProyectoJpaController;
import edu.enfasis3.entity.User;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
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
public class ListaProyectoBean {

    /**
     * Creates a new instance of ListaProyectoBean
     */
    private Proyecto seleccion ;
    private List<Proyecto> list;
    private List<Proyecto> proyectosPropios;
      
    public ListaProyectoBean() {
    }

    public List<Proyecto> getProyectosPropios() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        User usuario = ((SessionBean)(context.getApplication().evaluateExpressionGet(
                    context, "#{sessionBean}", Object.class))).getUser();
       
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        EntityManager em = emf.createEntityManager();
        
        Query q = em.createNamedQuery("Proyecto.findByAllproyectoUser");
        q.setParameter("iduser", usuario.getIduser());
        proyectosPropios = q.getResultList();
        return proyectosPropios;
    }

    public void setProyectosPropios(List<Proyecto> proyectosPropios) {
        this.proyectosPropios = proyectosPropios;
    }
    
    public Proyecto getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(Proyecto seleccion) {
        this.seleccion = seleccion;
    }

    public List<Proyecto> getList() {
        
        list = new ArrayList<>();
        FacesContext context = FacesContext.getCurrentInstance();
        User usuario = ((SessionBean)(context.getApplication().evaluateExpressionGet(
                    context, "#{sessionBean}", Object.class))).getUser();
       
        List<Proyecto> ListaPro = ((ParticipanteBean)(context.getApplication().evaluateExpressionGet(
                    context, "#{participanteBean}", Object.class))).getListaProyectoPart();
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        EntityManager em = emf.createEntityManager();
        
        Query q = em.createNamedQuery("Proyecto.findByAllproyectoUser");
        q.setParameter("iduser", usuario.getIduser());
        list = q.getResultList();
        
        int element1 = list.size();
        int element2 = ListaPro.size();
        
        for (int i=0;i<element2;i++){
        
            list.add(element1+i, ListaPro.get(i));
        
        
        }
    
        return list;
    
    }
 
    public void setList(List<Proyecto> list) {
        this.list = list;
    }
    
    public void nuevoProyecto(){
       seleccion = new Proyecto();
    }
    
    
    public void crearProyecto(){
        
        
        GregorianCalendar fecha = new GregorianCalendar();
        Date d = fecha.getTime();
        
        seleccion.setRegistrationdateProyecto(d);
        seleccion.setUpdatedateProyecto(d);
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        ProyectoJpaController pjc = new ProyectoJpaController(emf);
        
        try {
            
            FacesContext context = FacesContext.getCurrentInstance();
            User useri = ((SessionBean)(context.getApplication().evaluateExpressionGet(
                    context, "#{sessionBean}", Object.class))).getUser();
            
            seleccion.setManager(useri);
            pjc.create(seleccion);
        } catch(Exception e) {
            System.out.println(e);
        }
        
    }
    
    public void eliminarProyecto(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        ProyectoJpaController pjc = new ProyectoJpaController(emf);
        
         try {
                pjc.destroy(seleccion.getIdproyecto());
            
        } catch(Exception e) {
            System.out.println(e);
        }
        
    }
    
    public void guardarProyecto(){
    
        GregorianCalendar fecha = new GregorianCalendar();
        Date d = fecha.getTime();
        
        seleccion.setUpdatedateProyecto(d);
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        ProyectoJpaController pjc = new ProyectoJpaController(emf);
        
        try {
            
            pjc.edit(seleccion);
            
        } catch(Exception e) {
            System.out.println(e);
        }
        
    }
    
  }
  