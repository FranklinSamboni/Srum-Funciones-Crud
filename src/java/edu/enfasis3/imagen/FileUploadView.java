/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.imagen;


import edu.enfasis3.beans.RegistrarBean;
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
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
    //FileInputStream fis;
   
    int longitudBytes;
    private String destination="D:\\Universidad\\Enfasis 3\\ProyectoEnfasis\\SCRUMproyecto\\web\\fotos";
    private User user;     
    private InputStream is;

    
    
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
 
   public void upload() throws RollbackFailureException, Exception {
        if(file != null) {         
            System.out.println("exitooo "+file.getFileName()+"  se subio"); 
        }
        
        String nombre_img, conversion64;
        InputStream archivo=null;
        OutputStream out =null;
         int longitudBytes;
        
         FacesContext context = FacesContext.getCurrentInstance();
            User user = ((SessionBean)(context.getApplication().evaluateExpressionGet
        (context, "#{sessionBean}", Object.class))).getUser();
            
           
        try {
            nombre_img = file.getFileName();
            archivo = file.getInputstream();
            
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
                System.out.println("bytes es: "+bytes);
                System.out.println("el iduser de usuario es "+user.getIduser());
/**************************convertir InputStream a String************************* */
          
            //    byte [] conver=new byte[1024];
             //    conver=getBytes(archivo);
            // System.out.println("la conversion es: " + conver);
 /*********************************************************************************/               
                
                
          /*      
                
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
            UserJpaController pjc = new UserJpaController(emf);
            
            user.setPhoto(file.toString());//meter archivo tipo String
            pjc.edit(user);*/
              
               
             //obtener el usuario   
                
           
         
           
           }catch (IOException e) {e.printStackTrace();}
           
          //user.setPhoto(is);
          
                
           
                
           /*     try{
            String sql="INSERT INTO \"user\" WHERE iduser=20";//(name_user, lastname_user, docid, username, password, email, iduser, photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps=conexion.getConexion().prepareStatement(sql);
            ps.setString(1,"heyman");
            ps.setString(2,"coral");
            ps.setString(3,"1061778712");
            ps.setString(4,"heyn");
            ps.setString(5,"pass");
            ps.setString(6,"aksnmak");
           ps.setInt(7,20);
            
            //ps.setString(2,txtnombre.getText());
            ps.setBinaryStream(8,fis,longitudBytes);//file input string
            
            ps.execute();
            ps.close();

           
   
            //JOptionPane.showMessageDialog(rootPane,"Guardado correctamente");
            System.out.println("Guardado correctamente");
        }catch(SQLException | NumberFormatException | HeadlessException x){
           // JOptionPane.showMessageDialog(rootPane, "exception 2 "+x);
             System.out.println("excepcion 2"+x);
        }           // TODO add your handling code here:
                
            */
               
            
            
            
            
        
    
    }//fin upload
   // convert InputStream to String
   /**************************convertir InputStream a String************************* */
            /*  InputStream is = new ByteArrayInputStream("file content..blah blah".getBytes());
                String result = getStringFromInputStream(is);
		System.out.println(result);
		System.out.println("Done");*/
 /*********************************************************************************/  
	
        
public static byte[] getBytes(InputStream is) throws IOException {

    int len;
    int size = 1024;
    byte[] buf;

    if (is instanceof ByteArrayInputStream) {
      size = is.available();
      buf = new byte[size];
      len = is.read(buf, 0, size);
    } else {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      buf = new byte[size];
      while ((len = is.read(buf, 0, size)) != -1)
        bos.write(buf, 0, len);
      buf = bos.toByteArray();
    }
    return buf;
  }
   
    
}