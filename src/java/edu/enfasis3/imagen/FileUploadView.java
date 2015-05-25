/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.imagen;



import edu.enfasis3.beans.SessionBean;
import edu.enfasis3.entity.User;
import edu.enfasis3.entity.UserJpaController;
import edu.enfasis3.entity.exceptions.RollbackFailureException;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.swing.JOptionPane;
 
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author USER
 */
@ManagedBean
public class FileUploadView {
     
    private UploadedFile file;
    private UploadedFile photo;
    private byte[] binPhoto;
    
   
    int longitudBytes;
    private String destination="D:\\Universidad\\Enfasis 3\\ProyectoEnfasis\\SCRUMproyecto\\web\\fotos";
    private User usuario;    //es el selected user 
    private InputStream is;
    
   public FileUploadView(){
    FacesContext context = FacesContext.getCurrentInstance();
    usuario = ((SessionBean)(context.getApplication().evaluateExpressionGet(
                            context, "#{sessionBean}", Object.class))).getUser();
}

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
   
    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        System.out.println("fijo set de file");
        this.file = file;
    }

    public UploadedFile getPhoto() {
        return photo;
    }

    public void setPhoto(UploadedFile photo) {
        this.photo = photo;
    }

    public byte[] getBinPhoto() {
        return binPhoto;
    }

    public void setBinPhoto(byte[] binPhoto) {
        this.binPhoto = binPhoto;
    }
    
    
    
     public void uploadPhoto(FileUploadEvent event) {
        photo = event.getFile();
        System.out.println("[Debug] Archivo recibido: "+photo.getFileName());     
   
              

                  EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
                  UserJpaController pjc = new UserJpaController(emf);
                  System.out.println("subio del usuario"+ usuario.getIduser());
                
               
                    String filePhoto;
                    OutputStream out =null;
                    InputStream archivo=null;
                    int longi=0;
                     
                    if (photo != null) {
                    System.out.println("[Debug]photo != null");
                    }                    
                     try {
           
                            archivo = photo.getInputstream();       
                //********crear archivo en la carpeta destination************+*********
                            out = new FileOutputStream(new File(destination +"/"+usuario.getIduser()+".png"));
                                int read = 0;

                                byte[] bytes = new byte[1024];//buffer donde se dejara la lectura


                                while ((read = archivo.read(bytes)) != -1) {
                                    out.write(bytes, 0, read);
                                    longi=longi+1;     
                                } 
                                archivo.close();
                                out.flush();
                                out.close();
                                System.out.println("la longitud es: "+longi);
                               }catch (IOException e) {e.printStackTrace();}
 //**************************obtener la longitud en bytes del archivo***********
                  
                      System.out.println("convertir tmaño: "+photo.getSize());
                   
                    filePhoto = Base64.encode(photo.getContents(), 0, longi,null).toString();
                    System.out.println("el archivo file foto es: "+filePhoto);
                    usuario.setPhoto(filePhoto);
                    
           
            try { 
                    pjc.edit(usuario);
                
            } catch(Exception e) {System.out.println("no sube a la base da datos: "+e);}
           
    }
     
     
     /*
         public void save() {
                System.out.println("[entro a metodo save");
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
                UserJpaController pjc = new UserJpaController(emf);

                

        String filePhoto;
     

       
            try {
                if (photo != null) {
                    System.out.println("[Debug]photo != null");
                   // filePhoto = Base64.encode(photo.getContents());
                  //  usuario.setPhoto(filePhoto);
                   // pjc.edit(usuario);
                   
                   // System.out.println("[Debug] Archivo "+photo.getFileName()+" codificado y guardado.");
                }
      
            } catch(Exception e) {System.out.println("no sube a la base da datos: "+e);}
           
          }*/
      
    
    
    
    
 
 /*  public void upload() throws RollbackFailureException, Exception {
       
        String nombre_img, conversion,fie;
        InputStream archivo=null;
        OutputStream out =null;
         int longitudBytes;
        
         FacesContext context = FacesContext.getCurrentInstance();
            User user = ((SessionBean)(context.getApplication().evaluateExpressionGet
        (context, "#{sessionBean}", Object.class))).getUser();
            
           
        try {
            nombre_img = file.getFileName();
            archivo = file.getInputstream();
           conversion=file.getContentType();
           
           System.out.println("getContentType "+conversion+" en byte es: ");
           
          
            
//********crear archivo en la carpeta destination************+*********
            out = new FileOutputStream(new File(destination +"/"+user.getIduser()+".png"));
                int read = 0;
                 int longi=0;
                byte[] bytes = new byte[1024];//buffer donde se dejara la lectura
                
               
                while ((read = archivo.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                    longi=longi+1;     
                } 
                archivo.close();
                out.flush();
                out.close();
                 System.out.println("New file created! "+archivo);
                System.out.println("la longitud es: "+longi);
                System.out.println("el iduser de usuario es "+user.getIduser());
               }catch (IOException e) {e.printStackTrace();}
 
    }//fin upload
   // convert InputStream to String
   /**************************convertir InputStream a String************************* */
            /*  InputStream is = new ByteArrayInputStream("file content..blah blah".getBytes());
                String result = getStringFromInputStream(is);
		System.out.println(result);
		System.out.println("Done");*/
 /*********************************************************************************/  
	
   
    
}