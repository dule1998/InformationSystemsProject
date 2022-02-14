package entities;

import entities.Korisnik;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.0.v20170811-rNA", date="2021-07-11T13:27:25")
@StaticMetamodel(Dogadjaj.class)
public class Dogadjaj_ { 

    public static volatile SingularAttribute<Dogadjaj, Korisnik> idK;
    public static volatile SingularAttribute<Dogadjaj, Date> kraj;
    public static volatile SingularAttribute<Dogadjaj, Integer> idD;
    public static volatile SingularAttribute<Dogadjaj, String> destinacija;
    public static volatile SingularAttribute<Dogadjaj, Date> pocetak;

}