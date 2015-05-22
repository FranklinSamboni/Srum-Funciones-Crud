/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.beans;


import edu.enfasis3.entity.Proyecto;
import edu.enfasis3.entity.ProyectoJpaController;
import edu.enfasis3.entity.User;
import java.io.Serializable;
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
    private List proyectoNombres;
    private List<Proyecto> list;
    private Integer idproyecto=2;
    
    
    
    public ListaProyectoBean() {
    }

    public List<String> getProyectoNombres() {
        
        proyectoNombres = new ArrayList<>();
        int element;
        Proyecto pro;
        
        FacesContext context = FacesContext.getCurrentInstance();
        User usuario = ((SessionBean)(context.getApplication().evaluateExpressionGet(
                    context, "#{sessionBean}", Object.class))).getUser();
       
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        EntityManager em = emf.createEntityManager();
        
        Query q = em.createNamedQuery("Proyecto.findByAllproyectoUser");
        q.setParameter("iduser", usuario.getIduser());
        list = q.getResultList();
        
        element = list.size();
        
        for (int i=0; i<element ;i++){
            
            pro=list.get(i);
            proyectoNombres.add(i, pro.getIdproyecto());
        }
        
        
        return proyectoNombres;
    }

    public void setProyectoNombres(List<String> proyectoNombres) {
        this.proyectoNombres = proyectoNombres;
    }

       
    public Proyecto getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(Proyecto seleccion) {
        this.seleccion = seleccion;
    }

    public Integer getIdproyecto() {
        return idproyecto;
    }

    public void setIdproyecto(Integer idproyecto) {
        this.idproyecto = idproyecto;
    }

    
    
    public List<Proyecto> getList() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        User usuario = ((SessionBean)(context.getApplication().evaluateExpressionGet(
                    context, "#{sessionBean}", Object.class))).getUser();
       
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        EntityManager em = emf.createEntityManager();
        
        Query q = em.createNamedQuery("Proyecto.findByAllproyectoUser");
        q.setParameter("iduser", usuario.getIduser());
        list = q.getResultList();
        
        //list = usuario.getProyectoList();
        return list;
        
        
       /* EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("Proyecto.findAll");
        list = q.getResultList();*/
        
    
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
  