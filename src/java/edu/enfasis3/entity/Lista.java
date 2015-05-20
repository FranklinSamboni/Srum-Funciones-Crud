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
@Table(name = "public.lista")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lista.findAll", query = "SELECT l FROM Lista l"),
    @NamedQuery(name = "Lista.findByIdlista", query = "SELECT l FROM Lista l WHERE l.idlista = :idlista"),
    @NamedQuery(name = "Lista.findByNameLista", query = "SELECT l FROM Lista l WHERE l.nameLista = :nameLista"),
    @NamedQuery(name = "Lista.findByObservationLista", query = "SELECT l FROM Lista l WHERE l.observationLista = :observationLista"),
    @NamedQuery(name = "Lista.findByRegistrationdateLista", query = "SELECT l FROM Lista l WHERE l.registrationdateLista = :registrationdateLista"),
    @NamedQuery(name = "Lista.findByUpdatedateLista", query = "SELECT l FROM Lista l WHERE l.updatedateLista = :updatedateLista")})
public class Lista implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idlista")
    private Integer idlista;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "name_lista")
    private String nameLista;
    @Size(max = 2147483647)
    @Column(name = "observation_lista")
    private String observationLista;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registrationdate_lista")
    @Temporal(TemporalType.DATE)
    private Date registrationdateLista;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updatedate_lista")
    @Temporal(TemporalType.DATE)
    private Date updatedateLista;
    @JoinColumn(name = "idproyecto", referencedColumnName = "idproyecto")
    @ManyToOne(optional = false)
    private Proyecto idproyecto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idlista")
    private List<Actividad> actividadList;

    public Lista() {
    }

    public Lista(Integer idlista) {
        this.idlista = idlista;
    }

    public Lista(Integer idlista, String nameLista, Date registrationdateLista, Date updatedateLista) {
        this.idlista = idlista;
        this.nameLista = nameLista;
        this.registrationdateLista = registrationdateLista;
        this.updatedateLista = updatedateLista;
    }

    public Integer getIdlista() {
        return idlista;
    }

    public void setIdlista(Integer idlista) {
        this.idlista = idlista;
    }

    public String getNameLista() {
        return nameLista;
    }

    public void setNameLista(String nameLista) {
        this.nameLista = nameLista;
    }

    public String getObservationLista() {
        return observationLista;
    }

    public void setObservationLista(String observationLista) {
        this.observationLista = observationLista;
    }

    public Date getRegistrationdateLista() {
        return registrationdateLista;
    }

    public void setRegistrationdateLista(Date registrationdateLista) {
        this.registrationdateLista = registrationdateLista;
    }

    public Date getUpdatedateLista() {
        return updatedateLista;
    }

    public void setUpdatedateLista(Date updatedateLista) {
        this.updatedateLista = updatedateLista;
    }

    public Proyecto getIdproyecto() {
        return idproyecto;
    }

    public void setIdproyecto(Proyecto idproyecto) {
        this.idproyecto = idproyecto;
    }

    @XmlTransient
    public List<Actividad> getActividadList() {
        return actividadList;
    }

    public void setActividadList(List<Actividad> actividadList) {
        this.actividadList = actividadList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idlista != null ? idlista.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lista)) {
            return false;
        }
        Lista other = (Lista) object;
        if ((this.idlista == null && other.idlista != null) || (this.idlista != null && !this.idlista.equals(other.idlista))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.enfasis3.entity.Lista[ idlista=" + idlista + " ]";
    }
    
}
