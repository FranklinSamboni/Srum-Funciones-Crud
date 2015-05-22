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
@Table(name = "public.proyecto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proyecto.findAll", query = "SELECT p FROM Proyecto p"),
 // @NamedQuery(name = "Lista.findByIdUser", query = "SELECT l FROM Lista l JOIN l.idproyecto AS p JOIN p.manager AS m WHERE m.iduser = :idUser"),
    @NamedQuery(name = "Proyecto.findByAllproyectoUser", query = "SELECT p FROM Proyecto p JOIN p.manager AS u WHERE u.iduser = :iduser"),
    
    @NamedQuery(name = "Proyecto.findByIdproyecto", query = "SELECT p FROM Proyecto p WHERE p.idproyecto = :idproyecto"),
    @NamedQuery(name = "Proyecto.findByNameProyecto", query = "SELECT p FROM Proyecto p WHERE p.nameProyecto = :nameProyecto"),
    @NamedQuery(name = "Proyecto.findByDescriptionProyecto", query = "SELECT p FROM Proyecto p WHERE p.descriptionProyecto = :descriptionProyecto"),
    @NamedQuery(name = "Proyecto.findByBegindateProyecto", query = "SELECT p FROM Proyecto p WHERE p.begindateProyecto = :begindateProyecto"),
    @NamedQuery(name = "Proyecto.findByEnddateProyecto", query = "SELECT p FROM Proyecto p WHERE p.enddateProyecto = :enddateProyecto"),
    @NamedQuery(name = "Proyecto.findByRegistrationdateProyecto", query = "SELECT p FROM Proyecto p WHERE p.registrationdateProyecto = :registrationdateProyecto"),
    @NamedQuery(name = "Proyecto.findByUpdatedateProyecto", query = "SELECT p FROM Proyecto p WHERE p.updatedateProyecto = :updatedateProyecto")})
public class Proyecto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idproyecto")
    private Integer idproyecto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "name_proyecto")
    private String nameProyecto;
    @Size(max = 2147483647)
    @Column(name = "description_proyecto")
    private String descriptionProyecto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "begindate_proyecto")
    @Temporal(TemporalType.DATE)
    private Date begindateProyecto;
    @Column(name = "enddate_proyecto")
    @Temporal(TemporalType.DATE)
    private Date enddateProyecto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registrationdate_proyecto")
    @Temporal(TemporalType.DATE)
    private Date registrationdateProyecto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updatedate_proyecto")
    @Temporal(TemporalType.DATE)
    private Date updatedateProyecto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idproyecto")
    private List<Lista> listaList;
    @JoinColumn(name = "manager", referencedColumnName = "iduser")
    @ManyToOne(optional = false)
    private User manager;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idproyecto")
    private List<Participante> participanteList;

    public Proyecto() {
    }

    public Proyecto(Integer idproyecto) {
        this.idproyecto = idproyecto;
    }

    public Proyecto(Integer idproyecto, String nameProyecto, Date begindateProyecto, Date registrationdateProyecto, Date updatedateProyecto) {
        this.idproyecto = idproyecto;
        this.nameProyecto = nameProyecto;
        this.begindateProyecto = begindateProyecto;
        this.registrationdateProyecto = registrationdateProyecto;
        this.updatedateProyecto = updatedateProyecto;
    }

    public Integer getIdproyecto() {
        return idproyecto;
    }

    public void setIdproyecto(Integer idproyecto) {
        this.idproyecto = idproyecto;
    }

    public String getNameProyecto() {
        return nameProyecto;
    }

    public void setNameProyecto(String nameProyecto) {
        this.nameProyecto = nameProyecto;
    }

    public String getDescriptionProyecto() {
        return descriptionProyecto;
    }

    public void setDescriptionProyecto(String descriptionProyecto) {
        this.descriptionProyecto = descriptionProyecto;
    }

    public Date getBegindateProyecto() {
        return begindateProyecto;
    }

    public void setBegindateProyecto(Date begindateProyecto) {
        this.begindateProyecto = begindateProyecto;
    }

    public Date getEnddateProyecto() {
        return enddateProyecto;
    }

    public void setEnddateProyecto(Date enddateProyecto) {
        this.enddateProyecto = enddateProyecto;
    }

    public Date getRegistrationdateProyecto() {
        return registrationdateProyecto;
    }

    public void setRegistrationdateProyecto(Date registrationdateProyecto) {
        this.registrationdateProyecto = registrationdateProyecto;
    }

    public Date getUpdatedateProyecto() {
        return updatedateProyecto;
    }

    public void setUpdatedateProyecto(Date updatedateProyecto) {
        this.updatedateProyecto = updatedateProyecto;
    }

    @XmlTransient
    public List<Lista> getListaList() {
        return listaList;
    }

    public void setListaList(List<Lista> listaList) {
        this.listaList = listaList;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    @XmlTransient
    public List<Participante> getParticipanteList() {
        return participanteList;
    }

    public void setParticipanteList(List<Participante> participanteList) {
        this.participanteList = participanteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idproyecto != null ? idproyecto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proyecto)) {
            return false;
        }
        Proyecto other = (Proyecto) object;
        if ((this.idproyecto == null && other.idproyecto != null) || (this.idproyecto != null && !this.idproyecto.equals(other.idproyecto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.enfasis3.entity.Proyecto[ idproyecto=" + idproyecto + " ]";
    }
    
}
