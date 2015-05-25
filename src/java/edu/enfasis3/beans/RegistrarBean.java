/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.beans;


import edu.enfasis3.GeneradoContraseñas.PasswordGenerator;
import edu.enfasis3.correo.EnviarCorreoScrum;
import edu.enfasis3.entity.User;

import edu.enfasis3.entity.UserJpaController;
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
public class RegistrarBean {

    
    private String nombre;
    private String apellido;
    private String identidad;
    private String nombreusuario;
    private String contraseña;
    private String correo;
    private User user;
    
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getIdentidad() {
        return identidad;
    }

    public void setIdentidad(String identidad) {
        this.identidad = identidad;
    }

    public String getNombreusuario() {
        return nombreusuario;
    }

    public void setNombreusuario(String nombreusuario) {
        this.nombreusuario = nombreusuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    
    
    public void verificar() {
        
         EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
         EntityManager em = emf.createEntityManager();
         
         Query q = em.createNamedQuery("User.findByEmail");
         q.setParameter("email", correo);
        
         List listaCorreo = q.getResultList();
         
         if(listaCorreo.isEmpty()){ // el correo no existe
           
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion registrada", "Se ha enviado un correo a la direccion de correo ingresada."));
                user = new User();
                user.setName(nombre);
                user.setLastname(apellido);
                user.setDocid(identidad);
                user.setEmail(correo);
                user.setUsername(nombreusuario);

                contraseña = PasswordGenerator.getPassword(PasswordGenerator.NUMEROS+
                     PasswordGenerator.MINUSCULAS+
                     PasswordGenerator.MAYUSCULAS+
                     PasswordGenerator.ESPECIALES,10);
        
        user.setPassword(contraseña);
         
        EnviarCorreoScrum send = new EnviarCorreoScrum(user.getEmail(),user.getPassword());
        send.enviarCorreo();
        
        UserJpaController pjc = new UserJpaController(emf);
        try {
            //project.set
            //pjc.create(project);
            
            pjc.create(user);
        } catch(Exception e) {
            System.out.println(e);
            }
        
        }
            
         else{
        
         user = (User) listaCorreo.get(0);
         
         if(user.getName().equals("0000default") && user.getEmail().equals(correo)){ // se editan las caracteristicas del usuario por defecto
         
                user.setName(nombre);
                user.setLastname(apellido);
                user.setDocid(identidad);
                user.setUsername(nombreusuario);

                contraseña = PasswordGenerator.getPassword(PasswordGenerator.NUMEROS+
                     PasswordGenerator.MINUSCULAS+
                     PasswordGenerator.MAYUSCULAS+
                     PasswordGenerator.ESPECIALES,10);
        
                user.setPassword(contraseña);
         
             UserJpaController pjc = new UserJpaController(emf);
              try {
        
                pjc.edit(user);
                } catch(Exception e) {
                System.out.println(e);
                }
         
         }
         else{
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ese correo ", "Ya esta registrado"));
         }
         
         }
    }
}
