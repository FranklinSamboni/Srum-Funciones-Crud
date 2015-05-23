/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.beans;

import edu.enfasis3.entity.User;
import edu.enfasis3.entity.UserJpaController;
import edu.enfasis3.entity.exceptions.NonexistentEntityException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;



/**
 *
 * @author USER
 */
@ManagedBean
@RequestScoped

public class PerfilBean {

    private User usuario;
    private String images;
    private String password;
    private String contraseña;
    
    
    public PerfilBean() {
        FacesContext context = FacesContext.getCurrentInstance();
        usuario = ((SessionBean)(context.getApplication().evaluateExpressionGet
        (context, "#{sessionBean}", Object.class))).getUser();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    
    
    public User getUsuario() {
            
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
    
    public String getImages() {
        images="fotos/"+usuario.getIduser()+".png";
        System.out.println("get de images");
        
    return images;
    }
      public void setImages(String images) {
       
        this.images = images;
    }  

    public void comprobar () throws NonexistentEntityException{
    
        if (password.equals(usuario.getPassword())){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Contraseña Modificada", "exitos"));
        
            usuario.setPassword(contraseña);
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
            UserJpaController ujp = new UserJpaController(emf);
            
            try{
            ujp.edit(usuario);
            
            } catch(Exception e){
              System.out.println(e);
            }
        }
        else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "No se cambio", "exitos"));
        
        
        
        }
    
    }
      
}
