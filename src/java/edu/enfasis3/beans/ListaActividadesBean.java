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
    private Integer identi;
  
    public ListaActividadesBean() {
    }

    public Integer getIdenti() {
        return identi;
    }

    public void setIdenti(Integer identi) {
        this.identi = identi;
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
        //ProyectoJpaController pjc = new ProyectoJpaController(emf);
        
        Query q = em.createNamedQuery("Lista.findAll");
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
        proyectolista=pjc.findProyecto(identi);
        
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
    
    /* public String comprobacion(){
    
            FacesContext context = FacesContext.getCurrentInstance();
            Proyecto pro = ((ListaProyectoBean)(context.getApplication().evaluateExpressionGet(
                    context, "#{listaProyectoBean}", Object.class))).getSeleccion();
            
            if(pro !=null){
                
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
            ProyectoJpaController pjc = new ProyectoJpaController(emf);
            
            proyectolista=new Proyecto();
            proyectolista= pjc.findProyecto(pro.getIdproyecto());
            
            return "ListaActividades"+"?faces-redirect=true";
            
            }
            else {
            return "Proyectos";
            }
         }*/
}
