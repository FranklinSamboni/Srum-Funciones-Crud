/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Frank
 */
@Entity
@Table(name = "public.participante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Participante.findAll", query = "SELECT p FROM Participante p"),
    @NamedQuery(name = "Participante.findListaParticipante", query = "SELECT p FROM Participante p JOIN p.iduser AS u WHERE u.iduser = :iduser"),
    @NamedQuery(name = "Participante.findUsuariosEnProyecto", query = "SELECT p FROM Participante p JOIN p.idproyecto AS m WHERE m.idproyecto = :idproyecto"),
    
    @NamedQuery(name = "Participante.findByIdparticipante", query = "SELECT p FROM Participante p WHERE p.idparticipante = :idparticipante")})
public class Participante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idparticipante")
    private Integer idparticipante;
    @JoinColumn(name = "idproyecto", referencedColumnName = "idproyecto")
    @ManyToOne(optional = false)
    private Proyecto idproyecto;
    @JoinColumn(name = "iduser", referencedColumnName = "iduser")
    @ManyToOne(optional = false)
    private User iduser;

    public Participante() {
    }

    public Participante(Integer idparticipante) {
        this.idparticipante = idparticipante;
    }

    public Integer getIdparticipante() {
        return idparticipante;
    }

    public void setIdparticipante(Integer idparticipante) {
        this.idparticipante = idparticipante;
    }

    public Proyecto getIdproyecto() {
        return idproyecto;
    }

    public void setIdproyecto(Proyecto idproyecto) {
        this.idproyecto = idproyecto;
    }

    public User getIduser() {
        return iduser;
    }

    public void setIduser(User iduser) {
        this.iduser = iduser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idparticipante != null ? idparticipante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Participante)) {
            return false;
        }
        Participante other = (Participante) object;
        if ((this.idparticipante == null && other.idparticipante != null) || (this.idparticipante != null && !this.idparticipante.equals(other.idparticipante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.enfasis3.entity.Participante[ idparticipante=" + idparticipante + " ]";
    }
    
}
