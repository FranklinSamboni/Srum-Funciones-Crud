/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.beans;


import edu.enfasis3.entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Frank
 */
@ManagedBean
@SessionScoped

public class SessionBean implements Serializable {
    private String login;
    private String password;
    private boolean validation = false;
    private User user;

    

    public SessionBean() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isValidation() {
        return validation;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }

    public String validate() {
        String page;
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("User.findByUsername");
        q.setParameter("username", login);
        List res = q.getResultList();
        
        FacesContext context = FacesContext.getCurrentInstance();
        if (!res.isEmpty()) {
            user = (User)res.get(0);
            System.out.println("El loguin y password es: "+ user.getUsername()+"  "+user.getPassword());
            //System.out.println("la contraseña es :" + user.getPassword());
        
            if (password.equals(user.getPassword())) {
                validation = true;
                page="Principal"+"?faces-redirect=true";
            } else {
                validation = false;
                context.addMessage(null, new FacesMessage("Contraseña incorrecta",  "Intentelo nuevamente") );
                page="index";
            }
        } else {
            validation = false;
            context.addMessage(null, new FacesMessage("El nombre de usuario o contraseña estan mal",  "Intentelo nuevamente") );
            page="index";
        }
        return page;
    }
    
    public String logout() throws IOException {
        validation=false;
        return "index?faces-redirect=true";
}

    
}