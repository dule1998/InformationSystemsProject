/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

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
 * @author dusan
 */
@Entity
@Table(name = "reprodukcija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reprodukcija.findAll", query = "SELECT r FROM Reprodukcija r"),
    @NamedQuery(name = "Reprodukcija.findByIdR", query = "SELECT r FROM Reprodukcija r WHERE r.idR = :idR")})
public class Reprodukcija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdR")
    private Integer idR;
    @JoinColumn(name = "IdK", referencedColumnName = "IdK")
    @ManyToOne(optional = false)
    private Korisnik idK;
    @JoinColumn(name = "IdP", referencedColumnName = "IdP")
    @ManyToOne(optional = false)
    private Pesma idP;

    public Reprodukcija() {
    }

    public Reprodukcija(Integer idR) {
        this.idR = idR;
    }

    public Integer getIdR() {
        return idR;
    }

    public void setIdR(Integer idR) {
        this.idR = idR;
    }

    public Korisnik getIdK() {
        return idK;
    }

    public void setIdK(Korisnik idK) {
        this.idK = idK;
    }

    public Pesma getIdP() {
        return idP;
    }

    public void setIdP(Pesma idP) {
        this.idP = idP;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idR != null ? idR.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reprodukcija)) {
            return false;
        }
        Reprodukcija other = (Reprodukcija) object;
        if ((this.idR == null && other.idR != null) || (this.idR != null && !this.idR.equals(other.idR))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Reprodukcija[ idR=" + idR + " ]";
    }
    
}
