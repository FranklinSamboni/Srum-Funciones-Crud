/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Frank
 */
@Entity
@Table(name = "public.comentario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comentario.findAll", query = "SELECT c FROM Comentario c"),
    @NamedQuery(name = "Comentario.findByComentario", query = "SELECT c FROM Comentario c JOIN c.idactividad AS a JOIN a.idlista AS l JOIN l.idproyecto AS p JOIN p.manager AS u WHERE u.iduser = :iduser"),
    @NamedQuery(name = "Comentario.findByComentarioListaAc", query = "SELECT c FROM Comentario c JOIN c.idactividad AS a WHERE a.idactividad = :idactividad"),
   
    
    @NamedQuery(name = "Comentario.findByIdcomentario", query = "SELECT c FROM Comentario c WHERE c.idcomentario = :idcomentario"),
    @NamedQuery(name = "Comentario.findByNameComentario", query = "SELECT c FROM Comentario c WHERE c.nameComentario = :nameComentario"),
    @NamedQuery(name = "Comentario.findByDescriptionComentario", query = "SELECT c FROM Comentario c WHERE c.descriptionComentario = :descriptionComentario"),
    @NamedQuery(name = "Comentario.findByRegistrationdateComentario", query = "SELECT c FROM Comentario c WHERE c.registrationdateComentario = :registrationdateComentario")})
public class Comentario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcomentario")
    private Integer idcomentario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "name_comentario")
    private String nameComentario;
    @Size(max = 2147483647)
    @Column(name = "description_comentario")
    private String descriptionComentario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registrationdate_comentario")
    @Temporal(TemporalType.DATE)
    private Date registrationdateComentario;
    @JoinColumn(name = "idactividad", referencedColumnName = "idactividad")
    @ManyToOne(optional = false)
    private Actividad idactividad;

    public Comentario() {
    }

    public Comentario(Integer idcomentario) {
        this.idcomentario = idcomentario;
    }

    public Comentario(Integer idcomentario, String nameComentario, Date registrationdateComentario) {
        this.idcomentario = idcomentario;
        this.nameComentario = nameComentario;
        this.registrationdateComentario = registrationdateComentario;
    }

    public Integer getIdcomentario() {
        return idcomentario;
    }

    public void setIdcomentario(Integer idcomentario) {
        this.idcomentario = idcomentario;
    }

    public String getNameComentario() {
        return nameComentario;
    }

    public void setNameComentario(String nameComentario) {
        this.nameComentario = nameComentario;
    }

    public String getDescriptionComentario() {
        return descriptionComentario;
    }

    public void setDescriptionComentario(String descriptionComentario) {
        this.descriptionComentario = descriptionComentario;
    }

    public Date getRegistrationdateComentario() {
        return registrationdateComentario;
    }

    public void setRegistrationdateComentario(Date registrationdateComentario) {
        this.registrationdateComentario = registrationdateComentario;
    }

    public Actividad getIdactividad() {
        return idactividad;
    }

    public void setIdactividad(Actividad idactividad) {
        this.idactividad = idactividad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcomentario != null ? idcomentario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comentario)) {
            return false;
        }
        Comentario other = (Comentario) object;
        if ((this.idcomentario == null && other.idcomentario != null) || (this.idcomentario != null && !this.idcomentario.equals(other.idcomentario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.enfasis3.entity.Comentario[ idcomentario=" + idcomentario + " ]";
    }
    
}
