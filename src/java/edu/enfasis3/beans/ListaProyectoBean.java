/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.beans;


import edu.enfasis3.entity.Proyecto;
import edu.enfasis3.entity.ProyectoJpaController;
import edu.enfasis3.entity.User;
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
    
    private String nombrePro;
    private String descripcion;
    private Date inicio;
    private Date fin;
    private Date registro;
    private Date actualizacion;
    
    public ListaProyectoBean() {
    }

    public String getNombrePro() {
        return nombrePro;
    }

    public void setNombrePro(String nombrePro) {
        this.nombrePro = nombrePro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public Date getRegistro() {
        return registro;
    }

    public void setRegistro(Date registro) {
        this.registro = registro;
    }

    public Date getActualizacion() {
        return actualizacion;
    }

    public void setActualizacion(Date actualizacion) {
        this.actualizacion = actualizacion;
    }
    
    public Proyecto getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(Proyecto seleccion) {
        this.seleccion = seleccion;
    }


    public List<Proyecto> getList() {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("Proyecto.findAll");
        list = q.getResultList();
        
        return list;
    }
    

    public void setList(List<Proyecto> list) {
        this.list = list;
    }
    
    public void nuevoProyecto(){
       seleccion = new Proyecto();
    }
    
    
    public void crearProyecto(){
        
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
    
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        ProyectoJpaController pjc = new ProyectoJpaController(emf);
        
        System.out.println("NOMBRE PROYECTO MODIFICADO: "+ seleccion.getNameProyecto());
        
        try {
            
            pjc.edit(seleccion);
            
            
        } catch(Exception e) {
            System.out.println(e);
        }
        
    }
    
}
  