package entities;

import entities.Korisnik;
import entities.Melodija;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.0.v20170811-rNA", date="2021-07-10T18:45:16")
@StaticMetamodel(AlarmNavijen.class)
public class AlarmNavijen_ { 

    public static volatile SingularAttribute<AlarmNavijen, Korisnik> idK;
    public static volatile SingularAttribute<AlarmNavijen, Integer> period;
    public static volatile SingularAttribute<AlarmNavijen, Melodija> idM;
    public static volatile SingularAttribute<AlarmNavijen, Date> vreme;
    public static volatile SingularAttribute<AlarmNavijen, Integer> idA;

}