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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dusan
 */
@Entity
@Table(name = "alarm_navijen")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlarmNavijen.findAll", query = "SELECT a FROM AlarmNavijen a"),
    @NamedQuery(name = "AlarmNavijen.findByIdA", query = "SELECT a FROM AlarmNavijen a WHERE a.idA = :idA"),
    @NamedQuery(name = "AlarmNavijen.findByVreme", query = "SELECT a FROM AlarmNavijen a WHERE a.vreme = :vreme"),
    @NamedQuery(name = "AlarmNavijen.findByPeriod", query = "SELECT a FROM AlarmNavijen a WHERE a.period = :period")})
public class AlarmNavijen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdA")
    private Integer idA;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Vreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Period")
    private int period;
    @JoinColumn(name = "IdK", referencedColumnName = "IdK")
    @ManyToOne(optional = false)
    private Korisnik idK;
    @JoinColumn(name = "IdM", referencedColumnName = "IdM")
    @ManyToOne(optional = false)
    private Melodija idM;

    public AlarmNavijen() {
    }

    public AlarmNavijen(Integer idA) {
        this.idA = idA;
    }

    public AlarmNavijen(Integer idA, Date vreme, int period) {
        this.idA = idA;
        this.vreme = vreme;
        this.period = period;
    }

    public Integer getIdA() {
        return idA;
    }

    public void setIdA(Integer idA) {
        this.idA = idA;
    }

    public Date getVreme() {
        return vreme;
    }

    public void setVreme(Date vreme) {
        this.vreme = vreme;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public Korisnik getIdK() {
        return idK;
    }

    public void setIdK(Korisnik idK) {
        this.idK = idK;
    }

    public Melodija getIdM() {
        return idM;
    }

    public void setIdM(Melodija idM) {
        this.idM = idM;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idA != null ? idA.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlarmNavijen)) {
            return false;
        }
        AlarmNavijen other = (AlarmNavijen) object;
        if ((this.idA == null && other.idA != null) || (this.idA != null && !this.idA.equals(other.idA))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AlarmNavijen[ idA=" + idA + " ]";
    }
    
}
