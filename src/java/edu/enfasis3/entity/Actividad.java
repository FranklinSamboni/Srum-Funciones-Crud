/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Frank
 */
@Entity
@Table(name = "public.actividad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Actividad.findAll", query = "SELECT a FROM Actividad a"),
    @NamedQuery(name = "Actividad.findByIdactividad", query = "SELECT a FROM Actividad a WHERE a.idactividad = :idactividad"),
    @NamedQuery(name = "Actividad.findByNameActividad", query = "SELECT a FROM Actividad a WHERE a.nameActividad = :nameActividad"),
    @NamedQuery(name = "Actividad.findByDescriptionActividad", query = "SELECT a FROM Actividad a WHERE a.descriptionActividad = :descriptionActividad"),
    @NamedQuery(name = "Actividad.findByBegindateActividad", query = "SELECT a FROM Actividad a WHERE a.begindateActividad = :begindateActividad"),
    @NamedQuery(name = "Actividad.findByEnddateActividad", query = "SELECT a FROM Actividad a WHERE a.enddateActividad = :enddateActividad"),
    @NamedQuery(name = "Actividad.findByRegistrariondateActividad", query = "SELECT a FROM Actividad a WHERE a.registrariondateActividad = :registrariondateActividad"),
    @NamedQuery(name = "Actividad.findByUpdatedateActividad", query = "SELECT a FROM Actividad a WHERE a.updatedateActividad = :updatedateActividad")})
public class Actividad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idactividad")
    private Integer idactividad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "name_actividad")
    private String nameActividad;
    @Size(max = 2147483647)
    @Column(name = "description_actividad")
    private String descriptionActividad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "begindate_actividad")
    @Temporal(TemporalType.DATE)
    private Date begindateActividad;
    @Column(name = "enddate_actividad")
    @Temporal(TemporalType.DATE)
    private Date enddateActividad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registrariondate_actividad")
    @Temporal(TemporalType.DATE)
    private Date registrariondateActividad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updatedate_actividad")
    @Temporal(TemporalType.DATE)
    private Date updatedateActividad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idactividad")
    private List<Comentario> comentarioList;
    @JoinColumn(name = "idlista", referencedColumnName = "idlista")
    @ManyToOne(optional = false)
    private Lista idlista;

    public Actividad() {
    }

    public Actividad(Integer idactividad) {
        this.idactividad = idactividad;
    }

    public Actividad(Integer idactividad, String nameActividad, Date begindateActividad, Date registrariondateActividad, Date updatedateActividad) {
        this.idactividad = idactividad;
        this.nameActividad = nameActividad;
        this.begindateActividad = begindateActividad;
        this.registrariondateActividad = registrariondateActividad;
        this.updatedateActividad = updatedateActividad;
    }

    public Integer getIdactividad() {
        return idactividad;
    }

    public void setIdactividad(Integer idactividad) {
        this.idactividad = idactividad;
    }

    public String getNameActividad() {
        return nameActividad;
    }

    public void setNameActividad(String nameActividad) {
        this.nameActividad = nameActividad;
    }

    public String getDescriptionActividad() {
        return descriptionActividad;
    }

    public void setDescriptionActividad(String descriptionActividad) {
        this.descriptionActividad = descriptionActividad;
    }

    public Date getBegindateActividad() {
        return begindateActividad;
    }

    public void setBegindateActividad(Date begindateActividad) {
        this.begindateActividad = begindateActividad;
    }

    public Date getEnddateActividad() {
        return enddateActividad;
    }

    public void setEnddateActividad(Date enddateActividad) {
        this.enddateActividad = enddateActividad;
    }

    public Date getRegistrariondateActividad() {
        return registrariondateActividad;
    }

    public void setRegistrariondateActividad(Date registrariondateActividad) {
        this.registrariondateActividad = registrariondateActividad;
    }

    public Date getUpdatedateActividad() {
        return updatedateActividad;
    }

    public void setUpdatedateActividad(Date updatedateActividad) {
        this.updatedateActividad = updatedateActividad;
    }

    @XmlTransient
    public List<Comentario> getComentarioList() {
        return comentarioList;
    }

    public void setComentarioList(List<Comentario> comentarioList) {
        this.comentarioList = comentarioList;
    }

    public Lista getIdlista() {
        return idlista;
    }

    public void setIdlista(Lista idlista) {
        this.idlista = idlista;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idactividad != null ? idactividad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Actividad)) {
            return false;
        }
        Actividad other = (Actividad) object;
        if ((this.idactividad == null && other.idactividad != null) || (this.idactividad != null && !this.idactividad.equals(other.idactividad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.enfasis3.entity.Actividad[ idactividad=" + idactividad + " ]";
    }
    
}
