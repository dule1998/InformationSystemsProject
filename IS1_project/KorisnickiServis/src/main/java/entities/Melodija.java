/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author dusan
 */
@Entity
@Table(name = "melodija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Melodija.findAll", query = "SELECT m FROM Melodija m"),
    @NamedQuery(name = "Melodija.findByIdM", query = "SELECT m FROM Melodija m WHERE m.idM = :idM"),
    @NamedQuery(name = "Melodija.findByNaziv", query = "SELECT m FROM Melodija m WHERE m.naziv = :naziv"),
    @NamedQuery(name = "Melodija.findByLokacija", query = "SELECT m FROM Melodija m WHERE m.lokacija = :lokacija")})
public class Melodija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdM")
    private Integer idM;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Naziv")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Lokacija")
    private String lokacija;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idM")
    private List<AlarmNavijen> alarmNavijenList;

    public Melodija() {
    }

    public Melodija(Integer idM) {
        this.idM = idM;
    }

    public Melodija(Integer idM, String naziv, String lokacija) {
        this.idM = idM;
        this.naziv = naziv;
        this.lokacija = lokacija;
    }

    public Integer getIdM() {
        return idM;
    }

    public void setIdM(Integer idM) {
        this.idM = idM;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    @XmlTransient
    public List<AlarmNavijen> getAlarmNavijenList() {
        return alarmNavijenList;
    }

    public void setAlarmNavijenList(List<AlarmNavijen> alarmNavijenList) {
        this.alarmNavijenList = alarmNavijenList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idM != null ? idM.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Melodija)) {
            return false;
        }
        Melodija other = (Melodija) object;
        if ((this.idM == null && other.idM != null) || (this.idM != null && !this.idM.equals(other.idM))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Melodija[ idM=" + idM + " ]";
    }
    
}
