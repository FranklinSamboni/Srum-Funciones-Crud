/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.beans;

import edu.enfasis3.entity.Proyecto;
import edu.enfasis3.entity.User;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Frank
 */
@ManagedBean
@RequestScoped
public class ParticipanteBean {

    private User usuario;
    private Proyecto proyecto;
    
    
   
    public ParticipanteBean() {
    }
    
    public List sessionParticipante(){
    
        FacesContext context = FacesContext.getCurrentInstance();
        usuario = ((SessionBean)(context.getApplication().evaluateExpressionGet(
                    context, "#{sessionBean}", Object.class))).getUser();
        
        List<Proyecto> list = usuario.getProyectoList();
        return list;
        
    }
    
}
