package entities;

import entities.AlarmNavijen;
import entities.Dogadjaj;
import entities.Reprodukcija;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.0.v20170811-rNA", date="2021-07-05T19:00:22")
@StaticMetamodel(Korisnik.class)
public class Korisnik_ { 

    public static volatile SingularAttribute<Korisnik, String> ime;
    public static volatile SingularAttribute<Korisnik, String> prezime;
    public static volatile ListAttribute<Korisnik, Dogadjaj> dogadjajList;
    public static volatile SingularAttribute<Korisnik, Integer> idK;
    public static volatile SingularAttribute<Korisnik, String> korIme;
    public static volatile ListAttribute<Korisnik, Reprodukcija> reprodukcijaList;
    public static volatile SingularAttribute<Korisnik, String> sifra;
    public static volatile ListAttribute<Korisnik, AlarmNavijen> alarmNavijenList;

}