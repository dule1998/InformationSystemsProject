/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

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
 * @author dusan
 */
@Entity
@Table(name = "dogadjaj")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dogadjaj.findAll", query = "SELECT d FROM Dogadjaj d"),
    @NamedQuery(name = "Dogadjaj.findByIdD", query = "SELECT d FROM Dogadjaj d WHERE d.idD = :idD"),
    @NamedQuery(name = "Dogadjaj.findByPocetak", query = "SELECT d FROM Dogadjaj d WHERE d.pocetak = :pocetak"),
    @NamedQuery(name = "Dogadjaj.findByKraj", query = "SELECT d FROM Dogadjaj d WHERE d.kraj = :kraj"),
    @NamedQuery(name = "Dogadjaj.findByDestinacija", query = "SELECT d FROM Dogadjaj d WHERE d.destinacija = :destinacija")})
public class Dogadjaj implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdD")
    private Integer idD;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Pocetak")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pocetak;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Kraj")
    @Temporal(TemporalType.TIMESTAMP)
    private Date kraj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Destinacija")
    private String destinacija;
    @JoinColumn(name = "IdK", referencedColumnName = "IdK")
    @ManyToOne(optional = false)
    private Korisnik idK;

    public Dogadjaj() {
    }

    public Dogadjaj(Integer idD) {
        this.idD = idD;
    }

    public Dogadjaj(Integer idD, Date pocetak, Date kraj, String destinacija) {
        this.idD = idD;
        this.pocetak = pocetak;
        this.kraj = kraj;
        this.destinacija = destinacija;
    }

    public Integer getIdD() {
        return idD;
    }

    public void setIdD(Integer idD) {
        this.idD = idD;
    }

    public Date getPocetak() {
        return pocetak;
    }

    public void setPocetak(Date pocetak) {
        this.pocetak = pocetak;
    }

    public Date getKraj() {
        return kraj;
    }

    public void setKraj(Date kraj) {
        this.kraj = kraj;
    }

    public String getDestinacija() {
        return destinacija;
    }

    public void setDestinacija(String destinacija) {
        this.destinacija = destinacija;
    }

    public Korisnik getIdK() {
        return idK;
    }

    public void setIdK(Korisnik idK) {
        this.idK = idK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idD != null ? idD.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dogadjaj)) {
            return false;
        }
        Dogadjaj other = (Dogadjaj) object;
        if ((this.idD == null && other.idD != null) || (this.idD != null && !this.idD.equals(other.idD))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Dogadjaj[ idD=" + idD + " ]";
    }
    
}
