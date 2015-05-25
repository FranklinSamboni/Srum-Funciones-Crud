/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.beans;

import edu.enfasis3.correo.EnviarCorreoScrum;
import edu.enfasis3.entity.Participante;
import edu.enfasis3.entity.ParticipanteJpaController;
import edu.enfasis3.entity.Proyecto;
import edu.enfasis3.entity.User;
import edu.enfasis3.entity.UserJpaController;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Frank
 */
@ManagedBean
@RequestScoped
public class ParticipanteBean {

    private Integer idProyectoSeleccion; // selecciona un proyecto del usuario. El boton que despliega las opciones
    
    private List <User> participantes; // guarda la lista de participantes a un proyecto
    private User participanteSele; // selecciona un participante.
        
    private Proyecto proyectoSeleccion; // selecciona un proyecto en "Proyecto.xhtml" 
    private List <Proyecto> listaProyectoPart; // guarda la lista de proyectos en los que el usuario participa
    
    private String correo;
    private Integer invitacion;
    
    
    
    public ParticipanteBean() {
    }

    public String getCorreo() {
        return correo;
    }

    public Integer getInvitacion() {
        return invitacion;
    }

    public void setInvitacion(Integer invitacion) {
        this.invitacion = invitacion;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public User getParticipanteSele() {
        return participanteSele;
    }

    public void setParticipanteSele(User participanteSele) {
        this.participanteSele = participanteSele;
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
   

    public List <User> getParticipantes() {
        
        participantes = new ArrayList<>();
       
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        EntityManager em = emf.createEntityManager();
        
        Query q = em.createNamedQuery("Participante.findUsuariosEnProyecto");
        q.setParameter("idproyecto", idProyectoSeleccion);
       
        List<Participante> ListParti  = q.getResultList();
        Participante parti;
        
        int element = ListParti.size();
        
        for(int i=0; i<element ;i++){
            
            parti = ListParti.get(i);
            participantes.add(i, parti.getIduser());
            
        }
                
        
        return participantes;
    }

    public void setParticipant(List<User> participantes) {
        this.participantes = participantes;
    }
    
    public void eliminarParticipante(){
    
       
        System.out.println("NO ES NULO");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        ParticipanteJpaController pjc = new ParticipanteJpaController(emf);
        
        
        
         try {
                
                pjc.destroy(7);
            
        } catch(Exception e) {
            System.out.println(e);
        }
        
        
          System.out.println("ES NULO");
       
        
    
       
    }
    
    public void crearParticipante(){
        
        System.out.println("coRREO =" + correo);
           
         if(correo!=null){
             
         invitacion = idProyectoSeleccion; // se captura el id del proyecto
         EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
         EntityManager em = emf.createEntityManager();
         
         Query q = em.createNamedQuery("User.findByEmail");
         q.setParameter("email", correo); 
         List listaCorreo = q.getResultList(); // se obtiene un usuario que coincida con el correo o si no existe es nulo
         
         
         User user ;
         UserJpaController pjc = new UserJpaController(emf);
        
         EnviarCorreoScrum send = new EnviarCorreoScrum(correo,"");
             
         if(listaCorreo.isEmpty()){ // Correo no esta registrado
             
              send.enviarCorreoNoRegistrado();
               user = new User();
              user.setDocid("0000default");
              user.setEmail(correo);
              user.setLastname("0000default");
              user.setName("0000default");
              user.setPassword("0000default");
              user.setUsername("0000default");
              System.out.println("idProyectoSeleccion: "+ idProyectoSeleccion);
              
              user.setInvitacion(idProyectoSeleccion);
              
          
              try {
                    pjc.create(user);  // se crea un usuario por defecto que luego es modificado en el registro
                } catch(Exception e) {
                    System.out.println(e);
                }
             }
         
             else{ // Correo registrado
             user = (User) listaCorreo.get(0); // Se envia un invitacion y se asigna un proyecto.
             user.setInvitacion(idProyectoSeleccion);
             try {
                    pjc.edit(user);
                } catch(Exception e) {
                    System.out.println(e);
                }
             
             send.enviarCorreoRegistrado();
             
         }
       }
         else{
             System.out.println("coRREO =" + correo);
         }
     }
    
    
    public String aceptarInvitacion() throws Exception{
        
            Proyecto proyectoInvitado;
            FacesContext context = FacesContext.getCurrentInstance();
            User usuario = ((SessionBean)(context.getApplication().evaluateExpressionGet(
                    context, "#{sessionBean}", Object.class))).getUser();
            
    
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
            EntityManager em = emf.createEntityManager();
            
            Query q = em.createNamedQuery("Proyecto.findByIdproyecto"); // se obtiene el proyecto al que fue invitado
            q.setParameter("idproyecto", usuario.getInvitacion()); 
            List listInvitado = q.getResultList(); // se obtiene un Proyecto que coincida con el id del proyecto invitado
            
            proyectoInvitado = (Proyecto) listInvitado.get(0);
            
            Participante ParticipanteInvitado = new Participante();
            ParticipanteInvitado.setIduser(usuario);
            ParticipanteInvitado.setIdproyecto(proyectoInvitado);
            
            ParticipanteJpaController injpa = new ParticipanteJpaController(emf);
            UserJpaController upa = new UserJpaController(emf);
            usuario.setInvitacion(0); // se borra la notificacion de invitacion.
            upa.edit(usuario);
            try{
            
                
            injpa.create(ParticipanteInvitado);
            
                return "Principal?faces-redirect=true";
            }catch(Exception e)  {
                System.out.println(e);
            return "Error?faces-redirect=true";
            }
            
        }
    
    public String rechazarInvitacion(){ // se elimina la invitacion
    
        FacesContext context = FacesContext.getCurrentInstance();
            User usuario = ((SessionBean)(context.getApplication().evaluateExpressionGet(
                    context, "#{sessionBean}", Object.class))).getUser();
    
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
            UserJpaController pjc = new UserJpaController(emf);
            usuario.setInvitacion(0);
              
          
              try {
                    pjc.edit(usuario);  // se crea un usuario por defecto que luego es modificado en el registro
                    return "Principal?faces-redirect=true";
            
              } catch(Exception e) {
                    System.out.println(e);
                return "Error?faces-redirect=true";
            
                }
    }
    
    
    public void crearInvitacion(){
    
        if(idProyectoSeleccion!=null){
            
            System.out.println("Proyecto seleccionado :"+ idProyectoSeleccion);
            
        }
        else{
            System.out.println("No se selecciono proyecto"+ idProyectoSeleccion);
            
        
        }
        
    }
    
}
