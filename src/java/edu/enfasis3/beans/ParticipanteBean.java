/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.beans;

import edu.enfasis3.entity.Participante;
import edu.enfasis3.entity.ParticipanteJpaController;
import edu.enfasis3.entity.Proyecto;
import edu.enfasis3.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
@RequestScoped
public class ParticipanteBean {

    private List <Participante> participantes;
    private Participante participanteSele;
    private Integer idProyectoSeleccion;
    
    
    private Proyecto proyectoSeleccion;
    private List <Proyecto> listaProyectoPart;
    
   
   
    private Integer iduser;
    private Integer idproyecto;
    
    public ParticipanteBean() {
    }

    public Participante getParticipanteSele() {
        return participanteSele;
    }

    public void setParticipanteSele(Participante participanteSele) {
        this.participanteSele = participanteSele;
    }

    public Integer getIduser() {
        return iduser;
    }

    public void setIduser(Integer iduser) {
        this.iduser = iduser;
    }

    public Integer getIdproyecto() {
        return idproyecto;
    }

    public void setIdproyecto(Integer idproyecto) {
        this.idproyecto = idproyecto;
    }
    
    public Integer getIdProyectoSeleccion() {
        return idProyectoSeleccion;
    }

    public void setIdProyectoSeleccion(Integer idProyectoSeleccion) {
        this.idProyectoSeleccion = idProyectoSeleccion;
    }
  
    
    public Proyecto getProyectoSeleccion() {
        return proyectoSeleccion;
    }

    public void setProyectoSeleccion(Proyecto proyectoSeleccion) {
        this.proyectoSeleccion = proyectoSeleccion;
    }

    
    
    public List<Proyecto> getListaProyectoPart() {
        
        int elemt;
        Participante part;
        Proyecto pro;
        listaProyectoPart = new ArrayList<>();
        
      
        FacesContext context = FacesContext.getCurrentInstance();
        User usuario = ((SessionBean)(context.getApplication().evaluateExpressionGet(
                    context, "#{sessionBean}", Object.class))).getUser();
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        EntityManager em = emf.createEntityManager();
        
        Query q = em.createNamedQuery("Participante.findListaParticipante");
        q.setParameter("iduser", usuario.getIduser());
        
        List participant = q.getResultList();
        
        elemt = participant.size();
        
        for(int i=0;i<elemt;i++){
        
            part = (Participante) participant.get(i);
            pro = part.getIdproyecto();
            listaProyectoPart.add(i, pro);
                    
        }
                
        return listaProyectoPart;
    }

    public void setListaProyectoPart(List<Proyecto> listaProyectoPart) {
        this.listaProyectoPart = listaProyectoPart;
    }
   

    public List <Participante> getParticipantes() {
       
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        EntityManager em = emf.createEntityManager();
        
        Query q = em.createNamedQuery("Participante.findUsuariosEnProyecto");
        q.setParameter("idproyecto", idProyectoSeleccion);
        
        participantes = q.getResultList();
        
        return participantes;
    }

    public void setParticipant(List<Participante> participantes) {
        this.participantes = participantes;
    }
    
    public void eliminarParticipante(){
    
       
        if(participanteSele != null){
            System.out.println("NO ES NULO");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        ParticipanteJpaController pjc = new ParticipanteJpaController(emf);
        
        
        
         try {
                
                pjc.destroy(participanteSele.getIdparticipante());
            
        } catch(Exception e) {
            System.out.println(e);
        }
        }
        
        else {
        
            System.out.println("ES NULO");
        
        }
        
    
       
    }
     public void crearParticipante(){
           
        }
     public void guardarParticipante(){
        
            
        }
     public void nuevoParticipante(){
         
         participanteSele= new Participante();
           
     }
     
    
}
